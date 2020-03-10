package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the TariffFundingTypeFields entity.
 */
@Data
public class TariffFundingTypeFieldsDTO implements Serializable {

  private Long id;

  private LocalDate effectiveDateFrom;

  private LocalDate effectiveDateTo;

  private BigDecimal tariffRate;

  private BigDecimal placementRate;

  private String levelOfPostId;

  private String placementRateFundedById;

  private String placementRateProvidedToId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO = (TariffFundingTypeFieldsDTO) o;

    if (!Objects.equals(id, tariffFundingTypeFieldsDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
