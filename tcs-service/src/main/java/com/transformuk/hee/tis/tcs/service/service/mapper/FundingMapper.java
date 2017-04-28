package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.FundingDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity Funding and its DTO FundingDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FundingMapper {

	FundingDTO fundingToFundingDTO(Funding funding);

	List<FundingDTO> fundingsToFundingDTOs(List<Funding> fundings);

	Funding fundingDTOToFunding(FundingDTO fundingDTO);

	List<Funding> fundingDTOsToFundings(List<FundingDTO> fundingDTOs);

	/**
	 * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
	 * creating a new attribute to know if the entity has any relationship from some other entity
	 *
	 * @param id id of the entity
	 * @return the entity instance
	 */

	default Funding fundingFromId(Long id) {
		if (id == null) {
			return null;
		}
		Funding funding = new Funding();
		funding.setId(id);
		return funding;
	}


}
