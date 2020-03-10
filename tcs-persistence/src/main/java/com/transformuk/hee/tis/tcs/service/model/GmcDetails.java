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
 * A GmcDetails.
 */
@Data
@Entity
public class GmcDetails implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  @Convert(converter = RegistrationNumberConverter.class)
  private String gmcNumber;

  private String gmcStatus;

  private LocalDate gmcStartDate;

  private LocalDate gmcEndDate;

  @Version
  private LocalDateTime amendedDate;

  public GmcDetails id(Long id) {
    this.id = id;
    return this;
  }

  public GmcDetails gmcNumber(String gmcNumber) {
    this.gmcNumber = gmcNumber;
    return this;
  }

  public GmcDetails gmcStatus(String gmcStatus) {
    this.gmcStatus = gmcStatus;
    return this;
  }

  public GmcDetails gmcStartDate(LocalDate gmcStartDate) {
    this.gmcStartDate = gmcStartDate;
    return this;
  }

  public GmcDetails gmcEndDate(LocalDate gmcEndDate) {
    this.gmcEndDate = gmcEndDate;
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
}
