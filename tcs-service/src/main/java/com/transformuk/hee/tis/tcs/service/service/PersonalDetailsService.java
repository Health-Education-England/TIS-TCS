package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing PersonalDetails.
 */
public interface PersonalDetailsService {

  /**
   * Save a personalDetails.
   *
   * @param personalDetailsDTO the entity to save
   * @return the persisted entity
   */
  PersonalDetailsDTO save(PersonalDetailsDTO personalDetailsDTO);

  /**
   * Save a list of personalDetails
   *
   * @param personalDetailsDTOs the list of entities to save
   * @return a list of persisted entities
   */
  List<PersonalDetailsDTO> save(List<PersonalDetailsDTO> personalDetailsDTOs);

  /**
   * Get all the personalDetails.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<PersonalDetailsDTO> findAll(Pageable pageable);

  /**
   * Get the "id" personalDetails.
   *
   * @param id the id of the entity
   * @return the entity
   */
  PersonalDetailsDTO findOne(Long id);

  /**
   * Delete the "id" personalDetails.
   *
   * @param id the id of the entity
   */
  void delete(Long id);

  /**
   * Update Person Details which are not null in incoming DTO.
   * @param personalDetailsDTO updated personalDetailsDTO
   * @return up to date object
   */
  Optional<PersonalDetailsDTO> patchUpdate(PersonalDetailsDTO personalDetailsDTO);
}
