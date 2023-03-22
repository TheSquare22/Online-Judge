package com.olimpiici.arena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.olimpiici.arena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagCollectionTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagCollectionTag.class);
        TagCollectionTag tagCollectionTag1 = new TagCollectionTag();
        tagCollectionTag1.setId(1L);
        TagCollectionTag tagCollectionTag2 = new TagCollectionTag();
        tagCollectionTag2.setId(tagCollectionTag1.getId());
        assertThat(tagCollectionTag1).isEqualTo(tagCollectionTag2);
        tagCollectionTag2.setId(2L);
        assertThat(tagCollectionTag1).isNotEqualTo(tagCollectionTag2);
        tagCollectionTag1.setId(null);
        assertThat(tagCollectionTag1).isNotEqualTo(tagCollectionTag2);
    }
}
