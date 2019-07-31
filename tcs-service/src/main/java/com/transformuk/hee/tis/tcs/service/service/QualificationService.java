package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import java.util.List;
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
   * Save multiple qualifications.
   *
   * @param qualificationDTOs the entities to save
   * @return the persisted entities
   */
  List<QualificationDTO> save(List<QualificationDTO> qualificationDTOs);

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

  /**
   * find all qualifications related to the person
   *
   * @param personId id unique id for the person
   * @return a list of qualifications linked to the person
   */
  List<QualificationDTO> findPersonQualifications(Long personId);
}
