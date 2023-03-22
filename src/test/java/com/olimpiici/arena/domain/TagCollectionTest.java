package com.olimpiici.arena.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.olimpiici.arena.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TagCollectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TagCollection.class);
        TagCollection tagCollection1 = new TagCollection();
        tagCollection1.setId(1L);
        TagCollection tagCollection2 = new TagCollection();
        tagCollection2.setId(tagCollection1.getId());
        assertThat(tagCollection1).isEqualTo(tagCollection2);
        tagCollection2.setId(2L);
        assertThat(tagCollection1).isNotEqualTo(tagCollection2);
        tagCollection1.setId(null);
        assertThat(tagCollection1).isNotEqualTo(tagCollection2);
    }
}
