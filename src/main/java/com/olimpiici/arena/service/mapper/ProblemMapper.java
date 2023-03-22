package com.olimpiici.arena.service.mapper;

import com.olimpiici.arena.domain.Problem;
import com.olimpiici.arena.domain.TagCollection;
import com.olimpiici.arena.service.dto.ProblemDTO;
import com.olimpiici.arena.service.dto.TagCollectionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Problem} and its DTO {@link ProblemDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProblemMapper extends EntityMapper<ProblemDTO, Problem> {
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagCollectionId")
    ProblemDTO toDto(Problem s);

    @Named("tagCollectionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagCollectionDTO toDtoTagCollectionId(TagCollection tagCollection);
}
