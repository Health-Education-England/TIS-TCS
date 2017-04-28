package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.FundingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Funding.
 */
public interface FundingService {

	/**
	 * Save a funding.
	 *
	 * @param fundingDTO the entity to save
	 * @return the persisted entity
	 */
	FundingDTO save(FundingDTO fundingDTO);

	/**
	 * Get all the fundings.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<FundingDTO> findAll(Pageable pageable);

	/**
	 * Get the "id" funding.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	FundingDTO findOne(Long id);

	/**
	 * Delete the "id" funding.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
