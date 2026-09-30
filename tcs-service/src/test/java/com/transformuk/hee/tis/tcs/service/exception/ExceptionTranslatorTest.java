package com.transformuk.hee.tis.tcs.service.exception;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;

class ExceptionTranslatorTest {

  private ExceptionTranslator translator;
  private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    translator = new ExceptionTranslator();
    objectMapper = new ObjectMapper();
  }

  @Test
  void shouldIdentifyFieldWhenEnumValueInvalid() {
    HttpMessageNotReadableException ex = readException("{\"status\":\"NOT_A_STATUS\"}");

    ErrorVM error = translator.processBadEnumError(ex);

    assertThat(error.getMessage()).isEqualTo(ErrorConstants.ERR_VALIDATION);
    assertThat(error.getFieldErrors()).hasSize(1);
    FieldErrorVM fieldError = error.getFieldErrors().get(0);
    assertThat(fieldError.getObjectName()).isNull();
    assertThat(fieldError.getField()).isEqualTo("status");
    assertThat(fieldError.getMessage())
        .isEqualTo("The value provided is not valid for field 'status'");
  }

  @Test
  void shouldIdentifyInnermostFieldWhenNestedValueInvalid() {
    HttpMessageNotReadableException ex = readException(
        "{\"children\":[{\"status\":\"CURRENT\"},{\"status\":\"NOT_A_STATUS\"}]}");

    ErrorVM error = translator.processBadEnumError(ex);

    FieldErrorVM fieldError = error.getFieldErrors().get(0);
    assertThat(fieldError.getField()).isEqualTo("status");
    assertThat(fieldError.getMessage())
        .isEqualTo("The value provided is not valid for field 'status'");
  }

  @Test
  void shouldIdentifyFieldWhenTypeInvalid() {
    HttpMessageNotReadableException ex = readException("{\"count\":\"not-a-number\"}");

    ErrorVM error = translator.processBadEnumError(ex);

    FieldErrorVM fieldError = error.getFieldErrors().get(0);
    assertThat(fieldError.getField()).isEqualTo("count");
    assertThat(fieldError.getMessage())
        .isEqualTo("The value provided is not valid for field 'count'");
  }

  @Test
  void shouldReturnGenericMessageWhenJsonMalformed() {
    HttpMessageNotReadableException ex = readException("{\"status\":");

    ErrorVM error = translator.processBadEnumError(ex);

    assertThat(error.getMessage()).isEqualTo(ErrorConstants.ERR_VALIDATION);
    assertThat(error.getFieldErrors()).hasSize(1);
    FieldErrorVM fieldError = error.getFieldErrors().get(0);
    assertThat(fieldError.getField()).isNull();
    assertThat(fieldError.getMessage()).isEqualTo("The request body could not be read");
  }

  @Test
  void shouldReturnGenericMessageWhenNoCause() {
    HttpMessageNotReadableException ex = new HttpMessageNotReadableException(
        "Required request body is missing", mock(HttpInputMessage.class));

    ErrorVM error = translator.processBadEnumError(ex);

    FieldErrorVM fieldError = error.getFieldErrors().get(0);
    assertThat(fieldError.getField()).isNull();
    assertThat(fieldError.getMessage()).isEqualTo("The request body could not be read");
  }

  /**
   * Deserialize the given JSON and wrap the resulting Jackson exception as Spring would.
   */
  private HttpMessageNotReadableException readException(String json) {
    JsonProcessingException cause = assertThrows(JsonProcessingException.class,
        () -> objectMapper.readValue(json, TestDto.class));
    return new HttpMessageNotReadableException("JSON parse error: " + cause.getMessage(), cause,
        mock(HttpInputMessage.class));
  }

  static class TestDto {

    public Status status;
    public Integer count;
    public List<TestDto> children;
  }
}
