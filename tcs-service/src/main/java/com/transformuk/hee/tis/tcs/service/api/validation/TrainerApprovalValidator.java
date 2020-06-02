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

  public void validate(TrainerApprovalDTO dto) throws MethodArgumentNotValidException {
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkPerson(dto));
    fieldErrors.addAll(checkStartDate(dto));
    fieldErrors.addAll(checkEndDate(dto));
    fieldErrors.addAll(checkApprovalStatus(dto));

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
    } else if (dto.getPerson() != null && dto.getPerson().getId() != null) {
      if (!personRepository.existsById(dto.getPerson().getId())) {
        fieldErrors.add(new FieldError(TRAINER_APPROVAL_DTO_NAME, "person",
            String.format("Person with id %d does not exist", dto.getPerson().getId())));
      }
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

  private List<FieldError> checkEndDate(TrainerApprovalDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    if (dto.getStartDate() != null && dto.getEndDate() == null) {
      FieldError fieldError = new FieldError(TRAINER_APPROVAL_DTO_NAME, "endDate",
          "endDate is required when startDate is populated.");
      fieldErrors.add(fieldError);
    }

    return fieldErrors;
  }

  private List<FieldError> checkApprovalStatus(TrainerApprovalDTO dto) {
    List<FieldError> fieldErrors = new ArrayList<>();

    LocalDate startDate = dto.getStartDate();
    LocalDate endDate = dto.getEndDate();
    ApprovalStatus approvalStatus = dto.getApprovalStatus();

    if ((startDate != null || endDate != null) && approvalStatus == null) {
      FieldError fieldError = new FieldError(TRAINER_APPROVAL_DTO_NAME, "approvalStatus",
          "approvalStatus is required when dates are populated.");
      fieldErrors.add(fieldError);
    }

    return fieldErrors;
  }

  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(TRAINER_APPROVAL_DTO_NAME, field,
        String.format("%s is required", field)));
  }

  public List<FieldError> validateForBulk(TrainerApprovalDTO trainerApprovalDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkStartDate(trainerApprovalDTO));
    fieldErrors.addAll(checkEndDate(trainerApprovalDTO));
    fieldErrors.addAll(checkApprovalStatus(trainerApprovalDTO));
    return fieldErrors;
  }
}
