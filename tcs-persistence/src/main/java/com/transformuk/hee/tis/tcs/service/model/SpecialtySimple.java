package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * A Specialty without it's relationships. Queries on this only hit the Specialty table
 */
@Data
@Entity
@Table(name = "Specialty")
public class SpecialtySimple implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String intrepidId;

  @Enumerated(EnumType.STRING)
  private Status status;

  @Column(name = "college")
  private String college;

  @Column(name = "specialtyCode")
  private String specialtyCode;

  @Column(name = "name")
  private String name;

  public SpecialtySimple status(Status status) {
    this.status = status;
    return this;
  }

  public SpecialtySimple college(String college) {
    this.college = college;
    return this;
  }

  public SpecialtySimple specialtyCode(String specialtyCode) {
    this.specialtyCode = specialtyCode;
    return this;
  }

  public SpecialtySimple intrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
    return this;
  }

  public SpecialtySimple name(String name) {
    this.name = name;
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
    SpecialtySimple specialty = (SpecialtySimple) o;
    if (specialty.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, specialty.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
