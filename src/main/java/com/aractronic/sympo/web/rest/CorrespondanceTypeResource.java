package com.aractronic.sympo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aractronic.sympo.domain.CorrespondanceType;

import com.aractronic.sympo.repository.CorrespondanceTypeRepository;
import com.aractronic.sympo.repository.search.CorrespondanceTypeSearchRepository;
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
 * REST controller for managing CorrespondanceType.
 */
@RestController
@RequestMapping("/api")
public class CorrespondanceTypeResource {

    private final Logger log = LoggerFactory.getLogger(CorrespondanceTypeResource.class);

    private static final String ENTITY_NAME = "correspondanceType";

    private final CorrespondanceTypeRepository correspondanceTypeRepository;

    private final CorrespondanceTypeSearchRepository correspondanceTypeSearchRepository;

    public CorrespondanceTypeResource(CorrespondanceTypeRepository correspondanceTypeRepository, CorrespondanceTypeSearchRepository correspondanceTypeSearchRepository) {
        this.correspondanceTypeRepository = correspondanceTypeRepository;
        this.correspondanceTypeSearchRepository = correspondanceTypeSearchRepository;
    }

    /**
     * POST  /correspondance-types : Create a new correspondanceType.
     *
     * @param correspondanceType the correspondanceType to create
     * @return the ResponseEntity with status 201 (Created) and with body the new correspondanceType, or with status 400 (Bad Request) if the correspondanceType has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/correspondance-types")
    @Timed
    public ResponseEntity<CorrespondanceType> createCorrespondanceType(@Valid @RequestBody CorrespondanceType correspondanceType) throws URISyntaxException {
        log.debug("REST request to save CorrespondanceType : {}", correspondanceType);
        if (correspondanceType.getId() != null) {
            throw new BadRequestAlertException("A new correspondanceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CorrespondanceType result = correspondanceTypeRepository.save(correspondanceType);
        correspondanceTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/correspondance-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /correspondance-types : Updates an existing correspondanceType.
     *
     * @param correspondanceType the correspondanceType to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated correspondanceType,
     * or with status 400 (Bad Request) if the correspondanceType is not valid,
     * or with status 500 (Internal Server Error) if the correspondanceType couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/correspondance-types")
    @Timed
    public ResponseEntity<CorrespondanceType> updateCorrespondanceType(@Valid @RequestBody CorrespondanceType correspondanceType) throws URISyntaxException {
        log.debug("REST request to update CorrespondanceType : {}", correspondanceType);
        if (correspondanceType.getId() == null) {
            return createCorrespondanceType(correspondanceType);
        }
        CorrespondanceType result = correspondanceTypeRepository.save(correspondanceType);
        correspondanceTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, correspondanceType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /correspondance-types : get all the correspondanceTypes.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of correspondanceTypes in body
     */
    @GetMapping("/correspondance-types")
    @Timed
    public List<CorrespondanceType> getAllCorrespondanceTypes(@RequestParam(required = false) String filter) {
        if ("custom-is-null".equals(filter)) {
            log.debug("REST request to get all CorrespondanceTypes where custom is null");
            return StreamSupport
                .stream(correspondanceTypeRepository.findAll().spliterator(), false)
                .filter(correspondanceType -> correspondanceType.getCustom() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all CorrespondanceTypes");
        return correspondanceTypeRepository.findAll();
        }

    /**
     * GET  /correspondance-types/:id : get the "id" correspondanceType.
     *
     * @param id the id of the correspondanceType to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the correspondanceType, or with status 404 (Not Found)
     */
    @GetMapping("/correspondance-types/{id}")
    @Timed
    public ResponseEntity<CorrespondanceType> getCorrespondanceType(@PathVariable Long id) {
        log.debug("REST request to get CorrespondanceType : {}", id);
        CorrespondanceType correspondanceType = correspondanceTypeRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(correspondanceType));
    }

    /**
     * DELETE  /correspondance-types/:id : delete the "id" correspondanceType.
     *
     * @param id the id of the correspondanceType to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/correspondance-types/{id}")
    @Timed
    public ResponseEntity<Void> deleteCorrespondanceType(@PathVariable Long id) {
        log.debug("REST request to delete CorrespondanceType : {}", id);
        correspondanceTypeRepository.delete(id);
        correspondanceTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/correspondance-types?query=:query : search for the correspondanceType corresponding
     * to the query.
     *
     * @param query the query of the correspondanceType search
     * @return the result of the search
     */
    @GetMapping("/_search/correspondance-types")
    @Timed
    public List<CorrespondanceType> searchCorrespondanceTypes(@RequestParam String query) {
        log.debug("REST request to search CorrespondanceTypes for query {}", query);
        return StreamSupport
            .stream(correspondanceTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
