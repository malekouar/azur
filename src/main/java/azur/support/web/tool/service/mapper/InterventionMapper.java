package azur.support.web.tool.service.mapper;

import azur.support.web.tool.domain.*;
import azur.support.web.tool.service.dto.InterventionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Intervention} and its DTO {@link InterventionDTO}.
 */
@Mapper(componentModel = "spring", uses = {DossierMapper.class})
public interface InterventionMapper extends EntityMapper<InterventionDTO, Intervention> {

    @Mapping(source = "dossier.id", target = "dossierId")
    InterventionDTO toDto(Intervention intervention);

    @Mapping(source = "dossierId", target = "dossier")
    @Mapping(target = "livraisons", ignore = true)
    @Mapping(target = "removeLivraisons", ignore = true)
    Intervention toEntity(InterventionDTO interventionDTO);

    default Intervention fromId(Long id) {
        if (id == null) {
            return null;
        }
        Intervention intervention = new Intervention();
        intervention.setId(id);
        return intervention;
    }
}
