package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementEsrExportedDto;
import com.transformuk.hee.tis.tcs.service.model.PlacementEsrEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlacementEsrExportedDtoMapper {

  @Mapping(target = "eventDateTime", source = "exportedAt")
  PlacementEsrEvent placementEsrExportedDtoToPlacementEsrEvent(
      PlacementEsrExportedDto placementEsrExportedDto);

}
