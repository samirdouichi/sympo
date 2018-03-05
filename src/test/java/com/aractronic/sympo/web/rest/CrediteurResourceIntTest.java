package com.aractronic.sympo.web.rest;

import com.aractronic.sympo.SympoApp;

import com.aractronic.sympo.domain.Crediteur;
import com.aractronic.sympo.repository.CrediteurRepository;
import com.aractronic.sympo.repository.search.CrediteurSearchRepository;
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
 * Test class for the CrediteurResource REST controller.
 *
 * @see CrediteurResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SympoApp.class)
public class CrediteurResourceIntTest {

    private static final String DEFAULT_CODE_CREDITEUR = "AAAAAAAAAA";
    private static final String UPDATED_CODE_CREDITEUR = "BBBBBBBBBB";

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

    private static final String DEFAULT_NOM_CREDITEUR = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CREDITEUR = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM_CREDITEUR = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM_CREDITEUR = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVATED = false;
    private static final Boolean UPDATED_ACTIVATED = true;

    private static final String DEFAULT_REMARQUE = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUE = "BBBBBBBBBB";

    @Autowired
    private CrediteurRepository crediteurRepository;

    @Autowired
    private CrediteurSearchRepository crediteurSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCrediteurMockMvc;

    private Crediteur crediteur;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CrediteurResource crediteurResource = new CrediteurResource(crediteurRepository, crediteurSearchRepository);
        this.restCrediteurMockMvc = MockMvcBuilders.standaloneSetup(crediteurResource)
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
    public static Crediteur createEntity(EntityManager em) {
        Crediteur crediteur = new Crediteur()
            .codeCrediteur(DEFAULT_CODE_CREDITEUR)
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
            .nomCrediteur(DEFAULT_NOM_CREDITEUR)
            .prenomCrediteur(DEFAULT_PRENOM_CREDITEUR)
            .activated(DEFAULT_ACTIVATED)
            .remarque(DEFAULT_REMARQUE);
        return crediteur;
    }

    @Before
    public void initTest() {
        crediteurSearchRepository.deleteAll();
        crediteur = createEntity(em);
    }

