package com.olimpiici.arena.service.mapper;

import com.olimpiici.arena.domain.Competition;
import com.olimpiici.arena.service.dto.CompetitionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Competition} and its DTO {@link CompetitionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompetitionMapper extends EntityMapper<CompetitionDTO, Competition> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "competitionId")
    CompetitionDTO toDto(Competition s);

    @Named("competitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompetitionDTO toDtoCompetitionId(Competition competition);
}
