package azur.support.web.tool.web.rest;

import azur.support.web.tool.service.InterventionService;
import azur.support.web.tool.web.rest.errors.BadRequestAlertException;
import azur.support.web.tool.service.dto.InterventionDTO;
import azur.support.web.tool.service.dto.InterventionCriteria;
import azur.support.web.tool.service.InterventionQueryService;

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
 * REST controller for managing {@link azur.support.web.tool.domain.Intervention}.
 */
@RestController
@RequestMapping("/api")
public class InterventionResource {

    private final Logger log = LoggerFactory.getLogger(InterventionResource.class);

    private static final String ENTITY_NAME = "intervention";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterventionService interventionService;

    private final InterventionQueryService interventionQueryService;

    public InterventionResource(InterventionService interventionService, InterventionQueryService interventionQueryService) {
        this.interventionService = interventionService;
        this.interventionQueryService = interventionQueryService;
    }

    /**
     * {@code POST  /interventions} : Create a new intervention.
     *
     * @param interventionDTO the interventionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interventionDTO, or with status {@code 400 (Bad Request)} if the intervention has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interventions")
    public ResponseEntity<InterventionDTO> createIntervention(@Valid @RequestBody InterventionDTO interventionDTO) throws URISyntaxException {
        log.debug("REST request to save Intervention : {}", interventionDTO);
        if (interventionDTO.getId() != null) {
            throw new BadRequestAlertException("A new intervention cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterventionDTO result = interventionService.save(interventionDTO);
        return ResponseEntity.created(new URI("/api/interventions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interventions} : Updates an existing intervention.
     *
     * @param interventionDTO the interventionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interventionDTO,
     * or with status {@code 400 (Bad Request)} if the interventionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interventionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interventions")
    public ResponseEntity<InterventionDTO> updateIntervention(@Valid @RequestBody InterventionDTO interventionDTO) throws URISyntaxException {
        log.debug("REST request to update Intervention : {}", interventionDTO);
        if (interventionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        InterventionDTO result = interventionService.save(interventionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interventionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /interventions} : get all the interventions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interventions in body.
     */
    @GetMapping("/interventions")
    public ResponseEntity<List<InterventionDTO>> getAllInterventions(InterventionCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Interventions by criteria: {}", criteria);
        Page<InterventionDTO> page = interventionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /interventions/count} : count all the interventions.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/interventions/count")
    public ResponseEntity<Long> countInterventions(InterventionCriteria criteria) {
        log.debug("REST request to count Interventions by criteria: {}", criteria);
        return ResponseEntity.ok().body(interventionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /interventions/:id} : get the "id" intervention.
     *
     * @param id the id of the interventionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interventionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interventions/{id}")
    public ResponseEntity<InterventionDTO> getIntervention(@PathVariable Long id) {
        log.debug("REST request to get Intervention : {}", id);
        Optional<InterventionDTO> interventionDTO = interventionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interventionDTO);
    }

    /**
     * {@code DELETE  /interventions/:id} : delete the "id" intervention.
     *
     * @param id the id of the interventionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interventions/{id}")
    public ResponseEntity<Void> deleteIntervention(@PathVariable Long id) {
        log.debug("REST request to delete Intervention : {}", id);
        interventionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/interventions?query=:query} : search for the intervention corresponding
     * to the query.
     *
     * @param query the query of the intervention search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/interventions")
    public ResponseEntity<List<InterventionDTO>> searchInterventions(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Interventions for query {}", query);
        Page<InterventionDTO> page = interventionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
