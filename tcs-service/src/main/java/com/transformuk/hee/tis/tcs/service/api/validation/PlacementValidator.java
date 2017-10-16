package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementViewDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Holds more complex custom validation for a {@link PlacementViewDTO} that
 * cannot be easily done via annotations
 */
@Component
public class PlacementValidator {

  public static final String PLACEMENT_DTO_NAME = "PlacementViewDTO";
  private final SpecialtyRepository specialtyRepository;
  private final ReferenceServiceImpl referenceService;
  private final PostRepository postRepository;
  private final PersonRepository personRepository;

  @Autowired
  public PlacementValidator(SpecialtyRepository specialtyRepository,
                            ReferenceServiceImpl referenceService,
                            PostRepository postRepository,
                            PersonRepository personRepository) {
    this.specialtyRepository = specialtyRepository;
    this.referenceService = referenceService;
    this.personRepository = personRepository;
    this.postRepository = postRepository;
  }

  public void validate(PlacementDTO placementDTO) throws ValidationException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkLocalOffice(placementDTO));
    fieldErrors.addAll(checkPost(placementDTO));
    fieldErrors.addAll(checkSite(placementDTO));
    fieldErrors.addAll(checkGrade(placementDTO));
    fieldErrors.addAll(checkSpecialties(placementDTO));
    fieldErrors.addAll(checkPersons(placementDTO));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(placementDTO, PLACEMENT_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      throw new ValidationException(bindingResult);
    }
  }

  private List<FieldError> checkPost(PlacementDTO placementDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    if (placementDTO.getPostId() == null || Long.valueOf(placementDTO.getPostId()) < 0) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "postId",
          "Post ID cannot be null or negative"));
    } else if (!postRepository.exists(placementDTO.getPostId())) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "postId",
          String.format("Post with id %d does not exist", placementDTO.getPostId())));
    }
    return fieldErrors;
  }

  private List<FieldError> checkPersons(PlacementDTO placementDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    if (placementDTO.getTraineeId() == null || placementDTO.getTraineeId() < 0) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "traineeId",
          "Trainee ID cannot be null or negative"));
    } else if (!personRepository.exists(placementDTO.getTraineeId())) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "traineeId",
          String.format("Trainee with id %d does not exist", placementDTO.getTraineeId())));
    }
    if (placementDTO.getClinicalSupervisorId() == null || placementDTO.getClinicalSupervisorId() < 0) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "clinicalSupervisorId",
          "Clinical Supervisor ID cannot be null or negative"));
    } else if (!personRepository.exists(placementDTO.getClinicalSupervisorId())) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "clinicalSupervisorId",
          String.format("Clinical Supervisor with id %d does not exist", placementDTO.getClinicalSupervisorId())));
    }
    return fieldErrors;
  }

  private List<FieldError> checkSite(PlacementDTO placementDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    if (placementDTO.getSiteId() == null || placementDTO.getSiteId() < 0) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "siteId",
          "Site ID cannot be null or negative"));
    } else {
      Map<Long, Boolean> siteIdsExistsMap = referenceService.siteExists(newArrayList(placementDTO.getSiteId()));
      notExistsFieldErrors(fieldErrors, siteIdsExistsMap, "siteId", "Site");
    }
    return fieldErrors;
  }

  private List<FieldError> checkGrade(PlacementDTO placementDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    if (placementDTO.getGradeId() == null || placementDTO.getGradeId() < 0) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "gradeId",
          "Grade ID cannot be null or negative"));
    } else {
      Map<Long, Boolean> gradeIdsExistsMap = referenceService.gradeExists(newArrayList(placementDTO.getGradeId()));
      notExistsFieldErrors(fieldErrors, gradeIdsExistsMap, "gradeId", "Grade");
    }
    return fieldErrors;
  }

  private void notExistsFieldErrors(List<FieldError> fieldErrors, Map<Long, Boolean> gradeIdsExistsMap,
                                    String field, String entityName) {
    gradeIdsExistsMap.forEach((k, v) -> {
      if (!v) {
        fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, field,
            String.format("%s with id %s does not exist", entityName, k)));
      }
    });
  }

  private List<FieldError> checkSpecialties(PlacementDTO placementDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the specialties
    if (placementDTO.getSpecialties() != null && !placementDTO.getSpecialties().isEmpty()) {
      int noOfPrimarySpecialtyCount = 0;
      int noOfSubSpecialtyCount = 0;
      for (PlacementSpecialtyDTO ps : placementDTO.getSpecialties()) {
        if (ps.getSpecialtyId() == null || ps.getSpecialtyId() < 0) {
          fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "specialties",
              "Specialty ID cannot be null or negative"));
        } else {
          if (!specialtyRepository.exists(ps.getSpecialtyId())) {
            fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "specialties",
                String.format("Specialty with id %d does not exist", ps.getSpecialtyId())));
          } else if (ps.getPlacementSpecialtyType().equals(PostSpecialtyType.PRIMARY)) {
            ++noOfPrimarySpecialtyCount;
          } else if (ps.getPlacementSpecialtyType().equals(PostSpecialtyType.SUB_SPECIALTY)) {
            ++noOfSubSpecialtyCount;
          }
        }
      }
      checkSpecialtyType(fieldErrors, noOfPrimarySpecialtyCount, noOfSubSpecialtyCount);
    }
    return fieldErrors;
  }

  private void checkSpecialtyType(List<FieldError> fieldErrors, int noOfPrimarySpecialtyCount, int noOfSubSpecialtyCount) {
    if (noOfPrimarySpecialtyCount > 1) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "specialties",
          String.format("Only one Specialty of type %s allowed", PostSpecialtyType.PRIMARY)));
    }
    if (noOfSubSpecialtyCount > 1) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "specialties",
          String.format("Only one Specialty of type %s allowed", PostSpecialtyType.SUB_SPECIALTY)));
    }
  }

  private List<FieldError> checkLocalOffice(PlacementDTO placementDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    //first check if the local office is valid
    if (!DesignatedBodyMapper.getAllLocalOffices().contains(placementDTO.getManagingLocalOffice())) {
      fieldErrors.add(new FieldError("placementDTO", "managingLocalOffice",
          "Unknown local office: " + placementDTO.getManagingLocalOffice()));
    }
    return fieldErrors;
  }
}
