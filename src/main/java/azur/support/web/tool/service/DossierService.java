package azur.support.web.tool.service;

import azur.support.web.tool.domain.Dossier;
import azur.support.web.tool.repository.DossierRepository;
import azur.support.web.tool.repository.search.DossierSearchRepository;
import azur.support.web.tool.service.dto.DossierDTO;
import azur.support.web.tool.service.mapper.DossierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Dossier}.
 */
@Service
@Transactional
public class DossierService {

    private final Logger log = LoggerFactory.getLogger(DossierService.class);

    private final DossierRepository dossierRepository;

    private final DossierMapper dossierMapper;

    private final DossierSearchRepository dossierSearchRepository;

    public DossierService(DossierRepository dossierRepository, DossierMapper dossierMapper, DossierSearchRepository dossierSearchRepository) {
        this.dossierRepository = dossierRepository;
        this.dossierMapper = dossierMapper;
        this.dossierSearchRepository = dossierSearchRepository;
    }

    /**
     * Save a dossier.
     *
     * @param dossierDTO the entity to save.
     * @return the persisted entity.
     */
    public DossierDTO save(DossierDTO dossierDTO) {
        log.debug("Request to save Dossier : {}", dossierDTO);
        Dossier dossier = dossierMapper.toEntity(dossierDTO);
        dossier = dossierRepository.save(dossier);
        DossierDTO result = dossierMapper.toDto(dossier);
        dossierSearchRepository.save(dossier);
        return result;
    }

    /**
     * Get all the dossiers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DossierDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Dossiers");
        return dossierRepository.findAll(pageable)
            .map(dossierMapper::toDto);
    }


    /**
     * Get one dossier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DossierDTO> findOne(Long id) {
        log.debug("Request to get Dossier : {}", id);
        return dossierRepository.findById(id)
            .map(dossierMapper::toDto);
    }

    /**
     * Delete the dossier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Dossier : {}", id);
        dossierRepository.deleteById(id);
        dossierSearchRepository.deleteById(id);
    }

    /**
     * Search for the dossier corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DossierDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Dossiers for query {}", query);
        return dossierSearchRepository.search(queryStringQuery(query), pageable)
            .map(dossierMapper::toDto);
    }
}
