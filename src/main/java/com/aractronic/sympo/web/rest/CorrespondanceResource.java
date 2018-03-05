package com.aractronic.sympo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aractronic.sympo.domain.Correspondance;

import com.aractronic.sympo.repository.CorrespondanceRepository;
import com.aractronic.sympo.repository.search.CorrespondanceSearchRepository;
import com.aractronic.sympo.web.rest.errors.BadRequestAlertException;
import com.aractronic.sympo.web.rest.util.HeaderUtil;
import com.aractronic.sympo.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
 * REST controller for managing Correspondance.
 */
@RestController
@RequestMapping("/api")
public class CorrespondanceResource {

    private final Logger log = LoggerFactory.getLogger(CorrespondanceResource.class);

    private static final String ENTITY_NAME = "correspondance";

    private final CorrespondanceRepository correspondanceRepository;

    private final CorrespondanceSearchRepository correspondanceSearchRepository;

    public CorrespondanceResource(CorrespondanceRepository correspondanceRepository, CorrespondanceSearchRepository correspondanceSearchRepository) {
        this.correspondanceRepository = correspondanceRepository;
        this.correspondanceSearchRepository = correspondanceSearchRepository;
    }

    /**
     * POST  /correspondances : Create a new correspondance.
     *
     * @param correspondance the correspondance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new correspondance, or with status 400 (Bad Request) if the correspondance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/correspondances")
    @Timed
    public ResponseEntity<Correspondance> createCorrespondance(@Valid @RequestBody Correspondance correspondance) throws URISyntaxException {
        log.debug("REST request to save Correspondance : {}", correspondance);
        if (correspondance.getId() != null) {
            throw new BadRequestAlertException("A new correspondance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Correspondance result = correspondanceRepository.save(correspondance);
        correspondanceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/correspondances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /correspondances : Updates an existing correspondance.
     *
     * @param correspondance the correspondance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated correspondance,
     * or with status 400 (Bad Request) if the correspondance is not valid,
     * or with status 500 (Internal Server Error) if the correspondance couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/correspondances")
    @Timed
    public ResponseEntity<Correspondance> updateCorrespondance(@Valid @RequestBody Correspondance correspondance) throws URISyntaxException {
        log.debug("REST request to update Correspondance : {}", correspondance);
        if (correspondance.getId() == null) {
            return createCorrespondance(correspondance);
        }
        Correspondance result = correspondanceRepository.save(correspondance);
        correspondanceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, correspondance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /correspondances : get all the correspondances.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of correspondances in body
     */
    @GetMapping("/correspondances")
    @Timed
    public ResponseEntity<List<Correspondance>> getAllCorrespondances(Pageable pageable) {
        log.debug("REST request to get a page of Correspondances");
        Page<Correspondance> page = correspondanceRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/correspondances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /correspondances/:id : get the "id" correspondance.
     *
     * @param id the id of the correspondance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the correspondance, or with status 404 (Not Found)
     */
    @GetMapping("/correspondances/{id}")
    @Timed
    public ResponseEntity<Correspondance> getCorrespondance(@PathVariable Long id) {
        log.debug("REST request to get Correspondance : {}", id);
        Correspondance correspondance = correspondanceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(correspondance));
    }

    /**
     * DELETE  /correspondances/:id : delete the "id" correspondance.
     *
     * @param id the id of the correspondance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/correspondances/{id}")
    @Timed
    public ResponseEntity<Void> deleteCorrespondance(@PathVariable Long id) {
        log.debug("REST request to delete Correspondance : {}", id);
        correspondanceRepository.delete(id);
        correspondanceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/correspondances?query=:query : search for the correspondance corresponding
     * to the query.
     *
     * @param query the query of the correspondance search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/correspondances")
    @Timed
    public ResponseEntity<List<Correspondance>> searchCorrespondances(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Correspondances for query {}", query);
        Page<Correspondance> page = correspondanceSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/correspondances");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
