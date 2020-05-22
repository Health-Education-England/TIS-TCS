package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RunWith(MockitoJUnitRunner.class)
public class PersonValidatorTest {

  public static final String PUBLIC_HEALTH_NUMBER = "ABC123";
  public static final String UNKNOWN_PUBLIC_HEALTH_NUMBER = "UNKNOWN";
  public static final long PERSON_ID = 123L;
  public static final long DIFFERENT_PERSON_ID = 999L;

  @InjectMocks
  private PersonValidator testObj;

  @Mock
  private ReferenceService referenceService;
  @Mock
  private PersonRepository personRepositoryMock;
  @Mock
  private PersonDTO personDTOMock;
  @Mock
  private Person personMock1, personMock2;

  @Before
  public void setup() {
    when(personDTOMock.getId()).thenReturn(PERSON_ID);
    when(personDTOMock.getPublicHealthNumber()).thenReturn(PUBLIC_HEALTH_NUMBER);
    when(personMock1.getId()).thenReturn(DIFFERENT_PERSON_ID);
  }

  @Test(expected = MethodArgumentNotValidException.class)
  public void validateShouldThrowExceptionWhenDifferentPersonWithPublicHealthNumberAlreadyExists()
      throws MethodArgumentNotValidException {
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1));
    try {
      testObj.validate(personDTOMock);
    } catch (MethodArgumentNotValidException e) {
      verify(personRepositoryMock).findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER);
      Assert.assertTrue(exceptionContainsFieldError(e, "publicHealthNumber"));
      throw e;
    }
  }


  @Test(expected = MethodArgumentNotValidException.class)
  public void validateShouldThrowExceptionWhenThereAreMultiplePeopleWithSamePublicHealthNumber()
      throws MethodArgumentNotValidException {
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1, personMock2));
    try {
      testObj.validate(personDTOMock);
    } catch (MethodArgumentNotValidException e) {
      verify(personRepositoryMock).findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER);
      Assert.assertTrue(exceptionContainsFieldError(e, "publicHealthNumber"));
      throw e;
    }
  }

  @Test(expected = MethodArgumentNotValidException.class)
  public void validateShouldThrowExceptionDuringCreatePublicHealthNumberAlreadyExists()
      throws MethodArgumentNotValidException {
    when(personDTOMock.getId()).thenReturn(null);
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1, personMock2));
    try {
      testObj.validate(personDTOMock);
    } catch (MethodArgumentNotValidException e) {
      verify(personRepositoryMock).findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER);
      Assert.assertTrue(exceptionContainsFieldError(e, "publicHealthNumber"));
      throw e;
    }
  }

  @Test
  public void validationSkippedIfPublicHealthNumberIsUnknownOrNA()
      throws MethodArgumentNotValidException {
    when(personDTOMock.getPublicHealthNumber()).thenReturn(UNKNOWN_PUBLIC_HEALTH_NUMBER);
    testObj.validate(personDTOMock);
    verify(personRepositoryMock, never()).findByPublicHealthNumber(anyString());
  }

  @Test(expected = MethodArgumentNotValidException.class)
  public void shouldThrowExceptionWhenRolesNotExist() throws MethodArgumentNotValidException {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setRole("role1;role2");

    Map<String, Boolean> roleToExists = new HashMap<>();
    roleToExists.put("role1", true);
    roleToExists.put("role2", false);
    when(referenceService.rolesExist(any(), eq(true))).thenReturn(roleToExists);

    try {
      // When.
      testObj.validate(dto);
    } catch (MethodArgumentNotValidException e) {
      // Then
      BindingResult result = e.getBindingResult();
      assertThat("Unexpected object name.", result.getObjectName(),
          is(PersonDTO.class.getSimpleName()));
      assertThat("Unexpected target object.", result.getTarget(), is(dto));

      FieldError fieldError = new FieldError(PersonDTO.class.getSimpleName(), "role",
          "role 'role2' did not match a reference value.");
      assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));

      throw e;
    }
  }

  @Test
  public void shouldNotThrowExceptionWhenRolesExist() throws MethodArgumentNotValidException {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setRole("role1;role2");

    Map<String, Boolean> roleToExists = new HashMap<>();
    roleToExists.put("role1", true);
    roleToExists.put("role2", true);
    when(referenceService.rolesExist(any(), eq(true))).thenReturn(roleToExists);

    // When.
    testObj.validate(dto);
  }

  private boolean exceptionContainsFieldError(MethodArgumentNotValidException e, String field) {
    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    boolean contains = false;
    for (FieldError fieldError : fieldErrors) {
      if (fieldError.getField().equalsIgnoreCase(field)) {
        contains = true;
        break;
      }
    }
    return contains;
  }
}
