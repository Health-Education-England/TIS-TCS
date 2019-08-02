package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing RightToWork.
 */
public interface RightToWorkService {

  /**
   * Save a rightToWork.
   *
   * @param rightToWorkDTO the entity to save
   * @return the persisted entity
   */
  RightToWorkDTO save(RightToWorkDTO rightToWorkDTO);

  /**
   * Save a list of rightToWork
   *
   * @param rightToWorkDTOs the list of entities to save
   * @return a list of persisted entities
   */
  List<RightToWorkDTO> save(List<RightToWorkDTO> rightToWorkDTOs);

  /**
   * Get all the rightToWorks.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<RightToWorkDTO> findAll(Pageable pageable);

  /**
   * Get the "id" rightToWork.
   *
   * @param id the id of the entity
   * @return the entity
   */
  RightToWorkDTO findOne(Long id);

  /**
   * Delete the "id" rightToWork.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
