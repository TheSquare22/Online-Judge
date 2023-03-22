package com.olimpiici.arena.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.olimpiici.arena.domain.Competition} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompetitionDTO implements Serializable {

    private Long id;

    private String label;

    private String description;

    private Integer order;

    private CompetitionDTO parent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public CompetitionDTO getParent() {
        return parent;
    }

    public void setParent(CompetitionDTO parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompetitionDTO)) {
            return false;
        }

        CompetitionDTO competitionDTO = (CompetitionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, competitionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompetitionDTO{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", description='" + getDescription() + "'" +
            ", order=" + getOrder() +
            ", parent=" + getParent() +
            "}";
    }
}
