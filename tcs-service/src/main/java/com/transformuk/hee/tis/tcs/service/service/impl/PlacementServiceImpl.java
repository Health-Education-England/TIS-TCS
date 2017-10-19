package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Placement.
 */
@Service
@Transactional
public class PlacementServiceImpl implements PlacementService {

  private final Logger log = LoggerFactory.getLogger(PlacementServiceImpl.class);

  private final PlacementRepository placementRepository;

  private final PlacementMapper placementMapper;

  public PlacementServiceImpl(PlacementRepository placementRepository,
                              PlacementMapper placementMapper) {
    this.placementRepository = placementRepository;
    this.placementMapper = placementMapper;
  }

  /**
   * Save a placement.
   *
   * @param placementDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PlacementDTO save(PlacementDTO placementDTO) {
    log.debug("Request to save Placement : {}", placementDTO);
    Placement placement = placementMapper.placementDTOToPlacement(placementDTO);
    placement = placementRepository.save(placement);
    PlacementDTO result = placementMapper.placementToPlacementDTO(placement);
    return result;
  }

  /**
   * Save a list of placements.
   *
   * @param placementDTO the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<PlacementDTO> save(List<PlacementDTO> placementDTO) {
    log.debug("Request to save Placements : {}", placementDTO);
    List<Placement> placements = placementMapper.placementDTOsToPlacements(placementDTO);
    placements = placementRepository.save(placements);
    List<PlacementDTO> result = placementMapper.placementsToPlacementDTOs(placements);
    return result;
  }

  /**
   * Get all the placements.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PlacementDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Placements");
    Page<Placement> result = placementRepository.findAll(pageable);
    return result.map(placement -> placementMapper.placementToPlacementDTO(placement));
  }

  /**
   * Get one placement by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public PlacementDTO findOne(Long id) {
    log.debug("Request to get Placement : {}", id);
    Placement placement = placementRepository.findOne(id);
    PlacementDTO placementViewDTO = placementMapper.placementToPlacementDTO(placement);
    return placementViewDTO;
  }

  /**
   * Delete the  placement by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Placement : {}", id);
    placementRepository.delete(id);
  }
}
