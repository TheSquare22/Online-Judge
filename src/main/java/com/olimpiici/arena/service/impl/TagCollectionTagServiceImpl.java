package com.olimpiici.arena.service.impl;

import com.olimpiici.arena.domain.TagCollectionTag;
import com.olimpiici.arena.repository.TagCollectionTagRepository;
import com.olimpiici.arena.service.TagCollectionTagService;
import com.olimpiici.arena.service.dto.TagCollectionTagDTO;
import com.olimpiici.arena.service.mapper.TagCollectionTagMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TagCollectionTag}.
 */
@Service
@Transactional
public class TagCollectionTagServiceImpl implements TagCollectionTagService {

    private final Logger log = LoggerFactory.getLogger(TagCollectionTagServiceImpl.class);

    private final TagCollectionTagRepository tagCollectionTagRepository;

    private final TagCollectionTagMapper tagCollectionTagMapper;

    public TagCollectionTagServiceImpl(
        TagCollectionTagRepository tagCollectionTagRepository,
        TagCollectionTagMapper tagCollectionTagMapper
    ) {
        this.tagCollectionTagRepository = tagCollectionTagRepository;
        this.tagCollectionTagMapper = tagCollectionTagMapper;
    }

    @Override
    public TagCollectionTagDTO save(TagCollectionTagDTO tagCollectionTagDTO) {
        log.debug("Request to save TagCollectionTag : {}", tagCollectionTagDTO);
        TagCollectionTag tagCollectionTag = tagCollectionTagMapper.toEntity(tagCollectionTagDTO);
        tagCollectionTag = tagCollectionTagRepository.save(tagCollectionTag);
        return tagCollectionTagMapper.toDto(tagCollectionTag);
    }

    @Override
    public TagCollectionTagDTO update(TagCollectionTagDTO tagCollectionTagDTO) {
        log.debug("Request to update TagCollectionTag : {}", tagCollectionTagDTO);
        TagCollectionTag tagCollectionTag = tagCollectionTagMapper.toEntity(tagCollectionTagDTO);
        tagCollectionTag = tagCollectionTagRepository.save(tagCollectionTag);
        return tagCollectionTagMapper.toDto(tagCollectionTag);
    }

    @Override
    public Optional<TagCollectionTagDTO> partialUpdate(TagCollectionTagDTO tagCollectionTagDTO) {
        log.debug("Request to partially update TagCollectionTag : {}", tagCollectionTagDTO);

        return tagCollectionTagRepository
            .findById(tagCollectionTagDTO.getId())
            .map(existingTagCollectionTag -> {
                tagCollectionTagMapper.partialUpdate(existingTagCollectionTag, tagCollectionTagDTO);

                return existingTagCollectionTag;
            })
            .map(tagCollectionTagRepository::save)
            .map(tagCollectionTagMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TagCollectionTagDTO> findAll() {
        log.debug("Request to get all TagCollectionTags");
        return tagCollectionTagRepository
            .findAll()
            .stream()
            .map(tagCollectionTagMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TagCollectionTagDTO> findOne(Long id) {
        log.debug("Request to get TagCollectionTag : {}", id);
        return tagCollectionTagRepository.findById(id).map(tagCollectionTagMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TagCollectionTag : {}", id);
        tagCollectionTagRepository.deleteById(id);
    }
}
