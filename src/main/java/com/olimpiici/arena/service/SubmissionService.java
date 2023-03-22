package com.olimpiici.arena.service;

import com.olimpiici.arena.service.dto.SubmissionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.olimpiici.arena.domain.Submission}.
 */
public interface SubmissionService {
    /**
     * Save a submission.
     *
     * @param submissionDTO the entity to save.
     * @return the persisted entity.
     */
    SubmissionDTO save(SubmissionDTO submissionDTO);

    /**
     * Updates a submission.
     *
     * @param submissionDTO the entity to update.
     * @return the persisted entity.
     */
    SubmissionDTO update(SubmissionDTO submissionDTO);

    /**
     * Partially updates a submission.
     *
     * @param submissionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubmissionDTO> partialUpdate(SubmissionDTO submissionDTO);

    /**
     * Get all the submissions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubmissionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" submission.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubmissionDTO> findOne(Long id);

    /**
     * Delete the "id" submission.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
