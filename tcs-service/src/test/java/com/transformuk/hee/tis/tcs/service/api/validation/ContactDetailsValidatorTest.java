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

  private static final String REGEX_EMAIL_ERROR = "Valid email format required.";
  private static final String REGEX_NAME_ERROR =
      "No special characters, with the exception of apostrophes, hyphens and spaces.";
  private static final String NULL_NAME_ERROR = "%s is required to create or update the record.";
  private static final String REGEX_PHONE_ERROR =
      "Only numerical values allowed for TelephoneNumber, no special characters, with the exception of plus, minus and spaces.";

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

    when(referenceService.isValueExists(TitleDTO.class, "myTitle", false)).thenReturn(false);

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
    assertThat("Expected field error not found.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldNotThrowExceptionWhenTitleExists() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setTitle("myTitle");

    when(referenceService.isValueExists(TitleDTO.class, "myTitle", false)).thenReturn(true);

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
    assertThat("Expected field error not found.", result.getFieldErrors(), hasItem(fieldError));
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
    assertThat("Expected field error not found.", result.getFieldErrors(), hasItem(fieldError));
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
    assertThat("Expected field error not found.", result.getFieldErrors(), hasItem(fieldError));
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
    assertThat("Expected field error not found.", result.getFieldErrors(), hasItem(fieldError));
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
    assertThat("Expected field error not found.", result.getFieldErrors(), hasItem(fieldError));
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
    assertThat("Expected field error not found.", result.getFieldErrors(), hasItem(fieldError));
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

    when(referenceService.isValueExists(TitleDTO.class, "myTitle", false)).thenReturn(true);

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

  @Test
  void shouldReturnEmptyFieldErrorsWhenValidateForBulkIsOkay() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setTitle("myTitle");
    dto.setForenames("name1");
    dto.setSurname("name2");
    dto.setAddress1("address1");
    dto.setAddress2("address2");
    dto.setAddress3("address3");
    dto.setPostCode("postCode");

    when(referenceService.isValueExists(TitleDTO.class, "myTitle", true)).thenReturn(true);

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnAllFieldErrorsWhenValidateForBulkGetsErrors() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setTitle("myTitle");
    dto.setForenames("name1*");
    dto.setSurname("name2*");
    dto.setAddress1("address1");
    dto.setAddress2("address2");
    dto.setAddress3("address3");

    when(referenceService.isValueExists(TitleDTO.class, "myTitle", true)).thenReturn(false);

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(4));
  }

  @Test
  void shouldReturnErrorsWhenForenamesHasSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setForenames("name1 name2-'name3 *;'");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "forenames", REGEX_NAME_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldReturnErrorsWhenForenamesNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError = new FieldError(ContactDetailsDTO.class.getSimpleName(), "forenames",
        String.format(NULL_NAME_ERROR, "forenames"));
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenForenamesEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setForenames("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenForenamesHasNoSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setForenames("name1 name2-'name3");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorsWhenLegalForenamesHasSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setLegalForenames("name1 name2-'name3 *;'");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "legalForenames", REGEX_NAME_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenLegalForenamesNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenLegalForenamesEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setLegalForenames("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenLegalForenamesHasNoSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setLegalForenames("name1 name2-'name3");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorsWhenSurnameHasSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("name1 name2-'name3 *;'");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "surname", REGEX_NAME_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldReturnErrorsWhenSurnameNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError = new FieldError(ContactDetailsDTO.class.getSimpleName(), "surname",
        String.format(NULL_NAME_ERROR, "surname"));
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenSurnameEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenSurnameHasNoSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("name1 name2-'name3");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorsWhenLegalSurnameHasSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setLegalSurname("name1 name2-'name3 *;'");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "legalSurname", REGEX_NAME_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenLegalSurnameNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenLegalSurnameEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setLegalSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenLegalSurnameHasNoSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setLegalSurname("name1 name2-'name3");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorsWhenKnownAsHasSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setKnownAs("name1 name2-'name3 *;'");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "knownAs", REGEX_NAME_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenKnownAsNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenKnownAsEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setKnownAs("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenKnownAsHasNoSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setKnownAs("name1 name2-'name3");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorsWhenMaidenNameHasSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setMaidenName("name1 name2-'name3 *;'");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "maidenName", REGEX_NAME_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenMaidenNameNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenMaidenNameEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setMaidenName("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenMaidenNameHasNoSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setMaidenName("name1 name2-'name3");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorsWhenInitialsHasSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setInitials("name1 name2-'name3 *;'");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "initials", REGEX_NAME_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenInitialsNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenInitialsEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setInitials("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenInitialsHasNoSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setInitials("name1 name2-'name3");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorsWhenCountryHasSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setCountry("name1 name2-'name3 *;'");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "country", REGEX_NAME_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenCountryNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenCountryEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setCountry("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenCountryHasNoSpecialCharacters() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setCountry("name1 name2-'name3");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorsWhenTelephoneNumberHasNonNumerics() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setTelephoneNumber("+123 456-7890 abc*;'");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "telephoneNumber",
            REGEX_PHONE_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenTelephoneNumberNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenTelephoneNumberEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setTelephoneNumber("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenTelephoneNumberIsNumeric() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setTelephoneNumber("+123 456-7890");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorsWhenMobileNumberHasNonNumerics() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setMobileNumber("+123 456-7890 abc*;'");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "mobileNumber", REGEX_PHONE_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenMobileNumberNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenMobileNumberEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setMobileNumber("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenMobileNumberIsNumeric() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setMobileNumber("+123 456-7890");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorsWhenEmailNotValid() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setEmail("a*@b.com");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "email", REGEX_EMAIL_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenEmailNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenEmailEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setEmail("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenEmailIsValid() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setEmail("a@b.com");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldReturnErrorsWhenWorkEmailNotValid() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setWorkEmail("@b.com");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "workEmail", REGEX_EMAIL_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenWorkEmailNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenWorkEmailEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setWorkEmail("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotReturnErrorsWhenWorkEmailIsValid() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setWorkEmail("a@b.com");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
  }
}
