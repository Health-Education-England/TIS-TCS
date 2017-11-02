package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.service.runnable.PostDecoratorRunnable;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator.LATCH_COUNT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PostViewDecoratorTest {

  private static final String PRIMARY_SITE_CODE_1 = "site1";
  private static final String APPROVED_GRADE_CODE_1 = "grade1";

  @Spy
  @InjectMocks
  private PostViewDecorator testObj;
  @Mock
  private ReferenceService referenceServiceMock;
  @Mock
  private ExecutorService executorServiceMock;
  @Mock
  private PostDecoratorRunnable gradeDecoratorRunnableMock, siteDecoratorRunnableMock;

  private List<PostViewDTO> postViewsList;
  private Set<String> siteCodesSet, gradeCodeSet;
  private PostViewDTO postView1, postViewWithNoGradeOrSite2;
  private CountDownLatch countDownLatch;

  @Before
  public void setup() {
    postView1 = new PostViewDTO();
    postView1.setPrimarySiteCode(PRIMARY_SITE_CODE_1);
    postView1.setApprovedGradeCode(APPROVED_GRADE_CODE_1);

    postViewWithNoGradeOrSite2 = new PostViewDTO();

    postViewsList = Lists.newArrayList(postView1, postViewWithNoGradeOrSite2);
    siteCodesSet = Sets.newHashSet(PRIMARY_SITE_CODE_1);
    gradeCodeSet = Sets.newHashSet(APPROVED_GRADE_CODE_1);

    countDownLatch = new CountDownLatch(LATCH_COUNT);
  }

  @Test(timeout = 5000L)
  public void decorateShouldSetGradesAndPosts() {

    doReturn(countDownLatch).when(testObj).getCountDownLatch(LATCH_COUNT);
    doReturn(gradeDecoratorRunnableMock).when(testObj).createGradesDecorator(eq(gradeCodeSet), eq(postViewsList), any(CountDownLatch.class));
    doReturn(siteDecoratorRunnableMock).when(testObj).createSiteDecorator(eq(siteCodesSet), eq(postViewsList), any(CountDownLatch.class));
    when(executorServiceMock.submit(gradeDecoratorRunnableMock)).then((mock) -> {
      countDownLatch.countDown();
      return CompletableFuture.completedFuture("");
    });
    when(executorServiceMock.submit(siteDecoratorRunnableMock)).then((mock) -> {
      countDownLatch.countDown();
      return CompletableFuture.completedFuture("");
    });
    when(executorServiceMock.submit(siteDecoratorRunnableMock)).thenReturn(null);

    testObj.decorate(postViewsList);

    verify(executorServiceMock, times(2)).submit(any(PostDecoratorRunnable.class));

  }

}