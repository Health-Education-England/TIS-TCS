package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.GdcStatusDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.FieldDiffUtil;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.IdProjection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Holds more complex custom validation for a {@link GdcDetails} that cannot be easily done via
 * annotations
 */
@Component
public class GdcDetailsValidator {

  private static final String GDC_DETAILS_DTO_NAME = "GdcDetailsDTO";
  protected static final String FIELD_NAME_GDC_NUMBER = "gdcNumber";
  protected static final String FIELD_NAME_GDC_STATUS = "gdcStatus";
  private static final String NA = "N/A";
  private static final String UNKNOWN = "UNKNOWN";

  private final GdcDetailsRepository gdcDetailsRepository;
  private final ReferenceServiceImpl referenceService;

  public GdcDetailsValidator(GdcDetailsRepository gdcDetailsRepository,
      ReferenceServiceImpl referenceService) {
    this.gdcDetailsRepository = gdcDetailsRepository;
    this.referenceService = referenceService;
  }

  /**
   * Custom validation on the gdcDetailsDTO DTO, this is meant to supplement the annotation based
   * validation already in place. It checks that the gmc status if gdc number is entered.
   * Validation for update doesn't validate the values which are not changed.
   *
   * @param gdcDetailsDto  the gdcDetails to check
   * @param originalDto    the original gdcDetails to update if validation type is Update,
   *                       can be set to null if validation type is Create
   * @param validationType the validation type
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(GdcDetailsDTO gdcDetailsDto, GdcDetailsDTO originalDto,
      Class<?> validationType) throws MethodArgumentNotValidException {

    final boolean currentOnly = true;
    List<FieldError> fieldErrors = new ArrayList<>();

    Map<String, Function<GdcDetailsDTO, List<FieldError>>> validators = Map.of(
        FIELD_NAME_GDC_NUMBER, this::checkGdcNumber,
        FIELD_NAME_GDC_STATUS, dto -> checkGdcStatusExists(dto, currentOnly)
    );

    if (validationType.equals(Update.class)) {
      Map<String, Object[]> diff = FieldDiffUtil.diff(gdcDetailsDto, originalDto);
      validators.forEach((field, validator) -> {
        if (diff.containsKey(field)) {
          fieldErrors.addAll(validator.apply(gdcDetailsDto));
        }
      });
    } else {
      validators.values().forEach(validator ->
          fieldErrors.addAll(validator.apply(gdcDetailsDto))
      );
    }

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(gdcDetailsDto,
          GDC_DETAILS_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  /**
   * Check gdc number is already exists
   *
   * @param gdcDetailsDTO
   * @return
   */
  private List<FieldError> checkGdcNumber(GdcDetailsDTO gdcDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    String gdcNumber = gdcDetailsDTO.getGdcNumber();
    if (StringUtils.containsWhitespace(gdcNumber)) {
      fieldErrors.add(new FieldError(GDC_DETAILS_DTO_NAME, FIELD_NAME_GDC_NUMBER,
          "gdcNumber should not contain any whitespaces"));
      return fieldErrors;
    }
    // Ignore if gdcNumber is N/A or UNKNOWN
    if (NA.equalsIgnoreCase(gdcNumber) || UNKNOWN.equalsIgnoreCase(gdcNumber)) {
      return fieldErrors;
    }

    if (StringUtils.isNotEmpty(gdcNumber)) {
      List<IdProjection> existingGdcDetails = gdcDetailsRepository.findByGdcNumber(gdcNumber);

      if (!existingGdcDetails.isEmpty()
          && gdcDetailsDTO.getId() != null) { /// should exclude the current one when update
        existingGdcDetails.removeIf(r -> r.getId().equals(gdcDetailsDTO.getId()));
      }

      int existingSize = existingGdcDetails.size();
      if (existingSize > 0) {
        fieldErrors.add(new FieldError(GDC_DETAILS_DTO_NAME, FIELD_NAME_GDC_NUMBER,
            String.format(
                "gdcNumber %s is not unique, there %s currently %d %s with this number (Person ID: %s)",
                gdcDetailsDTO.getGdcNumber(), existingSize > 1 ? "are" : "is", existingSize,
                existingSize > 1 ? "persons" : "person",
                existingGdcDetails.stream().map(r -> r.getId().toString())
                    .collect(Collectors.joining(","))
            )));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkGdcStatusExists(GdcDetailsDTO gdcDetailsDto, boolean currentOnly) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the gmc status
    if (StringUtils.isNotEmpty(gdcDetailsDto.getGdcStatus())) {
      Boolean isExists = referenceService
          .isValueExists(GdcStatusDTO.class, gdcDetailsDto.getGdcStatus(), currentOnly);
      if (!isExists) {
        fieldErrors.add(new FieldError(GDC_DETAILS_DTO_NAME, FIELD_NAME_GDC_STATUS,
            String.format("gdcStatus %s does not exist", gdcDetailsDto.getGdcStatus())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkGdcStatus(GdcDetailsDTO gdcDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    boolean isGdcNumberPresent = StringUtils.isNotEmpty(gdcDetailsDTO.getGdcNumber());
    boolean isGdcStatusPresent = StringUtils.isNotEmpty(gdcDetailsDTO.getGdcStatus());
    if (gdcDetailsDTO != null) {
      if (isGdcNumberPresent && !isGdcStatusPresent) {
        requireFieldErrors(fieldErrors, FIELD_NAME_GDC_STATUS);
      }
    }
    return fieldErrors;
  }

  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(GDC_DETAILS_DTO_NAME, field,
        String.format("%s is required", field)));
  }

  /**
   * Custom validation on the GdcDetailsDTO for bulk upload.
   *
   * @param gdcDetailsDto the GdcDetailsDTO to check
   * @return list of FieldErrors
   */
  public List<FieldError> validateForBulk(GdcDetailsDTO gdcDetailsDto) {
    final boolean currentOnly = true;
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkGdcNumber(gdcDetailsDto));
    fieldErrors.addAll(checkGdcStatusExists(gdcDetailsDto, currentOnly));
    return fieldErrors;
  }
}
