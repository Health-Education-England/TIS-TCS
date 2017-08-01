package com.transformuk.hee.tis.tcs.service.service;


import com.transformuk.hee.tis.tcs.api.dto.TariffRateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing TariffRate.
 */
public interface TariffRateService {

  /**
   * Save a tariffRate.
   *
   * @param tariffRateDTO the entity to save
   * @return the persisted entity
   */
  TariffRateDTO save(TariffRateDTO tariffRateDTO);

  /**
   * Save a list of tariffRate.
   *
   * @param tariffRateDTO the entities to save
   * @return the list of persisted entities
   */
  List<TariffRateDTO> save(List<TariffRateDTO> tariffRateDTO);

  /**
   * Get all the tariffRates.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<TariffRateDTO> findAll(Pageable pageable);

  /**
   * Get the "id" tariffRate.
   *
   * @param id the id of the entity
   * @return the entity
   */
  TariffRateDTO findOne(Long id);

  /**
   * Delete the "id" tariffRate.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
