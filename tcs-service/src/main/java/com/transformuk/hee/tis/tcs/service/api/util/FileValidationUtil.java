package com.transformuk.hee.tis.tcs.service.api.util;

import java.io.IOException;
import java.util.Locale;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class for validating uploaded files.
 */
public class FileValidationUtil {

  private static final Logger LOG = LoggerFactory.getLogger(FileValidationUtil.class);
  private static final Tika TIKA = new Tika();

  private FileValidationUtil() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Validates the file type using extension policy and Tika content detection.
   *
   * @param documentParam the file to validate
   * @return true if all validation layers pass, else false
   */
  public static boolean isValidFileType(final MultipartFile documentParam) {
    if (documentParam == null) {
      LOG.warn("Received null document for file type validation");
      return false;
    }

    final String filename = documentParam.getOriginalFilename();
    final String fileExtension = extractFileExtension(filename).toLowerCase(Locale.ROOT);
    final DocumentUploadFileType expectedType = DocumentUploadFileType
        .fromExtension(fileExtension).orElse(null);

    if (expectedType == null) {
      LOG.warn("Rejected upload due to disallowed file extension");
      return false;
    }

    try (var inputStream = documentParam.getInputStream()) {
      final String detectedType = TIKA.detect(inputStream, filename);

      if (!DocumentUploadFileType.allowedMediaTypes().contains(detectedType)) {
        LOG.warn("Rejected upload due to disallowed media type");
        return false;
      }

      if (!expectedType.mediaType().equals(detectedType)) {
        LOG.warn("Rejected upload due to mismatch between extension and detected media type");
        return false;
      }
    } catch (IOException ex) {
      LOG.error("Failed to detect file type", ex);
      return false;
    }

    return true;
  }

  /**
   * Extracts the file extension from a filename. Returns empty string if filename is null, empty,
   * or has no extension.
   *
   * @param filename the filename to extract extension from
   * @return the file extension or empty string
   */
  private static String extractFileExtension(final String filename) {
    if (!StringUtils.hasLength(filename) || filename.lastIndexOf('.') == 0) {
      return "";
    }

    final String extension = StringUtils.getFilenameExtension(filename);
    return extension == null ? "" : extension;
  }
}
