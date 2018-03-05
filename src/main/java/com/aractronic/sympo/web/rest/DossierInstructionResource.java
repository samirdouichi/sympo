package com.aractronic.sympo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.aractronic.sympo.domain.DossierInstruction;

import com.aractronic.sympo.repository.DossierInstructionRepository;
import com.aractronic.sympo.repository.search.DossierInstructionSearchRepository;
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
 * REST controller for managing DossierInstruction.
 */
@RestController
@RequestMapping("/api")
public class DossierInstructionResource {

    private final Logger log = LoggerFactory.getLogger(DossierInstructionResource.class);

    private static final String ENTITY_NAME = "dossierInstruction";

    private final DossierInstructionRepository dossierInstructionRepository;

    private final DossierInstructionSearchRepository dossierInstructionSearchRepository;

    public DossierInstructionResource(DossierInstructionRepository dossierInstructionRepository, DossierInstructionSearchRepository dossierInstructionSearchRepository) {
        this.dossierInstructionRepository = dossierInstructionRepository;
        this.dossierInstructionSearchRepository = dossierInstructionSearchRepository;
    }

    /**
     * POST  /dossier-instructions : Create a new dossierInstruction.
     *
     * @param dossierInstruction the dossierInstruction to create
     * @return the ResponseEntity with status 201 (Created) and with body the new dossierInstruction, or with status 400 (Bad Request) if the dossierInstruction has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/dossier-instructions")
    @Timed
    public ResponseEntity<DossierInstruction> createDossierInstruction(@Valid @RequestBody DossierInstruction dossierInstruction) throws URISyntaxException {
        log.debug("REST request to save DossierInstruction : {}", dossierInstruction);
        if (dossierInstruction.getId() != null) {
            throw new BadRequestAlertException("A new dossierInstruction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DossierInstruction result = dossierInstructionRepository.save(dossierInstruction);
        dossierInstructionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/dossier-instructions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /dossier-instructions : Updates an existing dossierInstruction.
     *
     * @param dossierInstruction the dossierInstruction to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated dossierInstruction,
     * or with status 400 (Bad Request) if the dossierInstruction is not valid,
     * or with status 500 (Internal Server Error) if the dossierInstruction couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/dossier-instructions")
    @Timed
    public ResponseEntity<DossierInstruction> updateDossierInstruction(@Valid @RequestBody DossierInstruction dossierInstruction) throws URISyntaxException {
        log.debug("REST request to update DossierInstruction : {}", dossierInstruction);
        if (dossierInstruction.getId() == null) {
            return createDossierInstruction(dossierInstruction);
        }
        DossierInstruction result = dossierInstructionRepository.save(dossierInstruction);
        dossierInstructionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, dossierInstruction.getId().toString()))
            .body(result);
    }

    /**
     * GET  /dossier-instructions : get all the dossierInstructions.
     *
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of dossierInstructions in body
     */
    @GetMapping("/dossier-instructions")
    @Timed
    public List<DossierInstruction> getAllDossierInstructions(@RequestParam(required = false) String filter) {
        if ("custom-is-null".equals(filter)) {
            log.debug("REST request to get all DossierInstructions where custom is null");
            return StreamSupport
                .stream(dossierInstructionRepository.findAll().spliterator(), false)
                .filter(dossierInstruction -> dossierInstruction.getCustom() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all DossierInstructions");
        return dossierInstructionRepository.findAll();
        }

    /**
     * GET  /dossier-instructions/:id : get the "id" dossierInstruction.
     *
     * @param id the id of the dossierInstruction to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the dossierInstruction, or with status 404 (Not Found)
     */
    @GetMapping("/dossier-instructions/{id}")
    @Timed
    public ResponseEntity<DossierInstruction> getDossierInstruction(@PathVariable Long id) {
        log.debug("REST request to get DossierInstruction : {}", id);
        DossierInstruction dossierInstruction = dossierInstructionRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(dossierInstruction));
    }

    /**
     * DELETE  /dossier-instructions/:id : delete the "id" dossierInstruction.
     *
     * @param id the id of the dossierInstruction to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/dossier-instructions/{id}")
    @Timed
    public ResponseEntity<Void> deleteDossierInstruction(@PathVariable Long id) {
        log.debug("REST request to delete DossierInstruction : {}", id);
        dossierInstructionRepository.delete(id);
        dossierInstructionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/dossier-instructions?query=:query : search for the dossierInstruction corresponding
     * to the query.
     *
     * @param query the query of the dossierInstruction search
     * @return the result of the search
     */
    @GetMapping("/_search/dossier-instructions")
    @Timed
    public List<DossierInstruction> searchDossierInstructions(@RequestParam String query) {
        log.debug("REST request to search DossierInstructions for query {}", query);
        return StreamSupport
            .stream(dossierInstructionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }

}
