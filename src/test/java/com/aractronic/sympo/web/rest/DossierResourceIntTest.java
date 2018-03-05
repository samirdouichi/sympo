package com.aractronic.sympo.web.rest;

import com.aractronic.sympo.SympoApp;

import com.aractronic.sympo.domain.Dossier;
import com.aractronic.sympo.repository.DossierRepository;
import com.aractronic.sympo.repository.search.DossierSearchRepository;
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

import com.aractronic.sympo.domain.enumeration.EtatDossier;
/**
 * Test class for the DossierResource REST controller.
 *
 * @see DossierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SympoApp.class)
public class DossierResourceIntTest {

    private static final String DEFAULT_NUM_DOSSIER = "AAAAAAAAAA";
    private static final String UPDATED_NUM_DOSSIER = "BBBBBBBBBB";

    private static final EtatDossier DEFAULT_STATUS = EtatDossier.ENCOURS;
    private static final EtatDossier UPDATED_STATUS = EtatDossier.ANNULE;

    private static final String DEFAULT_NOM_DOSSIER = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
    private static final String UPDATED_NOM_DOSSIER = "BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_UPDATED_AT = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_UPDATED_AT = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_CLOTURE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CLOTURE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REMARQUE = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUE = "BBBBBBBBBB";

    @Autowired
    private DossierRepository dossierRepository;

    @Autowired
    private DossierSearchRepository dossierSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDossierMockMvc;

    private Dossier dossier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DossierResource dossierResource = new DossierResource(dossierRepository, dossierSearchRepository);
        this.restDossierMockMvc = MockMvcBuilders.standaloneSetup(dossierResource)
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
    public static Dossier createEntity(EntityManager em) {
        Dossier dossier = new Dossier()
            .numDossier(DEFAULT_NUM_DOSSIER)
            .status(DEFAULT_STATUS)
            .nomDossier(DEFAULT_NOM_DOSSIER)
            .dateCreation(DEFAULT_DATE_CREATION)
            .updatedAt(DEFAULT_UPDATED_AT)
            .dateCloture(DEFAULT_DATE_CLOTURE)
            .remarque(DEFAULT_REMARQUE);
        return dossier;
    }

    @Before
    public void initTest() {
        dossierSearchRepository.deleteAll();
        dossier = createEntity(em);
    }

    @Test
    @Transactional
    public void createDossier() throws Exception {
        int databaseSizeBeforeCreate = dossierRepository.findAll().size();

        // Create the Dossier
        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isCreated());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate + 1);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNumDossier()).isEqualTo(DEFAULT_NUM_DOSSIER);
        assertThat(testDossier.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testDossier.getNomDossier()).isEqualTo(DEFAULT_NOM_DOSSIER);
        assertThat(testDossier.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testDossier.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testDossier.getDateCloture()).isEqualTo(DEFAULT_DATE_CLOTURE);
        assertThat(testDossier.getRemarque()).isEqualTo(DEFAULT_REMARQUE);

        // Validate the Dossier in Elasticsearch
        Dossier dossierEs = dossierSearchRepository.findOne(testDossier.getId());
        assertThat(dossierEs).isEqualToIgnoringGivenFields(testDossier);
    }

    @Test
    @Transactional
    public void createDossierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dossierRepository.findAll().size();

        // Create the Dossier with an existing ID
        dossier.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isBadRequest());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumDossierIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setNumDossier(null);

        // Create the Dossier, which fails.

        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isBadRequest());

        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNomDossierIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setNomDossier(null);

        // Create the Dossier, which fails.

        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isBadRequest());

        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setDateCreation(null);

        // Create the Dossier, which fails.

        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isBadRequest());

        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkUpdatedAtIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierRepository.findAll().size();
        // set the field null
        dossier.setUpdatedAt(null);

        // Create the Dossier, which fails.

        restDossierMockMvc.perform(post("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossier)))
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
            .andExpect(jsonPath("$.[*].numDossier").value(hasItem(DEFAULT_NUM_DOSSIER.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].nomDossier").value(hasItem(DEFAULT_NOM_DOSSIER.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].dateCloture").value(hasItem(DEFAULT_DATE_CLOTURE.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
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
            .andExpect(jsonPath("$.numDossier").value(DEFAULT_NUM_DOSSIER.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.nomDossier").value(DEFAULT_NOM_DOSSIER.toString()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()))
            .andExpect(jsonPath("$.dateCloture").value(DEFAULT_DATE_CLOTURE.toString()))
            .andExpect(jsonPath("$.remarque").value(DEFAULT_REMARQUE.toString()));
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
        dossierSearchRepository.save(dossier);
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Update the dossier
        Dossier updatedDossier = dossierRepository.findOne(dossier.getId());
        // Disconnect from session so that the updates on updatedDossier are not directly saved in db
        em.detach(updatedDossier);
        updatedDossier
            .numDossier(UPDATED_NUM_DOSSIER)
            .status(UPDATED_STATUS)
            .nomDossier(UPDATED_NOM_DOSSIER)
            .dateCreation(UPDATED_DATE_CREATION)
            .updatedAt(UPDATED_UPDATED_AT)
            .dateCloture(UPDATED_DATE_CLOTURE)
            .remarque(UPDATED_REMARQUE);

        restDossierMockMvc.perform(put("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDossier)))
            .andExpect(status().isOk());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate);
        Dossier testDossier = dossierList.get(dossierList.size() - 1);
        assertThat(testDossier.getNumDossier()).isEqualTo(UPDATED_NUM_DOSSIER);
        assertThat(testDossier.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testDossier.getNomDossier()).isEqualTo(UPDATED_NOM_DOSSIER);
        assertThat(testDossier.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testDossier.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testDossier.getDateCloture()).isEqualTo(UPDATED_DATE_CLOTURE);
        assertThat(testDossier.getRemarque()).isEqualTo(UPDATED_REMARQUE);

        // Validate the Dossier in Elasticsearch
        Dossier dossierEs = dossierSearchRepository.findOne(testDossier.getId());
        assertThat(dossierEs).isEqualToIgnoringGivenFields(testDossier);
    }

    @Test
    @Transactional
    public void updateNonExistingDossier() throws Exception {
        int databaseSizeBeforeUpdate = dossierRepository.findAll().size();

        // Create the Dossier

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDossierMockMvc.perform(put("/api/dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossier)))
            .andExpect(status().isCreated());

        // Validate the Dossier in the database
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);
        dossierSearchRepository.save(dossier);
        int databaseSizeBeforeDelete = dossierRepository.findAll().size();

        // Get the dossier
        restDossierMockMvc.perform(delete("/api/dossiers/{id}", dossier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dossierExistsInEs = dossierSearchRepository.exists(dossier.getId());
        assertThat(dossierExistsInEs).isFalse();

        // Validate the database is empty
        List<Dossier> dossierList = dossierRepository.findAll();
        assertThat(dossierList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDossier() throws Exception {
        // Initialize the database
        dossierRepository.saveAndFlush(dossier);
        dossierSearchRepository.save(dossier);

        // Search the dossier
        restDossierMockMvc.perform(get("/api/_search/dossiers?query=id:" + dossier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossier.getId().intValue())))
            .andExpect(jsonPath("$.[*].numDossier").value(hasItem(DEFAULT_NUM_DOSSIER.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].nomDossier").value(hasItem(DEFAULT_NOM_DOSSIER.toString())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())))
            .andExpect(jsonPath("$.[*].dateCloture").value(hasItem(DEFAULT_DATE_CLOTURE.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
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
}
