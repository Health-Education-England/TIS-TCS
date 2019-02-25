package com.transformuk.hee.tis.tcs.service.job.post;


import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.Funding;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Document(indexName = "posts", type = "post")
public class PostView implements Serializable {

  @Id
  private Long id;

  @Field(type = FieldType.Keyword)
  private String nationalPostNumber;

  @Field(type = FieldType.Keyword)
  private Long primarySiteId;

  @Field(type = FieldType.Keyword)
  private Long approvedGradeId;

  @Field(type = FieldType.Keyword)
  private Long primarySpecialty;

  @Field(type = FieldType.Nested)
  private Set<ProgrammeName> programmeNames;

  @Field(type = FieldType.Nested)
  private Set<CurrentTrainee> currentTrainee;

  @Field(type = FieldType.Nested)
  private Set<FundingType> fundingType;

  @Field(type = FieldType.Text, analyzer = "standard")
  private Status status;

  @Field(type = FieldType.Keyword)
  private String owner;

  @Field(type = FieldType.Nested)
  private Set<PostTrustDTO> trusts;

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

  public Long getPrimarySpecialty() {
    return primarySpecialty;
  }

  public void setPrimarySpecialty(Long primarySpecialty) {
    this.primarySpecialty = primarySpecialty;
  }

  public Set<ProgrammeName> getProgrammeNames() {
    return programmeNames;
  }

  public void setProgrammeNames(Set<ProgrammeName> programmeNames) {
    this.programmeNames = programmeNames;
  }

  public Set<CurrentTrainee> getCurrentTrainee() {
    return currentTrainee;
  }

  public void setCurrentTrainee(Set<CurrentTrainee> currentTrainee) {
    this.currentTrainee = currentTrainee;
  }

  public Set<FundingType> getFundingType() {
    return fundingType;
  }

  public void setFundingType(Set<FundingType> fundingType) {
    this.fundingType = fundingType;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Set<PostTrustDTO> getTrusts() {
    return trusts;
  }

  public void setTrusts(Set<PostTrustDTO> trusts) {
    this.trusts = trusts;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PostView postView = (PostView) o;
    return Objects.equals(id, postView.id) &&
        Objects.equals(nationalPostNumber, postView.nationalPostNumber) &&
        Objects.equals(primarySiteId, postView.primarySiteId) &&
        Objects.equals(approvedGradeId, postView.approvedGradeId) &&
        Objects.equals(primarySpecialty, postView.primarySpecialty) &&
        Objects.equals(programmeNames, postView.programmeNames) &&
        Objects.equals(currentTrainee, postView.currentTrainee) &&
        Objects.equals(fundingType, postView.fundingType) &&
        status == postView.status &&
        Objects.equals(owner, postView.owner) &&
        Objects.equals(trusts, postView.trusts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, nationalPostNumber, primarySiteId, approvedGradeId, primarySpecialty, programmeNames, currentTrainee, fundingType, status, owner, trusts);
  }
}
