package com.transformuk.hee.tis.service;

import com.transformuk.hee.tis.service.dto.TariffRateDTO;

import java.util.List;

/**
 * Service Interface for managing TariffRate.
 */
public interface TariffRateService {

	/**
	 * Save a tariffRate.
	 *
	 * @param tariffRateDTO the entity to save
	 * @return the persisted entity
	 */
	TariffRateDTO save(TariffRateDTO tariffRateDTO);

	/**
	 * Get all the tariffRates.
	 *
	 * @return the list of entities
	 */
	List<TariffRateDTO> findAll();

	/**
	 * Get the "id" tariffRate.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	TariffRateDTO findOne(Long id);

	/**
	 * Delete the "id" tariffRate.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
