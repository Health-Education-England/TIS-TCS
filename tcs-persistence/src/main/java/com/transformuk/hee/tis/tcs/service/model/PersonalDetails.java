package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.Disability;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import lombok.Data;

/**
 * A PersonalDetails.
 */
@Data
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

  private String nationalInsuranceNumber;

  @Version
  private LocalDateTime amendedDate;

  public PersonalDetails id(Long id) {
    this.id = id;
    return this;
  }

  public PersonalDetails maritalStatus(String maritalStatus) {
    this.maritalStatus = maritalStatus;
    return this;
  }

  public PersonalDetails dateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
    return this;
  }

  public PersonalDetails gender(String gender) {
    this.gender = gender;
    return this;
  }

  public PersonalDetails nationality(String nationality) {
    this.nationality = nationality;
    return this;
  }

  public PersonalDetails dualNationality(String dualNationality) {
    this.dualNationality = dualNationality;
    return this;
  }

  public PersonalDetails sexualOrientation(String sexualOrientation) {
    this.sexualOrientation = sexualOrientation;
    return this;
  }

  public PersonalDetails religiousBelief(String religiousBelief) {
    this.religiousBelief = religiousBelief;
    return this;
  }

  public PersonalDetails ethnicOrigin(String ethnicOrigin) {
    this.ethnicOrigin = ethnicOrigin;
    return this;
  }

  public PersonalDetails disability(String disability) {
    this.disability = normaliseOrKeepOriginal(disability);
    return this;
  }

  // Rewrite set method for disability
  public void setDisability(String disability) {
    this.disability = normaliseOrKeepOriginal(disability);
  }

  public PersonalDetails disabilityDetails(String disabilityDetails) {
    this.disabilityDetails = disabilityDetails;
    return this;
  }

  public PersonalDetails nationalInsuranceNumber(String nationalInsuranceNumber) {
    this.nationalInsuranceNumber = nationalInsuranceNumber;
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

  private static String normaliseOrKeepOriginal(String value) {
    if (value == null)
      return null;

    try {
      return Disability.valueOf(value.trim().toUpperCase()).name();
    } catch (IllegalArgumentException e) { // There are legacy non-enumeration values in the DB.
      return value;
    }
  }
}
