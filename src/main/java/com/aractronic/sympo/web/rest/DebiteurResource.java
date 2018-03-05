package com.aractronic.sympo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aractronic.sympo.domain.Debiteur;

import com.aractronic.sympo.repository.DebiteurRepository;
import com.aractronic.sympo.repository.search.DebiteurSearchRepository;
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
 * REST controller for managing Debiteur.
 */
@RestController
@RequestMapping("/api")
public class DebiteurResource {

    private final Logger log = LoggerFactory.getLogger(DebiteurResource.class);

    private static final String ENTITY_NAME = "debiteur";

    private final DebiteurRepository debiteurRepository;

    private final DebiteurSearchRepository debiteurSearchRepository;

    public DebiteurResource(DebiteurRepository debiteurRepository, DebiteurSearchRepository debiteurSearchRepository) {
        this.debiteurRepository = debiteurRepository;
        this.debiteurSearchRepository = debiteurSearchRepository;
    }

    /**
     * POST  /debiteurs : Create a new debiteur.
     *
     * @param debiteur the debiteur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new debiteur, or with status 400 (Bad Request) if the debiteur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/debiteurs")
    @Timed
    public ResponseEntity<Debiteur> createDebiteur(@Valid @RequestBody Debiteur debiteur) throws URISyntaxException {
        log.debug("REST request to save Debiteur : {}", debiteur);
        if (debiteur.getId() != null) {
            throw new BadRequestAlertException("A new debiteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Debiteur result = debiteurRepository.save(debiteur);
        debiteurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/debiteurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /debiteurs : Updates an existing debiteur.
     *
     * @param debiteur the debiteur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated debiteur,
     * or with status 400 (Bad Request) if the debiteur is not valid,
     * or with status 500 (Internal Server Error) if the debiteur couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/debiteurs")
    @Timed
    public ResponseEntity<Debiteur> updateDebiteur(@Valid @RequestBody Debiteur debiteur) throws URISyntaxException {
        log.debug("REST request to update Debiteur : {}", debiteur);
        if (debiteur.getId() == null) {
            return createDebiteur(debiteur);
        }
        Debiteur result = debiteurRepository.save(debiteur);
        debiteurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, debiteur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /debiteurs : get all the debiteurs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of debiteurs in body
     */
    @GetMapping("/debiteurs")
    @Timed
    public ResponseEntity<List<Debiteur>> getAllDebiteurs(Pageable pageable) {
        log.debug("REST request to get a page of Debiteurs");
        Page<Debiteur> page = debiteurRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/debiteurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /debiteurs/:id : get the "id" debiteur.
     *
     * @param id the id of the debiteur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the debiteur, or with status 404 (Not Found)
     */
    @GetMapping("/debiteurs/{id}")
    @Timed
    public ResponseEntity<Debiteur> getDebiteur(@PathVariable Long id) {
        log.debug("REST request to get Debiteur : {}", id);
        Debiteur debiteur = debiteurRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(debiteur));
    }

    /**
     * DELETE  /debiteurs/:id : delete the "id" debiteur.
     *
     * @param id the id of the debiteur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/debiteurs/{id}")
    @Timed
    public ResponseEntity<Void> deleteDebiteur(@PathVariable Long id) {
        log.debug("REST request to delete Debiteur : {}", id);
        debiteurRepository.delete(id);
        debiteurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/debiteurs?query=:query : search for the debiteur corresponding
     * to the query.
     *
     * @param query the query of the debiteur search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/debiteurs")
    @Timed
    public ResponseEntity<List<Debiteur>> searchDebiteurs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Debiteurs for query {}", query);
        Page<Debiteur> page = debiteurSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/debiteurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
