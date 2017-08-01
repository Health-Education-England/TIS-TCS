package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

@Component
public class CurriculumValidator {

  private final SpecialtyRepository specialtyRepository;

  @Autowired
  public CurriculumValidator(SpecialtyRepository specialtyRepository) {
    this.specialtyRepository = specialtyRepository;
  }

  /**
   * Custom validator used during create and update rest calls. Most structural validation is already on the DTO itself.
   * This validation checks that the provided specialty is valid and that it exists
   *
   * @param curriculumDTO The provided curriculum to validate
   * @throws MethodArgumentNotValidException
   */
  public void validate(CurriculumDTO curriculumDTO) throws MethodArgumentNotValidException {
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkSpecialty(curriculumDTO));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(curriculumDTO, "CurriculumDTO");
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private List<FieldError> checkSpecialty(CurriculumDTO curriculumDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    SpecialtyDTO specialty = curriculumDTO.getSpecialty();
    if (specialty != null) {
      if (specialty.getId() == null || specialty.getId() < 0) {
        fieldErrors.add(new FieldError("CurriculumDTO", "specialty",
            String.format("Specialty with id %d cannot be null or negative", specialty.getId())));
      } else if (!specialtyRepository.exists(specialty.getId())) {
        fieldErrors.add(new FieldError("CurriculumDTO", "specialty",
            String.format("Specialty with id %d does not exist", specialty.getId())));
      }
    }
    return fieldErrors;
  }
}
