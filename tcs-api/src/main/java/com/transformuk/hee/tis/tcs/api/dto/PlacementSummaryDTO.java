package com.transformuk.hee.tis.tcs.api.dto;

import java.math.BigInteger;
import java.util.Date;
import java.util.Objects;

/**
 * DTO to display placement details for a post or person
 */
public class PlacementSummaryDTO {

  private Date dateFrom;
  private Date dateTo;
  private BigInteger siteId;
  private String siteName;
  private String primarySpecialtyName;
  private BigInteger gradeId;
  private String gradeName;
  private String placementType;
  private String status;
  private String forenames;
  private String surname;
  private BigInteger traineeId;
  private BigInteger placementId;
  private String placementStatus;
  private String placementSpecialtyType;

  public PlacementSummaryDTO() {
  }

  public PlacementSummaryDTO(Date dateFrom, Date dateTo, BigInteger siteId, String primarySpecialtyName,
                             BigInteger gradeId, String placementType, String status, String forenames, String surname,
                             BigInteger traineeId, BigInteger placementId, String placementSpecialtyType) {
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.siteId = siteId;
    this.primarySpecialtyName = primarySpecialtyName;
    this.gradeId = gradeId;
    this.placementType = placementType;
    this.status = status;
    this.forenames = forenames;
    this.surname = surname;
    this.traineeId = traineeId;
    this.placementId = placementId;
    this.placementSpecialtyType = placementSpecialtyType;
  }

  public Date getDateFrom() {
    return dateFrom;
  }

  public void setDateFrom(Date dateFrom) {
    this.dateFrom = dateFrom;
  }

  public Date getDateTo() {
    return dateTo;
  }

  public void setDateTo(Date dateTo) {
    this.dateTo = dateTo;
  }

  public BigInteger getSiteId() {
    return siteId;
  }

  public void setSiteId(BigInteger siteId) {
    this.siteId = siteId;
  }

  public String getSiteName() {
    return siteName;
  }

  public void setSiteName(String siteName) {
    this.siteName = siteName;
  }

  public String getPrimarySpecialtyName() {
    return primarySpecialtyName;
  }

  public void setPrimarySpecialtyName(String primarySpecialtyName) {
    this.primarySpecialtyName = primarySpecialtyName;
  }

  public BigInteger getGradeId() {
    return gradeId;
  }

  public void setGradeId(BigInteger gradeId) {
    this.gradeId = gradeId;
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

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getForenames() {
    return forenames;
  }

  public void setForenames(String forenames) {
    this.forenames = forenames;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public BigInteger getTraineeId() {
    return traineeId;
  }

  public void setTraineeId(BigInteger traineeId) {
    this.traineeId = traineeId;
  }

  public BigInteger getPlacementId() {
    return placementId;
  }

  public void setPlacementId(BigInteger placementId) {
    this.placementId = placementId;
  }

  public String getPlacementStatus() {
    return placementStatus;
  }

  public void setPlacementStatus(String placementStatus) {
    this.placementStatus = placementStatus;
  }

  public String getPlacementSpecialtyType() {
    return placementSpecialtyType;
  }

  public void setPlacementSpecialtyType(String placementSpecialtyType) {
    this.placementSpecialtyType = placementSpecialtyType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlacementSummaryDTO that = (PlacementSummaryDTO) o;
    return Objects.equals(dateFrom, that.dateFrom) &&
        Objects.equals(dateTo, that.dateTo) &&
        Objects.equals(siteId, that.siteId) &&
        Objects.equals(siteName, that.siteName) &&
        Objects.equals(primarySpecialtyName, that.primarySpecialtyName) &&
        Objects.equals(gradeId, that.gradeId) &&
        Objects.equals(gradeName, that.gradeName) &&
        Objects.equals(placementType, that.placementType) &&
        Objects.equals(status, that.status) &&
        Objects.equals(forenames, that.forenames) &&
        Objects.equals(surname, that.surname) &&
        Objects.equals(traineeId, that.traineeId) &&
        Objects.equals(placementId, that.placementId) &&
        Objects.equals(placementStatus, that.placementStatus) &&
        Objects.equals(placementSpecialtyType, that.placementSpecialtyType);
  }

  @Override
  public int hashCode() {

    return Objects.hash(dateFrom, dateTo, siteId, siteName, primarySpecialtyName, gradeId, gradeName, placementType, status, forenames, surname, traineeId, placementId, placementStatus, placementSpecialtyType);
  }
}
