package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import java.util.List;
import java.util.Optional;
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
   * Bulk Save contactDetails.
   *
   * @param contactDetailsDTOs the entity to save
   * @return the persisted entity
   */
  List<ContactDetailsDTO> save(List<ContactDetailsDTO> contactDetailsDTOs);

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

  /**
   * Update Contact Details of a person which are not null in incoming DTO.
   *
   * @param contactDetailsDTO updated contactDetailsDTO object
   * @return up to date object
   */
  Optional<ContactDetailsDTO> patch(ContactDetailsDTO contactDetailsDTO);
}
