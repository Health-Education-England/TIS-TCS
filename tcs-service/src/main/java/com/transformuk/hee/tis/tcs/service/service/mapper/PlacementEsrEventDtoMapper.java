package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementEsrEventDto;
import com.transformuk.hee.tis.tcs.service.model.PlacementEsrEvent;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PlacementEsrEventDtoMapper {

  @Mapping(target = "eventDateTime", source = "exportedAt")
  PlacementEsrEvent placementEsrEventDtoToPlacementEsrEvent(
      PlacementEsrEventDto placementEsrExportedDto);

  @Mapping(target = "exportedAt", source = "eventDateTime")
  PlacementEsrEventDto placementEsrEventToPlacementEsrEventDto(
      PlacementEsrEvent placementEsrEvent);

  Set<PlacementEsrEventDto> placementEsrEventSetToPlacementEsrEventDtoSet(
      Set<PlacementEsrEvent> placementEsrEventSet);
}
