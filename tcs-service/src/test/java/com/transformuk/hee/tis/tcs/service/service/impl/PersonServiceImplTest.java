package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonV2DTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.api.validation.PersonValidator;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.model.CurriculumMembership;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonTrust;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonalDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.RightToWorkRepository;
import com.transformuk.hee.tis.tcs.service.service.ContactDetailsService;
import com.transformuk.hee.tis.tcs.service.service.GdcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.GmcDetailsService;
import com.transformuk.hee.tis.tcs.service.service.PersonalDetailsService;
import com.transformuk.hee.tis.tcs.service.service.RightToWorkService;
import com.transformuk.hee.tis.tcs.service.service.TrainerApprovalService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonDtoMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.TrainerApprovalDtoMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceImplTest {

  private static final Long PERSON_ID = 1L;
  private static final Long SAVED_PERSON_ID = 1L;
  private static final String DISABILITY_VALUE = "DISABILITY";
  private static final String DISABILITY_DETAILS_VALUE = "DISABILITY DETAILS";
  private static final String RELIGIOUS_BELIEF_VALUE = "RELIGIOUS BELIEF";
  private static final String SEXUAL_ORIENTATION_VALUE = "SEXUAL ORIENTATION";

  @Spy
  @InjectMocks
  private PersonServiceImpl testObj;

  @Mock
  private PermissionService permissionServiceMock;
  @Mock
  private PersonValidator personValidatorMock;
  @Mock
  private PersonRepository personRepositoryMock;
  @Mock
  private PersonMapper personMapperMock;
  @Captor
  private ArgumentCaptor<PersonDTO> personDTOArgumentCaptor;
  @Captor
  private ArgumentCaptor<PersonV2DTO> personV2DTOArgumentCaptor;

  private Person person;
  private CurriculumMembership pm1, pm2, pm3, pm4;
  private PersonDTO personDTO;
  private ProgrammeMembershipDTO pmDTO1, pmDTO2, pmDTO3, pmDTO4;
  private PersonDTO unsavedPersonDTOMock = mock(PersonDTO.class), savedPersonDTOMock = mock(
      PersonDTO.class);
  private PersonalDetailsDTO unsavedPersonDetailsDTOMock = mock(
      PersonalDetailsDTO.class), savedPersonDetailsDTOMock = mock(PersonalDetailsDTO.class);
  private Person unsavedPersonMock = mock(Person.class), savedPersonMock = mock(Person.class);

  @Mock
  private GdcDetailsRepository gdcDetailsRepositoryMock;
  @Mock
  private GmcDetailsRepository gmcDetailsRepositoryMock;
  @Mock
  private ContactDetailsRepository contactDetailsRepositoryMock;
  @Mock
  private PersonalDetailsRepository personalDetailsRepositoryMock;
  @Mock
  private RightToWorkRepository rightToWorkRepositoryMock;
  @Mock
  private PersonalDetailsService personalDetailsServiceMock;
  @Mock
  private ContactDetailsService contactDetailsServiceMock;
  @Mock
  private GmcDetailsService gmcDetailsServiceMock;
  @Mock
  private GdcDetailsService gdcDetailsServiceMock;
  @Mock
  private RightToWorkService rightToWorkServiceMock;
  @Mock
  private TrainerApprovalService trainerApprovalServiceMock;

  @Captor
  private ArgumentCaptor<GdcDetails> gdcDetailsArgumentCaptor;
  @Captor
  private ArgumentCaptor<GmcDetails> gmcDetailsArgumentCaptor;
  @Captor
  private ArgumentCaptor<ContactDetails> contactDetailsArgumentCaptor;
  @Captor
  private ArgumentCaptor<PersonalDetails> personalDetailsArgumentCaptor;
  @Captor
  private ArgumentCaptor<RightToWork> rightToWorkArgumentCaptor;

  @Mock
  private ApplicationEventPublisher applicationEventPublisherMock;

  @Mock
  private PersonDtoMapper personDtoMapperMock;
  @Mock
  private TrainerApprovalDtoMapper trainerApprovalDtoMapperMock;

  @Before
  public void setup() {
    person = new Person();
    person.setPersonalDetails(new PersonalDetails());
    person.setId(PERSON_ID);

    LocalDate pm1Date = LocalDate.of(2018, 1, 2);
    LocalDate pm2Date = LocalDate.of(2018, 1, 1);
    LocalDate pm3Date = null;
    LocalDate pm4Date = LocalDate.of(2000, 1, 1);

    pm1 = new CurriculumMembership().programmeStartDate(pm1Date);
    pm2 = new CurriculumMembership().programmeStartDate(pm2Date);
    pm3 = new CurriculumMembership().programmeStartDate(pm3Date);
    pm4 = new CurriculumMembership().programmeStartDate(pm4Date);

    person.setProgrammeMemberships(Sets.newHashSet(pm1, pm2, pm3, pm4));

    personDTO = new PersonDTO();
    pmDTO1 = new ProgrammeMembershipDTO();
    pmDTO2 = new ProgrammeMembershipDTO();
    pmDTO3 = new ProgrammeMembershipDTO();
    pmDTO4 = new ProgrammeMembershipDTO();
    pmDTO1.setProgrammeStartDate(pm1Date);
    pmDTO2.setProgrammeStartDate(pm2Date);
    pmDTO3.setProgrammeStartDate(pm3Date);
    pmDTO4.setProgrammeStartDate(pm4Date);

    personDTO.setProgrammeMemberships(Sets.newHashSet(pmDTO1, pmDTO2, pmDTO3, pmDTO4));

  }

  @Test
  public void canLoggedInUserViewOrAmendShouldDoNothingWhenUserIsNotTrustAdmin() {
    Long postId = 1L;
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(false);

    testObj.canLoggedInUserViewOrAmend(postId);

    verify(permissionServiceMock, never()).getUsersTrustIds();
    verify(personRepositoryMock, never()).findPersonById(any());
  }

  @Test
  public void canLoggedInUserViewOrAmendShouldDoNothingWhenPostCannotBeFound() {
    Long postId = 1L;
    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(true);
    when(personRepositoryMock.findPersonById(postId)).thenReturn(Optional.empty());

    testObj.canLoggedInUserViewOrAmend(postId);

    verify(permissionServiceMock).getUsersTrustIds();
    verify(personRepositoryMock).findPersonById(postId);
  }

  @Test
  public void canLoggedInUserViewOrAmendShouldDoNothingWhenPostIsPartOfUsersTrusts() {

    PostTrust associatedTrust1 = new PostTrust();
    associatedTrust1.setTrustId(1L);
    PostTrust associatedTrust2 = new PostTrust();
    associatedTrust2.setTrustId(2L);

    Long personId = 1L;
    Person foundPerson = new Person();
    foundPerson.setId(personId);

    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(true);
    when(permissionServiceMock.getUsersTrustIds()).thenReturn(Sets.newHashSet(1L));
    when(personRepositoryMock.findPersonById(personId)).thenReturn(Optional.of(foundPerson));

    testObj.canLoggedInUserViewOrAmend(personId);

    verify(permissionServiceMock).getUsersTrustIds();
    verify(personRepositoryMock).findPersonById(personId);
  }

  @Test(expected = AccessUnauthorisedException.class)
  public void canLoggedInUserViewOrAmendShouldThrowExceptionWhenPostIsNotPartOfUsersTrusts() {

    PersonTrust associatedTrust1 = new PersonTrust();
    associatedTrust1.setTrustId(1L);
    PersonTrust associatedTrust2 = new PersonTrust();
    associatedTrust2.setTrustId(2L);

    Long personId = 1L;
    Person foundPerson = new Person();
    foundPerson.setId(personId);
    foundPerson.setAssociatedTrusts(Sets.newHashSet(associatedTrust1, associatedTrust2));

    when(permissionServiceMock.isUserTrustAdmin()).thenReturn(true);
    when(permissionServiceMock.getUsersTrustIds()).thenReturn(Sets.newHashSet(99999L));
    when(personRepositoryMock.findPersonById(personId)).thenReturn(Optional.of(foundPerson));

    try {
      testObj.canLoggedInUserViewOrAmend(personId);
    } catch (Exception e) {
      verify(permissionServiceMock).getUsersTrustIds();
      verify(personRepositoryMock).findPersonById(personId);
      throw e;
    }
  }

  @Test
  public void findPersonPMSortedShouldReturnDTOWithProgrammeMembershipsInDescendingOrder() {
    when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(person));
    when(personMapperMock.toDto(person)).thenReturn(personDTO);

    PersonDTO result = testObj.findPersonWithProgrammeMembershipsSorted(PERSON_ID);

    Set<ProgrammeMembershipDTO> programmeMemberships = result.getProgrammeMemberships();
    List<ProgrammeMembershipDTO> pmList = new ArrayList<>(programmeMemberships);

    Assert.assertTrue(
        pmList.get(0).getProgrammeStartDate().isAfter(pmList.get(1).getProgrammeStartDate()));
    Assert.assertTrue(
        pmList.get(1).getProgrammeStartDate().isAfter(pmList.get(2).getProgrammeStartDate()));
    Assert.assertNull(pmList.get(3).getProgrammeStartDate());
  }

  @Test
  public void findPersonV2WithProgrammeMembershipsSortedShouldReturnPersonV2WithoutQualifactions() {
    doReturn(personDTO).when(testObj).findPersonWithProgrammeMembershipsSorted(PERSON_ID);
    doNothing().when(testObj)
        .copyProperties(personDTOArgumentCaptor.capture(), personV2DTOArgumentCaptor.capture());

    PersonV2DTO result = testObj.findPersonV2WithProgrammeMembershipsSorted(PERSON_ID);

    PersonDTO capturedPersonDTO = personDTOArgumentCaptor.getValue();
    Assert.assertEquals(capturedPersonDTO, personDTO);

    PersonV2DTO capturedPersonV2DTO = personV2DTOArgumentCaptor.getValue();
    Assert.assertEquals(capturedPersonV2DTO, result);
  }

  @Test
  public void saveShouldSaveAndFlushPersonForAUserThatCanEditSensitiveData() {
    Person unsavedPerson = new Person(), savedPerson = new Person();
    savedPerson.setId(SAVED_PERSON_ID);

    when(personMapperMock.toEntity(unsavedPersonDTOMock)).thenReturn(unsavedPerson);
    when(permissionServiceMock.canEditSensitiveData()).thenReturn(true);
    when(personRepositoryMock.saveAndFlush(unsavedPerson)).thenReturn(savedPerson);
    when(personMapperMock.toDto(savedPerson)).thenReturn(savedPersonDTOMock);

    PersonDTO result = testObj.save(unsavedPersonDTOMock);

    Assert.assertEquals(savedPersonDTOMock, result);

    verify(personRepositoryMock, never()).findById(anyLong());
    verify(unsavedPersonDTOMock, never()).getPersonalDetails();
    verify(savedPersonDTOMock, never()).getPersonalDetails();
  }

  @Test
  public void saveShouldSaveAndFlushPersonForAUserThatCannotEditSensitiveDataAndPersonIsNotNew() {
    when(unsavedPersonDTOMock.getId()).thenReturn(PERSON_ID);
    when(unsavedPersonDTOMock.getPersonalDetails()).thenReturn(unsavedPersonDetailsDTOMock);
    when(savedPersonDTOMock.getPersonalDetails()).thenReturn(savedPersonDetailsDTOMock);

    Person unsavedPerson = new Person(), savedPerson = new Person(), originalPersonMock = mock(
        Person.class);

    PersonalDetails originalPersonalDetailsMock = mock(PersonalDetails.class);
    when(originalPersonalDetailsMock.getDisability()).thenReturn(DISABILITY_VALUE);
    when(originalPersonalDetailsMock.getDisabilityDetails()).thenReturn(DISABILITY_DETAILS_VALUE);
    when(originalPersonalDetailsMock.getReligiousBelief()).thenReturn(RELIGIOUS_BELIEF_VALUE);
    when(originalPersonalDetailsMock.getSexualOrientation()).thenReturn(SEXUAL_ORIENTATION_VALUE);

    when(personMapperMock.toEntity(unsavedPersonDTOMock)).thenReturn(unsavedPerson);
    when(permissionServiceMock.canEditSensitiveData()).thenReturn(false);
    when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(originalPersonMock));
    when(originalPersonMock.getPersonalDetails()).thenReturn(originalPersonalDetailsMock);

    when(personRepositoryMock.saveAndFlush(unsavedPerson)).thenReturn(savedPerson);
    when(personMapperMock.toDto(savedPerson)).thenReturn(savedPersonDTOMock);

    PersonDTO result = testObj.save(unsavedPersonDTOMock);

    Assert.assertEquals(savedPersonDTOMock, result);

    verify(personRepositoryMock).findById(PERSON_ID);
    verify(unsavedPersonDTOMock).getPersonalDetails();
    verify(savedPersonDTOMock).getPersonalDetails();
    verify(unsavedPersonDetailsDTOMock).setDisability(DISABILITY_VALUE);
    verify(unsavedPersonDetailsDTOMock).setDisabilityDetails(DISABILITY_DETAILS_VALUE);
    verify(unsavedPersonDetailsDTOMock).setReligiousBelief(RELIGIOUS_BELIEF_VALUE);
    verify(unsavedPersonDetailsDTOMock).setSexualOrientation(SEXUAL_ORIENTATION_VALUE);
    verify(savedPersonDetailsDTOMock).setDisability(null);
    verify(savedPersonDetailsDTOMock).setDisabilityDetails(null);
    verify(savedPersonDetailsDTOMock).setReligiousBelief(null);
    verify(savedPersonDetailsDTOMock).setSexualOrientation(null);
  }

  @Test
  public void createShouldCreateNewPersonAndReturnDTO() {
    GdcDetails gdcDetailsMock = mock(GdcDetails.class);
    GmcDetails gmcDetailsMock = mock(GmcDetails.class);
    ContactDetails contactDetailsMock = mock(ContactDetails.class);
    PersonalDetails personalDetailsMock = mock(PersonalDetails.class);
    RightToWork rightToWorkMock = mock(RightToWork.class);

    when(savedPersonMock.getId()).thenReturn(PERSON_ID);
    when(personMapperMock.toEntity(unsavedPersonDTOMock)).thenReturn(unsavedPersonMock);
    when(personRepositoryMock.save(unsavedPersonMock)).thenReturn(savedPersonMock);

    when(gdcDetailsRepositoryMock.save(gdcDetailsArgumentCaptor.capture()))
        .thenReturn(gdcDetailsMock);
    when(gmcDetailsRepositoryMock.save(gmcDetailsArgumentCaptor.capture()))
        .thenReturn(gmcDetailsMock);
    when(contactDetailsRepositoryMock.save(contactDetailsArgumentCaptor.capture()))
        .thenReturn(contactDetailsMock);
    when(personalDetailsRepositoryMock.save(personalDetailsArgumentCaptor.capture()))
        .thenReturn(personalDetailsMock);
    when(rightToWorkRepositoryMock.save(rightToWorkArgumentCaptor.capture()))
        .thenReturn(rightToWorkMock);
    when(personMapperMock.toDto(savedPersonMock)).thenReturn(savedPersonDTOMock);

    PersonDTO result = testObj.create(unsavedPersonDTOMock);

    Assert.assertSame(savedPersonDTOMock, result);

    GdcDetails gdcDetailsValue = gdcDetailsArgumentCaptor.getValue();
    Assert.assertEquals(PERSON_ID, gdcDetailsValue.getId());

    GmcDetails gmcDetailsValue = gmcDetailsArgumentCaptor.getValue();
    Assert.assertEquals(PERSON_ID, gmcDetailsValue.getId());

    ContactDetails contactDetailsValue = contactDetailsArgumentCaptor.getValue();
    Assert.assertEquals(PERSON_ID, contactDetailsValue.getId());

    PersonalDetails personalDetailsValue = personalDetailsArgumentCaptor.getValue();
    Assert.assertEquals(PERSON_ID, personalDetailsValue.getId());

    RightToWork rightToWorkValue = rightToWorkArgumentCaptor.getValue();
    Assert.assertEquals(PERSON_ID, rightToWorkValue.getId());

    verify(savedPersonMock).setGdcDetails(gdcDetailsMock);
    verify(savedPersonMock).setGmcDetails(gmcDetailsMock);
    verify(savedPersonMock).setContactDetails(contactDetailsMock);
    verify(savedPersonMock).setPersonalDetails(personalDetailsMock);
    verify(savedPersonMock).setRightToWork(rightToWorkMock);
  }

  @Test
  public void patchShouldPatchPersonsAndReturnDtos() {
    // Given.
    PersonDTO personDto = new PersonDTO();
    personDto.setId(PERSON_ID);

    List<PersonDTO> personDTOList = Lists.newArrayList(personDto);

    when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(person));
    when(permissionServiceMock.canViewSensitiveData()).thenReturn(true);
    when(permissionServiceMock.canEditSensitiveData()).thenReturn(true);
    when(personMapperMock.toDto(person)).thenReturn(personDto);
    when(personMapperMock.toEntity(personDto)).thenReturn(person);
    when(personRepositoryMock.saveAndFlush(person)).thenReturn(person);

    // When.
    List<PersonDTO> returnedPersonDtoList = testObj.patch(personDTOList);

    // Then.
    verify(personDtoMapperMock).copyIfNotNull(any(), any());
    verify(personalDetailsServiceMock).save(personDto.getPersonalDetails());
    verify(contactDetailsServiceMock).save(personDto.getContactDetails());
    verify(gmcDetailsServiceMock).save(personDto.getGmcDetails());
    verify(gdcDetailsServiceMock).save(personDto.getGdcDetails());
    verify(rightToWorkServiceMock).save(personDto.getRightToWork());

    Assert.assertEquals(1, returnedPersonDtoList.size());
    Assert.assertEquals(0, returnedPersonDtoList.get(0).getMessageList().size());
  }
}
