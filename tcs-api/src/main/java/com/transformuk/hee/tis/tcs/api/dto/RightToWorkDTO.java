package com.transformuk.hee.tis.tcs.api.dto;


import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * A DTO for the RightToWork entity.
 */
public class RightToWorkDTO implements Serializable {

  @NotNull(message = "Id is required", groups = {Update.class, Create.class})
  @DecimalMin(value = "0", groups = {Update.class, Create.class}, message = "Id must not be negative")
  private Long id;

  private String eeaResident;

  private String permitToWork;

  private String settled;

  private LocalDate visaIssued;

  private LocalDate visaValidTo;

  private String visaDetails;

  private LocalDateTime amendedDate;

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
        ", amendedDate='" + getAmendedDate() + "'" +
        "}";
  }
}
