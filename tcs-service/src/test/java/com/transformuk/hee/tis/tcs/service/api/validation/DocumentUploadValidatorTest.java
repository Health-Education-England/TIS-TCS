package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.transformuk.hee.tis.tcs.service.api.util.FileSignature;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

class DocumentUploadValidatorTest {

  private static final String FORM_FIELD = "document";

  private DocumentUploadValidator validator;

  @BeforeEach
  void setUp() {
    validator = new DocumentUploadValidator();
  }

  @Test
  void validate_shouldThrowValidationException_whenFileIsNull() {
    assertThrows(ValidationException.class, () -> validator.validate(null));
  }

  @ParameterizedTest
  @MethodSource("invalidFiles")
  void validate_shouldThrowValidationException_whenFileTypeIsInvalid(MultipartFile file) {
    assertThrows(ValidationException.class, () -> validator.validate(file));
  }

  static Stream<MultipartFile> invalidFiles() {
    return Stream.of(
        new MockMultipartFile(FORM_FIELD, "malware.exe",
            "application/x-msdownload", FileSignature.MZ.bytes()),
        new MockMultipartFile(FORM_FIELD, "nodotfile",
            "application/pdf", FileSignature.PDF.bytes()),
        new MockMultipartFile(FORM_FIELD, null,
            "application/pdf", FileSignature.PDF.bytes()),
        new MockMultipartFile(FORM_FIELD, "spoofed.pdf",
            "application/pdf", FileSignature.MZ.bytes())
    );
  }

  @ParameterizedTest
  @MethodSource("validFiles")
  void validate_shouldNotThrow_whenFileTypeIsValid(MultipartFile file) {
    assertDoesNotThrow(() -> validator.validate(file));
  }

  static Stream<MultipartFile> validFiles() {
    return Stream.of(
        new MockMultipartFile(FORM_FIELD, "document.pdf",
            "application/pdf", FileSignature.PDF.bytes()),
        new MockMultipartFile(FORM_FIELD, "document.doc",
            "application/msword", FileSignature.OLE2.bytes()),
        new MockMultipartFile(FORM_FIELD, "spreadsheet.xls",
            "application/vnd.ms-excel", FileSignature.OLE2.bytes()),
        new MockMultipartFile(FORM_FIELD, "document.docx",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            FileSignature.ZIP.bytes()),
        new MockMultipartFile(FORM_FIELD, "spreadsheet.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            FileSignature.ZIP.bytes())
    );
  }

  @Test
  void validate_shouldPopulateFieldErrorOnDocument_whenFileTypeIsInvalid() {
    MultipartFile file = new MockMultipartFile(FORM_FIELD, "malware.exe",
        "application/x-msdownload", FileSignature.MZ.bytes());

    ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(file));
    assertThat(ex.getBindingResult().hasErrors()).isTrue();
    FieldError fieldError = ex.getBindingResult().getFieldError("document");
    assertThat(fieldError).isNotNull();
    assertThat(fieldError.getDefaultMessage()).contains(".doc", ".pdf", ".xls", ".xlsx");
  }
}
