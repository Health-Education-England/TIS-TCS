package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import com.transformuk.hee.tis.tcs.service.repository.PersonalDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonalDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonalDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * Service Implementation for managing PersonalDetails.
 */
@Service
@Transactional
public class PersonalDetailsServiceImpl implements PersonalDetailsService {

  private final Logger log = LoggerFactory.getLogger(PersonalDetailsServiceImpl.class);

  private final PersonalDetailsRepository personalDetailsRepository;

  private final PersonalDetailsMapper personalDetailsMapper;

  public PersonalDetailsServiceImpl(PersonalDetailsRepository personalDetailsRepository, PersonalDetailsMapper personalDetailsMapper) {
    this.personalDetailsRepository = personalDetailsRepository;
    this.personalDetailsMapper = personalDetailsMapper;
  }

  /**
   * Save a personalDetails.
   *
   * @param personalDetailsDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PersonalDetailsDTO save(PersonalDetailsDTO personalDetailsDTO) {
    log.debug("Request to save PersonalDetails : {}", personalDetailsDTO);
    PersonalDetails personalDetails = personalDetailsMapper.toEntity(personalDetailsDTO);
    personalDetails = personalDetailsRepository.saveAndFlush(personalDetails);
    return personalDetailsMapper.toDto(personalDetails);
  }

  /**
   * Save a list of personalDetails
   *
   * @param personalDetailsDTOs the list of entities to save
   * @return a list of persisted entities
   */
  @Override
  public List<PersonalDetailsDTO> save(List<PersonalDetailsDTO> personalDetailsDTOs) {
    log.debug("Request to save personalDetails : {}", personalDetailsDTOs);
    List<PersonalDetails> personalDetailsList = personalDetailsMapper.toEntity(personalDetailsDTOs);
    personalDetailsList = personalDetailsRepository.save(personalDetailsList);
    return personalDetailsMapper.toDto(personalDetailsList);
  }

  /**
   * Get all the personalDetails.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PersonalDetailsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all PersonalDetails");
    return personalDetailsRepository.findAll(pageable)
        .map(personalDetailsMapper::toDto);
  }

  /**
   * Get one personalDetails by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public PersonalDetailsDTO findOne(Long id) {
    log.debug("Request to get PersonalDetails : {}", id);
    PersonalDetails personalDetails = personalDetailsRepository.findOne(id);
    return personalDetailsMapper.toDto(personalDetails);
  }

  /**
   * Delete the  personalDetails by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete PersonalDetails : {}", id);
    personalDetailsRepository.delete(id);
  }
}
