package com.transformuk.hee.tis.tcs.service.model;

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
  private Long primarySiteId;

  @Column(name = "primarySiteCode")
  private String primarySiteCode;

  @Column(name = "approvedGradeId")
  private Long approvedGradeId;

  @Column(name = "approvedGradeCode")
  private String approvedGradeCode;

  @Column(name = "primarySpecialtyId")
  private Long primarySpecialtyId;

  @Column(name = "primarySpecialtyCode")
  private String primarySpecialtyCode;

  @Column(name = "primarySpecialtyName")
  private String primarySpecialtyName;

  @Column(name = "programmeNames")
  private String programmeNames;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private Status status;

  @Column(name = "fundingType")
  private String fundingType;

  @Column(name = "owner")
  private String owner;

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

  public String getPrimarySiteCode() {
    return primarySiteCode;
  }

  public void setPrimarySiteCode(String primarySiteCode) {
    this.primarySiteCode = primarySiteCode;
  }

  public String getApprovedGradeCode() {
    return approvedGradeCode;
  }

  public void setApprovedGradeCode(String approvedGradeCode) {
    this.approvedGradeCode = approvedGradeCode;
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

  public String getProgrammeNames() {
    return programmeNames;
  }

  public void setProgrammeNames(String programmeNames) {
    this.programmeNames = programmeNames;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getFundingType() {
    return fundingType;
  }

  public void setFundingType(String fundingType) {
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
    if (primarySiteCode != null ? !primarySiteCode.equals(postView.primarySiteCode) : postView.primarySiteCode != null)
      return false;
    if (approvedGradeCode != null ? !approvedGradeCode.equals(postView.approvedGradeCode) : postView.approvedGradeCode != null)
      return false;
    if (primarySpecialtyId != null ? !primarySpecialtyId.equals(postView.primarySpecialtyId) : postView.primarySpecialtyId != null)
      return false;
    if (primarySpecialtyCode != null ? !primarySpecialtyCode.equals(postView.primarySpecialtyCode) : postView.primarySpecialtyCode != null)
      return false;
    if (primarySpecialtyName != null ? !primarySpecialtyName.equals(postView.primarySpecialtyName) : postView.primarySpecialtyName != null)
      return false;
    if (programmeNames != null ? !programmeNames.equals(postView.programmeNames) : postView.programmeNames != null)
      return false;
    if (status != postView.status) return false;
    if (fundingType != postView.fundingType) return false;
    if (owner != null ? !owner.equals(postView.owner) : postView.owner != null)
      return false;
    return intrepidId != null ? intrepidId.equals(postView.intrepidId) : postView.intrepidId == null;
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
    result = 31 * result + (approvedGradeCode != null ? approvedGradeCode.hashCode() : 0);
    result = 31 * result + (primarySpecialtyId != null ? primarySpecialtyId.hashCode() : 0);
    result = 31 * result + (primarySpecialtyCode != null ? primarySpecialtyCode.hashCode() : 0);
    result = 31 * result + (primarySpecialtyName != null ? primarySpecialtyName.hashCode() : 0);
    result = 31 * result + (programmeNames != null ? programmeNames.hashCode() : 0);
    result = 31 * result + status.hashCode();
    result = 31 * result + (fundingType != null ? fundingType.hashCode() : 0);
    result = 31 * result + (owner != null ? owner.hashCode() : 0);
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
        ", primarySiteId=" + primarySiteId +
        ", primarySiteCode='" + primarySiteCode + '\'' +
        ", approvedGradeId='" + approvedGradeId + '\'' +
        ", approvedGradeCode='" + approvedGradeCode + '\'' +
        ", primarySpecialtyId=" + primarySpecialtyId +
        ", primarySpecialtyCode='" + primarySpecialtyCode + '\'' +
        ", primarySpecialtyName='" + primarySpecialtyName + '\'' +
        ", programmeNames='" + programmeNames + '\'' +
        ", status=" + status +
        ", fundingType=" + fundingType +
        ", owner='" + owner + '\'' +
        ", intrepidId='" + intrepidId + '\'' +
        '}';
  }
}
