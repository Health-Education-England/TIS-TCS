package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementSiteDTO;
import com.transformuk.hee.tis.tcs.service.model.PlacementSite;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlacementSiteMapper {

  PlacementSite toEntity(PlacementSiteDTO dto);

  @Mapping(target = "placementId", source = "placement.id")
  PlacementSiteDTO toDto(PlacementSite placementSite);
}
