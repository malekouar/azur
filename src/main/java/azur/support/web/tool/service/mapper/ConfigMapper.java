package azur.support.web.tool.service.mapper;

import azur.support.web.tool.domain.*;
import azur.support.web.tool.service.dto.ConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Config} and its DTO {@link ConfigDTO}.
 */
@Mapper(componentModel = "spring", uses = {ClientMapper.class})
public interface ConfigMapper extends EntityMapper<ConfigDTO, Config> {

    @Mapping(source = "client.id", target = "clientId")
    ConfigDTO toDto(Config config);

    @Mapping(source = "clientId", target = "client")
    @Mapping(target = "serveurs", ignore = true)
    @Mapping(target = "removeServeur", ignore = true)
    Config toEntity(ConfigDTO configDTO);

    default Config fromId(Long id) {
        if (id == null) {
            return null;
        }
        Config config = new Config();
        config.setId(id);
        return config;
    }
}
