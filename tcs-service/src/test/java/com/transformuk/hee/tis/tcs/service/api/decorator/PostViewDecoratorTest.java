package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class PostViewDecoratorTest {

  @Mock
  private ReferenceService referenceService;


  @InjectMocks
  private PostViewDecorator postViewDecorator;


  @Test
  public void shouldDecorateIfSitesGradesFound() {
    // given
    Set<String> gradeCodes = Sets.newHashSet("grade1", "grade2");
    GradeDTO gradeDTO1 = new GradeDTO();
    gradeDTO1.setAbbreviation("grade1");
    gradeDTO1.setName("gradeName1");
    GradeDTO gradeDTO2 = new GradeDTO();
    gradeDTO2.setAbbreviation("grade2");
    gradeDTO2.setName("gradeName2");
    given(referenceService.findGradesIn(gradeCodes)).willReturn(Lists.newArrayList(gradeDTO1, gradeDTO2));

    Set<String> siteCodes = Sets.newHashSet("site1", "site2");
    SiteDTO siteDTO1 = new SiteDTO();
    siteDTO1.setSiteCode("site1");
    siteDTO1.setSiteName("siteName1");
    SiteDTO siteDTO2 = new SiteDTO();
    siteDTO2.setSiteCode("site2");
    siteDTO2.setSiteName("siteName2");
    given(referenceService.findSitesIn(siteCodes)).willReturn(Lists.newArrayList(siteDTO1, siteDTO2));

    PostViewDTO postViewDTO1 = new PostViewDTO();
    postViewDTO1.setApprovedGradeCode("grade1");
    postViewDTO1.setPrimarySiteCode("site1");
    PostViewDTO postViewDTO2 = new PostViewDTO();
    postViewDTO2.setApprovedGradeCode("grade2");
    postViewDTO2.setPrimarySiteCode("site2");
    List<PostViewDTO> postViewDTOList = Lists.newArrayList(postViewDTO1, postViewDTO2);

    // when
    postViewDecorator.decorate(postViewDTOList);

    // then
    PostViewDTO decoratedPostViewDTO1 = postViewDTOList.get(0);
    PostViewDTO decoratedPostViewDTO2 = postViewDTOList.get(1);

    assertEquals(decoratedPostViewDTO1.getApprovedGradeCode(), "grade1");
    assertEquals(decoratedPostViewDTO1.getApprovedGradeName(), "gradeName1");
    assertEquals(decoratedPostViewDTO1.getPrimarySiteCode(), "site1");
    assertEquals(decoratedPostViewDTO1.getPrimarySiteName(), "siteName1");
    assertEquals(decoratedPostViewDTO2.getApprovedGradeCode(), "grade2");
    assertEquals(decoratedPostViewDTO2.getApprovedGradeName(), "gradeName2");
    assertEquals(decoratedPostViewDTO2.getPrimarySiteCode(), "site2");
    assertEquals(decoratedPostViewDTO2.getPrimarySiteName(), "siteName2");
  }

  @Test
  public void shouldDecorateIfSitesGradesNotFound() {
    // given
    Set<String> gradeCodes = Sets.newHashSet("grade1", "grade2");
    given(referenceService.findGradesIn(gradeCodes)).willReturn(new ArrayList<>());

    Set<String> siteCodes = Sets.newHashSet("site1", "site2");
    given(referenceService.findSitesIn(siteCodes)).willReturn(new ArrayList<>());

    PostViewDTO postViewDTO1 = new PostViewDTO();
    postViewDTO1.setApprovedGradeCode("grade1");
    postViewDTO1.setPrimarySiteCode("site1");
    PostViewDTO postViewDTO2 = new PostViewDTO();
    postViewDTO2.setApprovedGradeCode("grade2");
    postViewDTO2.setPrimarySiteCode("site2");
    List<PostViewDTO> postViewDTOList = Lists.newArrayList(postViewDTO1, postViewDTO2);

    // when
    postViewDecorator.decorate(postViewDTOList);

    // then
    PostViewDTO decoratedPostViewDTO1 = postViewDTOList.get(0);
    PostViewDTO decoratedPostViewDTO2 = postViewDTOList.get(1);

    assertEquals(decoratedPostViewDTO1.getApprovedGradeCode(), "grade1");
    assertNull(decoratedPostViewDTO1.getApprovedGradeName());
    assertEquals(decoratedPostViewDTO1.getPrimarySiteCode(), "site1");
    assertNull(decoratedPostViewDTO1.getPrimarySiteName());
    assertEquals(decoratedPostViewDTO2.getApprovedGradeCode(), "grade2");
    assertNull(decoratedPostViewDTO2.getApprovedGradeName());
    assertEquals(decoratedPostViewDTO2.getPrimarySiteCode(), "site2");
    assertNull(decoratedPostViewDTO2.getPrimarySiteName());
  }

}