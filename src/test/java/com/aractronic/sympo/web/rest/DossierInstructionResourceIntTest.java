package com.aractronic.sympo.web.rest;

import com.aractronic.sympo.SympoApp;

import com.aractronic.sympo.domain.DossierInstruction;
import com.aractronic.sympo.repository.DossierInstructionRepository;
import com.aractronic.sympo.repository.search.DossierInstructionSearchRepository;
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
 * Test class for the DossierInstructionResource REST controller.
 *
 * @see DossierInstructionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SympoApp.class)
public class DossierInstructionResourceIntTest {

    private static final String DEFAULT_NUM_INSTRUCTION = "AAAAAA";
    private static final String UPDATED_NUM_INSTRUCTION = "BBBBBB";

    private static final String DEFAULT_INSTRUCTION = "AAAAAAAAAA";
    private static final String UPDATED_INSTRUCTION = "BBBBBBBBBB";

    private static final String DEFAULT_SUPPORT_INSTRUCTION = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORT_INSTRUCTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE_CREATION_INSTRUCTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATION_INSTRUCTION = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_REMARQUE = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUE = "BBBBBBBBBB";

    @Autowired
    private DossierInstructionRepository dossierInstructionRepository;

    @Autowired
    private DossierInstructionSearchRepository dossierInstructionSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restDossierInstructionMockMvc;

    private DossierInstruction dossierInstruction;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final DossierInstructionResource dossierInstructionResource = new DossierInstructionResource(dossierInstructionRepository, dossierInstructionSearchRepository);
        this.restDossierInstructionMockMvc = MockMvcBuilders.standaloneSetup(dossierInstructionResource)
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
    public static DossierInstruction createEntity(EntityManager em) {
        DossierInstruction dossierInstruction = new DossierInstruction()
            .numInstruction(DEFAULT_NUM_INSTRUCTION)
            .instruction(DEFAULT_INSTRUCTION)
            .supportInstruction(DEFAULT_SUPPORT_INSTRUCTION)
            .dateCreationInstruction(DEFAULT_DATE_CREATION_INSTRUCTION)
            .remarque(DEFAULT_REMARQUE);
        return dossierInstruction;
    }

    @Before
    public void initTest() {
        dossierInstructionSearchRepository.deleteAll();
        dossierInstruction = createEntity(em);
    }

