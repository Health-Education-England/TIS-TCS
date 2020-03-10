package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PermitToWorkType;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Version;
import lombok.Data;

/**
 * A RightToWork.
 */
@Data
@Entity
public class RightToWork implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  private Long id;

  private String eeaResident;

  @Enumerated(EnumType.STRING)
  private PermitToWorkType permitToWork;

  private String settled;

  private LocalDate visaIssued;

  private LocalDate visaValidTo;

  private String visaDetails;

  @Version
  private LocalDateTime amendedDate;

  public RightToWork id(Long id) {
    this.id = id;
    return this;
  }

  public RightToWork eeaResident(String eeaResident) {
    this.eeaResident = eeaResident;
    return this;
  }

  public RightToWork permitToWork(PermitToWorkType permitToWork) {
    this.permitToWork = permitToWork;
    return this;
  }

  public RightToWork settled(String settled) {
    this.settled = settled;
    return this;
  }

  public RightToWork visaIssued(LocalDate visaIssued) {
    this.visaIssued = visaIssued;
    return this;
  }

  public RightToWork visaValidTo(LocalDate visaValidTo) {
    this.visaValidTo = visaValidTo;
    return this;
  }

  public RightToWork visaDetails(String visaDetails) {
    this.visaDetails = visaDetails;
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
    RightToWork rightToWork = (RightToWork) o;
    if (rightToWork.getId() == null || getId() == null) {
      return false;
    }
    return Objects.equals(getId(), rightToWork.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId());
  }
}
