package com.transformuk.hee.tis.tcs.service.api.validation;

import static com.transformuk.hee.tis.tcs.service.api.validation.GmcDetailsValidator.FIELD_NAME_GMC_NUMBER;
import static com.transformuk.hee.tis.tcs.service.api.validation.GmcDetailsValidator.FIELD_NAME_GMC_STATUS;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
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
import com.transformuk.hee.tis.reference.api.dto.GmcStatusDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class GmcDetailsValidatorTest {

  private static final String DEFAULT_GMC_NUMBER = "DEFAULT_GMC_NUMBER";
  private static final String WHITESPACE_GMC_NUMBER = "WHITESPACE_GMC_NUMBER  ";
  private static final String UNKNOWN_GMC_NUMBER = "UNKNOWN";
  private static final String DEFAULT_GMC_STATUS = "DEFAULT_GMC_STATUS";
  private static final Long GMC_DETAILS_ID = 123L;
  private static final Long DIFFERENT_GMC_DETAILS_ID = 999L;

  private static final String DTO_NAME = GmcDetailsDTO.class.getSimpleName();

  @InjectMocks
  private GmcDetailsValidator validator;

  @Mock
  private ReferenceServiceImpl referenceService;

  @Mock
  private GmcDetailsRepository gmcDetailsRepository;

  @Mock
  private GmcDetailsDTO gmcDetailsDtoMock, gmcDetailsDtoMock_whitespace;

  @Mock
  private GmcDetails gmcDetailsMock1, gmcDetailsMock2;


  @Test
  void validateShouldIncludeParameterInThrownException() {
    // Given.
    when(gmcDetailsDtoMock_whitespace.getGmcNumber()).thenReturn(WHITESPACE_GMC_NUMBER);

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(gmcDetailsDtoMock_whitespace, null, Create.class));

    // Then.
    MethodParameter parameter = thrown.getParameter();
    String executableName = parameter.getExecutable().getName();

    assertThat("Unexpected method name.", executableName, is("validate"));
    assertThat("Unexpected parameter type.", parameter.getParameter().getType(),
        is(GmcDetailsDTO.class));
  }

  @Test
  void validateShouldThrowExceptionWhenGmcNumberContainsWhitespace() {
    // Given.
    when(gmcDetailsDtoMock_whitespace.getGmcNumber()).thenReturn(WHITESPACE_GMC_NUMBER);

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(gmcDetailsDtoMock_whitespace, null, Create.class));

    // Then.
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(gmcDetailsDtoMock_whitespace));

    String errorMessage = "gmcNumber should not contain any whitespaces";
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        containsString(errorMessage));
  }

  @Test
  void validateDuringUpdateShouldThrowExceptionWhenOneGmcNumberAlreadyExists() {
    // Given.
    when(gmcDetailsDtoMock.getId()).thenReturn(GMC_DETAILS_ID);
    when(gmcDetailsDtoMock.getGmcNumber()).thenReturn(DEFAULT_GMC_NUMBER);
    when(gmcDetailsMock1.getId()).thenReturn(DIFFERENT_GMC_DETAILS_ID);

    when(gmcDetailsRepository.findByGmcNumberOrderById(DEFAULT_GMC_NUMBER))
        .thenReturn(Lists.newArrayList(gmcDetailsMock1));

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(gmcDetailsDtoMock, null, Create.class));

    // Then.
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(gmcDetailsDtoMock));

    String errorMessage = String
        .format("gmcNumber %s is not unique, there is currently 1 person with this number",
            DEFAULT_GMC_NUMBER);
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        containsString(errorMessage));
  }

  @Test
  void validateDuringUpdateShouldThrowExceptionWhenMultiplePeopleWithSameGmcNumbers() {
    // Given.
    when(gmcDetailsDtoMock.getId()).thenReturn(GMC_DETAILS_ID);
    when(gmcDetailsDtoMock.getGmcNumber()).thenReturn(DEFAULT_GMC_NUMBER);
    when(gmcDetailsMock1.getId()).thenReturn(DIFFERENT_GMC_DETAILS_ID);

    when(gmcDetailsRepository.findByGmcNumberOrderById(DEFAULT_GMC_NUMBER))
        .thenReturn(Lists.newArrayList(gmcDetailsMock1, gmcDetailsMock2));

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(gmcDetailsDtoMock, null, Create.class));

    // Then.
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(gmcDetailsDtoMock));

    String errorMessage = String
        .format("gmcNumber %s is not unique, there are currently 2 persons with this number",
            DEFAULT_GMC_NUMBER);
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        containsString(errorMessage));
  }

  @Test
  void validateDuringCreateShouldThrowExceptionWhenOneGmcNumberAlreadyExists() {
    // Given.
    when(gmcDetailsDtoMock.getId()).thenReturn(null);
    when(gmcDetailsDtoMock.getGmcNumber()).thenReturn(DEFAULT_GMC_NUMBER);
    when(gmcDetailsMock1.getId()).thenReturn(DIFFERENT_GMC_DETAILS_ID);

    when(gmcDetailsRepository.findByGmcNumberOrderById(DEFAULT_GMC_NUMBER))
        .thenReturn(Lists.newArrayList(gmcDetailsMock1));

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(gmcDetailsDtoMock, null, Create.class));

    // Then.
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(gmcDetailsDtoMock));

    String errorMessage = String
        .format("gmcNumber %s is not unique, there is currently 1 person with this number",
            DEFAULT_GMC_NUMBER);
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        containsString(errorMessage));
  }

  @Test
  void validateShouldNotThrowExceptionWhenNoGmcNumberExists() {
    // Given.
    when(gmcDetailsDtoMock.getGmcNumber()).thenReturn(DEFAULT_GMC_NUMBER);

    when(gmcDetailsRepository.findByGmcNumberOrderById(DEFAULT_GMC_NUMBER))
        .thenReturn(Lists.newArrayList());

    // When, then.
    assertDoesNotThrow(() -> validator.validate(gmcDetailsDtoMock, null, Create.class));
  }

  @Test
  void validationSkippedIfGmcNumberIsUnknownOrNA() {
    // Given.
    when(gmcDetailsDtoMock.getGmcNumber()).thenReturn(UNKNOWN_GMC_NUMBER);

    // When, then.
    assertDoesNotThrow(() -> validator.validate(gmcDetailsDtoMock, null, Create.class));
  }

  @Test
  void shouldThrowExceptionWhenGmcStatusNotExists() {
    // Given.
    when(gmcDetailsDtoMock.getGmcNumber()).thenReturn(DEFAULT_GMC_NUMBER);
    when(gmcDetailsDtoMock.getGmcStatus()).thenReturn(DEFAULT_GMC_STATUS);
    when(referenceService.isValueExists(GmcStatusDTO.class, DEFAULT_GMC_STATUS, true))
        .thenReturn(false);

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(gmcDetailsDtoMock, null, Create.class));

    // Then.
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(gmcDetailsDtoMock));

    String errorMessage = String.format("gmcStatus %s does not exist", DEFAULT_GMC_STATUS);
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        equalTo(errorMessage));
  }

  @Test
  void shouldNotThrowExceptionWhenGmcStatusExists() {
    // Given.
    when(gmcDetailsDtoMock.getGmcNumber()).thenReturn(DEFAULT_GMC_NUMBER);
    when(gmcDetailsDtoMock.getGmcStatus()).thenReturn(DEFAULT_GMC_STATUS);
    when(referenceService.isValueExists(GmcStatusDTO.class, DEFAULT_GMC_STATUS, true))
        .thenReturn(true);

    // When, then.
    assertDoesNotThrow(() -> validator.validate(gmcDetailsDtoMock, null, Create.class));
  }

  @Test
  void shouldReturnEmptyFieldErrorsWhenValidateForBulkIsOkay() {
    // Given.
    when(gmcDetailsDtoMock.getGmcNumber()).thenReturn(DEFAULT_GMC_NUMBER);
    when(gmcDetailsDtoMock.getGmcStatus()).thenReturn(DEFAULT_GMC_STATUS);
    when(referenceService.isValueExists(GmcStatusDTO.class, DEFAULT_GMC_STATUS, true))
        .thenReturn(true);
    when(gmcDetailsRepository.findByGmcNumberOrderById(DEFAULT_GMC_NUMBER))
        .thenReturn(Lists.newArrayList());

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(gmcDetailsDtoMock);
    // Then.
    assertThat("Error list should be empty.", fieldErrors.size(), equalTo(0));
  }

  @Test
  void shouldReturnAllFieldErrorsWhenValidateForBulkGetsErrors() {
    // Given.
    when(gmcDetailsDtoMock.getId()).thenReturn(GMC_DETAILS_ID);
    when(gmcDetailsDtoMock.getGmcNumber()).thenReturn(DEFAULT_GMC_NUMBER);
    when(gmcDetailsMock1.getId()).thenReturn(DIFFERENT_GMC_DETAILS_ID);
    when(gmcDetailsDtoMock.getGmcStatus()).thenReturn(DEFAULT_GMC_STATUS);

    when(gmcDetailsRepository.findByGmcNumberOrderById(DEFAULT_GMC_NUMBER))
        .thenReturn(Lists.newArrayList(gmcDetailsMock1));

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(gmcDetailsDtoMock);
    // Then.
    assertThat("Error list should contain 2 errors.", fieldErrors.size(), equalTo(2));
  }

  @Test
  void shouldNotThrowExceptionWhenUpdateWithValidFields() {
    GmcDetailsDTO dto = new GmcDetailsDTO();
    dto.setGmcNumber(DEFAULT_GMC_NUMBER);
    dto.setGmcStatus(DEFAULT_GMC_STATUS);

    GmcDetailsDTO originalDto = new GmcDetailsDTO();
    when(referenceService.isValueExists(GmcStatusDTO.class, DEFAULT_GMC_STATUS, true))
        .thenReturn(true);

    assertDoesNotThrow(() -> validator.validate(dto, originalDto, Update.class));
  }

  @Test
  void shouldThrowExceptionWhenUpdateWithInvalidFields() {
    GmcDetailsDTO dto = new GmcDetailsDTO();
    dto.setGmcNumber(WHITESPACE_GMC_NUMBER);
    dto.setGmcStatus(DEFAULT_GMC_STATUS);

    GmcDetailsDTO originalDto = new GmcDetailsDTO();
    when(referenceService.isValueExists(GmcStatusDTO.class, DEFAULT_GMC_STATUS, true))
        .thenReturn(false);

    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(dto, originalDto, Update.class));

    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        Matchers.is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), Matchers.is(dto));

    FieldError fieldError1 = new FieldError(DTO_NAME, FIELD_NAME_GMC_NUMBER,
        "gmcNumber should not contain any whitespaces");
    FieldError fieldError2 = new FieldError(DTO_NAME, FIELD_NAME_GMC_STATUS,
        String.format("gmcStatus %s does not exist", DEFAULT_GMC_STATUS));

    assertThat("Unexpected error count.", result.getFieldErrors().size(), Matchers.is(2));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError1, fieldError2));
  }

  @Test
  void shouldNotThrowExceptionWhenUpdateWithExistingFieldValues() {
    GmcDetailsDTO dto = new GmcDetailsDTO();
    dto.setGmcNumber(WHITESPACE_GMC_NUMBER);
    dto.setGmcStatus(DEFAULT_GMC_STATUS);

    GmcDetailsDTO originalDto = new GmcDetailsDTO();
    originalDto.setGmcNumber(WHITESPACE_GMC_NUMBER);
    originalDto.setGmcStatus(DEFAULT_GMC_STATUS);

    assertDoesNotThrow(() -> validator.validate(dto, originalDto, Update.class));
    verify(referenceService, never()).isValueExists(any(Class.class), anyString(), anyBoolean());
  }
}
