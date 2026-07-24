package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.tcs.service.service.helper.TransactionSynchronizationTestUtil.clearTransactionSynchronization;
import static com.transformuk.hee.tis.tcs.service.service.helper.TransactionSynchronizationTestUtil.startTransactionSynchronization;
import static com.transformuk.hee.tis.tcs.service.service.helper.TransactionSynchronizationTestUtil.triggerAfterCommit;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.service.event.ContactDetailsSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.ContactDetailsMapper;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

@ExtendWith(MockitoExtension.class)
class ContactDetailsServiceImplTest {

  private static final Long CONTACT_DETAILS_ID_1 = 1L;
  private static final String EXISTING_FORENAMES_1 = "Existing";
  private static final String UPDATED_FORENAMES_1 = "Updated";
  private static final String SURNAME = "User";

  private static final Long CONTACT_DETAILS_ID_2 = 2L;
  private static final String EXISTING_FORENAMES_2 = "Another Existing";
  private static final String UPDATED_FORENAMES_2 = "Another Updated";

  @Mock
  private ContactDetailsRepository contactDetailsRepository;

  @Mock
  private ContactDetailsMapper contactDetailsMapper;

  @Mock
  private ApplicationEventPublisher applicationEventPublisher;

  @InjectMocks
  private ContactDetailsServiceImpl service;

  @Captor
  private ArgumentCaptor<ContactDetailsSavedEvent> eventCaptor;

  private ContactDetailsDTO inputDto;
  private ContactDetails existingEntity;
  private ContactDetails existingMappedEntity;
  private ContactDetails savedEntity;
  private ContactDetailsDTO existingDto;
  private ContactDetailsDTO updatedDto;

  @BeforeEach
  void setUp() {
    inputDto = new ContactDetailsDTO();
    inputDto.setId(CONTACT_DETAILS_ID_1);
    inputDto.setForenames(UPDATED_FORENAMES_1);
    inputDto.setSurname(SURNAME);

    existingEntity = new ContactDetails();
    existingEntity.setId(CONTACT_DETAILS_ID_1);
    existingEntity.setForenames(EXISTING_FORENAMES_1);
    existingEntity.setSurname(SURNAME);

    existingMappedEntity = new ContactDetails();
    existingMappedEntity.setId(CONTACT_DETAILS_ID_1);
    existingMappedEntity.setForenames(UPDATED_FORENAMES_1);
    existingMappedEntity.setSurname(SURNAME);

    savedEntity = new ContactDetails();
    savedEntity.setId(CONTACT_DETAILS_ID_1);
    savedEntity.setForenames(UPDATED_FORENAMES_1);
    savedEntity.setSurname(SURNAME);

    existingDto = new ContactDetailsDTO();
    existingDto.setId(CONTACT_DETAILS_ID_1);
    existingDto.setForenames(EXISTING_FORENAMES_1);
    existingDto.setSurname(SURNAME);

    updatedDto = new ContactDetailsDTO();
    updatedDto.setId(CONTACT_DETAILS_ID_1);
    updatedDto.setForenames(UPDATED_FORENAMES_1);
    updatedDto.setSurname(SURNAME);
  }

  @Test
  void saveShouldPersistMappedEntityAndPublishSavedEvent() {
    when(contactDetailsRepository.findById(CONTACT_DETAILS_ID_1)).thenReturn(java.util.Optional.of(existingEntity));
    when(contactDetailsMapper.toDto(existingEntity)).thenReturn(existingDto);
    when(contactDetailsMapper.toEntity(inputDto)).thenReturn(existingMappedEntity);
    when(contactDetailsRepository.saveAndFlush(existingMappedEntity)).thenReturn(savedEntity);
    when(contactDetailsMapper.toDto(savedEntity)).thenReturn(updatedDto);

    startTransactionSynchronization();
    ContactDetailsDTO result = service.save(inputDto);

    assertSame(updatedDto, result);
    verify(contactDetailsRepository).findById(CONTACT_DETAILS_ID_1);
    verify(contactDetailsMapper).toEntity(inputDto);
    verify(contactDetailsRepository).saveAndFlush(existingMappedEntity);

    triggerAfterCommit();
    verify(applicationEventPublisher).publishEvent(eventCaptor.capture());

    ContactDetailsSavedEvent publishedEvent = eventCaptor.getValue();
    assertEquals(existingDto, publishedEvent.getPreviousContactDetailsDto());
    assertEquals(updatedDto, publishedEvent.getContactDetailsDto());
  }

