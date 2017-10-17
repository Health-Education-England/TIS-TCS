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

  private String nationalPostNumber;

  private String primarySiteId;

  private String approvedGradeId;

  private Long primarySpecialtyId;

  private String programmeName;

  private Status status;

  private FundingType fundingType;

  private String managingLocalOffice;

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNationalPostNumber() {
    return nationalPostNumber;
  }

  public void setNationalPostNumber(String nationalPostNumber) {
    this.nationalPostNumber = nationalPostNumber;
  }

  public String getPrimarySiteId() {
    return primarySiteId;
  }

  public void setPrimarySiteId(String primarySiteId) {
    this.primarySiteId = primarySiteId;
  }

  public String getApprovedGradeId() {
    return approvedGradeId;
  }

  public void setApprovedGradeId(String approvedGradeId) {
    this.approvedGradeId = approvedGradeId;
  }

  public Long getPrimarySpecialtyId() {
    return primarySpecialtyId;
  }

  public void setPrimarySpecialtyId(Long primarySpecialtyId) {
    this.primarySpecialtyId = primarySpecialtyId;
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

  public String getManagingLocalOffice() {
    return managingLocalOffice;
  }

  public void setManagingLocalOffice(String managingLocalOffice) {
    this.managingLocalOffice = managingLocalOffice;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PostViewDTO that = (PostViewDTO) o;

    if (!id.equals(that.id)) return false;
    if (nationalPostNumber != null ? !nationalPostNumber.equals(that.nationalPostNumber) : that.nationalPostNumber != null)
      return false;
    if (primarySiteId != null ? !primarySiteId.equals(that.primarySiteId) : that.primarySiteId != null) return false;
    if (approvedGradeId != null ? !approvedGradeId.equals(that.approvedGradeId) : that.approvedGradeId != null)
      return false;
    if (primarySpecialtyId != null ? !primarySpecialtyId.equals(that.primarySpecialtyId) : that.primarySpecialtyId != null)
      return false;
    if (programmeName != null ? !programmeName.equals(that.programmeName) : that.programmeName != null) return false;
    if (status != that.status) return false;
    if (fundingType != that.fundingType) return false;
    return managingLocalOffice != null ? managingLocalOffice.equals(that.managingLocalOffice) : that.managingLocalOffice == null;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + (nationalPostNumber != null ? nationalPostNumber.hashCode() : 0);
    result = 31 * result + (primarySiteId != null ? primarySiteId.hashCode() : 0);
    result = 31 * result + (approvedGradeId != null ? approvedGradeId.hashCode() : 0);
    result = 31 * result + (primarySpecialtyId != null ? primarySpecialtyId.hashCode() : 0);
    result = 31 * result + (programmeName != null ? programmeName.hashCode() : 0);
    result = 31 * result + status.hashCode();
    result = 31 * result + (fundingType != null ? fundingType.hashCode() : 0);
    result = 31 * result + (managingLocalOffice != null ? managingLocalOffice.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostViewDTO{" +
        "id=" + id +
        ", nationalPostNumber='" + nationalPostNumber + '\'' +
        ", primarySiteId='" + primarySiteId + '\'' +
        ", approvedGradeId='" + approvedGradeId + '\'' +
        ", primarySpecialtyId=" + primarySpecialtyId +
        ", programmeName='" + programmeName + '\'' +
        ", status=" + status +
        ", fundingType=" + fundingType +
        ", managingLocalOffice='" + managingLocalOffice + '\'' +
        '}';
  }
}

