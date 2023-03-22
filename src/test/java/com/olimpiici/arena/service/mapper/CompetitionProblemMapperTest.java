package com.olimpiici.arena.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompetitionProblemMapperTest {

    private CompetitionProblemMapper competitionProblemMapper;

    @BeforeEach
    public void setUp() {
        competitionProblemMapper = new CompetitionProblemMapperImpl();
    }
}
