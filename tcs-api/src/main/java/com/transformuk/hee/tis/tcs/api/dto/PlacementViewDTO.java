package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Holds the fields necessary for an item in a placement list
 */
public class PlacementViewDTO implements Serializable {

  private Long id;

  private String intrepidId;

  private PlacementStatus status;

  private Long traineeId;

  private Long postId;

  private String siteCode;

  private String siteName;

  private String gradeAbbreviation;

  private String gradeName;

  private LocalDate dateFrom;

  private LocalDate dateTo;

  private String placementType;

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

  public String getGradeName() {
    return gradeName;
  }

  public void setGradeName(String gradeName) {
    this.gradeName = gradeName;
  }

  public String getGradeAbbreviation() {
    return gradeAbbreviation;
  }

  public void setGradeAbbreviation(String gradeAbbreviation) {
    this.gradeAbbreviation = gradeAbbreviation;
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

  public String getPlacementType() {
    return placementType;
  }

  public void setPlacementType(String placementType) {
    this.placementType = placementType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlacementViewDTO that = (PlacementViewDTO) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (intrepidId != null ? !intrepidId.equals(that.intrepidId) : that.intrepidId != null) return false;
    if (status != that.status) return false;
    if (traineeId != null ? !traineeId.equals(that.traineeId) : that.traineeId != null) return false;
    if (postId != null ? !postId.equals(that.postId) : that.postId != null) return false;
    if (siteCode != null ? !siteCode.equals(that.siteCode) : that.siteCode != null) return false;
    if (siteName != null ? !siteName.equals(that.siteName) : that.siteName != null) return false;
    if (gradeAbbreviation != null ? !gradeAbbreviation.equals(that.gradeAbbreviation) : that.gradeAbbreviation != null)
      return false;
    if (gradeName != null ? !gradeName.equals(that.gradeName) : that.gradeName != null) return false;
    if (dateFrom != null ? !dateFrom.equals(that.dateFrom) : that.dateFrom != null) return false;
    if (dateTo != null ? !dateTo.equals(that.dateTo) : that.dateTo != null) return false;
    return placementType != null ? placementType.equals(that.placementType) : that.placementType == null;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (traineeId != null ? traineeId.hashCode() : 0);
    result = 31 * result + (postId != null ? postId.hashCode() : 0);
    result = 31 * result + (siteCode != null ? siteCode.hashCode() : 0);
    result = 31 * result + (siteName != null ? siteName.hashCode() : 0);
    result = 31 * result + (gradeAbbreviation != null ? gradeAbbreviation.hashCode() : 0);
    result = 31 * result + (gradeName != null ? gradeName.hashCode() : 0);
    result = 31 * result + (dateFrom != null ? dateFrom.hashCode() : 0);
    result = 31 * result + (dateTo != null ? dateTo.hashCode() : 0);
    result = 31 * result + (placementType != null ? placementType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PlacementViewDTO{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", status=" + status +
        ", traineeId=" + traineeId +
        ", postId=" + postId +
        ", siteCode='" + siteCode + '\'' +
        ", siteName='" + siteName + '\'' +
        ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
        ", gradeName='" + gradeName + '\'' +
        ", dateFrom=" + dateFrom +
        ", dateTo=" + dateTo +
        ", placementType='" + placementType + '\'' +
        '}';
  }
}
