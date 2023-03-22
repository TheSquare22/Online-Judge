package com.olimpiici.arena.service.mapper;

import com.olimpiici.arena.domain.TagCollection;
import com.olimpiici.arena.service.dto.TagCollectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TagCollection} and its DTO {@link TagCollectionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagCollectionMapper extends EntityMapper<TagCollectionDTO, TagCollection> {}
