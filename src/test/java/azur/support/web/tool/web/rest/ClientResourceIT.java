package azur.support.web.tool.web.rest;

import azur.support.web.tool.AzursupportwebtoolApp;
import azur.support.web.tool.domain.Client;
import azur.support.web.tool.domain.Config;
import azur.support.web.tool.domain.Dossier;
import azur.support.web.tool.repository.ClientRepository;
import azur.support.web.tool.repository.search.ClientSearchRepository;
import azur.support.web.tool.service.ClientService;
import azur.support.web.tool.service.dto.ClientDTO;
import azur.support.web.tool.service.mapper.ClientMapper;
import azur.support.web.tool.web.rest.errors.ExceptionTranslator;
import azur.support.web.tool.service.dto.ClientCriteria;
import azur.support.web.tool.service.ClientQueryService;

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
 * Integration tests for the {@Link ClientResource} REST controller.
 */
@SpringBootTest(classes = AzursupportwebtoolApp.class)
public class ClientResourceIT {

    private static final String DEFAULT_RAISON_SOCIALE = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIALE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    private static final String DEFAULT_TEL = "AAAAAAAAAA";
    private static final String UPDATED_TEL = "BBBBBBBBBB";

    private static final String DEFAULT_MOBILE = "AAAAAAAAAA";
    private static final String UPDATED_MOBILE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private ClientService clientService;

    /**
     * This repository is mocked in the azur.support.web.tool.repository.search test package.
     *
     * @see azur.support.web.tool.repository.search.ClientSearchRepositoryMockConfiguration
     */
    @Autowired
    private ClientSearchRepository mockClientSearchRepository;

    @Autowired
    private ClientQueryService clientQueryService;

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

    private MockMvc restClientMockMvc;

