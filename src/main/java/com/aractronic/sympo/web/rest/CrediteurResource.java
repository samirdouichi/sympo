package com.aractronic.sympo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aractronic.sympo.domain.Crediteur;

import com.aractronic.sympo.repository.CrediteurRepository;
import com.aractronic.sympo.repository.search.CrediteurSearchRepository;
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
 * REST controller for managing Crediteur.
 */
@RestController
@RequestMapping("/api")
public class CrediteurResource {

    private final Logger log = LoggerFactory.getLogger(CrediteurResource.class);

    private static final String ENTITY_NAME = "crediteur";

    private final CrediteurRepository crediteurRepository;

    private final CrediteurSearchRepository crediteurSearchRepository;

    public CrediteurResource(CrediteurRepository crediteurRepository, CrediteurSearchRepository crediteurSearchRepository) {
        this.crediteurRepository = crediteurRepository;
        this.crediteurSearchRepository = crediteurSearchRepository;
    }

    /**
     * POST  /crediteurs : Create a new crediteur.
     *
     * @param crediteur the crediteur to create
     * @return the ResponseEntity with status 201 (Created) and with body the new crediteur, or with status 400 (Bad Request) if the crediteur has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/crediteurs")
    @Timed
    public ResponseEntity<Crediteur> createCrediteur(@Valid @RequestBody Crediteur crediteur) throws URISyntaxException {
        log.debug("REST request to save Crediteur : {}", crediteur);
        if (crediteur.getId() != null) {
            throw new BadRequestAlertException("A new crediteur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Crediteur result = crediteurRepository.save(crediteur);
        crediteurSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/crediteurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /crediteurs : Updates an existing crediteur.
     *
     * @param crediteur the crediteur to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated crediteur,
     * or with status 400 (Bad Request) if the crediteur is not valid,
     * or with status 500 (Internal Server Error) if the crediteur couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/crediteurs")
    @Timed
    public ResponseEntity<Crediteur> updateCrediteur(@Valid @RequestBody Crediteur crediteur) throws URISyntaxException {
        log.debug("REST request to update Crediteur : {}", crediteur);
        if (crediteur.getId() == null) {
            return createCrediteur(crediteur);
        }
        Crediteur result = crediteurRepository.save(crediteur);
        crediteurSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, crediteur.getId().toString()))
            .body(result);
    }

    /**
     * GET  /crediteurs : get all the crediteurs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of crediteurs in body
     */
    @GetMapping("/crediteurs")
    @Timed
    public ResponseEntity<List<Crediteur>> getAllCrediteurs(Pageable pageable) {
        log.debug("REST request to get a page of Crediteurs");
        Page<Crediteur> page = crediteurRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/crediteurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /crediteurs/:id : get the "id" crediteur.
     *
     * @param id the id of the crediteur to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the crediteur, or with status 404 (Not Found)
     */
    @GetMapping("/crediteurs/{id}")
    @Timed
    public ResponseEntity<Crediteur> getCrediteur(@PathVariable Long id) {
        log.debug("REST request to get Crediteur : {}", id);
        Crediteur crediteur = crediteurRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(crediteur));
    }

    /**
     * DELETE  /crediteurs/:id : delete the "id" crediteur.
     *
     * @param id the id of the crediteur to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/crediteurs/{id}")
    @Timed
    public ResponseEntity<Void> deleteCrediteur(@PathVariable Long id) {
        log.debug("REST request to delete Crediteur : {}", id);
        crediteurRepository.delete(id);
        crediteurSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/crediteurs?query=:query : search for the crediteur corresponding
     * to the query.
     *
     * @param query the query of the crediteur search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/crediteurs")
    @Timed
    public ResponseEntity<List<Crediteur>> searchCrediteurs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Crediteurs for query {}", query);
        Page<Crediteur> page = crediteurSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/crediteurs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
