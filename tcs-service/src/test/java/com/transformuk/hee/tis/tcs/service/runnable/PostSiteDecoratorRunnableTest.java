package com.transformuk.hee.tis.tcs.service.runnable;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(BlockJUnit4ClassRunner.class)
public class PostSiteDecoratorRunnableTest {

  private static final String SITE_CODE = "SITE_CODE";
  private static final String SITE_NAME = "SITE_NAME";

  @InjectMocks
  private PostSiteDecoratorRunnable testObj;
  @Mock
  private CountDownLatch countDownLatchMock;
  @Mock
  private ReferenceService referenceServiceMock;

  private Set<String> siteCodes;
  private List<SiteDTO> siteDTOS;
  private List<PostViewDTO> postViews;
  private PostViewDTO postViewDTO1, missingDataPostViewDTO;

  @Before
  public void setup() {
    siteCodes = Sets.newHashSet(SITE_CODE);

    SiteDTO siteDTO = new SiteDTO();
    siteDTO.setSiteCode(SITE_CODE);
    siteDTO.setSiteName(SITE_NAME);
    siteDTOS = Lists.newArrayList(siteDTO);

    postViewDTO1 = new PostViewDTO();
    postViewDTO1.setPrimarySiteCode(SITE_CODE);

    missingDataPostViewDTO = new PostViewDTO();

    postViews = Lists.newArrayList(postViewDTO1, missingDataPostViewDTO);
    testObj = new PostSiteDecoratorRunnable(siteCodes, postViews, countDownLatchMock, referenceServiceMock);

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void runShouldPopulateGradeNamesOnPost() {
    when(referenceServiceMock.findSitesIn(siteCodes)).thenReturn(siteDTOS);

    testObj.run();

    Mockito.verify(referenceServiceMock).findSitesIn(siteCodes);
    Mockito.verify(countDownLatchMock).countDown();

    Assert.assertEquals(SITE_NAME, postViewDTO1.getPrimarySiteName());
    Assert.assertNull(missingDataPostViewDTO.getPrimarySiteName());
  }

  @Test
  public void runShouldContinueWhenRestCallFails() {

    doThrow(new RuntimeException("HTTP 500 error")).when(referenceServiceMock).findGradesIn(siteCodes);

    testObj.run();

    Mockito.verify(referenceServiceMock).findSitesIn(siteCodes);
    Mockito.verify(countDownLatchMock).countDown();

    Assert.assertNull(postViewDTO1.getPrimarySiteName());
    Assert.assertNull(missingDataPostViewDTO.getPrimarySiteName());
  }


}