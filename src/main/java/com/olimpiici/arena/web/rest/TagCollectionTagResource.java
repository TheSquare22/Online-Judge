package com.olimpiici.arena.web.rest;

import com.olimpiici.arena.repository.TagCollectionTagRepository;
import com.olimpiici.arena.service.TagCollectionTagService;
import com.olimpiici.arena.service.dto.TagCollectionTagDTO;
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
 * REST controller for managing {@link com.olimpiici.arena.domain.TagCollectionTag}.
 */
@RestController
@RequestMapping("/api")
public class TagCollectionTagResource {

    private final Logger log = LoggerFactory.getLogger(TagCollectionTagResource.class);

    private static final String ENTITY_NAME = "tagCollectionTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TagCollectionTagService tagCollectionTagService;

    private final TagCollectionTagRepository tagCollectionTagRepository;

    public TagCollectionTagResource(
        TagCollectionTagService tagCollectionTagService,
        TagCollectionTagRepository tagCollectionTagRepository
    ) {
        this.tagCollectionTagService = tagCollectionTagService;
        this.tagCollectionTagRepository = tagCollectionTagRepository;
    }

    /**
     * {@code POST  /tag-collection-tags} : Create a new tagCollectionTag.
     *
     * @param tagCollectionTagDTO the tagCollectionTagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tagCollectionTagDTO, or with status {@code 400 (Bad Request)} if the tagCollectionTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tag-collection-tags")
    public ResponseEntity<TagCollectionTagDTO> createTagCollectionTag(@RequestBody TagCollectionTagDTO tagCollectionTagDTO)
        throws URISyntaxException {
        log.debug("REST request to save TagCollectionTag : {}", tagCollectionTagDTO);
        if (tagCollectionTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new tagCollectionTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TagCollectionTagDTO result = tagCollectionTagService.save(tagCollectionTagDTO);
        return ResponseEntity
            .created(new URI("/api/tag-collection-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tag-collection-tags/:id} : Updates an existing tagCollectionTag.
     *
     * @param id the id of the tagCollectionTagDTO to save.
     * @param tagCollectionTagDTO the tagCollectionTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagCollectionTagDTO,
     * or with status {@code 400 (Bad Request)} if the tagCollectionTagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tagCollectionTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tag-collection-tags/{id}")
    public ResponseEntity<TagCollectionTagDTO> updateTagCollectionTag(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TagCollectionTagDTO tagCollectionTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TagCollectionTag : {}, {}", id, tagCollectionTagDTO);
        if (tagCollectionTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagCollectionTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagCollectionTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TagCollectionTagDTO result = tagCollectionTagService.update(tagCollectionTagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagCollectionTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tag-collection-tags/:id} : Partial updates given fields of an existing tagCollectionTag, field will ignore if it is null
     *
     * @param id the id of the tagCollectionTagDTO to save.
     * @param tagCollectionTagDTO the tagCollectionTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagCollectionTagDTO,
     * or with status {@code 400 (Bad Request)} if the tagCollectionTagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tagCollectionTagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tagCollectionTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tag-collection-tags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TagCollectionTagDTO> partialUpdateTagCollectionTag(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TagCollectionTagDTO tagCollectionTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TagCollectionTag partially : {}, {}", id, tagCollectionTagDTO);
        if (tagCollectionTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagCollectionTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagCollectionTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TagCollectionTagDTO> result = tagCollectionTagService.partialUpdate(tagCollectionTagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagCollectionTagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tag-collection-tags} : get all the tagCollectionTags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tagCollectionTags in body.
     */
    @GetMapping("/tag-collection-tags")
    public List<TagCollectionTagDTO> getAllTagCollectionTags() {
        log.debug("REST request to get all TagCollectionTags");
        return tagCollectionTagService.findAll();
    }

    /**
     * {@code GET  /tag-collection-tags/:id} : get the "id" tagCollectionTag.
     *
     * @param id the id of the tagCollectionTagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tagCollectionTagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tag-collection-tags/{id}")
    public ResponseEntity<TagCollectionTagDTO> getTagCollectionTag(@PathVariable Long id) {
        log.debug("REST request to get TagCollectionTag : {}", id);
        Optional<TagCollectionTagDTO> tagCollectionTagDTO = tagCollectionTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tagCollectionTagDTO);
    }

    /**
     * {@code DELETE  /tag-collection-tags/:id} : delete the "id" tagCollectionTag.
     *
     * @param id the id of the tagCollectionTagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tag-collection-tags/{id}")
    public ResponseEntity<Void> deleteTagCollectionTag(@PathVariable Long id) {
        log.debug("REST request to delete TagCollectionTag : {}", id);
        tagCollectionTagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
