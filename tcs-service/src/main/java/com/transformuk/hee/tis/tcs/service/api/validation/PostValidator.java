package com.transformuk.hee.tis.tcs.service.api.validation;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.client.impl.ReferenceServiceImpl;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.SpecialtyType;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.impl.NationalPostNumberServiceImpl;
import com.transformuk.hee.tis.tcs.service.service.mapper.DesignatedBodyMapper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * Holds more complex custom validation for a {@link PostDTO} that cannot be easily done via
 * annotations
 */
@Component
public class PostValidator {

  public static final String POST_DTO_NAME = "PostDTO";
  public static final String SPECIALTIES = "specialties";
  private ProgrammeRepository programmeRepository;
  private PostRepository postRepository;
  private SpecialtyRepository specialtyRepository;
  private PlacementRepository placementRepository;
  private ReferenceServiceImpl referenceService;
  private NationalPostNumberServiceImpl nationalPostNumberServiceImpl;

  @Autowired
  public PostValidator(ProgrammeRepository programmeRepository,
      PostRepository postRepository,
      SpecialtyRepository specialtyRepository,
      PlacementRepository placementRepository,
      ReferenceServiceImpl referenceService,
      NationalPostNumberServiceImpl nationalPostNumberServiceImpl) {
    this.programmeRepository = programmeRepository;
    this.postRepository = postRepository;
    this.specialtyRepository = specialtyRepository;
    this.placementRepository = placementRepository;
    this.referenceService = referenceService;
    this.nationalPostNumberServiceImpl = nationalPostNumberServiceImpl;
  }

  /**
   * Custom validation on the post DTO, this is meant to supplement the annotation based validation
   * already in place. It checks that the Owner,Programme,Site,Grade,Specialty and Placement are
   * valid. An owner is valid if the text matches exactly the name of a known owner and if the
   * current user making the call can create or modify a post within that owner. Programmes is valid
   * if the ID's supplied already exist in the database. Sites are valid if the ID's supplied
   * already exist in the database. Grades are valid if the ID's supplied already exist in the
   * database. Specialties are valid if the ID's supplied already exist in the database. Placement
   * are valid if the ID's supplied already exist in the database.
   *
   * @param postDTO the post to check
   * @throws MethodArgumentNotValidException if there are validation errors
   */
  public void validate(PostDTO postDTO) throws MethodArgumentNotValidException {

    List<FieldError> fieldErrors = new ArrayList<>();
    fieldErrors.addAll(checkOwner(postDTO));
    fieldErrors.addAll(checkProgramme(postDTO));
    fieldErrors.addAll(checkSites(postDTO));
    fieldErrors.addAll(checkGrades(postDTO));
    fieldErrors.addAll(checkSpecialties(postDTO));
    fieldErrors.addAll(checkPlacementHistory(postDTO));
    fieldErrors.addAll(checkNationalPostNumber(postDTO));
    fieldErrors.addAll(checkLegacy(postDTO.getId()));

    if (!fieldErrors.isEmpty()) {
      BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(postDTO,
          POST_DTO_NAME);
      fieldErrors.forEach(bindingResult::addError);
      Method method = this.getClass().getMethods()[0];
      throw new MethodArgumentNotValidException(new MethodParameter(method, 0), bindingResult);
    }
  }

  private List<FieldError> checkProgramme(PostDTO postDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();

    if (postDTO.getProgrammes() == null || postDTO.getProgrammes().isEmpty()) {
      return fieldErrors;
    }

    for (ProgrammeDTO programmeDTO : postDTO.getProgrammes()) {
      if (programmeDTO.getId() == null || programmeDTO.getId() < 0) {
        fieldErrors.add(new FieldError(POST_DTO_NAME, "programmes",
            "Programme ID cannot be null or negative"));
      } else {
        if (!programmeRepository.existsById(programmeDTO.getId())) {
          fieldErrors.add(new FieldError(POST_DTO_NAME, "programmes",
              String.format("Programme with id %d does not exist", programmeDTO.getId())));
        }
      }
    }

    return fieldErrors;
  }

