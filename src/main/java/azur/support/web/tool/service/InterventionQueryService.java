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

import azur.support.web.tool.domain.Intervention;
import azur.support.web.tool.domain.*; // for static metamodels
import azur.support.web.tool.repository.InterventionRepository;
import azur.support.web.tool.repository.search.InterventionSearchRepository;
import azur.support.web.tool.service.dto.InterventionCriteria;
import azur.support.web.tool.service.dto.InterventionDTO;
import azur.support.web.tool.service.mapper.InterventionMapper;

/**
 * Service for executing complex queries for {@link Intervention} entities in the database.
 * The main input is a {@link InterventionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InterventionDTO} or a {@link Page} of {@link InterventionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InterventionQueryService extends QueryService<Intervention> {

    private final Logger log = LoggerFactory.getLogger(InterventionQueryService.class);

    private final InterventionRepository interventionRepository;

    private final InterventionMapper interventionMapper;

    private final InterventionSearchRepository interventionSearchRepository;

    public InterventionQueryService(InterventionRepository interventionRepository, InterventionMapper interventionMapper, InterventionSearchRepository interventionSearchRepository) {
        this.interventionRepository = interventionRepository;
        this.interventionMapper = interventionMapper;
        this.interventionSearchRepository = interventionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InterventionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InterventionDTO> findByCriteria(InterventionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Intervention> specification = createSpecification(criteria);
        return interventionMapper.toDto(interventionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InterventionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InterventionDTO> findByCriteria(InterventionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Intervention> specification = createSpecification(criteria);
        return interventionRepository.findAll(specification, page)
            .map(interventionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InterventionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Intervention> specification = createSpecification(criteria);
        return interventionRepository.count(specification);
    }

    /**
     * Function to convert InterventionCriteria to a {@link Specification}.
     */
    private Specification<Intervention> createSpecification(InterventionCriteria criteria) {
        Specification<Intervention> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Intervention_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Intervention_.type));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Intervention_.dateDebut));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), Intervention_.dateFin));
            }
            if (criteria.getResponsable() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResponsable(), Intervention_.responsable));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEtat(), Intervention_.etat));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Intervention_.description));
            }
            if (criteria.getDetail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetail(), Intervention_.detail));
            }
            if (criteria.getDossierId() != null) {
                specification = specification.and(buildSpecification(criteria.getDossierId(),
                    root -> root.join(Intervention_.dossier, JoinType.LEFT).get(Dossier_.id)));
            }
            if (criteria.getLivraisonsId() != null) {
                specification = specification.and(buildSpecification(criteria.getLivraisonsId(),
                    root -> root.join(Intervention_.livraisons, JoinType.LEFT).get(Livraisons_.id)));
            }
        }
        return specification;
    }
}
