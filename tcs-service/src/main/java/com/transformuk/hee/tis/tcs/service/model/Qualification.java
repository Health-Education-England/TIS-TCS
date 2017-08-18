package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Qualification.
 */
@Entity
public class Qualification implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  private String qualification;

  private String qualificationType;

  private LocalDate qualifiactionAttainedDate;

  private String medicalSchool;

  private String countryOfQualification;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Qualification id(Long id) {
    this.id = id;
    return this;
  }

  public String getQualification() {
    return qualification;
  }

  public void setQualification(String qualification) {
    this.qualification = qualification;
  }

  public Qualification qualification(String qualification) {
    this.qualification = qualification;
    return this;
  }

  public String getQualificationType() {
    return qualificationType;
  }

  public void setQualificationType(String qualificationType) {
    this.qualificationType = qualificationType;
  }

  public Qualification qualificationType(String qualificationType) {
    this.qualificationType = qualificationType;
    return this;
  }

  public LocalDate getQualifiactionAttainedDate() {
    return qualifiactionAttainedDate;
  }

  public void setQualifiactionAttainedDate(LocalDate qualifiactionAttainedDate) {
    this.qualifiactionAttainedDate = qualifiactionAttainedDate;
  }

  public Qualification qualifiactionAttainedDate(LocalDate qualifiactionAttainedDate) {
    this.qualifiactionAttainedDate = qualifiactionAttainedDate;
    return this;
  }

  public String getMedicalSchool() {
    return medicalSchool;
  }

  public void setMedicalSchool(String medicalSchool) {
    this.medicalSchool = medicalSchool;
  }

  public Qualification medicalSchool(String medicalSchool) {
    this.medicalSchool = medicalSchool;
    return this;
  }

  public String getCountryOfQualification() {
    return countryOfQualification;
  }

  public void setCountryOfQualification(String countryOfQualification) {
    this.countryOfQualification = countryOfQualification;
  }

  public Qualification countryOfQualification(String countryOfQualification) {
    this.countryOfQualification = countryOfQualification;
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
    Qualification qualification = (Qualification) o;
    if (qualification.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), qualification.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "Qualification{" +
        "id=" + getId() +
        ", qualification='" + getQualification() + "'" +
        ", qualificationType='" + getQualificationType() + "'" +
        ", qualifiactionAttainedDate='" + getQualifiactionAttainedDate() + "'" +
        ", medicalSchool='" + getMedicalSchool() + "'" +
        ", countryOfQualification='" + getCountryOfQualification() + "'" +
        "}";
  }
}
