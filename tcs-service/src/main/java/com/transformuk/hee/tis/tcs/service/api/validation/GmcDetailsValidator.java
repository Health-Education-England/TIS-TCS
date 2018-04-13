package com.transformuk.hee.tis.tcs.service.api.validation;


import com.transformuk.hee.tis.reference.api.dto.GmcStatusDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.GmcDetails;
import com.transformuk.hee.tis.tcs.service.repository.GmcDetailsRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds more complex custom validation for a {@link GmcDetails} that
 * cannot be easily done via annotations
 */
@Component
public class GmcDetailsValidator {

  private static final String GMC_DETAILS_DTO_NAME = "GmcDetailsDTO";
  private static final String NA = "N/A";
  private static final String UNKNOWN = "UNKNOWN";
  private GmcDetailsRepository gmcDetailsRepository;
  private ReferenceServiceImpl referenceService;

  public GmcDetailsValidator(GmcDetailsRepository gmcDetailsRepository,ReferenceServiceImpl referenceService) {
    this.gmcDetailsRepository = gmcDetailsRepository;
    this.referenceService = referenceService;
  }

  /**
   * Custom validation on the gmcDetailsDTO DTO, this is meant to supplement the annotation based validation
   * already in place. It checks that the gmc status if gmc number is entered.
   *
   * @param gmcDetailsDTO the gmcDetails to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(GmcDetailsDTO gmcDetailsDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
//    fieldErrors.addAll(checkGmcStatus(gmcDetailsDTO));
    fieldErrors.addAll(checkGmcNumber(gmcDetailsDTO));
    fieldErrors.addAll(checkGmcStatusExists(gmcDetailsDTO));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(gmcDetailsDTO, "GmcDetailsDTO");
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
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
    // Ignore if gmcNumber is N/A or UNKNOWN
    if(NA.equalsIgnoreCase(gmcNumber) || UNKNOWN.equalsIgnoreCase(gmcNumber)){
      return fieldErrors;
    }
    if (gmcDetailsDTO.getId() != null) {
      if (StringUtils.isNotEmpty(gmcNumber)) {
        List<GmcDetails> existingGmcDetails = gmcDetailsRepository.findByGmcNumberOrderById(gmcNumber);
        if (existingGmcDetails.size() > 1) {
          fieldErrors.add(new FieldError(GMC_DETAILS_DTO_NAME, "gmcNumber",
                  String.format("gmcNumber %s is not unique, there are currently %d persons with this number: %s",
                          gmcDetailsDTO.getGmcNumber(), existingGmcDetails.size(),
                          existingGmcDetails)));
        } else if (existingGmcDetails.size() == 1) {
          if (!gmcDetailsDTO.getId().equals(existingGmcDetails.get(0).getId())) {
            fieldErrors.add(new FieldError(GMC_DETAILS_DTO_NAME, "gmcNumber",
                    String.format("gmcNumber %s is not unique, there is currently one person with this number: %s",
                            gmcDetailsDTO.getGmcNumber(), existingGmcDetails.get(0))));
          }
        }
      }
    } else {
      //if we create a gmc details
      if (StringUtils.isNotEmpty(gmcNumber)) {
        List<GmcDetails> existingGmcDetails = gmcDetailsRepository.findByGmcNumberOrderById(gmcNumber);
        if (!existingGmcDetails.isEmpty()) {
          fieldErrors.add(new FieldError(GMC_DETAILS_DTO_NAME, "gmcNumber",
                  String.format("gmcNumber %s is not unique, there is currently one person with this number: %s",
                          gmcDetailsDTO.getGmcNumber(), existingGmcDetails.get(0))));
        }
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkGmcStatusExists(GmcDetailsDTO gmcDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the gmc status
    if (StringUtils.isNotEmpty(gmcDetailsDTO.getGmcStatus())) {
      Boolean isExists = referenceService.isValueExists(GmcStatusDTO.class, gmcDetailsDTO.getGmcStatus());
      if (!isExists) {
        fieldErrors.add(new FieldError(GMC_DETAILS_DTO_NAME, "gmcStatus",
                String.format("gmcStatus %s does not exist", gmcDetailsDTO.getGmcStatus())));
      }
    }
    return fieldErrors;
  }

  /**
   * Check gmc status required field populated
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
        requireFieldErrors(fieldErrors, "gmcStatus");
      }
    }
    return fieldErrors;
  }

  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(GMC_DETAILS_DTO_NAME, field,
        String.format("%s is required", field)));
  }

}
