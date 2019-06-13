package azur.support.web.tool.web.rest;

import azur.support.web.tool.service.ConfigService;
import azur.support.web.tool.web.rest.errors.BadRequestAlertException;
import azur.support.web.tool.service.dto.ConfigDTO;
import azur.support.web.tool.service.dto.ConfigCriteria;
import azur.support.web.tool.service.ConfigQueryService;

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
 * REST controller for managing {@link azur.support.web.tool.domain.Config}.
 */
@RestController
@RequestMapping("/api")
public class ConfigResource {

    private final Logger log = LoggerFactory.getLogger(ConfigResource.class);

    private static final String ENTITY_NAME = "config";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigService configService;

    private final ConfigQueryService configQueryService;

    public ConfigResource(ConfigService configService, ConfigQueryService configQueryService) {
        this.configService = configService;
        this.configQueryService = configQueryService;
    }

    /**
     * {@code POST  /configs} : Create a new config.
     *
     * @param configDTO the configDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configDTO, or with status {@code 400 (Bad Request)} if the config has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/configs")
    public ResponseEntity<ConfigDTO> createConfig(@Valid @RequestBody ConfigDTO configDTO) throws URISyntaxException {
        log.debug("REST request to save Config : {}", configDTO);
        if (configDTO.getId() != null) {
            throw new BadRequestAlertException("A new config cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigDTO result = configService.save(configDTO);
        return ResponseEntity.created(new URI("/api/configs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /configs} : Updates an existing config.
     *
     * @param configDTO the configDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configDTO,
     * or with status {@code 400 (Bad Request)} if the configDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/configs")
    public ResponseEntity<ConfigDTO> updateConfig(@Valid @RequestBody ConfigDTO configDTO) throws URISyntaxException {
        log.debug("REST request to update Config : {}", configDTO);
        if (configDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ConfigDTO result = configService.save(configDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /configs} : get all the configs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configs in body.
     */
    @GetMapping("/configs")
    public ResponseEntity<List<ConfigDTO>> getAllConfigs(ConfigCriteria criteria, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get Configs by criteria: {}", criteria);
        Page<ConfigDTO> page = configQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /configs/count} : count all the configs.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/configs/count")
    public ResponseEntity<Long> countConfigs(ConfigCriteria criteria) {
        log.debug("REST request to count Configs by criteria: {}", criteria);
        return ResponseEntity.ok().body(configQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /configs/:id} : get the "id" config.
     *
     * @param id the id of the configDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/configs/{id}")
    public ResponseEntity<ConfigDTO> getConfig(@PathVariable Long id) {
        log.debug("REST request to get Config : {}", id);
        Optional<ConfigDTO> configDTO = configService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configDTO);
    }

    /**
     * {@code DELETE  /configs/:id} : delete the "id" config.
     *
     * @param id the id of the configDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/configs/{id}")
    public ResponseEntity<Void> deleteConfig(@PathVariable Long id) {
        log.debug("REST request to delete Config : {}", id);
        configService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/configs?query=:query} : search for the config corresponding
     * to the query.
     *
     * @param query the query of the config search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/configs")
    public ResponseEntity<List<ConfigDTO>> searchConfigs(@RequestParam String query, Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to search for a page of Configs for query {}", query);
        Page<ConfigDTO> page = configService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

}
