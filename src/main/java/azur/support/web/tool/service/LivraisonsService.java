package azur.support.web.tool.service;

import azur.support.web.tool.domain.Livraisons;
import azur.support.web.tool.repository.LivraisonsRepository;
import azur.support.web.tool.repository.search.LivraisonsSearchRepository;
import azur.support.web.tool.service.dto.LivraisonsDTO;
import azur.support.web.tool.service.mapper.LivraisonsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing {@link Livraisons}.
 */
@Service
@Transactional
public class LivraisonsService {

    private final Logger log = LoggerFactory.getLogger(LivraisonsService.class);

    private final LivraisonsRepository livraisonsRepository;

    private final LivraisonsMapper livraisonsMapper;

    private final LivraisonsSearchRepository livraisonsSearchRepository;

    public LivraisonsService(LivraisonsRepository livraisonsRepository, LivraisonsMapper livraisonsMapper, LivraisonsSearchRepository livraisonsSearchRepository) {
        this.livraisonsRepository = livraisonsRepository;
        this.livraisonsMapper = livraisonsMapper;
        this.livraisonsSearchRepository = livraisonsSearchRepository;
    }

    /**
     * Save a livraisons.
     *
     * @param livraisonsDTO the entity to save.
     * @return the persisted entity.
     */
    public LivraisonsDTO save(LivraisonsDTO livraisonsDTO) {
        log.debug("Request to save Livraisons : {}", livraisonsDTO);
        Livraisons livraisons = livraisonsMapper.toEntity(livraisonsDTO);
        livraisons = livraisonsRepository.save(livraisons);
        LivraisonsDTO result = livraisonsMapper.toDto(livraisons);
        livraisonsSearchRepository.save(livraisons);
        return result;
    }

    /**
     * Get all the livraisons.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LivraisonsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Livraisons");
        return livraisonsRepository.findAll(pageable)
            .map(livraisonsMapper::toDto);
    }


    /**
     * Get one livraisons by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LivraisonsDTO> findOne(Long id) {
        log.debug("Request to get Livraisons : {}", id);
        return livraisonsRepository.findById(id)
            .map(livraisonsMapper::toDto);
    }

    /**
     * Delete the livraisons by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Livraisons : {}", id);
        livraisonsRepository.deleteById(id);
        livraisonsSearchRepository.deleteById(id);
    }

    /**
     * Search for the livraisons corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LivraisonsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Livraisons for query {}", query);
        return livraisonsSearchRepository.search(queryStringQuery(query), pageable)
            .map(livraisonsMapper::toDto);
    }
}
