package com.transformuk.hee.tis.tcs.service.model;


import java.io.Serializable;
import java.util.Objects;

public class PersonLite implements Serializable {

  private static final long serialVersionUID = -3365982802660265305L;

  private Long id;
  private String name;
  private String publicHealthNumber;
  private String gmcNumber;
  private String gdcNumber;

  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public String getPublicHealthNumber() {
    return publicHealthNumber;
  }

  public void setPublicHealthNumber(final String publicHealthNumber) {
    this.publicHealthNumber = publicHealthNumber;
  }

  public String getGmcNumber() {
    return gmcNumber;
  }

  public void setGmcNumber(final String gmcNumber) {
    this.gmcNumber = gmcNumber;
  }

  public String getGdcNumber() {
    return gdcNumber;
  }

  public void setGdcNumber(final String gdcNumber) {
    this.gdcNumber = gdcNumber;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final PersonLite person = (PersonLite) o;
    if (person.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), person.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "PersonLite{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", publicHealthNumber='" + publicHealthNumber + '\'' +
        ", gmcNumber='" + gmcNumber + '\'' +
        ", gdcNumber='" + gdcNumber + '\'' +
        '}';
  }
}
