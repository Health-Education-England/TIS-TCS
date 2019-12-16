package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import java.io.Serializable;
import java.util.Objects;

public class PlacementSpecialtyDTO implements Serializable {

  private Long placementId;
  private Long specialtyId;
  private PostSpecialtyType placementSpecialtyType;
  private String specialtyName;

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

  public String getSpecialtyName() {
    return specialtyName;
  }

  public void setSpecialtyName(String specialtyName) {
    this.specialtyName = specialtyName;
  }

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

  @Override
  public String toString() {
    return "PlacementSpecialtyDTO{" +
        "placementId=" + placementId +
        ", specialtyId=" + specialtyId +
        ", placementSpecialtyType=" + placementSpecialtyType +
        ", specialtyName=" + specialtyName +
        '}';
  }
}
