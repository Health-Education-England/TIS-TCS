package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ContactDetailsMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing ContactDetails.
 */
@Service
@Transactional
public class ContactDetailsServiceImpl implements ContactDetailsService {

  private final Logger log = LoggerFactory.getLogger(ContactDetailsServiceImpl.class);

  private final ContactDetailsRepository contactDetailsRepository;

  private final ContactDetailsMapper contactDetailsMapper;

  public ContactDetailsServiceImpl(ContactDetailsRepository contactDetailsRepository,
      ContactDetailsMapper contactDetailsMapper) {
    this.contactDetailsRepository = contactDetailsRepository;
    this.contactDetailsMapper = contactDetailsMapper;
  }

  /**
   * Save a contactDetails.
   *
   * @param contactDetailsDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public ContactDetailsDTO save(ContactDetailsDTO contactDetailsDTO) {
    log.debug("Request to save ContactDetails : {}", contactDetailsDTO);
    ContactDetails contactDetails = contactDetailsMapper.toEntity(contactDetailsDTO);
    contactDetails = contactDetailsRepository.saveAndFlush(contactDetails);
    return contactDetailsMapper.toDto(contactDetails);
  }

  /**
   * Save list of contactDetails.
   *
   * @param contactDetailsDTOs the list of entity to save
   * @return the persisted list of entity
   */
  @Override
  public List<ContactDetailsDTO> save(List<ContactDetailsDTO> contactDetailsDTOs) {
    log.debug("Request to save ContactDetails : {}", contactDetailsDTOs);
    List<ContactDetails> contactDetails = contactDetailsMapper.toEntity(contactDetailsDTOs);
    contactDetails = contactDetailsRepository.saveAll(contactDetails);
    return contactDetailsMapper.toDto(contactDetails);
  }

  /**
   * Get all the contactDetails.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<ContactDetailsDTO> findAll(Pageable pageable) {
    log.debug("Request to get all ContactDetails");
    return contactDetailsRepository.findAll(pageable)
        .map(contactDetailsMapper::toDto);
  }

  /**
   * Get one contactDetails by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public ContactDetailsDTO findOne(Long id) {
    log.debug("Request to get ContactDetails : {}", id);
    ContactDetails contactDetails = contactDetailsRepository.findById(id).orElse(null);
    return contactDetailsMapper.toDto(contactDetails);
  }

  /**
   * Delete the  contactDetails by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete ContactDetails : {}", id);
    contactDetailsRepository.deleteById(id);
  }
}
