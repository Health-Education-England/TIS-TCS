package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.service.model.PlacementFunder;
import com.transformuk.hee.tis.tcs.service.repository.PlacementFunderRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementFunderService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementFunderMapper;
import com.transformuk.hee.tis.tcs.api.dto.PlacementFunderDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing PlacementFunder.
 */
@Service
@Transactional
public class PlacementFunderServiceImpl implements PlacementFunderService {

	private final Logger log = LoggerFactory.getLogger(PlacementFunderServiceImpl.class);

	private final PlacementFunderRepository placementFunderRepository;

	private final PlacementFunderMapper placementFunderMapper;

	public PlacementFunderServiceImpl(PlacementFunderRepository placementFunderRepository, PlacementFunderMapper placementFunderMapper) {
		this.placementFunderRepository = placementFunderRepository;
		this.placementFunderMapper = placementFunderMapper;
	}

	/**
	 * Save a placementFunder.
	 *
	 * @param placementFunderDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public PlacementFunderDTO save(PlacementFunderDTO placementFunderDTO) {
		log.debug("Request to save PlacementFunder : {}", placementFunderDTO);
		PlacementFunder placementFunder = placementFunderMapper.placementFunderDTOToPlacementFunder(placementFunderDTO);
		placementFunder = placementFunderRepository.save(placementFunder);
		PlacementFunderDTO result = placementFunderMapper.placementFunderToPlacementFunderDTO(placementFunder);
		return result;
	}

	/**
	 * Get all the placementFunders.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<PlacementFunderDTO> findAll(Pageable pageable) {
		log.debug("Request to get all PlacementFunders");
		Page<PlacementFunder> result = placementFunderRepository.findAll(pageable);
		return result.map(placementFunder -> placementFunderMapper.placementFunderToPlacementFunderDTO(placementFunder));
	}

	/**
	 * Get one placementFunder by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public PlacementFunderDTO findOne(Long id) {
		log.debug("Request to get PlacementFunder : {}", id);
		PlacementFunder placementFunder = placementFunderRepository.findOne(id);
		PlacementFunderDTO placementFunderDTO = placementFunderMapper.placementFunderToPlacementFunderDTO(placementFunder);
		return placementFunderDTO;
	}

	/**
	 * Delete the  placementFunder by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete PlacementFunder : {}", id);
		placementFunderRepository.delete(id);
	}
}
