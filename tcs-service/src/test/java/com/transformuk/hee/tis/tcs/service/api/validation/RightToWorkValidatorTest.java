package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.PermitToWorkDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import java.time.LocalDate;
import java.util.List;

import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class RightToWorkValidatorTest {

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
    List<FieldError> fieldErrors = validator.validateForBulk(null, null );
    assertThat("Unexpected number of errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenEeaResidentNotValid() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("invalid");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto, null );

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
    List<FieldError> fieldErrors = validator.validateForBulk(dto,null);

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
    List<FieldError> fieldErrors = validator.validateForBulk(dto,null);

    // Then.
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenEeaResidentNo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("NO");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto,null);

    // Then.
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenEeaResidentUnknown() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("UNKNOWN");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto,null);

    // Then.
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenSettledNotValid() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setSettled("invalid");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto,null);

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
    List<FieldError> fieldErrors = validator.validateForBulk(dto,null);

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
    List<FieldError> fieldErrors = validator.validateForBulk(dto,null);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenSettledNo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setSettled("NO");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto,null);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenVisaIssuedAfterVisaValidTo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaIssued(LocalDate.now().plusDays(1));
    dto.setVisaValidTo(LocalDate.now());
    PersonDTO personDTO = new PersonDTO();
    personDTO.setRightToWork(dto);

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto,personDTO);

    // Then
    assertThat("should return 1 error", fieldErrors.size(), is(1));
    assertThat("Unexpected error message", fieldErrors.get(0).getDefaultMessage(),
        is("visaIssued must be before visaValidTo."));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenVisaIssuedAndNoVisaValidTo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaIssued(LocalDate.now());
    PersonDTO personDTO = new PersonDTO();
    personDTO.setRightToWork(dto);

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto,personDTO);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorWhenNotVisaIssuedAndVisaValidTo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaValidTo(LocalDate.now());
    PersonDTO personDTO = new PersonDTO();
    personDTO.setRightToWork(dto);

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto,personDTO);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

   @Test
  void shouldNotReturnErrorWhenEmpty() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    PersonDTO personDTO = new PersonDTO();
    personDTO.setRightToWork(dto);

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto,personDTO);

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
    List<FieldError> fieldErrors = validator.validateForBulk(dto,null);
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
    List<FieldError> fieldErrors = validator.validateForBulk(dto,personDTO);
    // Then.
    assertThat("Error list should contain 3 errors.", fieldErrors.size(), equalTo(3));
  }

  @Test
  void shouldPassValidationWhenDtoNull() {
    assertDoesNotThrow(() -> validator.validate(null));
  }

  @Test
  void shouldFailValidationPermitToWorkNotExists() {
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setPermitToWork("doesNotExist");

    when(referenceService.isValueExists(PermitToWorkDTO.class, "doesNotExist", true))
        .thenReturn(false);

    MethodArgumentNotValidException exception = assertThrows(MethodArgumentNotValidException.class,
        () -> validator.validate(dto));

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

    assertDoesNotThrow(() -> validator.validate(dto));
  }
}