    @Test
    @Transactional
    public void createCrediteur() throws Exception {
        int databaseSizeBeforeCreate = crediteurRepository.findAll().size();

        // Create the Crediteur
        restCrediteurMockMvc.perform(post("/api/crediteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crediteur)))
            .andExpect(status().isCreated());

        // Validate the Crediteur in the database
        List<Crediteur> crediteurList = crediteurRepository.findAll();
        assertThat(crediteurList).hasSize(databaseSizeBeforeCreate + 1);
        Crediteur testCrediteur = crediteurList.get(crediteurList.size() - 1);
        assertThat(testCrediteur.getCodeCrediteur()).isEqualTo(DEFAULT_CODE_CREDITEUR);
        assertThat(testCrediteur.getRaisonSocial()).isEqualTo(DEFAULT_RAISON_SOCIAL);
        assertThat(testCrediteur.getRaisonSocialArabe()).isEqualTo(DEFAULT_RAISON_SOCIAL_ARABE);
        assertThat(testCrediteur.getRc()).isEqualTo(DEFAULT_RC);
        assertThat(testCrediteur.getPatente()).isEqualTo(DEFAULT_PATENTE);
        assertThat(testCrediteur.getNumTelephone()).isEqualTo(DEFAULT_NUM_TELEPHONE);
        assertThat(testCrediteur.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCrediteur.getAdresse()).isEqualTo(DEFAULT_ADRESSE);
        assertThat(testCrediteur.getQuartier()).isEqualTo(DEFAULT_QUARTIER);
        assertThat(testCrediteur.getVille()).isEqualTo(DEFAULT_VILLE);
        assertThat(testCrediteur.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testCrediteur.getNomCrediteur()).isEqualTo(DEFAULT_NOM_CREDITEUR);
        assertThat(testCrediteur.getPrenomCrediteur()).isEqualTo(DEFAULT_PRENOM_CREDITEUR);
        assertThat(testCrediteur.isActivated()).isEqualTo(DEFAULT_ACTIVATED);
        assertThat(testCrediteur.getRemarque()).isEqualTo(DEFAULT_REMARQUE);

        // Validate the Crediteur in Elasticsearch
        Crediteur crediteurEs = crediteurSearchRepository.findOne(testCrediteur.getId());
        assertThat(crediteurEs).isEqualToIgnoringGivenFields(testCrediteur);
    }

    @Test
    @Transactional
    public void createCrediteurWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = crediteurRepository.findAll().size();

        // Create the Crediteur with an existing ID
        crediteur.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrediteurMockMvc.perform(post("/api/crediteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crediteur)))
            .andExpect(status().isBadRequest());

        // Validate the Crediteur in the database
        List<Crediteur> crediteurList = crediteurRepository.findAll();
        assertThat(crediteurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkActivatedIsRequired() throws Exception {
        int databaseSizeBeforeTest = crediteurRepository.findAll().size();
        // set the field null
        crediteur.setActivated(null);

        // Create the Crediteur, which fails.

        restCrediteurMockMvc.perform(post("/api/crediteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crediteur)))
            .andExpect(status().isBadRequest());

        List<Crediteur> crediteurList = crediteurRepository.findAll();
        assertThat(crediteurList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCrediteurs() throws Exception {
        // Initialize the database
        crediteurRepository.saveAndFlush(crediteur);

        // Get all the crediteurList
        restCrediteurMockMvc.perform(get("/api/crediteurs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crediteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeCrediteur").value(hasItem(DEFAULT_CODE_CREDITEUR.toString())))
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
            .andExpect(jsonPath("$.[*].nomCrediteur").value(hasItem(DEFAULT_NOM_CREDITEUR.toString())))
            .andExpect(jsonPath("$.[*].prenomCrediteur").value(hasItem(DEFAULT_PRENOM_CREDITEUR.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void getCrediteur() throws Exception {
        // Initialize the database
        crediteurRepository.saveAndFlush(crediteur);

        // Get the crediteur
        restCrediteurMockMvc.perform(get("/api/crediteurs/{id}", crediteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(crediteur.getId().intValue()))
            .andExpect(jsonPath("$.codeCrediteur").value(DEFAULT_CODE_CREDITEUR.toString()))
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
            .andExpect(jsonPath("$.nomCrediteur").value(DEFAULT_NOM_CREDITEUR.toString()))
            .andExpect(jsonPath("$.prenomCrediteur").value(DEFAULT_PRENOM_CREDITEUR.toString()))
            .andExpect(jsonPath("$.activated").value(DEFAULT_ACTIVATED.booleanValue()))
            .andExpect(jsonPath("$.remarque").value(DEFAULT_REMARQUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCrediteur() throws Exception {
        // Get the crediteur
        restCrediteurMockMvc.perform(get("/api/crediteurs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCrediteur() throws Exception {
        // Initialize the database
        crediteurRepository.saveAndFlush(crediteur);
        crediteurSearchRepository.save(crediteur);
        int databaseSizeBeforeUpdate = crediteurRepository.findAll().size();

        // Update the crediteur
        Crediteur updatedCrediteur = crediteurRepository.findOne(crediteur.getId());
        // Disconnect from session so that the updates on updatedCrediteur are not directly saved in db
        em.detach(updatedCrediteur);
        updatedCrediteur
            .codeCrediteur(UPDATED_CODE_CREDITEUR)
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
            .nomCrediteur(UPDATED_NOM_CREDITEUR)
            .prenomCrediteur(UPDATED_PRENOM_CREDITEUR)
            .activated(UPDATED_ACTIVATED)
            .remarque(UPDATED_REMARQUE);

        restCrediteurMockMvc.perform(put("/api/crediteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCrediteur)))
            .andExpect(status().isOk());

        // Validate the Crediteur in the database
        List<Crediteur> crediteurList = crediteurRepository.findAll();
        assertThat(crediteurList).hasSize(databaseSizeBeforeUpdate);
        Crediteur testCrediteur = crediteurList.get(crediteurList.size() - 1);
        assertThat(testCrediteur.getCodeCrediteur()).isEqualTo(UPDATED_CODE_CREDITEUR);
        assertThat(testCrediteur.getRaisonSocial()).isEqualTo(UPDATED_RAISON_SOCIAL);
        assertThat(testCrediteur.getRaisonSocialArabe()).isEqualTo(UPDATED_RAISON_SOCIAL_ARABE);
        assertThat(testCrediteur.getRc()).isEqualTo(UPDATED_RC);
        assertThat(testCrediteur.getPatente()).isEqualTo(UPDATED_PATENTE);
        assertThat(testCrediteur.getNumTelephone()).isEqualTo(UPDATED_NUM_TELEPHONE);
        assertThat(testCrediteur.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCrediteur.getAdresse()).isEqualTo(UPDATED_ADRESSE);
        assertThat(testCrediteur.getQuartier()).isEqualTo(UPDATED_QUARTIER);
        assertThat(testCrediteur.getVille()).isEqualTo(UPDATED_VILLE);
        assertThat(testCrediteur.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testCrediteur.getNomCrediteur()).isEqualTo(UPDATED_NOM_CREDITEUR);
        assertThat(testCrediteur.getPrenomCrediteur()).isEqualTo(UPDATED_PRENOM_CREDITEUR);
        assertThat(testCrediteur.isActivated()).isEqualTo(UPDATED_ACTIVATED);
        assertThat(testCrediteur.getRemarque()).isEqualTo(UPDATED_REMARQUE);

        // Validate the Crediteur in Elasticsearch
        Crediteur crediteurEs = crediteurSearchRepository.findOne(testCrediteur.getId());
        assertThat(crediteurEs).isEqualToIgnoringGivenFields(testCrediteur);
    }

    @Test
    @Transactional
    public void updateNonExistingCrediteur() throws Exception {
        int databaseSizeBeforeUpdate = crediteurRepository.findAll().size();

        // Create the Crediteur

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCrediteurMockMvc.perform(put("/api/crediteurs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(crediteur)))
            .andExpect(status().isCreated());

        // Validate the Crediteur in the database
        List<Crediteur> crediteurList = crediteurRepository.findAll();
        assertThat(crediteurList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCrediteur() throws Exception {
        // Initialize the database
        crediteurRepository.saveAndFlush(crediteur);
        crediteurSearchRepository.save(crediteur);
        int databaseSizeBeforeDelete = crediteurRepository.findAll().size();

        // Get the crediteur
        restCrediteurMockMvc.perform(delete("/api/crediteurs/{id}", crediteur.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean crediteurExistsInEs = crediteurSearchRepository.exists(crediteur.getId());
        assertThat(crediteurExistsInEs).isFalse();

        // Validate the database is empty
        List<Crediteur> crediteurList = crediteurRepository.findAll();
        assertThat(crediteurList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCrediteur() throws Exception {
        // Initialize the database
        crediteurRepository.saveAndFlush(crediteur);
        crediteurSearchRepository.save(crediteur);

        // Search the crediteur
        restCrediteurMockMvc.perform(get("/api/_search/crediteurs?query=id:" + crediteur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crediteur.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeCrediteur").value(hasItem(DEFAULT_CODE_CREDITEUR.toString())))
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
            .andExpect(jsonPath("$.[*].nomCrediteur").value(hasItem(DEFAULT_NOM_CREDITEUR.toString())))
            .andExpect(jsonPath("$.[*].prenomCrediteur").value(hasItem(DEFAULT_PRENOM_CREDITEUR.toString())))
            .andExpect(jsonPath("$.[*].activated").value(hasItem(DEFAULT_ACTIVATED.booleanValue())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Crediteur.class);
        Crediteur crediteur1 = new Crediteur();
        crediteur1.setId(1L);
        Crediteur crediteur2 = new Crediteur();
        crediteur2.setId(crediteur1.getId());
        assertThat(crediteur1).isEqualTo(crediteur2);
        crediteur2.setId(2L);
        assertThat(crediteur1).isNotEqualTo(crediteur2);
        crediteur1.setId(null);
        assertThat(crediteur1).isNotEqualTo(crediteur2);
    }
}
