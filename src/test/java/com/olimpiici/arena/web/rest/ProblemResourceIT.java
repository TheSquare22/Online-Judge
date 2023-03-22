package com.olimpiici.arena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.olimpiici.arena.IntegrationTest;
import com.olimpiici.arena.domain.Problem;
import com.olimpiici.arena.repository.ProblemRepository;
import com.olimpiici.arena.service.dto.ProblemDTO;
import com.olimpiici.arena.service.mapper.ProblemMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProblemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProblemResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DIRECTORY = "AAAAAAAAAA";
    private static final String UPDATED_DIRECTORY = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final String ENTITY_API_URL = "/api/problems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ProblemMapper problemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProblemMockMvc;

    private Problem problem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Problem createEntity(EntityManager em) {
        Problem problem = new Problem().title(DEFAULT_TITLE).directory(DEFAULT_DIRECTORY).version(DEFAULT_VERSION);
        return problem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Problem createUpdatedEntity(EntityManager em) {
        Problem problem = new Problem().title(UPDATED_TITLE).directory(UPDATED_DIRECTORY).version(UPDATED_VERSION);
        return problem;
    }

    @BeforeEach
    public void initTest() {
        problem = createEntity(em);
    }

    @Test
    @Transactional
    void createProblem() throws Exception {
        int databaseSizeBeforeCreate = problemRepository.findAll().size();
        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);
        restProblemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemDTO)))
            .andExpect(status().isCreated());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeCreate + 1);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProblem.getDirectory()).isEqualTo(DEFAULT_DIRECTORY);
        assertThat(testProblem.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void createProblemWithExistingId() throws Exception {
        // Create the Problem with an existing ID
        problem.setId(1L);
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        int databaseSizeBeforeCreate = problemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProblemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllProblems() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get all the problemList
        restProblemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(problem.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].directory").value(hasItem(DEFAULT_DIRECTORY)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)));
    }

    @Test
    @Transactional
    void getProblem() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        // Get the problem
        restProblemMockMvc
            .perform(get(ENTITY_API_URL_ID, problem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(problem.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.directory").value(DEFAULT_DIRECTORY))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION));
    }

    @Test
    @Transactional
    void getNonExistingProblem() throws Exception {
        // Get the problem
        restProblemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProblem() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        int databaseSizeBeforeUpdate = problemRepository.findAll().size();

        // Update the problem
        Problem updatedProblem = problemRepository.findById(problem.getId()).get();
        // Disconnect from session so that the updates on updatedProblem are not directly saved in db
        em.detach(updatedProblem);
        updatedProblem.title(UPDATED_TITLE).directory(UPDATED_DIRECTORY).version(UPDATED_VERSION);
        ProblemDTO problemDTO = problemMapper.toDto(updatedProblem);

        restProblemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, problemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isOk());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProblem.getDirectory()).isEqualTo(UPDATED_DIRECTORY);
        assertThat(testProblem.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    void putNonExistingProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, problemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(problemDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProblemWithPatch() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        int databaseSizeBeforeUpdate = problemRepository.findAll().size();

        // Update the problem using partial update
        Problem partialUpdatedProblem = new Problem();
        partialUpdatedProblem.setId(problem.getId());

        restProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProblem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProblem))
            )
            .andExpect(status().isOk());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testProblem.getDirectory()).isEqualTo(DEFAULT_DIRECTORY);
        assertThat(testProblem.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void fullUpdateProblemWithPatch() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        int databaseSizeBeforeUpdate = problemRepository.findAll().size();

        // Update the problem using partial update
        Problem partialUpdatedProblem = new Problem();
        partialUpdatedProblem.setId(problem.getId());

        partialUpdatedProblem.title(UPDATED_TITLE).directory(UPDATED_DIRECTORY).version(UPDATED_VERSION);

        restProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProblem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProblem))
            )
            .andExpect(status().isOk());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
        Problem testProblem = problemList.get(problemList.size() - 1);
        assertThat(testProblem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testProblem.getDirectory()).isEqualTo(UPDATED_DIRECTORY);
        assertThat(testProblem.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    void patchNonExistingProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, problemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProblem() throws Exception {
        int databaseSizeBeforeUpdate = problemRepository.findAll().size();
        problem.setId(count.incrementAndGet());

        // Create the Problem
        ProblemDTO problemDTO = problemMapper.toDto(problem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProblemMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(problemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Problem in the database
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProblem() throws Exception {
        // Initialize the database
        problemRepository.saveAndFlush(problem);

        int databaseSizeBeforeDelete = problemRepository.findAll().size();

        // Delete the problem
        restProblemMockMvc
            .perform(delete(ENTITY_API_URL_ID, problem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Problem> problemList = problemRepository.findAll();
        assertThat(problemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
