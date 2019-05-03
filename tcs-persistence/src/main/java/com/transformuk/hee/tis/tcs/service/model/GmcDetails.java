package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A GmcDetails.
 */
@Entity
public class GmcDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  private String gmcNumber;

  private String gmcStatus;

  private LocalDate gmcStartDate;

  private LocalDate gmcEndDate;

  @Version
  private LocalDateTime amendedDate;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public GmcDetails id(Long id) {
    this.id = id;
    return this;
  }

  public String getGmcNumber() {
    return gmcNumber;
  }

  public void setGmcNumber(String gmcNumber) {
    this.gmcNumber = gmcNumber;
  }

  public GmcDetails gmcNumber(String gmcNumber) {
    this.gmcNumber = gmcNumber;
    return this;
  }

  public String getGmcStatus() {
    return gmcStatus;
  }

  public void setGmcStatus(String gmcStatus) {
    this.gmcStatus = gmcStatus;
  }

  public GmcDetails gmcStatus(String gmcStatus) {
    this.gmcStatus = gmcStatus;
    return this;
  }

  public LocalDate getGmcStartDate() {
    return gmcStartDate;
  }

  public void setGmcStartDate(LocalDate gmcStartDate) {
    this.gmcStartDate = gmcStartDate;
  }

  public GmcDetails gmcStartDate(LocalDate gmcStartDate) {
    this.gmcStartDate = gmcStartDate;
    return this;
  }

  public LocalDate getGmcEndDate() {
    return gmcEndDate;
  }

  public void setGmcEndDate(LocalDate gmcEndDate) {
    this.gmcEndDate = gmcEndDate;
  }

  public GmcDetails gmcEndDate(LocalDate gmcEndDate) {
    this.gmcEndDate = gmcEndDate;
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
    GmcDetails gmcDetails = (GmcDetails) o;
    if (gmcDetails.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), gmcDetails.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "GmcDetails{" +
        "id=" + getId() +
        ", gmcNumber='" + getGmcNumber() + "'" +
        ", gmcStatus='" + getGmcStatus() + "'" +
        ", gmcStartDate='" + getGmcStartDate() + "'" +
        ", gmcEndDate='" + getGmcEndDate() + "'" +
        ", amendedDate='" + getAmendedDate() + "'" +
        "}";
  }
}
