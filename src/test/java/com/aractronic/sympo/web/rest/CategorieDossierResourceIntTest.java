package com.aractronic.sympo.web.rest;

import com.aractronic.sympo.SympoApp;

import com.aractronic.sympo.domain.CategorieDossier;
import com.aractronic.sympo.repository.CategorieDossierRepository;
import com.aractronic.sympo.repository.search.CategorieDossierSearchRepository;
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
import java.util.List;

import static com.aractronic.sympo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CategorieDossierResource REST controller.
 *
 * @see CategorieDossierResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SympoApp.class)
public class CategorieDossierResourceIntTest {

    private static final String DEFAULT_NUM_CAT_DOSSIER = "AAAAAA";
    private static final String UPDATED_NUM_CAT_DOSSIER = "BBBBBB";

    private static final String DEFAULT_NOM_CAT_DOSSIER = "AAAAAA";
    private static final String UPDATED_NOM_CAT_DOSSIER = "BBBBBB";

    private static final String DEFAULT_REMARQUE = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUE = "BBBBBBBBBB";

    @Autowired
    private CategorieDossierRepository categorieDossierRepository;

    @Autowired
    private CategorieDossierSearchRepository categorieDossierSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCategorieDossierMockMvc;

    private CategorieDossier categorieDossier;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CategorieDossierResource categorieDossierResource = new CategorieDossierResource(categorieDossierRepository, categorieDossierSearchRepository);
        this.restCategorieDossierMockMvc = MockMvcBuilders.standaloneSetup(categorieDossierResource)
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
    public static CategorieDossier createEntity(EntityManager em) {
        CategorieDossier categorieDossier = new CategorieDossier()
            .numCatDossier(DEFAULT_NUM_CAT_DOSSIER)
            .nomCatDossier(DEFAULT_NOM_CAT_DOSSIER)
            .remarque(DEFAULT_REMARQUE);
        return categorieDossier;
    }

    @Before
    public void initTest() {
        categorieDossierSearchRepository.deleteAll();
        categorieDossier = createEntity(em);
    }

