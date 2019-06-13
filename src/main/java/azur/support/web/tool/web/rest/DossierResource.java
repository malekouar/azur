package azur.support.web.tool.web.rest;

import azur.support.web.tool.service.DossierService;
import azur.support.web.tool.web.rest.errors.BadRequestAlertException;
import azur.support.web.tool.service.dto.DossierDTO;
import azur.support.web.tool.service.dto.DossierCriteria;
import azur.support.web.tool.service.DossierQueryService;

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
 * REST controller for managing {@link azur.support.web.tool.domain.Dossier}.
 */
@RestController
@RequestMapping("/api")
public class DossierResource {

    private final Logger log = LoggerFactory.getLogger(DossierResource.class);

    private static final String ENTITY_NAME = "dossier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DossierService dossierService;

    private final DossierQueryService dossierQueryService;

    public DossierResource(DossierService dossierService, DossierQueryService dossierQueryService) {
        this.dossierService = dossierService;
        this.dossierQueryService = dossierQueryService;
    }

    /**
     * {@code POST  /dossiers} : Create a new dossier.
     *
     * @param dossierDTO the dossierDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dossierDTO, or with status {@code 400 (Bad Request)} if the dossier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/dossiers")
    public ResponseEntity<DossierDTO> createDossier(@Valid @RequestBody DossierDTO dossierDTO) throws URISyntaxException {
        log.debug("REST request to save Dossier : {}", dossierDTO);
        if (dossierDTO.getId() != null) {
            throw new BadRequestAlertException("A new dossier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DossierDTO result = dossierService.save(dossierDTO);
        return ResponseEntity.created(new URI("/api/dossiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /dossiers} : Updates an existing dossier.
     *
     * @param dossierDTO the dossierDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dossierDTO,
     * or with status {@code 400 (Bad Request)} if the dossierDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dossierDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/dossiers")
    public ResponseEntity<DossierDTO> updateDossier(@Valid @RequestBody DossierDTO dossierDTO) throws URISyntaxException {
        log.debug("REST request to update Dossier : {}", dossierDTO);
        if (dossierDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DossierDTO result = dossierService.save(dossierDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dossierDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /dossiers} : get all the dossiers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dossiers in body.
     */
    @GetMapping("/dossiers")
    public ResponseEntity<List<DossierDTO>> getAllDossiers(DossierCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Dossiers by criteria: {}", criteria);
        Page<DossierDTO> page = dossierQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /dossiers/count} : count all the dossiers.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/dossiers/count")
    public ResponseEntity<Long> countDossiers(DossierCriteria criteria) {
        log.debug("REST request to count Dossiers by criteria: {}", criteria);
        return ResponseEntity.ok().body(dossierQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /dossiers/:id} : get the "id" dossier.
     *
     * @param id the id of the dossierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dossierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/dossiers/{id}")
    public ResponseEntity<DossierDTO> getDossier(@PathVariable Long id) {
        log.debug("REST request to get Dossier : {}", id);
        Optional<DossierDTO> dossierDTO = dossierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dossierDTO);
    }

    /**
     * {@code DELETE  /dossiers/:id} : delete the "id" dossier.
     *
     * @param id the id of the dossierDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/dossiers/{id}")
    public ResponseEntity<Void> deleteDossier(@PathVariable Long id) {
        log.debug("REST request to delete Dossier : {}", id);
        dossierService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/dossiers?query=:query} : search for the dossier corresponding
     * to the query.
     *
     * @param query the query of the dossier search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/dossiers")
    public ResponseEntity<List<DossierDTO>> searchDossiers(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Dossiers for query {}", query);
        Page<DossierDTO> page = dossierService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
