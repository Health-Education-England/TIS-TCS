package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.GmcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.GmcDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing GmcDetails.
 */
@Service
@Transactional
public class GmcDetailsServiceImpl implements GmcDetailsService {

  private final Logger log = LoggerFactory.getLogger(GmcDetailsServiceImpl.class);

  private final GmcDetailsRepository gmcDetailsRepository;

  private final GmcDetailsMapper gmcDetailsMapper;

  public GmcDetailsServiceImpl(GmcDetailsRepository gmcDetailsRepository, GmcDetailsMapper gmcDetailsMapper) {
    this.gmcDetailsRepository = gmcDetailsRepository;
    this.gmcDetailsMapper = gmcDetailsMapper;
  }

  /**
   * Save a gmcDetails.
   *
   * @param gmcDetailsDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public GmcDetailsDTO save(GmcDetailsDTO gmcDetailsDTO) {
    log.debug("Request to save GmcDetails : {}", gmcDetailsDTO);
    GmcDetails gmcDetails = gmcDetailsMapper.toEntity(gmcDetailsDTO);
    gmcDetails = gmcDetailsRepository.save(gmcDetails);
    return gmcDetailsMapper.toDto(gmcDetails);
  }

  /**
   * Get all the gmcDetails.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<GmcDetailsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all GmcDetails");
    return gmcDetailsRepository.findAll(pageable)
        .map(gmcDetailsMapper::toDto);
  }

  /**
   * Get one gmcDetails by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public GmcDetailsDTO findOne(Long id) {
    log.debug("Request to get GmcDetails : {}", id);
    GmcDetails gmcDetails = gmcDetailsRepository.findOne(id);
    return gmcDetailsMapper.toDto(gmcDetails);
  }

  /**
   * Delete the  gmcDetails by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete GmcDetails : {}", id);
    gmcDetailsRepository.delete(id);
  }
}
