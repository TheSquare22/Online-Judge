package com.olimpiici.arena.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.olimpiici.arena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagCollectionTagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagCollectionTagDTO.class);
        TagCollectionTagDTO tagCollectionTagDTO1 = new TagCollectionTagDTO();
        tagCollectionTagDTO1.setId(1L);
        TagCollectionTagDTO tagCollectionTagDTO2 = new TagCollectionTagDTO();
        assertThat(tagCollectionTagDTO1).isNotEqualTo(tagCollectionTagDTO2);
        tagCollectionTagDTO2.setId(tagCollectionTagDTO1.getId());
        assertThat(tagCollectionTagDTO1).isEqualTo(tagCollectionTagDTO2);
        tagCollectionTagDTO2.setId(2L);
        assertThat(tagCollectionTagDTO1).isNotEqualTo(tagCollectionTagDTO2);
        tagCollectionTagDTO1.setId(null);
        assertThat(tagCollectionTagDTO1).isNotEqualTo(tagCollectionTagDTO2);
    }
}
