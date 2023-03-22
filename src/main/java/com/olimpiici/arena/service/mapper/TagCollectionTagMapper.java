package com.olimpiici.arena.service.mapper;

import com.olimpiici.arena.domain.Tag;
import com.olimpiici.arena.domain.TagCollection;
import com.olimpiici.arena.domain.TagCollectionTag;
import com.olimpiici.arena.service.dto.TagCollectionDTO;
import com.olimpiici.arena.service.dto.TagCollectionTagDTO;
import com.olimpiici.arena.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TagCollectionTag} and its DTO {@link TagCollectionTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagCollectionTagMapper extends EntityMapper<TagCollectionTagDTO, TagCollectionTag> {
    @Mapping(target = "collection", source = "collection", qualifiedByName = "tagCollectionId")
    @Mapping(target = "tag", source = "tag", qualifiedByName = "tagId")
    TagCollectionTagDTO toDto(TagCollectionTag s);

    @Named("tagCollectionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagCollectionDTO toDtoTagCollectionId(TagCollection tagCollection);

    @Named("tagId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagDTO toDtoTagId(Tag tag);
}
