package azur.support.web.tool.web.rest;

import azur.support.web.tool.AzursupportwebtoolApp;
import azur.support.web.tool.domain.Dossier;
import azur.support.web.tool.domain.Client;
import azur.support.web.tool.domain.Intervention;
import azur.support.web.tool.repository.DossierRepository;
import azur.support.web.tool.repository.search.DossierSearchRepository;
import azur.support.web.tool.service.DossierService;
import azur.support.web.tool.service.dto.DossierDTO;
import azur.support.web.tool.service.mapper.DossierMapper;
import azur.support.web.tool.web.rest.errors.ExceptionTranslator;
import azur.support.web.tool.service.dto.DossierCriteria;
import azur.support.web.tool.service.DossierQueryService;

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
 * Integration tests for the {@Link DossierResource} REST controller.
 */
@SpringBootTest(classes = AzursupportwebtoolApp.class)
public class DossierResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_DEBUT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_DEBUT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_URL_AZIMUT = "AAAAAAAAAA";
    private static final String UPDATED_URL_AZIMUT = "BBBBBBBBBB";

    private static final String DEFAULT_URL_REDMINE = "AAAAAAAAAA";
    private static final String UPDATED_URL_REDMINE = "BBBBBBBBBB";

    private static final String DEFAULT_URL_AKUITEO = "AAAAAAAAAA";
    private static final String UPDATED_URL_AKUITEO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_FIN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_FIN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private DossierMapper dossierMapper;

    @Autowired
    private DossierService dossierService;

    /**
     * This repository is mocked in the azur.support.web.tool.repository.search test package.
     *
     * @see azur.support.web.tool.repository.search.DossierSearchRepositoryMockConfiguration
     */
    @Autowired
    private DossierSearchRepository mockDossierSearchRepository;

    @Autowired
    private DossierQueryService dossierQueryService;

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

    private MockMvc restDossierMockMvc;

    private Dossier dossier;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DossierResource dossierResource = new DossierResource(dossierService, dossierQueryService);
        this.restDossierMockMvc = MockMvcBuilders.standaloneSetup(dossierResource)
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
    public static Dossier createEntity(EntityManager em) {
        Dossier dossier = new Dossier()
            .type(DEFAULT_TYPE)
            .dateDebut(DEFAULT_DATE_DEBUT)
            .responsable(DEFAULT_RESPONSABLE)
            .etat(DEFAULT_ETAT)
            .urlAzimut(DEFAULT_URL_AZIMUT)
            .urlRedmine(DEFAULT_URL_REDMINE)
            .urlAkuiteo(DEFAULT_URL_AKUITEO)
            .dateFin(DEFAULT_DATE_FIN);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        dossier.setClient(client);
        return dossier;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dossier createUpdatedEntity(EntityManager em) {
        Dossier dossier = new Dossier()
            .type(UPDATED_TYPE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .responsable(UPDATED_RESPONSABLE)
            .etat(UPDATED_ETAT)
            .urlAzimut(UPDATED_URL_AZIMUT)
            .urlRedmine(UPDATED_URL_REDMINE)
            .urlAkuiteo(UPDATED_URL_AKUITEO)
            .dateFin(UPDATED_DATE_FIN);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        dossier.setClient(client);
        return dossier;
    }

    @BeforeEach
    public void initTest() {
        dossier = createEntity(em);
    }

    @Test
    @Transactional
    public void createDossier() throws Exception {
        int databaseSizeBeforeCreate = dossierRepository.findAll().size();

        // Create the Dossier
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);
        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierDTO)))
            .andExpect(status().isCreated());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate + 1);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testDossier.getDateDebut()).isEqualTo(DEFAULT_DATE_DEBUT);
        assertThat(testDossier.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testDossier.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testDossier.getUrlAzimut()).isEqualTo(DEFAULT_URL_AZIMUT);
        assertThat(testDossier.getUrlRedmine()).isEqualTo(DEFAULT_URL_REDMINE);
        assertThat(testDossier.getUrlAkuiteo()).isEqualTo(DEFAULT_URL_AKUITEO);
        assertThat(testDossier.getDateFin()).isEqualTo(DEFAULT_DATE_FIN);

        // Validate the Dossier in Elasticsearch
        verify(mockDossierSearchRepository, times(1)).save(testDossier);
    }

    @Test
    @Transactional
    public void createDossierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dossierRepository.findAll().size();

        // Create the Dossier with an existing ID
        dossier.setId(1L);
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate);

        // Validate the Dossier in Elasticsearch
        verify(mockDossierSearchRepository, times(0)).save(dossier);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setType(null);

        // Create the Dossier, which fails.
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierDTO)))
            .andExpect(status().isBadRequest());

        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateDebutIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setDateDebut(null);

        // Create the Dossier, which fails.
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierDTO)))
            .andExpect(status().isBadRequest());

        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setResponsable(null);

        // Create the Dossier, which fails.
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierDTO)))
            .andExpect(status().isBadRequest());

        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setEtat(null);

        // Create the Dossier, which fails.
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierDTO)))
            .andExpect(status().isBadRequest());

        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateFinIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setDateFin(null);

        // Create the Dossier, which fails.
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierDTO)))
            .andExpect(status().isBadRequest());

        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDossiers() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList
        restDossierMockMvc.perform(get("/api/dossiers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossier.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].urlAzimut").value(hasItem(DEFAULT_URL_AZIMUT.toString())))
            .andExpect(jsonPath("$.[*].urlRedmine").value(hasItem(DEFAULT_URL_REDMINE.toString())))
            .andExpect(jsonPath("$.[*].urlAkuiteo").value(hasItem(DEFAULT_URL_AKUITEO.toString())))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }
    
    @Test
    @Transactional
    public void getDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get the dossier
        restDossierMockMvc.perform(get("/api/dossiers/{id}", dossier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dossier.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.dateDebut").value(DEFAULT_DATE_DEBUT.toString()))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.urlAzimut").value(DEFAULT_URL_AZIMUT.toString()))
            .andExpect(jsonPath("$.urlRedmine").value(DEFAULT_URL_REDMINE.toString()))
            .andExpect(jsonPath("$.urlAkuiteo").value(DEFAULT_URL_AKUITEO.toString()))
            .andExpect(jsonPath("$.dateFin").value(DEFAULT_DATE_FIN.toString()));
    }

    @Test
    @Transactional
    public void getAllDossiersByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where type equals to DEFAULT_TYPE
        defaultDossierShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the dossierList where type equals to UPDATED_TYPE
        defaultDossierShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDossiersByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultDossierShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the dossierList where type equals to UPDATED_TYPE
        defaultDossierShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllDossiersByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where type is not null
        defaultDossierShouldBeFound("type.specified=true");

        // Get all the dossierList where type is null
        defaultDossierShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllDossiersByDateDebutIsEqualToSomething() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where dateDebut equals to DEFAULT_DATE_DEBUT
        defaultDossierShouldBeFound("dateDebut.equals=" + DEFAULT_DATE_DEBUT);

        // Get all the dossierList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultDossierShouldNotBeFound("dateDebut.equals=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllDossiersByDateDebutIsInShouldWork() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where dateDebut in DEFAULT_DATE_DEBUT or UPDATED_DATE_DEBUT
        defaultDossierShouldBeFound("dateDebut.in=" + DEFAULT_DATE_DEBUT + "," + UPDATED_DATE_DEBUT);

        // Get all the dossierList where dateDebut equals to UPDATED_DATE_DEBUT
        defaultDossierShouldNotBeFound("dateDebut.in=" + UPDATED_DATE_DEBUT);
    }

    @Test
    @Transactional
    public void getAllDossiersByDateDebutIsNullOrNotNull() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where dateDebut is not null
        defaultDossierShouldBeFound("dateDebut.specified=true");

        // Get all the dossierList where dateDebut is null
        defaultDossierShouldNotBeFound("dateDebut.specified=false");
    }

    @Test
    @Transactional
    public void getAllDossiersByResponsableIsEqualToSomething() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where responsable equals to DEFAULT_RESPONSABLE
        defaultDossierShouldBeFound("responsable.equals=" + DEFAULT_RESPONSABLE);

        // Get all the dossierList where responsable equals to UPDATED_RESPONSABLE
        defaultDossierShouldNotBeFound("responsable.equals=" + UPDATED_RESPONSABLE);
    }

    @Test
    @Transactional
    public void getAllDossiersByResponsableIsInShouldWork() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where responsable in DEFAULT_RESPONSABLE or UPDATED_RESPONSABLE
        defaultDossierShouldBeFound("responsable.in=" + DEFAULT_RESPONSABLE + "," + UPDATED_RESPONSABLE);

        // Get all the dossierList where responsable equals to UPDATED_RESPONSABLE
        defaultDossierShouldNotBeFound("responsable.in=" + UPDATED_RESPONSABLE);
    }

    @Test
    @Transactional
    public void getAllDossiersByResponsableIsNullOrNotNull() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where responsable is not null
        defaultDossierShouldBeFound("responsable.specified=true");

        // Get all the dossierList where responsable is null
        defaultDossierShouldNotBeFound("responsable.specified=false");
    }

    @Test
    @Transactional
    public void getAllDossiersByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where etat equals to DEFAULT_ETAT
        defaultDossierShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the dossierList where etat equals to UPDATED_ETAT
        defaultDossierShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllDossiersByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultDossierShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the dossierList where etat equals to UPDATED_ETAT
        defaultDossierShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllDossiersByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where etat is not null
        defaultDossierShouldBeFound("etat.specified=true");

        // Get all the dossierList where etat is null
        defaultDossierShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    public void getAllDossiersByUrlAzimutIsEqualToSomething() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where urlAzimut equals to DEFAULT_URL_AZIMUT
        defaultDossierShouldBeFound("urlAzimut.equals=" + DEFAULT_URL_AZIMUT);

        // Get all the dossierList where urlAzimut equals to UPDATED_URL_AZIMUT
        defaultDossierShouldNotBeFound("urlAzimut.equals=" + UPDATED_URL_AZIMUT);
    }

    @Test
    @Transactional
    public void getAllDossiersByUrlAzimutIsInShouldWork() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where urlAzimut in DEFAULT_URL_AZIMUT or UPDATED_URL_AZIMUT
        defaultDossierShouldBeFound("urlAzimut.in=" + DEFAULT_URL_AZIMUT + "," + UPDATED_URL_AZIMUT);

        // Get all the dossierList where urlAzimut equals to UPDATED_URL_AZIMUT
        defaultDossierShouldNotBeFound("urlAzimut.in=" + UPDATED_URL_AZIMUT);
    }

    @Test
    @Transactional
    public void getAllDossiersByUrlAzimutIsNullOrNotNull() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where urlAzimut is not null
        defaultDossierShouldBeFound("urlAzimut.specified=true");

        // Get all the dossierList where urlAzimut is null
        defaultDossierShouldNotBeFound("urlAzimut.specified=false");
    }

    @Test
    @Transactional
    public void getAllDossiersByUrlRedmineIsEqualToSomething() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where urlRedmine equals to DEFAULT_URL_REDMINE
        defaultDossierShouldBeFound("urlRedmine.equals=" + DEFAULT_URL_REDMINE);

        // Get all the dossierList where urlRedmine equals to UPDATED_URL_REDMINE
        defaultDossierShouldNotBeFound("urlRedmine.equals=" + UPDATED_URL_REDMINE);
    }

    @Test
    @Transactional
    public void getAllDossiersByUrlRedmineIsInShouldWork() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where urlRedmine in DEFAULT_URL_REDMINE or UPDATED_URL_REDMINE
        defaultDossierShouldBeFound("urlRedmine.in=" + DEFAULT_URL_REDMINE + "," + UPDATED_URL_REDMINE);

        // Get all the dossierList where urlRedmine equals to UPDATED_URL_REDMINE
        defaultDossierShouldNotBeFound("urlRedmine.in=" + UPDATED_URL_REDMINE);
    }

    @Test
    @Transactional
    public void getAllDossiersByUrlRedmineIsNullOrNotNull() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where urlRedmine is not null
        defaultDossierShouldBeFound("urlRedmine.specified=true");

        // Get all the dossierList where urlRedmine is null
        defaultDossierShouldNotBeFound("urlRedmine.specified=false");
    }

    @Test
    @Transactional
    public void getAllDossiersByUrlAkuiteoIsEqualToSomething() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where urlAkuiteo equals to DEFAULT_URL_AKUITEO
        defaultDossierShouldBeFound("urlAkuiteo.equals=" + DEFAULT_URL_AKUITEO);

        // Get all the dossierList where urlAkuiteo equals to UPDATED_URL_AKUITEO
        defaultDossierShouldNotBeFound("urlAkuiteo.equals=" + UPDATED_URL_AKUITEO);
    }

    @Test
    @Transactional
    public void getAllDossiersByUrlAkuiteoIsInShouldWork() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where urlAkuiteo in DEFAULT_URL_AKUITEO or UPDATED_URL_AKUITEO
        defaultDossierShouldBeFound("urlAkuiteo.in=" + DEFAULT_URL_AKUITEO + "," + UPDATED_URL_AKUITEO);

        // Get all the dossierList where urlAkuiteo equals to UPDATED_URL_AKUITEO
        defaultDossierShouldNotBeFound("urlAkuiteo.in=" + UPDATED_URL_AKUITEO);
    }

    @Test
    @Transactional
    public void getAllDossiersByUrlAkuiteoIsNullOrNotNull() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where urlAkuiteo is not null
        defaultDossierShouldBeFound("urlAkuiteo.specified=true");

        // Get all the dossierList where urlAkuiteo is null
        defaultDossierShouldNotBeFound("urlAkuiteo.specified=false");
    }

    @Test
    @Transactional
    public void getAllDossiersByDateFinIsEqualToSomething() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where dateFin equals to DEFAULT_DATE_FIN
        defaultDossierShouldBeFound("dateFin.equals=" + DEFAULT_DATE_FIN);

        // Get all the dossierList where dateFin equals to UPDATED_DATE_FIN
        defaultDossierShouldNotBeFound("dateFin.equals=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllDossiersByDateFinIsInShouldWork() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where dateFin in DEFAULT_DATE_FIN or UPDATED_DATE_FIN
        defaultDossierShouldBeFound("dateFin.in=" + DEFAULT_DATE_FIN + "," + UPDATED_DATE_FIN);

        // Get all the dossierList where dateFin equals to UPDATED_DATE_FIN
        defaultDossierShouldNotBeFound("dateFin.in=" + UPDATED_DATE_FIN);
    }

    @Test
    @Transactional
    public void getAllDossiersByDateFinIsNullOrNotNull() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        // Get all the dossierList where dateFin is not null
        defaultDossierShouldBeFound("dateFin.specified=true");

        // Get all the dossierList where dateFin is null
        defaultDossierShouldNotBeFound("dateFin.specified=false");
    }

    @Test
    @Transactional
    public void getAllDossiersByClientIsEqualToSomething() throws Exception {
        // Get already existing entity
        Client client = dossier.getClient();
        dossierRepository.saveAndFlush(dossier);
        Long clientId = client.getId();

        // Get all the dossierList where client equals to clientId
        defaultDossierShouldBeFound("clientId.equals=" + clientId);

        // Get all the dossierList where client equals to clientId + 1
        defaultDossierShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }


    @Test
    @Transactional
    public void getAllDossiersByInterventionIsEqualToSomething() throws Exception {
        // Initialize the database
        Intervention intervention = InterventionResourceIT.createEntity(em);
        em.persist(intervention);
        em.flush();
        dossier.addIntervention(intervention);
        dossierRepository.saveAndFlush(dossier);
        Long interventionId = intervention.getId();

        // Get all the dossierList where intervention equals to interventionId
        defaultDossierShouldBeFound("interventionId.equals=" + interventionId);

        // Get all the dossierList where intervention equals to interventionId + 1
        defaultDossierShouldNotBeFound("interventionId.equals=" + (interventionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDossierShouldBeFound(String filter) throws Exception {
        restDossierMockMvc.perform(get("/api/dossiers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossier.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].urlAzimut").value(hasItem(DEFAULT_URL_AZIMUT)))
            .andExpect(jsonPath("$.[*].urlRedmine").value(hasItem(DEFAULT_URL_REDMINE)))
            .andExpect(jsonPath("$.[*].urlAkuiteo").value(hasItem(DEFAULT_URL_AKUITEO)))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));

        // Check, that the count call also returns 1
        restDossierMockMvc.perform(get("/api/dossiers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDossierShouldNotBeFound(String filter) throws Exception {
        restDossierMockMvc.perform(get("/api/dossiers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDossierMockMvc.perform(get("/api/dossiers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDossier() throws Exception {
        // Get the dossier
        restDossierMockMvc.perform(get("/api/dossiers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier
        Dossier updatedDossier = dossierRepository.findById(dossier.getId()).get();
        // Disconnect from session so that the updates on updatedDossier are not directly saved in db
        em.detach(updatedDossier);
        updatedDossier
            .type(UPDATED_TYPE)
            .dateDebut(UPDATED_DATE_DEBUT)
            .responsable(UPDATED_RESPONSABLE)
            .etat(UPDATED_ETAT)
            .urlAzimut(UPDATED_URL_AZIMUT)
            .urlRedmine(UPDATED_URL_REDMINE)
            .urlAkuiteo(UPDATED_URL_AKUITEO)
            .dateFin(UPDATED_DATE_FIN);
        DossierDTO dossierDTO = dossierMapper.toDto(updatedDossier);

        restDossierMockMvc.perform(put("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierDTO)))
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testDossier.getDateDebut()).isEqualTo(UPDATED_DATE_DEBUT);
        assertThat(testDossier.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testDossier.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testDossier.getUrlAzimut()).isEqualTo(UPDATED_URL_AZIMUT);
        assertThat(testDossier.getUrlRedmine()).isEqualTo(UPDATED_URL_REDMINE);
        assertThat(testDossier.getUrlAkuiteo()).isEqualTo(UPDATED_URL_AKUITEO);
        assertThat(testDossier.getDateFin()).isEqualTo(UPDATED_DATE_FIN);

        // Validate the Dossier in Elasticsearch
        verify(mockDossierSearchRepository, times(1)).save(testDossier);
    }

    @Test
    @Transactional
    public void updateNonExistingDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Create the Dossier
        DossierDTO dossierDTO = dossierMapper.toDto(dossier);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDossierMockMvc.perform(put("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Dossier in Elasticsearch
        verify(mockDossierSearchRepository, times(0)).save(dossier);
    }

    @Test
    @Transactional
    public void deleteDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);

        int databaseSizeBeforeDelete = dossierRepository.findAll().size();

        // Delete the dossier
        restDossierMockMvc.perform(delete("/api/dossiers/{id}", dossier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Dossier in Elasticsearch
        verify(mockDossierSearchRepository, times(1)).deleteById(dossier.getId());
    }

    @Test
    @Transactional
    public void searchDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);
        when(mockDossierSearchRepository.search(queryStringQuery("id:" + dossier.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(dossier), PageRequest.of(0, 1), 1));
        // Search the dossier
        restDossierMockMvc.perform(get("/api/_search/dossiers?query=id:" + dossier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossier.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].dateDebut").value(hasItem(DEFAULT_DATE_DEBUT.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].urlAzimut").value(hasItem(DEFAULT_URL_AZIMUT)))
            .andExpect(jsonPath("$.[*].urlRedmine").value(hasItem(DEFAULT_URL_REDMINE)))
            .andExpect(jsonPath("$.[*].urlAkuiteo").value(hasItem(DEFAULT_URL_AKUITEO)))
            .andExpect(jsonPath("$.[*].dateFin").value(hasItem(DEFAULT_DATE_FIN.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Dossier.class);
        Dossier dossier1 = new Dossier();
        dossier1.setId(1L);
        Dossier dossier2 = new Dossier();
        dossier2.setId(dossier1.getId());
        assertThat(dossier1).isEqualTo(dossier2);
        dossier2.setId(2L);
        assertThat(dossier1).isNotEqualTo(dossier2);
        dossier1.setId(null);
        assertThat(dossier1).isNotEqualTo(dossier2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierDTO.class);
        DossierDTO dossierDTO1 = new DossierDTO();
        dossierDTO1.setId(1L);
        DossierDTO dossierDTO2 = new DossierDTO();
        assertThat(dossierDTO1).isNotEqualTo(dossierDTO2);
        dossierDTO2.setId(dossierDTO1.getId());
        assertThat(dossierDTO1).isEqualTo(dossierDTO2);
        dossierDTO2.setId(2L);
        assertThat(dossierDTO1).isNotEqualTo(dossierDTO2);
        dossierDTO1.setId(null);
        assertThat(dossierDTO1).isNotEqualTo(dossierDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(dossierMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(dossierMapper.fromId(null)).isNull();
    }
}
