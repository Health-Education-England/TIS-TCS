package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementViewDTO;
import com.transformuk.hee.tis.tcs.service.model.PlacementView;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity PlacementView and its DTO PlacementViewDTO.
 * <p>
 * This mapper was created as mapstruct was having difficulty with some of the relationships within
 * placements It was having issues with the parent/child relationship between old/new placement
 * records causing stack overflows and causing NPE's when trying to traverse through joins outside
 * the JPA session.
 * <p>
 * This mapper gives more control over what details are converted
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlacementViewMapper {

  PlacementViewDTO placementViewToPlacementViewDTO(PlacementView placementView);

  List<PlacementViewDTO> placementViewsToPlacementViewDTOs(List<PlacementView> placementViews);

  PlacementView placementViewDTOToPlacementView(PlacementViewDTO placementViewDTO);

  List<PlacementView> placementViewDTOsToPlacementViews(List<PlacementViewDTO> placementViewDTOs);

}
