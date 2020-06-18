package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.FieldError;

class RightToWorkValidatorTest {

  private static final String DTO_NAME = RightToWorkDTO.class.getSimpleName();

  private RightToWorkValidator validator;

  @BeforeEach
  void setUp() {
    validator = new RightToWorkValidator();
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
  void bulkValidateShouldReturnErrorWhenVisaIssuedAfterVisaValidTo() {
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
  void bulkValidateShouldNotReturnErrorWhenVisaIssuedAndNoVisaValidTo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaIssued(LocalDate.now());

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorWhenNotVisaIssuedAndVisaValidTo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaValidTo(LocalDate.now());

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("should not return errors", fieldErrors.size(), is(0));
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

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);
    // Then.
    assertThat("Error list should contain 3 errors.", fieldErrors.size(), equalTo(3));
  }
}
