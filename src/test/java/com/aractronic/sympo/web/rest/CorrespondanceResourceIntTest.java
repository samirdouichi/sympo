package com.aractronic.sympo.web.rest;

import com.aractronic.sympo.SympoApp;

import com.aractronic.sympo.domain.Correspondance;
import com.aractronic.sympo.repository.CorrespondanceRepository;
import com.aractronic.sympo.repository.search.CorrespondanceSearchRepository;
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

import com.aractronic.sympo.domain.enumeration.EtatCorrespondance;
/**
 * Test class for the CorrespondanceResource REST controller.
 *
 * @see CorrespondanceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SympoApp.class)
public class CorrespondanceResourceIntTest {

    private static final String DEFAULT_CORRESPONDANCE_NOM = "AAAAAAAAAA";
    private static final String UPDATED_CORRESPONDANCE_NOM = "BBBBBBBBBB";

    private static final EtatCorrespondance DEFAULT_ETAT_CORRESPONDANCE = EtatCorrespondance.MED;
    private static final EtatCorrespondance UPDATED_ETAT_CORRESPONDANCE = EtatCorrespondance.MAKAL;

    private static final String DEFAULT_REMARQUE = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private CorrespondanceRepository correspondanceRepository;

    @Autowired
    private CorrespondanceSearchRepository correspondanceSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCorrespondanceMockMvc;

    private Correspondance correspondance;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CorrespondanceResource correspondanceResource = new CorrespondanceResource(correspondanceRepository, correspondanceSearchRepository);
        this.restCorrespondanceMockMvc = MockMvcBuilders.standaloneSetup(correspondanceResource)
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
    public static Correspondance createEntity(EntityManager em) {
        Correspondance correspondance = new Correspondance()
            .correspondanceNom(DEFAULT_CORRESPONDANCE_NOM)
            .etatCorrespondance(DEFAULT_ETAT_CORRESPONDANCE)
            .remarque(DEFAULT_REMARQUE)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
        return correspondance;
    }

    @Before
    public void initTest() {
        correspondanceSearchRepository.deleteAll();
        correspondance = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorrespondance() throws Exception {
        int databaseSizeBeforeCreate = correspondanceRepository.findAll().size();

        // Create the Correspondance
        restCorrespondanceMockMvc.perform(post("/api/correspondances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondance)))
            .andExpect(status().isCreated());

        // Validate the Correspondance in the database
        List<Correspondance> correspondanceList = correspondanceRepository.findAll();
        assertThat(correspondanceList).hasSize(databaseSizeBeforeCreate + 1);
        Correspondance testCorrespondance = correspondanceList.get(correspondanceList.size() - 1);
        assertThat(testCorrespondance.getCorrespondanceNom()).isEqualTo(DEFAULT_CORRESPONDANCE_NOM);
        assertThat(testCorrespondance.getEtatCorrespondance()).isEqualTo(DEFAULT_ETAT_CORRESPONDANCE);
        assertThat(testCorrespondance.getRemarque()).isEqualTo(DEFAULT_REMARQUE);
        assertThat(testCorrespondance.getCreatedAt()).isEqualTo(DEFAULT_CREATED_AT);
        assertThat(testCorrespondance.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);

        // Validate the Correspondance in Elasticsearch
        Correspondance correspondanceEs = correspondanceSearchRepository.findOne(testCorrespondance.getId());
        assertThat(correspondanceEs).isEqualToIgnoringGivenFields(testCorrespondance);
    }

    @Test
    @Transactional
    public void createCorrespondanceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = correspondanceRepository.findAll().size();

        // Create the Correspondance with an existing ID
        correspondance.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorrespondanceMockMvc.perform(post("/api/correspondances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondance)))
            .andExpect(status().isBadRequest());

        // Validate the Correspondance in the database
        List<Correspondance> correspondanceList = correspondanceRepository.findAll();
        assertThat(correspondanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCorrespondanceNomIsRequired() throws Exception {
        int databaseSizeBeforeTest = correspondanceRepository.findAll().size();
        // set the field null
        correspondance.setCorrespondanceNom(null);

        // Create the Correspondance, which fails.

        restCorrespondanceMockMvc.perform(post("/api/correspondances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondance)))
            .andExpect(status().isBadRequest());

        List<Correspondance> correspondanceList = correspondanceRepository.findAll();
        assertThat(correspondanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = correspondanceRepository.findAll().size();
        // set the field null
        correspondance.setCreatedAt(null);

        // Create the Correspondance, which fails.

        restCorrespondanceMockMvc.perform(post("/api/correspondances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondance)))
            .andExpect(status().isBadRequest());

        List<Correspondance> correspondanceList = correspondanceRepository.findAll();
        assertThat(correspondanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = correspondanceRepository.findAll().size();
        // set the field null
        correspondance.setUpdatedAt(null);

        // Create the Correspondance, which fails.

        restCorrespondanceMockMvc.perform(post("/api/correspondances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondance)))
            .andExpect(status().isBadRequest());

        List<Correspondance> correspondanceList = correspondanceRepository.findAll();
        assertThat(correspondanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCorrespondances() throws Exception {
        // Initialize the database
        correspondanceRepository.saveAndFlush(correspondance);

        // Get all the correspondanceList
        restCorrespondanceMockMvc.perform(get("/api/correspondances?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correspondance.getId().intValue())))
            .andExpect(jsonPath("$.[*].correspondanceNom").value(hasItem(DEFAULT_CORRESPONDANCE_NOM.toString())))
            .andExpect(jsonPath("$.[*].etatCorrespondance").value(hasItem(DEFAULT_ETAT_CORRESPONDANCE.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    public void getCorrespondance() throws Exception {
        // Initialize the database
        correspondanceRepository.saveAndFlush(correspondance);

        // Get the correspondance
        restCorrespondanceMockMvc.perform(get("/api/correspondances/{id}", correspondance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(correspondance.getId().intValue()))
            .andExpect(jsonPath("$.correspondanceNom").value(DEFAULT_CORRESPONDANCE_NOM.toString()))
            .andExpect(jsonPath("$.etatCorrespondance").value(DEFAULT_ETAT_CORRESPONDANCE.toString()))
            .andExpect(jsonPath("$.remarque").value(DEFAULT_REMARQUE.toString()))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCorrespondance() throws Exception {
        // Get the correspondance
        restCorrespondanceMockMvc.perform(get("/api/correspondances/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorrespondance() throws Exception {
        // Initialize the database
        correspondanceRepository.saveAndFlush(correspondance);
        correspondanceSearchRepository.save(correspondance);
        int databaseSizeBeforeUpdate = correspondanceRepository.findAll().size();

        // Update the correspondance
        Correspondance updatedCorrespondance = correspondanceRepository.findOne(correspondance.getId());
        // Disconnect from session so that the updates on updatedCorrespondance are not directly saved in db
        em.detach(updatedCorrespondance);
        updatedCorrespondance
            .correspondanceNom(UPDATED_CORRESPONDANCE_NOM)
            .etatCorrespondance(UPDATED_ETAT_CORRESPONDANCE)
            .remarque(UPDATED_REMARQUE)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restCorrespondanceMockMvc.perform(put("/api/correspondances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCorrespondance)))
            .andExpect(status().isOk());

        // Validate the Correspondance in the database
        List<Correspondance> correspondanceList = correspondanceRepository.findAll();
        assertThat(correspondanceList).hasSize(databaseSizeBeforeUpdate);
        Correspondance testCorrespondance = correspondanceList.get(correspondanceList.size() - 1);
        assertThat(testCorrespondance.getCorrespondanceNom()).isEqualTo(UPDATED_CORRESPONDANCE_NOM);
        assertThat(testCorrespondance.getEtatCorrespondance()).isEqualTo(UPDATED_ETAT_CORRESPONDANCE);
        assertThat(testCorrespondance.getRemarque()).isEqualTo(UPDATED_REMARQUE);
        assertThat(testCorrespondance.getCreatedAt()).isEqualTo(UPDATED_CREATED_AT);
        assertThat(testCorrespondance.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);

        // Validate the Correspondance in Elasticsearch
        Correspondance correspondanceEs = correspondanceSearchRepository.findOne(testCorrespondance.getId());
        assertThat(correspondanceEs).isEqualToIgnoringGivenFields(testCorrespondance);
    }

    @Test
    @Transactional
    public void updateNonExistingCorrespondance() throws Exception {
        int databaseSizeBeforeUpdate = correspondanceRepository.findAll().size();

        // Create the Correspondance

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCorrespondanceMockMvc.perform(put("/api/correspondances")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondance)))
            .andExpect(status().isCreated());

        // Validate the Correspondance in the database
        List<Correspondance> correspondanceList = correspondanceRepository.findAll();
        assertThat(correspondanceList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCorrespondance() throws Exception {
        // Initialize the database
        correspondanceRepository.saveAndFlush(correspondance);
        correspondanceSearchRepository.save(correspondance);
        int databaseSizeBeforeDelete = correspondanceRepository.findAll().size();

        // Get the correspondance
        restCorrespondanceMockMvc.perform(delete("/api/correspondances/{id}", correspondance.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean correspondanceExistsInEs = correspondanceSearchRepository.exists(correspondance.getId());
        assertThat(correspondanceExistsInEs).isFalse();

        // Validate the database is empty
        List<Correspondance> correspondanceList = correspondanceRepository.findAll();
        assertThat(correspondanceList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCorrespondance() throws Exception {
        // Initialize the database
        correspondanceRepository.saveAndFlush(correspondance);
        correspondanceSearchRepository.save(correspondance);

        // Search the correspondance
        restCorrespondanceMockMvc.perform(get("/api/_search/correspondances?query=id:" + correspondance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correspondance.getId().intValue())))
            .andExpect(jsonPath("$.[*].correspondanceNom").value(hasItem(DEFAULT_CORRESPONDANCE_NOM.toString())))
            .andExpect(jsonPath("$.[*].etatCorrespondance").value(hasItem(DEFAULT_ETAT_CORRESPONDANCE.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Correspondance.class);
        Correspondance correspondance1 = new Correspondance();
        correspondance1.setId(1L);
        Correspondance correspondance2 = new Correspondance();
        correspondance2.setId(correspondance1.getId());
        assertThat(correspondance1).isEqualTo(correspondance2);
        correspondance2.setId(2L);
        assertThat(correspondance1).isNotEqualTo(correspondance2);
        correspondance1.setId(null);
        assertThat(correspondance1).isNotEqualTo(correspondance2);
    }
}
