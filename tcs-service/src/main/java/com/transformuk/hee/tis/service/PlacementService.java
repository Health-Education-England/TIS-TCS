package com.transformuk.hee.tis.service;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Placement.
 */
public interface PlacementService {

	/**
	 * Save a placement.
	 *
	 * @param placementDTO the entity to save
	 * @return the persisted entity
	 */
	PlacementDTO save(PlacementDTO placementDTO);

	/**
	 * Get all the placements.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<PlacementDTO> findAll(Pageable pageable);

	/**
	 * Get the "id" placement.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	PlacementDTO findOne(Long id);

	/**
	 * Delete the "id" placement.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
