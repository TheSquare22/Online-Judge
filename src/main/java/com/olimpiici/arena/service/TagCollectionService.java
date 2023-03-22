package com.olimpiici.arena.service;

import com.olimpiici.arena.service.dto.TagCollectionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.olimpiici.arena.domain.TagCollection}.
 */
public interface TagCollectionService {
    /**
     * Save a tagCollection.
     *
     * @param tagCollectionDTO the entity to save.
     * @return the persisted entity.
     */
    TagCollectionDTO save(TagCollectionDTO tagCollectionDTO);

    /**
     * Updates a tagCollection.
     *
     * @param tagCollectionDTO the entity to update.
     * @return the persisted entity.
     */
    TagCollectionDTO update(TagCollectionDTO tagCollectionDTO);

    /**
     * Partially updates a tagCollection.
     *
     * @param tagCollectionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TagCollectionDTO> partialUpdate(TagCollectionDTO tagCollectionDTO);

    /**
     * Get all the tagCollections.
     *
     * @return the list of entities.
     */
    List<TagCollectionDTO> findAll();

    /**
     * Get the "id" tagCollection.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TagCollectionDTO> findOne(Long id);

    /**
     * Delete the "id" tagCollection.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
