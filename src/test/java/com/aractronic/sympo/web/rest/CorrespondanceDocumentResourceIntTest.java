package com.aractronic.sympo.web.rest;

import com.aractronic.sympo.SympoApp;

import com.aractronic.sympo.domain.CorrespondanceDocument;
import com.aractronic.sympo.repository.CorrespondanceDocumentRepository;
import com.aractronic.sympo.repository.search.CorrespondanceDocumentSearchRepository;
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
import org.springframework.util.Base64Utils;

import javax.persistence.EntityManager;
import java.util.List;

import static com.aractronic.sympo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CorrespondanceDocumentResource REST controller.
 *
 * @see CorrespondanceDocumentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SympoApp.class)
public class CorrespondanceDocumentResourceIntTest {

    private static final String DEFAULT_CORRESPONDANCE_DOCUMENT = "AAAAAAAAAA";
    private static final String UPDATED_CORRESPONDANCE_DOCUMENT = "BBBBBBBBBB";

    private static final String DEFAULT_REMARQUE = "AAAAAAAAAA";
    private static final String UPDATED_REMARQUE = "BBBBBBBBBB";

    @Autowired
    private CorrespondanceDocumentRepository correspondanceDocumentRepository;

    @Autowired
    private CorrespondanceDocumentSearchRepository correspondanceDocumentSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCorrespondanceDocumentMockMvc;

    private CorrespondanceDocument correspondanceDocument;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CorrespondanceDocumentResource correspondanceDocumentResource = new CorrespondanceDocumentResource(correspondanceDocumentRepository, correspondanceDocumentSearchRepository);
        this.restCorrespondanceDocumentMockMvc = MockMvcBuilders.standaloneSetup(correspondanceDocumentResource)
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
    public static CorrespondanceDocument createEntity(EntityManager em) {
        CorrespondanceDocument correspondanceDocument = new CorrespondanceDocument()
            .correspondanceDocument(DEFAULT_CORRESPONDANCE_DOCUMENT)
            .remarque(DEFAULT_REMARQUE);
        return correspondanceDocument;
    }

    @Before
    public void initTest() {
        correspondanceDocumentSearchRepository.deleteAll();
        correspondanceDocument = createEntity(em);
    }

