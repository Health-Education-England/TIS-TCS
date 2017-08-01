package com.transformuk.hee.tis.tcs.api.dto;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the FundingComponents entity.
 */
public class FundingComponentsDTO implements Serializable {

  private Long id;

  private Integer percentage;

  private BigDecimal amount;

  private String fundingOrganisationId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getPercentage() {
    return percentage;
  }

  public void setPercentage(Integer percentage) {
    this.percentage = percentage;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public String getFundingOrganisationId() {
    return fundingOrganisationId;
  }

  public void setFundingOrganisationId(String placementFunderId) {
    this.fundingOrganisationId = placementFunderId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FundingComponentsDTO fundingComponentsDTO = (FundingComponentsDTO) o;

    if (!Objects.equals(id, fundingComponentsDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }

  @Override
  public String toString() {
    return "FundingComponentsDTO{" +
        "id=" + id +
        ", percentage='" + percentage + "'" +
        ", amount='" + amount + "'" +
        '}';
  }
}
