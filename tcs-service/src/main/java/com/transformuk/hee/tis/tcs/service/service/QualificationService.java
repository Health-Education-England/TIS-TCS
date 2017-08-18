package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Qualification.
 */
public interface QualificationService {

  /**
   * Save a qualification.
   *
   * @param qualificationDTO the entity to save
   * @return the persisted entity
   */
  QualificationDTO save(QualificationDTO qualificationDTO);

  /**
   * Get all the qualifications.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<QualificationDTO> findAll(Pageable pageable);

  /**
   * Get the "id" qualification.
   *
   * @param id the id of the entity
   * @return the entity
   */
  QualificationDTO findOne(Long id);

  /**
   * Delete the "id" qualification.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
