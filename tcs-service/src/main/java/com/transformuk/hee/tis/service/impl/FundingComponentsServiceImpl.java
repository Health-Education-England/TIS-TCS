package com.transformuk.hee.tis.service.impl;

import com.transformuk.hee.tis.domain.FundingComponents;
import com.transformuk.hee.tis.repository.FundingComponentsRepository;
import com.transformuk.hee.tis.service.FundingComponentsService;
import com.transformuk.hee.tis.service.mapper.FundingComponentsMapper;
import com.transformuk.hee.tis.tcs.api.dto.FundingComponentsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing FundingComponents.
 */
@Service
@Transactional
public class FundingComponentsServiceImpl implements FundingComponentsService {

	private final Logger log = LoggerFactory.getLogger(FundingComponentsServiceImpl.class);

	private final FundingComponentsRepository fundingComponentsRepository;

	private final FundingComponentsMapper fundingComponentsMapper;

	public FundingComponentsServiceImpl(FundingComponentsRepository fundingComponentsRepository, FundingComponentsMapper fundingComponentsMapper) {
		this.fundingComponentsRepository = fundingComponentsRepository;
		this.fundingComponentsMapper = fundingComponentsMapper;
	}

	/**
	 * Save a fundingComponents.
	 *
	 * @param fundingComponentsDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public FundingComponentsDTO save(FundingComponentsDTO fundingComponentsDTO) {
		log.debug("Request to save FundingComponents : {}", fundingComponentsDTO);
		FundingComponents fundingComponents = fundingComponentsMapper.fundingComponentsDTOToFundingComponents(fundingComponentsDTO);
		fundingComponents = fundingComponentsRepository.save(fundingComponents);
		FundingComponentsDTO result = fundingComponentsMapper.fundingComponentsToFundingComponentsDTO(fundingComponents);
		return result;
	}

	/**
	 * Get all the fundingComponents.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<FundingComponentsDTO> findAll(Pageable pageable) {
		log.debug("Request to get all FundingComponents");
		Page<FundingComponents> result = fundingComponentsRepository.findAll(pageable);
		return result.map(fundingComponents -> fundingComponentsMapper.fundingComponentsToFundingComponentsDTO(fundingComponents));
	}

	/**
	 * Get one fundingComponents by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public FundingComponentsDTO findOne(Long id) {
		log.debug("Request to get FundingComponents : {}", id);
		FundingComponents fundingComponents = fundingComponentsRepository.findOne(id);
		FundingComponentsDTO fundingComponentsDTO = fundingComponentsMapper.fundingComponentsToFundingComponentsDTO(fundingComponents);
		return fundingComponentsDTO;
	}

	/**
	 * Delete the  fundingComponents by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete FundingComponents : {}", id);
		fundingComponentsRepository.delete(id);
	}
}
