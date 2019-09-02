package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.event.GmcDetailsDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.GmcDetailsSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.GmcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.GmcDetailsMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
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
  private final ApplicationEventPublisher applicationEventPublisher;

  public GmcDetailsServiceImpl(GmcDetailsRepository gmcDetailsRepository,
      GmcDetailsMapper gmcDetailsMapper, ApplicationEventPublisher applicationEventPublisher) {
    this.gmcDetailsRepository = gmcDetailsRepository;
    this.gmcDetailsMapper = gmcDetailsMapper;
    this.applicationEventPublisher = applicationEventPublisher;
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
    gmcDetails = gmcDetailsRepository.saveAndFlush(gmcDetails);

    gmcDetailsDTO = gmcDetailsMapper.toDto(gmcDetails);
    applicationEventPublisher.publishEvent(new GmcDetailsSavedEvent(gmcDetailsDTO));
    return gmcDetailsDTO;
  }

  /**
   * Save a list of gmcDetails
   *
   * @param gmcDetailsDTOs the list of entities to save
   * @return a list of persisted entities
   */
  @Override
  public List<GmcDetailsDTO> save(List<GmcDetailsDTO> gmcDetailsDTOs) {
    log.debug("Request to save GmcDetails : {}", gmcDetailsDTOs);
    List<GmcDetails> gmcDetailsList = gmcDetailsMapper.toEntity(gmcDetailsDTOs);
    gmcDetailsList = gmcDetailsRepository.saveAll(gmcDetailsList);
    List<GmcDetailsDTO> gmcDetailsDTOS = gmcDetailsMapper.toDto(gmcDetailsList);

    gmcDetailsDTOS.stream().map(GmcDetailsSavedEvent::new)
        .forEach(applicationEventPublisher::publishEvent);

    return gmcDetailsDTOS;
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
    return gmcDetailsRepository.findAll(pageable).map(gmcDetailsMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public List<GmcDetailsDTO> findByIdIn(List<String> gmcIds) {
    log.debug("Request to get all GmcDetails");

    List<GmcDetails> byGmcIdsIn = gmcDetailsRepository.findByGmcNumberIn(gmcIds);
    return gmcDetailsMapper.gmcDetailsToGmcDetailsDTO(byGmcIdsIn);
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
    GmcDetails gmcDetails = gmcDetailsRepository.findById(id).orElse(null);
    return gmcDetailsMapper.toDto(gmcDetails);
  }

  /**
   * Delete the gmcDetails by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete GmcDetails : {}", id);
    gmcDetailsRepository.deleteById(id);
    applicationEventPublisher.publishEvent(new GmcDetailsDeletedEvent(id));
  }
}
