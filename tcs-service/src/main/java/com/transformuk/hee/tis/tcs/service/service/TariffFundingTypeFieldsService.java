package com.transformuk.hee.tis.tcs.service.service;


import com.transformuk.hee.tis.tcs.api.dto.TariffFundingTypeFieldsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing TariffFundingTypeFields.
 */
public interface TariffFundingTypeFieldsService {

  /**
   * Save a tariffFundingTypeFields.
   *
   * @param tariffFundingTypeFieldsDTO the entity to save
   * @return the persisted entity
   */
  TariffFundingTypeFieldsDTO save(TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO);

  /**
   * Save a list of tariffFundingTypeFields.
   *
   * @param tariffFundingTypeFieldsDTO the entities to save
   * @return the list of persisted entities
   */
  List<TariffFundingTypeFieldsDTO> save(List<TariffFundingTypeFieldsDTO> tariffFundingTypeFieldsDTO);

  /**
   * Get all the tariffFundingTypeFields.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  Page<TariffFundingTypeFieldsDTO> findAll(Pageable pageable);

  /**
   * Get the "id" tariffFundingTypeFields.
   *
   * @param id the id of the entity
   * @return the entity
   */
  TariffFundingTypeFieldsDTO findOne(Long id);

  /**
   * Delete the "id" tariffFundingTypeFields.
   *
   * @param id the id of the entity
   */
  void delete(Long id);
}
