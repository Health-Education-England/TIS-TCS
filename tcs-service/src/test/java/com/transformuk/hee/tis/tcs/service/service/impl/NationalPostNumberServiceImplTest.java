package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.tcs.api.dto.PostDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostGradeDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSiteDTO;
import com.transformuk.hee.tis.tcs.api.dto.PostSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PostGradeType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSiteType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSpecialtyType;
import com.transformuk.hee.tis.tcs.api.enumeration.PostSuffix;
import com.transformuk.hee.tis.tcs.service.api.decorator.AsyncReferenceService;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostGrade;
import com.transformuk.hee.tis.tcs.service.model.PostSpecialty;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import org.apache.commons.lang.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anySet;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class NationalPostNumberServiceImplTest {

  private static final Long SITE_ID = 12345L;
  private static final long POST_ID = 1L;
  private static final long GRADE_ID = 2L;
  private static final String MILITARY_SUFFIX = "M";
  private static final String SLASH = "/";
  private static final String LOCAL_OFFICE_ABBR = "NTH";
  private static final String SITE_CODE = "RTD01";
  private static final String SPECIALTY_CODE = "007";
  private static final Long SPECIALTY_ID = 10L;
  private static final String GRADE_ABBR = "STR";
  private static final String UNIQUE_NUMBER = "006";
  private static final String UNIQUE_NUMBER_PLUS_1 = "007";

  private static final String CURRENT_NATIONAL_POST_NUMBER = LOCAL_OFFICE_ABBR + SLASH + SITE_CODE + SLASH +
      SPECIALTY_CODE + SLASH + GRADE_ABBR + SLASH + UNIQUE_NUMBER;

  @Spy
  @InjectMocks
  private NationalPostNumberServiceImpl testObj;

  @Mock
  private AsyncReferenceService asyncReferenceServiceMock;
  @Mock
  private PostRepository postRepositoryMock;
  @Mock
  private SpecialtyRepository specialtyRepositoryMock;
  @Mock
  private PostGradeDTO postGradeDTOMock;
  @Mock
  private PostSiteDTO postSiteDTOMock;
  @Mock
  private PostSpecialtyDTO postSpecialtyDTOMock;
  @Mock
  private SpecialtyDTO specialtyDTOMock;
  @Mock
  private PostDTO postDTOMock1;
  @Mock
  private Post postMock1;
  @Mock
  private Specialty specialtyMock;
  @Mock
  private PostSpecialty postSpecialtyMock;
  @Mock
  private PostGrade postGradeMock;

  @Before
  public void setup() {
    when(postGradeDTOMock.getGradeId()).thenReturn(GRADE_ID);
    when(postGradeDTOMock.getPostGradeType()).thenReturn(PostGradeType.APPROVED);
    when(postSiteDTOMock.getSiteId()).thenReturn(SITE_ID);
    when(postSiteDTOMock.getPostSiteType()).thenReturn(PostSiteType.PRIMARY);
    when(postSpecialtyDTOMock.getSpecialty()).thenReturn(specialtyDTOMock);
    when(postSpecialtyDTOMock.getPostSpecialtyType()).thenReturn(PostSpecialtyType.PRIMARY);
    when(specialtyDTOMock.getSpecialtyCode()).thenReturn(SPECIALTY_CODE);
    when(specialtyDTOMock.getId()).thenReturn(SPECIALTY_ID);

    when(postDTOMock1.getId()).thenReturn(POST_ID);
    when(postDTOMock1.getGrades()).thenReturn(Sets.newHashSet(postGradeDTOMock));
    when(postDTOMock1.getSpecialties()).thenReturn(Sets.newHashSet(postSpecialtyDTOMock));
    when(postDTOMock1.getSites()).thenReturn(Sets.newHashSet(postSiteDTOMock));

    when(postRepositoryMock.findOne(POST_ID)).thenReturn(postMock1);
    when(postMock1.getNationalPostNumber()).thenReturn(CURRENT_NATIONAL_POST_NUMBER);
    when(postSpecialtyMock.getPostSpecialtyType()).thenReturn(PostSpecialtyType.PRIMARY);
    when(postSpecialtyMock.getSpecialty()).thenReturn(specialtyMock);
    when(specialtyMock.getSpecialtyCode()).thenReturn(SPECIALTY_CODE);
    when(postMock1.getSpecialties()).thenReturn(Sets.newHashSet(postSpecialtyMock));
    when(postGradeMock.getPostGradeType()).thenReturn(PostGradeType.APPROVED);
    when(postGradeMock.getGradeId()).thenReturn(GRADE_ID);
    when(postMock1.getGrades()).thenReturn(Sets.newHashSet(postGradeMock));
  }

  @Test
  public void generateNationalPostNumberShouldReturnNewUniquePostNumberWithCounterOneHigherThanCurrentHighest() {
    when(postMock1.getNationalPostNumber()).thenReturn(LOCAL_OFFICE_ABBR + SLASH + SITE_CODE + SLASH +
        SPECIALTY_CODE + SLASH + GRADE_ABBR + SLASH + UNIQUE_NUMBER + SLASH + MILITARY_SUFFIX);
    when(postMock1.getSuffix()).thenReturn(PostSuffix.MILITARY);
    when(postRepositoryMock.findByNationalPostNumberStartingWith(LOCAL_OFFICE_ABBR + SLASH + SITE_CODE + SLASH +
        SPECIALTY_CODE + SLASH + GRADE_ABBR)).thenReturn(Sets.newHashSet(postMock1));

    String result = testObj.generateNationalPostNumber(LOCAL_OFFICE_ABBR, SITE_CODE, SPECIALTY_CODE, GRADE_ABBR, PostSuffix.MILITARY);

    String expectedNationalPostNumber = LOCAL_OFFICE_ABBR + SLASH + SITE_CODE + SLASH +
        SPECIALTY_CODE + SLASH + GRADE_ABBR + SLASH + UNIQUE_NUMBER_PLUS_1 + SLASH + MILITARY_SUFFIX;

    Assert.assertEquals(expectedNationalPostNumber, result);
  }

  @Test
  public void getLocalOfficeAbbrShouldReturnTheAbbrForTheCurrentUser() {
    UserProfile userProfile = mock(UserProfile.class);
    when(userProfile.getDesignatedBodyCodes()).thenReturn(Sets.newHashSet("1-AIIDR8"));
    doReturn(userProfile).when(testObj).getProfileFromContext();

    String result = testObj.getLocalOfficeAbbrOrEmpty();

    Assert.assertEquals("LDN", result);
  }

  @Test
  public void getLocalOfficeAbbrShouldReturnEmptyWhenUsersDbcIsNotMapped() {
    UserProfile userProfile = mock(UserProfile.class);
    when(userProfile.getDesignatedBodyCodes()).thenReturn(Sets.newHashSet("RANDOM_DBC_CODE"));
    doReturn(userProfile).when(testObj).getProfileFromContext();

    String result = testObj.getLocalOfficeAbbrOrEmpty();

    Assert.assertEquals(StringUtils.EMPTY, result);
  }

  @Test
  public void getPrimarySpecialtyCodeOrEmptyShouldReturnAnEmptyStringWhenThereAreNoSpecialties() {
    when(postDTOMock1.getSpecialties()).thenReturn(null);
    String result = testObj.getPrimarySpecialtyCodeOrEmpty(postDTOMock1);
    Assert.assertEquals(StringUtils.EMPTY, result);
  }

  @Test
  public void getPrimarySpecialtyCodeOrEmptyShouldReturnCode() {
    when(specialtyRepositoryMock.findOne(SPECIALTY_ID)).thenReturn(specialtyMock);
    when(specialtyMock.getSpecialtyCode()).thenReturn(SPECIALTY_CODE);
    String result = testObj.getPrimarySpecialtyCodeOrEmpty(postDTOMock1);
    Assert.assertEquals(SPECIALTY_CODE, result);
  }

  @Test
  public void getApprovedOrEmptyShouldPopulateGradeWithAbbr() {
    GradeDTO gradeDTO = new GradeDTO();
    CompletableFuture<Void> expectedCompletedFuture = CompletableFuture.completedFuture(null);
    when(asyncReferenceServiceMock.doWithGradesAsync(any(), any())).thenReturn(expectedCompletedFuture);

    CompletableFuture<Void> result = testObj.getApprovedGradeOrEmpty(postDTOMock1, gradeDTO);
    Assert.assertEquals(expectedCompletedFuture, result);
  }

  @Test
  public void getSiteCodeOrEmptyShouldPopulateSiteWithCode() {
    SiteDTO siteDTO = new SiteDTO();
    CompletableFuture<Void> expectedCompletedFuture = CompletableFuture.completedFuture(null);
    when(asyncReferenceServiceMock.doWithSitesAsync(anySet(), any())).thenReturn(expectedCompletedFuture);

    CompletableFuture<Void> result = testObj.getSiteCodeOrEmpty(postDTOMock1, siteDTO);
    Assert.assertEquals(expectedCompletedFuture, result);
  }

}