package com.aractronic.sympo.web.rest;

import com.aractronic.sympo.SympoApp;

import com.aractronic.sympo.domain.Debiteur;
import com.aractronic.sympo.repository.DebiteurRepository;
import com.aractronic.sympo.repository.search.DebiteurSearchRepository;
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
 * Test class for the DebiteurResource REST controller.
 *
 * @see DebiteurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SympoApp.class)
public class DebiteurResourceIntTest {

    private static final String DEFAULT_CODE_DEBITEUR = "AAAAAAAAAA";
    private static final String UPDATED_CODE_DEBITEUR = "BBBBBBBBBB";

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

    private static final String DEFAULT_QUARTIER = "AAAAAAAAAA";
    private static final String UPDATED_QUARTIER = "BBBBBBBBBB";

    private static final String DEFAULT_VILLE = "AAAAAAAAAA";
    private static final String UPDATED_VILLE = "BBBBBBBBBB";

    private static final String DEFAULT_FAX = "AAAAAAAAAA";
    private static final String UPDATED_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_DEBITEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_DEBITEUR = "BBBBBBBBBB";

    private static final String DEFAULT_CIN = "AAAAAAAAAA";
    private static final String UPDATED_CIN = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSION = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_REMARQUE = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUE = "BBBBBBBBBB";

    @Autowired
    private DebiteurRepository debiteurRepository;

    @Autowired
    private DebiteurSearchRepository debiteurSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDebiteurMockMvc;

    private Debiteur debiteur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DebiteurResource debiteurResource = new DebiteurResource(debiteurRepository, debiteurSearchRepository);
        this.restDebiteurMockMvc = MockMvcBuilders.standaloneSetup(debiteurResource)
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
    public static Debiteur createEntity(EntityManager em) {
        Debiteur debiteur = new Debiteur()
            .codeDebiteur(DEFAULT_CODE_DEBITEUR)
            .raisonSocial(DEFAULT_RAISON_SOCIAL)
            .raisonSocialArabe(DEFAULT_RAISON_SOCIAL_ARABE)
            .rc(DEFAULT_RC)
            .patente(DEFAULT_PATENTE)
            .numTelephone(DEFAULT_NUM_TELEPHONE)
            .email(DEFAULT_EMAIL)
            .adresse(DEFAULT_ADRESSE)
            .quartier(DEFAULT_QUARTIER)
            .ville(DEFAULT_VILLE)
            .fax(DEFAULT_FAX)
            .nomDebiteur(DEFAULT_NOM_DEBITEUR)
            .cin(DEFAULT_CIN)
            .profession(DEFAULT_PROFESSION)
            .activated(DEFAULT_ACTIVATED)
            .remarque(DEFAULT_REMARQUE);
        return debiteur;
    }

    @Before
    public void initTest() {
        debiteurSearchRepository.deleteAll();
        debiteur = createEntity(em);
    }

