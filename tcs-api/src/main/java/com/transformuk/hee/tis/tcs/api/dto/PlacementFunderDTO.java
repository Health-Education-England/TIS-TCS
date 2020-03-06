package com.transformuk.hee.tis.tcs.api.dto;

import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

/**
 * A DTO for the PlacementFunder entity.
 */
@Data
public class PlacementFunderDTO implements Serializable {

  private Long id;

  private String localOffice;

  private String trust;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlacementFunderDTO placementFunderDTO = (PlacementFunderDTO) o;

    if (!Objects.equals(id, placementFunderDTO.id)) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
