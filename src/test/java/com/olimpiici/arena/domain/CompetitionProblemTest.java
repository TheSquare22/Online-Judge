package com.olimpiici.arena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.olimpiici.arena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompetitionProblemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompetitionProblem.class);
        CompetitionProblem competitionProblem1 = new CompetitionProblem();
        competitionProblem1.setId(1L);
        CompetitionProblem competitionProblem2 = new CompetitionProblem();
        competitionProblem2.setId(competitionProblem1.getId());
        assertThat(competitionProblem1).isEqualTo(competitionProblem2);
        competitionProblem2.setId(2L);
        assertThat(competitionProblem1).isNotEqualTo(competitionProblem2);
        competitionProblem1.setId(null);
        assertThat(competitionProblem1).isNotEqualTo(competitionProblem2);
    }
}
