package azur.support.web.tool.web.rest;

import azur.support.web.tool.AzursupportwebtoolApp;
import azur.support.web.tool.domain.Config;
import azur.support.web.tool.domain.Client;
import azur.support.web.tool.domain.Serveur;
import azur.support.web.tool.repository.ConfigRepository;
import azur.support.web.tool.repository.search.ConfigSearchRepository;
import azur.support.web.tool.service.ConfigService;
import azur.support.web.tool.service.dto.ConfigDTO;
import azur.support.web.tool.service.mapper.ConfigMapper;
import azur.support.web.tool.web.rest.errors.ExceptionTranslator;
import azur.support.web.tool.service.dto.ConfigCriteria;
import azur.support.web.tool.service.ConfigQueryService;

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
 * Integration tests for the {@Link ConfigResource} REST controller.
 */
@SpringBootTest(classes = AzursupportwebtoolApp.class)
public class ConfigResourceIT {

    private static final String DEFAULT_TEAMVIEWER_ID = "AAAAAAAAAA";
    private static final String UPDATED_TEAMVIEWER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TEAMVIEWER_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_TEAMVIEWER_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_VPN_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_VPN_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VPN_IP = "AAAAAAAAAA";
    private static final String UPDATED_VPN_IP = "BBBBBBBBBB";

    private static final String DEFAULT_VPN_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_VPN_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_VPN_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_VPN_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private ConfigRepository configRepository;

    @Autowired
    private ConfigMapper configMapper;

    @Autowired
    private ConfigService configService;

    /**
     * This repository is mocked in the azur.support.web.tool.repository.search test package.
     *
     * @see azur.support.web.tool.repository.search.ConfigSearchRepositoryMockConfiguration
     */
    @Autowired
    private ConfigSearchRepository mockConfigSearchRepository;

    @Autowired
    private ConfigQueryService configQueryService;

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

    private MockMvc restConfigMockMvc;

