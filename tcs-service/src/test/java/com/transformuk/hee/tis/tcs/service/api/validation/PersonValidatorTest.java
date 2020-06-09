package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.RoleCategoryDTO;
import com.transformuk.hee.tis.reference.api.dto.RoleDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
  @Mock
  private ContactDetailsValidator contactDetailsValidatorMock;
  @Mock
  private GdcDetailsValidator gdcDetailsValidatorMock;
  @Mock
  private GmcDetailsValidator gmcDetailsValidatorMock;
  @Mock
  private PersonalDetailsValidator personalDetailsValidatorMock;
  @Mock
  private RightToWorkValidator rightToWorkValidatorMock;
  @Mock
  private TrainerApprovalValidator trainerApprovalValidatorMock;

  @Before
  public void setup() {
    when(personDTOMock.getId()).thenReturn(PERSON_ID);
    when(personDTOMock.getPublicHealthNumber()).thenReturn(PUBLIC_HEALTH_NUMBER);
  }

  @Test(expected = MethodArgumentNotValidException.class)
  public void validateShouldThrowExceptionWhenDifferentPersonWithPublicHealthNumberAlreadyExists()
      throws MethodArgumentNotValidException {
    when(personMock1.getId()).thenReturn(DIFFERENT_PERSON_ID);
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
    when(personMock1.getId()).thenReturn(DIFFERENT_PERSON_ID);
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
    when(personRepositoryMock.existsById(123L)).thenReturn(true);
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
    dto.setRole("role1 ; role2,");

    Map<String, Boolean> roleToExists = new HashMap<>();
    roleToExists.put("role1", true);
    roleToExists.put("role2", true);
    when(referenceService.rolesExist(any(), eq(true))).thenReturn(roleToExists);

    // When, then.
    assertDoesNotThrow(() -> testObj.validate(dto));
  }

  @Test(expected = MethodArgumentNotValidException.class)
  public void shouldThrowExceptionWhenPersonNotExist() throws MethodArgumentNotValidException {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setId(1L);
    when(personRepositoryMock.existsById(1L)).thenReturn(false);

    // When.
    try {
      // When.
      testObj.validate(dto);
    } catch (MethodArgumentNotValidException e) {
      // Then
      BindingResult result = e.getBindingResult();
      assertThat("Unexpected object name.", result.getObjectName(),
          is(PersonDTO.class.getSimpleName()));
      assertThat("Unexpected target object.", result.getTarget(), is(dto));

      FieldError fieldError = new FieldError(PersonDTO.class.getSimpleName(), "person",
          "Person with id 1 does not exist");
      assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));

      throw e;
    }
  }

  @Test
  public void shouldNotThrowExceptionWhenPersonExist() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setId(1L);
    when(personRepositoryMock.existsById(1L)).thenReturn(true);

    // When, then.
    assertDoesNotThrow(() -> testObj.validate(dto));
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

  @Test
  public void shouldReturnEmptyFieldErrorsWhenValidateForBulkIsOkay() {
    // Given.
    when(personMock1.getId()).thenReturn(PERSON_ID);
    when(personRepositoryMock.existsById(123L)).thenReturn(true);
    when(personDTOMock.getRole()).thenReturn("role1");
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1));

    Map<String, Boolean> roleToExists = new HashMap<>();
    roleToExists.put("role1", true);
    when(referenceService.rolesExist(any(), eq(true))).thenReturn(roleToExists);

    RoleDTO roleDto = new RoleDTO();
    RoleCategoryDTO roleCategoryDto = new RoleCategoryDTO();
    roleCategoryDto.setId(2L);
    roleDto.setRoleCategory(roleCategoryDto);
    when(referenceService.findRolesIn("role1")).thenReturn(Lists.newArrayList(roleDto));

    when(contactDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(gdcDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(gmcDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(personalDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(rightToWorkValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());

    // When.
    testObj.validateForBulk(Lists.newArrayList(personDTOMock));
    // Then.
    verify(personDTOMock, times(0)).addMessage(anyString());
  }

  @Test
  public void shouldReturnAllFieldErrorsWhenValidateForBulkGetsErrors() {
    // Given.
    when(personMock1.getId()).thenReturn(PERSON_ID);
    when(personMock2.getId()).thenReturn(DIFFERENT_PERSON_ID);
    when(personRepositoryMock.existsById(PERSON_ID)).thenReturn(false); // first error
    when(personDTOMock.getRole()).thenReturn("role1");
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1, personMock2)); // second error

    Map<String, Boolean> roleToExists = new HashMap<>();
    roleToExists.put("role1", true);
    when(referenceService.rolesExist(any(), eq(true))).thenReturn(roleToExists);

    RoleDTO roleDto = new RoleDTO();
    RoleCategoryDTO roleCategoryDto = new RoleCategoryDTO();
    roleCategoryDto.setId(3L); // third error
    roleDto.setRoleCategory(roleCategoryDto);
    when(referenceService.findRolesIn("role1")).thenReturn(Lists.newArrayList(roleDto));
    TrainerApprovalDTO trainerApprovalDto = new TrainerApprovalDTO();
    when(personDTOMock.getTrainerApprovals()).thenReturn(Sets.newHashSet(trainerApprovalDto));

    when(contactDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(gdcDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(gmcDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(personalDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(rightToWorkValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(trainerApprovalValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());

    // When.
    testObj.validateForBulk(Lists.newArrayList(personDTOMock));
    // Then.
    verify(personDTOMock, times(3)).addMessage(anyString());
  }

  @Test
  public void ShouldValidateTrainerApprovalWhenRoleNotUpdated() {
    // Given.
    when(personMock1.getId()).thenReturn(PERSON_ID);
    when(personRepositoryMock.existsById(PERSON_ID)).thenReturn(true);
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1));
    Person existingPerson = new Person();
    existingPerson.setRole("role1");
    when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(existingPerson));

    Map<String, Boolean> roleToExists = new HashMap<>();
    roleToExists.put("role1", true);

    RoleDTO roleDto = new RoleDTO();
    RoleCategoryDTO roleCategoryDto = new RoleCategoryDTO();
    roleCategoryDto.setId(3L);
    roleDto.setRoleCategory(roleCategoryDto);
    when(referenceService.findRolesIn("role1")).thenReturn(Lists.newArrayList(roleDto));
    TrainerApprovalDTO trainerApprovalDto = new TrainerApprovalDTO();
    when(personDTOMock.getTrainerApprovals()).thenReturn(Sets.newHashSet(trainerApprovalDto));

    when(contactDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(gdcDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(gmcDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(personalDetailsValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(rightToWorkValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());
    when(trainerApprovalValidatorMock.validateForBulk(any())).thenReturn(Lists.emptyList());

    // When.
    testObj.validateForBulk(Lists.newArrayList(personDTOMock));
    // Then.
    verify(personDTOMock, times(1)).addMessage(
        "To have a Trainer Approval, the role should contain at least one of 'Educational supervisors/Clinical supervisors/Leave approvers' categories");
  }

}
