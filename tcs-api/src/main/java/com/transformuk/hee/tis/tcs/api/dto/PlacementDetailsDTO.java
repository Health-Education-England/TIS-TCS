package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.DraftStatus;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class PlacementDetailsDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a placement")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new placement")
  private Long id;

  private String intrepidId;

  @NotNull(message = "TraineeId is required", groups = {Update.class, Create.class})
  private Long traineeId;

  private String traineeFirstName;

  private String traineeLastName;

  private String traineeGmcNumber;

  private String nationalPostNumber;

  @NotNull(message = "Date from is required", groups = {Update.class, Create.class})
  private LocalDate dateFrom;

  @NotNull(message = "Date to is required", groups = {Update.class, Create.class})
  private LocalDate dateTo;

  private BigDecimal wholeTimeEquivalent;

  private String siteCode;

  private Set<PlacementSiteDTO> sites = new HashSet<>();

  @NotNull(message = "SiteId is required", groups = {Update.class, Create.class})
  private Long siteId;

  private String siteName;

  private String gradeAbbreviation;

  @NotNull(message = "GradeId is required", groups = {Update.class, Create.class})
  private Long gradeId;

  private String gradeName;

  //TODO to add clinical supervisor and specialties

  @NotNull(message = "PlacementType is required", groups = {Update.class, Create.class})
  private String placementType;

  private String owner;

  private String trainingDescription;

  private PlacementStatus status;

  @NotNull(message = "PostId is required", groups = {Update.class, Create.class})
  private Long postId;

  private String localPostNumber;

  private Set<PlacementSpecialtyDTO> specialties;

  private LocalDateTime addedDate;

  private LocalDateTime amendedDate;

  private Set<PlacementSupervisorDTO> supervisors = new HashSet<>();

  private Set<PlacementCommentDTO> comments = new HashSet<>();

  private DraftStatus draftStatus;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
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

  public String getTraineeFirstName() {
    return traineeFirstName;
  }

  public void setTraineeFirstName(final String traineeFirstName) {
    this.traineeFirstName = traineeFirstName;
  }

  public String getTraineeLastName() {
    return traineeLastName;
  }

  public void setTraineeLastName(final String traineeLastName) {
    this.traineeLastName = traineeLastName;
  }

  public String getTraineeGmcNumber() {
    return traineeGmcNumber;
  }

  public void setTraineeGmcNumber(final String traineeGmcNumber) {
    this.traineeGmcNumber = traineeGmcNumber;
  }

  public String getNationalPostNumber() {
    return nationalPostNumber;
  }

  public void setNationalPostNumber(final String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
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

  public BigDecimal getWholeTimeEquivalent() {
    return wholeTimeEquivalent;
  }

  public void setWholeTimeEquivalent(final BigDecimal wholeTimeEquivalent) {
    if (wholeTimeEquivalent != null) {
      this.wholeTimeEquivalent = wholeTimeEquivalent.setScale(2, BigDecimal.ROUND_HALF_UP);
    } else {
      this.wholeTimeEquivalent = wholeTimeEquivalent;
    }
  }

  public String getSiteCode() {
    return siteCode;
  }

  public void setSiteCode(final String siteCode) {
    this.siteCode = siteCode;
  }

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(final String siteName) {
    this.siteName = siteName;
  }

  public String getGradeAbbreviation() {
    return gradeAbbreviation;
  }

  public void setGradeAbbreviation(final String gradeAbbreviation) {
    this.gradeAbbreviation = gradeAbbreviation;
  }

  public String getGradeName() {
    return gradeName;
  }

  public void setGradeName(final String gradeName) {
    this.gradeName = gradeName;
  }

  public String getPlacementType() {
    return placementType;
  }

  public void setPlacementType(final String placementType) {
    this.placementType = placementType;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(final String owner) {
    this.owner = owner;
  }

  public String getTrainingDescription() {
    return trainingDescription;
  }

  public void setTrainingDescription(final String trainingDescription) {
    this.trainingDescription = trainingDescription;
  }

  public PlacementStatus getStatus() {
    return status;
  }

  public void setStatus(final PlacementStatus status) {
    this.status = status;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(final Long postId) {
    this.postId = postId;
  }

  public String getLocalPostNumber() {
    return localPostNumber;
  }

  public void setLocalPostNumber(final String localPostNumber) {
    this.localPostNumber = localPostNumber;
  }

  public Long getSiteId() {
    return siteId;
  }

  public void setSiteId(final Long siteId) {
    this.siteId = siteId;
  }

  public Set<PlacementSiteDTO> getSites() {
    return sites;
  }

  public void setSites(Set<PlacementSiteDTO> sites) {
    this.sites = sites;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(final Long gradeId) {
    this.gradeId = gradeId;
  }

  public LocalDateTime getAmendedDate() {
    return amendedDate;
  }

  public void setAmendedDate(LocalDateTime amendedDate) {
    this.amendedDate = amendedDate;
  }

  public LocalDateTime getAddedDate() {
    return addedDate;
  }

  public void setAddedDate(LocalDateTime addedDate) {
    this.addedDate = addedDate;
  }

  public Set<PlacementSpecialtyDTO> getSpecialties() {
    return specialties;
  }

  public void setSpecialties(final Set<PlacementSpecialtyDTO> specialties) {
    this.specialties = specialties;
  }

  public Set<PlacementSupervisorDTO> getSupervisors() {
    return supervisors;
  }

  public void setSupervisors(final Set<PlacementSupervisorDTO> supervisors) {
    this.supervisors = supervisors;
  }

  public Set<PlacementCommentDTO> getComments() {
    return comments;
  }

  public void setComments(Set<PlacementCommentDTO> comments) {
    this.comments = comments;
  }

  public DraftStatus getDraftStatus() {
    return draftStatus;
  }

  public void setDraftStatus(DraftStatus draftStatus) {
    this.draftStatus = draftStatus;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final PlacementDetailsDTO that = (PlacementDetailsDTO) o;

    return Objects.equals(id, that.id) &&
        Objects.equals(intrepidId, that.intrepidId) &&
        Objects.equals(traineeId, that.traineeId) &&
        Objects.equals(traineeFirstName, that.traineeFirstName) &&
        Objects.equals(traineeLastName, that.traineeLastName) &&
        Objects.equals(traineeGmcNumber, that.traineeGmcNumber) &&
        Objects.equals(nationalPostNumber, that.nationalPostNumber) &&
        Objects.equals(dateFrom, that.dateFrom) &&
        Objects.equals(dateTo, that.dateTo) &&
        Objects.equals(wholeTimeEquivalent, that.wholeTimeEquivalent) &&
        Objects.equals(siteCode, that.siteCode) &&
        Objects.equals(siteName, that.siteName) &&
        Objects.equals(gradeAbbreviation, that.gradeAbbreviation) &&
        Objects.equals(gradeName, that.gradeName) &&
        Objects.equals(placementType, that.placementType) &&
        Objects.equals(owner, that.owner) &&
        Objects.equals(trainingDescription, that.trainingDescription) &&
        Objects.equals(status, that.status) &&
        Objects.equals(postId, that.postId) &&
        Objects.equals(addedDate, that.addedDate) &&
        Objects.equals(amendedDate, that.amendedDate) &&
        Objects.equals(localPostNumber, that.localPostNumber) &&
        Objects.equals(draftStatus, that.draftStatus);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(id, intrepidId, traineeId, traineeFirstName, traineeLastName, traineeGmcNumber,
            nationalPostNumber, dateFrom, dateTo, wholeTimeEquivalent, siteCode, siteName,
            gradeAbbreviation, gradeName, placementType, owner, trainingDescription,
            status, postId, addedDate, amendedDate, localPostNumber, draftStatus);
  }

  @Override
  public String toString() {
    return "PlacementDetailsDTO{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", traineeId=" + traineeId +
        ", traineeFirstName='" + traineeFirstName + '\'' +
        ", traineeLastName='" + traineeLastName + '\'' +
        ", traineeGmcNumber='" + traineeGmcNumber + '\'' +
        ", nationalPostNumber='" + nationalPostNumber + '\'' +
        ", dateFrom=" + dateFrom +
        ", dateTo=" + dateTo +
        ", wholeTimeEquivalent=" + wholeTimeEquivalent +
        ", siteCode='" + siteCode + '\'' +
        ", siteName='" + siteName + '\'' +
        ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
        ", gradeName='" + gradeName + '\'' +
        ", placementType='" + placementType + '\'' +
        ", owner='" + owner + '\'' +
        ", trainingDescription='" + trainingDescription + '\'' +
        ", status=" + status +
        ", postId=" + postId +
        ", addedDate=" + addedDate +
        ", amendedDate=" + amendedDate +
        ", localPostNumber='" + localPostNumber + '\'' +
        ", draftStatus='" + draftStatus + '\'' +
        '}';
  }
}
