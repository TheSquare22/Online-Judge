package com.olimpiici.arena.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.olimpiici.arena.IntegrationTest;
import com.olimpiici.arena.domain.TagCollection;
import com.olimpiici.arena.repository.TagCollectionRepository;
import com.olimpiici.arena.service.dto.TagCollectionDTO;
import com.olimpiici.arena.service.mapper.TagCollectionMapper;
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
 * Integration tests for the {@link TagCollectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagCollectionResourceIT {

    private static final String ENTITY_API_URL = "/api/tag-collections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TagCollectionRepository tagCollectionRepository;

    @Autowired
    private TagCollectionMapper tagCollectionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagCollectionMockMvc;

    private TagCollection tagCollection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagCollection createEntity(EntityManager em) {
        TagCollection tagCollection = new TagCollection();
        return tagCollection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagCollection createUpdatedEntity(EntityManager em) {
        TagCollection tagCollection = new TagCollection();
        return tagCollection;
    }

    @BeforeEach
    public void initTest() {
        tagCollection = createEntity(em);
    }

    @Test
    @Transactional
    void createTagCollection() throws Exception {
        int databaseSizeBeforeCreate = tagCollectionRepository.findAll().size();
        // Create the TagCollection
        TagCollectionDTO tagCollectionDTO = tagCollectionMapper.toDto(tagCollection);
        restTagCollectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagCollectionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the TagCollection in the database
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeCreate + 1);
        TagCollection testTagCollection = tagCollectionList.get(tagCollectionList.size() - 1);
    }

    @Test
    @Transactional
    void createTagCollectionWithExistingId() throws Exception {
        // Create the TagCollection with an existing ID
        tagCollection.setId(1L);
        TagCollectionDTO tagCollectionDTO = tagCollectionMapper.toDto(tagCollection);

        int databaseSizeBeforeCreate = tagCollectionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagCollectionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagCollectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCollection in the database
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTagCollections() throws Exception {
        // Initialize the database
        tagCollectionRepository.saveAndFlush(tagCollection);

        // Get all the tagCollectionList
        restTagCollectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tagCollection.getId().intValue())));
    }

    @Test
    @Transactional
    void getTagCollection() throws Exception {
        // Initialize the database
        tagCollectionRepository.saveAndFlush(tagCollection);

        // Get the tagCollection
        restTagCollectionMockMvc
            .perform(get(ENTITY_API_URL_ID, tagCollection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tagCollection.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTagCollection() throws Exception {
        // Get the tagCollection
        restTagCollectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTagCollection() throws Exception {
        // Initialize the database
        tagCollectionRepository.saveAndFlush(tagCollection);

        int databaseSizeBeforeUpdate = tagCollectionRepository.findAll().size();

        // Update the tagCollection
        TagCollection updatedTagCollection = tagCollectionRepository.findById(tagCollection.getId()).get();
        // Disconnect from session so that the updates on updatedTagCollection are not directly saved in db
        em.detach(updatedTagCollection);
        TagCollectionDTO tagCollectionDTO = tagCollectionMapper.toDto(updatedTagCollection);

        restTagCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagCollectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TagCollection in the database
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeUpdate);
        TagCollection testTagCollection = tagCollectionList.get(tagCollectionList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingTagCollection() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionRepository.findAll().size();
        tagCollection.setId(count.incrementAndGet());

        // Create the TagCollection
        TagCollectionDTO tagCollectionDTO = tagCollectionMapper.toDto(tagCollection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagCollectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCollection in the database
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTagCollection() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionRepository.findAll().size();
        tagCollection.setId(count.incrementAndGet());

        // Create the TagCollection
        TagCollectionDTO tagCollectionDTO = tagCollectionMapper.toDto(tagCollection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCollection in the database
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTagCollection() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionRepository.findAll().size();
        tagCollection.setId(count.incrementAndGet());

        // Create the TagCollection
        TagCollectionDTO tagCollectionDTO = tagCollectionMapper.toDto(tagCollection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCollectionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(tagCollectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TagCollection in the database
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTagCollectionWithPatch() throws Exception {
        // Initialize the database
        tagCollectionRepository.saveAndFlush(tagCollection);

        int databaseSizeBeforeUpdate = tagCollectionRepository.findAll().size();

        // Update the tagCollection using partial update
        TagCollection partialUpdatedTagCollection = new TagCollection();
        partialUpdatedTagCollection.setId(tagCollection.getId());

        restTagCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTagCollection))
            )
            .andExpect(status().isOk());

        // Validate the TagCollection in the database
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeUpdate);
        TagCollection testTagCollection = tagCollectionList.get(tagCollectionList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateTagCollectionWithPatch() throws Exception {
        // Initialize the database
        tagCollectionRepository.saveAndFlush(tagCollection);

        int databaseSizeBeforeUpdate = tagCollectionRepository.findAll().size();

        // Update the tagCollection using partial update
        TagCollection partialUpdatedTagCollection = new TagCollection();
        partialUpdatedTagCollection.setId(tagCollection.getId());

        restTagCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTagCollection))
            )
            .andExpect(status().isOk());

        // Validate the TagCollection in the database
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeUpdate);
        TagCollection testTagCollection = tagCollectionList.get(tagCollectionList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingTagCollection() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionRepository.findAll().size();
        tagCollection.setId(count.incrementAndGet());

        // Create the TagCollection
        TagCollectionDTO tagCollectionDTO = tagCollectionMapper.toDto(tagCollection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tagCollectionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCollection in the database
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTagCollection() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionRepository.findAll().size();
        tagCollection.setId(count.incrementAndGet());

        // Create the TagCollection
        TagCollectionDTO tagCollectionDTO = tagCollectionMapper.toDto(tagCollection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCollection in the database
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTagCollection() throws Exception {
        int databaseSizeBeforeUpdate = tagCollectionRepository.findAll().size();
        tagCollection.setId(count.incrementAndGet());

        // Create the TagCollection
        TagCollectionDTO tagCollectionDTO = tagCollectionMapper.toDto(tagCollection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tagCollectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TagCollection in the database
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTagCollection() throws Exception {
        // Initialize the database
        tagCollectionRepository.saveAndFlush(tagCollection);

        int databaseSizeBeforeDelete = tagCollectionRepository.findAll().size();

        // Delete the tagCollection
        restTagCollectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, tagCollection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TagCollection> tagCollectionList = tagCollectionRepository.findAll();
        assertThat(tagCollectionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
