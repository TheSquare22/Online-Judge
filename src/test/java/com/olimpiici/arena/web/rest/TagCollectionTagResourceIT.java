package com.olimpiici.arena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.olimpiici.arena.IntegrationTest;
import com.olimpiici.arena.domain.TagCollectionTag;
import com.olimpiici.arena.repository.TagCollectionTagRepository;
import com.olimpiici.arena.service.dto.TagCollectionTagDTO;
import com.olimpiici.arena.service.mapper.TagCollectionTagMapper;
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
 * Integration tests for the {@link TagCollectionTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagCollectionTagResourceIT {

    private static final String ENTITY_API_URL = "/api/tag-collection-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TagCollectionTagRepository tagCollectionTagRepository;

    @Autowired
    private TagCollectionTagMapper tagCollectionTagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagCollectionTagMockMvc;

    private TagCollectionTag tagCollectionTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagCollectionTag createEntity(EntityManager em) {
        TagCollectionTag tagCollectionTag = new TagCollectionTag();
        return tagCollectionTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagCollectionTag createUpdatedEntity(EntityManager em) {
        TagCollectionTag tagCollectionTag = new TagCollectionTag();
        return tagCollectionTag;
    }

    @BeforeEach
    public void initTest() {
        tagCollectionTag = createEntity(em);
    }

    @Test
    @Transactional
    void createTagCollectionTag() throws Exception {
        int databaseSizeBeforeCreate = tagCollectionTagRepository.findAll().size();
        // Create the TagCollectionTag
        TagCollectionTagDTO tagCollectionTagDTO = tagCollectionTagMapper.toDto(tagCollectionTag);
        restTagCollectionTagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagCollectionTagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TagCollectionTag in the database
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeCreate + 1);
        TagCollectionTag testTagCollectionTag = tagCollectionTagList.get(tagCollectionTagList.size() - 1);
    }

    @Test
    @Transactional
    void createTagCollectionTagWithExistingId() throws Exception {
        // Create the TagCollectionTag with an existing ID
        tagCollectionTag.setId(1L);
        TagCollectionTagDTO tagCollectionTagDTO = tagCollectionTagMapper.toDto(tagCollectionTag);

        int databaseSizeBeforeCreate = tagCollectionTagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagCollectionTagMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagCollectionTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCollectionTag in the database
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTagCollectionTags() throws Exception {
        // Initialize the database
        tagCollectionTagRepository.saveAndFlush(tagCollectionTag);

        // Get all the tagCollectionTagList
        restTagCollectionTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tagCollectionTag.getId().intValue())));
    }

    @Test
    @Transactional
    void getTagCollectionTag() throws Exception {
        // Initialize the database
        tagCollectionTagRepository.saveAndFlush(tagCollectionTag);

        // Get the tagCollectionTag
        restTagCollectionTagMockMvc
            .perform(get(ENTITY_API_URL_ID, tagCollectionTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tagCollectionTag.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTagCollectionTag() throws Exception {
        // Get the tagCollectionTag
        restTagCollectionTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTagCollectionTag() throws Exception {
        // Initialize the database
        tagCollectionTagRepository.saveAndFlush(tagCollectionTag);

        int databaseSizeBeforeUpdate = tagCollectionTagRepository.findAll().size();

        // Update the tagCollectionTag
        TagCollectionTag updatedTagCollectionTag = tagCollectionTagRepository.findById(tagCollectionTag.getId()).get();
        // Disconnect from session so that the updates on updatedTagCollectionTag are not directly saved in db
        em.detach(updatedTagCollectionTag);
        TagCollectionTagDTO tagCollectionTagDTO = tagCollectionTagMapper.toDto(updatedTagCollectionTag);

        restTagCollectionTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagCollectionTagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionTagDTO))
            )
            .andExpect(status().isOk());

        // Validate the TagCollectionTag in the database
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeUpdate);
        TagCollectionTag testTagCollectionTag = tagCollectionTagList.get(tagCollectionTagList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingTagCollectionTag() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionTagRepository.findAll().size();
        tagCollectionTag.setId(count.incrementAndGet());

        // Create the TagCollectionTag
        TagCollectionTagDTO tagCollectionTagDTO = tagCollectionTagMapper.toDto(tagCollectionTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagCollectionTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagCollectionTagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCollectionTag in the database
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTagCollectionTag() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionTagRepository.findAll().size();
        tagCollectionTag.setId(count.incrementAndGet());

        // Create the TagCollectionTag
        TagCollectionTagDTO tagCollectionTagDTO = tagCollectionTagMapper.toDto(tagCollectionTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCollectionTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCollectionTag in the database
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTagCollectionTag() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionTagRepository.findAll().size();
        tagCollectionTag.setId(count.incrementAndGet());

        // Create the TagCollectionTag
        TagCollectionTagDTO tagCollectionTagDTO = tagCollectionTagMapper.toDto(tagCollectionTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCollectionTagMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagCollectionTagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TagCollectionTag in the database
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTagCollectionTagWithPatch() throws Exception {
        // Initialize the database
        tagCollectionTagRepository.saveAndFlush(tagCollectionTag);

        int databaseSizeBeforeUpdate = tagCollectionTagRepository.findAll().size();

        // Update the tagCollectionTag using partial update
        TagCollectionTag partialUpdatedTagCollectionTag = new TagCollectionTag();
        partialUpdatedTagCollectionTag.setId(tagCollectionTag.getId());

        restTagCollectionTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagCollectionTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTagCollectionTag))
            )
            .andExpect(status().isOk());

        // Validate the TagCollectionTag in the database
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeUpdate);
        TagCollectionTag testTagCollectionTag = tagCollectionTagList.get(tagCollectionTagList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateTagCollectionTagWithPatch() throws Exception {
        // Initialize the database
        tagCollectionTagRepository.saveAndFlush(tagCollectionTag);

        int databaseSizeBeforeUpdate = tagCollectionTagRepository.findAll().size();

        // Update the tagCollectionTag using partial update
        TagCollectionTag partialUpdatedTagCollectionTag = new TagCollectionTag();
        partialUpdatedTagCollectionTag.setId(tagCollectionTag.getId());

        restTagCollectionTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagCollectionTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTagCollectionTag))
            )
            .andExpect(status().isOk());

        // Validate the TagCollectionTag in the database
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeUpdate);
        TagCollectionTag testTagCollectionTag = tagCollectionTagList.get(tagCollectionTagList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingTagCollectionTag() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionTagRepository.findAll().size();
        tagCollectionTag.setId(count.incrementAndGet());

        // Create the TagCollectionTag
        TagCollectionTagDTO tagCollectionTagDTO = tagCollectionTagMapper.toDto(tagCollectionTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagCollectionTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tagCollectionTagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCollectionTag in the database
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTagCollectionTag() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionTagRepository.findAll().size();
        tagCollectionTag.setId(count.incrementAndGet());

        // Create the TagCollectionTag
        TagCollectionTagDTO tagCollectionTagDTO = tagCollectionTagMapper.toDto(tagCollectionTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCollectionTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCollectionTag in the database
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTagCollectionTag() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionTagRepository.findAll().size();
        tagCollectionTag.setId(count.incrementAndGet());

        // Create the TagCollectionTag
        TagCollectionTagDTO tagCollectionTagDTO = tagCollectionTagMapper.toDto(tagCollectionTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCollectionTagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionTagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TagCollectionTag in the database
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTagCollectionTag() throws Exception {
        // Initialize the database
        tagCollectionTagRepository.saveAndFlush(tagCollectionTag);

        int databaseSizeBeforeDelete = tagCollectionTagRepository.findAll().size();

        // Delete the tagCollectionTag
        restTagCollectionTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, tagCollectionTag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TagCollectionTag> tagCollectionTagList = tagCollectionTagRepository.findAll();
        assertThat(tagCollectionTagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
