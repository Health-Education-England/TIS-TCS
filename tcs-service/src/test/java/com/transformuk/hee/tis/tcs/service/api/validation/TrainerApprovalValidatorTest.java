package com.transformuk.hee.tis.tcs.service.api.validation;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ApprovalStatus;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import java.time.LocalDate;
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
class TrainerApprovalValidatorTest {

  @Mock
  PersonRepository personRepository;

  private TrainerApprovalValidator validator;

  @BeforeEach
  void setUp() {
    validator = new TrainerApprovalValidator(personRepository);
  }

  @Test
  void shouldThrowExceptionWhenNoPerson() {
    // Given.
    TrainerApprovalDTO dto = new TrainerApprovalDTO();

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(TrainerApprovalDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError =
        new FieldError(TrainerApprovalDTO.class.getSimpleName(), "person", "person is required");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenNoPersonId() {
    // Given.
    TrainerApprovalDTO dto = new TrainerApprovalDTO();
    dto.setPerson(new PersonDTO());

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(TrainerApprovalDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError =
        new FieldError(TrainerApprovalDTO.class.getSimpleName(), "person", "person is required");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenPersonNotExists() {
    // Given.
    TrainerApprovalDTO dto = new TrainerApprovalDTO();
    PersonDTO personDto = new PersonDTO();
    personDto.setId(1L);
    dto.setPerson(personDto);

    when(personRepository.existsById(1L)).thenReturn(false);

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(TrainerApprovalDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError = new FieldError(TrainerApprovalDTO.class.getSimpleName(), "person",
        "Person with id 1 does not exist");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldThrowExceptionWhenStartDateAfterEndDate() {
    // Given.
    TrainerApprovalDTO dto = new TrainerApprovalDTO();
    dto.setStartDate(LocalDate.now());
    dto.setEndDate(LocalDate.now().minusDays(1));

    // When.
    MethodArgumentNotValidException thrown =
        assertThrows(MethodArgumentNotValidException.class, () -> validator.validate(dto));

    // Then
    BindingResult result = thrown.getBindingResult();
    assertThat("Unexpected object name.", result.getObjectName(),
        is(TrainerApprovalDTO.class.getSimpleName()));
    assertThat("Unexpected target object.", result.getTarget(), is(dto));

    FieldError fieldError = new FieldError(TrainerApprovalDTO.class.getSimpleName(), "startDate",
        "startDate must be before endDate.");
    assertThat("Unexpected error object name.", result.getFieldErrors(), hasItem(fieldError));
  }

  @Test
  void shouldNotThrowExceptionWhenValid() {
    // Given.
    TrainerApprovalDTO dto = new TrainerApprovalDTO();
    dto.setPerson(new PersonDTO());
    dto.setStartDate(LocalDate.now().minusDays(1));
    dto.setEndDate(LocalDate.now());
    dto.setApprovalStatus(ApprovalStatus.CURRENT);

    PersonDTO personDto = new PersonDTO();
    personDto.setId(1L);
    dto.setPerson(personDto);

    when(personRepository.existsById(1L)).thenReturn(true);

    // When, Then.
    assertDoesNotThrow(() -> validator.validate(dto));
  }

  @Test
  void shouldReturnEmptyFieldErrorsWhenValidateForBulkIsOkay() {
    // Given.
    TrainerApprovalDTO dto = new TrainerApprovalDTO();
    dto.setStartDate(LocalDate.now().minusDays(1));
    dto.setEndDate(LocalDate.now());
    dto.setApprovalStatus(ApprovalStatus.CURRENT);

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);
    // Then.
    assertThat("Error list should be empty.", fieldErrors.size(), equalTo(0));
  }

  @Test
  void shouldReturnAllFieldErrorsWhenValidateForBulkGetsErrors() {
    // Given.
    TrainerApprovalDTO dto = new TrainerApprovalDTO();
    dto.setStartDate(LocalDate.now().plusDays(1));
    dto.setEndDate(LocalDate.now());

    // When.
    List<FieldError> fieldErrors = validator.validateForBulk(dto);
    // Then.
    assertThat("Error list should contain 3 errors.", fieldErrors.size(), equalTo(1));
  }

}