  private List<FieldError> checkSites(PostDTO postDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the sites
    if (postDTO.getSites() != null && !postDTO.getSites().isEmpty()) {
      List<Long> siteIds = Lists.newArrayList();
      for (PostSiteDTO ps : postDTO.getSites()) {
        if (ps.getSiteId() == null) {
          fieldErrors.add(new FieldError(POST_DTO_NAME, "sites",
              "Site ID cannot be null or blank"));
        } else {
          siteIds.add(ps.getSiteId());
        }
      }
      if (!CollectionUtils.isEmpty(siteIds)) {
        Map<Long, Boolean> siteIdsExistsMap = referenceService.siteIdExists(siteIds);
        notExistsFieldErrors(fieldErrors, siteIdsExistsMap, "sites", "Site");
      }

    }
    return fieldErrors;
  }

  private List<FieldError> checkGrades(PostDTO postDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the grades
    if (postDTO.getGrades() != null && !postDTO.getGrades().isEmpty()) {
      List<Long> gradeIds = Lists.newArrayList();
      for (PostGradeDTO pg : postDTO.getGrades()) {
        if (pg.getGradeId() == null) {
          fieldErrors.add(new FieldError(POST_DTO_NAME, "grades",
              "Grade ID cannot be null or blank"));
        } else {
          gradeIds.add(pg.getGradeId());
        }
      }
      if (!CollectionUtils.isEmpty(gradeIds)) {
        Map<Long, Boolean> gradeIdsExistsMap = referenceService.gradeIdsExists(gradeIds);
        notExistsFieldErrors(fieldErrors, gradeIdsExistsMap, "grades", "Grade");
      }
    }
    return fieldErrors;
  }

  private void notExistsFieldErrors(List<FieldError> fieldErrors,
      Map<Long, Boolean> gradeIdsExistsMap,
      String field, String entityName) {
    gradeIdsExistsMap.forEach((k, v) -> {
      if (!v) {
        fieldErrors.add(new FieldError(POST_DTO_NAME, field,
            String.format("%s with id %s does not exist", entityName, k)));
      }
    });
  }

  private List<FieldError> checkSpecialties(PostDTO postDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the specialties
    if (postDTO.getSpecialties() != null && !postDTO.getSpecialties().isEmpty()) {
      int noOfPrimarySpecialtyCount = 0;
      for (PostSpecialtyDTO ps : postDTO.getSpecialties()) {
        if (ps.getSpecialty() == null || ps.getSpecialty().getId() < 0) {
          fieldErrors.add(new FieldError(POST_DTO_NAME, SPECIALTIES,
              "Specialty ID cannot be null or negative"));
        } else {
          if (!specialtyRepository.existsById(ps.getSpecialty().getId())) {
            fieldErrors.add(new FieldError(POST_DTO_NAME, SPECIALTIES,
                String.format("Specialty with id %d does not exist", ps.getSpecialty().getId())));
          } else if (PostSpecialtyType.PRIMARY.equals(ps.getPostSpecialtyType())) {
            ++noOfPrimarySpecialtyCount;
          } else if (PostSpecialtyType.SUB_SPECIALTY.equals(ps.getPostSpecialtyType())) {
            specialtyRepository.findSpecialtyByIdEagerFetch(ps.getSpecialty().getId())
                .ifPresent(specialty -> checkSpecialtyIsOfTypeSubspecialty(fieldErrors, specialty));
          }
        }
      }
      checkSpecialtyType(fieldErrors, noOfPrimarySpecialtyCount);
    }
    return fieldErrors;
  }

  private void checkSpecialtyIsOfTypeSubspecialty(List<FieldError> fieldErrors,
      Specialty specialty) {
    if (!specialty.getSpecialtyTypes().contains(SpecialtyType.SUB_SPECIALTY)) {
      fieldErrors.add(new FieldError(POST_DTO_NAME, SPECIALTIES,
          String.format("The specialty %s is not a %s", specialty.getName(),
              SpecialtyType.SUB_SPECIALTY)));
    }
  }

