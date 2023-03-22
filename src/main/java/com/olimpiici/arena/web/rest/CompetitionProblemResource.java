package com.olimpiici.arena.web.rest;

import com.olimpiici.arena.repository.CompetitionProblemRepository;
import com.olimpiici.arena.service.CompetitionProblemService;
import com.olimpiici.arena.service.dto.CompetitionProblemDTO;
import com.olimpiici.arena.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.olimpiici.arena.domain.CompetitionProblem}.
 */
@RestController
@RequestMapping("/api")
public class CompetitionProblemResource {

    private final Logger log = LoggerFactory.getLogger(CompetitionProblemResource.class);

    private static final String ENTITY_NAME = "competitionProblem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompetitionProblemService competitionProblemService;

    private final CompetitionProblemRepository competitionProblemRepository;

    public CompetitionProblemResource(
        CompetitionProblemService competitionProblemService,
        CompetitionProblemRepository competitionProblemRepository
    ) {
        this.competitionProblemService = competitionProblemService;
        this.competitionProblemRepository = competitionProblemRepository;
    }

    /**
     * {@code POST  /competition-problems} : Create a new competitionProblem.
     *
     * @param competitionProblemDTO the competitionProblemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new competitionProblemDTO, or with status {@code 400 (Bad Request)} if the competitionProblem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/competition-problems")
    public ResponseEntity<CompetitionProblemDTO> createCompetitionProblem(@RequestBody CompetitionProblemDTO competitionProblemDTO)
        throws URISyntaxException {
        log.debug("REST request to save CompetitionProblem : {}", competitionProblemDTO);
        if (competitionProblemDTO.getId() != null) {
            throw new BadRequestAlertException("A new competitionProblem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompetitionProblemDTO result = competitionProblemService.save(competitionProblemDTO);
        return ResponseEntity
            .created(new URI("/api/competition-problems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /competition-problems/:id} : Updates an existing competitionProblem.
     *
     * @param id the id of the competitionProblemDTO to save.
     * @param competitionProblemDTO the competitionProblemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competitionProblemDTO,
     * or with status {@code 400 (Bad Request)} if the competitionProblemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the competitionProblemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/competition-problems/{id}")
    public ResponseEntity<CompetitionProblemDTO> updateCompetitionProblem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompetitionProblemDTO competitionProblemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CompetitionProblem : {}, {}", id, competitionProblemDTO);
        if (competitionProblemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competitionProblemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competitionProblemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompetitionProblemDTO result = competitionProblemService.update(competitionProblemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competitionProblemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /competition-problems/:id} : Partial updates given fields of an existing competitionProblem, field will ignore if it is null
     *
     * @param id the id of the competitionProblemDTO to save.
     * @param competitionProblemDTO the competitionProblemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated competitionProblemDTO,
     * or with status {@code 400 (Bad Request)} if the competitionProblemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the competitionProblemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the competitionProblemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/competition-problems/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompetitionProblemDTO> partialUpdateCompetitionProblem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompetitionProblemDTO competitionProblemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompetitionProblem partially : {}, {}", id, competitionProblemDTO);
        if (competitionProblemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, competitionProblemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!competitionProblemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompetitionProblemDTO> result = competitionProblemService.partialUpdate(competitionProblemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, competitionProblemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /competition-problems} : get all the competitionProblems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of competitionProblems in body.
     */
    @GetMapping("/competition-problems")
    public ResponseEntity<List<CompetitionProblemDTO>> getAllCompetitionProblems(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CompetitionProblems");
        Page<CompetitionProblemDTO> page = competitionProblemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /competition-problems/:id} : get the "id" competitionProblem.
     *
     * @param id the id of the competitionProblemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the competitionProblemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/competition-problems/{id}")
    public ResponseEntity<CompetitionProblemDTO> getCompetitionProblem(@PathVariable Long id) {
        log.debug("REST request to get CompetitionProblem : {}", id);
        Optional<CompetitionProblemDTO> competitionProblemDTO = competitionProblemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(competitionProblemDTO);
    }

    /**
     * {@code DELETE  /competition-problems/:id} : delete the "id" competitionProblem.
     *
     * @param id the id of the competitionProblemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/competition-problems/{id}")
    public ResponseEntity<Void> deleteCompetitionProblem(@PathVariable Long id) {
        log.debug("REST request to delete CompetitionProblem : {}", id);
        competitionProblemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
