package com.olimpiici.arena.web.rest;

import com.olimpiici.arena.repository.TagCollectionRepository;
import com.olimpiici.arena.service.TagCollectionService;
import com.olimpiici.arena.service.dto.TagCollectionDTO;
import com.olimpiici.arena.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.olimpiici.arena.domain.TagCollection}.
 */
@RestController
@RequestMapping("/api")
public class TagCollectionResource {

    private final Logger log = LoggerFactory.getLogger(TagCollectionResource.class);

    private static final String ENTITY_NAME = "tagCollection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TagCollectionService tagCollectionService;

    private final TagCollectionRepository tagCollectionRepository;

    public TagCollectionResource(TagCollectionService tagCollectionService, TagCollectionRepository tagCollectionRepository) {
        this.tagCollectionService = tagCollectionService;
        this.tagCollectionRepository = tagCollectionRepository;
    }

    /**
     * {@code POST  /tag-collections} : Create a new tagCollection.
     *
     * @param tagCollectionDTO the tagCollectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tagCollectionDTO, or with status {@code 400 (Bad Request)} if the tagCollection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tag-collections")
    public ResponseEntity<TagCollectionDTO> createTagCollection(@RequestBody TagCollectionDTO tagCollectionDTO) throws URISyntaxException {
        log.debug("REST request to save TagCollection : {}", tagCollectionDTO);
        if (tagCollectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new tagCollection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TagCollectionDTO result = tagCollectionService.save(tagCollectionDTO);
        return ResponseEntity
            .created(new URI("/api/tag-collections/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tag-collections/:id} : Updates an existing tagCollection.
     *
     * @param id the id of the tagCollectionDTO to save.
     * @param tagCollectionDTO the tagCollectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagCollectionDTO,
     * or with status {@code 400 (Bad Request)} if the tagCollectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tagCollectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tag-collections/{id}")
    public ResponseEntity<TagCollectionDTO> updateTagCollection(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TagCollectionDTO tagCollectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TagCollection : {}, {}", id, tagCollectionDTO);
        if (tagCollectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagCollectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagCollectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TagCollectionDTO result = tagCollectionService.update(tagCollectionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagCollectionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tag-collections/:id} : Partial updates given fields of an existing tagCollection, field will ignore if it is null
     *
     * @param id the id of the tagCollectionDTO to save.
     * @param tagCollectionDTO the tagCollectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagCollectionDTO,
     * or with status {@code 400 (Bad Request)} if the tagCollectionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tagCollectionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tagCollectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tag-collections/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TagCollectionDTO> partialUpdateTagCollection(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TagCollectionDTO tagCollectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TagCollection partially : {}, {}", id, tagCollectionDTO);
        if (tagCollectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagCollectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagCollectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TagCollectionDTO> result = tagCollectionService.partialUpdate(tagCollectionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagCollectionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tag-collections} : get all the tagCollections.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tagCollections in body.
     */
    @GetMapping("/tag-collections")
    public List<TagCollectionDTO> getAllTagCollections() {
        log.debug("REST request to get all TagCollections");
        return tagCollectionService.findAll();
    }

    /**
     * {@code GET  /tag-collections/:id} : get the "id" tagCollection.
     *
     * @param id the id of the tagCollectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tagCollectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tag-collections/{id}")
    public ResponseEntity<TagCollectionDTO> getTagCollection(@PathVariable Long id) {
        log.debug("REST request to get TagCollection : {}", id);
        Optional<TagCollectionDTO> tagCollectionDTO = tagCollectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tagCollectionDTO);
    }

    /**
     * {@code DELETE  /tag-collections/:id} : delete the "id" tagCollection.
     *
     * @param id the id of the tagCollectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tag-collections/{id}")
    public ResponseEntity<Void> deleteTagCollection(@PathVariable Long id) {
        log.debug("REST request to delete TagCollection : {}", id);
        tagCollectionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
