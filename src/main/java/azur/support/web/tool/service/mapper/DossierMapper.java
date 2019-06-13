package azur.support.web.tool.service.mapper;

import azur.support.web.tool.domain.*;
import azur.support.web.tool.service.dto.DossierDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Dossier} and its DTO {@link DossierDTO}.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class})
public interface DossierMapper extends EntityMapper<DossierDTO, Dossier> {

    @Mapping(source = "client.id", target = "clientId")
    DossierDTO toDto(Dossier dossier);

    @Mapping(source = "clientId", target = "client")
    @Mapping(target = "interventions", ignore = true)
    @Mapping(target = "removeIntervention", ignore = true)
    Dossier toEntity(DossierDTO dossierDTO);

    default Dossier fromId(Long id) {
        if (id == null) {
            return null;
        }
        Dossier dossier = new Dossier();
        dossier.setId(id);
        return dossier;
    }
}
