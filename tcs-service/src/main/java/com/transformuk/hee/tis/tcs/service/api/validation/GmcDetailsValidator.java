package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.GmcStatusDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.api.util.FieldDiffUtil;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Holds more complex custom validation for a {@link GmcDetails} that cannot be easily done via
 * annotations
 */
@Component
public class GmcDetailsValidator {

  protected static final String FIELD_NAME_GMC_NUMBER = "gmcNumber";
  protected static final String FIELD_NAME_GMC_STATUS = "gmcStatus";
  private static final String GMC_DETAILS_DTO_NAME = "GmcDetailsDTO";
  private static final String NA = "N/A";
  private static final String UNKNOWN = "UNKNOWN";

  private final GmcDetailsRepository gmcDetailsRepository;
  private final ReferenceServiceImpl referenceService;

  public GmcDetailsValidator(GmcDetailsRepository gmcDetailsRepository,
      ReferenceServiceImpl referenceService) {
    this.gmcDetailsRepository = gmcDetailsRepository;
    this.referenceService = referenceService;
  }

  /**
   * Custom validation on the gmcDetailsDTO DTO, this is meant to supplement the annotation based
   * validation already in place. It checks that the gmc status if gmc number is entered.
   * Validation for update doesn't validate the values which are not changed.
   *
   * @param gmcDetailsDto  the gmcDetails to check
   * @param originalDto    the original gmcDetails to update if validation type is Update,
   *                       can be set to null if validation type is Create
   * @param validationType the validation type
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(GmcDetailsDTO gmcDetailsDto, GmcDetailsDTO originalDto,
      Class<?> validationType) throws MethodArgumentNotValidException {

    final boolean currentOnly = true;
    List<FieldError> fieldErrors = new ArrayList<>();

    Map<String, Function<GmcDetailsDTO, List<FieldError>>> validators = Map.of(
        FIELD_NAME_GMC_NUMBER, this::checkGmcNumber,
        FIELD_NAME_GMC_STATUS, dto -> checkGmcStatusExists(dto, currentOnly)
    );

    if (validationType.equals(Update.class)) {
      Map<String, Object[]> diff = FieldDiffUtil.diff(gmcDetailsDto, originalDto);
      validators.forEach((field, validator) -> {
        if (diff.containsKey(field)) {
          fieldErrors.addAll(validator.apply(gmcDetailsDto));
        }
      });
    } else {
      validators.values().forEach(validator ->
          fieldErrors.addAll(validator.apply(gmcDetailsDto))
      );
    }

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(gmcDetailsDto,
          GMC_DETAILS_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);

      try {
        Method method = this.getClass()
            .getDeclaredMethod("validate", GmcDetailsDTO.class, GmcDetailsDTO.class, Class.class);
        throw new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult);
      } catch (NoSuchMethodException e) {
        // This should only happen if the method name is changed without updating the code.
        throw new RuntimeException(e);
      }
    }
  }

  /**
   * Check gmc number is already exists
   *
   * @param gmcDetailsDTO
   * @return
   */
  private List<FieldError> checkGmcNumber(GmcDetailsDTO gmcDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    String gmcNumber = gmcDetailsDTO.getGmcNumber();
    if (StringUtils.containsWhitespace(gmcNumber)) {
      fieldErrors.add(new FieldError(GMC_DETAILS_DTO_NAME, FIELD_NAME_GMC_NUMBER,
          "gmcNumber should not contain any whitespaces"));
      return fieldErrors;
    }
    // Ignore if gmcNumber is N/A or UNKNOWN
    if (NA.equalsIgnoreCase(gmcNumber) || UNKNOWN.equalsIgnoreCase(gmcNumber)) {
      return fieldErrors;
    }

    if (StringUtils.isNotEmpty(gmcNumber)) {
      List<GmcDetails> existingGmcDetails = gmcDetailsRepository
          .findByGmcNumberOrderById(gmcNumber);

      if (!existingGmcDetails.isEmpty()
          && gmcDetailsDTO.getId() != null) { // should exclude the current one when update
        existingGmcDetails.removeIf(r -> r.getId().equals(gmcDetailsDTO.getId()));
      }

      int existingSize = existingGmcDetails.size();
      if (existingSize > 0) {
        fieldErrors.add(new FieldError(GMC_DETAILS_DTO_NAME, FIELD_NAME_GMC_NUMBER,
            String.format(
                "gmcNumber %s is not unique, there %s currently %d %s with this number (Person ID: %s)",
                gmcDetailsDTO.getGmcNumber(), existingSize > 1 ? "are" : "is", existingSize,
                existingSize > 1 ? "persons" : "person",
                existingGmcDetails.stream().map(r -> r.getId().toString()).collect(
                    Collectors.joining(","))
            )));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkGmcStatusExists(GmcDetailsDTO gmcDetailsDto, boolean currentOnly) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the gmc status
    if (StringUtils.isNotEmpty(gmcDetailsDto.getGmcStatus())) {
      Boolean isExists = referenceService
          .isValueExists(GmcStatusDTO.class, gmcDetailsDto.getGmcStatus(), currentOnly);
      if (!isExists) {
        fieldErrors.add(new FieldError(GMC_DETAILS_DTO_NAME, FIELD_NAME_GMC_STATUS,
            String.format("gmcStatus %s does not exist", gmcDetailsDto.getGmcStatus())));
      }
    }
    return fieldErrors;
  }

  /**
   * Check gmc status required field populated.
   *
   * @param gmcDetailsDTO
   * @return
   */
  private List<FieldError> checkGmcStatus(GmcDetailsDTO gmcDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    boolean isGmcNumberPresent = StringUtils.isNotEmpty(gmcDetailsDTO.getGmcNumber());
    boolean isGmcStatusPresent = StringUtils.isNotEmpty(gmcDetailsDTO.getGmcStatus());
    if (gmcDetailsDTO != null) {
      if (isGmcNumberPresent && !isGmcStatusPresent) {
        requireFieldErrors(fieldErrors, FIELD_NAME_GMC_STATUS);
      }
    }
    return fieldErrors;
  }

  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(GMC_DETAILS_DTO_NAME, field,
        String.format("%s is required", field)));
  }

  /**
   * Custom validation on the GmcDetailsDTO for bulk upload
   *
   * @param gmcDetailsDto the GmcDetailsDTO to check
   * @return list of FieldErrors
   */
  public List<FieldError> validateForBulk(GmcDetailsDTO gmcDetailsDto) {
    final boolean currentOnly = true;
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkGmcNumber(gmcDetailsDto));
    fieldErrors.addAll(checkGmcStatusExists(gmcDetailsDto, currentOnly));
    return fieldErrors;
  }
}
