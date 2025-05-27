package com.transformuk.hee.tis.tcs.service.api.validation;

import static com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator.FIELD_NAME_DISABILITY;
import static com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator.FIELD_NAME_DUAL_NATIONALITY;
import static com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator.FIELD_NAME_ETHNIC_ORIGIN;
import static com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator.FIELD_NAME_GENDER;
import static com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator.FIELD_NAME_MARITAL_STATUS;
import static com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator.FIELD_NAME_NATIONALITY;
import static com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator.FIELD_NAME_RELIGIOUS_BELIEF;
import static com.transformuk.hee.tis.tcs.service.api.validation.PersonalDetailsValidator.FIELD_NAME_SEXUAL_ORIENTATION;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.reference.api.dto.EthnicOriginDTO;
import com.transformuk.hee.tis.reference.api.dto.GenderDTO;
import com.transformuk.hee.tis.reference.api.dto.MaritalStatusDTO;
import com.transformuk.hee.tis.reference.api.dto.NationalityDTO;
import com.transformuk.hee.tis.reference.api.dto.ReligiousBeliefDTO;
import com.transformuk.hee.tis.reference.api.dto.SexualOrientationDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.api.enumeration.Disability;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@ExtendWith(MockitoExtension.class)
class PersonalDetailsValidatorTest {

  private static final String DEFAULT_GENDER = "DEFAULT_GENDER";
  private static final String DEFAULT_NATIONALITY = "DEFAULT_NATIONALITY";
  private static final String DEFAULT_DUAL_NATIONALITY = "DEFAULT_DUAL_NATIONALITY";
  private static final String DEFAULT_ETHNIC_ORIGIN = "DEFAULT_ETHNIC_ORIGIN";
  private static final String DEFAULT_MARITAL_STATUS = "DEFAULT_MARITAL_STATUS";
  private static final String DEFAULT_SEXUAL_ORIENTATION = "DEFAULT_SEXUAL_ORIENTATION";
  private static final String DEFAULT_RELIGIOUS_BELIEF = "DEFAULT_RELIGIOUS_BELIEF";
  private static final String INVALID_DISABILITY = "INVALID_DISABILITY";

  private static final String UPDATED_GENDER = "UPDATED_GENDER";
  private static final String UPDATED_NATIONALITY = "UPDATED_NATIONALITY";
  private static final String UPDATED_DUAL_NATIONALITY = "UPDATED_DUAL_NATIONALITY";
  private static final String UPDATED_ETHNIC_ORIGIN = "UPDATED_ETHNIC_ORIGIN";
  private static final String UPDATED_MARITAL_STATUS = "UPDATED_MARITAL_STATUS";
  private static final String UPDATED_SEXUAL_ORIENTATION = "UPDATED_SEXUAL_ORIENTATION";
  private static final String UPDATED_RELIGIOUS_BELIEF = "UPDATED_RELIGIOUS_BELIEF";

  private static final String DTO_NAME = PersonalDetailsDTO.class.getSimpleName();

  private PersonalDetailsValidator validator;

  @Mock
  private ReferenceServiceImpl referenceService;

  @BeforeEach
  void setUp() {
    validator = new PersonalDetailsValidator(referenceService);
  }

  @Test
  void shouldNotThrowExceptionWhenDisabilityYes() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setDisability("YES");

