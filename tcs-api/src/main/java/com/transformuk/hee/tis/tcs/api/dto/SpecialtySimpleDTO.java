package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Specialty entity. It does not contain it's relationships to types and groups. Use
 * this when you want a faster response that does not hit any other data table except Specialties.
 */
public class SpecialtySimpleDTO implements Serializable {

  private Long id;

  private String intrepidId;

  private Status status;

  private String college;

  private String specialtyCode;

  private String name;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getCollege() {
    return college;
  }

  public void setCollege(String college) {
    this.college = college;
  }

  public String getSpecialtyCode() {
    return specialtyCode;
  }

  public void setSpecialtyCode(String specialtyCode) {
    this.specialtyCode = specialtyCode;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    SpecialtySimpleDTO specialtyDTO = (SpecialtySimpleDTO) o;

    if (!Objects.equals(id, specialtyDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "SpecialtyDTO{" +
        "id=" + id +
        ", intrepidId='" + intrepidId + '\'' +
        ", status=" + status +
        ", college='" + college + '\'' +
        ", specialtyCode='" + specialtyCode + '\'' +
        ", name=" + name +
        '}';
  }
}
