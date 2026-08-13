package com.transformuk.hee.tis.tcs.service.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;

class FileValidationUtilTest {

  private static final byte[] PDF_BYTES = new byte[]{
      (byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46, (byte) 0x2D
  };
  private static final byte[] OLE2_BYTES = new byte[]{
      (byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0,
      (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1
  };
  private static final byte[] ZIP_BYTES = new byte[]{
      (byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04
  };
  private static final byte[] MZ_BYTES = new byte[]{
      (byte) 0x4D, (byte) 0x5A
  };

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("invalidFileTypeProvider")
  void isValidFileType_shouldReturnFalse_forInvalidFiles(String description,
      MockMultipartFile file) {
    assertThat(FileValidationUtil.isValidFileType(file)).as(description).isFalse();
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("validFileTypeProvider")
  void isValidFileType_shouldReturnTrue_forValidFiles(String description, String filename,
      String contentType, byte[] content) {
    final MockMultipartFile file = new MockMultipartFile("file", filename, contentType, content);
    assertThat(FileValidationUtil.isValidFileType(file)).as(description).isTrue();
  }

  static Stream<Arguments> invalidFileTypeProvider() {
    return Stream.of(
        Arguments.of("Document is null", null),
        Arguments.of("Disallowed extension exe",
            new MockMultipartFile("file", "malware.exe", "application/x-msdownload", MZ_BYTES)),
        Arguments.of("No extension",
            new MockMultipartFile("file", "nodotfile", "application/octet-stream",
                "content".getBytes())),
        Arguments.of("Trailing dot extension",
            new MockMultipartFile("file", "endotfile.", "application/octet-stream",
                "content".getBytes())),
        Arguments.of("Spoofed pdf has mz header",
            new MockMultipartFile("file", "spoofed.pdf", "application/pdf", MZ_BYTES)),
        Arguments.of("Spoofed doc has pdf header",
            new MockMultipartFile("file", "spoofed.doc", "application/msword", PDF_BYTES))
    );
  }

  static Stream<Arguments> validFileTypeProvider() {
    return Stream.of(
        Arguments.of("Valid DOC file", "document.doc", "application/msword", OLE2_BYTES),
        Arguments.of("Valid XLS file", "spreadsheet.xls", "application/vnd.ms-excel", OLE2_BYTES),
        Arguments.of("Valid DOCX file", "document.docx",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", ZIP_BYTES),
        Arguments.of("Valid XLSX file", "spreadsheet.xlsx",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", ZIP_BYTES),
        Arguments.of("Case-insensitive extension", "document.PDF", "application/pdf", PDF_BYTES),
        Arguments.of("Multiple dots in filename", "document.one.pdf", "application/pdf", PDF_BYTES)
    );
  }
}
