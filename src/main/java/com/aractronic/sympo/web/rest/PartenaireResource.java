package com.aractronic.sympo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aractronic.sympo.domain.Partenaire;

import com.aractronic.sympo.repository.PartenaireRepository;
import com.aractronic.sympo.repository.search.PartenaireSearchRepository;
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
 * REST controller for managing Partenaire.
 */
@RestController
@RequestMapping("/api")
public class PartenaireResource {

    private final Logger log = LoggerFactory.getLogger(PartenaireResource.class);

    private static final String ENTITY_NAME = "partenaire";

    private final PartenaireRepository partenaireRepository;

    private final PartenaireSearchRepository partenaireSearchRepository;

    public PartenaireResource(PartenaireRepository partenaireRepository, PartenaireSearchRepository partenaireSearchRepository) {
        this.partenaireRepository = partenaireRepository;
        this.partenaireSearchRepository = partenaireSearchRepository;
    }

    /**
     * POST  /partenaires : Create a new partenaire.
     *
     * @param partenaire the partenaire to create
     * @return the ResponseEntity with status 201 (Created) and with body the new partenaire, or with status 400 (Bad Request) if the partenaire has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/partenaires")
    @Timed
    public ResponseEntity<Partenaire> createPartenaire(@Valid @RequestBody Partenaire partenaire) throws URISyntaxException {
        log.debug("REST request to save Partenaire : {}", partenaire);
        if (partenaire.getId() != null) {
            throw new BadRequestAlertException("A new partenaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Partenaire result = partenaireRepository.save(partenaire);
        partenaireSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/partenaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /partenaires : Updates an existing partenaire.
     *
     * @param partenaire the partenaire to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated partenaire,
     * or with status 400 (Bad Request) if the partenaire is not valid,
     * or with status 500 (Internal Server Error) if the partenaire couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/partenaires")
    @Timed
    public ResponseEntity<Partenaire> updatePartenaire(@Valid @RequestBody Partenaire partenaire) throws URISyntaxException {
        log.debug("REST request to update Partenaire : {}", partenaire);
        if (partenaire.getId() == null) {
            return createPartenaire(partenaire);
        }
        Partenaire result = partenaireRepository.save(partenaire);
        partenaireSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, partenaire.getId().toString()))
            .body(result);
    }

    /**
     * GET  /partenaires : get all the partenaires.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of partenaires in body
     */
    @GetMapping("/partenaires")
    @Timed
    public List<Partenaire> getAllPartenaires() {
        log.debug("REST request to get all Partenaires");
        return partenaireRepository.findAll();
        }

    /**
     * GET  /partenaires/:id : get the "id" partenaire.
     *
     * @param id the id of the partenaire to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the partenaire, or with status 404 (Not Found)
     */
    @GetMapping("/partenaires/{id}")
    @Timed
    public ResponseEntity<Partenaire> getPartenaire(@PathVariable Long id) {
        log.debug("REST request to get Partenaire : {}", id);
        Partenaire partenaire = partenaireRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(partenaire));
    }

    /**
     * DELETE  /partenaires/:id : delete the "id" partenaire.
     *
     * @param id the id of the partenaire to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/partenaires/{id}")
    @Timed
    public ResponseEntity<Void> deletePartenaire(@PathVariable Long id) {
        log.debug("REST request to delete Partenaire : {}", id);
        partenaireRepository.delete(id);
        partenaireSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/partenaires?query=:query : search for the partenaire corresponding
     * to the query.
     *
     * @param query the query of the partenaire search
     * @return the result of the search
     */
    @GetMapping("/_search/partenaires")
    @Timed
    public List<Partenaire> searchPartenaires(@RequestParam String query) {
        log.debug("REST request to search Partenaires for query {}", query);
        return StreamSupport
            .stream(partenaireSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
