package com.transformuk.hee.tis.tcs.service.api.validation;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

/**
 * Custom validator for validating requests to create and update Specialties.
 * <p>
 * Initially created as part of https://hee-tis.atlassian.net/browse/TISDEV-2189 it validates that the
 * id, status, nhsSpecialtyCode, specialtyType and name are required during update while during creation,
 * status, nhsSpecialtyCode, specialtyType and name are required
 */
@Component
public class SpecialtyValidator {

  private final SpecialtyGroupRepository specialtyGroupRepository;

  @Autowired
  public SpecialtyValidator(SpecialtyGroupRepository specialtyGroupRepository) {
    this.specialtyGroupRepository = specialtyGroupRepository;
  }

  public void validate(SpecialtyDTO specialtyDTO) throws MethodArgumentNotValidException {
    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkSpecialtyGroup(specialtyDTO.getSpecialtyGroup()));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(specialtyDTO, "SpecialtyDTO");
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private List<FieldError> checkSpecialtyGroup(SpecialtyGroupDTO specialtyGroup) {
    List<FieldError> fieldErrors = Lists.newArrayList();
    if (specialtyGroup != null) {
      if (specialtyGroup.getId() == null || specialtyGroup.getId() < 0) {
        fieldErrors.add(new FieldError("SpecialtyDTO", "specialtyGroup",
            String.format("SpecialtyGroup with id %d cannot be null or negative", specialtyGroup.getId())));
      } else if (!specialtyGroupRepository.exists(specialtyGroup.getId())) {
        fieldErrors.add(new FieldError("SpecialtyDTO", "specialtyGroup",
            String.format("SpecialtyGroup with id %d does not exist", specialtyGroup.getId())));
      }
    }
    return fieldErrors;
  }
}
