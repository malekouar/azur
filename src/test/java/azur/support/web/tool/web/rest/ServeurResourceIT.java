package azur.support.web.tool.web.rest;

import azur.support.web.tool.AzursupportwebtoolApp;
import azur.support.web.tool.domain.Serveur;
import azur.support.web.tool.domain.Config;
import azur.support.web.tool.repository.ServeurRepository;
import azur.support.web.tool.repository.search.ServeurSearchRepository;
import azur.support.web.tool.service.ServeurService;
import azur.support.web.tool.service.dto.ServeurDTO;
import azur.support.web.tool.service.mapper.ServeurMapper;
import azur.support.web.tool.web.rest.errors.ExceptionTranslator;
import azur.support.web.tool.service.dto.ServeurCriteria;
import azur.support.web.tool.service.ServeurQueryService;

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
 * Integration tests for the {@Link ServeurResource} REST controller.
 */
@SpringBootTest(classes = AzursupportwebtoolApp.class)
public class ServeurResourceIT {

    private static final String DEFAULT_SERVEUR_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SERVEUR_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SERVEUR_NOM = "AAAAAAAAAA";
    private static final String UPDATED_SERVEUR_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_SERVEUR_IP = "AAAAAAAAAA";
    private static final String UPDATED_SERVEUR_IP = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private ServeurRepository serveurRepository;

    @Autowired
    private ServeurMapper serveurMapper;

    @Autowired
    private ServeurService serveurService;

    /**
     * This repository is mocked in the azur.support.web.tool.repository.search test package.
     *
     * @see azur.support.web.tool.repository.search.ServeurSearchRepositoryMockConfiguration
     */
    @Autowired
    private ServeurSearchRepository mockServeurSearchRepository;

    @Autowired
    private ServeurQueryService serveurQueryService;

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

    private MockMvc restServeurMockMvc;

    private Serveur serveur;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ServeurResource serveurResource = new ServeurResource(serveurService, serveurQueryService);
        this.restServeurMockMvc = MockMvcBuilders.standaloneSetup(serveurResource)
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
    public static Serveur createEntity(EntityManager em) {
        Serveur serveur = new Serveur()
            .serveurType(DEFAULT_SERVEUR_TYPE)
            .serveurNom(DEFAULT_SERVEUR_NOM)
            .serveurIp(DEFAULT_SERVEUR_IP)
            .login(DEFAULT_LOGIN)
            .password(DEFAULT_PASSWORD);
        // Add required entity
        Config config;
        if (TestUtil.findAll(em, Config.class).isEmpty()) {
            config = ConfigResourceIT.createEntity(em);
            em.persist(config);
            em.flush();
        } else {
            config = TestUtil.findAll(em, Config.class).get(0);
        }
        serveur.setConfig(config);
        return serveur;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Serveur createUpdatedEntity(EntityManager em) {
        Serveur serveur = new Serveur()
            .serveurType(UPDATED_SERVEUR_TYPE)
            .serveurNom(UPDATED_SERVEUR_NOM)
            .serveurIp(UPDATED_SERVEUR_IP)
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD);
        // Add required entity
        Config config;
        if (TestUtil.findAll(em, Config.class).isEmpty()) {
            config = ConfigResourceIT.createUpdatedEntity(em);
            em.persist(config);
            em.flush();
        } else {
            config = TestUtil.findAll(em, Config.class).get(0);
        }
        serveur.setConfig(config);
        return serveur;
    }

    @BeforeEach
    public void initTest() {
        serveur = createEntity(em);
    }

