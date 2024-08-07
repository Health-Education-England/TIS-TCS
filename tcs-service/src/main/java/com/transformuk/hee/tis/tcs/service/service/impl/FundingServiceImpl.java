package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.FundingDTO;
import com.transformuk.hee.tis.tcs.service.model.Funding;
import com.transformuk.hee.tis.tcs.service.repository.FundingRepository;
import com.transformuk.hee.tis.tcs.service.service.FundingService;
import com.transformuk.hee.tis.tcs.service.service.mapper.FundingMapper;
import java.util.List;
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
    return fundingMapper.fundingToFundingDTO(funding);
  }

  /**
   * Save a list of funding
   *
   * @param fundingDTO tje list of entities to save
   * @return the persisted entities
   */
  @Override
  public List<FundingDTO> save(List<FundingDTO> fundingDTO) {
    log.debug("Request to save Fundings : {}", fundingDTO);
    List<Funding> fundings = fundingMapper.fundingDTOsToFundings(fundingDTO);
    fundings = fundingRepository.saveAll(fundings);
    return fundingMapper.fundingsToFundingDTOs(fundings);
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
    return result.map(fundingMapper::fundingToFundingDTO);
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
    Funding funding = fundingRepository.findById(id).orElse(null);
    return fundingMapper.fundingToFundingDTO(funding);
  }

  /**
   * Delete the  funding by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Funding : {}", id);
    fundingRepository.deleteById(id);
  }
}
