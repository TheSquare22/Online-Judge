package com.olimpiici.arena.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TagCollectionTag.
 */
@Entity
@Table(name = "tag_collection_tag")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TagCollectionTag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    private TagCollection collection;

    @ManyToOne
    private Tag tag;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TagCollectionTag id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TagCollection getCollection() {
        return this.collection;
    }

    public void setCollection(TagCollection tagCollection) {
        this.collection = tagCollection;
    }

    public TagCollectionTag collection(TagCollection tagCollection) {
        this.setCollection(tagCollection);
        return this;
    }

    public Tag getTag() {
        return this.tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public TagCollectionTag tag(Tag tag) {
        this.setTag(tag);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TagCollectionTag)) {
            return false;
        }
        return id != null && id.equals(((TagCollectionTag) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TagCollectionTag{" +
            "id=" + getId() +
            "}";
    }
}
