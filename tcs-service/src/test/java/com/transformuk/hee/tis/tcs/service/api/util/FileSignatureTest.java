package com.transformuk.hee.tis.tcs.service.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FileSignatureTest {

  @Test
  void fromExtension_shouldReturnEmpty_whenExtensionIsNull() {
    Optional<FileSignature> fileType = FileSignature.fromExtension(null);

    assertThat(fileType).isEmpty();
  }

  @Test
  void fromExtension_shouldReturnEmpty_whenExtensionIsUnknown() {
    Optional<FileSignature> fileType = FileSignature.fromExtension("txt");

    assertThat(fileType).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"pdf", "doc", "xls", "docx", "xlsx"})
  void fromExtension_shouldResolveCaseInsensitiveExtensions(String extension) {
    Optional<FileSignature> fileType = FileSignature.fromExtension(extension.toUpperCase());

    assertThat(fileType).isNotEmpty();
  }

  @Test
  void allowedExtensions_shouldContainOnlyAllowedUploadExtensions() {
    Set<String> extensions = FileSignature.allowedExtensions();

    assertThat(extensions)
        .containsExactlyInAnyOrder(
            FileSignature.PDF.extension(),
            FileSignature.DOC.extension(),
            FileSignature.XLS.extension(),
            FileSignature.DOCX.extension(),
            FileSignature.XLSX.extension())
        .doesNotContain(FileSignature.MZ.extension());
  }

  @Test
  void allowedMediaTypes_shouldContainOnlyAllowedUploadMediaTypes() {
    Set<String> mediaTypes = FileSignature.allowedMediaTypes();

    assertThat(mediaTypes)
        .containsExactlyInAnyOrder(
            FileSignature.PDF.mediaType(),
            FileSignature.DOC.mediaType(),
            FileSignature.XLS.mediaType(),
            FileSignature.DOCX.mediaType(),
            FileSignature.XLSX.mediaType())
        .doesNotContain(FileSignature.MZ.mediaType());
  }
}
