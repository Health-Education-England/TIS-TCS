package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementSiteType;
import java.io.Serializable;
import java.util.Objects;

public class PlacementSiteDTO implements Serializable {

  private Long id;
  private Long placementId;
  private Long siteId;
  private PlacementSiteType placementSiteType;

  public PlacementSiteDTO() {
  }

  public PlacementSiteDTO(Long placementId, Long siteId, PlacementSiteType placementSiteType) {
    this.placementId = placementId;
    this.siteId = siteId;
    this.placementSiteType = placementSiteType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPlacementId() {
    return placementId;
  }

  public void setPlacementId(Long placementId) {
    this.placementId = placementId;
  }

  public Long getSiteId() {
    return siteId;
  }

  public void setSiteId(Long siteId) {
    this.siteId = siteId;
  }

  public PlacementSiteType getPlacementSiteType() {
    return placementSiteType;
  }

  public void setPlacementSiteType(PlacementSiteType placementSiteType) {
    this.placementSiteType = placementSiteType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlacementSiteDTO)) {
      return false;
    }
    PlacementSiteDTO that = (PlacementSiteDTO) o;
    return Objects.equals(getPlacementId(), that.getPlacementId()) &&
        Objects.equals(getSiteId(), that.getSiteId()) &&
        getPlacementSiteType() == that.getPlacementSiteType();
  }

  @Override
  public int hashCode() {

    return Objects.hash(getPlacementId(), getSiteId(), getPlacementSiteType());
  }

  @Override
  public String toString() {
    return "PlacementSiteDTO{" +
        "placementId=" + placementId +
        ", siteId=" + siteId +
        ", placementSiteType=" + placementSiteType +
        '}';
  }
}
