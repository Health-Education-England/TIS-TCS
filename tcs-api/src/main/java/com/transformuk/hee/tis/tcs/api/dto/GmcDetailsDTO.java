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
 * A DTO for the GmcDetails entity.
 */
public class GmcDetailsDTO implements Serializable {

  @NotNull(message = "Id is required", groups = {Update.class, Create.class})
  @DecimalMin(value = "0", groups = {Update.class, Create.class}, message = "Id must not be negative")
  private Long id;

  private String gmcNumber;

  private String gmcStatus;

  private LocalDate gmcStartDate;

  private LocalDate gmcEndDate;

  private LocalDateTime amendedDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGmcNumber() {
    return gmcNumber;
  }

  public void setGmcNumber(String gmcNumber) {
    this.gmcNumber = gmcNumber;
  }

  public String getGmcStatus() {
    return gmcStatus;
  }

  public void setGmcStatus(String gmcStatus) {
    this.gmcStatus = gmcStatus;
  }

  public LocalDate getGmcStartDate() {
    return gmcStartDate;
  }

  public void setGmcStartDate(LocalDate gmcStartDate) {
    this.gmcStartDate = gmcStartDate;
  }

  public LocalDate getGmcEndDate() {
    return gmcEndDate;
  }

  public void setGmcEndDate(LocalDate gmcEndDate) {
    this.gmcEndDate = gmcEndDate;
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

    GmcDetailsDTO gmcDetailsDTO = (GmcDetailsDTO) o;
    if (gmcDetailsDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), gmcDetailsDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "GmcDetailsDTO{" +
        "id=" + getId() +
        ", gmcNumber='" + getGmcNumber() + "'" +
        ", gmcStatus='" + getGmcStatus() + "'" +
        ", gmcStartDate='" + getGmcStartDate() + "'" +
        ", gmcEndDate='" + getGmcEndDate() + "'" +
        ", amendedDate='" + getAmendedDate() + "'" +
        "}";
  }
}
