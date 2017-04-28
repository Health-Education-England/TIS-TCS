package com.transformuk.hee.tis.service.mapper;

import com.transformuk.hee.tis.domain.*;
import com.transformuk.hee.tis.tcs.api.dto.TariffRateDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity TariffRate and its DTO TariffRateDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TariffRateMapper {

	TariffRateDTO tariffRateToTariffRateDTO(TariffRate tariffRate);

	List<TariffRateDTO> tariffRatesToTariffRateDTOs(List<TariffRate> tariffRates);

	TariffRate tariffRateDTOToTariffRate(TariffRateDTO tariffRateDTO);

	List<TariffRate> tariffRateDTOsToTariffRates(List<TariffRateDTO> tariffRateDTOs);

	/**
	 * generating the fromId for all mappers if the databaseType is sql, as the class has relationship to it might need it, instead of
	 * creating a new attribute to know if the entity has any relationship from some other entity
	 *
	 * @param id id of the entity
	 * @return the entity instance
	 */

	default TariffRate tariffRateFromId(Long id) {
		if (id == null) {
			return null;
		}
		TariffRate tariffRate = new TariffRate();
		tariffRate.setId(id);
		return tariffRate;
	}


}
