package com.olimpiici.arena.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the {@link com.olimpiici.arena.domain.Submission} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubmissionDTO implements Serializable {

    private Long id;

    private String file;

    private String verdict;

    private String details;

    private Integer points;

    private Integer timeInMillis;

    private Integer memoryInBytes;

    private ZonedDateTime uploadDate;

    private String securityKey;

    private TagCollectionDTO tags;

    private UserDTO user;

    private CompetitionProblemDTO competitionProblem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getVerdict() {
        return verdict;
    }

    public void setVerdict(String verdict) {
        this.verdict = verdict;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public Integer getTimeInMillis() {
        return timeInMillis;
    }

    public void setTimeInMillis(Integer timeInMillis) {
        this.timeInMillis = timeInMillis;
    }

    public Integer getMemoryInBytes() {
        return memoryInBytes;
    }

    public void setMemoryInBytes(Integer memoryInBytes) {
        this.memoryInBytes = memoryInBytes;
    }

    public ZonedDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(ZonedDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public TagCollectionDTO getTags() {
        return tags;
    }

    public void setTags(TagCollectionDTO tags) {
        this.tags = tags;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public CompetitionProblemDTO getCompetitionProblem() {
        return competitionProblem;
    }

    public void setCompetitionProblem(CompetitionProblemDTO competitionProblem) {
        this.competitionProblem = competitionProblem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubmissionDTO)) {
            return false;
        }

        SubmissionDTO submissionDTO = (SubmissionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, submissionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubmissionDTO{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", verdict='" + getVerdict() + "'" +
            ", details='" + getDetails() + "'" +
            ", points=" + getPoints() +
            ", timeInMillis=" + getTimeInMillis() +
            ", memoryInBytes=" + getMemoryInBytes() +
            ", uploadDate='" + getUploadDate() + "'" +
            ", securityKey='" + getSecurityKey() + "'" +
            ", tags=" + getTags() +
            ", user=" + getUser() +
            ", competitionProblem=" + getCompetitionProblem() +
            "}";
    }
}
