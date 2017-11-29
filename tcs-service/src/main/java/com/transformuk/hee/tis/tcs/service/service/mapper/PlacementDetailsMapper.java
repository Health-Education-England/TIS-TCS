package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity PlacementDetails and its DTO PlacementDetailsDTO.
 *
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlacementDetailsMapper {

  PlacementDetailsDTO placementDetailsToPlacementDetailsDTO(PlacementDetails placementDetails);

  List<PlacementDetailsDTO> placementDetailsToPlacementDetailDTOs(List<PlacementDetails> placementDetails);

  PlacementDetails placementDetailsDTOToPlacementDetails(PlacementDetailsDTO placementDetailsDTO);

  List<PlacementDetails> placementDetailsDTOsToPlacementDetailss(List<PlacementDetailsDTO> placementDetailsDTOs);

}
