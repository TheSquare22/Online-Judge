package com.olimpiici.arena.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.olimpiici.arena.domain.Problem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProblemDTO implements Serializable {

    private Long id;

    private String title;

    private String directory;

    private Integer version;

    private TagCollectionDTO tags;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public TagCollectionDTO getTags() {
        return tags;
    }

    public void setTags(TagCollectionDTO tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProblemDTO)) {
            return false;
        }

        ProblemDTO problemDTO = (ProblemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, problemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProblemDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", directory='" + getDirectory() + "'" +
            ", version=" + getVersion() +
            ", tags=" + getTags() +
            "}";
    }
}
