package com.transformuk.hee.tis.tcs.service.dto.placementmanager;

import java.io.Serializable;
import java.util.Objects;

public class PersonDTO implements Serializable {

  private Long id;
  private String surname;
  private String forename;
  private String gmc;
  private String gdc;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSurname() {
    return surname;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public String getForename() {
    return forename;
  }

  public void setForename(String forename) {
    this.forename = forename;
  }

  public String getGmc() {
    return gmc;
  }

  public void setGmc(String gmc) {
    this.gmc = gmc;
  }

  public String getGdc() {
    return gdc;
  }

  public void setGdc(String gdc) {
    this.gdc = gdc;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PersonDTO personDTO = (PersonDTO) o;
    return Objects.equals(id, personDTO.id) &&
        Objects.equals(surname, personDTO.surname) &&
        Objects.equals(forename, personDTO.forename) &&
        Objects.equals(gmc, personDTO.gmc) &&
        Objects.equals(gdc, personDTO.gdc);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, surname, forename, gmc, gdc);
  }
}
