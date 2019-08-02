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

/**
 * A RightToWork.
 */
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


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public RightToWork id(Long id) {
    this.id = id;
    return this;
  }

  public String getEeaResident() {
    return eeaResident;
  }

  public void setEeaResident(String eeaResident) {
    this.eeaResident = eeaResident;
  }

  public RightToWork eeaResident(String eeaResident) {
    this.eeaResident = eeaResident;
    return this;
  }

  public PermitToWorkType getPermitToWork() {
    return permitToWork;
  }

  public void setPermitToWork(PermitToWorkType permitToWork) {
    this.permitToWork = permitToWork;
  }

  public RightToWork permitToWork(PermitToWorkType permitToWork) {
    this.permitToWork = permitToWork;
    return this;
  }

  public String getSettled() {
    return settled;
  }

  public void setSettled(String settled) {
    this.settled = settled;
  }

  public RightToWork settled(String settled) {
    this.settled = settled;
    return this;
  }

  public LocalDate getVisaIssued() {
    return visaIssued;
  }

  public void setVisaIssued(LocalDate visaIssued) {
    this.visaIssued = visaIssued;
  }

  public RightToWork visaIssued(LocalDate visaIssued) {
    this.visaIssued = visaIssued;
    return this;
  }

  public LocalDate getVisaValidTo() {
    return visaValidTo;
  }

  public void setVisaValidTo(LocalDate visaValidTo) {
    this.visaValidTo = visaValidTo;
  }

  public RightToWork visaValidTo(LocalDate visaValidTo) {
    this.visaValidTo = visaValidTo;
    return this;
  }

  public String getVisaDetails() {
    return visaDetails;
  }

  public void setVisaDetails(String visaDetails) {
    this.visaDetails = visaDetails;
  }

  public RightToWork visaDetails(String visaDetails) {
    this.visaDetails = visaDetails;
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

  @Override
  public String toString() {
    return "RightToWork{" +
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
