package com.olimpiici.arena.service.mapper;

import com.olimpiici.arena.domain.Tag;
import com.olimpiici.arena.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {}