  private void checkSpecialtyType(List<FieldError> fieldErrors, int noOfPrimarySpecialtyCount) {
    if (noOfPrimarySpecialtyCount > 1) {
      fieldErrors.add(new FieldError(POST_DTO_NAME, SPECIALTIES,
          String.format("Only one Specialty of type %s allowed", PostSpecialtyType.PRIMARY)));
    }
  }

  private List<FieldError> checkPlacementHistory(PostDTO postDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    // then check the placement
    if (postDTO.getPlacementHistory() != null && !postDTO.getPlacementHistory().isEmpty()) {
      for (PlacementDTO ps : postDTO.getPlacementHistory()) {
        if (ps.getId() == null || ps.getId() < 0) {
          fieldErrors.add(new FieldError(POST_DTO_NAME, "placementHistory",
              "Placement ID cannot be null or negative"));
        } else {
          if (!placementRepository.existsById(ps.getId())) {
            fieldErrors.add(new FieldError(POST_DTO_NAME, "placementHistory",
                String.format("Placement with id %d does not exist", ps.getId())));
          }
        }
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkOwner(PostDTO postDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    //first check if the owner is valid
    if (!DesignatedBodyMapper.getAllOwners().contains(postDTO.getOwner())) {
      fieldErrors.add(new FieldError("postDTO", "owner",
          "Unknown owner: " + postDTO.getOwner()));
    }
    return fieldErrors;
  }

  private List<FieldError> checkNationalPostNumber(PostDTO postDTO) {
    List<FieldError> fieldErrors = new ArrayList<>();
    List<Post> postWithSameNPN = Lists.newArrayList();

    if (StringUtils.isNotBlank(postDTO.getNationalPostNumber())) {
      postWithSameNPN = postRepository.findByNationalPostNumber(postDTO.getNationalPostNumber());
    }

    if (postDTO.getId() == null && CollectionUtils.isNotEmpty(postWithSameNPN) && postDTO
        .isBypassNPNGeneration()) {
      fieldErrors.add(new FieldError("postDTO", "nationalPostNumber",
          "Cannot create post with NPN override there are other posts with the same NPN"));
    } else if (postDTO.getId() == null && CollectionUtils.isNotEmpty(postWithSameNPN)
        && !nationalPostNumberServiceImpl.isAutoGenNpnEnabled()) {
      fieldErrors.add(new FieldError("postDTO", "nationalPostNumber",
          "Cannot create post with NPN as there are other posts with the same NPN"));
    } else if (postDTO.getId() == null && StringUtils.isEmpty(postDTO.getNationalPostNumber())
        && !nationalPostNumberServiceImpl.isAutoGenNpnEnabled()) {
      fieldErrors.add(new FieldError("postDTO", "nationalPostNumber",
          "Cannot create new post with an empty NPN when auto generation is switched off"));
    } else if (postDTO.isBypassNPNGeneration()) {
      if (StringUtils.isBlank(postDTO.getNationalPostNumber())) {
        fieldErrors.add(new FieldError("postDTO", "nationalPostNumber",
            "You cannot have an empty NPN if you are overriding auto generation"));
      } else if (postWithSameNPN.size() > 1) {
        fieldErrors.add(new FieldError("postDTO", "nationalPostNumber",
            "Cannot update post with this NPN as there are other posts with the same NPN"));
      } else if (postWithSameNPN.size() == 1 && !postWithSameNPN.get(0).getId()
          .equals(postDTO.getId())) {
        fieldErrors.add(new FieldError("postDTO", "nationalPostNumber",
            "Cannot update post with this NPN as there are other posts with the same NPN"));
      }
    }
    return fieldErrors;
  }

  private List<FieldError> checkLegacy(Long postId) {
    List<FieldError> fieldErrors = new ArrayList<>();
    if (postId != null) {
      Post post = postRepository.findById(postId).orElse(null);
      if (post != null && post.isLegacy()) {
        fieldErrors.add(new FieldError("postDTO", "legacy",
            "You cannot update a post that has been migrated from intrepid and marked as legacy"));
      }
    }
    return fieldErrors;
  }

}