    @Test
    @Transactional
    public void createCategorieDossier() throws Exception {
        int databaseSizeBeforeCreate = categorieDossierRepository.findAll().size();

        // Create the CategorieDossier
        restCategorieDossierMockMvc.perform(post("/api/categorie-dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categorieDossier)))
            .andExpect(status().isCreated());

        // Validate the CategorieDossier in the database
        List<CategorieDossier> categorieDossierList = categorieDossierRepository.findAll();
        assertThat(categorieDossierList).hasSize(databaseSizeBeforeCreate + 1);
        CategorieDossier testCategorieDossier = categorieDossierList.get(categorieDossierList.size() - 1);
        assertThat(testCategorieDossier.getNumCatDossier()).isEqualTo(DEFAULT_NUM_CAT_DOSSIER);
        assertThat(testCategorieDossier.getNomCatDossier()).isEqualTo(DEFAULT_NOM_CAT_DOSSIER);
        assertThat(testCategorieDossier.getRemarque()).isEqualTo(DEFAULT_REMARQUE);

        // Validate the CategorieDossier in Elasticsearch
        CategorieDossier categorieDossierEs = categorieDossierSearchRepository.findOne(testCategorieDossier.getId());
        assertThat(categorieDossierEs).isEqualToIgnoringGivenFields(testCategorieDossier);
    }

    @Test
    @Transactional
    public void createCategorieDossierWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = categorieDossierRepository.findAll().size();

        // Create the CategorieDossier with an existing ID
        categorieDossier.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategorieDossierMockMvc.perform(post("/api/categorie-dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categorieDossier)))
            .andExpect(status().isBadRequest());

        // Validate the CategorieDossier in the database
        List<CategorieDossier> categorieDossierList = categorieDossierRepository.findAll();
        assertThat(categorieDossierList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumCatDossierIsRequired() throws Exception {
        int databaseSizeBeforeTest = categorieDossierRepository.findAll().size();
        // set the field null
        categorieDossier.setNumCatDossier(null);

        // Create the CategorieDossier, which fails.

        restCategorieDossierMockMvc.perform(post("/api/categorie-dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categorieDossier)))
            .andExpect(status().isBadRequest());

        List<CategorieDossier> categorieDossierList = categorieDossierRepository.findAll();
        assertThat(categorieDossierList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCategorieDossiers() throws Exception {
        // Initialize the database
        categorieDossierRepository.saveAndFlush(categorieDossier);

        // Get all the categorieDossierList
        restCategorieDossierMockMvc.perform(get("/api/categorie-dossiers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorieDossier.getId().intValue())))
            .andExpect(jsonPath("$.[*].numCatDossier").value(hasItem(DEFAULT_NUM_CAT_DOSSIER.toString())))
            .andExpect(jsonPath("$.[*].nomCatDossier").value(hasItem(DEFAULT_NOM_CAT_DOSSIER.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void getCategorieDossier() throws Exception {
        // Initialize the database
        categorieDossierRepository.saveAndFlush(categorieDossier);

        // Get the categorieDossier
        restCategorieDossierMockMvc.perform(get("/api/categorie-dossiers/{id}", categorieDossier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(categorieDossier.getId().intValue()))
            .andExpect(jsonPath("$.numCatDossier").value(DEFAULT_NUM_CAT_DOSSIER.toString()))
            .andExpect(jsonPath("$.nomCatDossier").value(DEFAULT_NOM_CAT_DOSSIER.toString()))
            .andExpect(jsonPath("$.remarque").value(DEFAULT_REMARQUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCategorieDossier() throws Exception {
        // Get the categorieDossier
        restCategorieDossierMockMvc.perform(get("/api/categorie-dossiers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCategorieDossier() throws Exception {
        // Initialize the database
        categorieDossierRepository.saveAndFlush(categorieDossier);
        categorieDossierSearchRepository.save(categorieDossier);
        int databaseSizeBeforeUpdate = categorieDossierRepository.findAll().size();

        // Update the categorieDossier
        CategorieDossier updatedCategorieDossier = categorieDossierRepository.findOne(categorieDossier.getId());
        // Disconnect from session so that the updates on updatedCategorieDossier are not directly saved in db
        em.detach(updatedCategorieDossier);
        updatedCategorieDossier
            .numCatDossier(UPDATED_NUM_CAT_DOSSIER)
            .nomCatDossier(UPDATED_NOM_CAT_DOSSIER)
            .remarque(UPDATED_REMARQUE);

        restCategorieDossierMockMvc.perform(put("/api/categorie-dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCategorieDossier)))
            .andExpect(status().isOk());

        // Validate the CategorieDossier in the database
        List<CategorieDossier> categorieDossierList = categorieDossierRepository.findAll();
        assertThat(categorieDossierList).hasSize(databaseSizeBeforeUpdate);
        CategorieDossier testCategorieDossier = categorieDossierList.get(categorieDossierList.size() - 1);
        assertThat(testCategorieDossier.getNumCatDossier()).isEqualTo(UPDATED_NUM_CAT_DOSSIER);
        assertThat(testCategorieDossier.getNomCatDossier()).isEqualTo(UPDATED_NOM_CAT_DOSSIER);
        assertThat(testCategorieDossier.getRemarque()).isEqualTo(UPDATED_REMARQUE);

        // Validate the CategorieDossier in Elasticsearch
        CategorieDossier categorieDossierEs = categorieDossierSearchRepository.findOne(testCategorieDossier.getId());
        assertThat(categorieDossierEs).isEqualToIgnoringGivenFields(testCategorieDossier);
    }

    @Test
    @Transactional
    public void updateNonExistingCategorieDossier() throws Exception {
        int databaseSizeBeforeUpdate = categorieDossierRepository.findAll().size();

        // Create the CategorieDossier

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCategorieDossierMockMvc.perform(put("/api/categorie-dossiers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(categorieDossier)))
            .andExpect(status().isCreated());

        // Validate the CategorieDossier in the database
        List<CategorieDossier> categorieDossierList = categorieDossierRepository.findAll();
        assertThat(categorieDossierList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCategorieDossier() throws Exception {
        // Initialize the database
        categorieDossierRepository.saveAndFlush(categorieDossier);
        categorieDossierSearchRepository.save(categorieDossier);
        int databaseSizeBeforeDelete = categorieDossierRepository.findAll().size();

        // Get the categorieDossier
        restCategorieDossierMockMvc.perform(delete("/api/categorie-dossiers/{id}", categorieDossier.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean categorieDossierExistsInEs = categorieDossierSearchRepository.exists(categorieDossier.getId());
        assertThat(categorieDossierExistsInEs).isFalse();

        // Validate the database is empty
        List<CategorieDossier> categorieDossierList = categorieDossierRepository.findAll();
        assertThat(categorieDossierList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCategorieDossier() throws Exception {
        // Initialize the database
        categorieDossierRepository.saveAndFlush(categorieDossier);
        categorieDossierSearchRepository.save(categorieDossier);

        // Search the categorieDossier
        restCategorieDossierMockMvc.perform(get("/api/_search/categorie-dossiers?query=id:" + categorieDossier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categorieDossier.getId().intValue())))
            .andExpect(jsonPath("$.[*].numCatDossier").value(hasItem(DEFAULT_NUM_CAT_DOSSIER.toString())))
            .andExpect(jsonPath("$.[*].nomCatDossier").value(hasItem(DEFAULT_NOM_CAT_DOSSIER.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategorieDossier.class);
        CategorieDossier categorieDossier1 = new CategorieDossier();
        categorieDossier1.setId(1L);
        CategorieDossier categorieDossier2 = new CategorieDossier();
        categorieDossier2.setId(categorieDossier1.getId());
        assertThat(categorieDossier1).isEqualTo(categorieDossier2);
        categorieDossier2.setId(2L);
        assertThat(categorieDossier1).isNotEqualTo(categorieDossier2);
        categorieDossier1.setId(null);
        assertThat(categorieDossier1).isNotEqualTo(categorieDossier2);
    }
}