    // When, then.
    assertDoesNotThrow(() -> validator.validate(dto, null, Create.class));
  }

  @Test
  void shouldNotThrowExceptionWhenDisabilityNo() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setDisability("NO");

    // When, then.
    assertDoesNotThrow(() -> validator.validate(dto, null, Create.class));
  }

  @Test
  void shouldThrowExceptionWhenCreateWithInvalidFields() {
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setGender(DEFAULT_GENDER);
    dto.setNationality(DEFAULT_NATIONALITY);
    dto.setDualNationality(DEFAULT_DUAL_NATIONALITY);
    dto.setDisability(INVALID_DISABILITY);
    dto.setEthnicOrigin(DEFAULT_ETHNIC_ORIGIN);
    dto.setMaritalStatus(DEFAULT_MARITAL_STATUS);
    dto.setSexualOrientation(DEFAULT_SEXUAL_ORIENTATION);
    dto.setReligiousBelief(DEFAULT_RELIGIOUS_BELIEF);

    when(referenceService.isValueExists(GenderDTO.class, DEFAULT_GENDER, true))
        .thenReturn(false);
    when(referenceService.isValueExists(NationalityDTO.class, DEFAULT_NATIONALITY, true))
        .thenReturn(false);
    when(referenceService.isValueExists(NationalityDTO.class, DEFAULT_DUAL_NATIONALITY, true))
        .thenReturn(false);
    when(referenceService.isValueExists(EthnicOriginDTO.class, DEFAULT_ETHNIC_ORIGIN, true))
        .thenReturn(false);
    when(referenceService.isValueExists(MaritalStatusDTO.class, DEFAULT_MARITAL_STATUS, true))
        .thenReturn(false);
    when(referenceService.isValueExists(SexualOrientationDTO.class, DEFAULT_SEXUAL_ORIENTATION,
        true))
        .thenReturn(false);
    when(referenceService.isValueExists(ReligiousBeliefDTO.class, DEFAULT_RELIGIOUS_BELIEF, true))
        .thenReturn(false);

    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(dto, null, Create.class));

    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError1 = new FieldError(DTO_NAME, FIELD_NAME_GENDER,
        String.format("Gender %s does not exist", DEFAULT_GENDER));
    FieldError fieldError2 = new FieldError(DTO_NAME, FIELD_NAME_NATIONALITY,
        String.format("Nationality %s does not exist", DEFAULT_NATIONALITY));
    FieldError fieldError3 = new FieldError(DTO_NAME, FIELD_NAME_DUAL_NATIONALITY,
        String.format("DualNationality %s does not exist", DEFAULT_DUAL_NATIONALITY));
    FieldError fieldError4 = new FieldError(DTO_NAME, FIELD_NAME_ETHNIC_ORIGIN,
        String.format("EthnicOrigin %s does not exist", DEFAULT_ETHNIC_ORIGIN));
    FieldError fieldError5 = new FieldError(DTO_NAME, FIELD_NAME_MARITAL_STATUS,
        String.format("MaritalStatus %s does not exist", DEFAULT_MARITAL_STATUS));
    FieldError fieldError6 = new FieldError(DTO_NAME, FIELD_NAME_SEXUAL_ORIENTATION,
        String.format("SexualOrientation %s does not exist", DEFAULT_SEXUAL_ORIENTATION));
    FieldError fieldError7 = new FieldError(DTO_NAME, FIELD_NAME_RELIGIOUS_BELIEF,
        String.format("ReligiousBelief %s does not exist", DEFAULT_RELIGIOUS_BELIEF));
    FieldError fieldError8 = new FieldError(DTO_NAME, FIELD_NAME_DISABILITY,
        "disability must match a reference value.");

    assertThat("Unexpected error count.", result.getFieldErrors().size(), is(8));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError1, fieldError2, fieldError3, fieldError4, fieldError5, fieldError6,
            fieldError7, fieldError8));
  }

  @Test
  void shouldNotThrowExceptionWhenCreateWithValidFields() {
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setGender(DEFAULT_GENDER);
    dto.setNationality(DEFAULT_NATIONALITY);
    dto.setDualNationality(DEFAULT_DUAL_NATIONALITY);
    dto.setDisability(Disability.YES.name());
    dto.setEthnicOrigin(DEFAULT_ETHNIC_ORIGIN);
    dto.setMaritalStatus(DEFAULT_MARITAL_STATUS);
    dto.setSexualOrientation(DEFAULT_SEXUAL_ORIENTATION);
    dto.setReligiousBelief(DEFAULT_RELIGIOUS_BELIEF);

    when(referenceService.isValueExists(GenderDTO.class, DEFAULT_GENDER, true))
        .thenReturn(true);
    when(referenceService.isValueExists(NationalityDTO.class, DEFAULT_NATIONALITY, true))
        .thenReturn(true);
    when(referenceService.isValueExists(NationalityDTO.class, DEFAULT_DUAL_NATIONALITY, true))
        .thenReturn(true);
    when(referenceService.isValueExists(EthnicOriginDTO.class, DEFAULT_ETHNIC_ORIGIN, true))
        .thenReturn(true);
    when(referenceService.isValueExists(MaritalStatusDTO.class, DEFAULT_MARITAL_STATUS, true))
        .thenReturn(true);
    when(referenceService.isValueExists(SexualOrientationDTO.class, DEFAULT_SEXUAL_ORIENTATION,
        true))
        .thenReturn(true);
    when(referenceService.isValueExists(ReligiousBeliefDTO.class, DEFAULT_RELIGIOUS_BELIEF, true))
        .thenReturn(true);

    assertDoesNotThrow(() -> validator.validate(dto, null, Create.class));
  }

  @Test
  void shouldNotThrowExceptionWhenUpdateWithValidFields() {
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setGender(UPDATED_GENDER);
    dto.setNationality(UPDATED_NATIONALITY);
    dto.setDualNationality(UPDATED_DUAL_NATIONALITY);
    dto.setDisability(Disability.YES.name());
    dto.setEthnicOrigin(UPDATED_ETHNIC_ORIGIN);
    dto.setMaritalStatus(UPDATED_MARITAL_STATUS);
    dto.setSexualOrientation(UPDATED_SEXUAL_ORIENTATION);
    dto.setReligiousBelief(UPDATED_RELIGIOUS_BELIEF);

    PersonalDetailsDTO originalDto = new PersonalDetailsDTO();
    originalDto.setGender(DEFAULT_GENDER);
    originalDto.setNationality(DEFAULT_NATIONALITY);
    originalDto.setDualNationality(DEFAULT_DUAL_NATIONALITY);
    originalDto.setDisability(Disability.NO.name());
    originalDto.setEthnicOrigin(DEFAULT_ETHNIC_ORIGIN);
    originalDto.setMaritalStatus(DEFAULT_MARITAL_STATUS);
    originalDto.setSexualOrientation(DEFAULT_SEXUAL_ORIENTATION);
    originalDto.setReligiousBelief(DEFAULT_RELIGIOUS_BELIEF);

    when(referenceService.isValueExists(GenderDTO.class, UPDATED_GENDER, true))
        .thenReturn(true);
    when(referenceService.isValueExists(NationalityDTO.class, UPDATED_NATIONALITY, true))
        .thenReturn(true);
    when(referenceService.isValueExists(NationalityDTO.class, UPDATED_DUAL_NATIONALITY, true))
        .thenReturn(true);
    when(referenceService.isValueExists(EthnicOriginDTO.class, UPDATED_ETHNIC_ORIGIN, true))
        .thenReturn(true);
    when(referenceService.isValueExists(MaritalStatusDTO.class, UPDATED_MARITAL_STATUS, true))
        .thenReturn(true);
    when(referenceService.isValueExists(SexualOrientationDTO.class, UPDATED_SEXUAL_ORIENTATION,
        true))
        .thenReturn(true);
    when(referenceService.isValueExists(ReligiousBeliefDTO.class, UPDATED_RELIGIOUS_BELIEF, true))
        .thenReturn(true);

    assertDoesNotThrow(() -> validator.validate(dto, originalDto, Update.class));
  }

  @Test
  void shouldThrowExceptionWhenUpdateWithInvalidFields() {
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setGender(UPDATED_GENDER);
    dto.setNationality(UPDATED_NATIONALITY);
    dto.setDualNationality(UPDATED_DUAL_NATIONALITY);
    dto.setDisability(INVALID_DISABILITY);
    dto.setEthnicOrigin(UPDATED_ETHNIC_ORIGIN);
    dto.setMaritalStatus(UPDATED_MARITAL_STATUS);
    dto.setSexualOrientation(UPDATED_SEXUAL_ORIENTATION);
    dto.setReligiousBelief(UPDATED_RELIGIOUS_BELIEF);

    PersonalDetailsDTO originalDto = new PersonalDetailsDTO();
    originalDto.setGender(DEFAULT_GENDER);
    originalDto.setNationality(DEFAULT_NATIONALITY);
    originalDto.setDualNationality(DEFAULT_DUAL_NATIONALITY);
    originalDto.setDisability(Disability.NO.name());
    originalDto.setEthnicOrigin(DEFAULT_ETHNIC_ORIGIN);
    originalDto.setMaritalStatus(DEFAULT_MARITAL_STATUS);
    originalDto.setSexualOrientation(DEFAULT_SEXUAL_ORIENTATION);
    originalDto.setReligiousBelief(DEFAULT_RELIGIOUS_BELIEF);

    when(referenceService.isValueExists(GenderDTO.class, UPDATED_GENDER, true))
        .thenReturn(false);
    when(referenceService.isValueExists(NationalityDTO.class, UPDATED_NATIONALITY, true))
        .thenReturn(false);
    when(referenceService.isValueExists(NationalityDTO.class, UPDATED_DUAL_NATIONALITY, true))
        .thenReturn(false);
    when(referenceService.isValueExists(EthnicOriginDTO.class, UPDATED_ETHNIC_ORIGIN, true))
        .thenReturn(false);
    when(referenceService.isValueExists(MaritalStatusDTO.class, UPDATED_MARITAL_STATUS, true))
        .thenReturn(false);
    when(referenceService.isValueExists(SexualOrientationDTO.class, UPDATED_SEXUAL_ORIENTATION,
        true))
        .thenReturn(false);
    when(referenceService.isValueExists(ReligiousBeliefDTO.class, UPDATED_RELIGIOUS_BELIEF, true))
        .thenReturn(false);

    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class,
            () -> validator.validate(dto, originalDto, Update.class));

    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError1 = new FieldError(DTO_NAME, FIELD_NAME_GENDER,
        String.format("Gender %s does not exist", UPDATED_GENDER));
    FieldError fieldError2 = new FieldError(DTO_NAME, FIELD_NAME_NATIONALITY,
        String.format("Nationality %s does not exist", UPDATED_NATIONALITY));
    FieldError fieldError3 = new FieldError(DTO_NAME, FIELD_NAME_DUAL_NATIONALITY,
        String.format("DualNationality %s does not exist", UPDATED_DUAL_NATIONALITY));
    FieldError fieldError4 = new FieldError(DTO_NAME, FIELD_NAME_ETHNIC_ORIGIN,
        String.format("EthnicOrigin %s does not exist", UPDATED_ETHNIC_ORIGIN));
    FieldError fieldError5 = new FieldError(DTO_NAME, FIELD_NAME_MARITAL_STATUS,
        String.format("MaritalStatus %s does not exist", UPDATED_MARITAL_STATUS));
    FieldError fieldError6 = new FieldError(DTO_NAME, FIELD_NAME_SEXUAL_ORIENTATION,
        String.format("SexualOrientation %s does not exist", UPDATED_SEXUAL_ORIENTATION));
    FieldError fieldError7 = new FieldError(DTO_NAME, FIELD_NAME_RELIGIOUS_BELIEF,
        String.format("ReligiousBelief %s does not exist", UPDATED_RELIGIOUS_BELIEF));
    FieldError fieldError8 = new FieldError(DTO_NAME, FIELD_NAME_DISABILITY,
        "disability must match a reference value.");

    assertThat("Unexpected error count.", result.getFieldErrors().size(), is(8));
    assertThat("Expected field error not found.", result.getFieldErrors(),
        hasItems(fieldError1, fieldError2, fieldError3, fieldError4, fieldError5, fieldError6,
            fieldError7, fieldError8));
  }

  @Test
  void shouldNotThrowExceptionWhenUpdateWithExistingFieldValues() {
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setGender(DEFAULT_GENDER);
    dto.setNationality(DEFAULT_NATIONALITY);
    dto.setDualNationality(DEFAULT_DUAL_NATIONALITY);
    dto.setDisability(Disability.NO.name());
    dto.setEthnicOrigin(DEFAULT_ETHNIC_ORIGIN);
    dto.setMaritalStatus(DEFAULT_MARITAL_STATUS);
    dto.setSexualOrientation(DEFAULT_SEXUAL_ORIENTATION);
    dto.setReligiousBelief(DEFAULT_RELIGIOUS_BELIEF);

    PersonalDetailsDTO originalDto = new PersonalDetailsDTO();
    originalDto.setGender(DEFAULT_GENDER);
    originalDto.setNationality(DEFAULT_NATIONALITY);
    originalDto.setDualNationality(DEFAULT_DUAL_NATIONALITY);
    originalDto.setDisability(Disability.NO.name());
    originalDto.setEthnicOrigin(DEFAULT_ETHNIC_ORIGIN);
    originalDto.setMaritalStatus(DEFAULT_MARITAL_STATUS);
    originalDto.setSexualOrientation(DEFAULT_SEXUAL_ORIENTATION);
    originalDto.setReligiousBelief(DEFAULT_RELIGIOUS_BELIEF);

    assertDoesNotThrow(() -> validator.validate(dto, originalDto, Update.class));
    verify(referenceService, never()).isValueExists(any(Class.class), anyString(), anyBoolean());
  }

  @Test
  void shouldReturnEmptyFieldErrorsWhenValidateForBulkIsOkay() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setGender(DEFAULT_GENDER);
    dto.setNationality(DEFAULT_NATIONALITY);
    dto.setDisability("YES");
    dto.setEthnicOrigin(DEFAULT_ETHNIC_ORIGIN);
    dto.setMaritalStatus(DEFAULT_MARITAL_STATUS);
    dto.setSexualOrientation(DEFAULT_SEXUAL_ORIENTATION);
    dto.setReligiousBelief(DEFAULT_RELIGIOUS_BELIEF);

    lenient().when(referenceService.isValueExists(GenderDTO.class, DEFAULT_GENDER, true))
        .thenReturn(true);
    lenient().when(referenceService.isValueExists(NationalityDTO.class, DEFAULT_NATIONALITY, true))
        .thenReturn(true);
    lenient()
        .when(referenceService.isValueExists(EthnicOriginDTO.class, DEFAULT_ETHNIC_ORIGIN, true))
        .thenReturn(true);
    lenient()
        .when(referenceService.isValueExists(MaritalStatusDTO.class, DEFAULT_MARITAL_STATUS, true))
        .thenReturn(true);
    lenient().when(referenceService
            .isValueExists(SexualOrientationDTO.class, DEFAULT_SEXUAL_ORIENTATION, true))
        .thenReturn(true);
    lenient().when(
            referenceService.isValueExists(ReligiousBeliefDTO.class, DEFAULT_RELIGIOUS_BELIEF, true))
        .thenReturn(true);

    // When.
    List<FieldError> fieldErrorList = validator.validateForBulk(dto);
    // Then.
    assertThat("Error list should be empty.", fieldErrorList.size(), equalTo(0));
  }

  @Test
  void shouldReturnAllFieldErrorsWhenValidateForBulkGetsErrors() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setGender(DEFAULT_GENDER);
    dto.setNationality(DEFAULT_NATIONALITY);
    dto.setDisability("invalid");
    dto.setEthnicOrigin(DEFAULT_ETHNIC_ORIGIN);
    dto.setMaritalStatus(DEFAULT_MARITAL_STATUS);
    dto.setSexualOrientation(DEFAULT_SEXUAL_ORIENTATION);
    dto.setReligiousBelief(DEFAULT_RELIGIOUS_BELIEF);

    lenient().when(referenceService.isValueExists(GenderDTO.class, DEFAULT_GENDER, true))
        .thenReturn(false);
    lenient().when(referenceService.isValueExists(NationalityDTO.class, DEFAULT_NATIONALITY, true))
        .thenReturn(false);
    lenient()
        .when(referenceService.isValueExists(EthnicOriginDTO.class, DEFAULT_ETHNIC_ORIGIN, true))
        .thenReturn(false);
    lenient()
        .when(referenceService.isValueExists(MaritalStatusDTO.class, DEFAULT_MARITAL_STATUS, true))
        .thenReturn(false);
    lenient().when(referenceService
            .isValueExists(SexualOrientationDTO.class, DEFAULT_SEXUAL_ORIENTATION, true))
        .thenReturn(false);
    lenient().when(
            referenceService.isValueExists(ReligiousBeliefDTO.class, DEFAULT_RELIGIOUS_BELIEF, true))
        .thenReturn(false);

    // When.
    List<FieldError> fieldErrorList = validator.validateForBulk(dto);
    // Then.
    assertThat("Error list should contain 7 errors.", fieldErrorList.size(), equalTo(7));
  }

}
