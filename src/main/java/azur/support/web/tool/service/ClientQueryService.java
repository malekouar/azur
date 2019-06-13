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

import azur.support.web.tool.domain.Client;
import azur.support.web.tool.domain.*; // for static metamodels
import azur.support.web.tool.repository.ClientRepository;
import azur.support.web.tool.repository.search.ClientSearchRepository;
import azur.support.web.tool.service.dto.ClientCriteria;
import azur.support.web.tool.service.dto.ClientDTO;
import azur.support.web.tool.service.mapper.ClientMapper;

/**
 * Service for executing complex queries for {@link Client} entities in the database.
 * The main input is a {@link ClientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ClientDTO} or a {@link Page} of {@link ClientDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ClientQueryService extends QueryService<Client> {

    private final Logger log = LoggerFactory.getLogger(ClientQueryService.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;

    private final ClientSearchRepository clientSearchRepository;

    public ClientQueryService(ClientRepository clientRepository, ClientMapper clientMapper, ClientSearchRepository clientSearchRepository) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.clientSearchRepository = clientSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ClientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClientDTO> findByCriteria(ClientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Client> specification = createSpecification(criteria);
        return clientMapper.toDto(clientRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ClientDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClientDTO> findByCriteria(ClientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Client> specification = createSpecification(criteria);
        return clientRepository.findAll(specification, page)
            .map(clientMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ClientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Client> specification = createSpecification(criteria);
        return clientRepository.count(specification);
    }

    /**
     * Function to convert ClientCriteria to a {@link Specification}.
     */
    private Specification<Client> createSpecification(ClientCriteria criteria) {
        Specification<Client> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Client_.id));
            }
            if (criteria.getRaisonSociale() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRaisonSociale(), Client_.raisonSociale));
            }
            if (criteria.getContact() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContact(), Client_.contact));
            }
            if (criteria.getTel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTel(), Client_.tel));
            }
            if (criteria.getMobile() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMobile(), Client_.mobile));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Client_.email));
            }
            if (criteria.getConfigId() != null) {
                specification = specification.and(buildSpecification(criteria.getConfigId(),
                    root -> root.join(Client_.configs, JoinType.LEFT).get(Config_.id)));
            }
            if (criteria.getDossierId() != null) {
                specification = specification.and(buildSpecification(criteria.getDossierId(),
                    root -> root.join(Client_.dossiers, JoinType.LEFT).get(Dossier_.id)));
            }
        }
        return specification;
    }
}
