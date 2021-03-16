package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Entity;
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

  private String permitToWork;

  private String settled;

  private LocalDate visaIssued;

  private LocalDate visaValidTo;

  private String visaDetails;

  @Version
  private LocalDateTime amendedDate;

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
