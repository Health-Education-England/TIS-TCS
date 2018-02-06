package com.transformuk.hee.tis.tcs.api.dto;

import java.math.BigInteger;
import java.util.Date;

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

  public PlacementSummaryDTO(Date dateFrom, Date dateTo, BigInteger siteId, String primarySpecialtyName,
                             BigInteger gradeId, String placementType, String status, String forenames, String surname) {
    this.dateFrom = dateFrom;
    this.dateTo = dateTo;
    this.siteId = siteId;
    this.primarySpecialtyName = primarySpecialtyName;
    this.gradeId = gradeId;
    this.placementType = placementType;
    this.status = status;
    this.forenames = forenames;
    this.surname = surname;
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
}
