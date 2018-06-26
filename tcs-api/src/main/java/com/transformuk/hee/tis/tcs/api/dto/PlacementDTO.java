package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Placement entity.
 */
public class PlacementDTO implements Serializable {
    private static final long serialVersionUID = 2794590706651836140L;

    @NotNull(groups = Update.class, message = "Id must not be null when updating a placement")
    @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
    @Null(groups = Create.class, message = "Id must be null when creating a new placement")
    private Long id;
    private String intrepidId;
    private PlacementStatus status;
    @NotNull(message = "TraineeId is required", groups = {Update.class, Create.class})
    private Long traineeId;
    @NotNull(message = "PostId is required", groups = {Update.class, Create.class})
    private Long postId;
    @NotNull(message = "SiteId is required", groups = {Update.class, Create.class})
    private Long siteId;
    private String siteCode;
    @NotNull(message = "GradeId is required", groups = {Update.class, Create.class})
    private Long gradeId;
    private String gradeAbbreviation;
    private Set<PlacementSpecialtyDTO> specialties;
    @NotNull(message = "Date from is required", groups = {Update.class, Create.class})
    private LocalDate dateFrom;
    @NotNull(message = "Date to is required", groups = {Update.class, Create.class})
    private LocalDate dateTo;
    @NotNull(message = "PlacementType is required", groups = {Update.class, Create.class})
    private String placementType;
    private Float placementWholeTimeEquivalent;
    private String trainingDescription;
    private String localPostNumber;
    private Set<PlacementSupervisorDTO> supervisors = new HashSet<>();
    private Set<PlacementCommentDTO> comments = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public PlacementStatus getStatus() {
        return status;
    }

    public void setStatus(final PlacementStatus status) {
        this.status = status;
    }

    public String getIntrepidId() {
        return intrepidId;
    }

    public void setIntrepidId(final String intrepidId) {
        this.intrepidId = intrepidId;
    }

    public Long getTraineeId() {
        return traineeId;
    }

    public void setTraineeId(final Long traineeId) {
        this.traineeId = traineeId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(final Long postId) {
        this.postId = postId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(final String siteCode) {
        this.siteCode = siteCode;
    }

    public String getGradeAbbreviation() {
        return gradeAbbreviation;
    }

    public void setGradeAbbreviation(final String gradeAbbreviation) {
        this.gradeAbbreviation = gradeAbbreviation;
    }

    public String getTrainingDescription() {
        return trainingDescription;
    }

    public void setTrainingDescription(final String trainingDescription) {
        this.trainingDescription = trainingDescription;
    }

    public String getLocalPostNumber() {
        return localPostNumber;
    }

    public void setLocalPostNumber(final String localPostNumber) {
        this.localPostNumber = localPostNumber;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(final LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(final LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public String getPlacementType() {
        return placementType;
    }

    public void setPlacementType(final String placementType) {
        this.placementType = placementType;
    }

    public Float getPlacementWholeTimeEquivalent() {
        return placementWholeTimeEquivalent;
    }

    public void setPlacementWholeTimeEquivalent(final Float placementWholeTimeEquivalent) {
        this.placementWholeTimeEquivalent = placementWholeTimeEquivalent;
    }

    public Set<PlacementSpecialtyDTO> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(final Set<PlacementSpecialtyDTO> specialties) {
        this.specialties = specialties;
    }

    public Long getSiteId() {
        return siteId;
    }

    public void setSiteId(final Long siteId) {
        this.siteId = siteId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(final Long gradeId) {
        this.gradeId = gradeId;
    }

    public Set<PlacementSupervisorDTO> getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(Set<PlacementSupervisorDTO> supervisors) {
        this.supervisors = supervisors;
    }

    public Set<PlacementCommentDTO> getComments() {
        return comments;
    }

    public void setComments(Set<PlacementCommentDTO> comments) {
        this.comments = comments;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final PlacementDTO placementDTO = (PlacementDTO) o;

        if (!Objects.equals(id, placementDTO.id)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "PlacementDTO{" +
                "id=" + id +
                ", intrepidId='" + intrepidId + '\'' +
                ", status=" + status +
                ", traineeId=" + traineeId +
                ", postId=" + postId +
                ", siteId=" + siteId +
                ", siteCode='" + siteCode + '\'' +
                ", gradeId=" + gradeId +
                ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
                ", specialties=" + specialties +
                ", dateFrom=" + dateFrom +
                ", dateTo=" + dateTo +
                ", placementType='" + placementType + '\'' +
                ", placementWholeTimeEquivalent=" + placementWholeTimeEquivalent +
                ", trainingDescription='" + trainingDescription + '\'' +
                ", localPostNumber='" + localPostNumber + '\'' +
                '}';
    }
}
