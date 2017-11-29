package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Holds the fields necessary for an item in a placement list
 */
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

  private Double wholeTimeEquivalent;

  @NotNull(message = "SiteCode is required", groups = {Update.class, Create.class})
  private String siteCode;

  private String siteName;

  @NotNull(message = "GradeAbbreviation is required", groups = {Update.class, Create.class})
  private String gradeAbbreviation;

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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Long getTraineeId() {
    return traineeId;
  }

  public void setTraineeId(Long traineeId) {
    this.traineeId = traineeId;
  }

  public String getTraineeFirstName() {
    return traineeFirstName;
  }

  public void setTraineeFirstName(String traineeFirstName) {
    this.traineeFirstName = traineeFirstName;
  }

  public String getTraineeLastName() {
    return traineeLastName;
  }

  public void setTraineeLastName(String traineeLastName) {
    this.traineeLastName = traineeLastName;
  }

  public String getTraineeGmcNumber() {
    return traineeGmcNumber;
  }

  public void setTraineeGmcNumber(String traineeGmcNumber) {
    this.traineeGmcNumber = traineeGmcNumber;
  }

  public String getNationalPostNumber() {
    return nationalPostNumber;
  }

  public void setNationalPostNumber(String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
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

  public Double getWholeTimeEquivalent() {
    return wholeTimeEquivalent;
  }

  public void setWholeTimeEquivalent(Double wholeTimeEquivalent) {
    this.wholeTimeEquivalent = wholeTimeEquivalent;
  }

  public String getSiteCode() {
    return siteCode;
  }

  public void setSiteCode(String siteCode) {
    this.siteCode = siteCode;
  }

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  public String getGradeAbbreviation() {
    return gradeAbbreviation;
  }

  public void setGradeAbbreviation(String gradeAbbreviation) {
    this.gradeAbbreviation = gradeAbbreviation;
  }

  public String getGradeName() {
    return gradeName;
  }

  public void setGradeName(String gradeName) {
    this.gradeName = gradeName;
  }

  public String getPlacementType() {
    return placementType;
  }

  public void setPlacementType(String placementType) {
    this.placementType = placementType;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getTrainingDescription() {
    return trainingDescription;
  }

  public void setTrainingDescription(String trainingDescription) {
    this.trainingDescription = trainingDescription;
  }

  public PlacementStatus getStatus() {
    return status;
  }

  public void setStatus(PlacementStatus status) {
    this.status = status;
  }

  public Long getPostId() {
    return postId;
  }

  public void setPostId(Long postId) {
    this.postId = postId;
  }

  public String getLocalPostNumber() {
    return localPostNumber;
  }

  public void setLocalPostNumber(String localPostNumber) {
    this.localPostNumber = localPostNumber;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlacementDetailsDTO that = (PlacementDetailsDTO) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (intrepidId != null ? !intrepidId.equals(that.intrepidId) : that.intrepidId != null) return false;
    if (traineeId != null ? !traineeId.equals(that.traineeId) : that.traineeId != null) return false;
    if (traineeFirstName != null ? !traineeFirstName.equals(that.traineeFirstName) : that.traineeFirstName != null)
      return false;
    if (traineeLastName != null ? !traineeLastName.equals(that.traineeLastName) : that.traineeLastName != null)
      return false;
    if (traineeGmcNumber != null ? !traineeGmcNumber.equals(that.traineeGmcNumber) : that.traineeGmcNumber != null)
      return false;
    if (nationalPostNumber != null ? !nationalPostNumber.equals(that.nationalPostNumber) : that.nationalPostNumber != null)
      return false;
    if (dateFrom != null ? !dateFrom.equals(that.dateFrom) : that.dateFrom != null) return false;
    if (dateTo != null ? !dateTo.equals(that.dateTo) : that.dateTo != null) return false;
    if (wholeTimeEquivalent != null ? !wholeTimeEquivalent.equals(that.wholeTimeEquivalent) : that.wholeTimeEquivalent != null)
      return false;
    if (siteCode != null ? !siteCode.equals(that.siteCode) : that.siteCode != null) return false;
    if (siteName != null ? !siteName.equals(that.siteName) : that.siteName != null) return false;
    if (gradeAbbreviation != null ? !gradeAbbreviation.equals(that.gradeAbbreviation) : that.gradeAbbreviation != null)
      return false;
    if (gradeName != null ? !gradeName.equals(that.gradeName) : that.gradeName != null) return false;
    if (placementType != null ? !placementType.equals(that.placementType) : that.placementType != null) return false;
    if (owner != null ? !owner.equals(that.owner) : that.owner != null) return false;
    if (trainingDescription != null ? !trainingDescription.equals(that.trainingDescription) : that.trainingDescription != null)
      return false;
    if (status != that.status) return false;
    if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;
    return localPostNumber != null ? localPostNumber.equals(that.localPostNumber) : that.localPostNumber == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    result = 31 * result + (traineeId != null ? traineeId.hashCode() : 0);
    result = 31 * result + (traineeFirstName != null ? traineeFirstName.hashCode() : 0);
    result = 31 * result + (traineeLastName != null ? traineeLastName.hashCode() : 0);
    result = 31 * result + (traineeGmcNumber != null ? traineeGmcNumber.hashCode() : 0);
    result = 31 * result + (nationalPostNumber != null ? nationalPostNumber.hashCode() : 0);
    result = 31 * result + (dateFrom != null ? dateFrom.hashCode() : 0);
    result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
    result = 31 * result + (wholeTimeEquivalent != null ? wholeTimeEquivalent.hashCode() : 0);
    result = 31 * result + (siteCode != null ? siteCode.hashCode() : 0);
    result = 31 * result + (siteName != null ? siteName.hashCode() : 0);
    result = 31 * result + (gradeAbbreviation != null ? gradeAbbreviation.hashCode() : 0);
    result = 31 * result + (gradeName != null ? gradeName.hashCode() : 0);
    result = 31 * result + (placementType != null ? placementType.hashCode() : 0);
    result = 31 * result + (owner != null ? owner.hashCode() : 0);
    result = 31 * result + (trainingDescription != null ? trainingDescription.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (postId != null ? postId.hashCode() : 0);
    result = 31 * result + (localPostNumber != null ? localPostNumber.hashCode() : 0);
    return result;
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
        ", localPostNumber='" + localPostNumber + '\'' +
        '}';
  }
}
