package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.GdcStatusDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.IdProjection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class GdcDetailsValidatorTest {

  private static final String DEFAULT_GDC_NUMBER = "DEFAULT_GDC_NUMBER";
  private static final String WHITESPACE_GDC_NUMBER = "WHITESPACE_GDC_NUMBER  ";
  private static final String UNKNOWN_GDC_NUMBER = "UNKNOWN";
  private static final String DEFAULT_GDC_STATUS = "DEFAULT_GDC_STATUS";
  private static final Long GDC_DETAILS_ID = 123L;
  private static final Long DIFFERENT_GDC_DETAILS_ID = 999L;

  private static final String DTO_NAME = GdcDetailsDTO.class.getSimpleName();

  @InjectMocks
  private GdcDetailsValidator validator;

  @Mock
  private ReferenceServiceImpl referenceService;

  @Mock
  private GdcDetailsRepository GdcDetailsRepository;

  @Mock
  private GdcDetailsDTO GdcDetailsDTOMock, GdcDetailsDTOMock_whitespace;

  @Mock
  private GdcDetails GdcDetailsMock1, GdcDetailsMock2;

  @Test
  void validateShouldThrowExceptionWhenGdcNumberContainsWhitespace() {
    // Given.
    when(GdcDetailsDTOMock_whitespace.getGdcNumber()).thenReturn(WHITESPACE_GDC_NUMBER);

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(GdcDetailsDTOMock_whitespace));

    // Then.
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(GdcDetailsDTOMock_whitespace));

    String errorMessage = "gdcNumber should not contain any whitespaces";
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        containsString(errorMessage));
  }

  @Test
  void validateDuringUpdateShouldThrowExceptionWhenOneGdcNumberAlreadyExists() {
    // Given.
    when(GdcDetailsDTOMock.getId()).thenReturn(GDC_DETAILS_ID);
    when(GdcDetailsDTOMock.getGdcNumber()).thenReturn(DEFAULT_GDC_NUMBER);

    IdProjection idProjection = new IdProjection() {
      @Override
      public Long getId() {
        return DIFFERENT_GDC_DETAILS_ID;
      }
    };

    when(GdcDetailsRepository.findByGdcNumber(DEFAULT_GDC_NUMBER))
        .thenReturn(Lists.newArrayList(idProjection));

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(GdcDetailsDTOMock));

    // Then.
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(GdcDetailsDTOMock));

    String errorMessage = String
        .format("gdcNumber %s is not unique, there is currently 1 person with this number",
            DEFAULT_GDC_NUMBER);
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        containsString(errorMessage));
  }

  @Test
  void validateDuringUpdateShouldThrowExceptionWhenMultiplePeopleWithSameGdcNumbers() {
    // Given.
    when(GdcDetailsDTOMock.getId()).thenReturn(GDC_DETAILS_ID);
    when(GdcDetailsDTOMock.getGdcNumber()).thenReturn(DEFAULT_GDC_NUMBER);

    IdProjection idProjection = new IdProjection() {
      @Override
      public Long getId() {
        return DIFFERENT_GDC_DETAILS_ID;
      }
    };

    when(GdcDetailsRepository.findByGdcNumber(DEFAULT_GDC_NUMBER))
        .thenReturn(Lists.newArrayList(idProjection, idProjection));

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(GdcDetailsDTOMock));

    // Then.
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(GdcDetailsDTOMock));

    String errorMessage = String
        .format("gdcNumber %s is not unique, there are currently 2 persons with this number",
            DEFAULT_GDC_NUMBER);
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        containsString(errorMessage));
  }

  @Test
  void validateDuringCreateShouldThrowExceptionWhenOneGdcNumberAlreadyExists() {
    // Given.
    when(GdcDetailsDTOMock.getId()).thenReturn(null);
    when(GdcDetailsDTOMock.getGdcNumber()).thenReturn(DEFAULT_GDC_NUMBER);

    IdProjection idProjection = new IdProjection() {
      @Override
      public Long getId() {
        return DIFFERENT_GDC_DETAILS_ID;
      }
    };

    when(GdcDetailsRepository.findByGdcNumber(DEFAULT_GDC_NUMBER))
        .thenReturn(Lists.newArrayList(idProjection));

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(GdcDetailsDTOMock));

    // Then.
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(GdcDetailsDTOMock));

    String errorMessage = String
        .format("gdcNumber %s is not unique, there is currently 1 person with this number",
            DEFAULT_GDC_NUMBER);
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        containsString(errorMessage));
  }

  @Test
  void validateShouldNotThrowExceptionWhenNoGdcNumberExists() {
    // Given.
    when(GdcDetailsDTOMock.getGdcNumber()).thenReturn(DEFAULT_GDC_NUMBER);

    when(GdcDetailsRepository.findByGdcNumber(DEFAULT_GDC_NUMBER))
        .thenReturn(Lists.newArrayList());

    // When, then.
    assertDoesNotThrow(() -> validator.validate(GdcDetailsDTOMock));
  }

  @Test
  void validationSkippedIfGdcNumberIsUnknownOrNA() {
    // Given.
    when(GdcDetailsDTOMock.getGdcNumber()).thenReturn(UNKNOWN_GDC_NUMBER);

    // When, then.
    assertDoesNotThrow(() -> validator.validate(GdcDetailsDTOMock));
  }

  @Test
  void shouldThrowExceptionWhenGdcStatusNotExists() {
    // Given.
    when(GdcDetailsDTOMock.getGdcNumber()).thenReturn(DEFAULT_GDC_NUMBER);
    when(GdcDetailsDTOMock.getGdcStatus()).thenReturn(DEFAULT_GDC_STATUS);
    when(referenceService.isValueExists(GdcStatusDTO.class, DEFAULT_GDC_STATUS, true))
        .thenReturn(false);

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(GdcDetailsDTOMock));

    // Then.
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(GdcDetailsDTOMock));

    String errorMessage = String.format("gdcStatus %s does not exist", DEFAULT_GDC_STATUS);
    assertThat("Unexpected error message.", result.getFieldErrors().get(0).getDefaultMessage(),
        equalTo(errorMessage));
  }

  @Test
  void shouldNotThrowExceptionWhenGdcStatusExists() {
    // Given.
    when(GdcDetailsDTOMock.getGdcNumber()).thenReturn(DEFAULT_GDC_NUMBER);
    when(GdcDetailsDTOMock.getGdcStatus()).thenReturn(DEFAULT_GDC_STATUS);
    when(referenceService.isValueExists(GdcStatusDTO.class, DEFAULT_GDC_STATUS, true))
        .thenReturn(true);

    // When, then.
    assertDoesNotThrow(() -> validator.validate(GdcDetailsDTOMock));
  }

  // bulk upload
  @Test
  void shouldReturnEmptyFieldErrorsWhenValidateForBulkIsOkay() {
    // Given.
    when(GdcDetailsDTOMock.getGdcNumber()).thenReturn(DEFAULT_GDC_NUMBER);
    when(GdcDetailsDTOMock.getGdcStatus()).thenReturn(DEFAULT_GDC_STATUS);
    when(referenceService.isValueExists(GdcStatusDTO.class, DEFAULT_GDC_STATUS, true))
        .thenReturn(true);
    when(GdcDetailsRepository.findByGdcNumber(DEFAULT_GDC_NUMBER))
        .thenReturn(Lists.newArrayList());

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(GdcDetailsDTOMock);
    // Then.
    assertThat("Error list should be empty.", fieldErrors.size(), equalTo(0));
  }

  @Test
  void shouldReturnAllFieldErrorsWhenValidateForBulkGetsErrors() {
    // Given.
    when(GdcDetailsDTOMock.getId()).thenReturn(GDC_DETAILS_ID);
    when(GdcDetailsDTOMock.getGdcNumber()).thenReturn(DEFAULT_GDC_NUMBER);
    when(GdcDetailsDTOMock.getGdcStatus()).thenReturn(DEFAULT_GDC_STATUS);

    IdProjection idProjection = new IdProjection() {
      @Override
      public Long getId() {
        return DIFFERENT_GDC_DETAILS_ID;
      }
    };

    when(GdcDetailsRepository.findByGdcNumber(DEFAULT_GDC_NUMBER))
        .thenReturn(Lists.newArrayList(idProjection));

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(GdcDetailsDTOMock);
    // Then.
    assertThat("Error list should contain 2 errors.", fieldErrors.size(), equalTo(2));
  }
}
