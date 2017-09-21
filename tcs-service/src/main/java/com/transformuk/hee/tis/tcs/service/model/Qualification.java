package com.transformuk.hee.tis.tcs.service.model;


import com.transformuk.hee.tis.tcs.api.enumeration.QualificationType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "personId")
  private Person person;

  private String qualification;

  private QualificationType qualificationType;

  private LocalDate qualificationAttainedDate;

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

  public QualificationType getQualificationType() {
    return qualificationType;
  }

  public void setQualificationType(QualificationType qualificationType) {
    this.qualificationType = qualificationType;
  }

  public Qualification qualificationType(QualificationType qualificationType) {
    this.qualificationType = qualificationType;
    return this;
  }

  public LocalDate getQualificationAttainedDate() {
    return qualificationAttainedDate;
  }

  public void setQualificationAttainedDate(LocalDate qualificationAttainedDate) {
    this.qualificationAttainedDate = qualificationAttainedDate;
  }

  public Qualification qualificationAttainedDate(LocalDate qualifiactionAttainedDate) {
    this.qualificationAttainedDate = qualifiactionAttainedDate;
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

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
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
        ", qualificationAttainedDate='" + getQualificationAttainedDate() + "'" +
        ", medicalSchool='" + getMedicalSchool() + "'" +
        ", countryOfQualification='" + getCountryOfQualification() + "'" +
        "}";
  }
}
