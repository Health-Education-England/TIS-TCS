package com.transformuk.hee.tis.service.impl;

import com.transformuk.hee.tis.domain.Funding;
import com.transformuk.hee.tis.repository.FundingRepository;
import com.transformuk.hee.tis.service.FundingService;
import com.transformuk.hee.tis.service.dto.FundingDTO;
import com.transformuk.hee.tis.service.mapper.FundingMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Funding.
 */
@Service
@Transactional
public class FundingServiceImpl implements FundingService {

	private final Logger log = LoggerFactory.getLogger(FundingServiceImpl.class);

	private final FundingRepository fundingRepository;

	private final FundingMapper fundingMapper;

	public FundingServiceImpl(FundingRepository fundingRepository, FundingMapper fundingMapper) {
		this.fundingRepository = fundingRepository;
		this.fundingMapper = fundingMapper;
	}

	/**
	 * Save a funding.
	 *
	 * @param fundingDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public FundingDTO save(FundingDTO fundingDTO) {
		log.debug("Request to save Funding : {}", fundingDTO);
		Funding funding = fundingMapper.fundingDTOToFunding(fundingDTO);
		funding = fundingRepository.save(funding);
		FundingDTO result = fundingMapper.fundingToFundingDTO(funding);
		return result;
	}

	/**
	 * Get all the fundings.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<FundingDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Fundings");
		Page<Funding> result = fundingRepository.findAll(pageable);
		return result.map(funding -> fundingMapper.fundingToFundingDTO(funding));
	}

	/**
	 * Get one funding by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public FundingDTO findOne(Long id) {
		log.debug("Request to get Funding : {}", id);
		Funding funding = fundingRepository.findOne(id);
		FundingDTO fundingDTO = fundingMapper.fundingToFundingDTO(funding);
		return fundingDTO;
	}

	/**
	 * Delete the  funding by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Funding : {}", id);
		fundingRepository.delete(id);
	}
}
