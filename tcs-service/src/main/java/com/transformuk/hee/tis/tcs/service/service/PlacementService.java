package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementViewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Placement.
 */
public interface PlacementService {

  /**
   * Save a placement.
   *
   * @param placementDTO the entity to save
   * @return the persisted entity
   */
  PlacementViewDTO save(PlacementDTO placementDTO);

  /**
   * Save a list of placements.
   *
   * @param placementDTO the list of entities to save
   * @return the list of persisted entities
   */
  List<PlacementViewDTO> save(List<PlacementDTO> placementDTO);

  /**
   * Get all the placements.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<PlacementViewDTO> findAll(Pageable pageable);

  /**
   * Get the "id" placement.
   *
   * @param id the id of the entity
   * @return the entity
   */
  PlacementViewDTO findOne(Long id);

  /**
   * Delete the "id" placement.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
