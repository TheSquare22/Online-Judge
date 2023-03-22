package com.olimpiici.arena.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TagCollectionTagMapperTest {

    private TagCollectionTagMapper tagCollectionTagMapper;

    @BeforeEach
    public void setUp() {
        tagCollectionTagMapper = new TagCollectionTagMapperImpl();
    }
}
