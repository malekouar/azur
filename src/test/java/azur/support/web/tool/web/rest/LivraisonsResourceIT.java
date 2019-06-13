package azur.support.web.tool.web.rest;

import azur.support.web.tool.AzursupportwebtoolApp;
import azur.support.web.tool.domain.Livraisons;
import azur.support.web.tool.domain.Intervention;
import azur.support.web.tool.repository.LivraisonsRepository;
import azur.support.web.tool.repository.search.LivraisonsSearchRepository;
import azur.support.web.tool.service.LivraisonsService;
import azur.support.web.tool.service.dto.LivraisonsDTO;
import azur.support.web.tool.service.mapper.LivraisonsMapper;
import azur.support.web.tool.web.rest.errors.ExceptionTranslator;
import azur.support.web.tool.service.dto.LivraisonsCriteria;
import azur.support.web.tool.service.LivraisonsQueryService;

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
 * Integration tests for the {@Link LivraisonsResource} REST controller.
 */
@SpringBootTest(classes = AzursupportwebtoolApp.class)
public class LivraisonsResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_LIVRAISON = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_LIVRAISON = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RESPONSABLE = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSABLE = "BBBBBBBBBB";

    private static final String DEFAULT_ETAT = "AAAAAAAAAA";
    private static final String UPDATED_ETAT = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_PACKAGE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PACKAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_ID_SVN = 1;
    private static final Integer UPDATED_ID_SVN = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DETAIL = "AAAAAAAAAA";
    private static final String UPDATED_DETAIL = "BBBBBBBBBB";

    @Autowired
    private LivraisonsRepository livraisonsRepository;

    @Autowired
    private LivraisonsMapper livraisonsMapper;

    @Autowired
    private LivraisonsService livraisonsService;

    /**
     * This repository is mocked in the azur.support.web.tool.repository.search test package.
     *
     * @see azur.support.web.tool.repository.search.LivraisonsSearchRepositoryMockConfiguration
     */
    @Autowired
    private LivraisonsSearchRepository mockLivraisonsSearchRepository;

    @Autowired
    private LivraisonsQueryService livraisonsQueryService;

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

    private MockMvc restLivraisonsMockMvc;

    private Livraisons livraisons;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LivraisonsResource livraisonsResource = new LivraisonsResource(livraisonsService, livraisonsQueryService);
        this.restLivraisonsMockMvc = MockMvcBuilders.standaloneSetup(livraisonsResource)
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
    public static Livraisons createEntity(EntityManager em) {
        Livraisons livraisons = new Livraisons()
            .type(DEFAULT_TYPE)
            .dateLivraison(DEFAULT_DATE_LIVRAISON)
            .responsable(DEFAULT_RESPONSABLE)
            .etat(DEFAULT_ETAT)
            .nomPackage(DEFAULT_NOM_PACKAGE)
            .idSvn(DEFAULT_ID_SVN)
            .description(DEFAULT_DESCRIPTION)
            .detail(DEFAULT_DETAIL);
        // Add required entity
        Intervention intervention;
        if (TestUtil.findAll(em, Intervention.class).isEmpty()) {
            intervention = InterventionResourceIT.createEntity(em);
            em.persist(intervention);
            em.flush();
        } else {
            intervention = TestUtil.findAll(em, Intervention.class).get(0);
        }
        livraisons.setIntervention(intervention);
        return livraisons;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Livraisons createUpdatedEntity(EntityManager em) {
        Livraisons livraisons = new Livraisons()
            .type(UPDATED_TYPE)
            .dateLivraison(UPDATED_DATE_LIVRAISON)
            .responsable(UPDATED_RESPONSABLE)
            .etat(UPDATED_ETAT)
            .nomPackage(UPDATED_NOM_PACKAGE)
            .idSvn(UPDATED_ID_SVN)
            .description(UPDATED_DESCRIPTION)
            .detail(UPDATED_DETAIL);
        // Add required entity
        Intervention intervention;
        if (TestUtil.findAll(em, Intervention.class).isEmpty()) {
            intervention = InterventionResourceIT.createUpdatedEntity(em);
            em.persist(intervention);
            em.flush();
        } else {
            intervention = TestUtil.findAll(em, Intervention.class).get(0);
        }
        livraisons.setIntervention(intervention);
        return livraisons;
    }

    @BeforeEach
    public void initTest() {
        livraisons = createEntity(em);
    }

    @Test
    @Transactional
    public void createLivraisons() throws Exception {
        int databaseSizeBeforeCreate = livraisonsRepository.findAll().size();

        // Create the Livraisons
        LivraisonsDTO livraisonsDTO = livraisonsMapper.toDto(livraisons);
        restLivraisonsMockMvc.perform(post("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livraisonsDTO)))
            .andExpect(status().isCreated());

        // Validate the Livraisons in the database
        List<Livraisons> livraisonsList = livraisonsRepository.findAll();
        assertThat(livraisonsList).hasSize(databaseSizeBeforeCreate + 1);
        Livraisons testLivraisons = livraisonsList.get(livraisonsList.size() - 1);
        assertThat(testLivraisons.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testLivraisons.getDateLivraison()).isEqualTo(DEFAULT_DATE_LIVRAISON);
        assertThat(testLivraisons.getResponsable()).isEqualTo(DEFAULT_RESPONSABLE);
        assertThat(testLivraisons.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testLivraisons.getNomPackage()).isEqualTo(DEFAULT_NOM_PACKAGE);
        assertThat(testLivraisons.getIdSvn()).isEqualTo(DEFAULT_ID_SVN);
        assertThat(testLivraisons.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testLivraisons.getDetail()).isEqualTo(DEFAULT_DETAIL);

        // Validate the Livraisons in Elasticsearch
        verify(mockLivraisonsSearchRepository, times(1)).save(testLivraisons);
    }

    @Test
    @Transactional
    public void createLivraisonsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = livraisonsRepository.findAll().size();

        // Create the Livraisons with an existing ID
        livraisons.setId(1L);
        LivraisonsDTO livraisonsDTO = livraisonsMapper.toDto(livraisons);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLivraisonsMockMvc.perform(post("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livraisonsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Livraisons in the database
        List<Livraisons> livraisonsList = livraisonsRepository.findAll();
        assertThat(livraisonsList).hasSize(databaseSizeBeforeCreate);

        // Validate the Livraisons in Elasticsearch
        verify(mockLivraisonsSearchRepository, times(0)).save(livraisons);
    }


    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonsRepository.findAll().size();
        // set the field null
        livraisons.setType(null);

        // Create the Livraisons, which fails.
        LivraisonsDTO livraisonsDTO = livraisonsMapper.toDto(livraisons);

        restLivraisonsMockMvc.perform(post("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livraisonsDTO)))
            .andExpect(status().isBadRequest());

        List<Livraisons> livraisonsList = livraisonsRepository.findAll();
        assertThat(livraisonsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateLivraisonIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonsRepository.findAll().size();
        // set the field null
        livraisons.setDateLivraison(null);

        // Create the Livraisons, which fails.
        LivraisonsDTO livraisonsDTO = livraisonsMapper.toDto(livraisons);

        restLivraisonsMockMvc.perform(post("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livraisonsDTO)))
            .andExpect(status().isBadRequest());

        List<Livraisons> livraisonsList = livraisonsRepository.findAll();
        assertThat(livraisonsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkResponsableIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonsRepository.findAll().size();
        // set the field null
        livraisons.setResponsable(null);

        // Create the Livraisons, which fails.
        LivraisonsDTO livraisonsDTO = livraisonsMapper.toDto(livraisons);

        restLivraisonsMockMvc.perform(post("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livraisonsDTO)))
            .andExpect(status().isBadRequest());

        List<Livraisons> livraisonsList = livraisonsRepository.findAll();
        assertThat(livraisonsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEtatIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonsRepository.findAll().size();
        // set the field null
        livraisons.setEtat(null);

        // Create the Livraisons, which fails.
        LivraisonsDTO livraisonsDTO = livraisonsMapper.toDto(livraisons);

        restLivraisonsMockMvc.perform(post("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livraisonsDTO)))
            .andExpect(status().isBadRequest());

        List<Livraisons> livraisonsList = livraisonsRepository.findAll();
        assertThat(livraisonsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomPackageIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonsRepository.findAll().size();
        // set the field null
        livraisons.setNomPackage(null);

        // Create the Livraisons, which fails.
        LivraisonsDTO livraisonsDTO = livraisonsMapper.toDto(livraisons);

        restLivraisonsMockMvc.perform(post("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livraisonsDTO)))
            .andExpect(status().isBadRequest());

        List<Livraisons> livraisonsList = livraisonsRepository.findAll();
        assertThat(livraisonsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdSvnIsRequired() throws Exception {
        int databaseSizeBeforeTest = livraisonsRepository.findAll().size();
        // set the field null
        livraisons.setIdSvn(null);

        // Create the Livraisons, which fails.
        LivraisonsDTO livraisonsDTO = livraisonsMapper.toDto(livraisons);

        restLivraisonsMockMvc.perform(post("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livraisonsDTO)))
            .andExpect(status().isBadRequest());

        List<Livraisons> livraisonsList = livraisonsRepository.findAll();
        assertThat(livraisonsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllLivraisons() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList
        restLivraisonsMockMvc.perform(get("/api/livraisons?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livraisons.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].dateLivraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE.toString())))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].nomPackage").value(hasItem(DEFAULT_NOM_PACKAGE.toString())))
            .andExpect(jsonPath("$.[*].idSvn").value(hasItem(DEFAULT_ID_SVN)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL.toString())));
    }
    
    @Test
    @Transactional
    public void getLivraisons() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get the livraisons
        restLivraisonsMockMvc.perform(get("/api/livraisons/{id}", livraisons.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(livraisons.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.dateLivraison").value(DEFAULT_DATE_LIVRAISON.toString()))
            .andExpect(jsonPath("$.responsable").value(DEFAULT_RESPONSABLE.toString()))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.nomPackage").value(DEFAULT_NOM_PACKAGE.toString()))
            .andExpect(jsonPath("$.idSvn").value(DEFAULT_ID_SVN))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.detail").value(DEFAULT_DETAIL.toString()));
    }

    @Test
    @Transactional
    public void getAllLivraisonsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where type equals to DEFAULT_TYPE
        defaultLivraisonsShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the livraisonsList where type equals to UPDATED_TYPE
        defaultLivraisonsShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultLivraisonsShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the livraisonsList where type equals to UPDATED_TYPE
        defaultLivraisonsShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where type is not null
        defaultLivraisonsShouldBeFound("type.specified=true");

        // Get all the livraisonsList where type is null
        defaultLivraisonsShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    public void getAllLivraisonsByDateLivraisonIsEqualToSomething() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where dateLivraison equals to DEFAULT_DATE_LIVRAISON
        defaultLivraisonsShouldBeFound("dateLivraison.equals=" + DEFAULT_DATE_LIVRAISON);

        // Get all the livraisonsList where dateLivraison equals to UPDATED_DATE_LIVRAISON
        defaultLivraisonsShouldNotBeFound("dateLivraison.equals=" + UPDATED_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByDateLivraisonIsInShouldWork() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where dateLivraison in DEFAULT_DATE_LIVRAISON or UPDATED_DATE_LIVRAISON
        defaultLivraisonsShouldBeFound("dateLivraison.in=" + DEFAULT_DATE_LIVRAISON + "," + UPDATED_DATE_LIVRAISON);

        // Get all the livraisonsList where dateLivraison equals to UPDATED_DATE_LIVRAISON
        defaultLivraisonsShouldNotBeFound("dateLivraison.in=" + UPDATED_DATE_LIVRAISON);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByDateLivraisonIsNullOrNotNull() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where dateLivraison is not null
        defaultLivraisonsShouldBeFound("dateLivraison.specified=true");

        // Get all the livraisonsList where dateLivraison is null
        defaultLivraisonsShouldNotBeFound("dateLivraison.specified=false");
    }

    @Test
    @Transactional
    public void getAllLivraisonsByResponsableIsEqualToSomething() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where responsable equals to DEFAULT_RESPONSABLE
        defaultLivraisonsShouldBeFound("responsable.equals=" + DEFAULT_RESPONSABLE);

        // Get all the livraisonsList where responsable equals to UPDATED_RESPONSABLE
        defaultLivraisonsShouldNotBeFound("responsable.equals=" + UPDATED_RESPONSABLE);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByResponsableIsInShouldWork() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where responsable in DEFAULT_RESPONSABLE or UPDATED_RESPONSABLE
        defaultLivraisonsShouldBeFound("responsable.in=" + DEFAULT_RESPONSABLE + "," + UPDATED_RESPONSABLE);

        // Get all the livraisonsList where responsable equals to UPDATED_RESPONSABLE
        defaultLivraisonsShouldNotBeFound("responsable.in=" + UPDATED_RESPONSABLE);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByResponsableIsNullOrNotNull() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where responsable is not null
        defaultLivraisonsShouldBeFound("responsable.specified=true");

        // Get all the livraisonsList where responsable is null
        defaultLivraisonsShouldNotBeFound("responsable.specified=false");
    }

    @Test
    @Transactional
    public void getAllLivraisonsByEtatIsEqualToSomething() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where etat equals to DEFAULT_ETAT
        defaultLivraisonsShouldBeFound("etat.equals=" + DEFAULT_ETAT);

        // Get all the livraisonsList where etat equals to UPDATED_ETAT
        defaultLivraisonsShouldNotBeFound("etat.equals=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByEtatIsInShouldWork() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where etat in DEFAULT_ETAT or UPDATED_ETAT
        defaultLivraisonsShouldBeFound("etat.in=" + DEFAULT_ETAT + "," + UPDATED_ETAT);

        // Get all the livraisonsList where etat equals to UPDATED_ETAT
        defaultLivraisonsShouldNotBeFound("etat.in=" + UPDATED_ETAT);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByEtatIsNullOrNotNull() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where etat is not null
        defaultLivraisonsShouldBeFound("etat.specified=true");

        // Get all the livraisonsList where etat is null
        defaultLivraisonsShouldNotBeFound("etat.specified=false");
    }

    @Test
    @Transactional
    public void getAllLivraisonsByNomPackageIsEqualToSomething() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where nomPackage equals to DEFAULT_NOM_PACKAGE
        defaultLivraisonsShouldBeFound("nomPackage.equals=" + DEFAULT_NOM_PACKAGE);

        // Get all the livraisonsList where nomPackage equals to UPDATED_NOM_PACKAGE
        defaultLivraisonsShouldNotBeFound("nomPackage.equals=" + UPDATED_NOM_PACKAGE);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByNomPackageIsInShouldWork() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where nomPackage in DEFAULT_NOM_PACKAGE or UPDATED_NOM_PACKAGE
        defaultLivraisonsShouldBeFound("nomPackage.in=" + DEFAULT_NOM_PACKAGE + "," + UPDATED_NOM_PACKAGE);

        // Get all the livraisonsList where nomPackage equals to UPDATED_NOM_PACKAGE
        defaultLivraisonsShouldNotBeFound("nomPackage.in=" + UPDATED_NOM_PACKAGE);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByNomPackageIsNullOrNotNull() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where nomPackage is not null
        defaultLivraisonsShouldBeFound("nomPackage.specified=true");

        // Get all the livraisonsList where nomPackage is null
        defaultLivraisonsShouldNotBeFound("nomPackage.specified=false");
    }

    @Test
    @Transactional
    public void getAllLivraisonsByIdSvnIsEqualToSomething() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where idSvn equals to DEFAULT_ID_SVN
        defaultLivraisonsShouldBeFound("idSvn.equals=" + DEFAULT_ID_SVN);

        // Get all the livraisonsList where idSvn equals to UPDATED_ID_SVN
        defaultLivraisonsShouldNotBeFound("idSvn.equals=" + UPDATED_ID_SVN);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByIdSvnIsInShouldWork() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where idSvn in DEFAULT_ID_SVN or UPDATED_ID_SVN
        defaultLivraisonsShouldBeFound("idSvn.in=" + DEFAULT_ID_SVN + "," + UPDATED_ID_SVN);

        // Get all the livraisonsList where idSvn equals to UPDATED_ID_SVN
        defaultLivraisonsShouldNotBeFound("idSvn.in=" + UPDATED_ID_SVN);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByIdSvnIsNullOrNotNull() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where idSvn is not null
        defaultLivraisonsShouldBeFound("idSvn.specified=true");

        // Get all the livraisonsList where idSvn is null
        defaultLivraisonsShouldNotBeFound("idSvn.specified=false");
    }

    @Test
    @Transactional
    public void getAllLivraisonsByIdSvnIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where idSvn greater than or equals to DEFAULT_ID_SVN
        defaultLivraisonsShouldBeFound("idSvn.greaterOrEqualThan=" + DEFAULT_ID_SVN);

        // Get all the livraisonsList where idSvn greater than or equals to UPDATED_ID_SVN
        defaultLivraisonsShouldNotBeFound("idSvn.greaterOrEqualThan=" + UPDATED_ID_SVN);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByIdSvnIsLessThanSomething() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where idSvn less than or equals to DEFAULT_ID_SVN
        defaultLivraisonsShouldNotBeFound("idSvn.lessThan=" + DEFAULT_ID_SVN);

        // Get all the livraisonsList where idSvn less than or equals to UPDATED_ID_SVN
        defaultLivraisonsShouldBeFound("idSvn.lessThan=" + UPDATED_ID_SVN);
    }


    @Test
    @Transactional
    public void getAllLivraisonsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where description equals to DEFAULT_DESCRIPTION
        defaultLivraisonsShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the livraisonsList where description equals to UPDATED_DESCRIPTION
        defaultLivraisonsShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultLivraisonsShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the livraisonsList where description equals to UPDATED_DESCRIPTION
        defaultLivraisonsShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where description is not null
        defaultLivraisonsShouldBeFound("description.specified=true");

        // Get all the livraisonsList where description is null
        defaultLivraisonsShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllLivraisonsByDetailIsEqualToSomething() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where detail equals to DEFAULT_DETAIL
        defaultLivraisonsShouldBeFound("detail.equals=" + DEFAULT_DETAIL);

        // Get all the livraisonsList where detail equals to UPDATED_DETAIL
        defaultLivraisonsShouldNotBeFound("detail.equals=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByDetailIsInShouldWork() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where detail in DEFAULT_DETAIL or UPDATED_DETAIL
        defaultLivraisonsShouldBeFound("detail.in=" + DEFAULT_DETAIL + "," + UPDATED_DETAIL);

        // Get all the livraisonsList where detail equals to UPDATED_DETAIL
        defaultLivraisonsShouldNotBeFound("detail.in=" + UPDATED_DETAIL);
    }

    @Test
    @Transactional
    public void getAllLivraisonsByDetailIsNullOrNotNull() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        // Get all the livraisonsList where detail is not null
        defaultLivraisonsShouldBeFound("detail.specified=true");

        // Get all the livraisonsList where detail is null
        defaultLivraisonsShouldNotBeFound("detail.specified=false");
    }

    @Test
    @Transactional
    public void getAllLivraisonsByInterventionIsEqualToSomething() throws Exception {
        // Get already existing entity
        Intervention intervention = livraisons.getIntervention();
        livraisonsRepository.saveAndFlush(livraisons);
        Long interventionId = intervention.getId();

        // Get all the livraisonsList where intervention equals to interventionId
        defaultLivraisonsShouldBeFound("interventionId.equals=" + interventionId);

        // Get all the livraisonsList where intervention equals to interventionId + 1
        defaultLivraisonsShouldNotBeFound("interventionId.equals=" + (interventionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLivraisonsShouldBeFound(String filter) throws Exception {
        restLivraisonsMockMvc.perform(get("/api/livraisons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livraisons.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].dateLivraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].nomPackage").value(hasItem(DEFAULT_NOM_PACKAGE)))
            .andExpect(jsonPath("$.[*].idSvn").value(hasItem(DEFAULT_ID_SVN)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)));

        // Check, that the count call also returns 1
        restLivraisonsMockMvc.perform(get("/api/livraisons/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLivraisonsShouldNotBeFound(String filter) throws Exception {
        restLivraisonsMockMvc.perform(get("/api/livraisons?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLivraisonsMockMvc.perform(get("/api/livraisons/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLivraisons() throws Exception {
        // Get the livraisons
        restLivraisonsMockMvc.perform(get("/api/livraisons/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLivraisons() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        int databaseSizeBeforeUpdate = livraisonsRepository.findAll().size();

        // Update the livraisons
        Livraisons updatedLivraisons = livraisonsRepository.findById(livraisons.getId()).get();
        // Disconnect from session so that the updates on updatedLivraisons are not directly saved in db
        em.detach(updatedLivraisons);
        updatedLivraisons
            .type(UPDATED_TYPE)
            .dateLivraison(UPDATED_DATE_LIVRAISON)
            .responsable(UPDATED_RESPONSABLE)
            .etat(UPDATED_ETAT)
            .nomPackage(UPDATED_NOM_PACKAGE)
            .idSvn(UPDATED_ID_SVN)
            .description(UPDATED_DESCRIPTION)
            .detail(UPDATED_DETAIL);
        LivraisonsDTO livraisonsDTO = livraisonsMapper.toDto(updatedLivraisons);

        restLivraisonsMockMvc.perform(put("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livraisonsDTO)))
            .andExpect(status().isOk());

        // Validate the Livraisons in the database
        List<Livraisons> livraisonsList = livraisonsRepository.findAll();
        assertThat(livraisonsList).hasSize(databaseSizeBeforeUpdate);
        Livraisons testLivraisons = livraisonsList.get(livraisonsList.size() - 1);
        assertThat(testLivraisons.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLivraisons.getDateLivraison()).isEqualTo(UPDATED_DATE_LIVRAISON);
        assertThat(testLivraisons.getResponsable()).isEqualTo(UPDATED_RESPONSABLE);
        assertThat(testLivraisons.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testLivraisons.getNomPackage()).isEqualTo(UPDATED_NOM_PACKAGE);
        assertThat(testLivraisons.getIdSvn()).isEqualTo(UPDATED_ID_SVN);
        assertThat(testLivraisons.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testLivraisons.getDetail()).isEqualTo(UPDATED_DETAIL);

        // Validate the Livraisons in Elasticsearch
        verify(mockLivraisonsSearchRepository, times(1)).save(testLivraisons);
    }

    @Test
    @Transactional
    public void updateNonExistingLivraisons() throws Exception {
        int databaseSizeBeforeUpdate = livraisonsRepository.findAll().size();

        // Create the Livraisons
        LivraisonsDTO livraisonsDTO = livraisonsMapper.toDto(livraisons);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLivraisonsMockMvc.perform(put("/api/livraisons")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(livraisonsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Livraisons in the database
        List<Livraisons> livraisonsList = livraisonsRepository.findAll();
        assertThat(livraisonsList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Livraisons in Elasticsearch
        verify(mockLivraisonsSearchRepository, times(0)).save(livraisons);
    }

    @Test
    @Transactional
    public void deleteLivraisons() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);

        int databaseSizeBeforeDelete = livraisonsRepository.findAll().size();

        // Delete the livraisons
        restLivraisonsMockMvc.perform(delete("/api/livraisons/{id}", livraisons.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Livraisons> livraisonsList = livraisonsRepository.findAll();
        assertThat(livraisonsList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Livraisons in Elasticsearch
        verify(mockLivraisonsSearchRepository, times(1)).deleteById(livraisons.getId());
    }

    @Test
    @Transactional
    public void searchLivraisons() throws Exception {
        // Initialize the database
        livraisonsRepository.saveAndFlush(livraisons);
        when(mockLivraisonsSearchRepository.search(queryStringQuery("id:" + livraisons.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(livraisons), PageRequest.of(0, 1), 1));
        // Search the livraisons
        restLivraisonsMockMvc.perform(get("/api/_search/livraisons?query=id:" + livraisons.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(livraisons.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].dateLivraison").value(hasItem(DEFAULT_DATE_LIVRAISON.toString())))
            .andExpect(jsonPath("$.[*].responsable").value(hasItem(DEFAULT_RESPONSABLE)))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT)))
            .andExpect(jsonPath("$.[*].nomPackage").value(hasItem(DEFAULT_NOM_PACKAGE)))
            .andExpect(jsonPath("$.[*].idSvn").value(hasItem(DEFAULT_ID_SVN)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].detail").value(hasItem(DEFAULT_DETAIL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Livraisons.class);
        Livraisons livraisons1 = new Livraisons();
        livraisons1.setId(1L);
        Livraisons livraisons2 = new Livraisons();
        livraisons2.setId(livraisons1.getId());
        assertThat(livraisons1).isEqualTo(livraisons2);
        livraisons2.setId(2L);
        assertThat(livraisons1).isNotEqualTo(livraisons2);
        livraisons1.setId(null);
        assertThat(livraisons1).isNotEqualTo(livraisons2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LivraisonsDTO.class);
        LivraisonsDTO livraisonsDTO1 = new LivraisonsDTO();
        livraisonsDTO1.setId(1L);
        LivraisonsDTO livraisonsDTO2 = new LivraisonsDTO();
        assertThat(livraisonsDTO1).isNotEqualTo(livraisonsDTO2);
        livraisonsDTO2.setId(livraisonsDTO1.getId());
        assertThat(livraisonsDTO1).isEqualTo(livraisonsDTO2);
        livraisonsDTO2.setId(2L);
        assertThat(livraisonsDTO1).isNotEqualTo(livraisonsDTO2);
        livraisonsDTO1.setId(null);
        assertThat(livraisonsDTO1).isNotEqualTo(livraisonsDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(livraisonsMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(livraisonsMapper.fromId(null)).isNull();
    }
}
