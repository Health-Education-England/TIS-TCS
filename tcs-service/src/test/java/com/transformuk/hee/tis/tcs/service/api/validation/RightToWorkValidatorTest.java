package com.transformuk.hee.tis.tcs.service.api.validation;

import static com.transformuk.hee.tis.tcs.service.api.validation.RightToWorkValidator.FIELD_NAME_PERMIT_TO_WORK;
import static com.transformuk.hee.tis.tcs.service.api.validation.RightToWorkValidator.FIELD_NAME_VISA_ISSUED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.PermitToWorkDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.RightToWork;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class RightToWorkValidatorTest {

  private static final String DTO_NAME = RightToWorkDTO.class.getSimpleName();
  private RightToWorkValidator validator;
  private PersonRepository personRepository;
  private ReferenceService referenceService;

  @BeforeEach
  void setUp() {
    referenceService = mock(ReferenceService.class);
    personRepository = mock(PersonRepository.class);
    validator = new RightToWorkValidator(referenceService, personRepository);
  }

  @Test
  void shouldPassBulkValidationWhenDtoNull() {
    List<FieldError> fieldErrors = validator.validateForBulk(null);
    assertThat("Unexpected number of errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenEeaResidentNotValid() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("invalid");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("should return 1 error", fieldErrors.size(), is(1));
    assertThat("Unexpected error message", fieldErrors.get(0).getDefaultMessage(),
        is("eeaResident must match a reference value."));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenEeaResidentIncorrectCase() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("Yes");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("should return 1 error", fieldErrors.size(), is(1));
    assertThat("Unexpected error message", fieldErrors.get(0).getDefaultMessage(),
        is("eeaResident must match a reference value."));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenEeaResidentYes() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("YES");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenEeaResidentNo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("NO");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenEeaResidentUnknown() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("UNKNOWN");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenSettledNotValid() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setSettled("invalid");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("should return 1 error", fieldErrors.size(), is(1));
    assertThat("Unexpected error message", fieldErrors.get(0).getDefaultMessage(),
        is("settled must match a reference value."));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenSettledIncorrectCase() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setSettled("Yes");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should return 1 error", fieldErrors.size(), is(1));
    assertThat("Unexpected error message", fieldErrors.get(0).getDefaultMessage(),
        is("settled must match a reference value."));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenSettledYes() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setSettled("YES");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenSettledNo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setSettled("NO");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorWhenVisaIssuedAfterVisaValidTo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaIssued(LocalDate.now().plusDays(1));
    dto.setVisaValidTo(LocalDate.now());

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should return 1 error", fieldErrors.size(), is(1));
    assertThat("Unexpected error message", fieldErrors.get(0).getDefaultMessage(),
        is("visaIssued must be before visaValidTo."));
  }

  @Test
  void shouldNotReturnErrorWhenNoVisaValidTo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaIssued(LocalDate.now());

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorWhenNoVisaIssued() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaValidTo(LocalDate.now());

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorWhenVisaValidToIsAfterVisaIssued() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaIssued(LocalDate.now());
    dto.setVisaValidTo(LocalDate.now().plusDays(1));

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should return 1 error", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenVisaValidToIsAfterDbVisaIssued() {
    // Given.
    Long personId = 1L;
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setId(personId);
    dto.setVisaValidTo(LocalDate.now().plusDays(10));

    RightToWork dbDto = new RightToWork();
    dbDto.setVisaIssued(LocalDate.now());
    Person person = new Person();
    person.setId(personId);
    person.setRightToWork(dbDto);

    when(personRepository.findPersonById(personId)).thenReturn(Optional.of(person));

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenVisaValidToIsBeforeDbVisaIssued() {
    // Given.
    Long personId = 1L;
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setId(personId);
    dto.setVisaValidTo(LocalDate.now());

    RightToWork dbDto = new RightToWork();
    dbDto.setVisaIssued(LocalDate.now().plusDays(10));
    Person person = new Person();
    person.setId(personId);
    person.setRightToWork(dbDto);

    when(personRepository.findPersonById(personId)).thenReturn(Optional.of(person));

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should return 1 error", fieldErrors.size(), is(1));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenDbVisaValidToIsAfterVisaIssued() {
    // Given.
    Long personId = 1L;
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setId(personId);
    dto.setVisaIssued(LocalDate.now());

    RightToWork dbDto = new RightToWork();
    dbDto.setVisaValidTo(LocalDate.now().plusDays(10));
    Person person = new Person();
    person.setId(personId);
    person.setRightToWork(dbDto);

    when(personRepository.findPersonById(personId)).thenReturn(Optional.of(person));

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenDbVisaValidToIsBeforeVisaIssued() {
    // Given.
    Long personId = 1L;
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setId(personId);
    dto.setVisaIssued(LocalDate.now().plusDays(10));

    RightToWork dbDto = new RightToWork();
    dbDto.setVisaValidTo(LocalDate.now());
    Person person = new Person();
    person.setId(personId);
    person.setRightToWork(dbDto);

    when(personRepository.findPersonById(personId)).thenReturn(Optional.of(person));

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should return 1 error", fieldErrors.size(), is(1));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenNoDBRtwFoundForVisaDateCheck() {
    // Given.
    Long personId = 1L;
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setId(personId);
    dto.setVisaValidTo(LocalDate.now().plusDays(10));

    Person person = new Person();

    when(personRepository.findPersonById(personId)).thenReturn(Optional.of(person));

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorWhenDbVisaValidToIsBeforeVisaIssuedForBulkUpdate() {
    Long personId = 1L;
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setId(personId);
    dto.setVisaIssued(LocalDate.now().plusDays(10));

    verify(personRepository, times(0)).findPersonById(personId);

    assertDoesNotThrow(() -> validator.validate(dto, null, Create.class));
  }

  @Test
  void shouldNotReturnErrorWhenEmpty() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnEmptyFieldErrorsWhenValidateForBulkIsOkay() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("YES");
    dto.setSettled("YES");
    dto.setVisaIssued(LocalDate.now().minusDays(1));
    dto.setVisaValidTo(LocalDate.now());

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);
    // Then.
    assertThat("Error list should be empty.", fieldErrors.size(), equalTo(0));
  }

  @Test
  void shouldReturnAllFieldErrorsWhenValidateForBulkGetsErrors() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("Invalid");
    dto.setSettled("Invalid");
    dto.setVisaIssued(LocalDate.now().plusDays(1));
    dto.setVisaValidTo(LocalDate.now());
    PersonDTO personDTO = new PersonDTO();
    personDTO.setRightToWork(dto);

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);
    // Then.
    assertThat("Error list should contain 3 errors.", fieldErrors.size(), equalTo(3));
  }

  @Test
  void shouldPassValidationWhenDtoNull() {
    assertDoesNotThrow(() -> validator.validate(null, null, Create.class));
  }

  @Test
  void shouldFailValidationPermitToWorkNotExists() {
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setPermitToWork("doesNotExist");

    when(referenceService.isValueExists(PermitToWorkDTO.class, "doesNotExist", true))
        .thenReturn(false);

    MethodArgumentNotValidException exception = assertThrows(MethodArgumentNotValidException.class,
        () -> validator.validate(dto, null, Create.class));

    List<FieldError> permitToWorkErrors = exception.getBindingResult()
        .getFieldErrors("permitToWork");
    assertThat("Unexpected number of errors.", permitToWorkErrors.size(), is(1));

    FieldError permitToWorkError = permitToWorkErrors.get(0);
    assertThat("Unexpected rejected value.", permitToWorkError.getDefaultMessage(),
        is("permitToWork must match a current reference value."));
  }

  @Test
  void shouldPassValidationPermitToWorkExists() {
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setPermitToWork("doesExist");

    when(referenceService.isValueExists(PermitToWorkDTO.class, "doesExist", true)).thenReturn(true);

    assertDoesNotThrow(() -> validator.validate(dto, null, Create.class));
  }

  @Test
  void shouldNotThrowExceptionWhenUpdateWithValidFields() {
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setPermitToWork("Exists");
    dto.setVisaIssued(LocalDate.now().minusDays(1));
    dto.setVisaValidTo(LocalDate.now().plusDays(1));

    RightToWorkDTO originalDto = new RightToWorkDTO();
    when(referenceService.isValueExists(PermitToWorkDTO.class, "Exists", true))
        .thenReturn(true);

    assertDoesNotThrow(() -> validator.validate(dto, originalDto, Update.class));
  }

  @Test
  void shouldThrowExceptionWhenUpdateWithInvalidFields() {
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setPermitToWork("NotExists");
    dto.setVisaIssued(LocalDate.now().plusDays(1));
    dto.setVisaValidTo(LocalDate.now().minusDays(1));

    RightToWorkDTO originalDto = new RightToWorkDTO();
    when(referenceService.isValueExists(RightToWorkDTO.class, "NotExists", true))
        .thenReturn(false);

    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(dto, originalDto, Update.class));

    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        Matchers.is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), Matchers.is(dto));

    FieldError fieldError1 = new FieldError(DTO_NAME, FIELD_NAME_PERMIT_TO_WORK,
        "permitToWork must match a current reference value.");
    FieldError fieldError2 = new FieldError(DTO_NAME, FIELD_NAME_VISA_ISSUED,
        "visaIssued must be before visaValidTo.");

    assertThat("Unexpected error count.", result.getFieldErrors().size(), Matchers.is(2));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError1, fieldError2));
  }

  @Test
  void shouldNotThrowExceptionWhenUpdateWithExistingFieldValues() {
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setPermitToWork("NotExists");
    dto.setVisaIssued(LocalDate.now().plusDays(1));
    dto.setVisaValidTo(LocalDate.now().minusDays(1));

    RightToWorkDTO originalDto = new RightToWorkDTO();
    originalDto.setPermitToWork("NotExists");
    originalDto.setVisaIssued(LocalDate.now().plusDays(1));
    originalDto.setVisaValidTo(LocalDate.now().minusDays(1));

    assertDoesNotThrow(() -> validator.validate(dto, originalDto, Update.class));
    verify(referenceService, never()).isValueExists(any(Class.class), anyString(), anyBoolean());
  }
}
