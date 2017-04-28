package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.FundingComponentsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity FundingComponents and its DTO FundingComponentsDTO.
 */
@Mapper(componentModel = "spring", uses = {PlacementFunderMapper.class,})
public interface FundingComponentsMapper {

	@Mapping(source = "fundingOrganisation.id", target = "fundingOrganisationId")
    FundingComponentsDTO fundingComponentsToFundingComponentsDTO(FundingComponents fundingComponents);

	List<FundingComponentsDTO> fundingComponentsToFundingComponentsDTOs(List<FundingComponents> fundingComponents);

	@Mapping(source = "fundingOrganisationId", target = "fundingOrganisation")
	FundingComponents fundingComponentsDTOToFundingComponents(FundingComponentsDTO fundingComponentsDTO);

	List<FundingComponents> fundingComponentsDTOsToFundingComponents(List<FundingComponentsDTO> fundingComponentsDTOs);

	/**
	 * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
	 * creating a new attribute to know if the entity has any relationship from some other entity
	 *
	 * @param id id of the entity
	 * @return the entity instance
	 */

	default FundingComponents fundingComponentsFromId(Long id) {
		if (id == null) {
			return null;
		}
		FundingComponents fundingComponents = new FundingComponents();
		fundingComponents.setId(id);
		return fundingComponents;
	}


}
