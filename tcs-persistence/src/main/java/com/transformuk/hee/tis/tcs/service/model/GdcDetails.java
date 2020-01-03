package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.service.model.converter.RegistrationNumberConverter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import lombok.Data;

/**
 * A GdcDetails.
 */
@Data
@Entity
public class GdcDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  @Convert(converter = RegistrationNumberConverter.class)
  private String gdcNumber;

  private String gdcStatus;

  private LocalDate gdcStartDate;

  private LocalDate gdcEndDate;

  @Version
  private LocalDateTime amendedDate;

  public GdcDetails id(Long id) {
    this.id = id;
    return this;
  }

  public GdcDetails gdcNumber(String gdcNumber) {
    this.gdcNumber = gdcNumber;
    return this;
  }

  public GdcDetails gdcStatus(String gdcStatus) {
    this.gdcStatus = gdcStatus;
    return this;
  }

  public GdcDetails gdcStartDate(LocalDate gdcStartDate) {
    this.gdcStartDate = gdcStartDate;
    return this;
  }

  public GdcDetails gdcEndDate(LocalDate gdcEndDate) {
    this.gdcEndDate = gdcEndDate;
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
}
