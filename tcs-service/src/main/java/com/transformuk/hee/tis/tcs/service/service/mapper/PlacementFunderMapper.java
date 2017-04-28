package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PlacementFunderDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity PlacementFunder and its DTO PlacementFunderDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlacementFunderMapper {

	PlacementFunderDTO placementFunderToPlacementFunderDTO(PlacementFunder placementFunder);

	List<PlacementFunderDTO> placementFundersToPlacementFunderDTOs(List<PlacementFunder> placementFunders);

	PlacementFunder placementFunderDTOToPlacementFunder(PlacementFunderDTO placementFunderDTO);

	List<PlacementFunder> placementFunderDTOsToPlacementFunders(List<PlacementFunderDTO> placementFunderDTOs);

	/**
	 * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
	 * creating a new attribute to know if the entity has any relationship from some other entity
	 *
	 * @param id id of the entity
	 * @return the entity instance
	 */

	default PlacementFunder placementFunderFromId(Long id) {
		if (id == null) {
			return null;
		}
		PlacementFunder placementFunder = new PlacementFunder();
		placementFunder.setId(id);
		return placementFunder;
	}


}
