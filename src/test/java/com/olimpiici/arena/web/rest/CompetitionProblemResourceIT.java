package com.olimpiici.arena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.olimpiici.arena.IntegrationTest;
import com.olimpiici.arena.domain.CompetitionProblem;
import com.olimpiici.arena.repository.CompetitionProblemRepository;
import com.olimpiici.arena.service.dto.CompetitionProblemDTO;
import com.olimpiici.arena.service.mapper.CompetitionProblemMapper;
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
 * Integration tests for the {@link CompetitionProblemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompetitionProblemResourceIT {

    private static final Integer DEFAULT_ORDER = 1;
    private static final Integer UPDATED_ORDER = 2;

    private static final String ENTITY_API_URL = "/api/competition-problems";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompetitionProblemRepository competitionProblemRepository;

    @Autowired
    private CompetitionProblemMapper competitionProblemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompetitionProblemMockMvc;

    private CompetitionProblem competitionProblem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompetitionProblem createEntity(EntityManager em) {
        CompetitionProblem competitionProblem = new CompetitionProblem().order(DEFAULT_ORDER);
        return competitionProblem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompetitionProblem createUpdatedEntity(EntityManager em) {
        CompetitionProblem competitionProblem = new CompetitionProblem().order(UPDATED_ORDER);
        return competitionProblem;
    }

    @BeforeEach
    public void initTest() {
        competitionProblem = createEntity(em);
    }

    @Test
    @Transactional
    void createCompetitionProblem() throws Exception {
        int databaseSizeBeforeCreate = competitionProblemRepository.findAll().size();
        // Create the CompetitionProblem
        CompetitionProblemDTO competitionProblemDTO = competitionProblemMapper.toDto(competitionProblem);
        restCompetitionProblemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionProblemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CompetitionProblem in the database
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeCreate + 1);
        CompetitionProblem testCompetitionProblem = competitionProblemList.get(competitionProblemList.size() - 1);
        assertThat(testCompetitionProblem.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void createCompetitionProblemWithExistingId() throws Exception {
        // Create the CompetitionProblem with an existing ID
        competitionProblem.setId(1L);
        CompetitionProblemDTO competitionProblemDTO = competitionProblemMapper.toDto(competitionProblem);

        int databaseSizeBeforeCreate = competitionProblemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompetitionProblemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionProblemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetitionProblem in the database
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompetitionProblems() throws Exception {
        // Initialize the database
        competitionProblemRepository.saveAndFlush(competitionProblem);

        // Get all the competitionProblemList
        restCompetitionProblemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(competitionProblem.getId().intValue())))
            .andExpect(jsonPath("$.[*].order").value(hasItem(DEFAULT_ORDER)));
    }

    @Test
    @Transactional
    void getCompetitionProblem() throws Exception {
        // Initialize the database
        competitionProblemRepository.saveAndFlush(competitionProblem);

        // Get the competitionProblem
        restCompetitionProblemMockMvc
            .perform(get(ENTITY_API_URL_ID, competitionProblem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(competitionProblem.getId().intValue()))
            .andExpect(jsonPath("$.order").value(DEFAULT_ORDER));
    }

    @Test
    @Transactional
    void getNonExistingCompetitionProblem() throws Exception {
        // Get the competitionProblem
        restCompetitionProblemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompetitionProblem() throws Exception {
        // Initialize the database
        competitionProblemRepository.saveAndFlush(competitionProblem);

        int databaseSizeBeforeUpdate = competitionProblemRepository.findAll().size();

        // Update the competitionProblem
        CompetitionProblem updatedCompetitionProblem = competitionProblemRepository.findById(competitionProblem.getId()).get();
        // Disconnect from session so that the updates on updatedCompetitionProblem are not directly saved in db
        em.detach(updatedCompetitionProblem);
        updatedCompetitionProblem.order(UPDATED_ORDER);
        CompetitionProblemDTO competitionProblemDTO = competitionProblemMapper.toDto(updatedCompetitionProblem);

        restCompetitionProblemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, competitionProblemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionProblemDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompetitionProblem in the database
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeUpdate);
        CompetitionProblem testCompetitionProblem = competitionProblemList.get(competitionProblemList.size() - 1);
        assertThat(testCompetitionProblem.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingCompetitionProblem() throws Exception {
        int databaseSizeBeforeUpdate = competitionProblemRepository.findAll().size();
        competitionProblem.setId(count.incrementAndGet());

        // Create the CompetitionProblem
        CompetitionProblemDTO competitionProblemDTO = competitionProblemMapper.toDto(competitionProblem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetitionProblemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, competitionProblemDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionProblemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetitionProblem in the database
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompetitionProblem() throws Exception {
        int databaseSizeBeforeUpdate = competitionProblemRepository.findAll().size();
        competitionProblem.setId(count.incrementAndGet());

        // Create the CompetitionProblem
        CompetitionProblemDTO competitionProblemDTO = competitionProblemMapper.toDto(competitionProblem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionProblemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionProblemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetitionProblem in the database
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompetitionProblem() throws Exception {
        int databaseSizeBeforeUpdate = competitionProblemRepository.findAll().size();
        competitionProblem.setId(count.incrementAndGet());

        // Create the CompetitionProblem
        CompetitionProblemDTO competitionProblemDTO = competitionProblemMapper.toDto(competitionProblem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionProblemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(competitionProblemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompetitionProblem in the database
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompetitionProblemWithPatch() throws Exception {
        // Initialize the database
        competitionProblemRepository.saveAndFlush(competitionProblem);

        int databaseSizeBeforeUpdate = competitionProblemRepository.findAll().size();

        // Update the competitionProblem using partial update
        CompetitionProblem partialUpdatedCompetitionProblem = new CompetitionProblem();
        partialUpdatedCompetitionProblem.setId(competitionProblem.getId());

        restCompetitionProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetitionProblem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompetitionProblem))
            )
            .andExpect(status().isOk());

        // Validate the CompetitionProblem in the database
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeUpdate);
        CompetitionProblem testCompetitionProblem = competitionProblemList.get(competitionProblemList.size() - 1);
        assertThat(testCompetitionProblem.getOrder()).isEqualTo(DEFAULT_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateCompetitionProblemWithPatch() throws Exception {
        // Initialize the database
        competitionProblemRepository.saveAndFlush(competitionProblem);

        int databaseSizeBeforeUpdate = competitionProblemRepository.findAll().size();

        // Update the competitionProblem using partial update
        CompetitionProblem partialUpdatedCompetitionProblem = new CompetitionProblem();
        partialUpdatedCompetitionProblem.setId(competitionProblem.getId());

        partialUpdatedCompetitionProblem.order(UPDATED_ORDER);

        restCompetitionProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompetitionProblem.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompetitionProblem))
            )
            .andExpect(status().isOk());

        // Validate the CompetitionProblem in the database
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeUpdate);
        CompetitionProblem testCompetitionProblem = competitionProblemList.get(competitionProblemList.size() - 1);
        assertThat(testCompetitionProblem.getOrder()).isEqualTo(UPDATED_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingCompetitionProblem() throws Exception {
        int databaseSizeBeforeUpdate = competitionProblemRepository.findAll().size();
        competitionProblem.setId(count.incrementAndGet());

        // Create the CompetitionProblem
        CompetitionProblemDTO competitionProblemDTO = competitionProblemMapper.toDto(competitionProblem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompetitionProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, competitionProblemDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competitionProblemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetitionProblem in the database
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompetitionProblem() throws Exception {
        int databaseSizeBeforeUpdate = competitionProblemRepository.findAll().size();
        competitionProblem.setId(count.incrementAndGet());

        // Create the CompetitionProblem
        CompetitionProblemDTO competitionProblemDTO = competitionProblemMapper.toDto(competitionProblem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionProblemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competitionProblemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompetitionProblem in the database
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompetitionProblem() throws Exception {
        int databaseSizeBeforeUpdate = competitionProblemRepository.findAll().size();
        competitionProblem.setId(count.incrementAndGet());

        // Create the CompetitionProblem
        CompetitionProblemDTO competitionProblemDTO = competitionProblemMapper.toDto(competitionProblem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompetitionProblemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(competitionProblemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompetitionProblem in the database
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompetitionProblem() throws Exception {
        // Initialize the database
        competitionProblemRepository.saveAndFlush(competitionProblem);

        int databaseSizeBeforeDelete = competitionProblemRepository.findAll().size();

        // Delete the competitionProblem
        restCompetitionProblemMockMvc
            .perform(delete(ENTITY_API_URL_ID, competitionProblem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompetitionProblem> competitionProblemList = competitionProblemRepository.findAll();
        assertThat(competitionProblemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