    @Test
    @Transactional
    public void createDossierInstruction() throws Exception {
        int databaseSizeBeforeCreate = dossierInstructionRepository.findAll().size();

        // Create the DossierInstruction
        restDossierInstructionMockMvc.perform(post("/api/dossier-instructions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierInstruction)))
            .andExpect(status().isCreated());

        // Validate the DossierInstruction in the database
        List<DossierInstruction> dossierInstructionList = dossierInstructionRepository.findAll();
        assertThat(dossierInstructionList).hasSize(databaseSizeBeforeCreate + 1);
        DossierInstruction testDossierInstruction = dossierInstructionList.get(dossierInstructionList.size() - 1);
        assertThat(testDossierInstruction.getNumInstruction()).isEqualTo(DEFAULT_NUM_INSTRUCTION);
        assertThat(testDossierInstruction.getInstruction()).isEqualTo(DEFAULT_INSTRUCTION);
        assertThat(testDossierInstruction.getSupportInstruction()).isEqualTo(DEFAULT_SUPPORT_INSTRUCTION);
        assertThat(testDossierInstruction.getDateCreationInstruction()).isEqualTo(DEFAULT_DATE_CREATION_INSTRUCTION);
        assertThat(testDossierInstruction.getRemarque()).isEqualTo(DEFAULT_REMARQUE);

        // Validate the DossierInstruction in Elasticsearch
        DossierInstruction dossierInstructionEs = dossierInstructionSearchRepository.findOne(testDossierInstruction.getId());
        assertThat(dossierInstructionEs).isEqualToIgnoringGivenFields(testDossierInstruction);
    }

    @Test
    @Transactional
    public void createDossierInstructionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = dossierInstructionRepository.findAll().size();

        // Create the DossierInstruction with an existing ID
        dossierInstruction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDossierInstructionMockMvc.perform(post("/api/dossier-instructions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierInstruction)))
            .andExpect(status().isBadRequest());

        // Validate the DossierInstruction in the database
        List<DossierInstruction> dossierInstructionList = dossierInstructionRepository.findAll();
        assertThat(dossierInstructionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNumInstructionIsRequired() throws Exception {
        int databaseSizeBeforeTest = dossierInstructionRepository.findAll().size();
        // set the field null
        dossierInstruction.setNumInstruction(null);

        // Create the DossierInstruction, which fails.

        restDossierInstructionMockMvc.perform(post("/api/dossier-instructions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierInstruction)))
            .andExpect(status().isBadRequest());

        List<DossierInstruction> dossierInstructionList = dossierInstructionRepository.findAll();
        assertThat(dossierInstructionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllDossierInstructions() throws Exception {
        // Initialize the database
        dossierInstructionRepository.saveAndFlush(dossierInstruction);

        // Get all the dossierInstructionList
        restDossierInstructionMockMvc.perform(get("/api/dossier-instructions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossierInstruction.getId().intValue())))
            .andExpect(jsonPath("$.[*].numInstruction").value(hasItem(DEFAULT_NUM_INSTRUCTION.toString())))
            .andExpect(jsonPath("$.[*].instruction").value(hasItem(DEFAULT_INSTRUCTION.toString())))
            .andExpect(jsonPath("$.[*].supportInstruction").value(hasItem(DEFAULT_SUPPORT_INSTRUCTION.toString())))
            .andExpect(jsonPath("$.[*].dateCreationInstruction").value(hasItem(DEFAULT_DATE_CREATION_INSTRUCTION.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void getDossierInstruction() throws Exception {
        // Initialize the database
        dossierInstructionRepository.saveAndFlush(dossierInstruction);

        // Get the dossierInstruction
        restDossierInstructionMockMvc.perform(get("/api/dossier-instructions/{id}", dossierInstruction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(dossierInstruction.getId().intValue()))
            .andExpect(jsonPath("$.numInstruction").value(DEFAULT_NUM_INSTRUCTION.toString()))
            .andExpect(jsonPath("$.instruction").value(DEFAULT_INSTRUCTION.toString()))
            .andExpect(jsonPath("$.supportInstruction").value(DEFAULT_SUPPORT_INSTRUCTION.toString()))
            .andExpect(jsonPath("$.dateCreationInstruction").value(DEFAULT_DATE_CREATION_INSTRUCTION.toString()))
            .andExpect(jsonPath("$.remarque").value(DEFAULT_REMARQUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingDossierInstruction() throws Exception {
        // Get the dossierInstruction
        restDossierInstructionMockMvc.perform(get("/api/dossier-instructions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDossierInstruction() throws Exception {
        // Initialize the database
        dossierInstructionRepository.saveAndFlush(dossierInstruction);
        dossierInstructionSearchRepository.save(dossierInstruction);
        int databaseSizeBeforeUpdate = dossierInstructionRepository.findAll().size();

        // Update the dossierInstruction
        DossierInstruction updatedDossierInstruction = dossierInstructionRepository.findOne(dossierInstruction.getId());
        // Disconnect from session so that the updates on updatedDossierInstruction are not directly saved in db
        em.detach(updatedDossierInstruction);
        updatedDossierInstruction
            .numInstruction(UPDATED_NUM_INSTRUCTION)
            .instruction(UPDATED_INSTRUCTION)
            .supportInstruction(UPDATED_SUPPORT_INSTRUCTION)
            .dateCreationInstruction(UPDATED_DATE_CREATION_INSTRUCTION)
            .remarque(UPDATED_REMARQUE);

        restDossierInstructionMockMvc.perform(put("/api/dossier-instructions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedDossierInstruction)))
            .andExpect(status().isOk());

        // Validate the DossierInstruction in the database
        List<DossierInstruction> dossierInstructionList = dossierInstructionRepository.findAll();
        assertThat(dossierInstructionList).hasSize(databaseSizeBeforeUpdate);
        DossierInstruction testDossierInstruction = dossierInstructionList.get(dossierInstructionList.size() - 1);
        assertThat(testDossierInstruction.getNumInstruction()).isEqualTo(UPDATED_NUM_INSTRUCTION);
        assertThat(testDossierInstruction.getInstruction()).isEqualTo(UPDATED_INSTRUCTION);
        assertThat(testDossierInstruction.getSupportInstruction()).isEqualTo(UPDATED_SUPPORT_INSTRUCTION);
        assertThat(testDossierInstruction.getDateCreationInstruction()).isEqualTo(UPDATED_DATE_CREATION_INSTRUCTION);
        assertThat(testDossierInstruction.getRemarque()).isEqualTo(UPDATED_REMARQUE);

        // Validate the DossierInstruction in Elasticsearch
        DossierInstruction dossierInstructionEs = dossierInstructionSearchRepository.findOne(testDossierInstruction.getId());
        assertThat(dossierInstructionEs).isEqualToIgnoringGivenFields(testDossierInstruction);
    }

    @Test
    @Transactional
    public void updateNonExistingDossierInstruction() throws Exception {
        int databaseSizeBeforeUpdate = dossierInstructionRepository.findAll().size();

        // Create the DossierInstruction

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restDossierInstructionMockMvc.perform(put("/api/dossier-instructions")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(dossierInstruction)))
            .andExpect(status().isCreated());

        // Validate the DossierInstruction in the database
        List<DossierInstruction> dossierInstructionList = dossierInstructionRepository.findAll();
        assertThat(dossierInstructionList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteDossierInstruction() throws Exception {
        // Initialize the database
        dossierInstructionRepository.saveAndFlush(dossierInstruction);
        dossierInstructionSearchRepository.save(dossierInstruction);
        int databaseSizeBeforeDelete = dossierInstructionRepository.findAll().size();

        // Get the dossierInstruction
        restDossierInstructionMockMvc.perform(delete("/api/dossier-instructions/{id}", dossierInstruction.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean dossierInstructionExistsInEs = dossierInstructionSearchRepository.exists(dossierInstruction.getId());
        assertThat(dossierInstructionExistsInEs).isFalse();

        // Validate the database is empty
        List<DossierInstruction> dossierInstructionList = dossierInstructionRepository.findAll();
        assertThat(dossierInstructionList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchDossierInstruction() throws Exception {
        // Initialize the database
        dossierInstructionRepository.saveAndFlush(dossierInstruction);
        dossierInstructionSearchRepository.save(dossierInstruction);

        // Search the dossierInstruction
        restDossierInstructionMockMvc.perform(get("/api/_search/dossier-instructions?query=id:" + dossierInstruction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dossierInstruction.getId().intValue())))
            .andExpect(jsonPath("$.[*].numInstruction").value(hasItem(DEFAULT_NUM_INSTRUCTION.toString())))
            .andExpect(jsonPath("$.[*].instruction").value(hasItem(DEFAULT_INSTRUCTION.toString())))
            .andExpect(jsonPath("$.[*].supportInstruction").value(hasItem(DEFAULT_SUPPORT_INSTRUCTION.toString())))
            .andExpect(jsonPath("$.[*].dateCreationInstruction").value(hasItem(DEFAULT_DATE_CREATION_INSTRUCTION.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DossierInstruction.class);
        DossierInstruction dossierInstruction1 = new DossierInstruction();
        dossierInstruction1.setId(1L);
        DossierInstruction dossierInstruction2 = new DossierInstruction();
        dossierInstruction2.setId(dossierInstruction1.getId());
        assertThat(dossierInstruction1).isEqualTo(dossierInstruction2);
        dossierInstruction2.setId(2L);
        assertThat(dossierInstruction1).isNotEqualTo(dossierInstruction2);
        dossierInstruction1.setId(null);
        assertThat(dossierInstruction1).isNotEqualTo(dossierInstruction2);
    }
}
