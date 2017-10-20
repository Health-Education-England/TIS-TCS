package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.FundingType;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * This view is built to sit behind the post list.
 */
@Entity
@Table(name = "PostView")
public class PostView implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "currentTraineeId")
  private Long currentTraineeId;

  @Column(name = "currentTraineeGmcNumber")
  private String currentTraineeGmcNumber;

  @Column(name = "currentTraineeSurname")
  private String currentTraineeSurname;

  @Column(name = "currentTraineeForenames")
  private String currentTraineeForenames;

  @Column(name = "nationalPostNumber")
  private String nationalPostNumber;

  @Column(name = "primarySiteId")
  private String primarySiteId;

  @Column(name = "approvedGradeId")
  private String approvedGradeId;

  @Column(name = "primarySpecialtyId")
  private Long primarySpecialtyId;

  @Column(name = "programmeName")
  private String programmeName;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private Status status;

  @Enumerated(EnumType.STRING)
  @Column(name = "fundingType")
  private FundingType fundingType;

  @Column(name = "managingLocalOffice")
  private String managingLocalOffice;

  @Column(name = "intrepidId")
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

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PostView postView = (PostView) o;

    if (!id.equals(postView.id)) return false;
    if (currentTraineeId != null ? !currentTraineeId.equals(postView.currentTraineeId) : postView.currentTraineeId != null)
      return false;
    if (currentTraineeGmcNumber != null ? !currentTraineeGmcNumber.equals(postView.currentTraineeGmcNumber) : postView.currentTraineeGmcNumber != null)
      return false;
    if (currentTraineeSurname != null ? !currentTraineeSurname.equals(postView.currentTraineeSurname) : postView.currentTraineeSurname != null)
      return false;
    if (currentTraineeForenames != null ? !currentTraineeForenames.equals(postView.currentTraineeForenames) : postView.currentTraineeForenames != null)
      return false;
    if (nationalPostNumber != null ? !nationalPostNumber.equals(postView.nationalPostNumber) : postView.nationalPostNumber != null)
      return false;
    if (primarySiteId != null ? !primarySiteId.equals(postView.primarySiteId) : postView.primarySiteId != null)
      return false;
    if (approvedGradeId != null ? !approvedGradeId.equals(postView.approvedGradeId) : postView.approvedGradeId != null)
      return false;
    if (primarySpecialtyId != null ? !primarySpecialtyId.equals(postView.primarySpecialtyId) : postView.primarySpecialtyId != null)
      return false;
    if (programmeName != null ? !programmeName.equals(postView.programmeName) : postView.programmeName != null)
      return false;
    if (intrepidId != null ? !intrepidId.equals(postView.intrepidId) : postView.intrepidId != null)
      return false;
    if (status != postView.status) return false;
    if (fundingType != postView.fundingType) return false;
    return managingLocalOffice != null ? managingLocalOffice.equals(postView.managingLocalOffice) : postView.managingLocalOffice == null;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + (currentTraineeId != null ? currentTraineeId.hashCode() : 0);
    result = 31 * result + (currentTraineeGmcNumber != null ? currentTraineeGmcNumber.hashCode() : 0);
    result = 31 * result + (currentTraineeSurname != null ? currentTraineeSurname.hashCode() : 0);
    result = 31 * result + (currentTraineeForenames != null ? currentTraineeForenames.hashCode() : 0);
    result = 31 * result + (nationalPostNumber != null ? nationalPostNumber.hashCode() : 0);
    result = 31 * result + (primarySiteId != null ? primarySiteId.hashCode() : 0);
    result = 31 * result + (approvedGradeId != null ? approvedGradeId.hashCode() : 0);
    result = 31 * result + (primarySpecialtyId != null ? primarySpecialtyId.hashCode() : 0);
    result = 31 * result + (programmeName != null ? programmeName.hashCode() : 0);
    result = 31 * result + status.hashCode();
    result = 31 * result + (fundingType != null ? fundingType.hashCode() : 0);
    result = 31 * result + (managingLocalOffice != null ? managingLocalOffice.hashCode() : 0);
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostView{" +
        "id=" + id +
        ", currentTraineeId=" + currentTraineeId +
        ", currentTraineeGmcNumber='" + currentTraineeGmcNumber + '\'' +
        ", currentTraineeSurname='" + currentTraineeSurname + '\'' +
        ", currentTraineeForenames='" + currentTraineeForenames + '\'' +
        ", nationalPostNumber='" + nationalPostNumber + '\'' +
        ", primarySiteId='" + primarySiteId + '\'' +
        ", approvedGradeId='" + approvedGradeId + '\'' +
        ", primarySpecialtyId=" + primarySpecialtyId +
        ", programmeName='" + programmeName + '\'' +
        ", status=" + status +
        ", fundingType=" + fundingType +
        ", managingLocalOffice='" + managingLocalOffice + '\'' +
        ", intrepidId='" + intrepidId + '\'' +
        '}';
  }
}