    private Client client;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClientResource clientResource = new ClientResource(clientService, clientQueryService);
        this.restClientMockMvc = MockMvcBuilders.standaloneSetup(clientResource)
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
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
            .raisonSociale(DEFAULT_RAISON_SOCIALE)
            .contact(DEFAULT_CONTACT)
            .tel(DEFAULT_TEL)
            .mobile(DEFAULT_MOBILE)
            .email(DEFAULT_EMAIL);
        return client;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity(EntityManager em) {
        Client client = new Client()
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .contact(UPDATED_CONTACT)
            .tel(UPDATED_TEL)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL);
        return client;
    }

    @BeforeEach
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    public void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);
        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getRaisonSociale()).isEqualTo(DEFAULT_RAISON_SOCIALE);
        assertThat(testClient.getContact()).isEqualTo(DEFAULT_CONTACT);
        assertThat(testClient.getTel()).isEqualTo(DEFAULT_TEL);
        assertThat(testClient.getMobile()).isEqualTo(DEFAULT_MOBILE);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);

        // Validate the Client in Elasticsearch
        verify(mockClientSearchRepository, times(1)).save(testClient);
    }

    @Test
    @Transactional
    public void createClientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // Create the Client with an existing ID
        client.setId(1L);
        ClientDTO clientDTO = clientMapper.toDto(client);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);

        // Validate the Client in Elasticsearch
        verify(mockClientSearchRepository, times(0)).save(client);
    }


    @Test
    @Transactional
    public void checkRaisonSocialeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setRaisonSociale(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkContactIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setContact(null);

        // Create the Client, which fails.
        ClientDTO clientDTO = clientMapper.toDto(client);

        restClientMockMvc.perform(post("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc.perform(get("/api/clients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].raisonSociale").value(hasItem(DEFAULT_RAISON_SOCIALE.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL.toString())))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())));
    }
    
    @Test
    @Transactional
    public void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.raisonSociale").value(DEFAULT_RAISON_SOCIALE.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()))
            .andExpect(jsonPath("$.tel").value(DEFAULT_TEL.toString()))
            .andExpect(jsonPath("$.mobile").value(DEFAULT_MOBILE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()));
    }

    @Test
    @Transactional
    public void getAllClientsByRaisonSocialeIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where raisonSociale equals to DEFAULT_RAISON_SOCIALE
        defaultClientShouldBeFound("raisonSociale.equals=" + DEFAULT_RAISON_SOCIALE);

        // Get all the clientList where raisonSociale equals to UPDATED_RAISON_SOCIALE
        defaultClientShouldNotBeFound("raisonSociale.equals=" + UPDATED_RAISON_SOCIALE);
    }

    @Test
    @Transactional
    public void getAllClientsByRaisonSocialeIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where raisonSociale in DEFAULT_RAISON_SOCIALE or UPDATED_RAISON_SOCIALE
        defaultClientShouldBeFound("raisonSociale.in=" + DEFAULT_RAISON_SOCIALE + "," + UPDATED_RAISON_SOCIALE);

        // Get all the clientList where raisonSociale equals to UPDATED_RAISON_SOCIALE
        defaultClientShouldNotBeFound("raisonSociale.in=" + UPDATED_RAISON_SOCIALE);
    }

    @Test
    @Transactional
    public void getAllClientsByRaisonSocialeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where raisonSociale is not null
        defaultClientShouldBeFound("raisonSociale.specified=true");

        // Get all the clientList where raisonSociale is null
        defaultClientShouldNotBeFound("raisonSociale.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where contact equals to DEFAULT_CONTACT
        defaultClientShouldBeFound("contact.equals=" + DEFAULT_CONTACT);

        // Get all the clientList where contact equals to UPDATED_CONTACT
        defaultClientShouldNotBeFound("contact.equals=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void getAllClientsByContactIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where contact in DEFAULT_CONTACT or UPDATED_CONTACT
        defaultClientShouldBeFound("contact.in=" + DEFAULT_CONTACT + "," + UPDATED_CONTACT);

        // Get all the clientList where contact equals to UPDATED_CONTACT
        defaultClientShouldNotBeFound("contact.in=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void getAllClientsByContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where contact is not null
        defaultClientShouldBeFound("contact.specified=true");

        // Get all the clientList where contact is null
        defaultClientShouldNotBeFound("contact.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByTelIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where tel equals to DEFAULT_TEL
        defaultClientShouldBeFound("tel.equals=" + DEFAULT_TEL);

        // Get all the clientList where tel equals to UPDATED_TEL
        defaultClientShouldNotBeFound("tel.equals=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    public void getAllClientsByTelIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where tel in DEFAULT_TEL or UPDATED_TEL
        defaultClientShouldBeFound("tel.in=" + DEFAULT_TEL + "," + UPDATED_TEL);

        // Get all the clientList where tel equals to UPDATED_TEL
        defaultClientShouldNotBeFound("tel.in=" + UPDATED_TEL);
    }

    @Test
    @Transactional
    public void getAllClientsByTelIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where tel is not null
        defaultClientShouldBeFound("tel.specified=true");

        // Get all the clientList where tel is null
        defaultClientShouldNotBeFound("tel.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByMobileIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where mobile equals to DEFAULT_MOBILE
        defaultClientShouldBeFound("mobile.equals=" + DEFAULT_MOBILE);

        // Get all the clientList where mobile equals to UPDATED_MOBILE
        defaultClientShouldNotBeFound("mobile.equals=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllClientsByMobileIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where mobile in DEFAULT_MOBILE or UPDATED_MOBILE
        defaultClientShouldBeFound("mobile.in=" + DEFAULT_MOBILE + "," + UPDATED_MOBILE);

        // Get all the clientList where mobile equals to UPDATED_MOBILE
        defaultClientShouldNotBeFound("mobile.in=" + UPDATED_MOBILE);
    }

    @Test
    @Transactional
    public void getAllClientsByMobileIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where mobile is not null
        defaultClientShouldBeFound("mobile.specified=true");

        // Get all the clientList where mobile is null
        defaultClientShouldNotBeFound("mobile.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email equals to DEFAULT_EMAIL
        defaultClientShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the clientList where email equals to UPDATED_EMAIL
        defaultClientShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllClientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultClientShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the clientList where email equals to UPDATED_EMAIL
        defaultClientShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllClientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email is not null
        defaultClientShouldBeFound("email.specified=true");

        // Get all the clientList where email is null
        defaultClientShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllClientsByConfigIsEqualToSomething() throws Exception {
        // Initialize the database
        Config config = ConfigResourceIT.createEntity(em);
        em.persist(config);
        em.flush();
        client.addConfig(config);
        clientRepository.saveAndFlush(client);
        Long configId = config.getId();

        // Get all the clientList where config equals to configId
        defaultClientShouldBeFound("configId.equals=" + configId);

        // Get all the clientList where config equals to configId + 1
        defaultClientShouldNotBeFound("configId.equals=" + (configId + 1));
    }


    @Test
    @Transactional
    public void getAllClientsByDossierIsEqualToSomething() throws Exception {
        // Initialize the database
        Dossier dossier = DossierResourceIT.createEntity(em);
        em.persist(dossier);
        em.flush();
        client.addDossier(dossier);
        clientRepository.saveAndFlush(client);
        Long dossierId = dossier.getId();

        // Get all the clientList where dossier equals to dossierId
        defaultClientShouldBeFound("dossierId.equals=" + dossierId);

        // Get all the clientList where dossier equals to dossierId + 1
        defaultClientShouldNotBeFound("dossierId.equals=" + (dossierId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc.perform(get("/api/clients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].raisonSociale").value(hasItem(DEFAULT_RAISON_SOCIALE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restClientMockMvc.perform(get("/api/clients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc.perform(get("/api/clients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc.perform(get("/api/clients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get("/api/clients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).get();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .raisonSociale(UPDATED_RAISON_SOCIALE)
            .contact(UPDATED_CONTACT)
            .tel(UPDATED_TEL)
            .mobile(UPDATED_MOBILE)
            .email(UPDATED_EMAIL);
        ClientDTO clientDTO = clientMapper.toDto(updatedClient);

        restClientMockMvc.perform(put("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getRaisonSociale()).isEqualTo(UPDATED_RAISON_SOCIALE);
        assertThat(testClient.getContact()).isEqualTo(UPDATED_CONTACT);
        assertThat(testClient.getTel()).isEqualTo(UPDATED_TEL);
        assertThat(testClient.getMobile()).isEqualTo(UPDATED_MOBILE);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);

        // Validate the Client in Elasticsearch
        verify(mockClientSearchRepository, times(1)).save(testClient);
    }

    @Test
    @Transactional
    public void updateNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Create the Client
        ClientDTO clientDTO = clientMapper.toDto(client);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc.perform(put("/api/clients")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clientDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Client in Elasticsearch
        verify(mockClientSearchRepository, times(0)).save(client);
    }

    @Test
    @Transactional
    public void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Delete the client
        restClientMockMvc.perform(delete("/api/clients/{id}", client.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database is empty
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Client in Elasticsearch
        verify(mockClientSearchRepository, times(1)).deleteById(client.getId());
    }

    @Test
    @Transactional
    public void searchClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        when(mockClientSearchRepository.search(queryStringQuery("id:" + client.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(client), PageRequest.of(0, 1), 1));
        // Search the client
        restClientMockMvc.perform(get("/api/_search/clients?query=id:" + client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].raisonSociale").value(hasItem(DEFAULT_RAISON_SOCIALE)))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT)))
            .andExpect(jsonPath("$.[*].tel").value(hasItem(DEFAULT_TEL)))
            .andExpect(jsonPath("$.[*].mobile").value(hasItem(DEFAULT_MOBILE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = new Client();
        client1.setId(1L);
        Client client2 = new Client();
        client2.setId(client1.getId());
        assertThat(client1).isEqualTo(client2);
        client2.setId(2L);
        assertThat(client1).isNotEqualTo(client2);
        client1.setId(null);
        assertThat(client1).isNotEqualTo(client2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientDTO.class);
        ClientDTO clientDTO1 = new ClientDTO();
        clientDTO1.setId(1L);
        ClientDTO clientDTO2 = new ClientDTO();
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
        clientDTO2.setId(clientDTO1.getId());
        assertThat(clientDTO1).isEqualTo(clientDTO2);
        clientDTO2.setId(2L);
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
        clientDTO1.setId(null);
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(clientMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(clientMapper.fromId(null)).isNull();
    }
}
