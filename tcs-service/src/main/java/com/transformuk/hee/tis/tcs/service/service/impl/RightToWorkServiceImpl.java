package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import com.transformuk.hee.tis.tcs.service.repository.RightToWorkRepository;
import com.transformuk.hee.tis.tcs.service.service.RightToWorkService;
import com.transformuk.hee.tis.tcs.service.service.mapper.RightToWorkMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing RightToWork.
 */
@Service
@Transactional
public class RightToWorkServiceImpl implements RightToWorkService {

  private final Logger log = LoggerFactory.getLogger(RightToWorkServiceImpl.class);

  private final RightToWorkRepository rightToWorkRepository;

  private final RightToWorkMapper rightToWorkMapper;

  public RightToWorkServiceImpl(RightToWorkRepository rightToWorkRepository, RightToWorkMapper rightToWorkMapper) {
    this.rightToWorkRepository = rightToWorkRepository;
    this.rightToWorkMapper = rightToWorkMapper;
  }

  /**
   * Save a rightToWork.
   *
   * @param rightToWorkDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public RightToWorkDTO save(RightToWorkDTO rightToWorkDTO) {
    log.debug("Request to save RightToWork : {}", rightToWorkDTO);
    RightToWork rightToWork = rightToWorkMapper.toEntity(rightToWorkDTO);
    rightToWork = rightToWorkRepository.saveAndFlush(rightToWork);
    return rightToWorkMapper.toDto(rightToWork);
  }

  /**
   * Save a list of rightToWork
   *
   * @param rightToWorkDTOs the list of entities to save
   * @return a list of persisted entities
   */
  @Override
  public List<RightToWorkDTO> save(List<RightToWorkDTO> rightToWorkDTOs) {
    log.debug("Request to save RightToWork : {}", rightToWorkDTOs);
    List<RightToWork> rightToWorks = rightToWorkMapper.toEntity(rightToWorkDTOs);
    rightToWorks = rightToWorkRepository.save(rightToWorks);
    return rightToWorkMapper.toDto(rightToWorks);
  }

  /**
   * Get all the rightToWorks.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<RightToWorkDTO> findAll(Pageable pageable) {
    log.debug("Request to get all RightToWorks");
    return rightToWorkRepository.findAll(pageable)
        .map(rightToWorkMapper::toDto);
  }

  /**
   * Get one rightToWork by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public RightToWorkDTO findOne(Long id) {
    log.debug("Request to get RightToWork : {}", id);
    RightToWork rightToWork = rightToWorkRepository.findOne(id);
    return rightToWorkMapper.toDto(rightToWork);
  }

  /**
   * Delete the  rightToWork by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete RightToWork : {}", id);
    rightToWorkRepository.delete(id);
  }
}
