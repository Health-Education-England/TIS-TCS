package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementSiteDTO;
import com.transformuk.hee.tis.tcs.service.model.PlacementSite;
import org.springframework.stereotype.Component;

@Component
public class PlacementSiteMapper {

  public PlacementSite toEntity(PlacementSiteDTO dto) {
    PlacementSite placementSite = new PlacementSite();
    placementSite.setId(dto.getId());
    placementSite.setSiteId(dto.getSiteId());
    placementSite.setPlacementSiteType(dto.getPlacementSiteType());
    placementSite.setPlacement(placementSite.getPlacement());
    return placementSite;
  }

  public PlacementSiteDTO toDto(PlacementSite placementSite) {
    PlacementSiteDTO placementSiteDTO = new PlacementSiteDTO();
    placementSiteDTO.setId(placementSite.getId());
    //placementSiteDTO.setPlacementId(placementSite.getPlacement().getId());
    placementSiteDTO.setSiteId(placementSite.getSiteId());
    placementSiteDTO.setPlacementSiteType(placementSite.getPlacementSiteType());
    return placementSiteDTO;
  }
}
