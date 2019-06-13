package azur.support.web.tool.service.mapper;

import azur.support.web.tool.domain.*;
import azur.support.web.tool.service.dto.LivraisonsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Livraisons} and its DTO {@link LivraisonsDTO}.
 */
@Mapper(componentModel = "spring", uses = {InterventionMapper.class})
public interface LivraisonsMapper extends EntityMapper<LivraisonsDTO, Livraisons> {

    @Mapping(source = "intervention.id", target = "interventionId")
    LivraisonsDTO toDto(Livraisons livraisons);

    @Mapping(source = "interventionId", target = "intervention")
    Livraisons toEntity(LivraisonsDTO livraisonsDTO);

    default Livraisons fromId(Long id) {
        if (id == null) {
            return null;
        }
        Livraisons livraisons = new Livraisons();
        livraisons.setId(id);
        return livraisons;
    }
}
