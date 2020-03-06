package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import java.io.Serializable;
import java.util.Objects;
import lombok.Data;

@Data
public class PlacementSpecialtyDTO implements Serializable {

  private Long placementId;
  private Long specialtyId;
  private PostSpecialtyType placementSpecialtyType;
  private String specialtyName;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlacementSpecialtyDTO that = (PlacementSpecialtyDTO) o;
    return Objects.equals(placementId, that.placementId) &&
        Objects.equals(specialtyId, that.specialtyId) &&
        Objects.equals(specialtyName, that.specialtyName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(placementId, specialtyId, specialtyName);
  }
}
