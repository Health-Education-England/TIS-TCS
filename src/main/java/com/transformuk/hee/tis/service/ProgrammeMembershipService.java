package com.transformuk.hee.tis.service;

import com.transformuk.hee.tis.service.dto.ProgrammeMembershipDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ProgrammeMembership.
 */
public interface ProgrammeMembershipService {

	/**
	 * Save a programmeMembership.
	 *
	 * @param programmeMembershipDTO the entity to save
	 * @return the persisted entity
	 */
	ProgrammeMembershipDTO save(ProgrammeMembershipDTO programmeMembershipDTO);

	/**
	 * Get all the programmeMemberships.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	Page<ProgrammeMembershipDTO> findAll(Pageable pageable);

	/**
	 * Get the "id" programmeMembership.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	ProgrammeMembershipDTO findOne(Long id);

	/**
	 * Delete the "id" programmeMembership.
	 *
	 * @param id the id of the entity
	 */
	void delete(Long id);
}
