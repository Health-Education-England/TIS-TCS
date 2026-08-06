package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.tcs.service.api.util.DocumentUploadConstraints;
import com.transformuk.hee.tis.tcs.service.api.util.FileValidationUtil;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

/**
 * Validates the document upload request. Checks that the file is of a valid type and has a valid
 * extension.
 */
@Component
public class DocumentUploadValidator {

  private static final String OBJECT_NAME = "DocumentUpload";
  private static final String DOCUMENT_FIELD = "document";
  private static final String INVALID_FILE_TYPE_MESSAGE = "Only "
      + DocumentUploadConstraints.ALLOWED_FILE_EXTENSIONS.stream()
      .sorted()
      .map(ext -> "." + ext)
      .collect(Collectors.joining(", "))
      + " files are supported.";

  /**
   * Validates the uploaded document. Throws a ValidationException if the file is not a valid type.
   *
   * @param documentParam the uploaded file
   * @throws ValidationException if the file is not a valid type
   */
  public void validate(final MultipartFile documentParam) throws ValidationException {
    final BeanPropertyBindingResult bindingResult =
        new BeanPropertyBindingResult(documentParam, OBJECT_NAME);

    if (!FileValidationUtil.isValidFileType(documentParam)) {
      bindingResult.addError(
          new FieldError(OBJECT_NAME, DOCUMENT_FIELD, INVALID_FILE_TYPE_MESSAGE));
      throw new ValidationException(bindingResult);
    }
  }
}
