package com.transformuk.hee.tis.service;

import com.transformuk.hee.tis.service.dto.ProgrammeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Programme.
 */
public interface ProgrammeService {

	/**
	 * Save a programme.
	 *
	 * @param programmeDTO the entity to save
	 * @return the persisted entity
	 */
	ProgrammeDTO save(ProgrammeDTO programmeDTO);

	/**
	 * Get all the programmes.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<ProgrammeDTO> findAll(Pageable pageable);

	/**
	 * Get the "id" programme.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	ProgrammeDTO findOne(Long id);

	/**
	 * Delete the "id" programme.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
