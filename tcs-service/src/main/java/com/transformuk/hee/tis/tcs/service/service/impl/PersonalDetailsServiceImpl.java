package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import com.transformuk.hee.tis.tcs.service.repository.PersonalDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.PersonalDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonalDetailsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private PersonalDetailsRepository personalDetailsRepository;
  @Autowired
  private PersonalDetailsMapper personalDetailsMapper;
  @Autowired
  private PermissionService permissionService;


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
    Long personDetailId = personalDetailsDTO.getId();
    if (!permissionService.canEditSensitiveData()) {
      PersonalDetails originalPersonDetail = personalDetailsRepository.findById(personDetailId).orElse(null);
      if (originalPersonDetail == null) { //during create
        clearSensitiveData(personalDetails);
      } else {
        personalDetails.setDisability(originalPersonDetail.getDisability());
        personalDetails.setDisabilityDetails(originalPersonDetail.getDisabilityDetails());
        personalDetails.setReligiousBelief(originalPersonDetail.getReligiousBelief());
        personalDetails.setSexualOrientation(originalPersonDetail.getSexualOrientation());
      }
    }
    personalDetails = personalDetailsRepository.saveAndFlush(personalDetails);
    PersonalDetailsDTO personalDetailsDTO1 = personalDetailsMapper.toDto(personalDetails);

    if (!permissionService.canEditSensitiveData()) {
      clearSensitiveData(personalDetailsDTO1);
    }
    return personalDetailsDTO1;
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
    personalDetailsList = personalDetailsRepository.saveAll(personalDetailsList);
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
    Page<PersonalDetailsDTO> map = personalDetailsRepository.findAll(pageable)
        .map(personalDetailsMapper::toDto);

    if (!permissionService.canViewSensitiveData()) {
      for (PersonalDetailsDTO personalDetailDTO : map.getContent()) {
        clearSensitiveData(personalDetailDTO);
      }
    }

    return map;
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
    PersonalDetails personalDetails = personalDetailsRepository.findById(id).orElse(null);
    PersonalDetailsDTO personalDetailsDTO = personalDetailsMapper.toDto(personalDetails);

    if (!permissionService.canViewSensitiveData()) {
      clearSensitiveData(personalDetailsDTO);
    }
    return personalDetailsDTO;
  }

  private void clearSensitiveData(PersonalDetails personalDetails) {
    personalDetails.setDisability(null);
    personalDetails.setDisabilityDetails(null);
    personalDetails.setReligiousBelief(null);
    personalDetails.setSexualOrientation(null);
  }

  private void clearSensitiveData(PersonalDetailsDTO personalDetailsDTO) {
    personalDetailsDTO.setDisability(null);
    personalDetailsDTO.setDisabilityDetails(null);
    personalDetailsDTO.setReligiousBelief(null);
    personalDetailsDTO.setSexualOrientation(null);
  }


  /**
   * Delete the  personalDetails by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete PersonalDetails : {}", id);
    personalDetailsRepository.deleteById(id);
  }
}
