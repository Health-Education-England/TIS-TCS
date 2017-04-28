package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.TariffFundingTypeFieldsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity TariffFundingTypeFields and its DTO TariffFundingTypeFieldsDTO.
 */
@Mapper(componentModel = "spring", uses = {TariffRateMapper.class, PlacementFunderMapper.class,})
public interface TariffFundingTypeFieldsMapper {

	@Mapping(source = "levelOfPost.id", target = "levelOfPostId")
	@Mapping(source = "placementRateFundedBy.id", target = "placementRateFundedById")
	@Mapping(source = "placementRateProvidedTo.id", target = "placementRateProvidedToId")
    TariffFundingTypeFieldsDTO tariffFundingTypeFieldsToTariffFundingTypeFieldsDTO(TariffFundingTypeFields tariffFundingTypeFields);

	List<TariffFundingTypeFieldsDTO> tariffFundingTypeFieldsToTariffFundingTypeFieldsDTOs(List<TariffFundingTypeFields> tariffFundingTypeFields);

	@Mapping(source = "levelOfPostId", target = "levelOfPost")
	@Mapping(source = "placementRateFundedById", target = "placementRateFundedBy")
	@Mapping(source = "placementRateProvidedToId", target = "placementRateProvidedTo")
	TariffFundingTypeFields tariffFundingTypeFieldsDTOToTariffFundingTypeFields(TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO);

	List<TariffFundingTypeFields> tariffFundingTypeFieldsDTOsToTariffFundingTypeFields(List<TariffFundingTypeFieldsDTO> tariffFundingTypeFieldsDTOs);

	/**
	 * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
	 * creating a new attribute to know if the entity has any relationship from some other entity
	 *
	 * @param id id of the entity
	 * @return the entity instance
	 */

	default TariffFundingTypeFields tariffFundingTypeFieldsFromId(Long id) {
		if (id == null) {
			return null;
		}
		TariffFundingTypeFields tariffFundingTypeFields = new TariffFundingTypeFields();
		tariffFundingTypeFields.setId(id);
		return tariffFundingTypeFields;
	}


}
