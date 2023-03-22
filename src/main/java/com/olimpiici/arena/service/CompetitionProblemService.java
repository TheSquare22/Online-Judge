package com.olimpiici.arena.service;

import com.olimpiici.arena.service.dto.CompetitionProblemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.olimpiici.arena.domain.CompetitionProblem}.
 */
public interface CompetitionProblemService {
    /**
     * Save a competitionProblem.
     *
     * @param competitionProblemDTO the entity to save.
     * @return the persisted entity.
     */
    CompetitionProblemDTO save(CompetitionProblemDTO competitionProblemDTO);

    /**
     * Updates a competitionProblem.
     *
     * @param competitionProblemDTO the entity to update.
     * @return the persisted entity.
     */
    CompetitionProblemDTO update(CompetitionProblemDTO competitionProblemDTO);

    /**
     * Partially updates a competitionProblem.
     *
     * @param competitionProblemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompetitionProblemDTO> partialUpdate(CompetitionProblemDTO competitionProblemDTO);

    /**
     * Get all the competitionProblems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompetitionProblemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" competitionProblem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompetitionProblemDTO> findOne(Long id);

    /**
     * Delete the "id" competitionProblem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
