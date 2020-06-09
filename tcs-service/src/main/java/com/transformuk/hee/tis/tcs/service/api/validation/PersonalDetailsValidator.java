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
    final boolean currentOnly = false;
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkGender(personalDetailsDTO, currentOnly));
    fieldErrors.addAll(checkNationality(personalDetailsDTO, currentOnly));
    fieldErrors.addAll(checkDisability(personalDetailsDTO));
    fieldErrors.addAll(checkDualNationality(personalDetailsDTO, currentOnly));
    fieldErrors.addAll(checkEthnicOrigin(personalDetailsDTO, currentOnly));
    fieldErrors.addAll(checkMaritalStatus(personalDetailsDTO, currentOnly));
    fieldErrors.addAll(checkSexualOrientation(personalDetailsDTO, currentOnly));
    fieldErrors.addAll(checkReligiousBelief(personalDetailsDTO, currentOnly));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(personalDetailsDTO,
          PERSONAL_DETAILS_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);

      Method method = this.getClass().getMethods()[0];
      MethodParameter methodParameter = new MethodParameter(method, 0);
      throw new MethodArgumentNotValidException(methodParameter, bindingResult);
    }
  }

  private List<FieldError> checkNationality(PersonalDetailsDTO personalDetailsDto,
      boolean currentOnly) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the Gender
    if (StringUtils.isNotEmpty(personalDetailsDto.getNationality())) {
      Boolean isExists = referenceService
          .isValueExists(NationalityDTO.class, personalDetailsDto.getNationality(), currentOnly);
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "nationality",
            String.format("Nationality %s does not exist", personalDetailsDto.getNationality())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkGender(PersonalDetailsDTO personalDetailsDto, boolean currentOnly) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the Gender
    if (StringUtils.isNotEmpty(personalDetailsDto.getGender())) {
      Boolean isExists = referenceService
          .isValueExists(GenderDTO.class, personalDetailsDto.getGender(), currentOnly);
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "gender",
            String.format("Gender %s does not exist", personalDetailsDto.getGender())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkDisability(PersonalDetailsDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    String disability = dto.getDisability();

    if (disability != null && Arrays.stream(Disability.values()).map(Enum::name)
        .noneMatch(n -> n.equals(disability))) {
      FieldError fieldError = new FieldError(PERSONAL_DETAILS_DTO_NAME, "disability",
          "disability must match a reference value.");
      fieldErrors.add(fieldError);
    }

    return fieldErrors;
  }

  private List<FieldError> checkDualNationality(PersonalDetailsDTO personalDetailsDto,
      boolean currentOnly) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the DualNationality
    if (StringUtils.isNotEmpty(personalDetailsDto.getDualNationality())) {
      Boolean isExists = referenceService
          .isValueExists(NationalityDTO.class, personalDetailsDto.getDualNationality(),
              currentOnly);
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "dualNationality",
            String.format("DualNationality %s does not exist",
                personalDetailsDto.getDualNationality())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkEthnicOrigin(PersonalDetailsDTO personalDetailsDto,
      boolean currentOnly) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the EhtinicOrigin
    if (StringUtils.isNotEmpty(personalDetailsDto.getEthnicOrigin())) {
      Boolean isExists = referenceService
          .isValueExists(EthnicOriginDTO.class, personalDetailsDto.getEthnicOrigin(), currentOnly);
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "ethnicOrigin",
            String.format("EthnicOrigin %s does not exist", personalDetailsDto.getEthnicOrigin())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkMaritalStatus(PersonalDetailsDTO personalDetailsDto,
      boolean currentOnly) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the MaritalStatus
    if (StringUtils.isNotEmpty(personalDetailsDto.getMaritalStatus())) {
      Boolean isExists = referenceService
          .isValueExists(MaritalStatusDTO.class, personalDetailsDto.getMaritalStatus(),
              currentOnly);
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "maritalStatus",
            String
                .format("MaritalStatus %s does not exist", personalDetailsDto.getMaritalStatus())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkSexualOrientation(PersonalDetailsDTO personalDetailsDto,
      boolean currentOnly) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the SexualOrientation
    if (StringUtils.isNotEmpty(personalDetailsDto.getSexualOrientation())) {
      Boolean isExists = referenceService
          .isValueExists(SexualOrientationDTO.class, personalDetailsDto.getSexualOrientation(),
              currentOnly);
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "sexualOrientation",
            String.format("SexualOrientation %s does not exist",
                personalDetailsDto.getSexualOrientation())));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkReligiousBelief(PersonalDetailsDTO personalDetailsDto,
      boolean currentOnly) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the ReligiousBelie
    if (StringUtils.isNotEmpty(personalDetailsDto.getReligiousBelief())) {
      Boolean isExists = referenceService
          .isValueExists(ReligiousBeliefDTO.class, personalDetailsDto.getReligiousBelief(),
              currentOnly);
      if (!isExists) {
        fieldErrors.add(new FieldError(PERSONAL_DETAILS_DTO_NAME, "religiousBelief",
            String.format("ReligiousBelief %s does not exist",
                personalDetailsDto.getReligiousBelief())));
      }
    }
    return fieldErrors;
  }

  /**
   * Custom validation on the PersonalDetailsDTO for bulk upload.
   *
   * @param personalDetailsDto the PersonalDetailsDTO to check
   * @return list of FieldErrors
   */
  public List<FieldError> validateForBulk(PersonalDetailsDTO personalDetailsDto) {
    final boolean currentOnly = true;
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkGender(personalDetailsDto, currentOnly));
    fieldErrors.addAll(checkNationality(personalDetailsDto, currentOnly));
    fieldErrors.addAll(checkDisability(personalDetailsDto));
    fieldErrors.addAll(checkEthnicOrigin(personalDetailsDto, currentOnly));
    fieldErrors.addAll(checkMaritalStatus(personalDetailsDto, currentOnly));
    fieldErrors.addAll(checkSexualOrientation(personalDetailsDto, currentOnly));
    fieldErrors.addAll(checkReligiousBelief(personalDetailsDto, currentOnly));

    return fieldErrors;
  }
}
