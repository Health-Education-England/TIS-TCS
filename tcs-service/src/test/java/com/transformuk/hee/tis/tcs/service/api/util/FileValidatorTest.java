package com.transformuk.hee.tis.tcs.service.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.mock.web.MockMultipartFile;

class FileValidatorTest {

  private static final byte[] PDF_BYTES  = {(byte) 0x25, (byte) 0x50, (byte) 0x44, (byte) 0x46,
                                     (byte) 0x2D, (byte) 0x31, (byte) 0x2E, (byte) 0x34}; // PDF
  private static final byte[] OLE2_BYTES = {(byte) 0xD0, (byte) 0xCF, (byte) 0x11, (byte) 0xE0,
                                     (byte) 0xA1, (byte) 0xB1, (byte) 0x1A, (byte) 0xE1}; // OLE2
  private static final byte[] ZIP_BYTES  = {(byte) 0x50, (byte) 0x4B, (byte) 0x03, (byte) 0x04,
                                     (byte) 0x14, (byte) 0x00, (byte) 0x06, (byte) 0x00}; // PK
  private static final byte[] MZ_BYTES   = {(byte) 0x4D, (byte) 0x5A, (byte) 0x00, (byte) 0x00}; // MZ (exe)

  @Test
  void extractFileExtension_shouldReturnExtension_whenFilenameIsSimple() {
    assertThat(FileValidator.extractFileExtension("document.pdf")).isEqualTo("pdf");
  }

  @Test
  void extractFileExtension_shouldReturnLastExtension_whenFilenameHasMultipleDots() {
    assertThat(FileValidator.extractFileExtension("my.report.2026.xlsx")).isEqualTo("xlsx");
  }

  @Test
  void extractFileExtension_shouldPreserveCase() {
    assertThat(FileValidator.extractFileExtension("document.PDF")).isEqualTo("PDF");
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @NullAndEmptySource
  @ValueSource(strings = {"filename", "filename.", "."})
  void extractFileExtension_shouldReturnEmpty_whenNoExtensionFound(String filename) {
    assertThat(FileValidator.extractFileExtension(filename)).isEmpty();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenDocumentIsNull() {
    assertThat(FileValidator.isValidFileType(null)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenExtensionIsDisallowedTxt() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "document.txt", "text/plain", "plain text content".getBytes());
    assertThat(FileValidator.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenExtensionIsDisallowedExe() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "malware.exe", "application/x-msdownload", MZ_BYTES);
    assertThat(FileValidator.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenExtensionIsDisallowedJpg() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "image.jpg", "image/jpeg",
        new byte[]{(byte) 0xFF, (byte) 0xD8, (byte) 0xFF, (byte) 0xE0});
    assertThat(FileValidator.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenFileHasNoExtension() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "nodotfile", "application/octet-stream", "content".getBytes());
    assertThat(FileValidator.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenFilenameEndsWithTrailingDot() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "endotfile.", "application/octet-stream", "content".getBytes());
    assertThat(FileValidator.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenFileTooSmallForMagicBytesCheck() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "document.pdf", "application/pdf",
        new byte[]{(byte) 0x25, (byte) 0x50});
    assertThat(FileValidator.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenSpoofedPdfHasMzHeader() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "spoofed.pdf", "application/pdf", MZ_BYTES);
    assertThat(FileValidator.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenSpoofedDocHasPdfHeader() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "spoofed.doc", "application/msword", PDF_BYTES);
    assertThat(FileValidator.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenSpoofedXlsHasZipHeader() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "spoofed.xls", "application/vnd.ms-excel", ZIP_BYTES);
    assertThat(FileValidator.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenSpoofedDocxHasOle2Header() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "spoofed.docx",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        OLE2_BYTES);
    assertThat(FileValidator.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnFalse_whenSpoofedXlsxHasOle2Header() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "spoofed.xlsx",
        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        OLE2_BYTES);
    assertThat(FileValidator.isValidFileType(file)).isFalse();
  }

  @Test
  void isValidFileType_shouldReturnTrue_whenValidPdf() {
    final MockMultipartFile file = new MockMultipartFile(
        "file", "document.pdf", "application/pdf", PDF_BYTES);
    assertThat(FileValidator.isValidFileType(file)).isTrue();
  }

  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("validFileTypeProvider")
  void isValidFileType_shouldReturnTrue_forValidFiles(String description, String filename,
      String contentType, byte[] content) {
    final MockMultipartFile file = new MockMultipartFile("file", filename, contentType, content);
    assertThat(FileValidator.isValidFileType(file)).isTrue();
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