    private Config config;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ConfigResource configResource = new ConfigResource(configService, configQueryService);
        this.restConfigMockMvc = MockMvcBuilders.standaloneSetup(configResource)
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
    public static Config createEntity(EntityManager em) {
        Config config = new Config()
            .teamviewerId(DEFAULT_TEAMVIEWER_ID)
            .teamviewerPassword(DEFAULT_TEAMVIEWER_PASSWORD)
            .vpnType(DEFAULT_VPN_TYPE)
            .vpnIp(DEFAULT_VPN_IP)
            .vpnLogin(DEFAULT_VPN_LOGIN)
            .vpnPassword(DEFAULT_VPN_PASSWORD);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        config.setClient(client);
        return config;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Config createUpdatedEntity(EntityManager em) {
        Config config = new Config()
            .teamviewerId(UPDATED_TEAMVIEWER_ID)
            .teamviewerPassword(UPDATED_TEAMVIEWER_PASSWORD)
            .vpnType(UPDATED_VPN_TYPE)
            .vpnIp(UPDATED_VPN_IP)
            .vpnLogin(UPDATED_VPN_LOGIN)
            .vpnPassword(UPDATED_VPN_PASSWORD);
        // Add required entity
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createUpdatedEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        config.setClient(client);
        return config;
    }

    @BeforeEach
    public void initTest() {
        config = createEntity(em);
    }

    @Test
    @Transactional
    public void createConfig() throws Exception {
        int databaseSizeBeforeCreate = configRepository.findAll().size();

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);
        restConfigMockMvc.perform(post("/api/configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isCreated());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeCreate + 1);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getTeamviewerId()).isEqualTo(DEFAULT_TEAMVIEWER_ID);
        assertThat(testConfig.getTeamviewerPassword()).isEqualTo(DEFAULT_TEAMVIEWER_PASSWORD);
        assertThat(testConfig.getVpnType()).isEqualTo(DEFAULT_VPN_TYPE);
        assertThat(testConfig.getVpnIp()).isEqualTo(DEFAULT_VPN_IP);
        assertThat(testConfig.getVpnLogin()).isEqualTo(DEFAULT_VPN_LOGIN);
        assertThat(testConfig.getVpnPassword()).isEqualTo(DEFAULT_VPN_PASSWORD);

        // Validate the Config in Elasticsearch
        verify(mockConfigSearchRepository, times(1)).save(testConfig);
    }

    @Test
    @Transactional
    public void createConfigWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = configRepository.findAll().size();

        // Create the Config with an existing ID
        config.setId(1L);
        ConfigDTO configDTO = configMapper.toDto(config);

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigMockMvc.perform(post("/api/configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeCreate);

        // Validate the Config in Elasticsearch
        verify(mockConfigSearchRepository, times(0)).save(config);
    }


    @Test
    @Transactional
    public void getAllConfigs() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList
        restConfigMockMvc.perform(get("/api/configs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(config.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamviewerId").value(hasItem(DEFAULT_TEAMVIEWER_ID.toString())))
            .andExpect(jsonPath("$.[*].teamviewerPassword").value(hasItem(DEFAULT_TEAMVIEWER_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].vpnType").value(hasItem(DEFAULT_VPN_TYPE.toString())))
            .andExpect(jsonPath("$.[*].vpnIp").value(hasItem(DEFAULT_VPN_IP.toString())))
            .andExpect(jsonPath("$.[*].vpnLogin").value(hasItem(DEFAULT_VPN_LOGIN.toString())))
            .andExpect(jsonPath("$.[*].vpnPassword").value(hasItem(DEFAULT_VPN_PASSWORD.toString())));
    }
    
    @Test
    @Transactional
    public void getConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get the config
        restConfigMockMvc.perform(get("/api/configs/{id}", config.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(config.getId().intValue()))
            .andExpect(jsonPath("$.teamviewerId").value(DEFAULT_TEAMVIEWER_ID.toString()))
            .andExpect(jsonPath("$.teamviewerPassword").value(DEFAULT_TEAMVIEWER_PASSWORD.toString()))
            .andExpect(jsonPath("$.vpnType").value(DEFAULT_VPN_TYPE.toString()))
            .andExpect(jsonPath("$.vpnIp").value(DEFAULT_VPN_IP.toString()))
            .andExpect(jsonPath("$.vpnLogin").value(DEFAULT_VPN_LOGIN.toString()))
            .andExpect(jsonPath("$.vpnPassword").value(DEFAULT_VPN_PASSWORD.toString()));
    }

    @Test
    @Transactional
    public void getAllConfigsByTeamviewerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where teamviewerId equals to DEFAULT_TEAMVIEWER_ID
        defaultConfigShouldBeFound("teamviewerId.equals=" + DEFAULT_TEAMVIEWER_ID);

        // Get all the configList where teamviewerId equals to UPDATED_TEAMVIEWER_ID
        defaultConfigShouldNotBeFound("teamviewerId.equals=" + UPDATED_TEAMVIEWER_ID);
    }

    @Test
    @Transactional
    public void getAllConfigsByTeamviewerIdIsInShouldWork() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where teamviewerId in DEFAULT_TEAMVIEWER_ID or UPDATED_TEAMVIEWER_ID
        defaultConfigShouldBeFound("teamviewerId.in=" + DEFAULT_TEAMVIEWER_ID + "," + UPDATED_TEAMVIEWER_ID);

        // Get all the configList where teamviewerId equals to UPDATED_TEAMVIEWER_ID
        defaultConfigShouldNotBeFound("teamviewerId.in=" + UPDATED_TEAMVIEWER_ID);
    }

    @Test
    @Transactional
    public void getAllConfigsByTeamviewerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where teamviewerId is not null
        defaultConfigShouldBeFound("teamviewerId.specified=true");

        // Get all the configList where teamviewerId is null
        defaultConfigShouldNotBeFound("teamviewerId.specified=false");
    }

    @Test
    @Transactional
    public void getAllConfigsByTeamviewerPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where teamviewerPassword equals to DEFAULT_TEAMVIEWER_PASSWORD
        defaultConfigShouldBeFound("teamviewerPassword.equals=" + DEFAULT_TEAMVIEWER_PASSWORD);

        // Get all the configList where teamviewerPassword equals to UPDATED_TEAMVIEWER_PASSWORD
        defaultConfigShouldNotBeFound("teamviewerPassword.equals=" + UPDATED_TEAMVIEWER_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllConfigsByTeamviewerPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where teamviewerPassword in DEFAULT_TEAMVIEWER_PASSWORD or UPDATED_TEAMVIEWER_PASSWORD
        defaultConfigShouldBeFound("teamviewerPassword.in=" + DEFAULT_TEAMVIEWER_PASSWORD + "," + UPDATED_TEAMVIEWER_PASSWORD);

        // Get all the configList where teamviewerPassword equals to UPDATED_TEAMVIEWER_PASSWORD
        defaultConfigShouldNotBeFound("teamviewerPassword.in=" + UPDATED_TEAMVIEWER_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllConfigsByTeamviewerPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where teamviewerPassword is not null
        defaultConfigShouldBeFound("teamviewerPassword.specified=true");

        // Get all the configList where teamviewerPassword is null
        defaultConfigShouldNotBeFound("teamviewerPassword.specified=false");
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnType equals to DEFAULT_VPN_TYPE
        defaultConfigShouldBeFound("vpnType.equals=" + DEFAULT_VPN_TYPE);

        // Get all the configList where vpnType equals to UPDATED_VPN_TYPE
        defaultConfigShouldNotBeFound("vpnType.equals=" + UPDATED_VPN_TYPE);
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnTypeIsInShouldWork() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnType in DEFAULT_VPN_TYPE or UPDATED_VPN_TYPE
        defaultConfigShouldBeFound("vpnType.in=" + DEFAULT_VPN_TYPE + "," + UPDATED_VPN_TYPE);

        // Get all the configList where vpnType equals to UPDATED_VPN_TYPE
        defaultConfigShouldNotBeFound("vpnType.in=" + UPDATED_VPN_TYPE);
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnType is not null
        defaultConfigShouldBeFound("vpnType.specified=true");

        // Get all the configList where vpnType is null
        defaultConfigShouldNotBeFound("vpnType.specified=false");
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnIpIsEqualToSomething() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnIp equals to DEFAULT_VPN_IP
        defaultConfigShouldBeFound("vpnIp.equals=" + DEFAULT_VPN_IP);

        // Get all the configList where vpnIp equals to UPDATED_VPN_IP
        defaultConfigShouldNotBeFound("vpnIp.equals=" + UPDATED_VPN_IP);
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnIpIsInShouldWork() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnIp in DEFAULT_VPN_IP or UPDATED_VPN_IP
        defaultConfigShouldBeFound("vpnIp.in=" + DEFAULT_VPN_IP + "," + UPDATED_VPN_IP);

        // Get all the configList where vpnIp equals to UPDATED_VPN_IP
        defaultConfigShouldNotBeFound("vpnIp.in=" + UPDATED_VPN_IP);
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnIp is not null
        defaultConfigShouldBeFound("vpnIp.specified=true");

        // Get all the configList where vpnIp is null
        defaultConfigShouldNotBeFound("vpnIp.specified=false");
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnLoginIsEqualToSomething() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnLogin equals to DEFAULT_VPN_LOGIN
        defaultConfigShouldBeFound("vpnLogin.equals=" + DEFAULT_VPN_LOGIN);

        // Get all the configList where vpnLogin equals to UPDATED_VPN_LOGIN
        defaultConfigShouldNotBeFound("vpnLogin.equals=" + UPDATED_VPN_LOGIN);
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnLoginIsInShouldWork() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnLogin in DEFAULT_VPN_LOGIN or UPDATED_VPN_LOGIN
        defaultConfigShouldBeFound("vpnLogin.in=" + DEFAULT_VPN_LOGIN + "," + UPDATED_VPN_LOGIN);

        // Get all the configList where vpnLogin equals to UPDATED_VPN_LOGIN
        defaultConfigShouldNotBeFound("vpnLogin.in=" + UPDATED_VPN_LOGIN);
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnLoginIsNullOrNotNull() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnLogin is not null
        defaultConfigShouldBeFound("vpnLogin.specified=true");

        // Get all the configList where vpnLogin is null
        defaultConfigShouldNotBeFound("vpnLogin.specified=false");
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnPassword equals to DEFAULT_VPN_PASSWORD
        defaultConfigShouldBeFound("vpnPassword.equals=" + DEFAULT_VPN_PASSWORD);

        // Get all the configList where vpnPassword equals to UPDATED_VPN_PASSWORD
        defaultConfigShouldNotBeFound("vpnPassword.equals=" + UPDATED_VPN_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnPassword in DEFAULT_VPN_PASSWORD or UPDATED_VPN_PASSWORD
        defaultConfigShouldBeFound("vpnPassword.in=" + DEFAULT_VPN_PASSWORD + "," + UPDATED_VPN_PASSWORD);

        // Get all the configList where vpnPassword equals to UPDATED_VPN_PASSWORD
        defaultConfigShouldNotBeFound("vpnPassword.in=" + UPDATED_VPN_PASSWORD);
    }

    @Test
    @Transactional
    public void getAllConfigsByVpnPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        // Get all the configList where vpnPassword is not null
        defaultConfigShouldBeFound("vpnPassword.specified=true");

        // Get all the configList where vpnPassword is null
        defaultConfigShouldNotBeFound("vpnPassword.specified=false");
    }

    @Test
    @Transactional
    public void getAllConfigsByClientIsEqualToSomething() throws Exception {
        // Get already existing entity
        Client client = config.getClient();
        configRepository.saveAndFlush(config);
        Long clientId = client.getId();

        // Get all the configList where client equals to clientId
        defaultConfigShouldBeFound("clientId.equals=" + clientId);

        // Get all the configList where client equals to clientId + 1
        defaultConfigShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }


    @Test
    @Transactional
    public void getAllConfigsByServeurIsEqualToSomething() throws Exception {
        // Initialize the database
        Serveur serveur = ServeurResourceIT.createEntity(em);
        em.persist(serveur);
        em.flush();
        config.addServeur(serveur);
        configRepository.saveAndFlush(config);
        Long serveurId = serveur.getId();

        // Get all the configList where serveur equals to serveurId
        defaultConfigShouldBeFound("serveurId.equals=" + serveurId);

        // Get all the configList where serveur equals to serveurId + 1
        defaultConfigShouldNotBeFound("serveurId.equals=" + (serveurId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConfigShouldBeFound(String filter) throws Exception {
        restConfigMockMvc.perform(get("/api/configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(config.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamviewerId").value(hasItem(DEFAULT_TEAMVIEWER_ID)))
            .andExpect(jsonPath("$.[*].teamviewerPassword").value(hasItem(DEFAULT_TEAMVIEWER_PASSWORD)))
            .andExpect(jsonPath("$.[*].vpnType").value(hasItem(DEFAULT_VPN_TYPE)))
            .andExpect(jsonPath("$.[*].vpnIp").value(hasItem(DEFAULT_VPN_IP)))
            .andExpect(jsonPath("$.[*].vpnLogin").value(hasItem(DEFAULT_VPN_LOGIN)))
            .andExpect(jsonPath("$.[*].vpnPassword").value(hasItem(DEFAULT_VPN_PASSWORD)));

        // Check, that the count call also returns 1
        restConfigMockMvc.perform(get("/api/configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConfigShouldNotBeFound(String filter) throws Exception {
        restConfigMockMvc.perform(get("/api/configs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConfigMockMvc.perform(get("/api/configs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingConfig() throws Exception {
        // Get the config
        restConfigMockMvc.perform(get("/api/configs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        int databaseSizeBeforeUpdate = configRepository.findAll().size();

        // Update the config
        Config updatedConfig = configRepository.findById(config.getId()).get();
        // Disconnect from session so that the updates on updatedConfig are not directly saved in db
        em.detach(updatedConfig);
        updatedConfig
            .teamviewerId(UPDATED_TEAMVIEWER_ID)
            .teamviewerPassword(UPDATED_TEAMVIEWER_PASSWORD)
            .vpnType(UPDATED_VPN_TYPE)
            .vpnIp(UPDATED_VPN_IP)
            .vpnLogin(UPDATED_VPN_LOGIN)
            .vpnPassword(UPDATED_VPN_PASSWORD);
        ConfigDTO configDTO = configMapper.toDto(updatedConfig);

        restConfigMockMvc.perform(put("/api/configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isOk());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);
        Config testConfig = configList.get(configList.size() - 1);
        assertThat(testConfig.getTeamviewerId()).isEqualTo(UPDATED_TEAMVIEWER_ID);
        assertThat(testConfig.getTeamviewerPassword()).isEqualTo(UPDATED_TEAMVIEWER_PASSWORD);
        assertThat(testConfig.getVpnType()).isEqualTo(UPDATED_VPN_TYPE);
        assertThat(testConfig.getVpnIp()).isEqualTo(UPDATED_VPN_IP);
        assertThat(testConfig.getVpnLogin()).isEqualTo(UPDATED_VPN_LOGIN);
        assertThat(testConfig.getVpnPassword()).isEqualTo(UPDATED_VPN_PASSWORD);

        // Validate the Config in Elasticsearch
        verify(mockConfigSearchRepository, times(1)).save(testConfig);
    }

    @Test
    @Transactional
    public void updateNonExistingConfig() throws Exception {
        int databaseSizeBeforeUpdate = configRepository.findAll().size();

        // Create the Config
        ConfigDTO configDTO = configMapper.toDto(config);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigMockMvc.perform(put("/api/configs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(configDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Config in the database
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Config in Elasticsearch
        verify(mockConfigSearchRepository, times(0)).save(config);
    }

    @Test
    @Transactional
    public void deleteConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);

        int databaseSizeBeforeDelete = configRepository.findAll().size();

        // Delete the config
        restConfigMockMvc.perform(delete("/api/configs/{id}", config.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Config> configList = configRepository.findAll();
        assertThat(configList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Config in Elasticsearch
        verify(mockConfigSearchRepository, times(1)).deleteById(config.getId());
    }

    @Test
    @Transactional
    public void searchConfig() throws Exception {
        // Initialize the database
        configRepository.saveAndFlush(config);
        when(mockConfigSearchRepository.search(queryStringQuery("id:" + config.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(config), PageRequest.of(0, 1), 1));
        // Search the config
        restConfigMockMvc.perform(get("/api/_search/configs?query=id:" + config.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(config.getId().intValue())))
            .andExpect(jsonPath("$.[*].teamviewerId").value(hasItem(DEFAULT_TEAMVIEWER_ID)))
            .andExpect(jsonPath("$.[*].teamviewerPassword").value(hasItem(DEFAULT_TEAMVIEWER_PASSWORD)))
            .andExpect(jsonPath("$.[*].vpnType").value(hasItem(DEFAULT_VPN_TYPE)))
            .andExpect(jsonPath("$.[*].vpnIp").value(hasItem(DEFAULT_VPN_IP)))
            .andExpect(jsonPath("$.[*].vpnLogin").value(hasItem(DEFAULT_VPN_LOGIN)))
            .andExpect(jsonPath("$.[*].vpnPassword").value(hasItem(DEFAULT_VPN_PASSWORD)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Config.class);
        Config config1 = new Config();
        config1.setId(1L);
        Config config2 = new Config();
        config2.setId(config1.getId());
        assertThat(config1).isEqualTo(config2);
        config2.setId(2L);
        assertThat(config1).isNotEqualTo(config2);
        config1.setId(null);
        assertThat(config1).isNotEqualTo(config2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigDTO.class);
        ConfigDTO configDTO1 = new ConfigDTO();
        configDTO1.setId(1L);
        ConfigDTO configDTO2 = new ConfigDTO();
        assertThat(configDTO1).isNotEqualTo(configDTO2);
        configDTO2.setId(configDTO1.getId());
        assertThat(configDTO1).isEqualTo(configDTO2);
        configDTO2.setId(2L);
        assertThat(configDTO1).isNotEqualTo(configDTO2);
        configDTO1.setId(null);
        assertThat(configDTO1).isNotEqualTo(configDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(configMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(configMapper.fromId(null)).isNull();
    }
}
