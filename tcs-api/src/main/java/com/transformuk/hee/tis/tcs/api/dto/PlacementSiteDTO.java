package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementSiteType;
import java.io.Serializable;
import java.util.Objects;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlacementSiteDTO implements Serializable {

  private Long id;
  private Long placementId;
  private Long siteId;
  private PlacementSiteType placementSiteType;

  public PlacementSiteDTO(Long placementId, Long siteId, PlacementSiteType placementSiteType) {
    this.placementId = placementId;
    this.siteId = siteId;
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
}
