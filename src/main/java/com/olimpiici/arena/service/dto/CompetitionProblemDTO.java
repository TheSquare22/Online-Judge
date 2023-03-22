package com.olimpiici.arena.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.olimpiici.arena.domain.CompetitionProblem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompetitionProblemDTO implements Serializable {

    private Long id;

    private Integer order;

    private ProblemDTO problem;

    private CompetitionDTO competition;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public ProblemDTO getProblem() {
        return problem;
    }

    public void setProblem(ProblemDTO problem) {
        this.problem = problem;
    }

    public CompetitionDTO getCompetition() {
        return competition;
    }

    public void setCompetition(CompetitionDTO competition) {
        this.competition = competition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompetitionProblemDTO)) {
            return false;
        }

        CompetitionProblemDTO competitionProblemDTO = (CompetitionProblemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, competitionProblemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompetitionProblemDTO{" +
            "id=" + getId() +
            ", order=" + getOrder() +
            ", problem=" + getProblem() +
            ", competition=" + getCompetition() +
            "}";
    }
}
