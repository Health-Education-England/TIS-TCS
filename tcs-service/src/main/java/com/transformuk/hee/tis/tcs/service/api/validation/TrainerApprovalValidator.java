package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.ApprovalStatus;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class TrainerApprovalValidator {

  private static final String TRAINER_APPROVAL_DTO_NAME = "TrainerApprovalDTO";

  private final PersonRepository personRepository;

  @Autowired
  public TrainerApprovalValidator(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  /**
   * Custom validation on the TrainerApprovalDTO DTO.
   *
   * @param dto the TrainerApprovalDTO to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(TrainerApprovalDTO dto) throws MethodArgumentNotValidException {
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkPerson(dto));
    fieldErrors.addAll(checkStartDate(dto));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult =
          new BeanPropertyBindingResult(dto, TRAINER_APPROVAL_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private List<FieldError> checkPerson(TrainerApprovalDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // check the Person
    if (dto.getPerson() == null || dto.getPerson().getId() == null) {
      requireFieldErrors(fieldErrors, "person");
    } else if (dto.getPerson() != null && dto.getPerson().getId() != null && !personRepository
        .existsById(dto.getPerson().getId())) {
      fieldErrors.add(new FieldError(TRAINER_APPROVAL_DTO_NAME, "person",
          String.format("Person with id %d does not exist", dto.getPerson().getId())));
    }
    return fieldErrors;
  }

  private List<FieldError> checkStartDate(TrainerApprovalDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    LocalDate startDate = dto.getStartDate();
    LocalDate endDate = dto.getEndDate();

    if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
      FieldError fieldError = new FieldError(TRAINER_APPROVAL_DTO_NAME, "startDate",
          "startDate must be before endDate.");
      fieldErrors.add(fieldError);
    }

    return fieldErrors;
  }

  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(TRAINER_APPROVAL_DTO_NAME, field,
        String.format("%s is required", field)));
  }

  /**
   * Custom validation on the TrainerApprovalDTO for bulk upload.
   *
   * @param trainerApprovalDto the TrainerApprovalDTO to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public List<FieldError> validateForBulk(TrainerApprovalDTO trainerApprovalDto) {
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkStartDate(trainerApprovalDto));
    return fieldErrors;
  }
}
