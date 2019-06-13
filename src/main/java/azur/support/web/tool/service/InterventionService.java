package azur.support.web.tool.service;

import azur.support.web.tool.domain.Intervention;
import azur.support.web.tool.repository.InterventionRepository;
import azur.support.web.tool.repository.search.InterventionSearchRepository;
import azur.support.web.tool.service.dto.InterventionDTO;
import azur.support.web.tool.service.mapper.InterventionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Intervention}.
 */
@Service
@Transactional
public class InterventionService {

    private final Logger log = LoggerFactory.getLogger(InterventionService.class);

    private final InterventionRepository interventionRepository;

    private final InterventionMapper interventionMapper;

    private final InterventionSearchRepository interventionSearchRepository;

    public InterventionService(InterventionRepository interventionRepository, InterventionMapper interventionMapper, InterventionSearchRepository interventionSearchRepository) {
        this.interventionRepository = interventionRepository;
        this.interventionMapper = interventionMapper;
        this.interventionSearchRepository = interventionSearchRepository;
    }

    /**
     * Save a intervention.
     *
     * @param interventionDTO the entity to save.
     * @return the persisted entity.
     */
    public InterventionDTO save(InterventionDTO interventionDTO) {
        log.debug("Request to save Intervention : {}", interventionDTO);
        Intervention intervention = interventionMapper.toEntity(interventionDTO);
        intervention = interventionRepository.save(intervention);
        InterventionDTO result = interventionMapper.toDto(intervention);
        interventionSearchRepository.save(intervention);
        return result;
    }

    /**
     * Get all the interventions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InterventionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Interventions");
        return interventionRepository.findAll(pageable)
            .map(interventionMapper::toDto);
    }


    /**
     * Get one intervention by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InterventionDTO> findOne(Long id) {
        log.debug("Request to get Intervention : {}", id);
        return interventionRepository.findById(id)
            .map(interventionMapper::toDto);
    }

    /**
     * Delete the intervention by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Intervention : {}", id);
        interventionRepository.deleteById(id);
        interventionSearchRepository.deleteById(id);
    }

    /**
     * Search for the intervention corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<InterventionDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Interventions for query {}", query);
        return interventionSearchRepository.search(queryStringQuery(query), pageable)
            .map(interventionMapper::toDto);
    }
}
