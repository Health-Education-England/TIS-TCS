package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Specialty.
 */
public interface SpecialtyService {

	/**
	 * Save a specialty.
	 *
	 * @param specialtyDTO the entity to save
	 * @return the persisted entity
	 */
	SpecialtyDTO save(SpecialtyDTO specialtyDTO);

    /**
     * Save a list of specialties.
     *
     * @param specialtyDTO the entities to save
     * @return the list of persisted entities
     */
	List<SpecialtyDTO> save(List<SpecialtyDTO> specialtyDTO);

	/**
	 * Get all the specialties.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<SpecialtyDTO> findAll(Pageable pageable);

	/**
	 * Get the "id" specialty.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	SpecialtyDTO findOne(Long id);

	/**
	 * Delete the "id" specialty.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
