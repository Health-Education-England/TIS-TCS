package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the TariffRate entity.
 */
@Data
public class TariffRateDTO implements Serializable {

  private Long id;

  private String gradeAbbreviation;

  private String tariffRate;

  private String tariffRateFringe;

  private String tariffRateLondon;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TariffRateDTO tariffRateDTO = (TariffRateDTO) o;

    if (!Objects.equals(id, tariffRateDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
