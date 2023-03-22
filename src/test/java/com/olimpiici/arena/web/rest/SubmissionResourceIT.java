package com.olimpiici.arena.web.rest;

import static com.olimpiici.arena.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.olimpiici.arena.IntegrationTest;
import com.olimpiici.arena.domain.Submission;
import com.olimpiici.arena.repository.SubmissionRepository;
import com.olimpiici.arena.service.dto.SubmissionDTO;
import com.olimpiici.arena.service.mapper.SubmissionMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link SubmissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SubmissionResourceIT {

    private static final String DEFAULT_FILE = "AAAAAAAAAA";
    private static final String UPDATED_FILE = "BBBBBBBBBB";

    private static final String DEFAULT_VERDICT = "AAAAAAAAAA";
    private static final String UPDATED_VERDICT = "BBBBBBBBBB";

    private static final String DEFAULT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_DETAILS = "BBBBBBBBBB";

    private static final Integer DEFAULT_POINTS = 1;
    private static final Integer UPDATED_POINTS = 2;

    private static final Integer DEFAULT_TIME_IN_MILLIS = 1;
    private static final Integer UPDATED_TIME_IN_MILLIS = 2;

    private static final Integer DEFAULT_MEMORY_IN_BYTES = 1;
    private static final Integer UPDATED_MEMORY_IN_BYTES = 2;

    private static final ZonedDateTime DEFAULT_UPLOAD_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPLOAD_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_SECURITY_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SECURITY_KEY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/submissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private SubmissionMapper submissionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSubmissionMockMvc;

    private Submission submission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createEntity(EntityManager em) {
        Submission submission = new Submission()
            .file(DEFAULT_FILE)
            .verdict(DEFAULT_VERDICT)
            .details(DEFAULT_DETAILS)
            .points(DEFAULT_POINTS)
            .timeInMillis(DEFAULT_TIME_IN_MILLIS)
            .memoryInBytes(DEFAULT_MEMORY_IN_BYTES)
            .uploadDate(DEFAULT_UPLOAD_DATE)
            .securityKey(DEFAULT_SECURITY_KEY);
        return submission;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Submission createUpdatedEntity(EntityManager em) {
        Submission submission = new Submission()
            .file(UPDATED_FILE)
            .verdict(UPDATED_VERDICT)
            .details(UPDATED_DETAILS)
            .points(UPDATED_POINTS)
            .timeInMillis(UPDATED_TIME_IN_MILLIS)
            .memoryInBytes(UPDATED_MEMORY_IN_BYTES)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .securityKey(UPDATED_SECURITY_KEY);
        return submission;
    }

    @BeforeEach
    public void initTest() {
        submission = createEntity(em);
    }

    @Test
    @Transactional
    void createSubmission() throws Exception {
        int databaseSizeBeforeCreate = submissionRepository.findAll().size();
        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);
        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isCreated());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeCreate + 1);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getFile()).isEqualTo(DEFAULT_FILE);
        assertThat(testSubmission.getVerdict()).isEqualTo(DEFAULT_VERDICT);
        assertThat(testSubmission.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testSubmission.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testSubmission.getTimeInMillis()).isEqualTo(DEFAULT_TIME_IN_MILLIS);
        assertThat(testSubmission.getMemoryInBytes()).isEqualTo(DEFAULT_MEMORY_IN_BYTES);
        assertThat(testSubmission.getUploadDate()).isEqualTo(DEFAULT_UPLOAD_DATE);
        assertThat(testSubmission.getSecurityKey()).isEqualTo(DEFAULT_SECURITY_KEY);
    }

    @Test
    @Transactional
    void createSubmissionWithExistingId() throws Exception {
        // Create the Submission with an existing ID
        submission.setId(1L);
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        int databaseSizeBeforeCreate = submissionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSubmissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSubmissions() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get all the submissionList
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(submission.getId().intValue())))
            .andExpect(jsonPath("$.[*].file").value(hasItem(DEFAULT_FILE)))
            .andExpect(jsonPath("$.[*].verdict").value(hasItem(DEFAULT_VERDICT)))
            .andExpect(jsonPath("$.[*].details").value(hasItem(DEFAULT_DETAILS)))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS)))
            .andExpect(jsonPath("$.[*].timeInMillis").value(hasItem(DEFAULT_TIME_IN_MILLIS)))
            .andExpect(jsonPath("$.[*].memoryInBytes").value(hasItem(DEFAULT_MEMORY_IN_BYTES)))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(sameInstant(DEFAULT_UPLOAD_DATE))))
            .andExpect(jsonPath("$.[*].securityKey").value(hasItem(DEFAULT_SECURITY_KEY)));
    }

    @Test
    @Transactional
    void getSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        // Get the submission
        restSubmissionMockMvc
            .perform(get(ENTITY_API_URL_ID, submission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(submission.getId().intValue()))
            .andExpect(jsonPath("$.file").value(DEFAULT_FILE))
            .andExpect(jsonPath("$.verdict").value(DEFAULT_VERDICT))
            .andExpect(jsonPath("$.details").value(DEFAULT_DETAILS))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS))
            .andExpect(jsonPath("$.timeInMillis").value(DEFAULT_TIME_IN_MILLIS))
            .andExpect(jsonPath("$.memoryInBytes").value(DEFAULT_MEMORY_IN_BYTES))
            .andExpect(jsonPath("$.uploadDate").value(sameInstant(DEFAULT_UPLOAD_DATE)))
            .andExpect(jsonPath("$.securityKey").value(DEFAULT_SECURITY_KEY));
    }

    @Test
    @Transactional
    void getNonExistingSubmission() throws Exception {
        // Get the submission
        restSubmissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Update the submission
        Submission updatedSubmission = submissionRepository.findById(submission.getId()).get();
        // Disconnect from session so that the updates on updatedSubmission are not directly saved in db
        em.detach(updatedSubmission);
        updatedSubmission
            .file(UPDATED_FILE)
            .verdict(UPDATED_VERDICT)
            .details(UPDATED_DETAILS)
            .points(UPDATED_POINTS)
            .timeInMillis(UPDATED_TIME_IN_MILLIS)
            .memoryInBytes(UPDATED_MEMORY_IN_BYTES)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .securityKey(UPDATED_SECURITY_KEY);
        SubmissionDTO submissionDTO = submissionMapper.toDto(updatedSubmission);

        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testSubmission.getVerdict()).isEqualTo(UPDATED_VERDICT);
        assertThat(testSubmission.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testSubmission.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testSubmission.getTimeInMillis()).isEqualTo(UPDATED_TIME_IN_MILLIS);
        assertThat(testSubmission.getMemoryInBytes()).isEqualTo(UPDATED_MEMORY_IN_BYTES);
        assertThat(testSubmission.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
        assertThat(testSubmission.getSecurityKey()).isEqualTo(UPDATED_SECURITY_KEY);
    }

    @Test
    @Transactional
    void putNonExistingSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(submissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSubmissionWithPatch() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Update the submission using partial update
        Submission partialUpdatedSubmission = new Submission();
        partialUpdatedSubmission.setId(submission.getId());

        partialUpdatedSubmission.file(UPDATED_FILE).verdict(UPDATED_VERDICT);

        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubmission))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testSubmission.getVerdict()).isEqualTo(UPDATED_VERDICT);
        assertThat(testSubmission.getDetails()).isEqualTo(DEFAULT_DETAILS);
        assertThat(testSubmission.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testSubmission.getTimeInMillis()).isEqualTo(DEFAULT_TIME_IN_MILLIS);
        assertThat(testSubmission.getMemoryInBytes()).isEqualTo(DEFAULT_MEMORY_IN_BYTES);
        assertThat(testSubmission.getUploadDate()).isEqualTo(DEFAULT_UPLOAD_DATE);
        assertThat(testSubmission.getSecurityKey()).isEqualTo(DEFAULT_SECURITY_KEY);
    }

    @Test
    @Transactional
    void fullUpdateSubmissionWithPatch() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();

        // Update the submission using partial update
        Submission partialUpdatedSubmission = new Submission();
        partialUpdatedSubmission.setId(submission.getId());

        partialUpdatedSubmission
            .file(UPDATED_FILE)
            .verdict(UPDATED_VERDICT)
            .details(UPDATED_DETAILS)
            .points(UPDATED_POINTS)
            .timeInMillis(UPDATED_TIME_IN_MILLIS)
            .memoryInBytes(UPDATED_MEMORY_IN_BYTES)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .securityKey(UPDATED_SECURITY_KEY);

        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSubmission.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSubmission))
            )
            .andExpect(status().isOk());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
        Submission testSubmission = submissionList.get(submissionList.size() - 1);
        assertThat(testSubmission.getFile()).isEqualTo(UPDATED_FILE);
        assertThat(testSubmission.getVerdict()).isEqualTo(UPDATED_VERDICT);
        assertThat(testSubmission.getDetails()).isEqualTo(UPDATED_DETAILS);
        assertThat(testSubmission.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testSubmission.getTimeInMillis()).isEqualTo(UPDATED_TIME_IN_MILLIS);
        assertThat(testSubmission.getMemoryInBytes()).isEqualTo(UPDATED_MEMORY_IN_BYTES);
        assertThat(testSubmission.getUploadDate()).isEqualTo(UPDATED_UPLOAD_DATE);
        assertThat(testSubmission.getSecurityKey()).isEqualTo(UPDATED_SECURITY_KEY);
    }

    @Test
    @Transactional
    void patchNonExistingSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, submissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSubmission() throws Exception {
        int databaseSizeBeforeUpdate = submissionRepository.findAll().size();
        submission.setId(count.incrementAndGet());

        // Create the Submission
        SubmissionDTO submissionDTO = submissionMapper.toDto(submission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSubmissionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(submissionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Submission in the database
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSubmission() throws Exception {
        // Initialize the database
        submissionRepository.saveAndFlush(submission);

        int databaseSizeBeforeDelete = submissionRepository.findAll().size();

        // Delete the submission
        restSubmissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, submission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Submission> submissionList = submissionRepository.findAll();
        assertThat(submissionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
