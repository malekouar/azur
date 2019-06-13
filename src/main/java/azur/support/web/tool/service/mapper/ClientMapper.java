package azur.support.web.tool.service.mapper;

import azur.support.web.tool.domain.*;
import azur.support.web.tool.service.dto.ClientDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {


    @Mapping(target = "configs", ignore = true)
    @Mapping(target = "removeConfig", ignore = true)
    @Mapping(target = "dossiers", ignore = true)
    @Mapping(target = "removeDossier", ignore = true)
    Client toEntity(ClientDTO clientDTO);

    default Client fromId(Long id) {
        if (id == null) {
            return null;
        }
        Client client = new Client();
        client.setId(id);
        return client;
    }
}
