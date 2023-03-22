package com.olimpiici.arena.service.mapper;

import com.olimpiici.arena.domain.CompetitionProblem;
import com.olimpiici.arena.domain.Submission;
import com.olimpiici.arena.domain.TagCollection;
import com.olimpiici.arena.domain.User;
import com.olimpiici.arena.service.dto.CompetitionProblemDTO;
import com.olimpiici.arena.service.dto.SubmissionDTO;
import com.olimpiici.arena.service.dto.TagCollectionDTO;
import com.olimpiici.arena.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Submission} and its DTO {@link SubmissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubmissionMapper extends EntityMapper<SubmissionDTO, Submission> {
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagCollectionId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "competitionProblem", source = "competitionProblem", qualifiedByName = "competitionProblemId")
    SubmissionDTO toDto(Submission s);

    @Named("tagCollectionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagCollectionDTO toDtoTagCollectionId(TagCollection tagCollection);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("competitionProblemId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CompetitionProblemDTO toDtoCompetitionProblemId(CompetitionProblem competitionProblem);
}
