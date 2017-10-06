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
 * A DTO for the GdcDetails entity.
 */
public class GdcDetailsDTO implements Serializable {

  @NotNull(message = "Id is required", groups = {Update.class, Create.class})
  @DecimalMin(value = "0", groups = {Update.class, Create.class}, message = "Id must not be negative")
  private Long id;

  private String gdcNumber;

  private String gdcStatus;

  private LocalDate gdcStartDate;

  private LocalDate gdcEndDate;

  private LocalDateTime amendedDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getGdcNumber() {
    return gdcNumber;
  }

  public void setGdcNumber(String gdcNumber) {
    this.gdcNumber = gdcNumber;
  }

  public String getGdcStatus() {
    return gdcStatus;
  }

  public void setGdcStatus(String gdcStatus) {
    this.gdcStatus = gdcStatus;
  }

  public LocalDate getGdcStartDate() {
    return gdcStartDate;
  }

  public void setGdcStartDate(LocalDate gdcStartDate) {
    this.gdcStartDate = gdcStartDate;
  }

  public LocalDate getGdcEndDate() {
    return gdcEndDate;
  }

  public void setGdcEndDate(LocalDate gdcEndDate) {
    this.gdcEndDate = gdcEndDate;
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

    GdcDetailsDTO gdcDetailsDTO = (GdcDetailsDTO) o;
    if (gdcDetailsDTO.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), gdcDetailsDTO.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }

  @Override
  public String toString() {
    return "GdcDetailsDTO{" +
        "id=" + getId() +
        ", gdcNumber='" + getGdcNumber() + "'" +
        ", gdcStatus='" + getGdcStatus() + "'" +
        ", gdcStartDate='" + getGdcStartDate() + "'" +
        ", gdcEndDate='" + getGdcEndDate() + "'" +
        ", amendedDate='" + getAmendedDate() + "'" +
        "}";
  }
}
