package com.olimpiici.arena.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Submission.
 */
@Entity
@Table(name = "submission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Submission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file")
    private String file;

    @Column(name = "verdict")
    private String verdict;

    @Column(name = "details")
    private String details;

    @Column(name = "points")
    private Integer points;

    @Column(name = "time_in_millis")
    private Integer timeInMillis;

    @Column(name = "memory_in_bytes")
    private Integer memoryInBytes;

    @Column(name = "upload_date")
    private ZonedDateTime uploadDate;

    @Column(name = "security_key")
    private String securityKey;

    @ManyToOne
    private TagCollection tags;

    @ManyToOne
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = { "problem", "competition" }, allowSetters = true)
    private CompetitionProblem competitionProblem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Submission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFile() {
        return this.file;
    }

    public Submission file(String file) {
        this.setFile(file);
        return this;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getVerdict() {
        return this.verdict;
    }

    public Submission verdict(String verdict) {
        this.setVerdict(verdict);
        return this;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public String getDetails() {
        return this.details;
    }

    public Submission details(String details) {
        this.setDetails(details);
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getPoints() {
        return this.points;
    }

    public Submission points(Integer points) {
        this.setPoints(points);
        return this;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getTimeInMillis() {
        return this.timeInMillis;
    }

    public Submission timeInMillis(Integer timeInMillis) {
        this.setTimeInMillis(timeInMillis);
        return this;
    }

    public void setTimeInMillis(Integer timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public Integer getMemoryInBytes() {
        return this.memoryInBytes;
    }

    public Submission memoryInBytes(Integer memoryInBytes) {
        this.setMemoryInBytes(memoryInBytes);
        return this;
    }

    public void setMemoryInBytes(Integer memoryInBytes) {
        this.memoryInBytes = memoryInBytes;
    }

    public ZonedDateTime getUploadDate() {
        return this.uploadDate;
    }

    public Submission uploadDate(ZonedDateTime uploadDate) {
        this.setUploadDate(uploadDate);
        return this;
    }

    public void setUploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getSecurityKey() {
        return this.securityKey;
    }

    public Submission securityKey(String securityKey) {
        this.setSecurityKey(securityKey);
        return this;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public TagCollection getTags() {
        return this.tags;
    }

    public void setTags(TagCollection tagCollection) {
        this.tags = tagCollection;
    }

    public Submission tags(TagCollection tagCollection) {
        this.setTags(tagCollection);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Submission user(User user) {
        this.setUser(user);
        return this;
    }

    public CompetitionProblem getCompetitionProblem() {
        return this.competitionProblem;
    }

    public void setCompetitionProblem(CompetitionProblem competitionProblem) {
        this.competitionProblem = competitionProblem;
    }

    public Submission competitionProblem(CompetitionProblem competitionProblem) {
        this.setCompetitionProblem(competitionProblem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Submission)) {
            return false;
        }
        return id != null && id.equals(((Submission) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Submission{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", verdict='" + getVerdict() + "'" +
            ", details='" + getDetails() + "'" +
            ", points=" + getPoints() +
            ", timeInMillis=" + getTimeInMillis() +
            ", memoryInBytes=" + getMemoryInBytes() +
            ", uploadDate='" + getUploadDate() + "'" +
            ", securityKey='" + getSecurityKey() + "'" +
            "}";
    }
}
