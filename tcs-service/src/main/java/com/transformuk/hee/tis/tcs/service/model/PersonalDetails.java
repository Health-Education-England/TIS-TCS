package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A PersonalDetails.
 */
@Entity
public class PersonalDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  private String maritalStatus;

  private LocalDate dateOfBirth;

  private String gender;

  private String nationality;

  private String dualNationality;

  private String sexualOrientation;

  private String religiousBelief;

  private String ethnicOrigin;

  private String disability;

  private String disabilityDetails;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public PersonalDetails id(Long id) {
    this.id = id;
    return this;
  }

  public String getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public PersonalDetails maritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
    return this;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public PersonalDetails dateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public PersonalDetails gender(String gender) {
    this.gender = gender;
    return this;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public PersonalDetails nationality(String nationality) {
    this.nationality = nationality;
    return this;
  }

  public String getDualNationality() {
    return dualNationality;
  }

  public void setDualNationality(String dualNationality) {
    this.dualNationality = dualNationality;
  }

  public PersonalDetails dualNationality(String dualNationality) {
    this.dualNationality = dualNationality;
    return this;
  }

  public String getSexualOrientation() {
    return sexualOrientation;
  }

  public void setSexualOrientation(String sexualOrientation) {
    this.sexualOrientation = sexualOrientation;
  }

  public PersonalDetails sexualOrientation(String sexualOrientation) {
    this.sexualOrientation = sexualOrientation;
    return this;
  }

  public String getReligiousBelief() {
    return religiousBelief;
  }

  public void setReligiousBelief(String religiousBelief) {
    this.religiousBelief = religiousBelief;
  }

  public PersonalDetails religiousBelief(String religiousBelief) {
    this.religiousBelief = religiousBelief;
    return this;
  }

  public String getEthnicOrigin() {
    return ethnicOrigin;
  }

  public void setEthnicOrigin(String ethnicOrigin) {
    this.ethnicOrigin = ethnicOrigin;
  }

  public PersonalDetails ethnicOrigin(String ethnicOrigin) {
    this.ethnicOrigin = ethnicOrigin;
    return this;
  }

  public String getDisability() {
    return disability;
  }

  public void setDisability(String disability) {
    this.disability = disability;
  }

  public PersonalDetails disability(String disability) {
    this.disability = disability;
    return this;
  }

  public String getDisabilityDetails() {
    return disabilityDetails;
  }

  public void setDisabilityDetails(String disabilityDetails) {
    this.disabilityDetails = disabilityDetails;
  }

  public PersonalDetails disabilityDetails(String disabilityDetails) {
    this.disabilityDetails = disabilityDetails;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonalDetails personalDetails = (PersonalDetails) o;
    if (personalDetails.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), personalDetails.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "PersonalDetails{" +
        "id=" + getId() +
        ", maritalStatus='" + getMaritalStatus() + "'" +
        ", dateOfBirth='" + getDateOfBirth() + "'" +
        ", gender='" + getGender() + "'" +
        ", nationality='" + getNationality() + "'" +
        ", dualNationality='" + getDualNationality() + "'" +
        ", sexualOrientation='" + getSexualOrientation() + "'" +
        ", religiousBelief='" + getReligiousBelief() + "'" +
        ", ethnicOrigin='" + getEthnicOrigin() + "'" +
        ", disability='" + getDisability() + "'" +
        ", disabilityDetails='" + getDisabilityDetails() + "'" +
        "}";
  }
}
