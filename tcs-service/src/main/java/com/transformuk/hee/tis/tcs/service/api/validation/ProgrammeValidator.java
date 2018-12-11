package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds more complex custom validation for a {@link ProgrammeDTO} that
 * cannot be easily done via annotations
 */
@Component
public class ProgrammeValidator {

  private CurriculumRepository curriculumRepository;

  @Autowired
  public ProgrammeValidator(CurriculumRepository curriculumRepository) {
    this.curriculumRepository = curriculumRepository;
  }

  /**
   * Custom validation on the programme DTO, this is meant to supplement the annotation based validation
   * already in place. It checks that the owner and curricula are valid.
   * An owner is valid if the text matches exactly the name of a known owner and if the current
   * user making the call can create or modify a programme within that owner.
   * Curricula are valid if the ID's supplied already exist in the database.
   *
   * @param programmeDTO the programme to check
   * @param userProfile  the current user making the call
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(ProgrammeDTO programmeDTO, UserProfile userProfile) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkOwner(programmeDTO, userProfile));
    fieldErrors.addAll(checkCurricula(programmeDTO));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(programmeDTO, "ProgrammeDTO");
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }


  private List<FieldError> checkCurricula(ProgrammeDTO programmeDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the curricula
    if (programmeDTO.getCurricula() != null && !programmeDTO.getCurricula().isEmpty()) {
      for (CurriculumDTO c : programmeDTO.getCurricula()) {
        if (c.getId() == null || c.getId() < 0) {
          fieldErrors.add(new FieldError("ProgrammeDTO", "curricula",
              "Curriculum ID cannot be null or negative"));
        } else {
          if (!curriculumRepository.existsById(c.getId())) {
            fieldErrors.add(new FieldError("ProgrammeDTO", "curricula",
                String.format("Curricula with id %d does not exist", c.getId())));
          }
        }
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkOwner(ProgrammeDTO programmeDTO, UserProfile userProfile) {
    List<FieldError> fieldErrors = new ArrayList<>();
    //first check if the owner is valid
    if (!DesignatedBodyMapper.getAllOwners().contains(programmeDTO.getOwner())) {
      fieldErrors.add(new FieldError("ProgrammeDTO", "owner",
          "Unknown owner: " + programmeDTO.getOwner()));
    } else {
      //if the owner is valid, then check if the user has the rights to it
      if (!DesignatedBodyMapper.
          map(userProfile.getDesignatedBodyCodes()).contains(programmeDTO.getOwner())) {
        fieldErrors.add(new FieldError("ProgrammeDTO", "owner",
            "You do not have permission to create or modify a programme in the owner: " +
                programmeDTO.getOwner()));
      }
    }
    return fieldErrors;
  }
}
