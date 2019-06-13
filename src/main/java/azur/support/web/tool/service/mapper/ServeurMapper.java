package azur.support.web.tool.service.mapper;

import azur.support.web.tool.domain.*;
import azur.support.web.tool.service.dto.ServeurDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Serveur} and its DTO {@link ServeurDTO}.
 */
@Mapper(componentModel = "spring", uses = {ConfigMapper.class})
public interface ServeurMapper extends EntityMapper<ServeurDTO, Serveur> {

    @Mapping(source = "config.id", target = "configId")
    ServeurDTO toDto(Serveur serveur);

    @Mapping(source = "configId", target = "config")
    Serveur toEntity(ServeurDTO serveurDTO);

    default Serveur fromId(Long id) {
        if (id == null) {
            return null;
        }
        Serveur serveur = new Serveur();
        serveur.setId(id);
        return serveur;
    }
}
