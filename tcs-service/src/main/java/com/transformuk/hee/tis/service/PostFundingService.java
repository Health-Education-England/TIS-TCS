package com.transformuk.hee.tis.service;

import com.transformuk.hee.tis.tcs.api.dto.PostFundingDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing PostFunding.
 */
public interface PostFundingService {

	/**
	 * Save a postFunding.
	 *
	 * @param postFundingDTO the entity to save
	 * @return the persisted entity
	 */
	PostFundingDTO save(PostFundingDTO postFundingDTO);

	/**
	 * Get all the postFundings.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<PostFundingDTO> findAll(Pageable pageable);

	/**
	 * Get the "id" postFunding.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	PostFundingDTO findOne(Long id);

	/**
	 * Delete the "id" postFunding.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
