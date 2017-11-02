package com.transformuk.hee.tis.tcs.service.runnable;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
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
public class PostGradeDecoratorRunnableTest {

  private static final String GRADE_NAME = "GRADE_NAME";
  private final static String GRADE_CODE = "GRADECODE";

  @InjectMocks
  private PostGradeDecoratorRunnable testObj;
  @Mock
  private CountDownLatch countDownLatchMock;
  @Mock
  private ReferenceService referenceServiceMock;

  private Set<String> gradeCodes;
  private List<GradeDTO> gradeDTOS;
  private List<PostViewDTO> postViews;
  private PostViewDTO postViewDTO1, missingDataPostViewDTO;

  @Before
  public void setup() {
    gradeCodes = Sets.newHashSet(GRADE_CODE);
    GradeDTO gradeDTO = new GradeDTO();
    gradeDTO.setAbbreviation(GRADE_CODE);
    gradeDTO.setName(GRADE_NAME);
    gradeDTOS = Lists.newArrayList(gradeDTO);

    postViewDTO1 = new PostViewDTO();
    postViewDTO1.setApprovedGradeCode(GRADE_CODE);

    missingDataPostViewDTO = new PostViewDTO();

    postViews = Lists.newArrayList(postViewDTO1, missingDataPostViewDTO);
    testObj = new PostGradeDecoratorRunnable(gradeCodes, postViews, countDownLatchMock, referenceServiceMock);

    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void runShouldPopulateGradeNamesOnPost() {
    when(referenceServiceMock.findGradesIn(gradeCodes)).thenReturn(gradeDTOS);

    testObj.run();

    Mockito.verify(referenceServiceMock).findGradesIn(gradeCodes);
    Mockito.verify(countDownLatchMock).countDown();

    Assert.assertEquals(GRADE_NAME, postViewDTO1.getApprovedGradeName());
    Assert.assertNull(missingDataPostViewDTO.getApprovedGradeName());
  }

  @Test
  public void runShouldContinueWhenRestCallFails() {

    doThrow(new RuntimeException("HTTP 500 error")).when(referenceServiceMock).findGradesIn(gradeCodes);

    testObj.run();

    Mockito.verify(referenceServiceMock).findGradesIn(gradeCodes);
    Mockito.verify(countDownLatchMock).countDown();

    Assert.assertNull(postViewDTO1.getApprovedGradeName());
    Assert.assertNull(missingDataPostViewDTO.getApprovedGradeName());
  }


}