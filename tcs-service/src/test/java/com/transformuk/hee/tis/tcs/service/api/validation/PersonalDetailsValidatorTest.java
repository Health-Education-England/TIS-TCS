package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class PersonalDetailsValidatorTest {

  private static final String DTO_NAME = PersonalDetailsDTO.class.getSimpleName();

  private PersonalDetailsValidator validator;

  @Mock
  private ReferenceServiceImpl referenceService;

  @BeforeEach
  void setUp() {
    validator = new PersonalDetailsValidator(referenceService);
  }

  @Test
  void shouldThrowExceptionWhenDisabilityNotValid() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setDisability("invalid");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError =
        new FieldError(DTO_NAME, "disability", "disability must match a reference value.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenDisabilityIncorrectCase() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setDisability("Yes");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError =
        new FieldError(DTO_NAME, "disability", "disability must match a reference value.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldNotThrowExceptionWhenDisabilityYes() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setDisability("YES");

    // When.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldNotThrowExceptionWhenDisabilityNo() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setDisability("NO");

    // When.
    assertDoesNotThrow(() -> validator.validate(dto));
  }
}
