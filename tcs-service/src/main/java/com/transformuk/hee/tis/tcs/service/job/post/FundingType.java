package com.transformuk.hee.tis.tcs.service.job.post;

import java.util.Objects;

public class FundingType {
  private String fundingType;

  public FundingType(String fundingType) {
    this.fundingType = fundingType;
  }

  public FundingType() {
  }

  public String getFundingType() {
    return fundingType;
  }

  public void setFundingType(String fundingType) {
    this.fundingType = fundingType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FundingType that = (FundingType) o;
    return Objects.equals(fundingType, that.fundingType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fundingType);
  }
}
