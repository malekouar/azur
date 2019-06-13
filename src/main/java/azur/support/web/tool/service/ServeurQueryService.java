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

import azur.support.web.tool.domain.Serveur;
import azur.support.web.tool.domain.*; // for static metamodels
import azur.support.web.tool.repository.ServeurRepository;
import azur.support.web.tool.repository.search.ServeurSearchRepository;
import azur.support.web.tool.service.dto.ServeurCriteria;
import azur.support.web.tool.service.dto.ServeurDTO;
import azur.support.web.tool.service.mapper.ServeurMapper;

/**
 * Service for executing complex queries for {@link Serveur} entities in the database.
 * The main input is a {@link ServeurCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ServeurDTO} or a {@link Page} of {@link ServeurDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServeurQueryService extends QueryService<Serveur> {

    private final Logger log = LoggerFactory.getLogger(ServeurQueryService.class);

    private final ServeurRepository serveurRepository;

    private final ServeurMapper serveurMapper;

    private final ServeurSearchRepository serveurSearchRepository;

    public ServeurQueryService(ServeurRepository serveurRepository, ServeurMapper serveurMapper, ServeurSearchRepository serveurSearchRepository) {
        this.serveurRepository = serveurRepository;
        this.serveurMapper = serveurMapper;
        this.serveurSearchRepository = serveurSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ServeurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ServeurDTO> findByCriteria(ServeurCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Serveur> specification = createSpecification(criteria);
        return serveurMapper.toDto(serveurRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ServeurDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServeurDTO> findByCriteria(ServeurCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Serveur> specification = createSpecification(criteria);
        return serveurRepository.findAll(specification, page)
            .map(serveurMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServeurCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Serveur> specification = createSpecification(criteria);
        return serveurRepository.count(specification);
    }

    /**
     * Function to convert ServeurCriteria to a {@link Specification}.
     */
    private Specification<Serveur> createSpecification(ServeurCriteria criteria) {
        Specification<Serveur> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Serveur_.id));
            }
            if (criteria.getServeurType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServeurType(), Serveur_.serveurType));
            }
            if (criteria.getServeurNom() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServeurNom(), Serveur_.serveurNom));
            }
            if (criteria.getServeurIp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getServeurIp(), Serveur_.serveurIp));
            }
            if (criteria.getLogin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLogin(), Serveur_.login));
            }
            if (criteria.getPassword() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPassword(), Serveur_.password));
            }
            if (criteria.getConfigId() != null) {
                specification = specification.and(buildSpecification(criteria.getConfigId(),
                    root -> root.join(Serveur_.config, JoinType.LEFT).get(Config_.id)));
            }
        }
        return specification;
    }
}
