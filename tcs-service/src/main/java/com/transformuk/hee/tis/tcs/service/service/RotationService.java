package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Rotation.
 */
public interface RotationService {

  /**
   * Save a rotation.
   *
   * @param rotationDTO the entity to save
   * @return the persisted entity
   */
  RotationDTO save(RotationDTO rotationDTO);

  /**
   * Get all the rotations.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<RotationDTO> findAll(Pageable pageable);

  /**
   * Get the "id" rotation.
   *
   * @param id the id of the entity
   * @return the entity
   */
  RotationDTO findOne(Long id);

  Page<RotationDTO> advancedSearchBySpecification(String searchQuery,
      List<ColumnFilter> columnFilters, Pageable pageable);

  boolean rotationExists(Long id, Long programmeId);

  /**
   * Delete the "id" rotation.
   *
   * @param id the id of the entity
   */
  void delete(Long id);

  List<RotationDTO> findRotationsByNameAndProgrammeId(String name, Long programmeId);
}
