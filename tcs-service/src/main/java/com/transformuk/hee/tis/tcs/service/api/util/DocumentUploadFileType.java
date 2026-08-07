package com.transformuk.hee.tis.tcs.service.api.util;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Upload file-type policy definitions used by validation.
 */
public enum DocumentUploadFileType {
  PDF("pdf", "application/pdf"),
  DOC("doc", "application/msword"),
  XLS("xls", "application/vnd.ms-excel"),
  DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
  XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

  private static final Map<String, DocumentUploadFileType> BY_EXTENSION = Arrays.stream(values())
      .collect(Collectors.toUnmodifiableMap(DocumentUploadFileType::extension,
          Function.identity()));

  private final String extension;
  private final String mediaType;
  DocumentUploadFileType(final String extension, final String mediaType) {
    this.extension = extension;
    this.mediaType = mediaType;
  }

  public String extension() {
    return extension;
  }

  public String mediaType() {
    return mediaType;
  }

  public static Optional<DocumentUploadFileType> fromExtension(final String extension) {
    if (extension == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(BY_EXTENSION.get(extension.toLowerCase(Locale.ROOT)));
  }

  public static Set<String> allowedExtensions() {
    return Arrays.stream(values())
        .map(DocumentUploadFileType::extension)
        .collect(Collectors.toUnmodifiableSet());
  }

  public static Set<String> allowedMediaTypes() {
    return Arrays.stream(values())
        .map(DocumentUploadFileType::mediaType)
        .collect(Collectors.toUnmodifiableSet());
  }
}

