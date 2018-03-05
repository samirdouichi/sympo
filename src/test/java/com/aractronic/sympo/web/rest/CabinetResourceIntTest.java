package com.aractronic.sympo.web.rest;

import com.aractronic.sympo.SympoApp;

import com.aractronic.sympo.domain.Cabinet;
import com.aractronic.sympo.repository.CabinetRepository;
import com.aractronic.sympo.repository.search.CabinetSearchRepository;
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

/**
 * Test class for the CabinetResource REST controller.
 *
 * @see CabinetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SympoApp.class)
public class CabinetResourceIntTest {

    private static final String DEFAULT_RAISON_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_RAISON_SOCIAL_ARABE = "AAAAAAAAAA";
    private static final String UPDATED_RAISON_SOCIAL_ARABE = "BBBBBBBBBB";

    private static final String DEFAULT_RC = "AAAAAAAAAA";
    private static final String UPDATED_RC = "BBBBBBBBBB";

    private static final String DEFAULT_PATENTE = "AAAAAAAAAA";
    private static final String UPDATED_PATENTE = "BBBBBBBBBB";

    private static final String DEFAULT_NUM_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_NUM_TELEPHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESSE = "AAAAAAAAAA";
    private static final String UPDATED_ADRESSE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_GERANT = "AAAAAAAAAA";
    private static final String UPDATED_NOM_GERANT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final LocalDate DEFAULT_DATE_CREATION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REMARQUE = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUE = "BBBBBBBBBB";

    @Autowired
    private CabinetRepository cabinetRepository;

    @Autowired
    private CabinetSearchRepository cabinetSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCabinetMockMvc;

    private Cabinet cabinet;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CabinetResource cabinetResource = new CabinetResource(cabinetRepository, cabinetSearchRepository);
        this.restCabinetMockMvc = MockMvcBuilders.standaloneSetup(cabinetResource)
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
    public static Cabinet createEntity(EntityManager em) {
        Cabinet cabinet = new Cabinet()
            .raisonSocial(DEFAULT_RAISON_SOCIAL)
            .raisonSocialArabe(DEFAULT_RAISON_SOCIAL_ARABE)
            .rc(DEFAULT_RC)
            .patente(DEFAULT_PATENTE)
            .numTelephone(DEFAULT_NUM_TELEPHONE)
            .email(DEFAULT_EMAIL)
            .adresse(DEFAULT_ADRESSE)
            .nomGerant(DEFAULT_NOM_GERANT)
            .activated(DEFAULT_ACTIVATED)
            .dateCreation(DEFAULT_DATE_CREATION)
            .remarque(DEFAULT_REMARQUE);
        return cabinet;
    }

    @Before
    public void initTest() {
        cabinetSearchRepository.deleteAll();
        cabinet = createEntity(em);
    }

    @Test
    @Transactional
    public void createCabinet() throws Exception {
        int databaseSizeBeforeCreate = cabinetRepository.findAll().size();

        // Create the Cabinet
        restCabinetMockMvc.perform(post("/api/cabinets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isCreated());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeCreate + 1);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getRaisonSocial()).isEqualTo(DEFAULT_RAISON_SOCIAL);
        assertThat(testCabinet.getRaisonSocialArabe()).isEqualTo(DEFAULT_RAISON_SOCIAL_ARABE);
        assertThat(testCabinet.getRc()).isEqualTo(DEFAULT_RC);
        assertThat(testCabinet.getPatente()).isEqualTo(DEFAULT_PATENTE);
        assertThat(testCabinet.getNumTelephone()).isEqualTo(DEFAULT_NUM_TELEPHONE);
        assertThat(testCabinet.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCabinet.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testCabinet.getNomGerant()).isEqualTo(DEFAULT_NOM_GERANT);
        assertThat(testCabinet.isActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testCabinet.getDateCreation()).isEqualTo(DEFAULT_DATE_CREATION);
        assertThat(testCabinet.getRemarque()).isEqualTo(DEFAULT_REMARQUE);

        // Validate the Cabinet in Elasticsearch
        Cabinet cabinetEs = cabinetSearchRepository.findOne(testCabinet.getId());
        assertThat(cabinetEs).isEqualToIgnoringGivenFields(testCabinet);
    }

    @Test
    @Transactional
    public void createCabinetWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cabinetRepository.findAll().size();

        // Create the Cabinet with an existing ID
        cabinet.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCabinetMockMvc.perform(post("/api/cabinets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isBadRequest());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = cabinetRepository.findAll().size();
        // set the field null
        cabinet.setActivated(null);

        // Create the Cabinet, which fails.

        restCabinetMockMvc.perform(post("/api/cabinets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isBadRequest());

        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateCreationIsRequired() throws Exception {
        int databaseSizeBeforeTest = cabinetRepository.findAll().size();
        // set the field null
        cabinet.setDateCreation(null);

        // Create the Cabinet, which fails.

        restCabinetMockMvc.perform(post("/api/cabinets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isBadRequest());

        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCabinets() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        // Get all the cabinetList
        restCabinetMockMvc.perform(get("/api/cabinets?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cabinet.getId().intValue())))
            .andExpect(jsonPath("$.[*].raisonSocial").value(hasItem(DEFAULT_RAISON_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].raisonSocialArabe").value(hasItem(DEFAULT_RAISON_SOCIAL_ARABE.toString())))
            .andExpect(jsonPath("$.[*].rc").value(hasItem(DEFAULT_RC.toString())))
            .andExpect(jsonPath("$.[*].patente").value(hasItem(DEFAULT_PATENTE.toString())))
            .andExpect(jsonPath("$.[*].numTelephone").value(hasItem(DEFAULT_NUM_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].nomGerant").value(hasItem(DEFAULT_NOM_GERANT.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void getCabinet() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);

        // Get the cabinet
        restCabinetMockMvc.perform(get("/api/cabinets/{id}", cabinet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cabinet.getId().intValue()))
            .andExpect(jsonPath("$.raisonSocial").value(DEFAULT_RAISON_SOCIAL.toString()))
            .andExpect(jsonPath("$.raisonSocialArabe").value(DEFAULT_RAISON_SOCIAL_ARABE.toString()))
            .andExpect(jsonPath("$.rc").value(DEFAULT_RC.toString()))
            .andExpect(jsonPath("$.patente").value(DEFAULT_PATENTE.toString()))
            .andExpect(jsonPath("$.numTelephone").value(DEFAULT_NUM_TELEPHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.nomGerant").value(DEFAULT_NOM_GERANT.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()))
            .andExpect(jsonPath("$.remarque").value(DEFAULT_REMARQUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCabinet() throws Exception {
        // Get the cabinet
        restCabinetMockMvc.perform(get("/api/cabinets/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCabinet() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);
        cabinetSearchRepository.save(cabinet);
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();

        // Update the cabinet
        Cabinet updatedCabinet = cabinetRepository.findOne(cabinet.getId());
        // Disconnect from session so that the updates on updatedCabinet are not directly saved in db
        em.detach(updatedCabinet);
        updatedCabinet
            .raisonSocial(UPDATED_RAISON_SOCIAL)
            .raisonSocialArabe(UPDATED_RAISON_SOCIAL_ARABE)
            .rc(UPDATED_RC)
            .patente(UPDATED_PATENTE)
            .numTelephone(UPDATED_NUM_TELEPHONE)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .nomGerant(UPDATED_NOM_GERANT)
            .activated(UPDATED_ACTIVATED)
            .dateCreation(UPDATED_DATE_CREATION)
            .remarque(UPDATED_REMARQUE);

        restCabinetMockMvc.perform(put("/api/cabinets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCabinet)))
            .andExpect(status().isOk());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate);
        Cabinet testCabinet = cabinetList.get(cabinetList.size() - 1);
        assertThat(testCabinet.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
        assertThat(testCabinet.getRaisonSocialArabe()).isEqualTo(UPDATED_RAISON_SOCIAL_ARABE);
        assertThat(testCabinet.getRc()).isEqualTo(UPDATED_RC);
        assertThat(testCabinet.getPatente()).isEqualTo(UPDATED_PATENTE);
        assertThat(testCabinet.getNumTelephone()).isEqualTo(UPDATED_NUM_TELEPHONE);
        assertThat(testCabinet.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCabinet.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testCabinet.getNomGerant()).isEqualTo(UPDATED_NOM_GERANT);
        assertThat(testCabinet.isActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testCabinet.getDateCreation()).isEqualTo(UPDATED_DATE_CREATION);
        assertThat(testCabinet.getRemarque()).isEqualTo(UPDATED_REMARQUE);

        // Validate the Cabinet in Elasticsearch
        Cabinet cabinetEs = cabinetSearchRepository.findOne(testCabinet.getId());
        assertThat(cabinetEs).isEqualToIgnoringGivenFields(testCabinet);
    }

    @Test
    @Transactional
    public void updateNonExistingCabinet() throws Exception {
        int databaseSizeBeforeUpdate = cabinetRepository.findAll().size();

        // Create the Cabinet

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCabinetMockMvc.perform(put("/api/cabinets")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cabinet)))
            .andExpect(status().isCreated());

        // Validate the Cabinet in the database
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCabinet() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);
        cabinetSearchRepository.save(cabinet);
        int databaseSizeBeforeDelete = cabinetRepository.findAll().size();

        // Get the cabinet
        restCabinetMockMvc.perform(delete("/api/cabinets/{id}", cabinet.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean cabinetExistsInEs = cabinetSearchRepository.exists(cabinet.getId());
        assertThat(cabinetExistsInEs).isFalse();

        // Validate the database is empty
        List<Cabinet> cabinetList = cabinetRepository.findAll();
        assertThat(cabinetList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCabinet() throws Exception {
        // Initialize the database
        cabinetRepository.saveAndFlush(cabinet);
        cabinetSearchRepository.save(cabinet);

        // Search the cabinet
        restCabinetMockMvc.perform(get("/api/_search/cabinets?query=id:" + cabinet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cabinet.getId().intValue())))
            .andExpect(jsonPath("$.[*].raisonSocial").value(hasItem(DEFAULT_RAISON_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].raisonSocialArabe").value(hasItem(DEFAULT_RAISON_SOCIAL_ARABE.toString())))
            .andExpect(jsonPath("$.[*].rc").value(hasItem(DEFAULT_RC.toString())))
            .andExpect(jsonPath("$.[*].patente").value(hasItem(DEFAULT_PATENTE.toString())))
            .andExpect(jsonPath("$.[*].numTelephone").value(hasItem(DEFAULT_NUM_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].nomGerant").value(hasItem(DEFAULT_NOM_GERANT.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cabinet.class);
        Cabinet cabinet1 = new Cabinet();
        cabinet1.setId(1L);
        Cabinet cabinet2 = new Cabinet();
        cabinet2.setId(cabinet1.getId());
        assertThat(cabinet1).isEqualTo(cabinet2);
        cabinet2.setId(2L);
        assertThat(cabinet1).isNotEqualTo(cabinet2);
        cabinet1.setId(null);
        assertThat(cabinet1).isNotEqualTo(cabinet2);
    }
}
