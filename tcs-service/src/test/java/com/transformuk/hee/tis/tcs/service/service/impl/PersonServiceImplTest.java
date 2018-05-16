package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.service.exception.AccessUnauthorisedException;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonTrust;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceImplTest {

  @InjectMocks
  private PersonServiceImpl testObj;

  @Mock
  private PermissionService permissionServiceMock;
  @Mock
  private PersonRepository personRepositoryMock;

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
}