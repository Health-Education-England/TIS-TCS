package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing GdcDetails.
 */
public interface GdcDetailsService {

  /**
   * Save a gdcDetails.
   *
   * @param gdcDetailsDTO the entity to save
   * @return the persisted entity
   */
  GdcDetailsDTO save(GdcDetailsDTO gdcDetailsDTO);

  /**
   * Save a list of gdcDetails
   *
   * @param gdcDetailsDTOs the list of entities to save
   * @return a list of persisted entities
   */
  List<GdcDetailsDTO> save(List<GdcDetailsDTO> gdcDetailsDTOs);

  /**
   * Get all the gdcDetails.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<GdcDetailsDTO> findAll(Pageable pageable);

  /**
   * Get a list of gdcDetails
   *
   * @param gdcIds the list of entities to retrieve
   * @return the list of entities
   */
  List<GdcDetailsDTO> findByIdIn(List<String> gdcIds);

  /**
   * Get the "id" gdcDetails.
   *
   * @param id the id of the entity
   * @return the entity
   */
  GdcDetailsDTO findOne(Long id);

  /**
   * Delete the "id" gdcDetails.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
