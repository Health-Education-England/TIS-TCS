package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.FundingComponentsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing FundingComponents.
 */
public interface FundingComponentsService {

	/**
	 * Save a fundingComponents.
	 *
	 * @param fundingComponentsDTO the entity to save
	 * @return the persisted entity
	 */
	FundingComponentsDTO save(FundingComponentsDTO fundingComponentsDTO);

    /**
     * Save a list of fundingComponents
     * @param fundingComponentsDTO the list of entities to save
     * @return a list of persisted entities
     */
    List<FundingComponentsDTO> save(List<FundingComponentsDTO> fundingComponentsDTO);

    /**
	 * Get all the fundingComponents.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<FundingComponentsDTO> findAll(Pageable pageable);

	/**
	 * Get the "id" fundingComponents.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	FundingComponentsDTO findOne(Long id);

	/**
	 * Delete the "id" fundingComponents.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
