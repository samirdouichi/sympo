package com.aractronic.sympo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aractronic.sympo.domain.Dossier;

import com.aractronic.sympo.repository.DossierRepository;
import com.aractronic.sympo.repository.search.DossierSearchRepository;
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
 * REST controller for managing Dossier.
 */
@RestController
@RequestMapping("/api")
public class DossierResource {

    private final Logger log = LoggerFactory.getLogger(DossierResource.class);

    private static final String ENTITY_NAME = "dossier";

    private final DossierRepository dossierRepository;

    private final DossierSearchRepository dossierSearchRepository;

    public DossierResource(DossierRepository dossierRepository, DossierSearchRepository dossierSearchRepository) {
        this.dossierRepository = dossierRepository;
        this.dossierSearchRepository = dossierSearchRepository;
    }

    /**
     * POST  /dossiers : Create a new dossier.
     *
     * @param dossier the dossier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dossier, or with status 400 (Bad Request) if the dossier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dossiers")
    @Timed
    public ResponseEntity<Dossier> createDossier(@Valid @RequestBody Dossier dossier) throws URISyntaxException {
        log.debug("REST request to save Dossier : {}", dossier);
        if (dossier.getId() != null) {
            throw new BadRequestAlertException("A new dossier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Dossier result = dossierRepository.save(dossier);
        dossierSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/dossiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dossiers : Updates an existing dossier.
     *
     * @param dossier the dossier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dossier,
     * or with status 400 (Bad Request) if the dossier is not valid,
     * or with status 500 (Internal Server Error) if the dossier couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/dossiers")
    @Timed
    public ResponseEntity<Dossier> updateDossier(@Valid @RequestBody Dossier dossier) throws URISyntaxException {
        log.debug("REST request to update Dossier : {}", dossier);
        if (dossier.getId() == null) {
            return createDossier(dossier);
        }
        Dossier result = dossierRepository.save(dossier);
        dossierSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dossier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dossiers : get all the dossiers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of dossiers in body
     */
    @GetMapping("/dossiers")
    @Timed
    public ResponseEntity<List<Dossier>> getAllDossiers(Pageable pageable) {
        log.debug("REST request to get a page of Dossiers");
        Page<Dossier> page = dossierRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/dossiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /dossiers/:id : get the "id" dossier.
     *
     * @param id the id of the dossier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dossier, or with status 404 (Not Found)
     */
    @GetMapping("/dossiers/{id}")
    @Timed
    public ResponseEntity<Dossier> getDossier(@PathVariable Long id) {
        log.debug("REST request to get Dossier : {}", id);
        Dossier dossier = dossierRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dossier));
    }

    /**
     * DELETE  /dossiers/:id : delete the "id" dossier.
     *
     * @param id the id of the dossier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/dossiers/{id}")
    @Timed
    public ResponseEntity<Void> deleteDossier(@PathVariable Long id) {
        log.debug("REST request to delete Dossier : {}", id);
        dossierRepository.delete(id);
        dossierSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/dossiers?query=:query : search for the dossier corresponding
     * to the query.
     *
     * @param query the query of the dossier search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/dossiers")
    @Timed
    public ResponseEntity<List<Dossier>> searchDossiers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Dossiers for query {}", query);
        Page<Dossier> page = dossierSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/dossiers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
