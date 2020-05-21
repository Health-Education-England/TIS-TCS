package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.TitleDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class ContactDetailsValidatorTest {

  private ContactDetailsValidator validator;

  @Mock
  private ReferenceServiceImpl referenceService;

  @BeforeEach
  void setUp() {
    validator = new ContactDetailsValidator(referenceService);
  }

  @Test
  void shouldThrowExceptionWhenTitleNotExists() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setTitle("myTitle");

    when(referenceService.isValueExists(TitleDTO.class, "myTitle")).thenReturn(false);

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(ContactDetailsDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError = new FieldError(ContactDetailsDTO.class.getSimpleName(), "title",
        "title myTitle does not exist");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldNotThrowExceptionWhenTitleExists() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setTitle("myTitle");

    when(referenceService.isValueExists(TitleDTO.class, "myTitle")).thenReturn(true);

    // When.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldThrowExceptionWhenAddress1AndNotPostcode() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress1("address1");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult bindingResult = thrown.getBindingResult();
    assertThat("Unexpected object name.", bindingResult.getObjectName(),
        is(ContactDetailsDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", bindingResult.getTarget(), is(dto));

    List<FieldError> fieldErrors = bindingResult.getFieldErrors();
    assertThat("Unexpected number of errors.", fieldErrors.size(), is(1));

    FieldError fieldError = fieldErrors.get(0);
    assertThat("Unexpected error object name.", fieldError.getObjectName(),
        is(ContactDetailsDTO.class.getSimpleName()));
    assertThat("Unexpected error field.", fieldError.getField(), is("postCode"));
    assertThat("Unexpected error message.", fieldError.getDefaultMessage(),
        is("postCode is required when address is populated."));
  }

  @Test
  void shouldThrowExceptionWhenAddress2AndNotAddress1() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress2("address2");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(ContactDetailsDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError = new FieldError(ContactDetailsDTO.class.getSimpleName(), "address1",
        "address1 is required when address2 is populated.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenAddress2AndNotPostcode() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress2("address2");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(ContactDetailsDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError = new FieldError(ContactDetailsDTO.class.getSimpleName(), "postCode",
        "postCode is required when address is populated.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenAddress3AndNotAddress1() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress3("address3");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(ContactDetailsDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError = new FieldError(ContactDetailsDTO.class.getSimpleName(), "address1",
        "address1 is required when address3 is populated.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenAddress3AndNotAddress2() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress3("address2");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(ContactDetailsDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError = new FieldError(ContactDetailsDTO.class.getSimpleName(), "address2",
        "address2 is required when address3 is populated.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenAddress3AndNotPostcode() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress3("address3");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(ContactDetailsDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError = new FieldError(ContactDetailsDTO.class.getSimpleName(), "postCode",
        "postCode is required when address is populated.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenPostCodeAndNotAddress1() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setPostCode("postCode");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(ContactDetailsDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError = new FieldError(ContactDetailsDTO.class.getSimpleName(), "address1",
        "address1 is required when postCode is populated.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldNotThrowExceptionWhenAddressValid() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress1("address1");
    dto.setAddress2("address2");
    dto.setAddress3("address3");
    dto.setPostCode("postCode");

    // When, Then.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldNotThrowExceptionWhenAddressValidNoAddress3() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress1("address1");
    dto.setAddress2("address2");
    dto.setPostCode("postCode");

    // When, Then.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldNotThrowExceptionWhenAddressValidNoAddress2() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress1("address1");
    dto.setPostCode("postCode");

    // When, Then.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldNotThrowExceptionWhenAllValid() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setTitle("myTitle");
    dto.setAddress1("address1");
    dto.setAddress2("address2");
    dto.setAddress3("address3");
    dto.setPostCode("postCode");

    when(referenceService.isValueExists(TitleDTO.class, "myTitle")).thenReturn(true);

    // When, Then.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldNotThrowExceptionWhenEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();

    // When, Then.
    assertDoesNotThrow(() -> validator.validate(dto));
  }
}
