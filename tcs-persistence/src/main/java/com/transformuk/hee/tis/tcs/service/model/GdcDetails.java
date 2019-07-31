package com.transformuk.hee.tis.tcs.service.model;


import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;

/**
 * A GdcDetails.
 */
@Entity
public class GdcDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  private String gdcNumber;

  private String gdcStatus;

  private LocalDate gdcStartDate;

  private LocalDate gdcEndDate;

  @Version
  private LocalDateTime amendedDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public GdcDetails id(Long id) {
    this.id = id;
    return this;
  }

  public String getGdcNumber() {
    return gdcNumber;
  }

  public void setGdcNumber(String gdcNumber) {
    this.gdcNumber = gdcNumber;
  }

  public GdcDetails gdcNumber(String gdcNumber) {
    this.gdcNumber = gdcNumber;
    return this;
  }

  public String getGdcStatus() {
    return gdcStatus;
  }

  public void setGdcStatus(String gdcStatus) {
    this.gdcStatus = gdcStatus;
  }

  public GdcDetails gdcStatus(String gdcStatus) {
    this.gdcStatus = gdcStatus;
    return this;
  }

  public LocalDate getGdcStartDate() {
    return gdcStartDate;
  }

  public void setGdcStartDate(LocalDate gdcStartDate) {
    this.gdcStartDate = gdcStartDate;
  }

  public GdcDetails gdcStartDate(LocalDate gdcStartDate) {
    this.gdcStartDate = gdcStartDate;
    return this;
  }

  public LocalDate getGdcEndDate() {
    return gdcEndDate;
  }

  public void setGdcEndDate(LocalDate gdcEndDate) {
    this.gdcEndDate = gdcEndDate;
  }

  public GdcDetails gdcEndDate(LocalDate gdcEndDate) {
    this.gdcEndDate = gdcEndDate;
    return this;
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
    GdcDetails gdcDetails = (GdcDetails) o;
    if (gdcDetails.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), gdcDetails.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "GdcDetails{" +
        "id=" + getId() +
        ", gdcNumber='" + getGdcNumber() + "'" +
        ", gdcStatus='" + getGdcStatus() + "'" +
        ", gdcStartDate='" + getGdcStartDate() + "'" +
        ", gdcEndDate='" + getGdcEndDate() + "'" +
        ", amendedDate='" + getAmendedDate() + "'" +
        "}";
  }
}
