package com.transformuk.hee.tis.service;

import com.transformuk.hee.tis.service.dto.PlacementFunderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PlacementFunder.
 */
public interface PlacementFunderService {

	/**
	 * Save a placementFunder.
	 *
	 * @param placementFunderDTO the entity to save
	 * @return the persisted entity
	 */
	PlacementFunderDTO save(PlacementFunderDTO placementFunderDTO);

	/**
	 * Get all the placementFunders.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<PlacementFunderDTO> findAll(Pageable pageable);

	/**
	 * Get the "id" placementFunder.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	PlacementFunderDTO findOne(Long id);

	/**
	 * Delete the "id" placementFunder.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
