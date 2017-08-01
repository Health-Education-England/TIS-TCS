package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.TariffRateDTO;
import com.transformuk.hee.tis.tcs.service.model.TariffRate;
import com.transformuk.hee.tis.tcs.service.repository.TariffRateRepository;
import com.transformuk.hee.tis.tcs.service.service.TariffRateService;
import com.transformuk.hee.tis.tcs.service.service.mapper.TariffRateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing TariffRate.
 */
@Service
@Transactional
public class TariffRateServiceImpl implements TariffRateService {

  private final Logger log = LoggerFactory.getLogger(TariffRateServiceImpl.class);

  private final TariffRateRepository tariffRateRepository;

  private final TariffRateMapper tariffRateMapper;

  public TariffRateServiceImpl(TariffRateRepository tariffRateRepository, TariffRateMapper tariffRateMapper) {
    this.tariffRateRepository = tariffRateRepository;
    this.tariffRateMapper = tariffRateMapper;
  }

  /**
   * Save a tariffRate.
   *
   * @param tariffRateDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public TariffRateDTO save(TariffRateDTO tariffRateDTO) {
    log.debug("Request to save TariffRate : {}", tariffRateDTO);
    TariffRate tariffRate = tariffRateMapper.tariffRateDTOToTariffRate(tariffRateDTO);
    tariffRate = tariffRateRepository.save(tariffRate);
    TariffRateDTO result = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);
    return result;
  }

  /**
   * Save a list of tariffRate.
   *
   * @param tariffRateDTO the entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<TariffRateDTO> save(List<TariffRateDTO> tariffRateDTO) {
    log.debug("Request to save TariffRate : {}", tariffRateDTO);
    List<TariffRate> tariffRate = tariffRateMapper.tariffRateDTOsToTariffRates(tariffRateDTO);
    tariffRate = tariffRateRepository.save(tariffRate);
    List<TariffRateDTO> result = tariffRateMapper.tariffRatesToTariffRateDTOs(tariffRate);
    return result;
  }

  /**
   * Get all the tariffRates.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<TariffRateDTO> findAll(Pageable pageable) {
    log.debug("Request to get all TariffRates");
    Page<TariffRate> tariffRatesPage = tariffRateRepository.findAll(pageable);
    return tariffRatesPage.map(tariffRate -> tariffRateMapper.tariffRateToTariffRateDTO(tariffRate));
  }

  /**
   * Get one tariffRate by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public TariffRateDTO findOne(Long id) {
    log.debug("Request to get TariffRate : {}", id);
    TariffRate tariffRate = tariffRateRepository.findOne(id);
    TariffRateDTO tariffRateDTO = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);
    return tariffRateDTO;
  }

  /**
   * Delete the  tariffRate by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete TariffRate : {}", id);
    tariffRateRepository.delete(id);
  }
}
