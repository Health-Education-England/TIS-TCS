package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;

/**
 * Holds more complex custom validation for a {@link ProgrammeDTO} that
 * cannot be easily done via annotations
 */
@Component
public class ProgrammeValidator {

  private CurriculumRepository curriculumRepository;
  private ProgrammeRepository programmeRepository;

  @Autowired
  public ProgrammeValidator(CurriculumRepository curriculumRepository, ProgrammeRepository programmeRepository) {
    this.curriculumRepository = curriculumRepository;
    this.programmeRepository = programmeRepository;
  }

  /**
   * Custom validation on the programme DTO, this is meant to supplement the annotation based validation
   * already in place. It checks that the LocalOffice and curricula are valid.
   * A local office is valid if the text matches exactly the name of a known local office and if the current
   * user making the call can create or modify a programme within that local office.
   * Curricula are valid if the ID's supplied already exist in the database.
   *
   * @param programmeDTO the programme to check
   * @param userProfile  the current user making the call
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(ProgrammeDTO programmeDTO, UserProfile userProfile) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkLocalOffice(programmeDTO, userProfile));
    fieldErrors.addAll(checkCurricula(programmeDTO));
    fieldErrors.addAll(checkProgrammeNumber(programmeDTO));
    fieldErrors.addAll(checkTrainingNumber(programmeDTO));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(programmeDTO, "ProgrammeDTO");
      fieldErrors.forEach(bindingResult::addError);
      throw new MethodArgumentNotValidException(null, bindingResult);
    }
  }

  private List<FieldError> checkProgrammeNumber(ProgrammeDTO programmeDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    //check if the programme number is unique
    //if we update a programme
    if (programmeDTO.getId() != null) {
      Programme p = programmeRepository.findOne(programmeDTO.getId());
      List<Programme> programmeList = programmeRepository.findByProgrammeNumber(programmeDTO.getProgrammeNumber());
      if (programmeList.size() > 1) {
        fieldErrors.add(new FieldError("ProgrammeDTO", "programmeNumber",
            String.format("programmeNumber %s is not unique, there are currently %d programmes with this number: %s",
                programmeDTO.getProgrammeNumber(), programmeList.size(),
                programmeList)));
      } else if (programmeList.size() == 1) {
        if (!p.getId().equals(programmeList.get(0).getId())) {
          fieldErrors.add(new FieldError("ProgrammeDTO", "programmeNumber",
              String.format("programmeNumber %s is not unique, there is currently one programme with this number: %s",
                  programmeDTO.getProgrammeNumber(), programmeList.get(0))));
        }
      }
    } else {
      //if we create a programme
      List<Programme> programmeList = programmeRepository.findByProgrammeNumber(programmeDTO.getProgrammeNumber());
      if (!programmeList.isEmpty()) {
        fieldErrors.add(new FieldError("ProgrammeDTO", "programmeNumber",
            String.format("programmeNumber %s is not unique, there is currently one programme with this number: %s",
                programmeDTO.getProgrammeNumber(), programmeList.get(0))));
      }
    }
    return fieldErrors;
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
          if (!curriculumRepository.exists(c.getId())) {
            fieldErrors.add(new FieldError("ProgrammeDTO", "curricula",
                String.format("Curricula with id %d does not exist", c.getId())));
          }
        }
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkTrainingNumber(ProgrammeDTO programmeDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
      for (TrainingNumberDTO trainingNumber : programmeDTO.getTrainingNumber()) {
        if (trainingNumber.getProgramme() != null && trainingNumber.getProgramme() != programmeDTO.getId()) {
          fieldErrors.add(new FieldError("ProgrammeDTO", "training-number",
                  String.format("trainingNumber %s is already linked.", trainingNumber.getProgramme())));
        }
      }
    return fieldErrors;
  }

  private List<FieldError> checkLocalOffice(ProgrammeDTO programmeDTO, UserProfile userProfile) {
    List<FieldError> fieldErrors = new ArrayList<>();
    //first check if the local office is valid
    if (!DesignatedBodyMapper.getAllLocalOffices().contains(programmeDTO.getManagingDeanery())) {
      fieldErrors.add(new FieldError("ProgrammeDTO", "managingDeanery",
          "Unknown local office: " + programmeDTO.getManagingDeanery()));
    } else {
      //if the local office is valid, then check if the user has the rights to it
      if (!DesignatedBodyMapper.
          map(userProfile.getDesignatedBodyCodes()).contains(programmeDTO.getManagingDeanery())) {
        fieldErrors.add(new FieldError("ProgrammeDTO", "managingDeanery",
            "You do not have permission to create or modify a programme in the Local office: " +
                programmeDTO.getManagingDeanery()));
      }
    }
    return fieldErrors;
  }
}
