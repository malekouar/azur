package azur.support.web.tool.web.rest;

import azur.support.web.tool.service.LivraisonsService;
import azur.support.web.tool.web.rest.errors.BadRequestAlertException;
import azur.support.web.tool.service.dto.LivraisonsDTO;
import azur.support.web.tool.service.dto.LivraisonsCriteria;
import azur.support.web.tool.service.LivraisonsQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link azur.support.web.tool.domain.Livraisons}.
 */
@RestController
@RequestMapping("/api")
public class LivraisonsResource {

    private final Logger log = LoggerFactory.getLogger(LivraisonsResource.class);

    private static final String ENTITY_NAME = "livraisons";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LivraisonsService livraisonsService;

    private final LivraisonsQueryService livraisonsQueryService;

    public LivraisonsResource(LivraisonsService livraisonsService, LivraisonsQueryService livraisonsQueryService) {
        this.livraisonsService = livraisonsService;
        this.livraisonsQueryService = livraisonsQueryService;
    }

    /**
     * {@code POST  /livraisons} : Create a new livraisons.
     *
     * @param livraisonsDTO the livraisonsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new livraisonsDTO, or with status {@code 400 (Bad Request)} if the livraisons has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/livraisons")
    public ResponseEntity<LivraisonsDTO> createLivraisons(@Valid @RequestBody LivraisonsDTO livraisonsDTO) throws URISyntaxException {
        log.debug("REST request to save Livraisons : {}", livraisonsDTO);
        if (livraisonsDTO.getId() != null) {
            throw new BadRequestAlertException("A new livraisons cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LivraisonsDTO result = livraisonsService.save(livraisonsDTO);
        return ResponseEntity.created(new URI("/api/livraisons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /livraisons} : Updates an existing livraisons.
     *
     * @param livraisonsDTO the livraisonsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated livraisonsDTO,
     * or with status {@code 400 (Bad Request)} if the livraisonsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the livraisonsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/livraisons")
    public ResponseEntity<LivraisonsDTO> updateLivraisons(@Valid @RequestBody LivraisonsDTO livraisonsDTO) throws URISyntaxException {
        log.debug("REST request to update Livraisons : {}", livraisonsDTO);
        if (livraisonsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        LivraisonsDTO result = livraisonsService.save(livraisonsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, livraisonsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /livraisons} : get all the livraisons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of livraisons in body.
     */
    @GetMapping("/livraisons")
    public ResponseEntity<List<LivraisonsDTO>> getAllLivraisons(LivraisonsCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Livraisons by criteria: {}", criteria);
        Page<LivraisonsDTO> page = livraisonsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /livraisons/count} : count all the livraisons.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/livraisons/count")
    public ResponseEntity<Long> countLivraisons(LivraisonsCriteria criteria) {
        log.debug("REST request to count Livraisons by criteria: {}", criteria);
        return ResponseEntity.ok().body(livraisonsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /livraisons/:id} : get the "id" livraisons.
     *
     * @param id the id of the livraisonsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the livraisonsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/livraisons/{id}")
    public ResponseEntity<LivraisonsDTO> getLivraisons(@PathVariable Long id) {
        log.debug("REST request to get Livraisons : {}", id);
        Optional<LivraisonsDTO> livraisonsDTO = livraisonsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(livraisonsDTO);
    }

    /**
     * {@code DELETE  /livraisons/:id} : delete the "id" livraisons.
     *
     * @param id the id of the livraisonsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/livraisons/{id}")
    public ResponseEntity<Void> deleteLivraisons(@PathVariable Long id) {
        log.debug("REST request to delete Livraisons : {}", id);
        livraisonsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/livraisons?query=:query} : search for the livraisons corresponding
     * to the query.
     *
     * @param query the query of the livraisons search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/livraisons")
    public ResponseEntity<List<LivraisonsDTO>> searchLivraisons(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Livraisons for query {}", query);
        Page<LivraisonsDTO> page = livraisonsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
