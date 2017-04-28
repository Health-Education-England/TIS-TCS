package com.transformuk.hee.tis.service;

import com.transformuk.hee.tis.service.dto.CurriculumDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Curriculum.
 */
public interface CurriculumService {

	/**
	 * Save a curriculum.
	 *
	 * @param curriculumDTO the entity to save
	 * @return the persisted entity
	 */
	CurriculumDTO save(CurriculumDTO curriculumDTO);

	/**
	 * Get all the curricula.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<CurriculumDTO> findAll(Pageable pageable);

	/**
	 * Get the "id" curriculum.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	CurriculumDTO findOne(Long id);

	/**
	 * Delete the "id" curriculum.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
