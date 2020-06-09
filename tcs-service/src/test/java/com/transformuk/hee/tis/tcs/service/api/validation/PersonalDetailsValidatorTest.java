package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;

import com.transformuk.hee.tis.reference.api.dto.EthnicOriginDTO;
import com.transformuk.hee.tis.reference.api.dto.GenderDTO;
import com.transformuk.hee.tis.reference.api.dto.MaritalStatusDTO;
import com.transformuk.hee.tis.reference.api.dto.NationalityDTO;
import com.transformuk.hee.tis.reference.api.dto.ReligiousBeliefDTO;
import com.transformuk.hee.tis.reference.api.dto.SexualOrientationDTO;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
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
  private static final String DEFAULT_ETHNIC_ORIGIN = "DEFAULT_ETHNIC_ORIGIN";
  private static final String DEFAULT_MARITAL_STATUS = "DEFAULT_MARITAL_STATUS";
  private static final String DEFAULT_SEXUAL_ORIENTATION = "DEFAULT_SEXUAL_ORIENTATION";
  private static final String DEFAULT_RELIGIOUS_BELIEF = "DEFAULT_RELIGIOUS_BELIEF";

  private static final String DTO_NAME = PersonalDetailsDTO.class.getSimpleName();

  private PersonalDetailsValidator validator;

  @Mock
  private ReferenceServiceImpl referenceService;

  @BeforeEach
  void setUp() {
    validator = new PersonalDetailsValidator(referenceService);
  }

  @Test
  void shouldThrowExceptionWhenDisabilityNotValid() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setDisability("invalid");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError =
        new FieldError(DTO_NAME, "disability", "disability must match a reference value.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenDisabilityIncorrectCase() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setDisability("Yes");

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(), is(DTO_NAME));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError =
        new FieldError(DTO_NAME, "disability", "disability must match a reference value.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldNotThrowExceptionWhenDisabilityYes() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setDisability("YES");

    // When, then.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldNotThrowExceptionWhenDisabilityNo() {
    // Given.
    PersonalDetailsDTO dto = new PersonalDetailsDTO();
    dto.setDisability("NO");

    // When, then.
    assertDoesNotThrow(() -> validator.validate(dto));
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