    @Test
    @Transactional
    public void createServeur() throws Exception {
        int databaseSizeBeforeCreate = serveurRepository.findAll().size();

        // Create the Serveur
        ServeurDTO serveurDTO = serveurMapper.toDto(serveur);
        restServeurMockMvc.perform(post("/api/serveurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serveurDTO)))
            .andExpect(status().isCreated());

        // Validate the Serveur in the database
        List<Serveur> serveurList = serveurRepository.findAll();
        assertThat(serveurList).hasSize(databaseSizeBeforeCreate + 1);
        Serveur testServeur = serveurList.get(serveurList.size() - 1);
        assertThat(testServeur.getServeurType()).isEqualTo(DEFAULT_SERVEUR_TYPE);
        assertThat(testServeur.getServeurNom()).isEqualTo(DEFAULT_SERVEUR_NOM);
        assertThat(testServeur.getServeurIp()).isEqualTo(DEFAULT_SERVEUR_IP);
        assertThat(testServeur.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testServeur.getPassword()).isEqualTo(DEFAULT_PASSWORD);

        // Validate the Serveur in Elasticsearch
        verify(mockServeurSearchRepository, times(1)).save(testServeur);
    }

    @Test
    @Transactional
    public void createServeurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = serveurRepository.findAll().size();

        // Create the Serveur with an existing ID
        serveur.setId(1L);
        ServeurDTO serveurDTO = serveurMapper.toDto(serveur);

        // An entity with an existing ID cannot be created, so this API call must fail
        restServeurMockMvc.perform(post("/api/serveurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serveurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Serveur in the database
        List<Serveur> serveurList = serveurRepository.findAll();
        assertThat(serveurList).hasSize(databaseSizeBeforeCreate);

        // Validate the Serveur in Elasticsearch
        verify(mockServeurSearchRepository, times(0)).save(serveur);
    }


    @Test
    @Transactional
    public void getAllServeurs() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList
        restServeurMockMvc.perform(get("/api/serveurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serveur.getId().intValue())))
            .andExpect(jsonPath("$.[*].serveurType").value(hasItem(DEFAULT_SERVEUR_TYPE.toString())))
            .andExpect(jsonPath("$.[*].serveurNom").value(hasItem(DEFAULT_SERVEUR_NOM.toString())))
            .andExpect(jsonPath("$.[*].serveurIp").value(hasItem(DEFAULT_SERVEUR_IP.toString())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())));
    }
    
    @Test
    @Transactional
    public void getServeur() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get the serveur
        restServeurMockMvc.perform(get("/api/serveurs/{id}", serveur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(serveur.getId().intValue()))
            .andExpect(jsonPath("$.serveurType").value(DEFAULT_SERVEUR_TYPE.toString()))
            .andExpect(jsonPath("$.serveurNom").value(DEFAULT_SERVEUR_NOM.toString()))
            .andExpect(jsonPath("$.serveurIp").value(DEFAULT_SERVEUR_IP.toString()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getAllServeursByServeurTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where serveurType equals to DEFAULT_SERVEUR_TYPE
        defaultServeurShouldBeFound("serveurType.equals=" + DEFAULT_SERVEUR_TYPE);

        // Get all the serveurList where serveurType equals to UPDATED_SERVEUR_TYPE
        defaultServeurShouldNotBeFound("serveurType.equals=" + UPDATED_SERVEUR_TYPE);
    }

    @Test
    @Transactional
    public void getAllServeursByServeurTypeIsInShouldWork() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where serveurType in DEFAULT_SERVEUR_TYPE or UPDATED_SERVEUR_TYPE
        defaultServeurShouldBeFound("serveurType.in=" + DEFAULT_SERVEUR_TYPE + "," + UPDATED_SERVEUR_TYPE);

        // Get all the serveurList where serveurType equals to UPDATED_SERVEUR_TYPE
        defaultServeurShouldNotBeFound("serveurType.in=" + UPDATED_SERVEUR_TYPE);
    }

    @Test
    @Transactional
    public void getAllServeursByServeurTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where serveurType is not null
        defaultServeurShouldBeFound("serveurType.specified=true");

        // Get all the serveurList where serveurType is null
        defaultServeurShouldNotBeFound("serveurType.specified=false");
    }

    @Test
    @Transactional
    public void getAllServeursByServeurNomIsEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where serveurNom equals to DEFAULT_SERVEUR_NOM
        defaultServeurShouldBeFound("serveurNom.equals=" + DEFAULT_SERVEUR_NOM);

        // Get all the serveurList where serveurNom equals to UPDATED_SERVEUR_NOM
        defaultServeurShouldNotBeFound("serveurNom.equals=" + UPDATED_SERVEUR_NOM);
    }

    @Test
    @Transactional
    public void getAllServeursByServeurNomIsInShouldWork() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where serveurNom in DEFAULT_SERVEUR_NOM or UPDATED_SERVEUR_NOM
        defaultServeurShouldBeFound("serveurNom.in=" + DEFAULT_SERVEUR_NOM + "," + UPDATED_SERVEUR_NOM);

        // Get all the serveurList where serveurNom equals to UPDATED_SERVEUR_NOM
        defaultServeurShouldNotBeFound("serveurNom.in=" + UPDATED_SERVEUR_NOM);
    }

    @Test
    @Transactional
    public void getAllServeursByServeurNomIsNullOrNotNull() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where serveurNom is not null
        defaultServeurShouldBeFound("serveurNom.specified=true");

        // Get all the serveurList where serveurNom is null
        defaultServeurShouldNotBeFound("serveurNom.specified=false");
    }

