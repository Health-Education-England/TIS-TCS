package com.transformuk.hee.tis.tcs.service.api.validation;

import static com.transformuk.hee.tis.tcs.service.api.validation.ContactDetailsValidator.FIELD_NAME_EMAIL;
import static com.transformuk.hee.tis.tcs.service.api.validation.ContactDetailsValidator.FIELD_NAME_TITLE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.TitleDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.model.ContactDetails;
import com.transformuk.hee.tis.tcs.service.repository.ContactDetailsRepository;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class ContactDetailsValidatorTest {

  private static final String REGEX_EMAIL_ERROR = "Valid email format required.";
  private static final String REGEX_NAME_ERROR =
      "No special characters allowed for %s, with the exception of apostrophes, hyphens and spaces.";
  private static final String REGEX_PHONE_ERROR =
      "Only numerical values allowed for %s, no special characters, with the exception of plus, minus and spaces.";
  private static final String EMAIL_NOT_UNIQUE_ERROR = "email %s is not unique, there %s currently %d %s with this email (Person ID: %s)";

  private static final String DTO_NAME = ContactDetailsDTO.class.getSimpleName();

  private ContactDetailsValidator validator;

  @Mock
  private ReferenceServiceImpl referenceService;
  @Mock
  private ContactDetailsRepository contactDetailsRepository;
  @Mock
  private ContactDetails contactDetailsMock1, contactDetailsMock2;

  @BeforeEach
  void setUp() {
    validator = new ContactDetailsValidator(referenceService, contactDetailsRepository);
  }

  @Test
  void shouldThrowExceptionWhenTitleNotExists() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setTitle("myTitle");

    when(referenceService.isValueExists(TitleDTO.class, "myTitle", true)).thenReturn(false);

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(dto, null, Create.class));

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

    when(referenceService.isValueExists(TitleDTO.class, "myTitle", true)).thenReturn(true);

    // When.
    assertDoesNotThrow(() -> validator.validate(dto, null, Create.class));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenAddress1AndNotPostcode() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress1("address1");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("should return 1 error", fieldErrors.size(), is(1));
    assertThat("Unexpected error message", fieldErrors.get(0).getDefaultMessage(),
        is("postCode is required when address is populated."));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenAddress2AndNotAddress1() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress2("address2");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    List<String> errorMessages = fieldErrors.stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(
            Collectors.toList());
    assertThat("Unexpected error message", errorMessages,
        hasItem("address1 is required when address2 is populated."));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenAddress2AndNotPostcode() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress2("address2");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    List<String> errorMessages = fieldErrors.stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(
            Collectors.toList());
    assertThat("Unexpected error message", errorMessages,
        hasItem("postCode is required when address is populated."));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenAddress3AndNotAddress1() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress3("address3");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    List<String> errorMessages = fieldErrors.stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(
            Collectors.toList());
    assertThat("Unexpected error message", errorMessages,
        hasItem("address1 is required when address3 is populated."));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenAddress3AndNotAddress2() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress3("address2");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    List<String> errorMessages = fieldErrors.stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(
            Collectors.toList());
    assertThat("Unexpected error message", errorMessages,
        hasItem("address2 is required when address3 is populated."));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenAddress3AndNotPostcode() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress3("address3");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    List<String> errorMessages = fieldErrors.stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage).collect(
            Collectors.toList());
    assertThat("Unexpected error message", errorMessages,
        hasItem("postCode is required when address is populated."));
  }

  @Test
  void bulkValidateShouldReturnErrorWhenPostCodeAndNotAddress1() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setPostCode("postCode");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("should return 1 error", fieldErrors.size(), is(1));
    assertThat("Unexpected error message", fieldErrors.get(0).getDefaultMessage(),
        is("address1 is required when postCode is populated."));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenAddressValid() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress1("address1");
    dto.setAddress2("address2");
    dto.setAddress3("address3");
    dto.setPostCode("postCode");

    // When, Then.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenAddressValidNoAddress3() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress1("address1");
    dto.setAddress2("address2");
    dto.setPostCode("postCode");

    // When, Then.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenAddressValidNoAddress2() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setAddress1("address1");
    dto.setPostCode("postCode");

    // When, Then.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void bulkValidateShouldNotReturnErrorWhenAllValid() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setTitle("myTitle");
    dto.setAddress1("address1");
    dto.setAddress2("address2");
    dto.setAddress3("address3");
    dto.setPostCode("postCode");

    when(referenceService.isValueExists(TitleDTO.class, "myTitle", true)).thenReturn(true);

    // When, Then.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);
    assertThat("should not return errors", fieldErrors.size(), is(0));
  }

  @Test
  void shouldNotThrowExceptionWhenEmpty() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();

    // When, Then.
    assertDoesNotThrow(() -> validator.validate(dto, null, Create.class));
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
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "forenames",
            String.format(REGEX_NAME_ERROR, "forenames"));
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenForenamesNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setSurname("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
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
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "surname",
            String.format(REGEX_NAME_ERROR, "surname"));
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldNotReturnErrorsWhenSurnameNull() {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
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
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "knownAs",
            String.format(REGEX_NAME_ERROR, "knownAs"));
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
            String.format(REGEX_PHONE_ERROR, "telephoneNumber"));
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
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "mobileNumber",
            String.format(REGEX_PHONE_ERROR, "mobileNumber"));
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

  @ParameterizedTest
  @ValueSource(strings = {"a..b@b.com", "()@b.com", "a@f%.com", "a@b|.com", "a.b.com", "a@f'%com",
      " abc.com", "abc.com ", "a bc.com",
      "\u00A0abc.com", "abc.com\u00A0", "a\u00A0bc.com", // non-breaking spaces
      //following examples drawn from https://en.wikipedia.org/wiki/Email_address
      //failing entries with the current REGEXP are commented out
      "Abc.example.com",
      "A@b@c@example.com",
      "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com",
      "just\"not\"right@example.com",
      "this is\"not\\allowed@example.com",
      "this\\ still\\\"not\\\\allowed@example.com",
      /* "1234567890123456789012345678901234567890123456789012345678901234+x@example.com", */
      "i_like_underscore@but_its_not_allowed_in_this_part.example.com"})
  void shouldReturnErrorsWhenEmailFormatNotValid(String email) {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setEmail(email);

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("No field error for invalid email: '" + email + "'", fieldErrors.size(), is(1));

    FieldError fieldError =
        new FieldError(ContactDetailsDTO.class.getSimpleName(), "email", REGEX_EMAIL_ERROR);
    assertThat("Expected field error not found", fieldErrors, hasItem(fieldError));
  }

  @Test
  void shouldThrowErrorsWhenOneEmailAlreadyExists() {
    // Given.
    String email = "a@b.com";
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setEmail(email);
    dto.setId(null);

    Long idMocked = 1L;
    when(contactDetailsRepository.findContactDetailsByEmail(email)).thenReturn(
        Collections.singletonList(contactDetailsMock1));
    when(contactDetailsMock1.getId()).thenReturn(idMocked);

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(dto, null, Create.class));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    String errorMessage = String.format(EMAIL_NOT_UNIQUE_ERROR,
        email, "is", 1, "person", idMocked);
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        containsString(errorMessage));
  }

  @Test
  void shouldThrowErrorsWhenMultipleEmailsAlreadyExists() {
    // Given.
    String email = "a@b.com";
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setEmail(email);
    dto.setId(null);

    Long idMocked1 = 1L, idMocked2 = 2L;
    when(contactDetailsRepository.findContactDetailsByEmail(email)).thenReturn(
        Lists.newArrayList(contactDetailsMock1, contactDetailsMock2));
    when(contactDetailsMock1.getId()).thenReturn(idMocked1);
    when(contactDetailsMock2.getId()).thenReturn(idMocked2);

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(dto, null, Create.class));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    String errorMessage = String.format(EMAIL_NOT_UNIQUE_ERROR,
        email, "are", 2, "persons", idMocked1 + "," + idMocked2);
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        containsString(errorMessage));
  }

  @Test
  void shouldNotReturnErrorsWhenCurrentEmailIsFoundWithTheSameId() {
    // Given.
    String email = "a@b.com";
    Long id = 1L;
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setEmail(email);
    dto.setId(id);

    when(contactDetailsRepository.findContactDetailsByEmail(email)).thenReturn(
        Lists.newArrayList(contactDetailsMock1));
    when(contactDetailsMock1.getId()).thenReturn(id);

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then
    assertThat("Unexpected number of field errors.", fieldErrors.size(), is(0));
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

  @ParameterizedTest
  @ValueSource(strings = {"o'connor@abc.com",
      //following examples drawn from https://en.wikipedia.org/wiki/Email_address
      //failing entries with the current REGEXP are commented out
      "simple@example.com",
      "very.common@example.com",
      "disposable.style.email.with+symbol@example.com",
      "other.email-with-hyphen@example.com",
      "fully-qualified-domain@example.com",
      "user.name+tag+sorting@example.com",
      "x@example.com",
      "example-indeed@strange-example.com",
      "test/test@test.com",
      /* "admin@mailserver1", */
      "example@s.example",
      /* "\" \"@example.org", */
      /* "\"john..doe\"@example.org", */
      "mailhost!username@example.org",
      /* "\"very.(),:;<>[]\\\".VERY.\\\"very@\\\\ \\\"very\\\".unusual\"@strange.example.com", */
      "user%example.com@example.org",
      "user-@example.org"
      /*, "postmaster@[123.123.123.123]", */
      /* "postmaster@[IPv6:2001:0db8:85a3:0000:0000:8a2e:0370:7334]" */})
  void shouldNotReturnErrorsWhenEmailIsValid(String email) {
    // Given.
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setForenames("");
    dto.setSurname("");
    dto.setEmail(email);

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);

    // Then.
    assertThat("Field error for valid email: '" + email + "'", fieldErrors.size(), is(0));
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

  @Test
  void shouldNotThrowExceptionWhenUpdateWithValidFields() {
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setTitle("myTitle");
    dto.setEmail("a@b.com");

    ContactDetailsDTO originalDto = new ContactDetailsDTO();

    when(referenceService.isValueExists(TitleDTO.class, "myTitle", true)).thenReturn(true);

    assertDoesNotThrow(() -> validator.validate(dto, originalDto, Update.class));
  }

  @Test
  void shouldThrowExceptionWhenUpdateWithInvalidFields() {
    String title = "myTitle";
    String email = "a.b.com";
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setTitle(title);
    dto.setEmail(email);
    ContactDetailsDTO originalDto = new ContactDetailsDTO();

    when(referenceService.isValueExists(TitleDTO.class, title, true)).thenReturn(false);

    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(dto, originalDto, Update.class));

    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        Matchers.is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), Matchers.is(dto));

    FieldError fieldError1 = new FieldError(DTO_NAME, FIELD_NAME_TITLE,
        String.format("title %s does not exist", title));
    FieldError fieldError2 = new FieldError(DTO_NAME, FIELD_NAME_EMAIL,
        REGEX_EMAIL_ERROR);

    assertThat("Unexpected error count.", result.getFieldErrors().size(), Matchers.is(2));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError1, fieldError2));
  }

  @Test
  void shouldNotThrowExceptionWhenUpdateWithExistingFieldValues() {
    String title = "myTitle";
    String email = "a.b.com";
    ContactDetailsDTO dto = new ContactDetailsDTO();
    dto.setTitle(title);
    dto.setEmail(email);
    ContactDetailsDTO originalDto = new ContactDetailsDTO();
    originalDto.setTitle(title);
    originalDto.setEmail(email);

    assertDoesNotThrow(() -> validator.validate(dto, originalDto, Update.class));
    verify(referenceService, never()).isValueExists(any(Class.class), anyString(), anyBoolean());
  }
}
