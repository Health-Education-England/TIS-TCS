package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;
import lombok.Data;

/**
 * This is the hibernate representation of the composite primary key of Placement Specialty
 */
@Data
public class PlacementSpecialtyPK implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long placement;

  private Long specialty;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    PlacementSpecialtyPK that = (PlacementSpecialtyPK) o;

    if (placement != null ? !placement.equals(that.placement) : that.placement != null) {
      return false;
    }
    return specialty != null ? specialty.equals(that.specialty) : that.specialty == null;
  }

  @Override
  public int hashCode() {
    int result = placement != null ? placement.hashCode() : 0;
    result = 31 * result + (specialty != null ? specialty.hashCode() : 0);
    return result;
  }
}
