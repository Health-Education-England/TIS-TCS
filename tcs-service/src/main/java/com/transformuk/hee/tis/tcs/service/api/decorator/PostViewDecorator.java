package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.service.runnable.PostDecoratorRunnable;
import com.transformuk.hee.tis.tcs.service.runnable.PostGradeDecoratorRunnable;
import com.transformuk.hee.tis.tcs.service.runnable.PostSiteDecoratorRunnable;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Used to decorate the Post View list with labels such as grade and site labels
 */
@Component
public class PostViewDecorator {

  public static final int LATCH_COUNT = 2;
  private static final Logger log = LoggerFactory.getLogger(PostViewDecorator.class);
  private ReferenceService referenceService;
  private ExecutorService executorService;

  @Autowired
  public PostViewDecorator(ReferenceService referenceService, ExecutorService executorService) {
    this.referenceService = referenceService;
    this.executorService = executorService;
  }

  /**
   * Decorates the given post views with sites and grades labels
   *
   * @param postViews the post views to decorate
   */
  public void decorate(List<PostViewDTO> postViews) {
    // collect all the codes from the list
    Set<String> gradeCodes = new HashSet<>();
    Set<String> siteCodes = new HashSet<>();
    postViews.forEach(postView -> {
      if (StringUtils.isNotBlank(postView.getApprovedGradeCode())) {
        gradeCodes.add(postView.getApprovedGradeCode());
      }
      if (StringUtils.isNotBlank(postView.getPrimarySiteCode())) {
        siteCodes.add(postView.getPrimarySiteCode());
      }
    });

    CountDownLatch latch = getCountDownLatch(LATCH_COUNT);
    PostDecoratorRunnable gradesDecorator  = createGradesDecorator(gradeCodes, postViews, latch);
    executorService.submit(gradesDecorator);

    PostDecoratorRunnable sitesDecorator = createSiteDecorator(siteCodes, postViews, latch);
    executorService.submit(sitesDecorator);

    try {
      latch.await();
    } catch (InterruptedException e) {
      log.error("Exception occurred while waiting for executor service to shutdown {}", e);
    }
  }

  protected CountDownLatch getCountDownLatch(int count) {
    return new CountDownLatch(count);
  }

  protected PostDecoratorRunnable createGradesDecorator(Set<String> gradeCodes, List<PostViewDTO> postViews,
                                                      CountDownLatch latch) {
    return new PostGradeDecoratorRunnable(gradeCodes, postViews, latch, referenceService);
  }

  protected PostDecoratorRunnable createSiteDecorator(Set<String> siteCodes, List<PostViewDTO> postViews,
                                                      CountDownLatch latch) {
    return new PostSiteDecoratorRunnable(siteCodes, postViews, latch, referenceService);
  }

}
