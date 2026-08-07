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
public enum FileSignature {
  PDF("pdf", "application/pdf", true),
  DOC("doc", "application/msword", true),
  XLS("xls", "application/vnd.ms-excel", true),
  DOCX("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", true),
  XLSX("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", true),
  MZ("exe", "application/x-msdownload", false);

  private static final Map<String, FileSignature> BY_EXTENSION = Arrays.stream(values())
      .collect(Collectors.toUnmodifiableMap(FileSignature::extension, Function.identity()));

  private final String extension;
  private final String mediaType;
  private final boolean allowedUploadType;

  FileSignature(final String extension, final String mediaType, final boolean allowedUploadType) {
    this.extension = extension;
    this.mediaType = mediaType;
    this.allowedUploadType = allowedUploadType;
  }

  public String extension() {
    return extension;
  }

  public String mediaType() {
    return mediaType;
  }

  public boolean allowedUploadType() {
    return allowedUploadType;
  }


  public static Optional<FileSignature> fromExtension(final String extension) {
    if (extension == null) {
      return Optional.empty();
    }
    return Optional.ofNullable(BY_EXTENSION.get(extension.toLowerCase(Locale.ROOT)));
  }

  public static Set<String> allowedExtensions() {
    return Arrays.stream(values())
        .filter(FileSignature::allowedUploadType)
        .map(FileSignature::extension)
        .collect(Collectors.toUnmodifiableSet());
  }

  public static Set<String> allowedMediaTypes() {
    return Arrays.stream(values())
        .filter(FileSignature::allowedUploadType)
        .map(FileSignature::mediaType)
        .collect(Collectors.toUnmodifiableSet());
  }
}
