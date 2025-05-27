package com.transformuk.hee.tis.tcs.service.api.validation;

import static com.transformuk.hee.tis.tcs.service.api.validation.PersonValidator.FIELD_NAME_PH_NUMBER;
import static com.transformuk.hee.tis.tcs.service.api.validation.PersonValidator.FIELD_NAME_ROLE;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
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
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class PersonValidatorTest {

  public static final String PUBLIC_HEALTH_NUMBER = "ABC123";
  public static final String PUBLIC_HEALTH_NUMBER_WHITESPACE = "ABC 123";
  public static final String UNKNOWN_PUBLIC_HEALTH_NUMBER = "UNKNOWN";
  public static final long PERSON_ID = 123L;
  public static final long DIFFERENT_PERSON_ID = 999L;
  public static final String PERSON_ROLE = "PERSON_ROLE";

  private static final String DTO_NAME = PersonDTO.class.getSimpleName();

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

  @BeforeEach
  void setup() {
    lenient().when(personDTOMock.getId()).thenReturn(PERSON_ID);
    lenient().when(personDTOMock.getPublicHealthNumber()).thenReturn(PUBLIC_HEALTH_NUMBER);
    lenient().when(personDTOMock.getRole()).thenReturn(PERSON_ROLE);
  }

  @Test
  void validateShouldThrowExceptionWhenDifferentPersonWithPublicHealthNumberAlreadyExists() {
    when(personMock1.getId()).thenReturn(DIFFERENT_PERSON_ID);
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1));
    MethodArgumentNotValidException exception =
        assertThrows(MethodArgumentNotValidException.class,
            () -> testObj.validate(personDTOMock, null, Create.class));
    verify(personRepositoryMock).findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER);
    assertTrue(exceptionContainsFieldError(exception, "publicHealthNumber"));
  }

  @Test
  void validateShouldThrowExceptionWhenThereAreMultiplePeopleWithSamePublicHealthNumber() {
    when(personMock1.getId()).thenReturn(DIFFERENT_PERSON_ID);
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1, personMock2));
    MethodArgumentNotValidException exception = assertThrows(MethodArgumentNotValidException.class,
        () -> testObj.validate(personDTOMock, null, Create.class));
    verify(personRepositoryMock).findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER);
    assertTrue(exceptionContainsFieldError(exception, "publicHealthNumber"));
  }

  @Test
  void validateShouldThrowExceptionDuringCreatePublicHealthNumberAlreadyExists() {
    when(personDTOMock.getId()).thenReturn(null);
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1, personMock2));
    MethodArgumentNotValidException exception =
        assertThrows(MethodArgumentNotValidException.class,
            () -> testObj.validate(personDTOMock, null, Create.class));
    verify(personRepositoryMock).findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER);
    assertTrue(exceptionContainsFieldError(exception, "publicHealthNumber"));
  }

  @Test
  void validationSkippedIfPublicHealthNumberIsUnknownOrNA()
      throws MethodArgumentNotValidException {
    when(personDTOMock.getPublicHealthNumber()).thenReturn(UNKNOWN_PUBLIC_HEALTH_NUMBER);
    testObj.validate(personDTOMock, null, Create.class);
    verify(personRepositoryMock, never()).findByPublicHealthNumber(anyString());
  }

  @Test
  void validatePersonShouldThrowExceptionWhenRoleIsNull() {
    when(personDTOMock.getRole()).thenReturn(null);
    try {
      testObj.validate(personDTOMock, null, Create.class);
    } catch (MethodArgumentNotValidException e) {
      assertTrue(exceptionContainsFieldError(e, "role"));
    }
  }

  @Test
  void validatePersonShouldThrowExceptionWhenRoleIsEmptyString() {
    when(personDTOMock.getRole()).thenReturn("");
    try {
      testObj.validate(personDTOMock, null, Create.class);
    } catch (MethodArgumentNotValidException e) {
      assertTrue(exceptionContainsFieldError(e, "role"));
    }
  }

  @Test
  void bulkShouldGetErrorWhenRoleIsEmptyAndNotExistsInDB() {
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
  void bulkShouldNotGetErrorWhenRoleIsEmptyButExistsInDB() {
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
  void bulkShouldGetErrorWhenRoleNotExists() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setRole("role1;role2");
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    Map<String, String> roleToMatches = new HashMap<>();
    roleToMatches.put("role1", "role1");
    roleToMatches.put("role2", "");
    when(referenceService.rolesMatch(any(), eq(true))).thenReturn(roleToMatches);

    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should contain 1 error", dtoList.get(0).getMessageList().size(), is(1));
    assertThat("Unexpected error message", dtoList.get(0).getMessageList(),
        hasItem("Role 'role2' did not match a reference value."));
  }

  @Test
  void bulkShouldNotGetErrorWhenRoleExists() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setRole("role1 ; role2,");
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    Map<String, String> roleToMatches = new HashMap<>();
    roleToMatches.put("role1", "role1");
    roleToMatches.put("role2", "role2");
    when(referenceService.rolesMatch(any(), eq(true))).thenReturn(roleToMatches);

    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should not contain any errors", dtoList.get(0).getMessageList().size(), is(0));
  }

  @Test
  void bulkShouldGetErrorWhenPersonNotExists() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setId(1L);
    dto.setRole(PERSON_ROLE);
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);
    when(personRepositoryMock.existsById(1L)).thenReturn(false);

    testObj.validateForBulk(dtoList);

    assertThat("should contain 1 error", dtoList.get(0).getMessageList().size(), is(1));
    assertThat("Unexpected error message", dtoList.get(0).getMessageList(),
        hasItem("Person with id 1 does not exist"));
  }

  @Test
  void bulkShouldNotGetErrorWhenPersonExists() {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setId(1L);
    dto.setRole(PERSON_ROLE);
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);
    when(personRepositoryMock.existsById(1L)).thenReturn(true);

    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should not contain any errors", dtoList.get(0).getMessageList().size(), is(0));
  }

  private boolean exceptionContainsFieldError(MethodArgumentNotValidException e, String field) {
    List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
    for (FieldError fieldError : fieldErrors) {
      if (fieldError.getField().equalsIgnoreCase(field)) {
        return true;
      }
    }
    return false;
  }

  @Test
  void shouldReturnEmptyFieldErrorsWhenValidateForBulkIsOkay() {
    // Given.
    when(personMock1.getId()).thenReturn(PERSON_ID);
    when(personRepositoryMock.existsById(123L)).thenReturn(true);
    when(personDTOMock.getRole()).thenReturn("role1");
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1));

    Map<String, String> roleToMatches = new HashMap<>();
    roleToMatches.put("role1", "role1");
    when(referenceService.rolesMatch(any(), eq(true))).thenReturn(roleToMatches);

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
  void shouldReturnAllFieldErrorsWhenValidateForBulkGetsErrors() {
    // Given.
    when(personMock1.getId()).thenReturn(PERSON_ID);
    when(personMock2.getId()).thenReturn(DIFFERENT_PERSON_ID);
    when(personRepositoryMock.existsById(PERSON_ID)).thenReturn(false); // first error
    when(personDTOMock.getRole()).thenReturn("role1");
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1, personMock2)); // second error

    Map<String, String> roleToMatches = new HashMap<>();
    roleToMatches.put("role1", "role1");
    when(referenceService.rolesMatch(any(), eq(true))).thenReturn(roleToMatches);

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
  void shouldValidateTrainerApprovalWhenRoleNotUpdated() {
    // Given.
    when(personMock1.getId()).thenReturn(PERSON_ID);
    when(personRepositoryMock.existsById(PERSON_ID)).thenReturn(true);
    when(personRepositoryMock.findByPublicHealthNumber(PUBLIC_HEALTH_NUMBER))
        .thenReturn(Lists.newArrayList(personMock1));
    Person existingPerson = new Person();
    existingPerson.setRole("role1");

    RoleDTO roleDto = new RoleDTO();
    RoleCategoryDTO roleCategoryDto = new RoleCategoryDTO();
    roleCategoryDto.setId(3L);
    roleDto.setRoleCategory(roleCategoryDto);
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
  void roleCheckShouldHandleCommaSeparator() {
    testRolesSplit("role1 , role2,role3,");
  }

  @Test
  void roleCheckShouldHandleSemiColonSeparator() {
    testRolesSplit("role1 ; role2;role3;");
  }

  @Test
  void roleCheckShouldHandleMixedSeparators() {
    testRolesSplit("role1 ; role2,role3,");
  }

  private void testRolesSplit(String roleList) {
    // Given.
    PersonDTO dto = new PersonDTO();
    dto.setRole(roleList);
    List<PersonDTO> dtoList = new ArrayList<>();
    dtoList.add(dto);

    Map<String, String> roleToMatches = new HashMap<>();
    roleToMatches.put("role1", "role1");
    roleToMatches.put("role2", "role2");
    roleToMatches.put("role3", "role3");

    ArgumentCaptor<List<String>> rolesCaptor = ArgumentCaptor.forClass(List.class);
    when(referenceService.rolesMatch(rolesCaptor.capture(), eq(true))).thenReturn(roleToMatches);

    // When.
    testObj.validateForBulk(dtoList);
    // Then.
    assertThat("should not contain any errors", dtoList.get(0).getMessageList().size(), is(0));

    List<String> splitRoles = rolesCaptor.getValue();
    assertThat("Unexpected roles.", splitRoles, hasItems("role1", "role2", "role3"));
  }

  @Test
  void shouldNotThrowExceptionWhenUpdateWithValidFields() {
    PersonDTO dto = new PersonDTO();
    dto.setRole(PERSON_ROLE);
    dto.setPublicHealthNumber(PUBLIC_HEALTH_NUMBER);

    PersonDTO originalDto = new PersonDTO();

    Map<String, String> rolesMatch = new HashMap<>();
    rolesMatch.put(PERSON_ROLE, PERSON_ROLE);
    when(referenceService.rolesMatch(Lists.newArrayList(PERSON_ROLE), true))
        .thenReturn(rolesMatch);

    assertDoesNotThrow(() -> testObj.validate(dto, originalDto, Update.class));
  }

  @Test
  void shouldThrowExceptionWhenUpdateWithInvalidFields() {
    PersonDTO dto = new PersonDTO();
    dto.setRole(PERSON_ROLE);
    dto.setPublicHealthNumber(PUBLIC_HEALTH_NUMBER_WHITESPACE);

    PersonDTO originalDto = new PersonDTO();

    Map<String, String> rolesMatch = new HashMap<>();
    rolesMatch.put(PERSON_ROLE, null);
    when(referenceService.rolesMatch(Lists.newArrayList(PERSON_ROLE), true))
        .thenReturn(rolesMatch);

    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> testObj.validate(dto, originalDto, Update.class));

    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        Matchers.is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), Matchers.is(dto));

    FieldError fieldError1 = new FieldError(DTO_NAME, FIELD_NAME_ROLE,
        String.format("Role '%s' did not match a reference value.", PERSON_ROLE));
    FieldError fieldError2 = new FieldError(DTO_NAME, FIELD_NAME_PH_NUMBER,
        "publicHealthNumber should not contain any whitespaces");

    assertThat("Unexpected error count.", result.getFieldErrors().size(), Matchers.is(2));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        Matchers.hasItems(fieldError1, fieldError2));
  }

  @Test
  void shouldNotThrowExceptionWhenUpdateWithExistingFieldValues() {
    PersonDTO dto = new PersonDTO();
    dto.setRole(PERSON_ROLE);
    dto.setPublicHealthNumber(PUBLIC_HEALTH_NUMBER_WHITESPACE);

    PersonDTO originalDto = new PersonDTO();
    originalDto.setRole(PERSON_ROLE);
    originalDto.setPublicHealthNumber(PUBLIC_HEALTH_NUMBER_WHITESPACE);

    assertDoesNotThrow(() -> testObj.validate(dto, originalDto, Update.class));
    verify(referenceService, never()).rolesMatch(anyList(), anyBoolean());
  }
}
