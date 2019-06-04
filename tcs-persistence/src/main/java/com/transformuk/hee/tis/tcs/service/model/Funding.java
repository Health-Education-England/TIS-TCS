package com.transformuk.hee.tis.tcs.service.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Funding.
 */
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

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Funding status(String status) {
    this.status = status;
    return this;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public Funding startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public Funding endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  public String getFundingType() {
    return fundingType;
  }

  public void setFundingType(String fundingType) {
    this.fundingType = fundingType;
  }

  public Funding fundingType(String fundingType) {
    this.fundingType = fundingType;
    return this;
  }

  public String getFundingIssue() {
    return fundingIssue;
  }

  public void setFundingIssue(String fundingIssue) {
    this.fundingIssue = fundingIssue;
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

  @Override
  public String toString() {
    return "Funding{" +
        "id=" + id +
        ", status='" + status + "'" +
        ", startDate='" + startDate + "'" +
        ", endDate='" + endDate + "'" +
        ", fundingType='" + fundingType + "'" +
        ", fundingIssue='" + fundingIssue + "'" +
        '}';
  }
}
