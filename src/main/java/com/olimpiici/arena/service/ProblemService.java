package com.olimpiici.arena.service;

import com.olimpiici.arena.service.dto.ProblemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.olimpiici.arena.domain.Problem}.
 */
public interface ProblemService {
    /**
     * Save a problem.
     *
     * @param problemDTO the entity to save.
     * @return the persisted entity.
     */
    ProblemDTO save(ProblemDTO problemDTO);

    /**
     * Updates a problem.
     *
     * @param problemDTO the entity to update.
     * @return the persisted entity.
     */
    ProblemDTO update(ProblemDTO problemDTO);

    /**
     * Partially updates a problem.
     *
     * @param problemDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProblemDTO> partialUpdate(ProblemDTO problemDTO);

    /**
     * Get all the problems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProblemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" problem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProblemDTO> findOne(Long id);

    /**
     * Delete the "id" problem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
