package com.transformuk.hee.tis.tcs.service.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;

class FileValidationUtilTest {

  private static final byte[] PDF_BYTES = FileSignature.PDF.bytes();
  private static final byte[] OLE2_BYTES = FileSignature.OLE2.bytes();
  private static final byte[] ZIP_BYTES = FileSignature.ZIP.bytes();
  private static final byte[] MZ_BYTES = FileSignature.MZ.bytes();

  @Test
  void isValidFileType_shouldReturnFalse_whenDocumentIsNull() {
    assertThat(FileValidationUtil.isValidFileType(null)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenExtensionIsDisallowedTxt() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "document.txt", "text/plain", "plain text content".getBytes());
    assertThat(FileValidationUtil.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenExtensionIsDisallowedExe() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "malware.exe", "application/x-msdownload", MZ_BYTES);
    assertThat(FileValidationUtil.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenFileHasNoExtension() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "nodotfile", "application/octet-stream", "content".getBytes());
    assertThat(FileValidationUtil.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenFilenameEndsWithTrailingDot() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "endotfile.", "application/octet-stream", "content".getBytes());
    assertThat(FileValidationUtil.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenFileTooSmallForMagicBytesCheck() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "document.pdf", "application/pdf",
        Arrays.copyOf(FileSignature.PDF.bytes(), 4));
    assertThat(FileValidationUtil.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenSpoofedPdfHasMzHeader() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "spoofed.pdf", "application/pdf", MZ_BYTES);
    assertThat(FileValidationUtil.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenSpoofedDocHasPdfHeader() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "spoofed.doc", "application/msword", PDF_BYTES);
    assertThat(FileValidationUtil.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenSpoofedXlsHasZipHeader() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "spoofed.xls", "application/vnd.ms-excel", ZIP_BYTES);
    assertThat(FileValidationUtil.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenSpoofedDocxHasOle2Header() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "spoofed.docx",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        OLE2_BYTES);
    assertThat(FileValidationUtil.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenSpoofedXlsxHasOle2Header() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "spoofed.xlsx",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        OLE2_BYTES);
    assertThat(FileValidationUtil.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnTrue_whenValidPdf() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "document.pdf", "application/pdf", PDF_BYTES);
    assertThat(FileValidationUtil.isValidFileType(file)).isTrue();
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("validFileTypeProvider")
  void isValidFileType_shouldReturnTrue_forValidFiles(String description, String filename,
      String contentType, byte[] content) {
    final MockMultipartFile file = new MockMultipartFile("file", filename, contentType, content);
    assertThat(FileValidationUtil.isValidFileType(file)).isTrue();
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
