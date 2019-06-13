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

import azur.support.web.tool.domain.Livraisons;
import azur.support.web.tool.domain.*; // for static metamodels
import azur.support.web.tool.repository.LivraisonsRepository;
import azur.support.web.tool.repository.search.LivraisonsSearchRepository;
import azur.support.web.tool.service.dto.LivraisonsCriteria;
import azur.support.web.tool.service.dto.LivraisonsDTO;
import azur.support.web.tool.service.mapper.LivraisonsMapper;

/**
 * Service for executing complex queries for {@link Livraisons} entities in the database.
 * The main input is a {@link LivraisonsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LivraisonsDTO} or a {@link Page} of {@link LivraisonsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LivraisonsQueryService extends QueryService<Livraisons> {

    private final Logger log = LoggerFactory.getLogger(LivraisonsQueryService.class);

    private final LivraisonsRepository livraisonsRepository;

    private final LivraisonsMapper livraisonsMapper;

    private final LivraisonsSearchRepository livraisonsSearchRepository;

    public LivraisonsQueryService(LivraisonsRepository livraisonsRepository, LivraisonsMapper livraisonsMapper, LivraisonsSearchRepository livraisonsSearchRepository) {
        this.livraisonsRepository = livraisonsRepository;
        this.livraisonsMapper = livraisonsMapper;
        this.livraisonsSearchRepository = livraisonsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LivraisonsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LivraisonsDTO> findByCriteria(LivraisonsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Livraisons> specification = createSpecification(criteria);
        return livraisonsMapper.toDto(livraisonsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LivraisonsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LivraisonsDTO> findByCriteria(LivraisonsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Livraisons> specification = createSpecification(criteria);
        return livraisonsRepository.findAll(specification, page)
            .map(livraisonsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LivraisonsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Livraisons> specification = createSpecification(criteria);
        return livraisonsRepository.count(specification);
    }

    /**
     * Function to convert LivraisonsCriteria to a {@link Specification}.
     */
    private Specification<Livraisons> createSpecification(LivraisonsCriteria criteria) {
        Specification<Livraisons> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Livraisons_.id));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Livraisons_.type));
            }
            if (criteria.getDateLivraison() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateLivraison(), Livraisons_.dateLivraison));
            }
            if (criteria.getResponsable() != null) {
                specification = specification.and(buildStringSpecification(criteria.getResponsable(), Livraisons_.responsable));
            }
            if (criteria.getEtat() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEtat(), Livraisons_.etat));
            }
            if (criteria.getNomPackage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNomPackage(), Livraisons_.nomPackage));
            }
            if (criteria.getIdSvn() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIdSvn(), Livraisons_.idSvn));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Livraisons_.description));
            }
            if (criteria.getDetail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDetail(), Livraisons_.detail));
            }
            if (criteria.getInterventionId() != null) {
                specification = specification.and(buildSpecification(criteria.getInterventionId(),
                    root -> root.join(Livraisons_.intervention, JoinType.LEFT).get(Intervention_.id)));
            }
        }
        return specification;
    }
}
