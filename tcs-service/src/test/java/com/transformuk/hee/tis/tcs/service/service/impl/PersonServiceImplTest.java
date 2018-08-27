package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.PersonV2DTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonTrust;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.PersonMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceImplTest {

  public static final long PERSON_ID = 1L;

  @Spy
  @InjectMocks
  private PersonServiceImpl testObj;

  @Mock
  private PermissionService permissionServiceMock;
  @Mock
  private PersonRepository personRepositoryMock;
  @Mock
  private PersonMapper personMapperMock;
  @Captor
  private ArgumentCaptor<PersonDTO> personDTOArgumentCaptor;
  @Captor
  private ArgumentCaptor<PersonV2DTO> personV2DTOArgumentCaptor;

  private Person person;
  private ProgrammeMembership pm1, pm2, pm3, pm4;
  private PersonDTO personDTO;
  private ProgrammeMembershipDTO pmDTO1, pmDTO2, pmDTO3, pmDTO4;

  @Before
  public void setup() {
    person = new Person();
    person.setPersonalDetails(new PersonalDetails());
    person.setId(PERSON_ID);

    LocalDate pm1Date = LocalDate.of(2018, 1, 2);
    LocalDate pm2Date = LocalDate.of(2018, 1, 1);
    LocalDate pm3Date = null;
    LocalDate pm4Date = LocalDate.of(2000, 1, 1);

    pm1 = new ProgrammeMembership().programmeStartDate(pm1Date);
    pm2 = new ProgrammeMembership().programmeStartDate(pm2Date);
    pm3 = new ProgrammeMembership().programmeStartDate(pm3Date);
    pm4 = new ProgrammeMembership().programmeStartDate(pm4Date);

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
    when(personRepositoryMock.findOne(PERSON_ID)).thenReturn(person);
    when(personMapperMock.toDto(person)).thenReturn(personDTO);

    PersonDTO result = testObj.findPersonWithProgrammeMembershipsSorted(PERSON_ID);

    Set<ProgrammeMembershipDTO> programmeMemberships = result.getProgrammeMemberships();
    List<ProgrammeMembershipDTO> pmList = new ArrayList<>(programmeMemberships);

    Assert.assertTrue(pmList.get(0).getProgrammeStartDate().isAfter(pmList.get(1).getProgrammeStartDate()));
    Assert.assertTrue(pmList.get(1).getProgrammeStartDate().isAfter(pmList.get(2).getProgrammeStartDate()));
    Assert.assertNull(pmList.get(3).getProgrammeStartDate());
  }

  @Test
  public void findPersonV2WithProgrammeMembershipsSortedShouldReturnPersonV2WithoutQualifactions() {
    doReturn(personDTO).when(testObj).findPersonWithProgrammeMembershipsSorted(PERSON_ID);
    doNothing().when(testObj).copyProperties(personDTOArgumentCaptor.capture(), personV2DTOArgumentCaptor.capture());

    PersonV2DTO result = testObj.findPersonV2WithProgrammeMembershipsSorted(PERSON_ID);

    PersonDTO capturedPersonDTO = personDTOArgumentCaptor.getValue();
    Assert.assertEquals(capturedPersonDTO, personDTO);

    PersonV2DTO capturedPersonV2DTO = personV2DTOArgumentCaptor.getValue();
    Assert.assertEquals(capturedPersonV2DTO, result);
  }
}