package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
  private static final byte[] PDF_BYTES = new byte[]{
      (byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46, (byte) 0x2D
  };
  private static final byte[] DOC_BYTES = new byte[]{
      (byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0,
      (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1
  };
  private static final byte[] ZIP_BYTES = new byte[]{
      (byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04
  };
  private static final byte[] MZ_BYTES = new byte[]{
      (byte) 0x4D, (byte) 0x5A
  };

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
            "application/x-msdownload", MZ_BYTES),
        new MockMultipartFile(FORM_FIELD, "nodotfile",
            "application/pdf", PDF_BYTES),
        new MockMultipartFile(FORM_FIELD, null,
            "application/pdf", PDF_BYTES),
        new MockMultipartFile(FORM_FIELD, "spoofed.pdf",
            "application/pdf", MZ_BYTES)
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
            "application/pdf", PDF_BYTES),
        new MockMultipartFile(FORM_FIELD, "document.doc",
            "application/msword", DOC_BYTES),
        new MockMultipartFile(FORM_FIELD, "spreadsheet.xls",
            "application/vnd.ms-excel", DOC_BYTES),
        new MockMultipartFile(FORM_FIELD, "document.docx",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            ZIP_BYTES),
        new MockMultipartFile(FORM_FIELD, "spreadsheet.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            ZIP_BYTES)
    );
  }

  @Test
  void validate_shouldPopulateFieldErrorOnDocument_whenFileTypeIsInvalid() {
    MultipartFile file = new MockMultipartFile(FORM_FIELD, "malware.exe",
        "application/x-msdownload", MZ_BYTES);

    ValidationException ex = assertThrows(ValidationException.class, () -> validator.validate(file));
    assertThat(ex.getBindingResult().hasErrors()).isTrue();
    FieldError fieldError = ex.getBindingResult().getFieldError("document");
    assertThat(fieldError).isNotNull();
    assertThat(fieldError.getDefaultMessage()).contains(".doc", ".pdf", ".xls", ".xlsx");
  }
}
