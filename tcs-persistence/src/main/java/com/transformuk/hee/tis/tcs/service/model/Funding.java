package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

/**
 * A Funding.
 */
@Data
@Entity
public class Funding implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "status")
  private String status;

  @Column(name = "startDate")
  private LocalDate startDate;

  @Column(name = "endDate")
  private LocalDate endDate;

  @Column(name = "fundingType")
  private String fundingType;

  @Column(name = "fundingIssue")
  private String fundingIssue;

  public Funding status(String status) {
    this.status = status;
    return this;
  }

  public Funding startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  public Funding endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  public Funding fundingType(String fundingType) {
    this.fundingType = fundingType;
    return this;
  }

  public Funding fundingIssue(String fundingIssue) {
    this.fundingIssue = fundingIssue;
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
    Funding funding = (Funding) o;
    if (funding.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, funding.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
