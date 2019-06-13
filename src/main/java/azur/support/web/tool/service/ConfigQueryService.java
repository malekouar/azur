package azur.support.web.tool.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import azur.support.web.tool.domain.Config;
import azur.support.web.tool.domain.*; // for static metamodels
import azur.support.web.tool.repository.ConfigRepository;
import azur.support.web.tool.repository.search.ConfigSearchRepository;
import azur.support.web.tool.service.dto.ConfigCriteria;
import azur.support.web.tool.service.dto.ConfigDTO;
import azur.support.web.tool.service.mapper.ConfigMapper;

/**
 * Service for executing complex queries for {@link Config} entities in the database.
 * The main input is a {@link ConfigCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ConfigDTO} or a {@link Page} of {@link ConfigDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ConfigQueryService extends QueryService<Config> {

    private final Logger log = LoggerFactory.getLogger(ConfigQueryService.class);

    private final ConfigRepository configRepository;

    private final ConfigMapper configMapper;

    private final ConfigSearchRepository configSearchRepository;

    public ConfigQueryService(ConfigRepository configRepository, ConfigMapper configMapper, ConfigSearchRepository configSearchRepository) {
        this.configRepository = configRepository;
        this.configMapper = configMapper;
        this.configSearchRepository = configSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ConfigDTO> findByCriteria(ConfigCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Config> specification = createSpecification(criteria);
        return configMapper.toDto(configRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ConfigDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfigDTO> findByCriteria(ConfigCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Config> specification = createSpecification(criteria);
        return configRepository.findAll(specification, page)
            .map(configMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ConfigCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Config> specification = createSpecification(criteria);
        return configRepository.count(specification);
    }

    /**
     * Function to convert ConfigCriteria to a {@link Specification}.
     */
    private Specification<Config> createSpecification(ConfigCriteria criteria) {
        Specification<Config> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Config_.id));
            }
            if (criteria.getTeamviewerId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTeamviewerId(), Config_.teamviewerId));
            }
            if (criteria.getTeamviewerPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTeamviewerPassword(), Config_.teamviewerPassword));
            }
            if (criteria.getVpnType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVpnType(), Config_.vpnType));
            }
            if (criteria.getVpnIp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVpnIp(), Config_.vpnIp));
            }
            if (criteria.getVpnLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVpnLogin(), Config_.vpnLogin));
            }
            if (criteria.getVpnPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVpnPassword(), Config_.vpnPassword));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildSpecification(criteria.getClientId(),
                    root -> root.join(Config_.client, JoinType.LEFT).get(Client_.id)));
            }
            if (criteria.getServeurId() != null) {
                specification = specification.and(buildSpecification(criteria.getServeurId(),
                    root -> root.join(Config_.serveurs, JoinType.LEFT).get(Serveur_.id)));
            }
        }
        return specification;
    }
}
