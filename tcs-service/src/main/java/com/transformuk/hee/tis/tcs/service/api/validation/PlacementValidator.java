package com.transformuk.hee.tis.tcs.service.api.validation;

import static com.google.common.collect.Lists.newArrayList;

import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PlacementCommentDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;

/**
 * Holds more complex custom validation for a {@link PlacementDTO} that cannot be easily done via
 * annotations
 */
@Component
public class PlacementValidator {

  private static final String PLACEMENT_DTO_NAME = "PlacementDTO";
  private final ReferenceService referenceService;
  private final PostRepository postRepository;
  private final PersonRepository personRepository;
  private final PlacementRepository placementRepository;

  @Autowired
  public PlacementValidator(final ReferenceService referenceService,
      final PostRepository postRepository,
      final PersonRepository personRepository,
      final PlacementRepository placementRepository) {
    this.referenceService = referenceService;
    this.personRepository = personRepository;
    this.postRepository = postRepository;
    this.placementRepository = placementRepository;
  }

  public void validate(final PlacementDetailsDTO placementDetailsDTO) throws ValidationException {

    final List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkPost(placementDetailsDTO));
    fieldErrors.addAll(checkSite(placementDetailsDTO));
    fieldErrors.addAll(checkGrade(placementDetailsDTO));
    fieldErrors.addAll(checkPlacementType(placementDetailsDTO));
    fieldErrors.addAll(checkPlacementComments(placementDetailsDTO.getComments()));
    // TODO add specialties and clinical supervisors
    //fieldErrors.addAll(checkSpecialties(placementDetailsDTO));
    fieldErrors.addAll(checkPersons(placementDetailsDTO));
    if (placementDetailsDTO.getEsrEvents().size() > 0) {
      fieldErrors.addAll(checkNpnUpdateIsAllowed(placementDetailsDTO));
    }

    if (!fieldErrors.isEmpty()) {
      final BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(
          placementDetailsDTO, PLACEMENT_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      throw new ValidationException(bindingResult);
    }
  }

  public void validatePlacementForClose(final Long id) throws IllegalArgumentException {
    final Placement placement = placementRepository.findById(id).orElse(null);
    final LocalDate now = LocalDate.now();
    if (placement != null && placement.getDateFrom() != null && placement.getDateTo() != null) {
      if (!(now.isAfter(placement.getDateFrom()) && (now.isBefore(placement.getDateTo()) || now
          .isEqual(placement.getDateTo())))) {
        //if we're not currently between the start and end dates
        throw new IllegalArgumentException(
            "cannot deactivate placement as it is not a current placement");
      }
    } else {
      throw new IllegalArgumentException(String.format("No Placement found for id: [%s]", id));
    }
  }

  public void validatePlacementForDelete(final Long id) throws IllegalArgumentException {
    final Placement placement = placementRepository.findById(id).orElse(null);
    if (placement == null) {
      throw new IllegalArgumentException(String.format("No Placement found for id: [%s]", id));
    }
  }

