package azur.support.web.tool.web.rest;

import azur.support.web.tool.AzursupportwebtoolApp;
import azur.support.web.tool.domain.Intervention;
import azur.support.web.tool.domain.Dossier;
import azur.support.web.tool.domain.Livraisons;
import azur.support.web.tool.repository.InterventionRepository;
import azur.support.web.tool.repository.search.InterventionSearchRepository;
import azur.support.web.tool.service.InterventionService;
import azur.support.web.tool.service.dto.InterventionDTO;
import azur.support.web.tool.service.mapper.InterventionMapper;
import azur.support.web.tool.web.rest.errors.ExceptionTranslator;
import azur.support.web.tool.service.dto.InterventionCriteria;
import azur.support.web.tool.service.InterventionQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;

import static azur.support.web.tool.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link InterventionResource} REST controller.
 */
@SpringBootTest(classes = AzursupportwebtoolApp.class)
public class InterventionResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL = "BBBBBBBBBB";

    @Autowired
    private InterventionRepository interventionRepository;

    @Autowired
    private InterventionMapper interventionMapper;

    @Autowired
    private InterventionService interventionService;

    /**
     * This repository is mocked in the azur.support.web.tool.repository.search test package.
     *
     * @see azur.support.web.tool.repository.search.InterventionSearchRepositoryMockConfiguration
     */
    @Autowired
    private InterventionSearchRepository mockInterventionSearchRepository;

    @Autowired
    private InterventionQueryService interventionQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restInterventionMockMvc;

    private Intervention intervention;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final InterventionResource interventionResource = new InterventionResource(interventionService, interventionQueryService);
        this.restInterventionMockMvc = MockMvcBuilders.standaloneSetup(interventionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervention createEntity(EntityManager em) {
        Intervention intervention = new Intervention()
            .type(DEFAULT_TYPE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .dateFin(DEFAULT_DATE_FIN)
            .responsable(DEFAULT_RESPONSABLE)
            .etat(DEFAULT_ETAT)
            .description(DEFAULT_DESCRIPTION)
            .detail(DEFAULT_DETAIL);
        // Add required entity
        Dossier dossier;
        if (TestUtil.findAll(em, Dossier.class).isEmpty()) {
            dossier = DossierResourceIT.createEntity(em);
            em.persist(dossier);
            em.flush();
        } else {
            dossier = TestUtil.findAll(em, Dossier.class).get(0);
        }
        intervention.setDossier(dossier);
        return intervention;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Intervention createUpdatedEntity(EntityManager em) {
        Intervention intervention = new Intervention()
            .type(UPDATED_TYPE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .responsable(UPDATED_RESPONSABLE)
            .etat(UPDATED_ETAT)
            .description(UPDATED_DESCRIPTION)
            .detail(UPDATED_DETAIL);
        // Add required entity
        Dossier dossier;
        if (TestUtil.findAll(em, Dossier.class).isEmpty()) {
            dossier = DossierResourceIT.createUpdatedEntity(em);
            em.persist(dossier);
            em.flush();
        } else {
            dossier = TestUtil.findAll(em, Dossier.class).get(0);
        }
        intervention.setDossier(dossier);
        return intervention;
    }

    @BeforeEach
    public void initTest() {
        intervention = createEntity(em);
    }

    @Test
    @Transactional
    public void createIntervention() throws Exception {
        int databaseSizeBeforeCreate = interventionRepository.findAll().size();

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);
        restInterventionMockMvc.perform(post("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isCreated());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeCreate + 1);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testIntervention.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testIntervention.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);
        assertThat(testIntervention.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testIntervention.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testIntervention.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testIntervention.getDetail()).isEqualTo(DEFAULT_DETAIL);

        // Validate the Intervention in Elasticsearch
        verify(mockInterventionSearchRepository, times(1)).save(testIntervention);
    }

    @Test
    @Transactional
    public void createInterventionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = interventionRepository.findAll().size();

        // Create the Intervention with an existing ID
        intervention.setId(1L);
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // An entity with an existing ID cannot be created, so this API call must fail
        restInterventionMockMvc.perform(post("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeCreate);

        // Validate the Intervention in Elasticsearch
        verify(mockInterventionSearchRepository, times(0)).save(intervention);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = interventionRepository.findAll().size();
        // set the field null
        intervention.setType(null);

        // Create the Intervention, which fails.
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        restInterventionMockMvc.perform(post("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = interventionRepository.findAll().size();
        // set the field null
        intervention.setDateDebut(null);

        // Create the Intervention, which fails.
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        restInterventionMockMvc.perform(post("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = interventionRepository.findAll().size();
        // set the field null
        intervention.setResponsable(null);

        // Create the Intervention, which fails.
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        restInterventionMockMvc.perform(post("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = interventionRepository.findAll().size();
        // set the field null
        intervention.setEtat(null);

        // Create the Intervention, which fails.
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        restInterventionMockMvc.perform(post("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllInterventions() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList
        restInterventionMockMvc.perform(get("/api/interventions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL.toString())));
    }
    
    @Test
    @Transactional
    public void getIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get the intervention
        restInterventionMockMvc.perform(get("/api/interventions/{id}", intervention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(intervention.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL.toString()));
    }

    @Test
    @Transactional
    public void getAllInterventionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where type equals to DEFAULT_TYPE
        defaultInterventionShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the interventionList where type equals to UPDATED_TYPE
        defaultInterventionShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllInterventionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultInterventionShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the interventionList where type equals to UPDATED_TYPE
        defaultInterventionShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllInterventionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where type is not null
        defaultInterventionShouldBeFound("type.specified=true");

        // Get all the interventionList where type is null
        defaultInterventionShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllInterventionsByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultInterventionShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the interventionList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultInterventionShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllInterventionsByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultInterventionShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the interventionList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultInterventionShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllInterventionsByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateDebut is not null
        defaultInterventionShouldBeFound("dateDebut.specified=true");

        // Get all the interventionList where dateDebut is null
        defaultInterventionShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllInterventionsByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateFin equals to DEFAULT_DATE_FIN
        defaultInterventionShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the interventionList where dateFin equals to UPDATED_DATE_FIN
        defaultInterventionShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllInterventionsByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultInterventionShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the interventionList where dateFin equals to UPDATED_DATE_FIN
        defaultInterventionShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllInterventionsByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where dateFin is not null
        defaultInterventionShouldBeFound("dateFin.specified=true");

        // Get all the interventionList where dateFin is null
        defaultInterventionShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    public void getAllInterventionsByResponsableIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where responsable equals to DEFAULT_RESPONSABLE
        defaultInterventionShouldBeFound("responsable.equals=" + DEFAULT_RESPONSABLE);

        // Get all the interventionList where responsable equals to UPDATED_RESPONSABLE
        defaultInterventionShouldNotBeFound("responsable.equals=" + UPDATED_RESPONSABLE);
    }

    @Test
    @Transactional
    public void getAllInterventionsByResponsableIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where responsable in DEFAULT_RESPONSABLE or UPDATED_RESPONSABLE
        defaultInterventionShouldBeFound("responsable.in=" + DEFAULT_RESPONSABLE + "," + UPDATED_RESPONSABLE);

        // Get all the interventionList where responsable equals to UPDATED_RESPONSABLE
        defaultInterventionShouldNotBeFound("responsable.in=" + UPDATED_RESPONSABLE);
    }

    @Test
    @Transactional
    public void getAllInterventionsByResponsableIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where responsable is not null
        defaultInterventionShouldBeFound("responsable.specified=true");

        // Get all the interventionList where responsable is null
        defaultInterventionShouldNotBeFound("responsable.specified=false");
    }

    @Test
    @Transactional
    public void getAllInterventionsByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where etat equals to DEFAULT_ETAT
        defaultInterventionShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the interventionList where etat equals to UPDATED_ETAT
        defaultInterventionShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllInterventionsByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultInterventionShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the interventionList where etat equals to UPDATED_ETAT
        defaultInterventionShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllInterventionsByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where etat is not null
        defaultInterventionShouldBeFound("etat.specified=true");

        // Get all the interventionList where etat is null
        defaultInterventionShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    public void getAllInterventionsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where description equals to DEFAULT_DESCRIPTION
        defaultInterventionShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the interventionList where description equals to UPDATED_DESCRIPTION
        defaultInterventionShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInterventionsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultInterventionShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the interventionList where description equals to UPDATED_DESCRIPTION
        defaultInterventionShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllInterventionsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where description is not null
        defaultInterventionShouldBeFound("description.specified=true");

        // Get all the interventionList where description is null
        defaultInterventionShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllInterventionsByDetailIsEqualToSomething() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where detail equals to DEFAULT_DETAIL
        defaultInterventionShouldBeFound("detail.equals=" + DEFAULT_DETAIL);

        // Get all the interventionList where detail equals to UPDATED_DETAIL
        defaultInterventionShouldNotBeFound("detail.equals=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    public void getAllInterventionsByDetailIsInShouldWork() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where detail in DEFAULT_DETAIL or UPDATED_DETAIL
        defaultInterventionShouldBeFound("detail.in=" + DEFAULT_DETAIL + "," + UPDATED_DETAIL);

        // Get all the interventionList where detail equals to UPDATED_DETAIL
        defaultInterventionShouldNotBeFound("detail.in=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    public void getAllInterventionsByDetailIsNullOrNotNull() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        // Get all the interventionList where detail is not null
        defaultInterventionShouldBeFound("detail.specified=true");

        // Get all the interventionList where detail is null
        defaultInterventionShouldNotBeFound("detail.specified=false");
    }

    @Test
    @Transactional
    public void getAllInterventionsByDossierIsEqualToSomething() throws Exception {
        // Get already existing entity
        Dossier dossier = intervention.getDossier();
        interventionRepository.saveAndFlush(intervention);
        Long dossierId = dossier.getId();

        // Get all the interventionList where dossier equals to dossierId
        defaultInterventionShouldBeFound("dossierId.equals=" + dossierId);

        // Get all the interventionList where dossier equals to dossierId + 1
        defaultInterventionShouldNotBeFound("dossierId.equals=" + (dossierId + 1));
    }


    @Test
    @Transactional
    public void getAllInterventionsByLivraisonsIsEqualToSomething() throws Exception {
        // Initialize the database
        Livraisons livraisons = LivraisonsResourceIT.createEntity(em);
        em.persist(livraisons);
        em.flush();
        intervention.addLivraisons(livraisons);
        interventionRepository.saveAndFlush(intervention);
        Long livraisonsId = livraisons.getId();

        // Get all the interventionList where livraisons equals to livraisonsId
        defaultInterventionShouldBeFound("livraisonsId.equals=" + livraisonsId);

        // Get all the interventionList where livraisons equals to livraisonsId + 1
        defaultInterventionShouldNotBeFound("livraisonsId.equals=" + (livraisonsId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInterventionShouldBeFound(String filter) throws Exception {
        restInterventionMockMvc.perform(get("/api/interventions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)));

        // Check, that the count call also returns 1
        restInterventionMockMvc.perform(get("/api/interventions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInterventionShouldNotBeFound(String filter) throws Exception {
        restInterventionMockMvc.perform(get("/api/interventions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInterventionMockMvc.perform(get("/api/interventions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingIntervention() throws Exception {
        // Get the intervention
        restInterventionMockMvc.perform(get("/api/interventions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Update the intervention
        Intervention updatedIntervention = interventionRepository.findById(intervention.getId()).get();
        // Disconnect from session so that the updates on updatedIntervention are not directly saved in db
        em.detach(updatedIntervention);
        updatedIntervention
            .type(UPDATED_TYPE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .dateFin(UPDATED_DATE_FIN)
            .responsable(UPDATED_RESPONSABLE)
            .etat(UPDATED_ETAT)
            .description(UPDATED_DESCRIPTION)
            .detail(UPDATED_DETAIL);
        InterventionDTO interventionDTO = interventionMapper.toDto(updatedIntervention);

        restInterventionMockMvc.perform(put("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isOk());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);
        Intervention testIntervention = interventionList.get(interventionList.size() - 1);
        assertThat(testIntervention.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testIntervention.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testIntervention.getDateFin()).isEqualTo(UPDATED_DATE_FIN);
        assertThat(testIntervention.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testIntervention.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testIntervention.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testIntervention.getDetail()).isEqualTo(UPDATED_DETAIL);

        // Validate the Intervention in Elasticsearch
        verify(mockInterventionSearchRepository, times(1)).save(testIntervention);
    }

    @Test
    @Transactional
    public void updateNonExistingIntervention() throws Exception {
        int databaseSizeBeforeUpdate = interventionRepository.findAll().size();

        // Create the Intervention
        InterventionDTO interventionDTO = interventionMapper.toDto(intervention);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInterventionMockMvc.perform(put("/api/interventions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(interventionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Intervention in the database
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Intervention in Elasticsearch
        verify(mockInterventionSearchRepository, times(0)).save(intervention);
    }

    @Test
    @Transactional
    public void deleteIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);

        int databaseSizeBeforeDelete = interventionRepository.findAll().size();

        // Delete the intervention
        restInterventionMockMvc.perform(delete("/api/interventions/{id}", intervention.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Intervention> interventionList = interventionRepository.findAll();
        assertThat(interventionList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Intervention in Elasticsearch
        verify(mockInterventionSearchRepository, times(1)).deleteById(intervention.getId());
    }

    @Test
    @Transactional
    public void searchIntervention() throws Exception {
        // Initialize the database
        interventionRepository.saveAndFlush(intervention);
        when(mockInterventionSearchRepository.search(queryStringQuery("id:" + intervention.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(intervention), PageRequest.of(0, 1), 1));
        // Search the intervention
        restInterventionMockMvc.perform(get("/api/_search/interventions?query=id:" + intervention.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(intervention.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Intervention.class);
        Intervention intervention1 = new Intervention();
        intervention1.setId(1L);
        Intervention intervention2 = new Intervention();
        intervention2.setId(intervention1.getId());
        assertThat(intervention1).isEqualTo(intervention2);
        intervention2.setId(2L);
        assertThat(intervention1).isNotEqualTo(intervention2);
        intervention1.setId(null);
        assertThat(intervention1).isNotEqualTo(intervention2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InterventionDTO.class);
        InterventionDTO interventionDTO1 = new InterventionDTO();
        interventionDTO1.setId(1L);
        InterventionDTO interventionDTO2 = new InterventionDTO();
        assertThat(interventionDTO1).isNotEqualTo(interventionDTO2);
        interventionDTO2.setId(interventionDTO1.getId());
        assertThat(interventionDTO1).isEqualTo(interventionDTO2);
        interventionDTO2.setId(2L);
        assertThat(interventionDTO1).isNotEqualTo(interventionDTO2);
        interventionDTO1.setId(null);
        assertThat(interventionDTO1).isNotEqualTo(interventionDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(interventionMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(interventionMapper.fromId(null)).isNull();
    }
}
