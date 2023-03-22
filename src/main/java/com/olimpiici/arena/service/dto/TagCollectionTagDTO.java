package com.olimpiici.arena.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.olimpiici.arena.domain.TagCollectionTag} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagCollectionTagDTO implements Serializable {

    private Long id;

    private TagCollectionDTO collection;

    private TagDTO tag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TagCollectionDTO getCollection() {
        return collection;
    }

    public void setCollection(TagCollectionDTO collection) {
        this.collection = collection;
    }

    public TagDTO getTag() {
        return tag;
    }

    public void setTag(TagDTO tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagCollectionTagDTO)) {
            return false;
        }

        TagCollectionTagDTO tagCollectionTagDTO = (TagCollectionTagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tagCollectionTagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagCollectionTagDTO{" +
            "id=" + getId() +
            ", collection=" + getCollection() +
            ", tag=" + getTag() +
            "}";
    }
}
