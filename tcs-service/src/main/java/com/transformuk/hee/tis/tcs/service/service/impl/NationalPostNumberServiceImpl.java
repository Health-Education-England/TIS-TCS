package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.service.api.decorator.AsyncReferenceService;
import com.transformuk.hee.tis.tcs.service.exception.NationalPostNumberRuntimeException;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class NationalPostNumberServiceImpl {

  private static final int LOCAL_OFFICE_ABBR_INDEX = 0;
  private static final int SITE_CODE_INDEX = 1;
  private static final int SPECIALTY_CODE_INDEX = 2;
  private static final int GRADE_ABBR_INDEX = 3;
  private static final int UNIQUE_COUNTER_INDEX = 4;
  private static final int SUFFIX_INDEX = 5;
  private static final String SLASH = "/";

  private static final Map<String, String> dbcToLocalOfficeAbbrMap = ImmutableMap.<String, String>builder()
      .put("1-AIIDR8", "LDN")
      .put("1-AIIDWA", "LDN")
      .put("1-AIIDVS", "LDN")
      .put("1-AIIDWI", "LDN")
      .put("1-AIIDSA", "EMD")
      .put("1-AIIDWT", "EOE")
      .put("1-AIIDSI", "NTH")
      .put("1-AIIDH1", "OXF")
      .put("1-AIIDQQ", "YHD")
      .put("1-AIIDMY", "WMD")
      .put("1-AIIDMQ", "SW")
      .put("1-AIIDHJ", "WES")
      .put("1-AIIDNQ", "NWN")
      .build();

  @Autowired
  private AsyncReferenceService asyncReferenceService;
  @Autowired
  private PostRepository postRepository;
  @Autowired
  private SpecialtyRepository specialtyRepository;


  /**
   * Check the new post against the current post in the system. If there are changes in some of the values,
   * a new national post number will need to be generated
   *
   * @param postDTO
   * @return
   */
  @Transactional(readOnly = true)
  boolean requireNewNationalPostNumber(PostDTO postDTO) {
    Post currentPost = postRepository.findOne(postDTO.getId());
    if (currentPost == null) {
      return true;
    } else {
      boolean generateNewNumber = false;

      String localOfficeAbbr = getLocalOfficeAbbr();
      SiteDTO siteCodeContainer = new SiteDTO();
      GradeDTO gradeAbbrContainer = new GradeDTO();
      CompletableFuture.allOf(
          getSiteCode(postDTO, siteCodeContainer),
          getApprovedGrade(postDTO, gradeAbbrContainer))
          .join();
      String specialtyCode = getPrimarySpecialtyCode(postDTO);
      String suffixValue = postDTO.getSuffix() != null ? postDTO.getSuffix().getSuffixValue() : StringUtils.EMPTY;

      String currentNationalPostNumber = currentPost.getNationalPostNumber();
      String[] currentNationalPostNumberParts = currentNationalPostNumber.split(SLASH);
      if (currentNationalPostNumberParts.length == 5 || currentNationalPostNumberParts.length == 6) { // does the national post number have the correct number of parts?
        String currentPostLocalOfficeAbbr = currentNationalPostNumberParts[LOCAL_OFFICE_ABBR_INDEX];
        String currentPostSiteCode = currentNationalPostNumberParts[SITE_CODE_INDEX];
        String currentPostSpecialtyCode = currentNationalPostNumberParts[SPECIALTY_CODE_INDEX];
        String currentPostGradeAbbr = currentNationalPostNumberParts[GRADE_ABBR_INDEX];
        String currentPostSuffix = StringUtils.EMPTY;
        if (currentNationalPostNumberParts.length == 6) {
          currentPostSuffix = currentNationalPostNumberParts[SUFFIX_INDEX];
        }

        if (hasLocalOfficeAbbrChanged(localOfficeAbbr, currentPostLocalOfficeAbbr) ||
            hasSiteCodeChanged(siteCodeContainer, currentPostSiteCode) ||
            hasSpecialtyCodeChanged(specialtyCode, currentPostSpecialtyCode) ||
            hasGradeAbbrChanged(gradeAbbrContainer, currentPostGradeAbbr) ||
            hasSuffixChanged(suffixValue, currentPostSuffix)) {
          generateNewNumber = true;
        }
      }
      return generateNewNumber;
    }
  }

  private boolean hasLocalOfficeAbbrChanged(String localOfficeAbbr, String currentPostLocalOfficeAbbr) {
    return !localOfficeAbbr.equals(currentPostLocalOfficeAbbr);
  }

  private boolean hasSiteCodeChanged(SiteDTO siteCodeContainer, String currentPostLocationCode) {
    return !siteCodeContainer.getSiteCode().equals(currentPostLocationCode);
  }

  private boolean hasSpecialtyCodeChanged(String specialtyCode, String currentPostSpecialtyCode) {
    return !specialtyCode.equals(currentPostSpecialtyCode);
  }

  private boolean hasGradeAbbrChanged(GradeDTO gradeAbbrContainer, String currentPostGradeAbbr) {
    return !gradeAbbrContainer.getAbbreviation().equals(currentPostGradeAbbr);
  }

  private boolean hasSuffixChanged(String suffixValue, String currentPostSuffix) {
    return !suffixValue.equals(currentPostSuffix);
  }

  public void generateAndSetNewNationalPostNumber(PostDTO postDTO) {
    SiteDTO siteCodeContainer = new SiteDTO();
    GradeDTO gradeAbbrContainer = new GradeDTO();

    CompletableFuture.allOf(getApprovedGrade(postDTO, gradeAbbrContainer), getSiteCode(postDTO, siteCodeContainer))
        .join();

    String newSpecialtyCode = getPrimarySpecialtyCode(postDTO);

    String nationalPostNumber = generateNationalPostNumber(getLocalOfficeAbbr(), siteCodeContainer.getSiteCode(),
        newSpecialtyCode, gradeAbbrContainer.getAbbreviation(), postDTO.getSuffix());

    postDTO.setNationalPostNumber(nationalPostNumber);
  }

  public String generateNationalPostNumber(String localOfficeAbbr, String siteCode, String specialtyCode,
                                           String gradeAbbr, PostSuffix suffix) {
    Preconditions.checkNotNull(localOfficeAbbr);
    Preconditions.checkNotNull(siteCode);
    Preconditions.checkNotNull(specialtyCode);
    Preconditions.checkNotNull(gradeAbbr);

    String nationalPostNumberNoCounter = localOfficeAbbr + SLASH + siteCode + SLASH + specialtyCode + SLASH + gradeAbbr;

    Set<Post> postsWithSamePostNumber = postRepository.findByNationalPostNumberStartingWith(nationalPostNumberNoCounter);

    List<Integer> postNumberCounter = postsWithSamePostNumber.stream()
        .map(Post::getNationalPostNumber)
        .filter(StringUtils::isNotBlank)
        .filter(isPostNpnRightFormat(suffix))
        .map(npn -> npn.split(SLASH))
        .filter(npn -> npn.length > 1)
        .map(mapToUniqueCounter(suffix))
        .filter(NumberUtils::isDigits)
        .map(Integer::parseInt)
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList());

    String result;

    // up the counter part of the NPN
    if (CollectionUtils.isNotEmpty(postNumberCounter)) {
      Integer currentHighestCounter = postNumberCounter.iterator().next();
      String leftPadded = StringUtils.leftPad(Integer.toString(++currentHighestCounter), 3, '0');
      result = nationalPostNumberNoCounter + SLASH + leftPadded;

    } else {
      result = nationalPostNumberNoCounter + SLASH + "001";
    }

    // add suffix if provided
    if (suffix != null) {
      result += SLASH + suffix.getSuffixValue();
    }

    return result;
  }

  private Function<String[], String> mapToUniqueCounter(PostSuffix suffix) {
    return npn -> {
      if (suffix != null) {
        return npn[npn.length - 2]; //if we have a suffix, then the number component is second to last
      } else {
        return npn[npn.length - 1];
      }
    };
  }

  private Predicate<String> isPostNpnRightFormat(PostSuffix suffix) {
    return npn -> {
      if (suffix != null) {
        return npn.endsWith(suffix.getSuffixValue()); //filter out any npn's that dont match the suffix we're trying to generate
      } else {
        String[] npnParts = npn.split("/");
        String lastNpnPart = npnParts[npnParts.length - 1];
        return NumberUtils.isDigits(lastNpnPart); //if no suffix is supplied, filter out any that end with non digit
      }
    };
  }

  String getLocalOfficeAbbr() {
    UserProfile userProfile = getProfileFromContext();
    String dbc = userProfile.getDesignatedBodyCodes().stream().findFirst().orElseGet(() -> {
      throw new NationalPostNumberRuntimeException("No DBC code found for current logged in user:" + userProfile.getEmailAddress() + " - cannot generate full NPN");
    });
    String localOfficeAbbr = dbcToLocalOfficeAbbrMap.get(dbc);
    return localOfficeAbbr != null ? localOfficeAbbr : StringUtils.EMPTY;
  }

  /**
   * Helper method for testing so that the result of the static call can be spied
   *
   * @return
   */
  UserProfile getProfileFromContext() {
    return TisSecurityHelper.getProfileFromContext();
  }

  String getPrimarySpecialtyCode(PostDTO postDTO) {
    if (CollectionUtils.isNotEmpty(postDTO.getSpecialties())) {
      Long specialtyId = postDTO.getSpecialties().stream()
          .filter(sp -> PostSpecialtyType.PRIMARY.equals(sp.getPostSpecialtyType()))
          .map(ps -> ps.getSpecialty().getId())
          .findAny().orElseGet(() -> {
            throw new NationalPostNumberRuntimeException("No Primary Specialty ID found for PostSpecialty relation - cannot generate full NPN");
          });

      Specialty primarySpecialty = specialtyRepository.findOne(specialtyId);
      return primarySpecialty != null ? primarySpecialty.getSpecialtyCode() : StringUtils.EMPTY;

    }
    return StringUtils.EMPTY;
  }

  CompletableFuture<Void> getApprovedGrade(PostDTO postDTO, GradeDTO gradeDTO) {
    if (CollectionUtils.isNotEmpty(postDTO.getGrades())) {
      Long gradeId = postDTO.getGrades().stream()
          .filter(g -> PostGradeType.APPROVED.equals(g.getPostGradeType()))
          .map(PostGradeDTO::getGradeId)
          .findAny().orElseGet(() -> {
            throw new NationalPostNumberRuntimeException("No Approved Grade ID found for PostGrade relation - cannot generate full NPN");
          });

      return asyncReferenceService.doWithGradesAsync(Sets.newHashSet(gradeId), gradeIdsToGrades -> {
        gradeDTO.setId(gradeId);
        gradeDTO.setAbbreviation(gradeIdsToGrades.get(gradeId).getAbbreviation());
      });

    }
    return CompletableFuture.completedFuture(null);
  }

  CompletableFuture<Void> getSiteCode(PostDTO postDTO, SiteDTO siteDTO) {
    if (CollectionUtils.isNotEmpty(postDTO.getSites())) {
      Long siteId = postDTO.getSites().stream().filter(s -> PostSiteType.PRIMARY.equals(s.getPostSiteType()))
          .map(PostSiteDTO::getSiteId)
          .findAny().orElseGet(() -> {
            throw new NationalPostNumberRuntimeException("No Primary Site ID found for PostSite relation - cannot generate full NPN");
          });

      return asyncReferenceService.doWithSitesAsync(Sets.newHashSet(siteId), siteIdsToSites -> {
        siteDTO.setId(siteId);
        siteDTO.setSiteCode(siteIdsToSites.get(siteId).getSiteCode());
      });
    }
    return CompletableFuture.completedFuture(null);
  }


}
