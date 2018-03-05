package com.aractronic.sympo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aractronic.sympo.domain.Creance;

import com.aractronic.sympo.repository.CreanceRepository;
import com.aractronic.sympo.repository.search.CreanceSearchRepository;
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
 * REST controller for managing Creance.
 */
@RestController
@RequestMapping("/api")
public class CreanceResource {

    private final Logger log = LoggerFactory.getLogger(CreanceResource.class);

    private static final String ENTITY_NAME = "creance";

    private final CreanceRepository creanceRepository;

    private final CreanceSearchRepository creanceSearchRepository;

    public CreanceResource(CreanceRepository creanceRepository, CreanceSearchRepository creanceSearchRepository) {
        this.creanceRepository = creanceRepository;
        this.creanceSearchRepository = creanceSearchRepository;
    }

    /**
     * POST  /creances : Create a new creance.
     *
     * @param creance the creance to create
     * @return the ResponseEntity with status 201 (Created) and with body the new creance, or with status 400 (Bad Request) if the creance has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/creances")
    @Timed
    public ResponseEntity<Creance> createCreance(@Valid @RequestBody Creance creance) throws URISyntaxException {
        log.debug("REST request to save Creance : {}", creance);
        if (creance.getId() != null) {
            throw new BadRequestAlertException("A new creance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Creance result = creanceRepository.save(creance);
        creanceSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/creances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /creances : Updates an existing creance.
     *
     * @param creance the creance to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated creance,
     * or with status 400 (Bad Request) if the creance is not valid,
     * or with status 500 (Internal Server Error) if the creance couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/creances")
    @Timed
    public ResponseEntity<Creance> updateCreance(@Valid @RequestBody Creance creance) throws URISyntaxException {
        log.debug("REST request to update Creance : {}", creance);
        if (creance.getId() == null) {
            return createCreance(creance);
        }
        Creance result = creanceRepository.save(creance);
        creanceSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, creance.getId().toString()))
            .body(result);
    }

    /**
     * GET  /creances : get all the creances.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of creances in body
     */
    @GetMapping("/creances")
    @Timed
    public List<Creance> getAllCreances() {
        log.debug("REST request to get all Creances");
        return creanceRepository.findAll();
        }

    /**
     * GET  /creances/:id : get the "id" creance.
     *
     * @param id the id of the creance to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the creance, or with status 404 (Not Found)
     */
    @GetMapping("/creances/{id}")
    @Timed
    public ResponseEntity<Creance> getCreance(@PathVariable Long id) {
        log.debug("REST request to get Creance : {}", id);
        Creance creance = creanceRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(creance));
    }

    /**
     * DELETE  /creances/:id : delete the "id" creance.
     *
     * @param id the id of the creance to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/creances/{id}")
    @Timed
    public ResponseEntity<Void> deleteCreance(@PathVariable Long id) {
        log.debug("REST request to delete Creance : {}", id);
        creanceRepository.delete(id);
        creanceSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/creances?query=:query : search for the creance corresponding
     * to the query.
     *
     * @param query the query of the creance search
     * @return the result of the search
     */
    @GetMapping("/_search/creances")
    @Timed
    public List<Creance> searchCreances(@RequestParam String query) {
        log.debug("REST request to search Creances for query {}", query);
        return StreamSupport
            .stream(creanceSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
