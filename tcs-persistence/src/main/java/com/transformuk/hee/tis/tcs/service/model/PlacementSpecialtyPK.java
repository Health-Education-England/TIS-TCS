package com.transformuk.hee.tis.tcs.service.model;

import java.io.Serializable;

/**
 * This is the hibernate representation of the composite primary key of Placement Specialty
 */
public class PlacementSpecialtyPK implements Serializable {

  private static final long serialVersionUID = 1L;

  private Placement placement;

  private Specialty specialty;


  public Placement getPlacement() {
    return placement;
  }

  public void setPlacement(Placement placement) {
    this.placement = placement;
  }

  public Specialty getSpecialty() {
    return specialty;
  }

  public void setSpecialty(Specialty specialty) {
    this.specialty = specialty;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlacementSpecialtyPK that = (PlacementSpecialtyPK) o;

    if (placement != null ? !placement.equals(that.placement) : that.placement != null) return false;
    return specialty != null ? specialty.equals(that.specialty) : that.specialty == null;
  }

  @Override
  public int hashCode() {
    int result = placement != null ? placement.hashCode() : 0;
    result = 31 * result + (specialty != null ? specialty.hashCode() : 0);
    return result;
  }
}