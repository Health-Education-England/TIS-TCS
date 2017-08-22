package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing ContactDetails.
 */
public interface ContactDetailsService {

  /**
   * Save a contactDetails.
   *
   * @param contactDetailsDTO the entity to save
   * @return the persisted entity
   */
  ContactDetailsDTO save(ContactDetailsDTO contactDetailsDTO);

  /**
   * Get all the contactDetails.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<ContactDetailsDTO> findAll(Pageable pageable);

  /**
   * Get the "id" contactDetails.
   *
   * @param id the id of the entity
   * @return the entity
   */
  ContactDetailsDTO findOne(Long id);

  /**
   * Delete the "id" contactDetails.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
