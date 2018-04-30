package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A DTO for the PersonalDetails entity.
 */
public class PersonalDetailsDTO implements Serializable {

  @NotNull(message = "Id is required", groups = {Update.class, Create.class})
  @DecimalMin(value = "0", groups = {Update.class, Create.class}, message = "Id must not be negative")
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

  private String nationalInsuranceNumber;

  private LocalDateTime amendedDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getMaritalStatus() {
    return maritalStatus;
  }

  public void setMaritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getNationality() {
    return nationality;
  }

  public void setNationality(String nationality) {
    this.nationality = nationality;
  }

  public String getDualNationality() {
    return dualNationality;
  }

  public void setDualNationality(String dualNationality) {
    this.dualNationality = dualNationality;
  }

  public String getSexualOrientation() {
    return sexualOrientation;
  }

  public void setSexualOrientation(String sexualOrientation) {
    this.sexualOrientation = sexualOrientation;
  }

  public String getReligiousBelief() {
    return religiousBelief;
  }

  public void setReligiousBelief(String religiousBelief) {
    this.religiousBelief = religiousBelief;
  }

  public String getEthnicOrigin() {
    return ethnicOrigin;
  }

  public void setEthnicOrigin(String ethnicOrigin) {
    this.ethnicOrigin = ethnicOrigin;
  }

  public String getDisability() {
    return disability;
  }

  public void setDisability(String disability) {
    this.disability = disability;
  }

  public String getDisabilityDetails() {
    return disabilityDetails;
  }

  public void setDisabilityDetails(String disabilityDetails) {
    this.disabilityDetails = disabilityDetails;
  }

  public String getNationalInsuranceNumber() {
    return nationalInsuranceNumber;
  }

  public void setNationalInsuranceNumber(String nationalInsuranceNumber) {
    this.nationalInsuranceNumber = nationalInsuranceNumber;
  }

  public LocalDateTime getAmendedDate() {
    return amendedDate;
  }

  public void setAmendedDate(LocalDateTime amendedDate) {
    this.amendedDate = amendedDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PersonalDetailsDTO personalDetailsDTO = (PersonalDetailsDTO) o;
    if (personalDetailsDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), personalDetailsDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "PersonalDetailsDTO{" +
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
        ", nationalInsuranceNumber='" + getNationalInsuranceNumber() + "'" +
        ", amendedDate='" + getAmendedDate() + "'" +
        "}";
  }
}
