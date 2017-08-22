package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.GdcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.GdcDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing GdcDetails.
 */
@Service
@Transactional
public class GdcDetailsServiceImpl implements GdcDetailsService {

  private final Logger log = LoggerFactory.getLogger(GdcDetailsServiceImpl.class);

  private final GdcDetailsRepository gdcDetailsRepository;

  private final GdcDetailsMapper gdcDetailsMapper;

  public GdcDetailsServiceImpl(GdcDetailsRepository gdcDetailsRepository, GdcDetailsMapper gdcDetailsMapper) {
    this.gdcDetailsRepository = gdcDetailsRepository;
    this.gdcDetailsMapper = gdcDetailsMapper;
  }

  /**
   * Save a gdcDetails.
   *
   * @param gdcDetailsDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public GdcDetailsDTO save(GdcDetailsDTO gdcDetailsDTO) {
    log.debug("Request to save GdcDetails : {}", gdcDetailsDTO);
    GdcDetails gdcDetails = gdcDetailsMapper.toEntity(gdcDetailsDTO);
    gdcDetails = gdcDetailsRepository.save(gdcDetails);
    return gdcDetailsMapper.toDto(gdcDetails);
  }

  /**
   * Get all the gdcDetails.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<GdcDetailsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all GdcDetails");
    return gdcDetailsRepository.findAll(pageable)
        .map(gdcDetailsMapper::toDto);
  }

  /**
   * Get one gdcDetails by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public GdcDetailsDTO findOne(Long id) {
    log.debug("Request to get GdcDetails : {}", id);
    GdcDetails gdcDetails = gdcDetailsRepository.findOne(id);
    return gdcDetailsMapper.toDto(gdcDetails);
  }

  /**
   * Delete the  gdcDetails by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete GdcDetails : {}", id);
    gdcDetailsRepository.delete(id);
  }
}
