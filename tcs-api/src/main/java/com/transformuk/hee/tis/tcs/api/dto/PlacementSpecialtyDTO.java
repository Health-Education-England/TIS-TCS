package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;

import java.io.Serializable;

public class PlacementSpecialtyDTO implements Serializable {

  private Long id;
  private Long specialtyId;
  private PostSpecialtyType placementSpecialtyType;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getSpecialtyId() {
    return specialtyId;
  }

  public void setSpecialtyId(Long specialtyId) {
    this.specialtyId = specialtyId;
  }

  public PostSpecialtyType getPlacementSpecialtyType() {
    return placementSpecialtyType;
  }

  public void setPlacementSpecialtyType(PostSpecialtyType placementSpecialtyType) {
    this.placementSpecialtyType = placementSpecialtyType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlacementSpecialtyDTO that = (PlacementSpecialtyDTO) o;

    if (specialtyId != null ? !specialtyId.equals(that.specialtyId) : that.specialtyId != null) return false;
    return placementSpecialtyType == that.placementSpecialtyType;
  }

  @Override
  public int hashCode() {
    int result = 31 + (specialtyId != null ? specialtyId.hashCode() : 0);
    result = 31 * result + (placementSpecialtyType != null ? placementSpecialtyType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PostSpecialtyDTO{" +
        "id=" + id +
        ", specialtyId=" + specialtyId +
        ", placementSpecialtyType=" + placementSpecialtyType +
        '}';
  }
}
