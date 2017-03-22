package com.transformuk.hee.tis.service;

import com.transformuk.hee.tis.service.dto.SpecialtyGroupDTO;

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