    @Test
    @Transactional
    public void createDebiteur() throws Exception {
        int databaseSizeBeforeCreate = debiteurRepository.findAll().size();

        // Create the Debiteur
        restDebiteurMockMvc.perform(post("/api/debiteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(debiteur)))
            .andExpect(status().isCreated());

        // Validate the Debiteur in the database
        List<Debiteur> debiteurList = debiteurRepository.findAll();
        assertThat(debiteurList).hasSize(databaseSizeBeforeCreate + 1);
        Debiteur testDebiteur = debiteurList.get(debiteurList.size() - 1);
        assertThat(testDebiteur.getCodeDebiteur()).isEqualTo(DEFAULT_CODE_DEBITEUR);
        assertThat(testDebiteur.getRaisonSocial()).isEqualTo(DEFAULT_RAISON_SOCIAL);
        assertThat(testDebiteur.getRaisonSocialArabe()).isEqualTo(DEFAULT_RAISON_SOCIAL_ARABE);
        assertThat(testDebiteur.getRc()).isEqualTo(DEFAULT_RC);
        assertThat(testDebiteur.getPatente()).isEqualTo(DEFAULT_PATENTE);
        assertThat(testDebiteur.getNumTelephone()).isEqualTo(DEFAULT_NUM_TELEPHONE);
        assertThat(testDebiteur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testDebiteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testDebiteur.getQuartier()).isEqualTo(DEFAULT_QUARTIER);
        assertThat(testDebiteur.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testDebiteur.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testDebiteur.getNomDebiteur()).isEqualTo(DEFAULT_NOM_DEBITEUR);
        assertThat(testDebiteur.getCin()).isEqualTo(DEFAULT_CIN);
        assertThat(testDebiteur.getProfession()).isEqualTo(DEFAULT_PROFESSION);
        assertThat(testDebiteur.isActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testDebiteur.getRemarque()).isEqualTo(DEFAULT_REMARQUE);

        // Validate the Debiteur in Elasticsearch
        Debiteur debiteurEs = debiteurSearchRepository.findOne(testDebiteur.getId());
        assertThat(debiteurEs).isEqualToIgnoringGivenFields(testDebiteur);
    }

    @Test
    @Transactional
    public void createDebiteurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = debiteurRepository.findAll().size();

        // Create the Debiteur with an existing ID
        debiteur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDebiteurMockMvc.perform(post("/api/debiteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(debiteur)))
            .andExpect(status().isBadRequest());

        // Validate the Debiteur in the database
        List<Debiteur> debiteurList = debiteurRepository.findAll();
        assertThat(debiteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = debiteurRepository.findAll().size();
        // set the field null
        debiteur.setActivated(null);

        // Create the Debiteur, which fails.

        restDebiteurMockMvc.perform(post("/api/debiteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(debiteur)))
            .andExpect(status().isBadRequest());

        List<Debiteur> debiteurList = debiteurRepository.findAll();
        assertThat(debiteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDebiteurs() throws Exception {
        // Initialize the database
        debiteurRepository.saveAndFlush(debiteur);

        // Get all the debiteurList
        restDebiteurMockMvc.perform(get("/api/debiteurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(debiteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeDebiteur").value(hasItem(DEFAULT_CODE_DEBITEUR.toString())))
            .andExpect(jsonPath("$.[*].raisonSocial").value(hasItem(DEFAULT_RAISON_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].raisonSocialArabe").value(hasItem(DEFAULT_RAISON_SOCIAL_ARABE.toString())))
            .andExpect(jsonPath("$.[*].rc").value(hasItem(DEFAULT_RC.toString())))
            .andExpect(jsonPath("$.[*].patente").value(hasItem(DEFAULT_PATENTE.toString())))
            .andExpect(jsonPath("$.[*].numTelephone").value(hasItem(DEFAULT_NUM_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].quartier").value(hasItem(DEFAULT_QUARTIER.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX.toString())))
            .andExpect(jsonPath("$.[*].nomDebiteur").value(hasItem(DEFAULT_NOM_DEBITEUR.toString())))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN.toString())))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void getDebiteur() throws Exception {
        // Initialize the database
        debiteurRepository.saveAndFlush(debiteur);

        // Get the debiteur
        restDebiteurMockMvc.perform(get("/api/debiteurs/{id}", debiteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(debiteur.getId().intValue()))
            .andExpect(jsonPath("$.codeDebiteur").value(DEFAULT_CODE_DEBITEUR.toString()))
            .andExpect(jsonPath("$.raisonSocial").value(DEFAULT_RAISON_SOCIAL.toString()))
            .andExpect(jsonPath("$.raisonSocialArabe").value(DEFAULT_RAISON_SOCIAL_ARABE.toString()))
            .andExpect(jsonPath("$.rc").value(DEFAULT_RC.toString()))
            .andExpect(jsonPath("$.patente").value(DEFAULT_PATENTE.toString()))
            .andExpect(jsonPath("$.numTelephone").value(DEFAULT_NUM_TELEPHONE.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.adresse").value(DEFAULT_ADRESSE.toString()))
            .andExpect(jsonPath("$.quartier").value(DEFAULT_QUARTIER.toString()))
            .andExpect(jsonPath("$.ville").value(DEFAULT_VILLE.toString()))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX.toString()))
            .andExpect(jsonPath("$.nomDebiteur").value(DEFAULT_NOM_DEBITEUR.toString()))
            .andExpect(jsonPath("$.cin").value(DEFAULT_CIN.toString()))
            .andExpect(jsonPath("$.profession").value(DEFAULT_PROFESSION.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.remarque").value(DEFAULT_REMARQUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDebiteur() throws Exception {
        // Get the debiteur
        restDebiteurMockMvc.perform(get("/api/debiteurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDebiteur() throws Exception {
        // Initialize the database
        debiteurRepository.saveAndFlush(debiteur);
        debiteurSearchRepository.save(debiteur);
        int databaseSizeBeforeUpdate = debiteurRepository.findAll().size();

        // Update the debiteur
        Debiteur updatedDebiteur = debiteurRepository.findOne(debiteur.getId());
        // Disconnect from session so that the updates on updatedDebiteur are not directly saved in db
        em.detach(updatedDebiteur);
        updatedDebiteur
            .codeDebiteur(UPDATED_CODE_DEBITEUR)
            .raisonSocial(UPDATED_RAISON_SOCIAL)
            .raisonSocialArabe(UPDATED_RAISON_SOCIAL_ARABE)
            .rc(UPDATED_RC)
            .patente(UPDATED_PATENTE)
            .numTelephone(UPDATED_NUM_TELEPHONE)
            .email(UPDATED_EMAIL)
            .adresse(UPDATED_ADRESSE)
            .quartier(UPDATED_QUARTIER)
            .ville(UPDATED_VILLE)
            .fax(UPDATED_FAX)
            .nomDebiteur(UPDATED_NOM_DEBITEUR)
            .cin(UPDATED_CIN)
            .profession(UPDATED_PROFESSION)
            .activated(UPDATED_ACTIVATED)
            .remarque(UPDATED_REMARQUE);

        restDebiteurMockMvc.perform(put("/api/debiteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDebiteur)))
            .andExpect(status().isOk());

        // Validate the Debiteur in the database
        List<Debiteur> debiteurList = debiteurRepository.findAll();
        assertThat(debiteurList).hasSize(databaseSizeBeforeUpdate);
        Debiteur testDebiteur = debiteurList.get(debiteurList.size() - 1);
        assertThat(testDebiteur.getCodeDebiteur()).isEqualTo(UPDATED_CODE_DEBITEUR);
        assertThat(testDebiteur.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
        assertThat(testDebiteur.getRaisonSocialArabe()).isEqualTo(UPDATED_RAISON_SOCIAL_ARABE);
        assertThat(testDebiteur.getRc()).isEqualTo(UPDATED_RC);
        assertThat(testDebiteur.getPatente()).isEqualTo(UPDATED_PATENTE);
        assertThat(testDebiteur.getNumTelephone()).isEqualTo(UPDATED_NUM_TELEPHONE);
        assertThat(testDebiteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testDebiteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testDebiteur.getQuartier()).isEqualTo(UPDATED_QUARTIER);
        assertThat(testDebiteur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testDebiteur.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testDebiteur.getNomDebiteur()).isEqualTo(UPDATED_NOM_DEBITEUR);
        assertThat(testDebiteur.getCin()).isEqualTo(UPDATED_CIN);
        assertThat(testDebiteur.getProfession()).isEqualTo(UPDATED_PROFESSION);
        assertThat(testDebiteur.isActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testDebiteur.getRemarque()).isEqualTo(UPDATED_REMARQUE);

        // Validate the Debiteur in Elasticsearch
        Debiteur debiteurEs = debiteurSearchRepository.findOne(testDebiteur.getId());
        assertThat(debiteurEs).isEqualToIgnoringGivenFields(testDebiteur);
    }

    @Test
    @Transactional
    public void updateNonExistingDebiteur() throws Exception {
        int databaseSizeBeforeUpdate = debiteurRepository.findAll().size();

        // Create the Debiteur

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDebiteurMockMvc.perform(put("/api/debiteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(debiteur)))
            .andExpect(status().isCreated());

        // Validate the Debiteur in the database
        List<Debiteur> debiteurList = debiteurRepository.findAll();
        assertThat(debiteurList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDebiteur() throws Exception {
        // Initialize the database
        debiteurRepository.saveAndFlush(debiteur);
        debiteurSearchRepository.save(debiteur);
        int databaseSizeBeforeDelete = debiteurRepository.findAll().size();

        // Get the debiteur
        restDebiteurMockMvc.perform(delete("/api/debiteurs/{id}", debiteur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean debiteurExistsInEs = debiteurSearchRepository.exists(debiteur.getId());
        assertThat(debiteurExistsInEs).isFalse();

        // Validate the database is empty
        List<Debiteur> debiteurList = debiteurRepository.findAll();
        assertThat(debiteurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDebiteur() throws Exception {
        // Initialize the database
        debiteurRepository.saveAndFlush(debiteur);
        debiteurSearchRepository.save(debiteur);

        // Search the debiteur
        restDebiteurMockMvc.perform(get("/api/_search/debiteurs?query=id:" + debiteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(debiteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeDebiteur").value(hasItem(DEFAULT_CODE_DEBITEUR.toString())))
            .andExpect(jsonPath("$.[*].raisonSocial").value(hasItem(DEFAULT_RAISON_SOCIAL.toString())))
            .andExpect(jsonPath("$.[*].raisonSocialArabe").value(hasItem(DEFAULT_RAISON_SOCIAL_ARABE.toString())))
            .andExpect(jsonPath("$.[*].rc").value(hasItem(DEFAULT_RC.toString())))
            .andExpect(jsonPath("$.[*].patente").value(hasItem(DEFAULT_PATENTE.toString())))
            .andExpect(jsonPath("$.[*].numTelephone").value(hasItem(DEFAULT_NUM_TELEPHONE.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].adresse").value(hasItem(DEFAULT_ADRESSE.toString())))
            .andExpect(jsonPath("$.[*].quartier").value(hasItem(DEFAULT_QUARTIER.toString())))
            .andExpect(jsonPath("$.[*].ville").value(hasItem(DEFAULT_VILLE.toString())))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX.toString())))
            .andExpect(jsonPath("$.[*].nomDebiteur").value(hasItem(DEFAULT_NOM_DEBITEUR.toString())))
            .andExpect(jsonPath("$.[*].cin").value(hasItem(DEFAULT_CIN.toString())))
            .andExpect(jsonPath("$.[*].profession").value(hasItem(DEFAULT_PROFESSION.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Debiteur.class);
        Debiteur debiteur1 = new Debiteur();
        debiteur1.setId(1L);
        Debiteur debiteur2 = new Debiteur();
        debiteur2.setId(debiteur1.getId());
        assertThat(debiteur1).isEqualTo(debiteur2);
        debiteur2.setId(2L);
        assertThat(debiteur1).isNotEqualTo(debiteur2);
        debiteur1.setId(null);
        assertThat(debiteur1).isNotEqualTo(debiteur2);
    }
}
