package com.olimpiici.arena.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.olimpiici.arena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompetitionProblemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompetitionProblemDTO.class);
        CompetitionProblemDTO competitionProblemDTO1 = new CompetitionProblemDTO();
        competitionProblemDTO1.setId(1L);
        CompetitionProblemDTO competitionProblemDTO2 = new CompetitionProblemDTO();
        assertThat(competitionProblemDTO1).isNotEqualTo(competitionProblemDTO2);
        competitionProblemDTO2.setId(competitionProblemDTO1.getId());
        assertThat(competitionProblemDTO1).isEqualTo(competitionProblemDTO2);
        competitionProblemDTO2.setId(2L);
        assertThat(competitionProblemDTO1).isNotEqualTo(competitionProblemDTO2);
        competitionProblemDTO1.setId(null);
        assertThat(competitionProblemDTO1).isNotEqualTo(competitionProblemDTO2);
    }
}
