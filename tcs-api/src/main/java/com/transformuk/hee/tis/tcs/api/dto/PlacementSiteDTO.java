package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;

import java.io.Serializable;
import java.util.Objects;

public class PlacementSiteDTO implements Serializable {

  private Long id;
  private Long placementId;
  private Long siteId;
  private PostSiteType placementSiteType;

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

  public PostSiteType getPlacementSiteType() {
    return placementSiteType;
  }

  public void setPlacementSiteType(PostSiteType placementSiteType) {
    this.placementSiteType = placementSiteType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof PlacementSiteDTO)) return false;
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
