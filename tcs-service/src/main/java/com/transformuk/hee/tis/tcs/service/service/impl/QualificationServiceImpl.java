package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.service.model.Qualification;
import com.transformuk.hee.tis.tcs.service.repository.QualificationRepository;
import com.transformuk.hee.tis.tcs.service.service.QualificationService;
import com.transformuk.hee.tis.tcs.service.service.mapper.QualificationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing Qualification.
 */
@Service
@Transactional
public class QualificationServiceImpl implements QualificationService {

  private final Logger log = LoggerFactory.getLogger(QualificationServiceImpl.class);

  private final QualificationRepository qualificationRepository;

  private final QualificationMapper qualificationMapper;

  public QualificationServiceImpl(QualificationRepository qualificationRepository, QualificationMapper qualificationMapper) {
    this.qualificationRepository = qualificationRepository;
    this.qualificationMapper = qualificationMapper;
  }

  /**
   * Save a qualification.
   *
   * @param qualificationDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public QualificationDTO save(QualificationDTO qualificationDTO) {
    log.debug("Request to save Qualification : {}", qualificationDTO);
    Qualification qualification = qualificationMapper.toEntity(qualificationDTO);
    qualification = qualificationRepository.save(qualification);
    return qualificationMapper.toDto(qualification);
  }

  /**
   * Save a list of qualifications.
   *
   * @param qualificationDTOs the entities to save
   * @return the persisted entities
   */
  @Override
  public List<QualificationDTO> save(List<QualificationDTO> qualificationDTOs) {
    log.debug("Request to save Qualifications : {}", qualificationDTOs);
    List<Qualification> qualifications = qualificationMapper.toEntities(qualificationDTOs);
    qualifications = qualificationRepository.save(qualifications);
    return qualificationMapper.toDTOs(qualifications);
  }

  /**
   * Get all the qualifications.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<QualificationDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Qualifications");
    return qualificationRepository.findAll(pageable)
        .map(qualificationMapper::toDto);
  }

  /**
   * Get one qualification by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public QualificationDTO findOne(Long id) {
    log.debug("Request to get Qualification : {}", id);
    Qualification qualification = qualificationRepository.findOne(id);
    return qualificationMapper.toDto(qualification);
  }

  /**
   * Delete the  qualification by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Qualification : {}", id);
    qualificationRepository.delete(id);
  }
}