    @Test
    @Transactional
    public void getAllServeursByServeurIpIsEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where serveurIp equals to DEFAULT_SERVEUR_IP
        defaultServeurShouldBeFound("serveurIp.equals=" + DEFAULT_SERVEUR_IP);

        // Get all the serveurList where serveurIp equals to UPDATED_SERVEUR_IP
        defaultServeurShouldNotBeFound("serveurIp.equals=" + UPDATED_SERVEUR_IP);
    }

    @Test
    @Transactional
    public void getAllServeursByServeurIpIsInShouldWork() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where serveurIp in DEFAULT_SERVEUR_IP or UPDATED_SERVEUR_IP
        defaultServeurShouldBeFound("serveurIp.in=" + DEFAULT_SERVEUR_IP + "," + UPDATED_SERVEUR_IP);

        // Get all the serveurList where serveurIp equals to UPDATED_SERVEUR_IP
        defaultServeurShouldNotBeFound("serveurIp.in=" + UPDATED_SERVEUR_IP);
    }

    @Test
    @Transactional
    public void getAllServeursByServeurIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where serveurIp is not null
        defaultServeurShouldBeFound("serveurIp.specified=true");

        // Get all the serveurList where serveurIp is null
        defaultServeurShouldNotBeFound("serveurIp.specified=false");
    }

    @Test
    @Transactional
    public void getAllServeursByLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where login equals to DEFAULT_LOGIN
        defaultServeurShouldBeFound("login.equals=" + DEFAULT_LOGIN);

        // Get all the serveurList where login equals to UPDATED_LOGIN
        defaultServeurShouldNotBeFound("login.equals=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllServeursByLoginIsInShouldWork() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where login in DEFAULT_LOGIN or UPDATED_LOGIN
        defaultServeurShouldBeFound("login.in=" + DEFAULT_LOGIN + "," + UPDATED_LOGIN);

        // Get all the serveurList where login equals to UPDATED_LOGIN
        defaultServeurShouldNotBeFound("login.in=" + UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void getAllServeursByLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where login is not null
        defaultServeurShouldBeFound("login.specified=true");

        // Get all the serveurList where login is null
        defaultServeurShouldNotBeFound("login.specified=false");
    }

    @Test
    @Transactional
    public void getAllServeursByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where password equals to DEFAULT_PASSWORD
        defaultServeurShouldBeFound("password.equals=" + DEFAULT_PASSWORD);

        // Get all the serveurList where password equals to UPDATED_PASSWORD
        defaultServeurShouldNotBeFound("password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllServeursByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where password in DEFAULT_PASSWORD or UPDATED_PASSWORD
        defaultServeurShouldBeFound("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD);

        // Get all the serveurList where password equals to UPDATED_PASSWORD
        defaultServeurShouldNotBeFound("password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllServeursByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        // Get all the serveurList where password is not null
        defaultServeurShouldBeFound("password.specified=true");

        // Get all the serveurList where password is null
        defaultServeurShouldNotBeFound("password.specified=false");
    }

    @Test
    @Transactional
    public void getAllServeursByConfigIsEqualToSomething() throws Exception {
        // Get already existing entity
        Config config = serveur.getConfig();
        serveurRepository.saveAndFlush(serveur);
        Long configId = config.getId();

        // Get all the serveurList where config equals to configId
        defaultServeurShouldBeFound("configId.equals=" + configId);

        // Get all the serveurList where config equals to configId + 1
        defaultServeurShouldNotBeFound("configId.equals=" + (configId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultServeurShouldBeFound(String filter) throws Exception {
        restServeurMockMvc.perform(get("/api/serveurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serveur.getId().intValue())))
            .andExpect(jsonPath("$.[*].serveurType").value(hasItem(DEFAULT_SERVEUR_TYPE)))
            .andExpect(jsonPath("$.[*].serveurNom").value(hasItem(DEFAULT_SERVEUR_NOM)))
            .andExpect(jsonPath("$.[*].serveurIp").value(hasItem(DEFAULT_SERVEUR_IP)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));

        // Check, that the count call also returns 1
        restServeurMockMvc.perform(get("/api/serveurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultServeurShouldNotBeFound(String filter) throws Exception {
        restServeurMockMvc.perform(get("/api/serveurs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restServeurMockMvc.perform(get("/api/serveurs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingServeur() throws Exception {
        // Get the serveur
        restServeurMockMvc.perform(get("/api/serveurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServeur() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        int databaseSizeBeforeUpdate = serveurRepository.findAll().size();

        // Update the serveur
        Serveur updatedServeur = serveurRepository.findById(serveur.getId()).get();
        // Disconnect from session so that the updates on updatedServeur are not directly saved in db
        em.detach(updatedServeur);
        updatedServeur
            .serveurType(UPDATED_SERVEUR_TYPE)
            .serveurNom(UPDATED_SERVEUR_NOM)
            .serveurIp(UPDATED_SERVEUR_IP)
            .login(UPDATED_LOGIN)
            .password(UPDATED_PASSWORD);
        ServeurDTO serveurDTO = serveurMapper.toDto(updatedServeur);

        restServeurMockMvc.perform(put("/api/serveurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serveurDTO)))
            .andExpect(status().isOk());

        // Validate the Serveur in the database
        List<Serveur> serveurList = serveurRepository.findAll();
        assertThat(serveurList).hasSize(databaseSizeBeforeUpdate);
        Serveur testServeur = serveurList.get(serveurList.size() - 1);
        assertThat(testServeur.getServeurType()).isEqualTo(UPDATED_SERVEUR_TYPE);
        assertThat(testServeur.getServeurNom()).isEqualTo(UPDATED_SERVEUR_NOM);
        assertThat(testServeur.getServeurIp()).isEqualTo(UPDATED_SERVEUR_IP);
        assertThat(testServeur.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testServeur.getPassword()).isEqualTo(UPDATED_PASSWORD);

        // Validate the Serveur in Elasticsearch
        verify(mockServeurSearchRepository, times(1)).save(testServeur);
    }

    @Test
    @Transactional
    public void updateNonExistingServeur() throws Exception {
        int databaseSizeBeforeUpdate = serveurRepository.findAll().size();

        // Create the Serveur
        ServeurDTO serveurDTO = serveurMapper.toDto(serveur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServeurMockMvc.perform(put("/api/serveurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(serveurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Serveur in the database
        List<Serveur> serveurList = serveurRepository.findAll();
        assertThat(serveurList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Serveur in Elasticsearch
        verify(mockServeurSearchRepository, times(0)).save(serveur);
    }

    @Test
    @Transactional
    public void deleteServeur() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);

        int databaseSizeBeforeDelete = serveurRepository.findAll().size();

        // Delete the serveur
        restServeurMockMvc.perform(delete("/api/serveurs/{id}", serveur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Serveur> serveurList = serveurRepository.findAll();
        assertThat(serveurList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Serveur in Elasticsearch
        verify(mockServeurSearchRepository, times(1)).deleteById(serveur.getId());
    }

    @Test
    @Transactional
    public void searchServeur() throws Exception {
        // Initialize the database
        serveurRepository.saveAndFlush(serveur);
        when(mockServeurSearchRepository.search(queryStringQuery("id:" + serveur.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(serveur), PageRequest.of(0, 1), 1));
        // Search the serveur
        restServeurMockMvc.perform(get("/api/_search/serveurs?query=id:" + serveur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serveur.getId().intValue())))
            .andExpect(jsonPath("$.[*].serveurType").value(hasItem(DEFAULT_SERVEUR_TYPE)))
            .andExpect(jsonPath("$.[*].serveurNom").value(hasItem(DEFAULT_SERVEUR_NOM)))
            .andExpect(jsonPath("$.[*].serveurIp").value(hasItem(DEFAULT_SERVEUR_IP)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Serveur.class);
        Serveur serveur1 = new Serveur();
        serveur1.setId(1L);
        Serveur serveur2 = new Serveur();
        serveur2.setId(serveur1.getId());
        assertThat(serveur1).isEqualTo(serveur2);
        serveur2.setId(2L);
        assertThat(serveur1).isNotEqualTo(serveur2);
        serveur1.setId(null);
        assertThat(serveur1).isNotEqualTo(serveur2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ServeurDTO.class);
        ServeurDTO serveurDTO1 = new ServeurDTO();
        serveurDTO1.setId(1L);
        ServeurDTO serveurDTO2 = new ServeurDTO();
        assertThat(serveurDTO1).isNotEqualTo(serveurDTO2);
        serveurDTO2.setId(serveurDTO1.getId());
        assertThat(serveurDTO1).isEqualTo(serveurDTO2);
        serveurDTO2.setId(2L);
        assertThat(serveurDTO1).isNotEqualTo(serveurDTO2);
        serveurDTO1.setId(null);
        assertThat(serveurDTO1).isNotEqualTo(serveurDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(serveurMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(serveurMapper.fromId(null)).isNull();
    }
}
