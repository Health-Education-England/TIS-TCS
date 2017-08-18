package com.transformuk.hee.tis.tcs.api.dto;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the RightToWork entity.
 */
public class RightToWorkDTO implements Serializable {

  private Long id;

  private String eeaResident;

  private String permitToWork;

  private String settled;

  private LocalDate visaIssued;

  private LocalDate visaValidTo;

  private String visaDetails;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEeaResident() {
    return eeaResident;
  }

  public void setEeaResident(String eeaResident) {
    this.eeaResident = eeaResident;
  }

  public String getPermitToWork() {
    return permitToWork;
  }

  public void setPermitToWork(String permitToWork) {
    this.permitToWork = permitToWork;
  }

  public String getSettled() {
    return settled;
  }

  public void setSettled(String settled) {
    this.settled = settled;
  }

  public LocalDate getVisaIssued() {
    return visaIssued;
  }

  public void setVisaIssued(LocalDate visaIssued) {
    this.visaIssued = visaIssued;
  }

  public LocalDate getVisaValidTo() {
    return visaValidTo;
  }

  public void setVisaValidTo(LocalDate visaValidTo) {
    this.visaValidTo = visaValidTo;
  }

  public String getVisaDetails() {
    return visaDetails;
  }

  public void setVisaDetails(String visaDetails) {
    this.visaDetails = visaDetails;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RightToWorkDTO rightToWorkDTO = (RightToWorkDTO) o;
    if (rightToWorkDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), rightToWorkDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "RightToWorkDTO{" +
        "id=" + getId() +
        ", eeaResident='" + getEeaResident() + "'" +
        ", permitToWork='" + getPermitToWork() + "'" +
        ", settled='" + getSettled() + "'" +
        ", visaIssued='" + getVisaIssued() + "'" +
        ", visaValidTo='" + getVisaValidTo() + "'" +
        ", visaDetails='" + getVisaDetails() + "'" +
        "}";
  }
}
