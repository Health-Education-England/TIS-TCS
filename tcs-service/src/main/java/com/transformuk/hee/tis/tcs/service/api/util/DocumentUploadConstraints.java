package com.transformuk.hee.tis.tcs.service.api.util;

import java.util.Map;
import java.util.Set;

/**
 * Shared allow-list of file extensions for upload validation across the application.
 */
public final class DocumentUploadConstraints {

  public static final Set<String> ALLOWED_FILE_EXTENSIONS = Set.of(
      "pdf",
      "doc",
      "docx",
      "xls",
      "xlsx"
  );

  public static final Set<String> ALLOWED_MEDIA_TYPES = Set.of(
      "application/pdf",
      "application/msword",
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
      "application/vnd.ms-excel",
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
  );

  public static final Map<String, FileSignature> EXTENSION_SIGNATURES = Map.of(
      "pdf", FileSignature.PDF,
      "doc", FileSignature.OLE2,
      "xls", FileSignature.OLE2,
      "docx", FileSignature.ZIP,
      "xlsx", FileSignature.ZIP
  );

  private DocumentUploadConstraints() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }
}
