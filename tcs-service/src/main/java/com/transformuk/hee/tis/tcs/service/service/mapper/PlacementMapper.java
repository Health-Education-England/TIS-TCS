package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Placement and its DTO PlacementDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlacementMapper {

  PlacementDTO placementToPlacementDTO(Placement placement);

  List<PlacementDTO> placementsToPlacementDTOs(List<Placement> placements);

  Placement placementDTOToPlacement(PlacementDTO placementDTO);

  List<Placement> placementDTOsToPlacements(List<PlacementDTO> placementDTOs);

  /**
   * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
   * creating a new attribute to know if the entity has any relationship from some other entity
   *
   * @param id id of the entity
   * @return the entity instance
   */

  default Placement placementFromId(Long id) {
    if (id == null) {
      return null;
    }
    Placement placement = new Placement();
    placement.setId(id);
    return placement;
  }


}
