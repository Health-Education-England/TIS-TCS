package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;

import java.io.Serializable;

public class PlacementSpecialtyDTO implements Serializable {

  private Long placementId;
  private Long specialtyId;
  private PostSpecialtyType placementSpecialtyType;

  public Long getPlacementId() {
    return placementId;
  }

  public void setPlacementId(Long placementId) {
    this.placementId = placementId;
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

    if (placementId != null ? !placementId.equals(that.placementId) : that.placementId != null) return false;
    if (specialtyId != null ? !specialtyId.equals(that.specialtyId) : that.specialtyId != null) return false;
    return placementSpecialtyType == that.placementSpecialtyType;
  }

  @Override
  public int hashCode() {
    int result = placementId != null ? placementId.hashCode() : 0;
    result = 31 * result + (specialtyId != null ? specialtyId.hashCode() : 0);
    result = 31 * result + (placementSpecialtyType != null ? placementSpecialtyType.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PlacementSpecialtyDTO{" +
        "placementId=" + placementId +
        ", specialtyId=" + specialtyId +
        ", placementSpecialtyType=" + placementSpecialtyType +
        '}';
  }
}