    @Test
    @Transactional
    public void createCorrespondanceDocument() throws Exception {
        int databaseSizeBeforeCreate = correspondanceDocumentRepository.findAll().size();

        // Create the CorrespondanceDocument
        restCorrespondanceDocumentMockMvc.perform(post("/api/correspondance-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondanceDocument)))
            .andExpect(status().isCreated());

        // Validate the CorrespondanceDocument in the database
        List<CorrespondanceDocument> correspondanceDocumentList = correspondanceDocumentRepository.findAll();
        assertThat(correspondanceDocumentList).hasSize(databaseSizeBeforeCreate + 1);
        CorrespondanceDocument testCorrespondanceDocument = correspondanceDocumentList.get(correspondanceDocumentList.size() - 1);
        assertThat(testCorrespondanceDocument.getCorrespondanceDocument()).isEqualTo(DEFAULT_CORRESPONDANCE_DOCUMENT);
        assertThat(testCorrespondanceDocument.getRemarque()).isEqualTo(DEFAULT_REMARQUE);

        // Validate the CorrespondanceDocument in Elasticsearch
        CorrespondanceDocument correspondanceDocumentEs = correspondanceDocumentSearchRepository.findOne(testCorrespondanceDocument.getId());
        assertThat(correspondanceDocumentEs).isEqualToIgnoringGivenFields(testCorrespondanceDocument);
    }

    @Test
    @Transactional
    public void createCorrespondanceDocumentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = correspondanceDocumentRepository.findAll().size();

        // Create the CorrespondanceDocument with an existing ID
        correspondanceDocument.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorrespondanceDocumentMockMvc.perform(post("/api/correspondance-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondanceDocument)))
            .andExpect(status().isBadRequest());

        // Validate the CorrespondanceDocument in the database
        List<CorrespondanceDocument> correspondanceDocumentList = correspondanceDocumentRepository.findAll();
        assertThat(correspondanceDocumentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCorrespondanceDocuments() throws Exception {
        // Initialize the database
        correspondanceDocumentRepository.saveAndFlush(correspondanceDocument);

        // Get all the correspondanceDocumentList
        restCorrespondanceDocumentMockMvc.perform(get("/api/correspondance-documents?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correspondanceDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].correspondanceDocument").value(hasItem(DEFAULT_CORRESPONDANCE_DOCUMENT.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void getCorrespondanceDocument() throws Exception {
        // Initialize the database
        correspondanceDocumentRepository.saveAndFlush(correspondanceDocument);

        // Get the correspondanceDocument
        restCorrespondanceDocumentMockMvc.perform(get("/api/correspondance-documents/{id}", correspondanceDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(correspondanceDocument.getId().intValue()))
            .andExpect(jsonPath("$.correspondanceDocument").value(DEFAULT_CORRESPONDANCE_DOCUMENT.toString()))
            .andExpect(jsonPath("$.remarque").value(DEFAULT_REMARQUE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCorrespondanceDocument() throws Exception {
        // Get the correspondanceDocument
        restCorrespondanceDocumentMockMvc.perform(get("/api/correspondance-documents/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCorrespondanceDocument() throws Exception {
        // Initialize the database
        correspondanceDocumentRepository.saveAndFlush(correspondanceDocument);
        correspondanceDocumentSearchRepository.save(correspondanceDocument);
        int databaseSizeBeforeUpdate = correspondanceDocumentRepository.findAll().size();

        // Update the correspondanceDocument
        CorrespondanceDocument updatedCorrespondanceDocument = correspondanceDocumentRepository.findOne(correspondanceDocument.getId());
        // Disconnect from session so that the updates on updatedCorrespondanceDocument are not directly saved in db
        em.detach(updatedCorrespondanceDocument);
        updatedCorrespondanceDocument
            .correspondanceDocument(UPDATED_CORRESPONDANCE_DOCUMENT)
            .remarque(UPDATED_REMARQUE);

        restCorrespondanceDocumentMockMvc.perform(put("/api/correspondance-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCorrespondanceDocument)))
            .andExpect(status().isOk());

        // Validate the CorrespondanceDocument in the database
        List<CorrespondanceDocument> correspondanceDocumentList = correspondanceDocumentRepository.findAll();
        assertThat(correspondanceDocumentList).hasSize(databaseSizeBeforeUpdate);
        CorrespondanceDocument testCorrespondanceDocument = correspondanceDocumentList.get(correspondanceDocumentList.size() - 1);
        assertThat(testCorrespondanceDocument.getCorrespondanceDocument()).isEqualTo(UPDATED_CORRESPONDANCE_DOCUMENT);
        assertThat(testCorrespondanceDocument.getRemarque()).isEqualTo(UPDATED_REMARQUE);

        // Validate the CorrespondanceDocument in Elasticsearch
        CorrespondanceDocument correspondanceDocumentEs = correspondanceDocumentSearchRepository.findOne(testCorrespondanceDocument.getId());
        assertThat(correspondanceDocumentEs).isEqualToIgnoringGivenFields(testCorrespondanceDocument);
    }

    @Test
    @Transactional
    public void updateNonExistingCorrespondanceDocument() throws Exception {
        int databaseSizeBeforeUpdate = correspondanceDocumentRepository.findAll().size();

        // Create the CorrespondanceDocument

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCorrespondanceDocumentMockMvc.perform(put("/api/correspondance-documents")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(correspondanceDocument)))
            .andExpect(status().isCreated());

        // Validate the CorrespondanceDocument in the database
        List<CorrespondanceDocument> correspondanceDocumentList = correspondanceDocumentRepository.findAll();
        assertThat(correspondanceDocumentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCorrespondanceDocument() throws Exception {
        // Initialize the database
        correspondanceDocumentRepository.saveAndFlush(correspondanceDocument);
        correspondanceDocumentSearchRepository.save(correspondanceDocument);
        int databaseSizeBeforeDelete = correspondanceDocumentRepository.findAll().size();

        // Get the correspondanceDocument
        restCorrespondanceDocumentMockMvc.perform(delete("/api/correspondance-documents/{id}", correspondanceDocument.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean correspondanceDocumentExistsInEs = correspondanceDocumentSearchRepository.exists(correspondanceDocument.getId());
        assertThat(correspondanceDocumentExistsInEs).isFalse();

        // Validate the database is empty
        List<CorrespondanceDocument> correspondanceDocumentList = correspondanceDocumentRepository.findAll();
        assertThat(correspondanceDocumentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCorrespondanceDocument() throws Exception {
        // Initialize the database
        correspondanceDocumentRepository.saveAndFlush(correspondanceDocument);
        correspondanceDocumentSearchRepository.save(correspondanceDocument);

        // Search the correspondanceDocument
        restCorrespondanceDocumentMockMvc.perform(get("/api/_search/correspondance-documents?query=id:" + correspondanceDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correspondanceDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].correspondanceDocument").value(hasItem(DEFAULT_CORRESPONDANCE_DOCUMENT.toString())))
            .andExpect(jsonPath("$.[*].remarque").value(hasItem(DEFAULT_REMARQUE.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CorrespondanceDocument.class);
        CorrespondanceDocument correspondanceDocument1 = new CorrespondanceDocument();
        correspondanceDocument1.setId(1L);
        CorrespondanceDocument correspondanceDocument2 = new CorrespondanceDocument();
        correspondanceDocument2.setId(correspondanceDocument1.getId());
        assertThat(correspondanceDocument1).isEqualTo(correspondanceDocument2);
        correspondanceDocument2.setId(2L);
        assertThat(correspondanceDocument1).isNotEqualTo(correspondanceDocument2);
        correspondanceDocument1.setId(null);
        assertThat(correspondanceDocument1).isNotEqualTo(correspondanceDocument2);
    }
}
