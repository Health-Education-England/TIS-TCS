package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.TariffFundingTypeFieldsDTO;
import com.transformuk.hee.tis.tcs.service.model.TariffFundingTypeFields;
import com.transformuk.hee.tis.tcs.service.repository.TariffFundingTypeFieldsRepository;
import com.transformuk.hee.tis.tcs.service.service.TariffFundingTypeFieldsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.TariffFundingTypeFieldsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing TariffFundingTypeFields.
 */
@Service
@Transactional
public class TariffFundingTypeFieldsServiceImpl implements TariffFundingTypeFieldsService {

  private final Logger log = LoggerFactory.getLogger(TariffFundingTypeFieldsServiceImpl.class);

  private final TariffFundingTypeFieldsRepository tariffFundingTypeFieldsRepository;

  private final TariffFundingTypeFieldsMapper tariffFundingTypeFieldsMapper;

  public TariffFundingTypeFieldsServiceImpl(TariffFundingTypeFieldsRepository tariffFundingTypeFieldsRepository, TariffFundingTypeFieldsMapper tariffFundingTypeFieldsMapper) {
    this.tariffFundingTypeFieldsRepository = tariffFundingTypeFieldsRepository;
    this.tariffFundingTypeFieldsMapper = tariffFundingTypeFieldsMapper;
  }

  /**
   * Save a tariffFundingTypeFields.
   *
   * @param tariffFundingTypeFieldsDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public TariffFundingTypeFieldsDTO save(TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO) {
    log.debug("Request to save TariffFundingTypeFields : {}", tariffFundingTypeFieldsDTO);
    TariffFundingTypeFields tariffFundingTypeFields = tariffFundingTypeFieldsMapper.tariffFundingTypeFieldsDTOToTariffFundingTypeFields(tariffFundingTypeFieldsDTO);
    tariffFundingTypeFields = tariffFundingTypeFieldsRepository.save(tariffFundingTypeFields);
    TariffFundingTypeFieldsDTO result = tariffFundingTypeFieldsMapper.tariffFundingTypeFieldsToTariffFundingTypeFieldsDTO(tariffFundingTypeFields);
    return result;
  }

  /**
   * Save a list of tariffFundingTypeFields.
   *
   * @param tariffFundingTypeFieldsDTO the entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<TariffFundingTypeFieldsDTO> save(List<TariffFundingTypeFieldsDTO> tariffFundingTypeFieldsDTO) {
    log.debug("Request to save TariffFundingTypeFields : {}", tariffFundingTypeFieldsDTO);
    List<TariffFundingTypeFields> tariffFundingTypeFields = tariffFundingTypeFieldsMapper.tariffFundingTypeFieldsDTOsToTariffFundingTypeFields(tariffFundingTypeFieldsDTO);
    tariffFundingTypeFields = tariffFundingTypeFieldsRepository.save(tariffFundingTypeFields);
    List<TariffFundingTypeFieldsDTO> result = tariffFundingTypeFieldsMapper.tariffFundingTypeFieldsToTariffFundingTypeFieldsDTOs(tariffFundingTypeFields);
    return result;
  }

  /**
   * Get all the tariffFundingTypeFields.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<TariffFundingTypeFieldsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all TariffFundingTypeFields");
    Page<TariffFundingTypeFields> page = tariffFundingTypeFieldsRepository.findAll(pageable);
    return page.map(tftf -> tariffFundingTypeFieldsMapper.tariffFundingTypeFieldsToTariffFundingTypeFieldsDTO(tftf));
  }

  /**
   * Get one tariffFundingTypeFields by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public TariffFundingTypeFieldsDTO findOne(Long id) {
    log.debug("Request to get TariffFundingTypeFields : {}", id);
    TariffFundingTypeFields tariffFundingTypeFields = tariffFundingTypeFieldsRepository.findOne(id);
    TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO = tariffFundingTypeFieldsMapper.tariffFundingTypeFieldsToTariffFundingTypeFieldsDTO(tariffFundingTypeFields);
    return tariffFundingTypeFieldsDTO;
  }

  /**
   * Delete the  tariffFundingTypeFields by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete TariffFundingTypeFields : {}", id);
    tariffFundingTypeFieldsRepository.delete(id);
  }
}
