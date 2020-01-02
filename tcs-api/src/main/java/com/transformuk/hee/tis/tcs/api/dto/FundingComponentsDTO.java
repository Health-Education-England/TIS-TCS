package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the FundingComponents entity.
 */
@Data
public class FundingComponentsDTO implements Serializable {

  private Long id;

  private Integer percentage;

  private BigDecimal amount;

  private String fundingOrganisationId;

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
}
