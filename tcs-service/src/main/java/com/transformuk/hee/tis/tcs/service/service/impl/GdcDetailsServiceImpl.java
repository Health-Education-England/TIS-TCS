package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.GdcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.GdcDetailsMapper;
import java.util.List;
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

  public GdcDetailsServiceImpl(GdcDetailsRepository gdcDetailsRepository,
      GdcDetailsMapper gdcDetailsMapper) {
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
    gdcDetails = gdcDetailsRepository.saveAndFlush(gdcDetails);
    return gdcDetailsMapper.toDto(gdcDetails);
  }

  /**
   * Save a list of gdcDetails
   *
   * @param gdcDetailsDTOs the list of entities to save
   * @return a list of persisted entities
   */
  @Override
  public List<GdcDetailsDTO> save(List<GdcDetailsDTO> gdcDetailsDTOs) {
    log.debug("Request to save GdcDetails : {}", gdcDetailsDTOs);
    List<GdcDetails> gdcDetailsList = gdcDetailsMapper.toEntity(gdcDetailsDTOs);
    gdcDetailsList = gdcDetailsRepository.saveAll(gdcDetailsList);
    return gdcDetailsMapper.toDto(gdcDetailsList);
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

  @Override
  @Transactional(readOnly = true)
  public List<GdcDetailsDTO> findByIdIn(List<String> gdcIds) {
    log.debug("Request to get all GdcDetails");

    List<GdcDetails> byGdcIdsIn = gdcDetailsRepository.findByGdcNumberIn(gdcIds);
    return gdcDetailsMapper.gdcDetailsToGdcDetailsDTO(byGdcIdsIn);
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
    GdcDetails gdcDetails = gdcDetailsRepository.findById(id).orElse(null);
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
    gdcDetailsRepository.deleteById(id);
  }
}
