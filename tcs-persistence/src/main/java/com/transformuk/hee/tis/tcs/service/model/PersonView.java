package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PersonOwnerRule;
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
 * This view is built to sit behind the person list.
 */
@Entity
@Table(name = "PersonView")
public class PersonView implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
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

  private Long gradeId;

  private String gradeAbbreviation;

  private Long siteId;

  private String siteCode;

  private String placementType;

  private String specialty;

  private String role;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private Status status;

  private String currentOwner;

  @Enumerated(EnumType.STRING)
  private PersonOwnerRule currentOwnerRule;

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

  public String getSiteCode() {
    return siteCode;
  }

  public void setSiteCode(String siteCode) {
    this.siteCode = siteCode;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PersonView that = (PersonView) o;

    if (id != null ? !id.equals(that.id) : that.id != null) return false;
    if (intrepidId != null ? !intrepidId.equals(that.intrepidId) : that.intrepidId != null) return false;
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
    if (gradeId != null ? !gradeId.equals(that.gradeId) : that.gradeId != null) return false;
    if (gradeAbbreviation != null ? !gradeAbbreviation.equals(that.gradeAbbreviation) : that.gradeAbbreviation != null)
      return false;
    if (siteId != null ? !siteId.equals(that.siteId) : that.siteId != null) return false;
    if (siteCode != null ? !siteCode.equals(that.siteCode) : that.siteCode != null) return false;
    if (placementType != null ? !placementType.equals(that.placementType) : that.placementType != null) return false;
    if (specialty != null ? !specialty.equals(that.specialty) : that.specialty != null) return false;
    if (role != null ? !role.equals(that.role) : that.role != null) return false;
    if (status != that.status) return false;
    if (currentOwner != null ? !currentOwner.equals(that.currentOwner) : that.currentOwner != null) return false;
    return currentOwnerRule == that.currentOwnerRule;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (intrepidId != null ? intrepidId.hashCode() : 0);
    result = 31 * result + (surname != null ? surname.hashCode() : 0);
    result = 31 * result + (forenames != null ? forenames.hashCode() : 0);
    result = 31 * result + (gmcNumber != null ? gmcNumber.hashCode() : 0);
    result = 31 * result + (gdcNumber != null ? gdcNumber.hashCode() : 0);
    result = 31 * result + (publicHealthNumber != null ? publicHealthNumber.hashCode() : 0);
    result = 31 * result + (programmeId != null ? programmeId.hashCode() : 0);
    result = 31 * result + (programmeName != null ? programmeName.hashCode() : 0);
    result = 31 * result + (programmeNumber != null ? programmeNumber.hashCode() : 0);
    result = 31 * result + (trainingNumber != null ? trainingNumber.hashCode() : 0);
    result = 31 * result + (gradeId != null ? gradeId.hashCode() : 0);
    result = 31 * result + (gradeAbbreviation != null ? gradeAbbreviation.hashCode() : 0);
    result = 31 * result + (siteId != null ? siteId.hashCode() : 0);
    result = 31 * result + (siteCode != null ? siteCode.hashCode() : 0);
    result = 31 * result + (placementType != null ? placementType.hashCode() : 0);
    result = 31 * result + (specialty != null ? specialty.hashCode() : 0);
    result = 31 * result + (role != null ? role.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (currentOwner != null ? currentOwner.hashCode() : 0);
    result = 31 * result + (currentOwnerRule != null ? currentOwnerRule.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PersonView{" +
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
        ", gradeId=" + gradeId +
        ", gradeAbbreviation='" + gradeAbbreviation + '\'' +
        ", siteId=" + siteId +
        ", siteCode='" + siteCode + '\'' +
        ", placementType='" + placementType + '\'' +
        ", specialty='" + specialty + '\'' +
        ", role='" + role + '\'' +
        ", status=" + status +
        ", currentOwner='" + currentOwner + '\'' +
        ", currentOwnerRule=" + currentOwnerRule +
        '}';
  }
}