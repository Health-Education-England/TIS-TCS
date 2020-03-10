package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.QualificationType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import lombok.Data;

/**
 * A Qualification.
 */
@Data
@Entity
public class Qualification implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String intrepidId;

  @ManyToOne
  @JoinColumn(name = "personId")
  private Person person;

  private String qualification;

  @Enumerated(EnumType.STRING)
  private QualificationType qualificationType;

  private LocalDate qualificationAttainedDate;

  private String medicalSchool;

  private String countryOfQualification;

  @Version
  private LocalDateTime amendedDate;

  public Qualification id(Long id) {
    this.id = id;
    return this;
  }

  public Qualification intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public Qualification qualification(String qualification) {
    this.qualification = qualification;
    return this;
  }

  public Qualification qualificationType(QualificationType qualificationType) {
    this.qualificationType = qualificationType;
    return this;
  }

  public Qualification qualificationAttainedDate(LocalDate qualifiactionAttainedDate) {
    this.qualificationAttainedDate = qualifiactionAttainedDate;
    return this;
  }

  public Qualification medicalSchool(String medicalSchool) {
    this.medicalSchool = medicalSchool;
    return this;
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
}
