package com.aractronic.sympo.web.rest;

import com.aractronic.sympo.SympoApp;

import com.aractronic.sympo.domain.Creance;
import com.aractronic.sympo.repository.CreanceRepository;
import com.aractronic.sympo.repository.search.CreanceSearchRepository;
import com.aractronic.sympo.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.aractronic.sympo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.aractronic.sympo.domain.enumeration.CreancesType;
/**
 * Test class for the CreanceResource REST controller.
 *
 * @see CreanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SympoApp.class)
public class CreanceResourceIntTest {

    private static final String DEFAULT_NUM_CREANCE = "AAAAAA";
    private static final String UPDATED_NUM_CREANCE = "BBBBBB";

    private static final String DEFAULT_NOM_CREANCE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CREANCE = "BBBBBBBBBB";

    private static final CreancesType DEFAULT_CREANCES_TYPE = CreancesType.FACTURE;
    private static final CreancesType UPDATED_CREANCES_TYPE = CreancesType.CHEQUE;

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REMARQUE = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUE = "BBBBBBBBBB";

    @Autowired
    private CreanceRepository creanceRepository;

    @Autowired
    private CreanceSearchRepository creanceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCreanceMockMvc;

    private Creance creance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CreanceResource creanceResource = new CreanceResource(creanceRepository, creanceSearchRepository);
        this.restCreanceMockMvc = MockMvcBuilders.standaloneSetup(creanceResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Creance createEntity(EntityManager em) {
        Creance creance = new Creance()
            .numCreance(DEFAULT_NUM_CREANCE)
            .nomCreance(DEFAULT_NOM_CREANCE)
            .creancesType(DEFAULT_CREANCES_TYPE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .remarque(DEFAULT_REMARQUE);
        return creance;
    }

    @Before
    public void initTest() {
        creanceSearchRepository.deleteAll();
        creance = createEntity(em);
    }

    @Test
    @Transactional
    public void createCreance() throws Exception {
        int databaseSizeBeforeCreate = creanceRepository.findAll().size();

        // Create the Creance
        restCreanceMockMvc.perform(post("/api/creances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creance)))
            .andExpect(status().isCreated());

        // Validate the Creance in the database
        List<Creance> creanceList = creanceRepository.findAll();
        assertThat(creanceList).hasSize(databaseSizeBeforeCreate + 1);
        Creance testCreance = creanceList.get(creanceList.size() - 1);
        assertThat(testCreance.getNumCreance()).isEqualTo(DEFAULT_NUM_CREANCE);
        assertThat(testCreance.getNomCreance()).isEqualTo(DEFAULT_NOM_CREANCE);
        assertThat(testCreance.getCreancesType()).isEqualTo(DEFAULT_CREANCES_TYPE);
        assertThat(testCreance.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCreance.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testCreance.getRemarque()).isEqualTo(DEFAULT_REMARQUE);

        // Validate the Creance in Elasticsearch
        Creance creanceEs = creanceSearchRepository.findOne(testCreance.getId());
        assertThat(creanceEs).isEqualToIgnoringGivenFields(testCreance);
    }

    @Test
    @Transactional
    public void createCreanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = creanceRepository.findAll().size();

        // Create the Creance with an existing ID
        creance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCreanceMockMvc.perform(post("/api/creances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creance)))
            .andExpect(status().isBadRequest());

        // Validate the Creance in the database
        List<Creance> creanceList = creanceRepository.findAll();
        assertThat(creanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumCreanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = creanceRepository.findAll().size();
        // set the field null
        creance.setNumCreance(null);

        // Create the Creance, which fails.

        restCreanceMockMvc.perform(post("/api/creances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creance)))
            .andExpect(status().isBadRequest());

        List<Creance> creanceList = creanceRepository.findAll();
        assertThat(creanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomCreanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = creanceRepository.findAll().size();
        // set the field null
        creance.setNomCreance(null);

        // Create the Creance, which fails.

        restCreanceMockMvc.perform(post("/api/creances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creance)))
            .andExpect(status().isBadRequest());

        List<Creance> creanceList = creanceRepository.findAll();
        assertThat(creanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = creanceRepository.findAll().size();
        // set the field null
        creance.setCreatedAt(null);

        // Create the Creance, which fails.

        restCreanceMockMvc.perform(post("/api/creances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creance)))
            .andExpect(status().isBadRequest());

        List<Creance> creanceList = creanceRepository.findAll();
        assertThat(creanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = creanceRepository.findAll().size();
        // set the field null
        creance.setUpdatedAt(null);

        // Create the Creance, which fails.

        restCreanceMockMvc.perform(post("/api/creances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creance)))
            .andExpect(status().isBadRequest());

        List<Creance> creanceList = creanceRepository.findAll();
        assertThat(creanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCreances() throws Exception {
        // Initialize the database
        creanceRepository.saveAndFlush(creance);

        // Get all the creanceList
        restCreanceMockMvc.perform(get("/api/creances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creance.getId().intValue())))
            .andExpect(jsonPath("$.[*].numCreance").value(hasItem(DEFAULT_NUM_CREANCE.toString())))
            .andExpect(jsonPath("$.[*].nomCreance").value(hasItem(DEFAULT_NOM_CREANCE.toString())))
            .andExpect(jsonPath("$.[*].creancesType").value(hasItem(DEFAULT_CREANCES_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void getCreance() throws Exception {
        // Initialize the database
        creanceRepository.saveAndFlush(creance);

        // Get the creance
        restCreanceMockMvc.perform(get("/api/creances/{id}", creance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(creance.getId().intValue()))
            .andExpect(jsonPath("$.numCreance").value(DEFAULT_NUM_CREANCE.toString()))
            .andExpect(jsonPath("$.nomCreance").value(DEFAULT_NOM_CREANCE.toString()))
            .andExpect(jsonPath("$.creancesType").value(DEFAULT_CREANCES_TYPE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.remarque").value(DEFAULT_REMARQUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCreance() throws Exception {
        // Get the creance
        restCreanceMockMvc.perform(get("/api/creances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCreance() throws Exception {
        // Initialize the database
        creanceRepository.saveAndFlush(creance);
        creanceSearchRepository.save(creance);
        int databaseSizeBeforeUpdate = creanceRepository.findAll().size();

        // Update the creance
        Creance updatedCreance = creanceRepository.findOne(creance.getId());
        // Disconnect from session so that the updates on updatedCreance are not directly saved in db
        em.detach(updatedCreance);
        updatedCreance
            .numCreance(UPDATED_NUM_CREANCE)
            .nomCreance(UPDATED_NOM_CREANCE)
            .creancesType(UPDATED_CREANCES_TYPE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .remarque(UPDATED_REMARQUE);

        restCreanceMockMvc.perform(put("/api/creances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCreance)))
            .andExpect(status().isOk());

        // Validate the Creance in the database
        List<Creance> creanceList = creanceRepository.findAll();
        assertThat(creanceList).hasSize(databaseSizeBeforeUpdate);
        Creance testCreance = creanceList.get(creanceList.size() - 1);
        assertThat(testCreance.getNumCreance()).isEqualTo(UPDATED_NUM_CREANCE);
        assertThat(testCreance.getNomCreance()).isEqualTo(UPDATED_NOM_CREANCE);
        assertThat(testCreance.getCreancesType()).isEqualTo(UPDATED_CREANCES_TYPE);
        assertThat(testCreance.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCreance.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testCreance.getRemarque()).isEqualTo(UPDATED_REMARQUE);

        // Validate the Creance in Elasticsearch
        Creance creanceEs = creanceSearchRepository.findOne(testCreance.getId());
        assertThat(creanceEs).isEqualToIgnoringGivenFields(testCreance);
    }

    @Test
    @Transactional
    public void updateNonExistingCreance() throws Exception {
        int databaseSizeBeforeUpdate = creanceRepository.findAll().size();

        // Create the Creance

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCreanceMockMvc.perform(put("/api/creances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(creance)))
            .andExpect(status().isCreated());

        // Validate the Creance in the database
        List<Creance> creanceList = creanceRepository.findAll();
        assertThat(creanceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCreance() throws Exception {
        // Initialize the database
        creanceRepository.saveAndFlush(creance);
        creanceSearchRepository.save(creance);
        int databaseSizeBeforeDelete = creanceRepository.findAll().size();

        // Get the creance
        restCreanceMockMvc.perform(delete("/api/creances/{id}", creance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean creanceExistsInEs = creanceSearchRepository.exists(creance.getId());
        assertThat(creanceExistsInEs).isFalse();

        // Validate the database is empty
        List<Creance> creanceList = creanceRepository.findAll();
        assertThat(creanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCreance() throws Exception {
        // Initialize the database
        creanceRepository.saveAndFlush(creance);
        creanceSearchRepository.save(creance);

        // Search the creance
        restCreanceMockMvc.perform(get("/api/_search/creances?query=id:" + creance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(creance.getId().intValue())))
            .andExpect(jsonPath("$.[*].numCreance").value(hasItem(DEFAULT_NUM_CREANCE.toString())))
            .andExpect(jsonPath("$.[*].nomCreance").value(hasItem(DEFAULT_NOM_CREANCE.toString())))
            .andExpect(jsonPath("$.[*].creancesType").value(hasItem(DEFAULT_CREANCES_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Creance.class);
        Creance creance1 = new Creance();
        creance1.setId(1L);
        Creance creance2 = new Creance();
        creance2.setId(creance1.getId());
        assertThat(creance1).isEqualTo(creance2);
        creance2.setId(2L);
        assertThat(creance1).isNotEqualTo(creance2);
        creance1.setId(null);
        assertThat(creance1).isNotEqualTo(creance2);
    }
}
