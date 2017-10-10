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
 * A PostFunding.
 */
@Entity
public class PostFunding implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "fundingId")
  private String fundingId;

  @Column(name = "fundingComponentsId")
  private String fundingComponentsId;

  @Column(name = "startDate")
  private LocalDate startDate;

  @Column(name = "endDate")
  private LocalDate endDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getFundingId() {
    return fundingId;
  }

  public void setFundingId(String fundingId) {
    this.fundingId = fundingId;
  }

  public String getFundingComponentsId() {
    return fundingComponentsId;
  }

  public void setFundingComponentsId(String fundingComponentsId) {
    this.fundingComponentsId = fundingComponentsId;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostFunding postFunding = (PostFunding) o;
    if (postFunding.id == null || id == null) {
      return false;
    }
    return Objects.equals(id, postFunding.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "PostFunding{" +
        "id=" + id +
        '}';
  }
}
