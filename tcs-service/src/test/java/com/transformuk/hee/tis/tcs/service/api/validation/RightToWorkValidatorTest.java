package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PermitToWorkType;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

class RightToWorkValidatorTest {

  private static final String DTO_NAME = RightToWorkDTO.class.getSimpleName();

  private RightToWorkValidator validator;

  @BeforeEach
  void setUp() {
    validator = new RightToWorkValidator();
  }

  @Test
  void shouldThrowExceptionWhenPermitToWorkValid() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setPermitToWork(PermitToWorkType.OTHER);

    // When, Then.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldThrowExceptionWhenEeaResidentNotValid() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("invalid");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError =
        new FieldError(DTO_NAME, "eeaResident", "eeaResident must match a reference value.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenEeaResidentIncorrectCase() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("Yes");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError =
        new FieldError(DTO_NAME, "eeaResident", "eeaResident must match a reference value.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldNotThrowExceptionWhenEeaResidentYes() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("YES");

    // When.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldNotThrowExceptionWhenEeaResidentNo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("NO");

    // When.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldNotThrowExceptionWhenEeaResidentUnknown() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setEeaResident("UNKNOWN");

    // When.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldThrowExceptionWhenSettledNotValid() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setSettled("invalid");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError =
        new FieldError(DTO_NAME, "settled", "settled must match a reference value.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenSettledIncorrectCase() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setSettled("Yes");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError =
        new FieldError(DTO_NAME, "settled", "settled must match a reference value.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldNotThrowExceptionWhenSettledYes() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setSettled("YES");

    // When.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldNotThrowExceptionWhenSettledNo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setSettled("NO");

    // When.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldThrowExceptionWhenVisaIssuedAfterVisaValidTo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaIssued(LocalDate.now().plusDays(1));
    dto.setVisaValidTo(LocalDate.now());

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError =
        new FieldError(DTO_NAME, "visaIssued", "visaIssued must be before visaValidTo.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldNotThrowExceptionWhenVisaIssuedAndNoVisaValidTo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaIssued(LocalDate.now());

    // When, Then.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldNotThrowExceptionWhenNotVisaIssuedAndVisaValidTo() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();
    dto.setVisaValidTo(LocalDate.now());

    // When, Then.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldNotThrowExceptionWhenEmpty() {
    // Given.
    RightToWorkDTO dto = new RightToWorkDTO();

    // When, Then.
    assertDoesNotThrow(() -> validator.validate(dto));
  }
}
