package com.olimpiici.arena.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.olimpiici.arena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagCollectionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagCollectionDTO.class);
        TagCollectionDTO tagCollectionDTO1 = new TagCollectionDTO();
        tagCollectionDTO1.setId(1L);
        TagCollectionDTO tagCollectionDTO2 = new TagCollectionDTO();
        assertThat(tagCollectionDTO1).isNotEqualTo(tagCollectionDTO2);
        tagCollectionDTO2.setId(tagCollectionDTO1.getId());
        assertThat(tagCollectionDTO1).isEqualTo(tagCollectionDTO2);
        tagCollectionDTO2.setId(2L);
        assertThat(tagCollectionDTO1).isNotEqualTo(tagCollectionDTO2);
        tagCollectionDTO1.setId(null);
        assertThat(tagCollectionDTO1).isNotEqualTo(tagCollectionDTO2);
    }
}
