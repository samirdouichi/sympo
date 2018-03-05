package com.aractronic.sympo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aractronic.sympo.domain.CategorieDossier;

import com.aractronic.sympo.repository.CategorieDossierRepository;
import com.aractronic.sympo.repository.search.CategorieDossierSearchRepository;
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
 * REST controller for managing CategorieDossier.
 */
@RestController
@RequestMapping("/api")
public class CategorieDossierResource {

    private final Logger log = LoggerFactory.getLogger(CategorieDossierResource.class);

    private static final String ENTITY_NAME = "categorieDossier";

    private final CategorieDossierRepository categorieDossierRepository;

    private final CategorieDossierSearchRepository categorieDossierSearchRepository;

    public CategorieDossierResource(CategorieDossierRepository categorieDossierRepository, CategorieDossierSearchRepository categorieDossierSearchRepository) {
        this.categorieDossierRepository = categorieDossierRepository;
        this.categorieDossierSearchRepository = categorieDossierSearchRepository;
    }

    /**
     * POST  /categorie-dossiers : Create a new categorieDossier.
     *
     * @param categorieDossier the categorieDossier to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categorieDossier, or with status 400 (Bad Request) if the categorieDossier has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/categorie-dossiers")
    @Timed
    public ResponseEntity<CategorieDossier> createCategorieDossier(@Valid @RequestBody CategorieDossier categorieDossier) throws URISyntaxException {
        log.debug("REST request to save CategorieDossier : {}", categorieDossier);
        if (categorieDossier.getId() != null) {
            throw new BadRequestAlertException("A new categorieDossier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategorieDossier result = categorieDossierRepository.save(categorieDossier);
        categorieDossierSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/categorie-dossiers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categorie-dossiers : Updates an existing categorieDossier.
     *
     * @param categorieDossier the categorieDossier to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated categorieDossier,
     * or with status 400 (Bad Request) if the categorieDossier is not valid,
     * or with status 500 (Internal Server Error) if the categorieDossier couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/categorie-dossiers")
    @Timed
    public ResponseEntity<CategorieDossier> updateCategorieDossier(@Valid @RequestBody CategorieDossier categorieDossier) throws URISyntaxException {
        log.debug("REST request to update CategorieDossier : {}", categorieDossier);
        if (categorieDossier.getId() == null) {
            return createCategorieDossier(categorieDossier);
        }
        CategorieDossier result = categorieDossierRepository.save(categorieDossier);
        categorieDossierSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, categorieDossier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categorie-dossiers : get all the categorieDossiers.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of categorieDossiers in body
     */
    @GetMapping("/categorie-dossiers")
    @Timed
    public List<CategorieDossier> getAllCategorieDossiers() {
        log.debug("REST request to get all CategorieDossiers");
        return categorieDossierRepository.findAll();
        }

    /**
     * GET  /categorie-dossiers/:id : get the "id" categorieDossier.
     *
     * @param id the id of the categorieDossier to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the categorieDossier, or with status 404 (Not Found)
     */
    @GetMapping("/categorie-dossiers/{id}")
    @Timed
    public ResponseEntity<CategorieDossier> getCategorieDossier(@PathVariable Long id) {
        log.debug("REST request to get CategorieDossier : {}", id);
        CategorieDossier categorieDossier = categorieDossierRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(categorieDossier));
    }

    /**
     * DELETE  /categorie-dossiers/:id : delete the "id" categorieDossier.
     *
     * @param id the id of the categorieDossier to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/categorie-dossiers/{id}")
    @Timed
    public ResponseEntity<Void> deleteCategorieDossier(@PathVariable Long id) {
        log.debug("REST request to delete CategorieDossier : {}", id);
        categorieDossierRepository.delete(id);
        categorieDossierSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/categorie-dossiers?query=:query : search for the categorieDossier corresponding
     * to the query.
     *
     * @param query the query of the categorieDossier search
     * @return the result of the search
     */
    @GetMapping("/_search/categorie-dossiers")
    @Timed
    public List<CategorieDossier> searchCategorieDossiers(@RequestParam String query) {
        log.debug("REST request to search CategorieDossiers for query {}", query);
        return StreamSupport
            .stream(categorieDossierSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
