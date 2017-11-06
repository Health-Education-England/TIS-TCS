package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostViewDecoratorTest {

  private static final String PRIMARY_SITE_CODE_1 = "site1";
  private static final String APPROVED_GRADE_CODE_1 = "grade1";
  private static final String GRADE_ABBR = "GRADE_ABBR";
  private static final String SITE_CODE = "SITE_CODE";
  private static final String GRADE_NAME = "GRADE NAME";
  private static final String PRIMARY_SITE_NAME = "PRIMARY SITE NAME";

  @Spy
  @InjectMocks
  private PostViewDecorator testObj;
  @Mock
  private ReferenceService referenceServiceMock;
  @Mock
  private PostViewDTO postViewDTO1Mock, postViewDTO2Mock;
  @Mock
  private CompletableFuture<Void> gradesFutureMock, siteFutureMock;

  private List<PostViewDTO> postViewsList;
  private Set<String> siteCodesSet, gradeCodeSet;
  private PostViewDTO postView1, postViewWithNoGradeOrSite2;

  @Before
  public void setup() {
    postView1 = new PostViewDTO();
    postView1.setPrimarySiteCode(PRIMARY_SITE_CODE_1);
    postView1.setApprovedGradeCode(APPROVED_GRADE_CODE_1);

    postViewWithNoGradeOrSite2 = new PostViewDTO();

    postViewsList = Lists.newArrayList(postView1, postViewWithNoGradeOrSite2);
    siteCodesSet = Sets.newHashSet(PRIMARY_SITE_CODE_1);
    gradeCodeSet = Sets.newHashSet(APPROVED_GRADE_CODE_1);


    when(postViewDTO1Mock.getApprovedGradeCode()).thenReturn(GRADE_ABBR);
    when(postViewDTO1Mock.getApprovedGradeName()).thenReturn(GRADE_NAME);
    when(postViewDTO1Mock.getPrimarySiteCode()).thenReturn(SITE_CODE);
    when(postViewDTO1Mock.getPrimarySiteName()).thenReturn(PRIMARY_SITE_NAME);
  }

  @Test(timeout = 5000L)
  public void decorateShouldSetGradesAndPosts() {

    CompletableFuture<Class<Void>> gradesCompletedFuture = CompletableFuture.completedFuture(Void.class);
    CompletableFuture<Class<Void>> sitesCompletedFuture = CompletableFuture.completedFuture(Void.class);
    doReturn(gradesCompletedFuture).when(testObj).decorateGradesOnPost(gradeCodeSet, postViewsList);
    doReturn(sitesCompletedFuture).when(testObj).decorateSitesOnPost(siteCodesSet, postViewsList);

    testObj.decorate(postViewsList);

    verify(testObj).decorateGradesOnPost(gradeCodeSet, postViewsList);
    verify(testObj).decorateSitesOnPost(siteCodesSet, postViewsList);
  }

  @Test
  public void decorateGradesOnPostShouldCallReferenceAndPopulatePost() {
    GradeDTO gradeDTO = new GradeDTO();
    gradeDTO.setAbbreviation(GRADE_ABBR);
    gradeDTO.setName(GRADE_NAME);
    List<GradeDTO> gradesDTO = Lists.newArrayList(gradeDTO);
    List<PostViewDTO> postViewDTOS = Lists.newArrayList(postViewDTO1Mock, postViewDTO2Mock);

    when(referenceServiceMock.findGradesIn(gradeCodeSet)).thenReturn(gradesDTO);

    testObj.decorateGradesOnPost(gradeCodeSet, postViewDTOS);

    verify(postViewDTO1Mock).setApprovedGradeName(GRADE_NAME);
    verify(postViewDTO2Mock, never()).setApprovedGradeName(any());
  }

  @Test
  public void decorateSitesOnPostShouldCallReferenceAndPopulatePost() {
    SiteDTO siteDTO = new SiteDTO();
    siteDTO.setSiteCode(SITE_CODE);
    siteDTO.setSiteName(PRIMARY_SITE_NAME);
    List<SiteDTO> siteDTOS = Lists.newArrayList(siteDTO);
    List<PostViewDTO> postViewDTOS = Lists.newArrayList(postViewDTO1Mock, postViewDTO2Mock);

    when(referenceServiceMock.findSitesIn(siteCodesSet)).thenReturn(siteDTOS);

    testObj.decorateSitesOnPost(siteCodesSet, postViewDTOS);

    verify(postViewDTO1Mock).setPrimarySiteName(PRIMARY_SITE_NAME);
    verify(postViewDTO2Mock, never()).setPrimarySiteName(any());
  }

}