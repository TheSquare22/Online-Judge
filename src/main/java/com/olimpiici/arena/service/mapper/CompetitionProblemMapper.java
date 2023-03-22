package com.olimpiici.arena.service.mapper;

import com.olimpiici.arena.domain.Competition;
import com.olimpiici.arena.domain.CompetitionProblem;
import com.olimpiici.arena.domain.Problem;
import com.olimpiici.arena.service.dto.CompetitionDTO;
import com.olimpiici.arena.service.dto.CompetitionProblemDTO;
import com.olimpiici.arena.service.dto.ProblemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompetitionProblem} and its DTO {@link CompetitionProblemDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompetitionProblemMapper extends EntityMapper<CompetitionProblemDTO, CompetitionProblem> {
    @Mapping(target = "problem", source = "problem", qualifiedByName = "problemId")
    @Mapping(target = "competition", source = "competition", qualifiedByName = "competitionId")
    CompetitionProblemDTO toDto(CompetitionProblem s);

    @Named("problemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProblemDTO toDtoProblemId(Problem problem);

    @Named("competitionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompetitionDTO toDtoCompetitionId(Competition competition);
}
