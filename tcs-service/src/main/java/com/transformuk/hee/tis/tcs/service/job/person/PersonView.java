package com.transformuk.hee.tis.tcs.service.job.person;

import com.transformuk.hee.tis.tcs.api.enumeration.PersonOwnerRule;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.List;

@Document(indexName = "persons", type = "person")
public class PersonView implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String intrepidId;

  //  @Field(type = FieldType.Text)
  private String surname;

  //  @Field(type = FieldType.Text)
  private String forenames;

  @Field(type = FieldType.Text, analyzer = "standard")
  private String fullName;

  private String gmcNumber;

  private String gdcNumber;

  @Field(type = FieldType.Keyword)
  private String publicHealthNumber;

  private Long programmeId;

  @Field(type = FieldType.Keyword)
  private String programmeName;

  private String programmeNumber;

  private String trainingNumber;

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

  private PersonOwnerRule currentOwnerRule;

  @Field(type = FieldType.Nested)
  private List<PersonTrustDto> trusts;

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

  public Long getProgrammeId() {
    return programmeId;
  }

  public void setProgrammeId(Long programmeId) {
    this.programmeId = programmeId;
  }

  public String getProgrammeName() {
    return programmeName;
  }

  public void setProgrammeName(String programmeName) {
    this.programmeName = programmeName;
  }

  public String getProgrammeNumber() {
    return programmeNumber;
  }

  public void setProgrammeNumber(String programmeNumber) {
    this.programmeNumber = programmeNumber;
  }

  public String getTrainingNumber() {
    return trainingNumber;
  }

  public void setTrainingNumber(String trainingNumber) {
    this.trainingNumber = trainingNumber;
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

  public PersonOwnerRule getCurrentOwnerRule() {
    return currentOwnerRule;
  }

  public void setCurrentOwnerRule(PersonOwnerRule currentOwnerRule) {
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

  public List<PersonTrustDto> getTrusts() {
    return trusts;
  }

  public void setTrusts(List<PersonTrustDto> trusts) {
    this.trusts = trusts;
  }
}

