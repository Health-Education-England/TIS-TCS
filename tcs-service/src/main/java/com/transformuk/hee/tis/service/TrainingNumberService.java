package com.transformuk.hee.tis.service;

import com.transformuk.hee.tis.service.dto.TrainingNumberDTO;

import java.util.List;

/**
 * Service Interface for managing TrainingNumber.
 */
public interface TrainingNumberService {

	/**
	 * Save a trainingNumber.
	 *
	 * @param trainingNumberDTO the entity to save
	 * @return the persisted entity
	 */
	TrainingNumberDTO save(TrainingNumberDTO trainingNumberDTO);

	/**
	 * Get all the trainingNumbers.
	 *
	 * @return the list of entities
	 */
	List<TrainingNumberDTO> findAll();

	/**
	 * Get the "id" trainingNumber.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	TrainingNumberDTO findOne(Long id);

	/**
	 * Delete the "id" trainingNumber.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
