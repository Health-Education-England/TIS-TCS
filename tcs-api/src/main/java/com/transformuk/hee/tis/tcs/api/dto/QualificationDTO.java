package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.QualificationType;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A DTO for the Qualification entity.
 */
public class QualificationDTO implements Serializable {

  @NotNull(groups = Update.class, message = "Id must not be null when updating a qualification")
  @DecimalMin(value = "0", groups = Update.class, message = "Id must not be negative")
  @Null(groups = Create.class, message = "Id must be null when creating a new qualification")
  private Long id;

  private String intrepidId;

  @NotNull(message = "Qualification is required", groups = {Update.class, Create.class})
  private String qualification;

  private PersonDTO person;

  private QualificationType qualificationType;

  private LocalDate qualificationAttainedDate;

  @NotNull(message = "Medical School is required", groups = {Update.class, Create.class})
  private String medicalSchool;

  @NotNull(message = "Country Of Qualification is required", groups = {Update.class, Create.class})
  private String countryOfQualification;

  private LocalDateTime amendedDate;

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

  public String getQualification() {
    return qualification;
  }

  public void setQualification(String qualification) {
    this.qualification = qualification;
  }

  public QualificationType getQualificationType() {
    return qualificationType;
  }

  public void setQualificationType(QualificationType qualificationType) {
    this.qualificationType = qualificationType;
  }

  public LocalDate getQualificationAttainedDate() {
    return qualificationAttainedDate;
  }

  public void setQualificationAttainedDate(LocalDate qualificationAttainedDate) {
    this.qualificationAttainedDate = qualificationAttainedDate;
  }

  public String getMedicalSchool() {
    return medicalSchool;
  }

  public void setMedicalSchool(String medicalSchool) {
    this.medicalSchool = medicalSchool;
  }

  public String getCountryOfQualification() {
    return countryOfQualification;
  }

  public void setCountryOfQualification(String countryOfQualification) {
    this.countryOfQualification = countryOfQualification;
  }

  public PersonDTO getPerson() {
    return person;
  }

  public void setPerson(PersonDTO person) {
    this.person = person;
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

    QualificationDTO qualificationDTO = (QualificationDTO) o;
    if (qualificationDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), qualificationDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "QualificationDTO{" +
        "id=" + getId() +
        ", intrepidId='" + getIntrepidId() +  "'" +
        ", qualification='" + getQualification() + "'" +
        ", qualificationType='" + getQualificationType() + "'" +
        ", qualifiactionAttainedDate='" + getQualificationAttainedDate() + "'" +
        ", medicalSchool='" + getMedicalSchool() + "'" +
        ", countryOfQualification='" + getCountryOfQualification() + "'" +
        ", person='" + getPerson() + "'" +
        ", amendedDate='" + getAmendedDate() + "'" +
        "}";
  }
}
