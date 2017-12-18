package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.LocalOfficeRule;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;

import java.io.Serializable;

/**
 * This DTO is used in the person list, it's meant as a read only entity aggregating what the user needs to see
 * in a person list.
 */
public class PersonViewDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long id;

  private String intrepidId;

  private String surname;

  private String forenames;

  private String gmcNumber;

  private String gdcNumber;

  private String publicHealthNumber;

  private Long programmeId;

  private String programmeName;

  private String programmeNumber;

  private String trainingNumber;

  private String gradeAbbreviation;

  private String gradeName;

  private String siteCode;

  private String siteName;

  private String placementType;

  private String role;

  private Status status;

  private String currentLocalOffice;

  private LocalOfficeRule currentLocalOfficeRule;

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

  public String getCurrentLocalOffice() {
    return currentLocalOffice;
  }

  public void setCurrentLocalOffice(String currentLocalOffice) {
    this.currentLocalOffice = currentLocalOffice;
  }

  public LocalOfficeRule getCurrentLocalOfficeRule() {
    return currentLocalOfficeRule;
  }

  public void setCurrentLocalOfficeRule(LocalOfficeRule currentLocalOfficeRule) {
    this.currentLocalOfficeRule = currentLocalOfficeRule;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonViewDTO that = (PersonViewDTO) o;

    if (!id.equals(that.id)) return false;
    if (!intrepidId.equals(that.intrepidId)) return false;
    if (surname != null ? !surname.equals(that.surname) : that.surname != null) return false;
    if (forenames != null ? !forenames.equals(that.forenames) : that.forenames != null) return false;
    if (gmcNumber != null ? !gmcNumber.equals(that.gmcNumber) : that.gmcNumber != null) return false;
    if (gdcNumber != null ? !gdcNumber.equals(that.gdcNumber) : that.gdcNumber != null) return false;
    if (publicHealthNumber != null ? !publicHealthNumber.equals(that.publicHealthNumber) : that.publicHealthNumber != null)
      return false;
    if (programmeId != null ? !programmeId.equals(that.programmeId) : that.programmeId != null) return false;
    if (programmeName != null ? !programmeName.equals(that.programmeName) : that.programmeName != null) return false;
    if (programmeNumber != null ? !programmeNumber.equals(that.programmeNumber) : that.programmeNumber != null)
      return false;
    if (trainingNumber != null ? !trainingNumber.equals(that.trainingNumber) : that.trainingNumber != null)
      return false;
    if (gradeAbbreviation != null ? !gradeAbbreviation.equals(that.gradeAbbreviation) : that.gradeAbbreviation != null)
      return false;
    if (gradeName != null ? !gradeName.equals(that.gradeName) : that.gradeName != null) return false;
    if (siteCode != null ? !siteCode.equals(that.siteCode) : that.siteCode != null) return false;
    if (siteName != null ? !siteName.equals(that.siteName) : that.siteName != null) return false;
    if (placementType != null ? !placementType.equals(that.placementType) : that.placementType != null) return false;
    if (role != that.role) return false;
    if (status != that.status) return false;
    if (currentLocalOffice != null ? !currentLocalOffice.equals(that.currentLocalOffice) : that.currentLocalOffice != null)
      return false;
    return currentLocalOfficeRule != null ? currentLocalOfficeRule.equals(that.currentLocalOfficeRule) : that.currentLocalOfficeRule == null;
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + intrepidId.hashCode();
    result = 31 * result + (surname != null ? surname.hashCode() : 0);
    result = 31 * result + (forenames != null ? forenames.hashCode() : 0);
    result = 31 * result + (gmcNumber != null ? gmcNumber.hashCode() : 0);
    result = 31 * result + (gdcNumber != null ? gdcNumber.hashCode() : 0);
    result = 31 * result + (publicHealthNumber != null ? publicHealthNumber.hashCode() : 0);
    result = 31 * result + (programmeId != null ? programmeId.hashCode() : 0);
    result = 31 * result + (programmeName != null ? programmeName.hashCode() : 0);
    result = 31 * result + (programmeNumber != null ? programmeNumber.hashCode() : 0);
    result = 31 * result + (trainingNumber != null ? trainingNumber.hashCode() : 0);
    result = 31 * result + (gradeAbbreviation != null ? gradeAbbreviation.hashCode() : 0);
    result = 31 * result + (gradeName != null ? gradeName.hashCode() : 0);
    result = 31 * result + (siteCode != null ? siteCode.hashCode() : 0);
    result = 31 * result + (siteName != null ? siteName.hashCode() : 0);
    result = 31 * result + (placementType != null ? placementType.hashCode() : 0);
    result = 31 * result + (role != null ? role.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (currentLocalOffice != null ? currentLocalOffice.hashCode() : 0);
    result = 31 * result + (currentLocalOfficeRule != null ? currentLocalOfficeRule.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PersonViewDTO{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", surname='" + surname + '\'' +
        ", forenames='" + forenames + '\'' +
        ", gmcNumber='" + gmcNumber + '\'' +
        ", gdcNumber='" + gdcNumber + '\'' +
        ", publicHealthNumber='" + publicHealthNumber + '\'' +
        ", programmeId=" + programmeId +
        ", programmeName='" + programmeName + '\'' +
        ", programmeNumber='" + programmeNumber + '\'' +
        ", trainingNumber='" + trainingNumber + '\'' +
        ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
        ", gradeName='" + gradeName + '\'' +
        ", siteCode='" + siteCode + '\'' +
        ", siteName='" + siteName + '\'' +
        ", placementType='" + placementType + '\'' +
        ", role=" + role +
        ", status=" + status +
        ", currentLocalOffice='" + currentLocalOffice + '\'' +
        ", currentLocalOfficeRule='" + currentLocalOfficeRule + '\'' +
        '}';
  }
}

