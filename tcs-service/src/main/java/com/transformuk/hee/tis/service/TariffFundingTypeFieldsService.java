package com.transformuk.hee.tis.service;

import com.transformuk.hee.tis.service.dto.TariffFundingTypeFieldsDTO;

import java.util.List;

/**
 * Service Interface for managing TariffFundingTypeFields.
 */
public interface TariffFundingTypeFieldsService {

	/**
	 * Save a tariffFundingTypeFields.
	 *
	 * @param tariffFundingTypeFieldsDTO the entity to save
	 * @return the persisted entity
	 */
	TariffFundingTypeFieldsDTO save(TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO);

	/**
	 * Get all the tariffFundingTypeFields.
	 *
	 * @return the list of entities
	 */
	List<TariffFundingTypeFieldsDTO> findAll();

	/**
	 * Get the "id" tariffFundingTypeFields.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	TariffFundingTypeFieldsDTO findOne(Long id);

	/**
	 * Delete the "id" tariffFundingTypeFields.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
