package com.transformuk.hee.tis.tcs.service.api.validation;

import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Holds more complex custom validation for a {@link PlacementDTO} that
 * cannot be easily done via annotations
 */
@Component
public class PlacementValidator {

  private static final String PLACEMENT_DTO_NAME = "PlacementDTO";
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

  public void validate(PlacementDetailsDTO placementDetailsDTO) throws ValidationException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkPost(placementDetailsDTO));
    fieldErrors.addAll(checkSite(placementDetailsDTO));
    fieldErrors.addAll(checkGrade(placementDetailsDTO));
    fieldErrors.addAll(checkPlacementType(placementDetailsDTO));
    // TODO add specialties and clinical supervisors
    //fieldErrors.addAll(checkSpecialties(placementDetailsDTO));
    fieldErrors.addAll(checkPersons(placementDetailsDTO));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(placementDetailsDTO, PLACEMENT_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      throw new ValidationException(bindingResult);
    }
  }

  private List<FieldError> checkPost(PlacementDetailsDTO placementDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    if (placementDetailsDTO.getPostId() == null || Long.valueOf(placementDetailsDTO.getPostId()) < 0) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "postId",
          "Post ID cannot be null or negative"));
    } else if (!postRepository.exists(placementDetailsDTO.getPostId())) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "postId",
          String.format("Post with id %d does not exist", placementDetailsDTO.getPostId())));
    }
    return fieldErrors;
  }

  private List<FieldError> checkPersons(PlacementDetailsDTO placementDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    if (placementDetailsDTO.getTraineeId() == null || placementDetailsDTO.getTraineeId() < 0) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "traineeId",
          "Trainee ID cannot be null or negative"));
    } else if (!personRepository.exists(placementDetailsDTO.getTraineeId())) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "traineeId",
          String.format("Trainee with id %d does not exist", placementDetailsDTO.getTraineeId())));
    }
    // TODO add clinical supervisors
//    if (placementDTO.getClinicalSupervisorIds() == null || placementDTO.getClinicalSupervisorIds().isEmpty()) {
//      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "clinicalSupervisorIds",
//          "Clinical Supervisor IDs cannot be empty"));
//    } else {
//      for (Long clinicalSupervisorId : placementDTO.getClinicalSupervisorIds()) {
//        if (clinicalSupervisorId == null || clinicalSupervisorId < 0) {
//          fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "clinicalSupervisorIds",
//              "Clinical Supervisor ID cannot be null or negative"));
//        } else if (!personRepository.exists(clinicalSupervisorId)) {
//          fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "clinicalSupervisorIds",
//              String.format("Clinical Supervisor with id %d does not exist", clinicalSupervisorId)));
//        }
//      }
//    }
    return fieldErrors;
  }

  private List<FieldError> checkSite(PlacementDetailsDTO placementDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    if (StringUtils.isBlank(placementDetailsDTO.getSiteCode())) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "siteId",
          "Site ID cannot be null or negative"));
    } else {
      Map<String, Boolean> siteIdsExistsMap = referenceService.siteExists(newArrayList(placementDetailsDTO.getSiteCode()));
      notExistsFieldErrors(fieldErrors, siteIdsExistsMap, "siteId", "Site");
    }
    return fieldErrors;
  }

  private List<FieldError> checkGrade(PlacementDetailsDTO placementDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    if (StringUtils.isBlank(placementDetailsDTO.getGradeAbbreviation())) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "gradeId",
          "Grade ID cannot be empty"));
    } else {
      Map<String, Boolean> gradeIdsExistsMap = referenceService.gradeExists(newArrayList(placementDetailsDTO.getGradeAbbreviation()));
      notExistsFieldErrors(fieldErrors, gradeIdsExistsMap, "gradeId", "Grade");
    }
    return fieldErrors;
  }

  private List<FieldError> checkPlacementType(PlacementDetailsDTO placementDetailsDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();

    Map<String, Boolean> placementTypeCodesExistsMap = referenceService.
        placementTypeExists(newArrayList(placementDetailsDTO.getPlacementType()));
    notExistsFieldErrors(fieldErrors, placementTypeCodesExistsMap, "placementType", "PlacementType");

    return fieldErrors;
  }

  private void notExistsFieldErrors(List<FieldError> fieldErrors, Map<String, Boolean> gradeIdsExistsMap,
                                    String field, String entityName) {
    gradeIdsExistsMap.forEach((k, v) -> {
      if (!v) {
        fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, field,
            String.format("%s with id %s does not exist", entityName, k)));
      }
    });
  }

  private void notExistsFieldForLongIdsErrors(List<FieldError> fieldErrors, Map<Long, Boolean> gradeIdsExistsMap,
                                              String field, String entityName) {
    gradeIdsExistsMap.forEach((k, v) -> {
      if (!v) {
        fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, field,
            String.format("%s with id %s does not exist", entityName, k)));
      }
    });
  }

  // TODO add specialties
//  private List<FieldError> checkSpecialties(PlacementDetailsDTO placementDetailsDTO) {
//    List<FieldError> fieldErrors = new ArrayList<>();
//    // then check the specialties
//    if (placementDetailsDTO.getSpecialties() != null && !placementDetailsDTO.getSpecialties().isEmpty()) {
//      int noOfPrimarySpecialtyCount = 0;
//      int noOfSubSpecialtyCount = 0;
//      for (PlacementSpecialtyDTO ps : placementDTO.getSpecialties()) {
//        if (ps.getSpecialtyId() == null || ps.getSpecialtyId() < 0) {
//          fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "specialties",
//              "Specialty ID cannot be null or negative"));
//        } else {
//          if (!specialtyRepository.exists(ps.getSpecialtyId())) {
//            fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "specialties",
//                String.format("Specialty with id %d does not exist", ps.getSpecialtyId())));
//          } else if (ps.getPlacementSpecialtyType().equals(PostSpecialtyType.PRIMARY)) {
//            ++noOfPrimarySpecialtyCount;
//          } else if (ps.getPlacementSpecialtyType().equals(PostSpecialtyType.SUB_SPECIALTY)) {
//            ++noOfSubSpecialtyCount;
//          }
//        }
//      }
//      checkSpecialtyType(fieldErrors, noOfPrimarySpecialtyCount, noOfSubSpecialtyCount);
//    }
//    return fieldErrors;
//  }

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
}
