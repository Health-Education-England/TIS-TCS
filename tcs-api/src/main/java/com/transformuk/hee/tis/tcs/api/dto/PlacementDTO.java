package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the Placement entity.
 */
public class PlacementDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a placement")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new placement")
  private Long id;

  private String intrepidId;

  private PlacementStatus status;

  @NotNull(message = "TraineeId is required", groups = {Update.class, Create.class})
  private Long traineeId;

  @NotNull(message = "ClinicalSupervisorId is required", groups = {Update.class, Create.class})
  private Set<Long> clinicalSupervisorIds;

  @NotNull(message = "PostId is required", groups = {Update.class, Create.class})
  private Long postId;

  @NotNull(message = "SiteId is required", groups = {Update.class, Create.class})
  private Long siteId;

  @NotNull(message = "GradeId is required", groups = {Update.class, Create.class})
  private Long gradeId;

  private Set<PlacementSpecialtyDTO> specialties;

  @NotNull(message = "Date from is required", groups = {Update.class, Create.class})
  private LocalDate dateFrom;

  @NotNull(message = "Date to is required", groups = {Update.class, Create.class})
  private LocalDate dateTo;

  @NotNull(message = "PlacementTypeId is required", groups = {Update.class, Create.class})
  private Long placementTypeId;

  private Float placementWholeTimeEquivalent;

  private String trainingDescription;

  private String localPostNumber;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PlacementStatus getStatus() {
    return status;
  }

  public void setStatus(PlacementStatus status) {
    this.status = status;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Set<Long> getClinicalSupervisorIds() {
    return clinicalSupervisorIds;
  }

  public void setClinicalSupervisorIds(Set<Long> clinicalSupervisorIds) {
    this.clinicalSupervisorIds = clinicalSupervisorIds;
  }

  public Long getTraineeId() {
    return traineeId;
  }

  public void setTraineeId(Long traineeId) {
    this.traineeId = traineeId;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public Long getSiteId() {
    return siteId;
  }

  public void setSiteId(Long siteId) {
    this.siteId = siteId;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public String getTrainingDescription() {
    return trainingDescription;
  }

  public void setTrainingDescription(String trainingDescription) {
    this.trainingDescription = trainingDescription;
  }

  public String getLocalPostNumber() {
    return localPostNumber;
  }

  public void setLocalPostNumber(String localPostNumber) {
    this.localPostNumber = localPostNumber;
  }

  public LocalDate getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(LocalDate dateFrom) {
    this.dateFrom = dateFrom;
  }

  public LocalDate getDateTo() {
    return dateTo;
  }

  public void setDateTo(LocalDate dateTo) {
    this.dateTo = dateTo;
  }

  public Long getPlacementTypeId() {
    return placementTypeId;
  }

  public void setPlacementTypeId(Long placementTypeId) {
    this.placementTypeId = placementTypeId;
  }

  public Float getPlacementWholeTimeEquivalent() {
    return placementWholeTimeEquivalent;
  }

  public void setPlacementWholeTimeEquivalent(Float placementWholeTimeEquivalent) {
    this.placementWholeTimeEquivalent = placementWholeTimeEquivalent;
  }

  public Set<PlacementSpecialtyDTO> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(Set<PlacementSpecialtyDTO> specialties) {
    this.specialties = specialties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlacementDTO placementDTO = (PlacementDTO) o;

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
        ", intrepidId='" + intrepidId + "'" +
        ", traineeId='" + traineeId + "'" +
        ", clinicalSupervisorIds='" + clinicalSupervisorIds + "'" +
        ", postId='" + postId + "'" +
        ", siteId='" + siteId + "'" +
        ", gradeId='" + gradeId + "'" +
        ", specialties='" + specialties + "'" +
        ", dateFrom='" + dateFrom + "'" +
        ", dateTo='" + dateTo + "'" +
        ", placementTypeId='" + placementTypeId + "'" +
        ", placementWholeTimeEquivalent='" + placementWholeTimeEquivalent + "'" +
        ", localPostNumber='" + localPostNumber + "'" +
        ", trainingDescription='" + trainingDescription + "'" +
        '}';
  }
}
