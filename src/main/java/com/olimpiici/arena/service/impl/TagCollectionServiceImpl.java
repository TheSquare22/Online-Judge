package com.olimpiici.arena.service.impl;

import com.olimpiici.arena.domain.TagCollection;
import com.olimpiici.arena.repository.TagCollectionRepository;
import com.olimpiici.arena.service.TagCollectionService;
import com.olimpiici.arena.service.dto.TagCollectionDTO;
import com.olimpiici.arena.service.mapper.TagCollectionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TagCollection}.
 */
@Service
@Transactional
public class TagCollectionServiceImpl implements TagCollectionService {

    private final Logger log = LoggerFactory.getLogger(TagCollectionServiceImpl.class);

    private final TagCollectionRepository tagCollectionRepository;

    private final TagCollectionMapper tagCollectionMapper;

    public TagCollectionServiceImpl(TagCollectionRepository tagCollectionRepository, TagCollectionMapper tagCollectionMapper) {
        this.tagCollectionRepository = tagCollectionRepository;
        this.tagCollectionMapper = tagCollectionMapper;
    }

    @Override
    public TagCollectionDTO save(TagCollectionDTO tagCollectionDTO) {
        log.debug("Request to save TagCollection : {}", tagCollectionDTO);
        TagCollection tagCollection = tagCollectionMapper.toEntity(tagCollectionDTO);
        tagCollection = tagCollectionRepository.save(tagCollection);
        return tagCollectionMapper.toDto(tagCollection);
    }

    @Override
    public TagCollectionDTO update(TagCollectionDTO tagCollectionDTO) {
        log.debug("Request to update TagCollection : {}", tagCollectionDTO);
        TagCollection tagCollection = tagCollectionMapper.toEntity(tagCollectionDTO);
        // no save call needed as we have no fields that can be updated
        return tagCollectionMapper.toDto(tagCollection);
    }

    @Override
    public Optional<TagCollectionDTO> partialUpdate(TagCollectionDTO tagCollectionDTO) {
        log.debug("Request to partially update TagCollection : {}", tagCollectionDTO);

        return tagCollectionRepository
            .findById(tagCollectionDTO.getId())
            .map(existingTagCollection -> {
                tagCollectionMapper.partialUpdate(existingTagCollection, tagCollectionDTO);

                return existingTagCollection;
            })
            // .map(tagCollectionRepository::save)
            .map(tagCollectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagCollectionDTO> findAll() {
        log.debug("Request to get all TagCollections");
        return tagCollectionRepository.findAll().stream().map(tagCollectionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TagCollectionDTO> findOne(Long id) {
        log.debug("Request to get TagCollection : {}", id);
        return tagCollectionRepository.findById(id).map(tagCollectionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TagCollection : {}", id);
        tagCollectionRepository.deleteById(id);
    }
}
