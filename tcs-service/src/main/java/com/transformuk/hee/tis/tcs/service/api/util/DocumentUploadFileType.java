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

  /**
   * Creates a document upload file type definition.
   *
   * @param extension the file extension (without the dot)
   * @param mediaType the MIME type of the file
   */
  DocumentUploadFileType(final String extension, final String mediaType) {
    this.extension = extension;
    this.mediaType = mediaType;
  }

  /**
   * Returns the file extension associated with this upload type.
   *
   * @return the file extension (without the dot)
   */
  public String extension() {
    return extension;
  }

  /**
   * Returns the MIME type associated with this upload type.
   *
   * @return the MIME type of the file
   */
  public String mediaType() {
    return mediaType;
  }

  /**
   * Finds the upload file type for a given file extension.
   *
   * @param extension the file extension (without the dot)
   * @return the DocumentUploadFileType for the given extension, or empty if not found
   */
  public static Optional<DocumentUploadFileType> fromExtension(final String extension) {
    if (extension == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(BY_EXTENSION.get(extension.toLowerCase(Locale.ROOT)));
  }

  /**
   * Returns all allowed upload file extensions.
   *
   * @return the set of allowed file extensions (without the dot)
   */
  public static Set<String> allowedExtensions() {
    return Arrays.stream(values())
        .map(DocumentUploadFileType::extension)
        .collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Returns all allowed upload MIME types.
   *
   * @return the set of allowed MIME types
   */
  public static Set<String> allowedMediaTypes() {
    return Arrays.stream(values())
        .map(DocumentUploadFileType::mediaType)
        .collect(Collectors.toUnmodifiableSet());
  }
}

