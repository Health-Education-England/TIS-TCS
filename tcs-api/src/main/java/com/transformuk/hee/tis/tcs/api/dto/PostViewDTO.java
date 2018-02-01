package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.FundingType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import java.io.Serializable;

/**
 * This DTO is used in the post list, it's meant as a read only entity aggregating what the user needs to see
 * in a post list.
 */
public class PostViewDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private Long currentTraineeId;

  @Deprecated
  private String currentTraineeGmcNumber;

  private String currentTraineeSurname;

  private String currentTraineeForenames;

  private String nationalPostNumber;

  private Long primarySiteId;

  private String primarySiteCode;

  private String primarySiteName;

  private Long approvedGradeId;

  private String approvedGradeCode;

  private String approvedGradeName;

  private Long primarySpecialtyId;

  private String primarySpecialtyCode;

  private String primarySpecialtyName;

  private String programmeName;

  private Status status;

  private FundingType fundingType;

  private String owner;

  private String intrepidId;

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCurrentTraineeId() {
    return currentTraineeId;
  }

  public void setCurrentTraineeId(Long currentTraineeId) {
    this.currentTraineeId = currentTraineeId;
  }

  public String getCurrentTraineeGmcNumber() {
    return currentTraineeGmcNumber;
  }

  public void setCurrentTraineeGmcNumber(String currentTraineeGmcNumber) {
    this.currentTraineeGmcNumber = currentTraineeGmcNumber;
  }

  public String getCurrentTraineeSurname() {
    return currentTraineeSurname;
  }

  public void setCurrentTraineeSurname(String currentTraineeSurname) {
    this.currentTraineeSurname = currentTraineeSurname;
  }

  public String getCurrentTraineeForenames() {
    return currentTraineeForenames;
  }

  public void setCurrentTraineeForenames(String currentTraineeForenames) {
    this.currentTraineeForenames = currentTraineeForenames;
  }

  public String getNationalPostNumber() {
    return nationalPostNumber;
  }

  public void setNationalPostNumber(String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
  }

  public String getPrimarySiteCode() {
    return primarySiteCode;
  }

  public void setPrimarySiteCode(String primarySiteCode) {
    this.primarySiteCode = primarySiteCode;
  }

  public String getPrimarySiteName() {
    return primarySiteName;
  }

  public void setPrimarySiteName(String primarySiteName) {
    this.primarySiteName = primarySiteName;
  }

  public String getApprovedGradeCode() {
    return approvedGradeCode;
  }

  public void setApprovedGradeCode(String approvedGradeCode) {
    this.approvedGradeCode = approvedGradeCode;
  }

  public String getApprovedGradeName() {
    return approvedGradeName;
  }

  public void setApprovedGradeName(String approvedGradeName) {
    this.approvedGradeName = approvedGradeName;
  }

  public Long getPrimarySpecialtyId() {
    return primarySpecialtyId;
  }

  public void setPrimarySpecialtyId(Long primarySpecialtyId) {
    this.primarySpecialtyId = primarySpecialtyId;
  }

  public String getPrimarySpecialtyCode() {
    return primarySpecialtyCode;
  }

  public void setPrimarySpecialtyCode(String primarySpecialtyCode) {
    this.primarySpecialtyCode = primarySpecialtyCode;
  }

  public String getPrimarySpecialtyName() {
    return primarySpecialtyName;
  }

  public void setPrimarySpecialtyName(String primarySpecialtyName) {
    this.primarySpecialtyName = primarySpecialtyName;
  }

  public String getProgrammeName() {
    return programmeName;
  }

  public void setProgrammeName(String programmeName) {
    this.programmeName = programmeName;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public FundingType getFundingType() {
    return fundingType;
  }

  public void setFundingType(FundingType fundingType) {
    this.fundingType = fundingType;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public Long getPrimarySiteId() {
    return primarySiteId;
  }

  public void setPrimarySiteId(Long primarySiteId) {
    this.primarySiteId = primarySiteId;
  }

  public Long getApprovedGradeId() {
    return approvedGradeId;
  }

  public void setApprovedGradeId(Long approvedGradeId) {
    this.approvedGradeId = approvedGradeId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PostViewDTO that = (PostViewDTO) o;

    if (!id.equals(that.id)) return false;
    if (currentTraineeId != null ? !currentTraineeId.equals(that.currentTraineeId) : that.currentTraineeId != null)
      return false;
    if (currentTraineeGmcNumber != null ? !currentTraineeGmcNumber.equals(that.currentTraineeGmcNumber) : that.currentTraineeGmcNumber != null)
      return false;
    if (currentTraineeSurname != null ? !currentTraineeSurname.equals(that.currentTraineeSurname) : that.currentTraineeSurname != null)
      return false;
    if (currentTraineeForenames != null ? !currentTraineeForenames.equals(that.currentTraineeForenames) : that.currentTraineeForenames != null)
      return false;
    if (nationalPostNumber != null ? !nationalPostNumber.equals(that.nationalPostNumber) : that.nationalPostNumber != null)
      return false;
    if (primarySiteCode != null ? !primarySiteCode.equals(that.primarySiteCode) : that.primarySiteCode != null) return false;
    if (primarySiteName != null ? !primarySiteName.equals(that.primarySiteName) : that.primarySiteName != null)
      return false;
    if (approvedGradeCode != null ? !approvedGradeCode.equals(that.approvedGradeCode) : that.approvedGradeCode != null)
      return false;
    if (approvedGradeName != null ? !approvedGradeName.equals(that.approvedGradeName) : that.approvedGradeName != null)
      return false;
    if (primarySpecialtyId != null ? !primarySpecialtyId.equals(that.primarySpecialtyId) : that.primarySpecialtyId != null)
      return false;
    if (primarySpecialtyCode != null ? !primarySpecialtyCode.equals(that.primarySpecialtyCode) : that.primarySpecialtyCode != null)
      return false;
    if (primarySpecialtyName != null ? !primarySpecialtyName.equals(that.primarySpecialtyName) : that.primarySpecialtyName != null)
      return false;
    if (programmeName != null ? !programmeName.equals(that.programmeName) : that.programmeName != null) return false;
    if (status != that.status) return false;
    if (fundingType != that.fundingType) return false;
    if (owner != null ? !owner.equals(that.owner) : that.owner != null)
      return false;
    return intrepidId != null ? intrepidId.equals(that.intrepidId) : that.intrepidId == null;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + (currentTraineeId != null ? currentTraineeId.hashCode() : 0);
    result = 31 * result + (currentTraineeGmcNumber != null ? currentTraineeGmcNumber.hashCode() : 0);
    result = 31 * result + (currentTraineeSurname != null ? currentTraineeSurname.hashCode() : 0);
    result = 31 * result + (currentTraineeForenames != null ? currentTraineeForenames.hashCode() : 0);
    result = 31 * result + (nationalPostNumber != null ? nationalPostNumber.hashCode() : 0);
    result = 31 * result + (primarySiteCode != null ? primarySiteCode.hashCode() : 0);
    result = 31 * result + (primarySiteName != null ? primarySiteName.hashCode() : 0);
    result = 31 * result + (approvedGradeCode != null ? approvedGradeCode.hashCode() : 0);
    result = 31 * result + (approvedGradeName != null ? approvedGradeName.hashCode() : 0);
    result = 31 * result + (primarySpecialtyId != null ? primarySpecialtyId.hashCode() : 0);
    result = 31 * result + (primarySpecialtyCode != null ? primarySpecialtyCode.hashCode() : 0);
    result = 31 * result + (primarySpecialtyName != null ? primarySpecialtyName.hashCode() : 0);
    result = 31 * result + (programmeName != null ? programmeName.hashCode() : 0);
    result = 31 * result + status.hashCode();
    result = 31 * result + (fundingType != null ? fundingType.hashCode() : 0);
    result = 31 * result + (owner != null ? owner.hashCode() : 0);
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostViewDTO{" +
        "id=" + id +
        ", currentTraineeId=" + currentTraineeId +
        ", currentTraineeGmcNumber='" + currentTraineeGmcNumber + '\'' +
        ", currentTraineeSurname='" + currentTraineeSurname + '\'' +
        ", currentTraineeForenames='" + currentTraineeForenames + '\'' +
        ", nationalPostNumber='" + nationalPostNumber + '\'' +
        ", primarySiteId=" + primarySiteId +
        ", primarySiteCode='" + primarySiteCode + '\'' +
        ", primarySiteName='" + primarySiteName + '\'' +
        ", approvedGradeId='" + approvedGradeId + '\'' +
        ", approvedGradeCode='" + approvedGradeCode + '\'' +
        ", approvedGradeName='" + approvedGradeName + '\'' +
        ", primarySpecialtyId=" + primarySpecialtyId +
        ", primarySpecialtyCode='" + primarySpecialtyCode + '\'' +
        ", primarySpecialtyName='" + primarySpecialtyName + '\'' +
        ", programmeName='" + programmeName + '\'' +
        ", status=" + status +
        ", fundingType=" + fundingType +
        ", owner='" + owner + '\'' +
        ", intrepidId='" + intrepidId + '\'' +
        '}';
  }
}

