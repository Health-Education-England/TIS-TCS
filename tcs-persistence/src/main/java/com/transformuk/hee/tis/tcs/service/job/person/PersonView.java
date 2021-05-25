package com.transformuk.hee.tis.tcs.service.job.person;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "persons")
public class PersonView implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private String id;

  private Long personId;

  private String intrepidId;

  @Field(type = FieldType.Keyword)
  private String surname;

  @Field(type = FieldType.Keyword)
  private String forenames;

  @Field(type = FieldType.Text, analyzer = "standard", searchAnalyzer = "standard")
  private String fullName;

  @Field(type = FieldType.Keyword)
  private String gmcNumber;

  @Field(type = FieldType.Keyword)
  private String gdcNumber;

  @Field(type = FieldType.Keyword)
  private String publicHealthNumber;

  private Long gradeId;

  private String gradeAbbreviation;

  private String gradeName;

  private Long siteId;

  private String siteCode;

  private String siteName;

  @Field(type = FieldType.Keyword)
  private String placementType;

  @Field(type = FieldType.Keyword)
  private String specialty;

  @Field(type = FieldType.Keyword)
  private String role;

  @Field(type = FieldType.Text, analyzer = "standard")
  private Status status;

  @Field(type = FieldType.Keyword)
  private String currentOwner;

  private String currentOwnerRule;

  @Field(type = FieldType.Nested)
  private Set<PersonTrustDto> trusts;

  @Field(type = FieldType.Nested)
  private Set<ProgrammeMembershipDto> programmeMemberships;

  public Set<ProgrammeMembershipDto> getProgrammeMemberships() {
    return programmeMemberships;
  }

  public void setProgrammeMemberships(Set<ProgrammeMembershipDto> programmeMemberships) {
    this.programmeMemberships = programmeMemberships;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Long getPersonId() {
    return personId;
  }

  public void setPersonId(Long personId) {
    this.personId = personId;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getForenames() {
    return forenames;
  }

  public void setForenames(String forenames) {
    this.forenames = forenames;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getGmcNumber() {
    return gmcNumber;
  }

  public void setGmcNumber(String gmcNumber) {
    this.gmcNumber = gmcNumber;
  }

  public String getGdcNumber() {
    return gdcNumber;
  }

  public void setGdcNumber(String gdcNumber) {
    this.gdcNumber = gdcNumber;
  }

  public String getPublicHealthNumber() {
    return publicHealthNumber;
  }

  public void setPublicHealthNumber(String publicHealthNumber) {
    this.publicHealthNumber = publicHealthNumber;
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

  public String getPlacementType() {
    return placementType;
  }

  public void setPlacementType(String placementType) {
    this.placementType = placementType;
  }

  public String getSpecialty() {
    return specialty;
  }

  public void setSpecialty(String specialty) {
    this.specialty = specialty;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getCurrentOwner() {
    return currentOwner;
  }

  public void setCurrentOwner(String currentOwner) {
    this.currentOwner = currentOwner;
  }

  public String getCurrentOwnerRule() {
    return currentOwnerRule;
  }

  public void setCurrentOwnerRule(String currentOwnerRule) {
    this.currentOwnerRule = currentOwnerRule;
  }

  public Long getGradeId() {
    return gradeId;
  }

  public void setGradeId(Long gradeId) {
    this.gradeId = gradeId;
  }

  public Long getSiteId() {
    return siteId;
  }

  public void setSiteId(Long siteId) {
    this.siteId = siteId;
  }

  public Set<PersonTrustDto> getTrusts() {
    return trusts;
  }

  public void setTrusts(Set<PersonTrustDto> trusts) {
    this.trusts = trusts;
  }
}

