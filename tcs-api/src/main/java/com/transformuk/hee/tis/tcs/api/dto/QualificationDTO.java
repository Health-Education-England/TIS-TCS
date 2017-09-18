package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the Qualification entity.
 */
public class QualificationDTO implements Serializable {

  @NotNull(message = "Id is required", groups = {Update.class, Create.class})
  @DecimalMin(value = "0", groups = {Update.class, Create.class}, message = "Id must not be negative")
  private Long id;

  @NotNull(message = "Qualification is required", groups = {Update.class, Create.class})
  private String qualification;

  private String qualificationType;

  private LocalDate qualifiactionAttainedDate;

  @NotNull(message = "Medical School is required", groups = {Update.class, Create.class})
  private String medicalSchool;

  @NotNull(message = "Country Of Qualification is required", groups = {Update.class, Create.class})
  private String countryOfQualification;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getQualification() {
    return qualification;
  }

  public void setQualification(String qualification) {
    this.qualification = qualification;
  }

  public String getQualificationType() {
    return qualificationType;
  }

  public void setQualificationType(String qualificationType) {
    this.qualificationType = qualificationType;
  }

  public LocalDate getQualifiactionAttainedDate() {
    return qualifiactionAttainedDate;
  }

  public void setQualifiactionAttainedDate(LocalDate qualifiactionAttainedDate) {
    this.qualifiactionAttainedDate = qualifiactionAttainedDate;
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
        ", qualification='" + getQualification() + "'" +
        ", qualificationType='" + getQualificationType() + "'" +
        ", qualifiactionAttainedDate='" + getQualifiactionAttainedDate() + "'" +
        ", medicalSchool='" + getMedicalSchool() + "'" +
        ", countryOfQualification='" + getCountryOfQualification() + "'" +
        "}";
  }
}
