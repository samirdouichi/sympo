package com.aractronic.sympo.web.rest;

import com.aractronic.sympo.SympoApp;

import com.aractronic.sympo.domain.CorrespondanceType;
import com.aractronic.sympo.repository.CorrespondanceTypeRepository;
import com.aractronic.sympo.repository.search.CorrespondanceTypeSearchRepository;
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
 * Test class for the CorrespondanceTypeResource REST controller.
 *
 * @see CorrespondanceTypeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SympoApp.class)
public class CorrespondanceTypeResourceIntTest {

    private static final String DEFAULT_CODE_CORRESPONDANCE = "AAAAAAAAAA";
    private static final String UPDATED_CODE_CORRESPONDANCE = "BBBBBBBBBB";

    private static final String DEFAULT_NOM_CORRESPONDANCE = "AAAAAAAAAA";
    private static final String UPDATED_NOM_CORRESPONDANCE = "BBBBBBBBBB";

    @Autowired
    private CorrespondanceTypeRepository correspondanceTypeRepository;

    @Autowired
    private CorrespondanceTypeSearchRepository correspondanceTypeSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCorrespondanceTypeMockMvc;

    private CorrespondanceType correspondanceType;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CorrespondanceTypeResource correspondanceTypeResource = new CorrespondanceTypeResource(correspondanceTypeRepository, correspondanceTypeSearchRepository);
        this.restCorrespondanceTypeMockMvc = MockMvcBuilders.standaloneSetup(correspondanceTypeResource)
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
    public static CorrespondanceType createEntity(EntityManager em) {
        CorrespondanceType correspondanceType = new CorrespondanceType()
            .codeCorrespondance(DEFAULT_CODE_CORRESPONDANCE)
            .nomCorrespondance(DEFAULT_NOM_CORRESPONDANCE);
        return correspondanceType;
    }

