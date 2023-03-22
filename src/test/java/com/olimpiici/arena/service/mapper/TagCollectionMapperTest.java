package com.olimpiici.arena.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TagCollectionMapperTest {

    private TagCollectionMapper tagCollectionMapper;

    @BeforeEach
    public void setUp() {
        tagCollectionMapper = new TagCollectionMapperImpl();
    }
}
