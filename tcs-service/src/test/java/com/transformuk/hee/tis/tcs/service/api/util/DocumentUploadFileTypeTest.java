package com.transformuk.hee.tis.tcs.service.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DocumentUploadFileTypeTest {

  @Test
  void fromExtension_shouldReturnEmpty_whenExtensionIsNull() {
    Optional<DocumentUploadFileType> fileType = DocumentUploadFileType.fromExtension(null);

    assertThat(fileType).isEmpty();
  }

  @Test
  void fromExtension_shouldReturnEmpty_whenExtensionIsUnknown() {
    Optional<DocumentUploadFileType> fileType = DocumentUploadFileType.fromExtension("txt");

    assertThat(fileType).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"pdf", "doc", "xls", "docx", "xlsx"})
  void fromExtension_shouldResolveCaseInsensitiveExtensions(String extension) {
    Optional<DocumentUploadFileType> fileType = DocumentUploadFileType
        .fromExtension(extension.toUpperCase());

    assertThat(fileType).isNotEmpty();
  }

  @Test
  void allowedExtensions_shouldContainOnlyAllowedUploadExtensions() {
    Set<String> extensions = DocumentUploadFileType.allowedExtensions();

    assertThat(extensions)
        .containsExactlyInAnyOrder("pdf", "doc", "xls", "docx", "xlsx");
  }

  @Test
  void allowedMediaTypes_shouldContainOnlyAllowedUploadMediaTypes() {
    Set<String> mediaTypes = DocumentUploadFileType.allowedMediaTypes();

    assertThat(mediaTypes)
        .containsExactlyInAnyOrder(
            "application/pdf",
            "application/msword",
            "application/vnd.ms-excel",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
  }
}

