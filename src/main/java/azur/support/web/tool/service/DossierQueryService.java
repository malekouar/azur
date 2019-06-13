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

import azur.support.web.tool.domain.Dossier;
import azur.support.web.tool.domain.*; // for static metamodels
import azur.support.web.tool.repository.DossierRepository;
import azur.support.web.tool.repository.search.DossierSearchRepository;
import azur.support.web.tool.service.dto.DossierCriteria;
import azur.support.web.tool.service.dto.DossierDTO;
import azur.support.web.tool.service.mapper.DossierMapper;

/**
 * Service for executing complex queries for {@link Dossier} entities in the database.
 * The main input is a {@link DossierCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DossierDTO} or a {@link Page} of {@link DossierDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DossierQueryService extends QueryService<Dossier> {

    private final Logger log = LoggerFactory.getLogger(DossierQueryService.class);

    private final DossierRepository dossierRepository;

    private final DossierMapper dossierMapper;

    private final DossierSearchRepository dossierSearchRepository;

    public DossierQueryService(DossierRepository dossierRepository, DossierMapper dossierMapper, DossierSearchRepository dossierSearchRepository) {
        this.dossierRepository = dossierRepository;
        this.dossierMapper = dossierMapper;
        this.dossierSearchRepository = dossierSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DossierDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DossierDTO> findByCriteria(DossierCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Dossier> specification = createSpecification(criteria);
        return dossierMapper.toDto(dossierRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DossierDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DossierDTO> findByCriteria(DossierCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dossier> specification = createSpecification(criteria);
        return dossierRepository.findAll(specification, page)
            .map(dossierMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DossierCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Dossier> specification = createSpecification(criteria);
        return dossierRepository.count(specification);
    }

    /**
     * Function to convert DossierCriteria to a {@link Specification}.
     */
    private Specification<Dossier> createSpecification(DossierCriteria criteria) {
        Specification<Dossier> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Dossier_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Dossier_.type));
            }
            if (criteria.getDateDebut() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateDebut(), Dossier_.dateDebut));
            }
            if (criteria.getResponsable() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResponsable(), Dossier_.responsable));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEtat(), Dossier_.etat));
            }
            if (criteria.getUrlAzimut() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlAzimut(), Dossier_.urlAzimut));
            }
            if (criteria.getUrlRedmine() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlRedmine(), Dossier_.urlRedmine));
            }
            if (criteria.getUrlAkuiteo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrlAkuiteo(), Dossier_.urlAkuiteo));
            }
            if (criteria.getDateFin() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateFin(), Dossier_.dateFin));
            }
            if (criteria.getClientId() != null) {
                specification = specification.and(buildSpecification(criteria.getClientId(),
                    root -> root.join(Dossier_.client, JoinType.LEFT).get(Client_.id)));
            }
            if (criteria.getInterventionId() != null) {
                specification = specification.and(buildSpecification(criteria.getInterventionId(),
                    root -> root.join(Dossier_.interventions, JoinType.LEFT).get(Intervention_.id)));
            }
        }
        return specification;
    }
}