    @Before
    public void initTest() {
        correspondanceTypeSearchRepository.deleteAll();
        correspondanceType = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorrespondanceType() throws Exception {
        int databaseSizeBeforeCreate = correspondanceTypeRepository.findAll().size();

        // Create the CorrespondanceType
        restCorrespondanceTypeMockMvc.perform(post("/api/correspondance-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondanceType)))
            .andExpect(status().isCreated());

        // Validate the CorrespondanceType in the database
        List<CorrespondanceType> correspondanceTypeList = correspondanceTypeRepository.findAll();
        assertThat(correspondanceTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CorrespondanceType testCorrespondanceType = correspondanceTypeList.get(correspondanceTypeList.size() - 1);
        assertThat(testCorrespondanceType.getCodeCorrespondance()).isEqualTo(DEFAULT_CODE_CORRESPONDANCE);
        assertThat(testCorrespondanceType.getNomCorrespondance()).isEqualTo(DEFAULT_NOM_CORRESPONDANCE);

        // Validate the CorrespondanceType in Elasticsearch
        CorrespondanceType correspondanceTypeEs = correspondanceTypeSearchRepository.findOne(testCorrespondanceType.getId());
        assertThat(correspondanceTypeEs).isEqualToIgnoringGivenFields(testCorrespondanceType);
    }

    @Test
    @Transactional
    public void createCorrespondanceTypeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = correspondanceTypeRepository.findAll().size();

        // Create the CorrespondanceType with an existing ID
        correspondanceType.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorrespondanceTypeMockMvc.perform(post("/api/correspondance-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondanceType)))
            .andExpect(status().isBadRequest());

        // Validate the CorrespondanceType in the database
        List<CorrespondanceType> correspondanceTypeList = correspondanceTypeRepository.findAll();
        assertThat(correspondanceTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCorrespondanceTypes() throws Exception {
        // Initialize the database
        correspondanceTypeRepository.saveAndFlush(correspondanceType);

        // Get all the correspondanceTypeList
        restCorrespondanceTypeMockMvc.perform(get("/api/correspondance-types?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correspondanceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeCorrespondance").value(hasItem(DEFAULT_CODE_CORRESPONDANCE.toString())))
            .andExpect(jsonPath("$.[*].nomCorrespondance").value(hasItem(DEFAULT_NOM_CORRESPONDANCE.toString())));
    }

    @Test
    @Transactional
    public void getCorrespondanceType() throws Exception {
        // Initialize the database
        correspondanceTypeRepository.saveAndFlush(correspondanceType);

        // Get the correspondanceType
        restCorrespondanceTypeMockMvc.perform(get("/api/correspondance-types/{id}", correspondanceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(correspondanceType.getId().intValue()))
            .andExpect(jsonPath("$.codeCorrespondance").value(DEFAULT_CODE_CORRESPONDANCE.toString()))
            .andExpect(jsonPath("$.nomCorrespondance").value(DEFAULT_NOM_CORRESPONDANCE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCorrespondanceType() throws Exception {
        // Get the correspondanceType
        restCorrespondanceTypeMockMvc.perform(get("/api/correspondance-types/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorrespondanceType() throws Exception {
        // Initialize the database
        correspondanceTypeRepository.saveAndFlush(correspondanceType);
        correspondanceTypeSearchRepository.save(correspondanceType);
        int databaseSizeBeforeUpdate = correspondanceTypeRepository.findAll().size();

        // Update the correspondanceType
        CorrespondanceType updatedCorrespondanceType = correspondanceTypeRepository.findOne(correspondanceType.getId());
        // Disconnect from session so that the updates on updatedCorrespondanceType are not directly saved in db
        em.detach(updatedCorrespondanceType);
        updatedCorrespondanceType
            .codeCorrespondance(UPDATED_CODE_CORRESPONDANCE)
            .nomCorrespondance(UPDATED_NOM_CORRESPONDANCE);

        restCorrespondanceTypeMockMvc.perform(put("/api/correspondance-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCorrespondanceType)))
            .andExpect(status().isOk());

        // Validate the CorrespondanceType in the database
        List<CorrespondanceType> correspondanceTypeList = correspondanceTypeRepository.findAll();
        assertThat(correspondanceTypeList).hasSize(databaseSizeBeforeUpdate);
        CorrespondanceType testCorrespondanceType = correspondanceTypeList.get(correspondanceTypeList.size() - 1);
        assertThat(testCorrespondanceType.getCodeCorrespondance()).isEqualTo(UPDATED_CODE_CORRESPONDANCE);
        assertThat(testCorrespondanceType.getNomCorrespondance()).isEqualTo(UPDATED_NOM_CORRESPONDANCE);

        // Validate the CorrespondanceType in Elasticsearch
        CorrespondanceType correspondanceTypeEs = correspondanceTypeSearchRepository.findOne(testCorrespondanceType.getId());
        assertThat(correspondanceTypeEs).isEqualToIgnoringGivenFields(testCorrespondanceType);
    }

    @Test
    @Transactional
    public void updateNonExistingCorrespondanceType() throws Exception {
        int databaseSizeBeforeUpdate = correspondanceTypeRepository.findAll().size();

        // Create the CorrespondanceType

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCorrespondanceTypeMockMvc.perform(put("/api/correspondance-types")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondanceType)))
            .andExpect(status().isCreated());

        // Validate the CorrespondanceType in the database
        List<CorrespondanceType> correspondanceTypeList = correspondanceTypeRepository.findAll();
        assertThat(correspondanceTypeList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCorrespondanceType() throws Exception {
        // Initialize the database
        correspondanceTypeRepository.saveAndFlush(correspondanceType);
        correspondanceTypeSearchRepository.save(correspondanceType);
        int databaseSizeBeforeDelete = correspondanceTypeRepository.findAll().size();

        // Get the correspondanceType
        restCorrespondanceTypeMockMvc.perform(delete("/api/correspondance-types/{id}", correspondanceType.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean correspondanceTypeExistsInEs = correspondanceTypeSearchRepository.exists(correspondanceType.getId());
        assertThat(correspondanceTypeExistsInEs).isFalse();

        // Validate the database is empty
        List<CorrespondanceType> correspondanceTypeList = correspondanceTypeRepository.findAll();
        assertThat(correspondanceTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCorrespondanceType() throws Exception {
        // Initialize the database
        correspondanceTypeRepository.saveAndFlush(correspondanceType);
        correspondanceTypeSearchRepository.save(correspondanceType);

        // Search the correspondanceType
        restCorrespondanceTypeMockMvc.perform(get("/api/_search/correspondance-types?query=id:" + correspondanceType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correspondanceType.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeCorrespondance").value(hasItem(DEFAULT_CODE_CORRESPONDANCE.toString())))
            .andExpect(jsonPath("$.[*].nomCorrespondance").value(hasItem(DEFAULT_NOM_CORRESPONDANCE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorrespondanceType.class);
        CorrespondanceType correspondanceType1 = new CorrespondanceType();
        correspondanceType1.setId(1L);
        CorrespondanceType correspondanceType2 = new CorrespondanceType();
        correspondanceType2.setId(correspondanceType1.getId());
        assertThat(correspondanceType1).isEqualTo(correspondanceType2);
        correspondanceType2.setId(2L);
        assertThat(correspondanceType1).isNotEqualTo(correspondanceType2);
        correspondanceType1.setId(null);
        assertThat(correspondanceType1).isNotEqualTo(correspondanceType2);
    }
}
