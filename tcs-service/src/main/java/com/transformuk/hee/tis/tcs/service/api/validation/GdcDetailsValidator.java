package com.transformuk.hee.tis.tcs.service.api.validation;


import com.transformuk.hee.tis.tcs.api.dto.GdcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GdcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GdcDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.IdProjection;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds more complex custom validation for a {@link GdcDetails} that
 * cannot be easily done via annotations
 */
@Component
public class GdcDetailsValidator {

  private static final String GDC_DETAILS_DTO_NAME = "GdcDetailsDTO";
  private static final String NA = "N/A";
  private static final String UNKNOWN = "UNKNOWN";

  private GdcDetailsRepository gdcDetailsRepository;

  public GdcDetailsValidator(GdcDetailsRepository gdcDetailsRepository) {
    this.gdcDetailsRepository = gdcDetailsRepository;
  }

  /**
   * Custom validation on the gdcDetailsDTO DTO, this is meant to supplement the annotation based validation
   * already in place. It checks that the gmc status if gdc number is entered.
   *
   * @param gdcDetailsDTO the gdcDetails to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(GdcDetailsDTO gdcDetailsDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
//    fieldErrors.addAll(checkGdcStatus(gdcDetailsDTO));
    fieldErrors.addAll(checkGdcNumber(gdcDetailsDTO));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(gdcDetailsDTO, "GdcDetailsDTO");
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
    // Ignore if gdcNumber is N/A or UNKNOWN
    if(NA.equalsIgnoreCase(gdcNumber) || UNKNOWN.equalsIgnoreCase(gdcNumber)){
      return fieldErrors;
    }
    if (gdcDetailsDTO.getId() != null) {
      if (StringUtils.isNotEmpty(gdcNumber)) {
        List<IdProjection> existingGdcDetails = gdcDetailsRepository.findByGdcNumber(gdcNumber);
        if (existingGdcDetails.size() > 1) {
          fieldErrors.add(new FieldError(GDC_DETAILS_DTO_NAME, "gdcNumber",
                  String.format("gdcNumber %s is not unique, there are currently %d persons with this number: %s",
                          gdcDetailsDTO.getGdcNumber(), existingGdcDetails.size(),
                          existingGdcDetails)));
        } else if (existingGdcDetails.size() == 1) {
          if (!gdcDetailsDTO.getId().equals(existingGdcDetails.get(0).getId())) {
            fieldErrors.add(new FieldError(GDC_DETAILS_DTO_NAME, "gdcNumber",
                    String.format("gdcNumber %s is not unique, there is currently one person with this number: %s",
                            gdcDetailsDTO.getGdcNumber(), existingGdcDetails.get(0))));
          }
        }
      }
    } else {
      //if we create a gmc details
      if (StringUtils.isNotEmpty(gdcNumber)) {
        List<IdProjection> existingGdcDetails = gdcDetailsRepository.findByGdcNumber(gdcNumber);
        if (!existingGdcDetails.isEmpty()) {
          fieldErrors.add(new FieldError(GDC_DETAILS_DTO_NAME, "gdcNumber",
                  String.format("gdcNumber %s is not unique, there is currently one person with this number: %s",
                          gdcDetailsDTO.getGdcNumber(), existingGdcDetails.get(0))));
        }
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
        requireFieldErrors(fieldErrors, "gdcStatus");
      }
    }
    return fieldErrors;
  }

  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(GDC_DETAILS_DTO_NAME, field,
        String.format("%s is required", field)));
  }

}
