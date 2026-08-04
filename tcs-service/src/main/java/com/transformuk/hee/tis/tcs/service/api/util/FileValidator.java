package com.transformuk.hee.tis.tcs.service.api.util;

import java.io.IOException;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * Utility class for validating uploaded files.
 */
public class FileValidator {

  private static final Logger LOG = LoggerFactory.getLogger(FileValidator.class);
  private static final Tika TIKA = new Tika();

  // Allowed file extensions
  private static final Set<String> ALLOWED_FILE_EXTENSIONS = Set.of(
      "pdf",
      "doc",
      "docx",
      "xls",
      "xlsx"
  );

  // Allowed MIME types
  private static final Set<String> ALLOWED_MEDIA_TYPES = Set.of(
      "application/pdf",                                                                    // .pdf
      "application/msword",                                                                 // .doc
      "application/vnd.openxmlformats-officedocument.wordprocessingml.document",           // .docx
      "application/vnd.ms-excel",                                                           // .xls
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"                  // .xlsx
  );

  // Magic bytes (file signatures) for valid file types
  private static final byte[] PDF_SIGNATURE = {(byte) 0x25, (byte) 0x50, (byte) 0x44,
      (byte) 0x46}; // PDF
  private static final byte[] OLE2_SIGNATURE = {(byte) 0xD0, (byte) 0xCF, (byte) 0x11,
      (byte) 0xE0}; // OLE2 (.doc, .xls)
  private static final byte[] ZIP_SIGNATURE = {(byte) 0x50, (byte) 0x4B, (byte) 0x03,
      (byte) 0x04}; // PK  (.docx, .xlsx)

  private FileValidator() {
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
    if (!ALLOWED_FILE_EXTENSIONS.contains(fileExtension.toLowerCase())) {
      LOG.warn("File extension '{}' is not permitted for file '{}'", fileExtension, filename);
      return false;
    }

    // Magic bytes check
    try {
      if (!isValidMagicBytes(documentParam, fileExtension)) {
        LOG.warn("File signature validation failed for file '{}' with extension '{}'",
            filename, fileExtension);
        return false;
      }
    } catch (IOException ex) {
      LOG.error("Failed to validate magic bytes for '{}'", filename, ex);
      return false;
    }

    // MIME type check via Tika
    try {
      final String detectedType = TIKA.detect(documentParam.getInputStream(), filename);

      if (!ALLOWED_MEDIA_TYPES.contains(detectedType)) {
        LOG.warn("Detected file type '{}' is not permitted for file '{}'.", detectedType, filename);
        return false;
      }
    } catch (IOException ex) {
      LOG.error("Failed to detect file type for '{}'", filename, ex);
      return false;
    }

    return true;
  }

  /**
   * Extracts the file extension from a filename. Returns empty string if filename is null, empty,
   * or has no extension.
   *
   * @param filename the filename to extract extension from
   * @return the file extension (lowercase) or empty string
   */
  public static String extractFileExtension(final String filename) {
    if (StringUtils.isEmpty(filename)) {
      return "";
    }

    final int lastDotIndex = filename.lastIndexOf('.');
    if (lastDotIndex <= 0 || lastDotIndex == filename.length() - 1) {
      return "";
    }

    return filename.substring(lastDotIndex + 1);
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
    final byte[] fileHeader = new byte[4];
    final int bytesRead = documentParam.getInputStream().read(fileHeader);

    if (bytesRead < 4) {
      LOG.warn("File '{}' is too small to contain a valid header",
          documentParam.getOriginalFilename());
      return false;
    }

    switch (fileExtension.toLowerCase()) {
      case "pdf":
        return matchesSignature(fileHeader, PDF_SIGNATURE);
      case "doc":
      case "xls":
        return matchesSignature(fileHeader, OLE2_SIGNATURE);
      case "docx":
      case "xlsx":
        return matchesSignature(fileHeader, ZIP_SIGNATURE);
      default:
        LOG.warn("No magic bytes validation defined for extension '{}'", fileExtension);
        return false;
    }
  }

  /**
   * Compares file header bytes with a known file signature.
   *
   * @param fileHeader the bytes read from the start of the file
   * @param signature  the expected file signature
   * @return true if all signature bytes match, false otherwise
   */
  private static boolean matchesSignature(final byte[] fileHeader, final byte[] signature) {
    for (int i = 0; i < signature.length; i++) {
      if (fileHeader[i] != signature[i]) {
        return false;
      }
    }
    return true;
  }
}
