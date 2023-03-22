package com.olimpiici.arena.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.olimpiici.arena.domain.TagCollection} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagCollectionDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagCollectionDTO)) {
            return false;
        }

        TagCollectionDTO tagCollectionDTO = (TagCollectionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tagCollectionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagCollectionDTO{" +
            "id=" + getId() +
            "}";
    }
}
