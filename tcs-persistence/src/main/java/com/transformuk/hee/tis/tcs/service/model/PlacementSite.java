package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.PlacementSiteType;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "PlacementSite")
public class PlacementSite implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @ManyToOne(targetEntity = PlacementDetails.class, fetch = FetchType.LAZY)
  @JoinColumn(name = "placementId")
  private PlacementDetails placement;// eta hobe PlacementDetails

  @JoinColumn(name = "siteId")
  private Long siteId;

  @Enumerated(EnumType.STRING)
  @Column(name = "placementSiteType")
  private PlacementSiteType placementSiteType;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof PlacementSite)) {
      return false;
    }
    PlacementSite that = (PlacementSite) o;
    return Objects.equals(getId(), that.getId()) &&
        Objects.equals(getPlacement(), that.getPlacement()) &&
        Objects.equals(getSiteId(), that.getSiteId()) &&
        getPlacementSiteType() == that.getPlacementSiteType();
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getPlacement(), getSiteId(), getPlacementSiteType());
  }
}
