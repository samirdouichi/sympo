package com.aractronic.sympo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aractronic.sympo.domain.Cabinet;

import com.aractronic.sympo.repository.CabinetRepository;
import com.aractronic.sympo.repository.search.CabinetSearchRepository;
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
 * REST controller for managing Cabinet.
 */
@RestController
@RequestMapping("/api")
public class CabinetResource {

    private final Logger log = LoggerFactory.getLogger(CabinetResource.class);

    private static final String ENTITY_NAME = "cabinet";

    private final CabinetRepository cabinetRepository;

    private final CabinetSearchRepository cabinetSearchRepository;

    public CabinetResource(CabinetRepository cabinetRepository, CabinetSearchRepository cabinetSearchRepository) {
        this.cabinetRepository = cabinetRepository;
        this.cabinetSearchRepository = cabinetSearchRepository;
    }

    /**
     * POST  /cabinets : Create a new cabinet.
     *
     * @param cabinet the cabinet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new cabinet, or with status 400 (Bad Request) if the cabinet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/cabinets")
    @Timed
    public ResponseEntity<Cabinet> createCabinet(@Valid @RequestBody Cabinet cabinet) throws URISyntaxException {
        log.debug("REST request to save Cabinet : {}", cabinet);
        if (cabinet.getId() != null) {
            throw new BadRequestAlertException("A new cabinet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cabinet result = cabinetRepository.save(cabinet);
        cabinetSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/cabinets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /cabinets : Updates an existing cabinet.
     *
     * @param cabinet the cabinet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated cabinet,
     * or with status 400 (Bad Request) if the cabinet is not valid,
     * or with status 500 (Internal Server Error) if the cabinet couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/cabinets")
    @Timed
    public ResponseEntity<Cabinet> updateCabinet(@Valid @RequestBody Cabinet cabinet) throws URISyntaxException {
        log.debug("REST request to update Cabinet : {}", cabinet);
        if (cabinet.getId() == null) {
            return createCabinet(cabinet);
        }
        Cabinet result = cabinetRepository.save(cabinet);
        cabinetSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, cabinet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /cabinets : get all the cabinets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of cabinets in body
     */
    @GetMapping("/cabinets")
    @Timed
    public ResponseEntity<List<Cabinet>> getAllCabinets(Pageable pageable) {
        log.debug("REST request to get a page of Cabinets");
        Page<Cabinet> page = cabinetRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/cabinets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /cabinets/:id : get the "id" cabinet.
     *
     * @param id the id of the cabinet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the cabinet, or with status 404 (Not Found)
     */
    @GetMapping("/cabinets/{id}")
    @Timed
    public ResponseEntity<Cabinet> getCabinet(@PathVariable Long id) {
        log.debug("REST request to get Cabinet : {}", id);
        Cabinet cabinet = cabinetRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(cabinet));
    }

    /**
     * DELETE  /cabinets/:id : delete the "id" cabinet.
     *
     * @param id the id of the cabinet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/cabinets/{id}")
    @Timed
    public ResponseEntity<Void> deleteCabinet(@PathVariable Long id) {
        log.debug("REST request to delete Cabinet : {}", id);
        cabinetRepository.delete(id);
        cabinetSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/cabinets?query=:query : search for the cabinet corresponding
     * to the query.
     *
     * @param query the query of the cabinet search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/cabinets")
    @Timed
    public ResponseEntity<List<Cabinet>> searchCabinets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Cabinets for query {}", query);
        Page<Cabinet> page = cabinetSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/cabinets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
