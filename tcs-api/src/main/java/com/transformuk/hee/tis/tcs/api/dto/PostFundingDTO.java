package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the PostFunding entity.
 */
public class PostFundingDTO implements Serializable {

  private Long id;

  private String intrepidId;

  private String fundingType;

  private String info;

  private LocalDate startDate;

  private LocalDate endDate;

  private String fundingBodyId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getIntrepidId() {
    return intrepidId;
  }

  public void setIntrepidId(String intrepidId) {
    this.intrepidId = intrepidId;
  }

  public String getFundingType() {
    return fundingType;
  }

  public void setFundingType(String fundingType) {
    this.fundingType = fundingType;
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public String getFundingBodyId() {
    return fundingBodyId;
  }

  public void setFundingBodyId(String fundingBodyId) {
    this.fundingBodyId = fundingBodyId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PostFundingDTO that = (PostFundingDTO) o;
    return Objects.equals(id, that.id) &&
      Objects.equals(intrepidId, that.intrepidId) &&
      Objects.equals(fundingType, that.fundingType) &&
      Objects.equals(info, that.info) &&
      Objects.equals(startDate, that.startDate) &&
      Objects.equals(endDate, that.endDate) &&
      Objects.equals(fundingBodyId, that.fundingBodyId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, intrepidId, fundingType, info, startDate, endDate, fundingBodyId);
  }

  @Override
  public String toString() {
    return "PostFundingDTO{" +
      "id=" + id +
      ", intrepidId='" + intrepidId + '\'' +
      ", fundingType='" + fundingType + '\'' +
      ", info='" + info + '\'' +
      ", startDate=" + startDate +
      ", endDate=" + endDate +
      ", fundingBodyId='" + fundingBodyId + '\'' +
      '}';
  }
}
