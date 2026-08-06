package com.transformuk.hee.tis.tcs.service.api.util;

import java.io.IOException;
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
   * Validates the file type: 1. Extension check 2. Magic bytes check (file signature) 3. MIME type
   * check
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

    // Extension check
    final String fileExtension = extractFileExtension(filename);
    if (!DocumentUploadConstraints.ALLOWED_FILE_EXTENSIONS.contains(fileExtension.toLowerCase())) {
      LOG.warn("Rejected upload due to disallowed file extension");
      return false;
    }

    // Magic bytes check
    try {
      if (!isValidMagicBytes(documentParam, fileExtension)) {
        LOG.warn("Rejected upload due to invalid file signature");
        return false;
      }
    } catch (IOException ex) {
      LOG.error("Failed to validate file signature", ex);
      return false;
    }

    // MIME type check via Tika
    try (var inputStream = documentParam.getInputStream()) {
      final String detectedType = TIKA.detect(inputStream, filename);

      if (!DocumentUploadConstraints.ALLOWED_MEDIA_TYPES.contains(detectedType)) {
        LOG.warn("Rejected upload due to disallowed media type");
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

  /**
   * Validates the file by checking its magic bytes (file signature).
   *
   * @param documentParam the file to validate
   * @param fileExtension the file extension
   * @return true if magic bytes match the expected signature, false otherwise
   * @throws IOException if unable to read the file header
   */
  private static boolean isValidMagicBytes(final MultipartFile documentParam,
      final String fileExtension) throws IOException {
    final FileSignature expectedSignature = DocumentUploadConstraints
        .EXTENSION_SIGNATURES
        .get(fileExtension.toLowerCase());
    if (expectedSignature == null) {
      LOG.warn("Rejected upload due to unsupported extension mapping");
      return false;
    }

    final int signatureLength = expectedSignature.bytes().length;
    try (var inputStream = documentParam.getInputStream()) {
      final byte[] fileHeader = inputStream.readNBytes(signatureLength);
      // check to catch out of bounds exception early
      if (fileHeader.length < signatureLength) {
        LOG.warn("Rejected upload because file header is shorter than expected signature length");
        return false;
      }
      return matchesSignature(fileHeader, expectedSignature);
    }
  }

  /**
   * Compares file header bytes with a known file signature.
   *
   * @param fileHeader        the bytes read from the start of the file
   * @param expectedSignature the expected file signature
   * @return true if all signature bytes match, false otherwise
   */
  private static boolean matchesSignature(final byte[] fileHeader,
      final FileSignature expectedSignature) {
    final byte[] signature = expectedSignature.bytes();
    for (int i = 0; i < signature.length; i++) {
      if (fileHeader[i] != signature[i]) {
        return false;
      }
    }
    return true;
  }
}
