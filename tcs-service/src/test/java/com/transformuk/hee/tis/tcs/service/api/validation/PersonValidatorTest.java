package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RunWith(MockitoJUnitRunner.class)
public class PersonValidatorTest {

  public static final String PUBLIC_HEALTH_NUMBER = "ABC123";
  public static final String UNKNOWN_PUBLIC_HEALTH_NUMBER = "UNKNOWN";
  public static final long PERSON_ID = 123L;
  public static final long DIFFERENT_PERSON_ID = 999L;
  public static final String PERSON_ROLE = "VALID_ROLE";

  @InjectMocks
  @Spy
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
    when(personDTOMock.getRole()).thenReturn(PERSON_ROLE);
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

    RoleDTO roleDto = new RoleDTO();
    roleDto.setCode("VALID_ROLE");
    when(referenceService.getAllRoles()).thenReturn(Sets.newHashSet(roleDto));

    testObj.validate(personDTOMock);
    verify(personRepositoryMock, never()).findByPublicHealthNumber(anyString());
  }

  @Test
  public void validatePersonShouldThrowExceptionWhenRoleIsNull()
      throws MethodArgumentNotValidException {
    when(personDTOMock.getRole()).thenReturn(null);
    try {
      testObj.validate(personDTOMock);
    } catch (MethodArgumentNotValidException e) {
      Assert.assertTrue(exceptionContainsFieldError(e, "role"));
    }
  }

  @Test
  public void validatePersonShouldThrowExceptionWhenRoleIsEmptyString()
      throws MethodArgumentNotValidException {
    when(personDTOMock.getRole()).thenReturn("");
    try {
      testObj.validate(personDTOMock);
    } catch (MethodArgumentNotValidException e) {
      Assert.assertTrue(exceptionContainsFieldError(e, "role"));
    }
  }

  @Test
  public void bulkShouldGetErrorWhenRoleIsEmptyAndNotExistsInDB() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setId(PERSON_ID);
    dto.setRole(null);

    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    Person person = new Person();
    person.setId(PERSON_ID);
    person.setRole(null);
    when(personRepositoryMock.existsById(PERSON_ID)).thenReturn(true);
    when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(person));
    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should contain 1 error", dtoList.get(0).getMessageList().size(), is(1));
    assertThat("Unexpected error message", dtoList.get(0).getMessageList(),
        hasItem("Existing person record does not have a role, role is required."));
  }

  @Test
  public void bulkShouldNotGetErrorWhenRoleIsEmptyButExistsInDB() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setId(PERSON_ID);
    dto.setRole(null);

    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    Person person = new Person();
    person.setId(PERSON_ID);
    person.setRole(PERSON_ROLE);
    when(personRepositoryMock.existsById(PERSON_ID)).thenReturn(true);
    when(personRepositoryMock.findById(PERSON_ID)).thenReturn(Optional.of(person));
    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should not contain any errors", dtoList.get(0).getMessageList().size(), is(0));
  }

  @Test
  public void bulkShouldGetErrorWhenRoleNotExists() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setRole("role1;role2");
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    RoleDTO role1 = new RoleDTO();
    role1.setCode("role1");
    when(referenceService.getAllRoles()).thenReturn(Sets.newHashSet(role1));

    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should contain 1 error", dtoList.get(0).getMessageList().size(), is(1));
    assertThat("Unexpected error message", dtoList.get(0).getMessageList(),
        hasItem("Role 'role2' did not match a reference value."));
  }

  @Test
  public void bulkShouldNotGetErrorWhenRoleExists() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setRole("role1 ; role2,");
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    RoleDTO role1 = new RoleDTO();
    role1.setCode("role1");
    role1.setId(0L);
    RoleDTO role2 = new RoleDTO();
    role2.setCode("role2");
    role2.setId(1L);
    when(referenceService.getAllRoles()).thenReturn(Sets.newHashSet(role1, role2));

    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should not contain any errors",
        dtoList.get(0).getMessageList().size(), is(0));
  }

  /**
   * When role exists but is inaccurately spelt (different casing or spacing), fix it before
   * accepting the row
   */
  @Test
  public void bulkShouldFixWhenRoleExistsButIsInaccurate() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setRole("role1 ; ROle  2,");
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    RoleDTO role1 = new RoleDTO();
    role1.setCode("role1");
    role1.setId(0L);
    RoleDTO role2 = new RoleDTO();
    role2.setCode("role2");
    role2.setId(1L);
    when(referenceService.getAllRoles()).thenReturn(Sets.newHashSet(role1, role2));

    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should not contain any errors", dtoList.get(0).getMessageList().size(), is(0));
  }

  @Test
  public void bulkShouldGetErrorWhenPersonNotExists() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setId(1L);
    dto.setRole(PERSON_ROLE);
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setCode(PERSON_ROLE);
    when(referenceService.getAllRoles()).thenReturn(Sets.newHashSet(roleDTO));
    when(personRepositoryMock.existsById(1L)).thenReturn(false);

    testObj.validateForBulk(dtoList);

    assertThat("should contain 1 error", dtoList.get(0).getMessageList().size(), is(1));
    assertThat("Unexpected error message", dtoList.get(0).getMessageList(),
        hasItem("Person with id 1 does not exist"));
  }

  @Test
  public void bulkShouldNotGetErrorWhenPersonExists() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setId(1L);
    dto.setRole(PERSON_ROLE);
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setCode(PERSON_ROLE);
    when(referenceService.getAllRoles()).thenReturn(Sets.newHashSet(roleDTO));
    when(personRepositoryMock.existsById(1L)).thenReturn(true);

    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should not contain any errors", dtoList.get(0).getMessageList().size(), is(0));
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

    RoleDTO roleDto = new RoleDTO();
    roleDto.setCode("role1");
    when(referenceService.getAllRoles()).thenReturn(Sets.newHashSet(roleDto));

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

    RoleDTO roleDto = new RoleDTO();
    roleDto.setCode("VALID_ROLE");
    when(referenceService.getAllRoles()).thenReturn(Sets.newHashSet(roleDto));

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

  @Test
  public void roleCheckShouldHandleCommaSeparator() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setRole("role1 , role2,role3,");
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    RoleDTO role1 = new RoleDTO();
    role1.setCode("role1");
    role1.setId(0L);
    RoleDTO role2 = new RoleDTO();
    role2.setCode("role2");
    role2.setId(1L);
    RoleDTO role3 = new RoleDTO();
    role3.setCode("role3");
    role3.setId(2L);
    when(referenceService.getAllRoles()).thenReturn(Sets.newHashSet(role1, role2, role3));

    ArgumentCaptor<List<PersonDTO>> personsListCaptor = ArgumentCaptor.forClass(List.class);

    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should not contain any errors",
        dtoList.get(0).getMessageList().size(), is(0));

    verify(testObj).validateForBulk(personsListCaptor.capture());
    List<String> splitRoles = Arrays.asList(
        personsListCaptor.getValue().get(0).getRole().split(","));

    assertThat("Unexpected roles.", splitRoles, hasItems("role1", "role2", "role3"));
  }

  @Test
  public void roleCheckShouldHandleSemiColonSeparator() throws MethodArgumentNotValidException {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setRole("role1 ; role2;role3;");
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    RoleDTO role1 = new RoleDTO();
    role1.setCode("role1");
    role1.setId(0L);
    RoleDTO role2 = new RoleDTO();
    role2.setCode("role2");
    role2.setId(1L);
    RoleDTO role3 = new RoleDTO();
    role3.setCode("role3");
    role3.setId(2L);
    when(referenceService.getAllRoles()).thenReturn(Sets.newHashSet(role1, role2, role3));

    ArgumentCaptor<List<PersonDTO>> personsListCaptor = ArgumentCaptor.forClass(List.class);

    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should not contain any errors",
        dtoList.get(0).getMessageList().size(), is(0));

    verify(testObj).validateForBulk(personsListCaptor.capture());
    List<String> splitRoles = Arrays.asList(
        personsListCaptor.getValue().get(0).getRole().split(","));

    assertThat("Unexpected roles.", splitRoles, hasItems("role1", "role2", "role3"));
  }

  @Test
  public void roleCheckShouldHandleMixedSeparators() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setRole("role1 ; role2,role3,");
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    RoleDTO role1 = new RoleDTO();
    role1.setCode("role1");
    role1.setId(0L);
    RoleDTO role2 = new RoleDTO();
    role2.setCode("role2");
    role2.setId(1L);
    RoleDTO role3 = new RoleDTO();
    role3.setCode("role3");
    role3.setId(2L);
    when(referenceService.getAllRoles()).thenReturn(Sets.newHashSet(role1, role2, role3));

    ArgumentCaptor<List<PersonDTO>> personsListCaptor = ArgumentCaptor.forClass(List.class);

    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should not contain any errors",
        dtoList.get(0).getMessageList().size(), is(0));

    verify(testObj).validateForBulk(personsListCaptor.capture());
    List<String> splitRoles = Arrays.asList(
        personsListCaptor.getValue().get(0).getRole().split(","));

    assertThat("Unexpected roles.", splitRoles, hasItems("role1", "role2", "role3"));
  }
}
