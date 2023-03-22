package com.olimpiici.arena.service;

import com.olimpiici.arena.service.dto.CompetitionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.olimpiici.arena.domain.Competition}.
 */
public interface CompetitionService {
    /**
     * Save a competition.
     *
     * @param competitionDTO the entity to save.
     * @return the persisted entity.
     */
    CompetitionDTO save(CompetitionDTO competitionDTO);

    /**
     * Updates a competition.
     *
     * @param competitionDTO the entity to update.
     * @return the persisted entity.
     */
    CompetitionDTO update(CompetitionDTO competitionDTO);

    /**
     * Partially updates a competition.
     *
     * @param competitionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompetitionDTO> partialUpdate(CompetitionDTO competitionDTO);

    /**
     * Get all the competitions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompetitionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" competition.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompetitionDTO> findOne(Long id);

    /**
     * Delete the "id" competition.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
