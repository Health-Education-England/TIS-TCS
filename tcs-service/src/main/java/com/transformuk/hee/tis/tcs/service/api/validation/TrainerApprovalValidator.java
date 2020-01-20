package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
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

  private PersonRepository personRepository;
  private ReferenceServiceImpl referenceService;

  @Autowired
  public TrainerApprovalValidator(PersonRepository personRepository,
    ReferenceServiceImpl referenceService) {
    this.personRepository = personRepository;
    this.referenceService = referenceService;
  }

  public void validate(TrainerApprovalDTO trainerApprovalDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkPerson(trainerApprovalDTO));
    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(trainerApprovalDTO,
        TRAINER_APPROVAL_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private List<FieldError> checkPerson(TrainerApprovalDTO trainerApprovalDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // check the Person
    if (trainerApprovalDTO.getPerson() == null || trainerApprovalDTO.getPerson().getId() == null) {
      requireFieldErrors(fieldErrors, "person");
    } else if (trainerApprovalDTO.getPerson() != null
      && trainerApprovalDTO.getPerson().getId() != null) {
      if (!personRepository.existsById(trainerApprovalDTO.getPerson().getId())) {
        fieldErrors.add(new FieldError(TRAINER_APPROVAL_DTO_NAME, "person",
          String
            .format("Person with id %d does not exist", trainerApprovalDTO.getPerson().getId())));
      }
    }
    return fieldErrors;
  }

  private void requireFieldErrors(List<FieldError> fieldErrors, String field) {
    fieldErrors.add(new FieldError(TRAINER_APPROVAL_DTO_NAME, field,
      String.format("%s is required", field)));
  }

}
