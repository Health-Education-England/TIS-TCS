package com.transformuk.hee.tis.tcs.service.service;


import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;

import java.util.List;

/**
 * Service Interface for managing SpecialtyGroup.
 */
public interface SpecialtyGroupService {

	/**
	 * Save a specialtyGroup.
	 *
	 * @param specialtyGroupDTO the entity to save
	 * @return the persisted entity
	 */
	SpecialtyGroupDTO save(SpecialtyGroupDTO specialtyGroupDTO);

    /**
     * Save a list of specialtyGroup.
     *
     * @param specialtyGroupDTO the entities to save
     * @return the list of persisted entities
     */
	List<SpecialtyGroupDTO> save(List<SpecialtyGroupDTO> specialtyGroupDTO);

	/**
	 * Get all the specialtyGroups.
	 *
	 * @return the list of entities
	 */
	List<SpecialtyGroupDTO> findAll();

	/**
	 * Get the "id" specialtyGroup.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	SpecialtyGroupDTO findOne(Long id);

	/**
	 * Delete the "id" specialtyGroup.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
