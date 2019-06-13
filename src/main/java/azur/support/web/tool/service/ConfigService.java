package azur.support.web.tool.service;

import azur.support.web.tool.domain.Config;
import azur.support.web.tool.repository.ConfigRepository;
import azur.support.web.tool.repository.search.ConfigSearchRepository;
import azur.support.web.tool.service.dto.ConfigDTO;
import azur.support.web.tool.service.mapper.ConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Config}.
 */
@Service
@Transactional
public class ConfigService {

    private final Logger log = LoggerFactory.getLogger(ConfigService.class);

    private final ConfigRepository configRepository;

    private final ConfigMapper configMapper;

    private final ConfigSearchRepository configSearchRepository;

    public ConfigService(ConfigRepository configRepository, ConfigMapper configMapper, ConfigSearchRepository configSearchRepository) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
        this.configSearchRepository = configSearchRepository;
    }

    /**
     * Save a config.
     *
     * @param configDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfigDTO save(ConfigDTO configDTO) {
        log.debug("Request to save Config : {}", configDTO);
        Config config = configMapper.toEntity(configDTO);
        config = configRepository.save(config);
        ConfigDTO result = configMapper.toDto(config);
        configSearchRepository.save(config);
        return result;
    }

    /**
     * Get all the configs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Configs");
        return configRepository.findAll(pageable)
            .map(configMapper::toDto);
    }


    /**
     * Get one config by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConfigDTO> findOne(Long id) {
        log.debug("Request to get Config : {}", id);
        return configRepository.findById(id)
            .map(configMapper::toDto);
    }

    /**
     * Delete the config by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Config : {}", id);
        configRepository.deleteById(id);
        configSearchRepository.deleteById(id);
    }

    /**
     * Search for the config corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Configs for query {}", query);
        return configSearchRepository.search(queryStringQuery(query), pageable)
            .map(configMapper::toDto);
    }
}
