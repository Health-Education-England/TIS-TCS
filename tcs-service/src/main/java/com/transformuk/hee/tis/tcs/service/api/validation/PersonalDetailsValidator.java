package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.api.dto.EthnicOriginDTO;
import com.transformuk.hee.tis.reference.api.dto.GenderDTO;
import com.transformuk.hee.tis.reference.api.dto.MaritalStatusDTO;
import com.transformuk.hee.tis.reference.api.dto.NationalityDTO;
import com.transformuk.hee.tis.reference.api.dto.ReligiousBeliefDTO;
import com.transformuk.hee.tis.reference.api.dto.SexualOrientationDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Disability;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Holds more complex custom validation for a {@link PersonalDetails} that cannot be easily done via
 * annotations
 */
@Component
public class PersonalDetailsValidator {

  private static final String PERSONAL_DETAILS_DTO_NAME = "PersonalDetailsDTO";
  private final ReferenceServiceImpl referenceService;

  public PersonalDetailsValidator(ReferenceServiceImpl referenceService) {
    this.referenceService = referenceService;
  }

  /**
   * Custom validation on the personalDetailsDTO DTO, this is meant to supplement the annotation
   * based validation already in place. It checks that the gmc status if gmc number is entered.
   *
   * @param personalDetailsDTO the personalDetails to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(PersonalDetailsDTO personalDetailsDTO)
      throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkGender(personalDetailsDTO));
    fieldErrors.addAll(checkNationality(personalDetailsDTO));
    fieldErrors.addAll(checkDisability(personalDetailsDTO));
    fieldErrors.addAll(checkDualNationality(personalDetailsDTO));
    fieldErrors.addAll(checkEhtinicOrigin(personalDetailsDTO));
    fieldErrors.addAll(checkMaritalStatus(personalDetailsDTO));
    fieldErrors.addAll(checkSexualOrientation(personalDetailsDTO));
    fieldErrors.addAll(checkReligiousBelief(personalDetailsDTO));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(personalDetailsDTO,
          PERSONAL_DETAILS_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);

      Method method = this.getClass().getMethods()[0];
      MethodParameter methodParameter = new MethodParameter(method, 0);
      throw new MethodArgumentNotValidException(methodParameter, bindingResult);
    }
  }

  private List<FieldError> checkNationality(PersonalDetailsDTO personalDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the Gender
    if (StringUtils.isNotEmpty(personalDetailsDTO.getNationality())) {
      Boolean isExists = referenceService
          .isValueExists(NationalityDTO.class, personalDetailsDTO.getNationality());
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "nationality",
            String.format("Nationality %s does not exist", personalDetailsDTO.getNationality())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkGender(PersonalDetailsDTO personalDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the Gender
    if (StringUtils.isNotEmpty(personalDetailsDTO.getGender())) {
      Boolean isExists = referenceService
          .isValueExists(GenderDTO.class, personalDetailsDTO.getGender());
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "gender",
            String.format("Gender %s does not exist", personalDetailsDTO.getGender())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkDisability(PersonalDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    String disability = dto.getDisability();

    if (disability != null) {
      if (Arrays.stream(Disability.values()).map(Enum::name).noneMatch(n -> n.equals(disability))) {
        FieldError fieldError = new FieldError(PERSONAL_DETAILS_DTO_NAME, "disability",
            "disability must match a reference value.");
        fieldErrors.add(fieldError);
      }
    }

    return fieldErrors;
  }

  private List<FieldError> checkDualNationality(PersonalDetailsDTO personalDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the DualNationality
    if (StringUtils.isNotEmpty(personalDetailsDTO.getDualNationality())) {
      Boolean isExists = referenceService
          .isValueExists(NationalityDTO.class, personalDetailsDTO.getDualNationality());
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "dualNationality",
            String.format("DualNationality %s does not exist",
                personalDetailsDTO.getDualNationality())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkEhtinicOrigin(PersonalDetailsDTO personalDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the EhtinicOrigin
    if (StringUtils.isNotEmpty(personalDetailsDTO.getEthnicOrigin())) {
      Boolean isExists = referenceService
          .isValueExists(EthnicOriginDTO.class, personalDetailsDTO.getEthnicOrigin());
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "ethnicOrigin",
            String.format("EthnicOrigin %s does not exist", personalDetailsDTO.getEthnicOrigin())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkMaritalStatus(PersonalDetailsDTO personalDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the MaritalStatus
    if (StringUtils.isNotEmpty(personalDetailsDTO.getMaritalStatus())) {
      Boolean isExists = referenceService
          .isValueExists(MaritalStatusDTO.class, personalDetailsDTO.getMaritalStatus());
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "maritalStatus",
            String
                .format("MaritalStatus %s does not exist", personalDetailsDTO.getMaritalStatus())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkSexualOrientation(PersonalDetailsDTO personalDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the SexualOrientation
    if (StringUtils.isNotEmpty(personalDetailsDTO.getSexualOrientation())) {
      Boolean isExists = referenceService
          .isValueExists(SexualOrientationDTO.class, personalDetailsDTO.getSexualOrientation());
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "sexualOrientation",
            String.format("SexualOrientation %s does not exist",
                personalDetailsDTO.getSexualOrientation())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkReligiousBelief(PersonalDetailsDTO personalDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the ReligiousBelie
    if (StringUtils.isNotEmpty(personalDetailsDTO.getReligiousBelief())) {
      Boolean isExists = referenceService
          .isValueExists(ReligiousBeliefDTO.class, personalDetailsDTO.getReligiousBelief());
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "religiousBelief",
            String.format("ReligiousBelief %s does not exist",
                personalDetailsDTO.getReligiousBelief())));
      }
    }
    return fieldErrors;
  }
}