  private List<FieldError> checkPost(final PlacementDetailsDTO placementDetailsDTO) {
    final List<FieldError> fieldErrors = new ArrayList<>();
    if (placementDetailsDTO.getPostId() == null || placementDetailsDTO.getPostId() < 0) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "postId",
          "Post ID cannot be null or negative"));
    } else if (!postRepository.existsById(placementDetailsDTO.getPostId())) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "postId",
          String.format("Post with id %d does not exist", placementDetailsDTO.getPostId())));
    }
    return fieldErrors;
  }

  private List<FieldError> checkPersons(final PlacementDetailsDTO placementDetailsDTO) {
    final List<FieldError> fieldErrors = new ArrayList<>();
    if (placementDetailsDTO.getTraineeId() == null || placementDetailsDTO.getTraineeId() < 0) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "traineeId",
          "Trainee ID cannot be null or negative"));
    } else if (!personRepository.existsById(placementDetailsDTO.getTraineeId())) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "traineeId",
          String.format("Trainee with id %d does not exist", placementDetailsDTO.getTraineeId())));
    }

    return fieldErrors;
  }

  private List<FieldError> checkSite(final PlacementDetailsDTO placementDetailsDTO) {
    final List<FieldError> fieldErrors = new ArrayList<>();
    if (placementDetailsDTO.getSiteId() == null) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "siteId",
          "Site ID cannot be null or negative"));
    } else {
      final Map<Long, Boolean> siteIdsExistsMap = referenceService
          .siteIdExists(newArrayList(placementDetailsDTO.getSiteId()));
      notExistsFieldErrors(fieldErrors, siteIdsExistsMap, "siteId", "Site");
    }
    return fieldErrors;
  }

  private List<FieldError> checkGrade(final PlacementDetailsDTO placementDetailsDTO) {
    final List<FieldError> fieldErrors = new ArrayList<>();
    if (placementDetailsDTO.getGradeId() == null) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "gradeId",
          "Grade ID cannot be empty"));
    } else {
      final Map<Long, Boolean> gradeIdsExistsMap = referenceService
          .gradeIdsExists(newArrayList(placementDetailsDTO.getGradeId()));
      notExistsFieldErrors(fieldErrors, gradeIdsExistsMap, "gradeId", "Grade");
    }
    return fieldErrors;
  }

  private List<FieldError> checkPlacementType(final PlacementDetailsDTO placementDetailsDTO) {
    final List<FieldError> fieldErrors = new ArrayList<>();

    final Map<String, Boolean> placementTypeCodesExistsMap = referenceService.
        placementTypeExists(newArrayList(placementDetailsDTO.getPlacementType()));
    notExistsStringFieldErrors(fieldErrors, placementTypeCodesExistsMap, "placementType",
        "PlacementType");

    return fieldErrors;
  }

  private List<FieldError> checkPlacementComments(Set<PlacementCommentDTO> comments) {
    final List<FieldError> fieldErrors = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(comments) && comments.size() > 1) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "comments",
          "cannot have more than 1 placement comment"));
    }
    return fieldErrors;
  }

  private void notExistsFieldErrors(final List<FieldError> fieldErrors,
      final Map<Long, Boolean> gradeIdsExistsMap,
      final String field, final String entityName) {
    gradeIdsExistsMap.forEach((k, v) -> {
      if (!v) {
        fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, field,
            String.format("%s with id %s does not exist", entityName, k)));
      }
    });
  }

  private void notExistsStringFieldErrors(final List<FieldError> fieldErrors,
      final Map<String, Boolean> gradeIdsExistsMap,
      final String field, final String entityName) {
    gradeIdsExistsMap.forEach((k, v) -> {
      if (!v) {
        fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, field,
            String.format("%s with id %s does not exist", entityName, k)));
      }
    });
  }

  private void notExistsFieldForLongIdsErrors(final List<FieldError> fieldErrors,
      final Map<Long, Boolean> gradeIdsExistsMap,
      final String field, final String entityName) {
    gradeIdsExistsMap.forEach((k, v) -> {
      if (!v) {
        fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, field,
            String.format("%s with id %s does not exist", entityName, k)));
      }
    });
  }

  private void checkSpecialtyType(final List<FieldError> fieldErrors,
      final int noOfPrimarySpecialtyCount,
      final int noOfSubSpecialtyCount) {
    if (noOfPrimarySpecialtyCount > 1) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "specialties",
          String.format("Only one Specialty of type %s allowed", PostSpecialtyType.PRIMARY)));
    }
    if (noOfSubSpecialtyCount > 1) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "specialties",
          String.format("Only one Specialty of type %s allowed", PostSpecialtyType.SUB_SPECIALTY)));
    }
  }

  private List<FieldError> checkNpnUpdateIsAllowed(final PlacementDetailsDTO placementDetailsDTO) {
    final List<FieldError> fieldErrors = new ArrayList<>();

    Optional<Placement> dbPlacement = placementRepository.findPlacementById(
        placementDetailsDTO.getId());

    if (!dbPlacement.isPresent()) {
      return fieldErrors;
    }

    String oldNpn = dbPlacement.get().getPost().getNationalPostNumber();
    String newNpn = postRepository.findPostById(placementDetailsDTO.getPostId()).getNationalPostNumber();
    if (!Objects.equals(newNpn, oldNpn)) {
      fieldErrors.add(new FieldError(PLACEMENT_DTO_NAME, "nationalPostNumber",
          "National Post Number can't be edited for Placement exported to ESR"));
    }
    return fieldErrors;
  }
}
