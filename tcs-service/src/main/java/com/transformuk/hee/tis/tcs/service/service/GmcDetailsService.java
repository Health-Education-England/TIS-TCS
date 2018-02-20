package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

/**
 * Service Interface for managing GmcDetails.
 */
public interface GmcDetailsService {

  /**
   * Save a gmcDetails.
   *
   * @param gmcDetailsDTO the entity to save
   * @return the persisted entity
   */
  GmcDetailsDTO save(GmcDetailsDTO gmcDetailsDTO);

  /**
   * Save a list of gmcDetails
   *
   * @param gmcDetailsDTOs the list of entities to save
   * @return a list of persisted entities
   */
  List<GmcDetailsDTO> save(List<GmcDetailsDTO> gmcDetailsDTOs);

  /**
   * Get all the gmcDetails.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<GmcDetailsDTO> findAll(Pageable pageable);

  /**
   * Get a list of gmcDetails
   *
   * @param gmcIds the list of entities to retrieve
   * @return the list of entities
   */
  List<GmcDetailsDTO> findByIdIn(List<String> gmcIds);

  /**
   * Get the "id" gmcDetails.
   *
   * @param id the id of the entity
   * @return the entity
   */
  GmcDetailsDTO findOne(Long id);

  /**
   * Delete the "id" gmcDetails.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
