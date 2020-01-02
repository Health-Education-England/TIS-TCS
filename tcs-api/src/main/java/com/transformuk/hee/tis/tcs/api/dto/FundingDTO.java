package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the Funding entity.
 */
@Data
public class FundingDTO implements Serializable {

  private Long id;

  private String status;

  private LocalDate startDate;

  private LocalDate endDate;

  private String fundingType;

  private String fundingIssue;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FundingDTO fundingDTO = (FundingDTO) o;

    if (!Objects.equals(id, fundingDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
