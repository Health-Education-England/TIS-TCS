package com.transformuk.hee.tis.tcs.service.service.impl;

import static java.util.Collections.emptyMap;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.service.event.ContactDetailsSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.ContactDetailsMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
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

  private final ApplicationEventPublisher applicationEventPublisher;

  /**
   * Constructor for ContactDetailsServiceImpl.
   *
   * @param contactDetailsRepository the repository for ContactDetails
   * @param contactDetailsMapper the mapper for converting between ContactDetails and
   *                             ContactDetailsDTO
   * @param applicationEventPublisher the event publisher for publishing ContactDetailsSavedEvent
   */
  public ContactDetailsServiceImpl(ContactDetailsRepository contactDetailsRepository,
      ContactDetailsMapper contactDetailsMapper,
      ApplicationEventPublisher applicationEventPublisher) {
    this.contactDetailsRepository = contactDetailsRepository;
    this.contactDetailsMapper = contactDetailsMapper;
    this.applicationEventPublisher = applicationEventPublisher;
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
    ContactDetails existingContactDetails =
        contactDetailsRepository.findById(contactDetailsDTO.getId()).orElse(null);
    ContactDetailsDTO existingContactDetailsDto = contactDetailsMapper.toDto(
        existingContactDetails);

    ContactDetails contactDetails = contactDetailsMapper.toEntity(contactDetailsDTO);
    contactDetails = contactDetailsRepository.saveAndFlush(contactDetails);
    ContactDetailsDTO updatedContactDetailsDto = contactDetailsMapper.toDto(contactDetails);

    applicationEventPublisher.publishEvent(
        new ContactDetailsSavedEvent(existingContactDetailsDto, updatedContactDetailsDto));
    return updatedContactDetailsDto;
  }

  /**
   * Save list of contactDetails.
   *
   * @param contactDetailsDtos the list of entity to save
   * @return the persisted list of entity
   */
  @Override
  public List<ContactDetailsDTO> save(List<ContactDetailsDTO> contactDetailsDtos) {
    log.debug("Request to save ContactDetails : {}", contactDetailsDtos);

    Set<Long> contactDetailIds = contactDetailsDtos.stream()
        .map(ContactDetailsDTO::getId)
        .filter(java.util.Objects::nonNull)
        .collect(Collectors.toSet());

    Map<Long, ContactDetailsDTO> existingContactDetailDtos = contactDetailIds.isEmpty()
        ? emptyMap()
        : contactDetailsRepository.findAllById(contactDetailIds)
            .stream()
            .collect(Collectors.toMap(ContactDetails::getId, contactDetailsMapper::toDto));

    List<ContactDetails> contactDetails = contactDetailsMapper.toEntity(contactDetailsDtos);
    contactDetails = contactDetailsRepository.saveAll(contactDetails);

    List<ContactDetailsDTO> updatedContactDetailsDtos = contactDetailsMapper.toDto(contactDetails);

    updatedContactDetailsDtos.stream().distinct().forEach(contactDetailsDto -> {
      ContactDetailsDTO previousContactDetailsDto =
          existingContactDetailDtos.get(contactDetailsDto.getId());
      applicationEventPublisher.publishEvent(
          new ContactDetailsSavedEvent(previousContactDetailsDto, contactDetailsDto));
    });
    return updatedContactDetailsDtos;
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

  /**
   * Update Contact Details of a person which are not null in incoming DTO.
   *
   * @param contactDetailsDTO contactDetailsDTO to be updated
   * @return updated DTO
   */
  @Override
  public Optional<ContactDetailsDTO> patch(ContactDetailsDTO contactDetailsDTO) {
    log.debug("Request to patch contact details: {}", contactDetailsDTO);

    ContactDetails originalContactDetails = contactDetailsRepository
        .findById(contactDetailsDTO.getId())
        .orElse(null);

    Optional<ContactDetails> updatedContactDetailsOptional = contactDetailsMapper
        .toPatchedEntity(originalContactDetails, contactDetailsDTO);

    if (updatedContactDetailsOptional.isPresent()) {
      ContactDetails updatedContactDetails = updatedContactDetailsOptional.get();

      updatedContactDetails = contactDetailsRepository.saveAndFlush(updatedContactDetails);

      ContactDetailsDTO contactDetailsDTO1 = contactDetailsMapper.toDto(updatedContactDetails);

      return Optional.ofNullable(contactDetailsDTO1);
    }
    return Optional.empty();
  }

}