  @Test
  void saveShouldPersistMappedEntitiesAndPublishSavedEventsForDistinctDtos() {
    ContactDetailsDTO secondInputDto = new ContactDetailsDTO();
    secondInputDto.setId(CONTACT_DETAILS_ID_2);
    secondInputDto.setForenames(UPDATED_FORENAMES_2);
    secondInputDto.setSurname(SURNAME);

    ContactDetails secondExistingEntity = new ContactDetails();
    secondExistingEntity.setId(CONTACT_DETAILS_ID_2);
    secondExistingEntity.setForenames(EXISTING_FORENAMES_2);
    secondExistingEntity.setSurname(SURNAME);

    ContactDetails secondMappedEntity = new ContactDetails();
    secondMappedEntity.setId(CONTACT_DETAILS_ID_2);
    secondMappedEntity.setForenames(UPDATED_FORENAMES_2);
    secondMappedEntity.setSurname(SURNAME);

    ContactDetailsDTO secondExistingDto = new ContactDetailsDTO();
    secondExistingDto.setId(CONTACT_DETAILS_ID_2);
    secondExistingDto.setForenames(EXISTING_FORENAMES_2);
    secondExistingDto.setSurname(SURNAME);

    ContactDetailsDTO secondUpdatedDto = new ContactDetailsDTO();
    secondUpdatedDto.setId(CONTACT_DETAILS_ID_2);
    secondUpdatedDto.setForenames(UPDATED_FORENAMES_2);
    secondUpdatedDto.setSurname(SURNAME);

    List<ContactDetailsDTO> inputDtos = List.of(inputDto, secondInputDto);
    List<ContactDetails> existingEntities = List.of(existingEntity, secondExistingEntity);
    List<ContactDetails> mappedEntities = List.of(existingMappedEntity, secondMappedEntity);
    List<ContactDetailsDTO> updatedDtos = List.of(updatedDto, secondUpdatedDto, updatedDto);

    when(contactDetailsRepository.findAllById(argThat(ids -> ids.equals(Set.of(
        CONTACT_DETAILS_ID_1, CONTACT_DETAILS_ID_2))))).thenReturn(existingEntities);
    when(contactDetailsMapper.toDto(existingEntity)).thenReturn(existingDto);
    when(contactDetailsMapper.toDto(secondExistingEntity)).thenReturn(secondExistingDto);
    when(contactDetailsMapper.toEntity(inputDtos)).thenReturn(mappedEntities);
    when(contactDetailsRepository.saveAll(mappedEntities)).thenReturn(mappedEntities);
    when(contactDetailsMapper.toDto(mappedEntities)).thenReturn(updatedDtos);

    startTransactionSynchronization();
    List<ContactDetailsDTO> result = service.save(inputDtos);

    assertSame(updatedDtos, result);
    verify(contactDetailsRepository).findAllById(argThat(ids -> ids.equals(Set.of(
        CONTACT_DETAILS_ID_1, CONTACT_DETAILS_ID_2))));
    verify(contactDetailsMapper).toEntity(inputDtos);
    verify(contactDetailsRepository).saveAll(mappedEntities);

    triggerAfterCommit();
    verify(applicationEventPublisher, times(2)).publishEvent(eventCaptor.capture());

    List<ContactDetailsSavedEvent> publishedEvents = eventCaptor.getAllValues();
    assertEquals(existingDto, publishedEvents.get(0).getPreviousContactDetailsDto());
    assertEquals(updatedDto, publishedEvents.get(0).getContactDetailsDto());
    assertEquals(secondExistingDto, publishedEvents.get(1).getPreviousContactDetailsDto());
    assertEquals(secondUpdatedDto, publishedEvents.get(1).getContactDetailsDto());
  }

  @AfterEach
  void clearTransactionSynchronizationAfterEachTest() {
    clearTransactionSynchronization();
  }
}
