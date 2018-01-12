package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
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
  PlacementDTO save(PlacementDTO placementDTO);

  /**
   * Save a list of placements.
   *
   * @param placementDTO the list of entities to save
   * @return the list of persisted entities
   */
  List<PlacementDTO> save(List<PlacementDTO> placementDTO);

  /**
   * Get all the placements.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<PlacementDTO> findAll(Pageable pageable);

  /**
   * Get the "id" placement.
   *
   * @param id the id of the entity
   * @return the entity
   */
  PlacementDTO findOne(Long id);

  /**
   * Get the placement details given the placement ID
   *
   * @param id the placement ID
   * @return the placement details if found
   */
  PlacementDetailsDTO getDetails(Long id);


  /**
   * Saves a new placement
   *
   * @param placementDetailsDTO the placement details to save
   * @return the saved placement details
   */
  PlacementDetailsDTO saveDetails(PlacementDetailsDTO placementDetailsDTO);

  /**
   * Delete the "id" placement.
   *
   * @param id the id of the entity
   */
  void delete(Long id);

  /**
   * Patch a list of Placement specialties
   *
   * @param placementDTOList
   * @return
   */
  List<PlacementDTO> patchPlacementSpecialties(List<PlacementDTO> placementDTOList);

  /**
   * Patch a list of Placement clinical supervisors
   *
   * @param placementDTOList
   * @return
   */
  List<PlacementDTO> patchPlacementClinicalSupervisors(List<PlacementDTO> placementDTOList);

  /**
   * Get filtered  the placement details.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<PlacementDetailsDTO> findFilteredPlacements(String columnFilterJson, Pageable pageable) throws IOException;

  /**
   * Get all the placement details.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<PlacementDetailsDTO> findAllPlacementDetails(Pageable pageable);
}
