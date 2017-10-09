package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeMembershipRepository;
import com.transformuk.hee.tis.tcs.service.service.ProgrammeMembershipService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ProgrammeMembershipMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing ProgrammeMembership.
 */
@Service
@Transactional
public class ProgrammeMembershipServiceImpl implements ProgrammeMembershipService {

  private final Logger log = LoggerFactory.getLogger(ProgrammeMembershipServiceImpl.class);

  private final ProgrammeMembershipRepository programmeMembershipRepository;

  private final ProgrammeMembershipMapper programmeMembershipMapper;

  public ProgrammeMembershipServiceImpl(ProgrammeMembershipRepository programmeMembershipRepository, ProgrammeMembershipMapper programmeMembershipMapper) {
    this.programmeMembershipRepository = programmeMembershipRepository;
    this.programmeMembershipMapper = programmeMembershipMapper;
  }

  /**
   * Save a programmeMembership.
   *
   * @param programmeMembershipDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public ProgrammeMembershipDTO save(ProgrammeMembershipDTO programmeMembershipDTO) {
    log.debug("Request to save ProgrammeMembership : {}", programmeMembershipDTO);
    ProgrammeMembership programmeMembership = programmeMembershipMapper.toEntity(programmeMembershipDTO);
    programmeMembership = programmeMembershipRepository.saveAndFlush(programmeMembership);
    ProgrammeMembershipDTO result = programmeMembershipMapper.toDto(programmeMembership);
    return result;
  }

  /**
   * Save a list of programmeMembership.
   *
   * @param programmeMembershipDTO the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<ProgrammeMembershipDTO> save(List<ProgrammeMembershipDTO> programmeMembershipDTO) {
    log.debug("Request to save ProgrammeMembership : {}", programmeMembershipDTO);
    List<ProgrammeMembership> programmeMembership = programmeMembershipMapper.programmeMembershipDTOsToProgrammeMemberships(programmeMembershipDTO);
    programmeMembership = programmeMembershipRepository.save(programmeMembership);
    List<ProgrammeMembershipDTO> result = programmeMembershipMapper.programmeMembershipsToProgrammeMembershipDTOs(programmeMembership);
    return result;
  }

  /**
   * Get all the programmeMemberships.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<ProgrammeMembershipDTO> findAll(Pageable pageable) {
    log.debug("Request to get all ProgrammeMemberships");
    Page<ProgrammeMembership> result = programmeMembershipRepository.findAll(pageable);
    return result.map(programmeMembership -> programmeMembershipMapper.toDto(programmeMembership));
  }

  /**
   * Get one programmeMembership by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public ProgrammeMembershipDTO findOne(Long id) {
    log.debug("Request to get ProgrammeMembership : {}", id);
    ProgrammeMembership programmeMembership = programmeMembershipRepository.findOne(id);
    ProgrammeMembershipDTO programmeMembershipDTO = programmeMembershipMapper.toDto(programmeMembership);
    return programmeMembershipDTO;
  }

  /**
   * Delete the  programmeMembership by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete ProgrammeMembership : {}", id);
    programmeMembershipRepository.delete(id);
  }
}
