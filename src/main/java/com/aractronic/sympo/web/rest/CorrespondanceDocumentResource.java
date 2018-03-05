package com.aractronic.sympo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aractronic.sympo.domain.CorrespondanceDocument;

import com.aractronic.sympo.repository.CorrespondanceDocumentRepository;
import com.aractronic.sympo.repository.search.CorrespondanceDocumentSearchRepository;
import com.aractronic.sympo.web.rest.errors.BadRequestAlertException;
import com.aractronic.sympo.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CorrespondanceDocument.
 */
@RestController
@RequestMapping("/api")
public class CorrespondanceDocumentResource {

    private final Logger log = LoggerFactory.getLogger(CorrespondanceDocumentResource.class);

    private static final String ENTITY_NAME = "correspondanceDocument";

    private final CorrespondanceDocumentRepository correspondanceDocumentRepository;

    private final CorrespondanceDocumentSearchRepository correspondanceDocumentSearchRepository;

    public CorrespondanceDocumentResource(CorrespondanceDocumentRepository correspondanceDocumentRepository, CorrespondanceDocumentSearchRepository correspondanceDocumentSearchRepository) {
        this.correspondanceDocumentRepository = correspondanceDocumentRepository;
        this.correspondanceDocumentSearchRepository = correspondanceDocumentSearchRepository;
    }

    /**
     * POST  /correspondance-documents : Create a new correspondanceDocument.
     *
     * @param correspondanceDocument the correspondanceDocument to create
     * @return the ResponseEntity with status 201 (Created) and with body the new correspondanceDocument, or with status 400 (Bad Request) if the correspondanceDocument has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/correspondance-documents")
    @Timed
    public ResponseEntity<CorrespondanceDocument> createCorrespondanceDocument(@Valid @RequestBody CorrespondanceDocument correspondanceDocument) throws URISyntaxException {
        log.debug("REST request to save CorrespondanceDocument : {}", correspondanceDocument);
        if (correspondanceDocument.getId() != null) {
            throw new BadRequestAlertException("A new correspondanceDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CorrespondanceDocument result = correspondanceDocumentRepository.save(correspondanceDocument);
        correspondanceDocumentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/correspondance-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /correspondance-documents : Updates an existing correspondanceDocument.
     *
     * @param correspondanceDocument the correspondanceDocument to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated correspondanceDocument,
     * or with status 400 (Bad Request) if the correspondanceDocument is not valid,
     * or with status 500 (Internal Server Error) if the correspondanceDocument couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/correspondance-documents")
    @Timed
    public ResponseEntity<CorrespondanceDocument> updateCorrespondanceDocument(@Valid @RequestBody CorrespondanceDocument correspondanceDocument) throws URISyntaxException {
        log.debug("REST request to update CorrespondanceDocument : {}", correspondanceDocument);
        if (correspondanceDocument.getId() == null) {
            return createCorrespondanceDocument(correspondanceDocument);
        }
        CorrespondanceDocument result = correspondanceDocumentRepository.save(correspondanceDocument);
        correspondanceDocumentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, correspondanceDocument.getId().toString()))
            .body(result);
    }

    /**
     * GET  /correspondance-documents : get all the correspondanceDocuments.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of correspondanceDocuments in body
     */
    @GetMapping("/correspondance-documents")
    @Timed
    public List<CorrespondanceDocument> getAllCorrespondanceDocuments(@RequestParam(required = false) String filter) {
        if ("custom-is-null".equals(filter)) {
            log.debug("REST request to get all CorrespondanceDocuments where custom is null");
            return StreamSupport
                .stream(correspondanceDocumentRepository.findAll().spliterator(), false)
                .filter(correspondanceDocument -> correspondanceDocument.getCustom() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all CorrespondanceDocuments");
        return correspondanceDocumentRepository.findAll();
        }

    /**
     * GET  /correspondance-documents/:id : get the "id" correspondanceDocument.
     *
     * @param id the id of the correspondanceDocument to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the correspondanceDocument, or with status 404 (Not Found)
     */
    @GetMapping("/correspondance-documents/{id}")
    @Timed
    public ResponseEntity<CorrespondanceDocument> getCorrespondanceDocument(@PathVariable Long id) {
        log.debug("REST request to get CorrespondanceDocument : {}", id);
        CorrespondanceDocument correspondanceDocument = correspondanceDocumentRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(correspondanceDocument));
    }

    /**
     * DELETE  /correspondance-documents/:id : delete the "id" correspondanceDocument.
     *
     * @param id the id of the correspondanceDocument to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/correspondance-documents/{id}")
    @Timed
    public ResponseEntity<Void> deleteCorrespondanceDocument(@PathVariable Long id) {
        log.debug("REST request to delete CorrespondanceDocument : {}", id);
        correspondanceDocumentRepository.delete(id);
        correspondanceDocumentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/correspondance-documents?query=:query : search for the correspondanceDocument corresponding
     * to the query.
     *
     * @param query the query of the correspondanceDocument search
     * @return the result of the search
     */
    @GetMapping("/_search/correspondance-documents")
    @Timed
    public List<CorrespondanceDocument> searchCorrespondanceDocuments(@RequestParam String query) {
        log.debug("REST request to search CorrespondanceDocuments for query {}", query);
        return StreamSupport
            .stream(correspondanceDocumentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
