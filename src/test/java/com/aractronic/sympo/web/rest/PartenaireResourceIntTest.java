package com.aractronic.sympo.web.rest;

import com.aractronic.sympo.SympoApp;

import com.aractronic.sympo.domain.Partenaire;
import com.aractronic.sympo.repository.PartenaireRepository;
import com.aractronic.sympo.repository.search.PartenaireSearchRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.aractronic.sympo.web.rest.TestUtil.sameInstant;
import static com.aractronic.sympo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PartenaireResource REST controller.
 *
 * @see PartenaireResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SympoApp.class)
public class PartenaireResourceIntTest {

    private static final String DEFAULT_NOM_PARTENAIRE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_PARTENAIRE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_RESET_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RESET_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private PartenaireRepository partenaireRepository;

    @Autowired
    private PartenaireSearchRepository partenaireSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPartenaireMockMvc;

    private Partenaire partenaire;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PartenaireResource partenaireResource = new PartenaireResource(partenaireRepository, partenaireSearchRepository);
        this.restPartenaireMockMvc = MockMvcBuilders.standaloneSetup(partenaireResource)
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
    public static Partenaire createEntity(EntityManager em) {
        Partenaire partenaire = new Partenaire()
            .nomPartenaire(DEFAULT_NOM_PARTENAIRE)
            .resetDate(DEFAULT_RESET_DATE);
        return partenaire;
    }

    @Before
    public void initTest() {
        partenaireSearchRepository.deleteAll();
        partenaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createPartenaire() throws Exception {
        int databaseSizeBeforeCreate = partenaireRepository.findAll().size();

        // Create the Partenaire
        restPartenaireMockMvc.perform(post("/api/partenaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partenaire)))
            .andExpect(status().isCreated());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeCreate + 1);
        Partenaire testPartenaire = partenaireList.get(partenaireList.size() - 1);
        assertThat(testPartenaire.getNomPartenaire()).isEqualTo(DEFAULT_NOM_PARTENAIRE);
        assertThat(testPartenaire.getResetDate()).isEqualTo(DEFAULT_RESET_DATE);

        // Validate the Partenaire in Elasticsearch
        Partenaire partenaireEs = partenaireSearchRepository.findOne(testPartenaire.getId());
        assertThat(testPartenaire.getResetDate()).isEqualTo(testPartenaire.getResetDate());
        assertThat(partenaireEs).isEqualToIgnoringGivenFields(testPartenaire, "resetDate");
    }

    @Test
    @Transactional
    public void createPartenaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = partenaireRepository.findAll().size();

        // Create the Partenaire with an existing ID
        partenaire.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPartenaireMockMvc.perform(post("/api/partenaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partenaire)))
            .andExpect(status().isBadRequest());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNomPartenaireIsRequired() throws Exception {
        int databaseSizeBeforeTest = partenaireRepository.findAll().size();
        // set the field null
        partenaire.setNomPartenaire(null);

        // Create the Partenaire, which fails.

        restPartenaireMockMvc.perform(post("/api/partenaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partenaire)))
            .andExpect(status().isBadRequest());

        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPartenaires() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get all the partenaireList
        restPartenaireMockMvc.perform(get("/api/partenaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partenaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPartenaire").value(hasItem(DEFAULT_NOM_PARTENAIRE.toString())))
            .andExpect(jsonPath("$.[*].resetDate").value(hasItem(sameInstant(DEFAULT_RESET_DATE))));
    }

    @Test
    @Transactional
    public void getPartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);

        // Get the partenaire
        restPartenaireMockMvc.perform(get("/api/partenaires/{id}", partenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(partenaire.getId().intValue()))
            .andExpect(jsonPath("$.nomPartenaire").value(DEFAULT_NOM_PARTENAIRE.toString()))
            .andExpect(jsonPath("$.resetDate").value(sameInstant(DEFAULT_RESET_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingPartenaire() throws Exception {
        // Get the partenaire
        restPartenaireMockMvc.perform(get("/api/partenaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);
        partenaireSearchRepository.save(partenaire);
        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();

        // Update the partenaire
        Partenaire updatedPartenaire = partenaireRepository.findOne(partenaire.getId());
        // Disconnect from session so that the updates on updatedPartenaire are not directly saved in db
        em.detach(updatedPartenaire);
        updatedPartenaire
            .nomPartenaire(UPDATED_NOM_PARTENAIRE)
            .resetDate(UPDATED_RESET_DATE);

        restPartenaireMockMvc.perform(put("/api/partenaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPartenaire)))
            .andExpect(status().isOk());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate);
        Partenaire testPartenaire = partenaireList.get(partenaireList.size() - 1);
        assertThat(testPartenaire.getNomPartenaire()).isEqualTo(UPDATED_NOM_PARTENAIRE);
        assertThat(testPartenaire.getResetDate()).isEqualTo(UPDATED_RESET_DATE);

        // Validate the Partenaire in Elasticsearch
        Partenaire partenaireEs = partenaireSearchRepository.findOne(testPartenaire.getId());
        assertThat(testPartenaire.getResetDate()).isEqualTo(testPartenaire.getResetDate());
        assertThat(partenaireEs).isEqualToIgnoringGivenFields(testPartenaire, "resetDate");
    }

    @Test
    @Transactional
    public void updateNonExistingPartenaire() throws Exception {
        int databaseSizeBeforeUpdate = partenaireRepository.findAll().size();

        // Create the Partenaire

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPartenaireMockMvc.perform(put("/api/partenaires")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(partenaire)))
            .andExpect(status().isCreated());

        // Validate the Partenaire in the database
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);
        partenaireSearchRepository.save(partenaire);
        int databaseSizeBeforeDelete = partenaireRepository.findAll().size();

        // Get the partenaire
        restPartenaireMockMvc.perform(delete("/api/partenaires/{id}", partenaire.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean partenaireExistsInEs = partenaireSearchRepository.exists(partenaire.getId());
        assertThat(partenaireExistsInEs).isFalse();

        // Validate the database is empty
        List<Partenaire> partenaireList = partenaireRepository.findAll();
        assertThat(partenaireList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPartenaire() throws Exception {
        // Initialize the database
        partenaireRepository.saveAndFlush(partenaire);
        partenaireSearchRepository.save(partenaire);

        // Search the partenaire
        restPartenaireMockMvc.perform(get("/api/_search/partenaires?query=id:" + partenaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(partenaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomPartenaire").value(hasItem(DEFAULT_NOM_PARTENAIRE.toString())))
            .andExpect(jsonPath("$.[*].resetDate").value(hasItem(sameInstant(DEFAULT_RESET_DATE))));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Partenaire.class);
        Partenaire partenaire1 = new Partenaire();
        partenaire1.setId(1L);
        Partenaire partenaire2 = new Partenaire();
        partenaire2.setId(partenaire1.getId());
        assertThat(partenaire1).isEqualTo(partenaire2);
        partenaire2.setId(2L);
        assertThat(partenaire1).isNotEqualTo(partenaire2);
        partenaire1.setId(null);
        assertThat(partenaire1).isNotEqualTo(partenaire2);
    }
}
