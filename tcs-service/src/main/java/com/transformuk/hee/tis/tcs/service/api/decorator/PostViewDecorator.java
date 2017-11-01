package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Used to decorate the Post View list with labels such as grade and site labels
 */
@Component
public class PostViewDecorator {

  private static final Logger log = LoggerFactory.getLogger(PostViewDecorator.class);
  private ReferenceService referenceService;
  private DelegatingSecurityContextExecutorService executorService;

  @Autowired
  public PostViewDecorator(ReferenceService referenceService, DelegatingSecurityContextExecutorService executorService) {
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

    CountDownLatch latch = new CountDownLatch(2);
    decorateGradesOnPost(gradeCodes, executorService, postViews, latch);
    decorateSiteOnPost(siteCodes, executorService, postViews, latch);

    try {
      latch.await();
    } catch (InterruptedException e) {
      log.error("Exception occurred while waiting for executor service to shutdown {}", e);
    }
  }


  private void decorateGradesOnPost(Set<String> gradeCodes, ExecutorService executorService,
                                    List<PostViewDTO> postViews, CountDownLatch latch) {
    Runnable postGradeDecoratorRunnable = new PostGradeDecoratorRunnable(gradeCodes, postViews, latch);
    executorService.submit(postGradeDecoratorRunnable);
  }

  private void decorateSiteOnPost(Set<String> siteCodes, ExecutorService executorService,
                                  List<PostViewDTO> postViews, CountDownLatch latch) {
    Runnable postSiteDecoratorRunnable = new PostSiteDecoratorRunnable(siteCodes, postViews, latch);
    executorService.submit(postSiteDecoratorRunnable);
  }






  class PostGradeDecoratorRunnable implements Runnable {
    private Set<String> gradeCodes;
    private List<PostViewDTO> postViews;
    private CountDownLatch latch;

    public PostGradeDecoratorRunnable(Set<String> gradeCodes, List<PostViewDTO> postViews, CountDownLatch latch) {
      this.gradeCodes = gradeCodes;
      this.postViews = postViews;
      this.latch = latch;
    }

    @Override
    public void run() {
      try {
        List<GradeDTO> grades = referenceService.findGradesIn(gradeCodes);
        Map<String, GradeDTO> gradeMap = grades.stream().collect(Collectors.toMap(GradeDTO::getAbbreviation, g -> g));
        for (PostViewDTO postView : postViews) {
          if (postView.getApprovedGradeCode() != null && !postView.getApprovedGradeCode().isEmpty()) {
            if (gradeMap.containsKey(postView.getApprovedGradeCode())) {
              postView.setApprovedGradeName(gradeMap.get(postView.getApprovedGradeCode()).getName());
            }
          }
        }
        latch.countDown();
      } catch (Exception e) {
        log.warn("Reference decorator call to grades failed", e);
        latch.countDown();
      }
    }
  }

  class PostSiteDecoratorRunnable implements Runnable {

    private Set<String> siteCodes;
    private List<PostViewDTO> postViews;
    private CountDownLatch latch;

    public PostSiteDecoratorRunnable(Set<String> siteCodes, List<PostViewDTO> postViews, CountDownLatch latch) {
      this.siteCodes = siteCodes;
      this.postViews = postViews;
      this.latch = latch;
    }

    @Override
    public void run() {
      try {
        List<SiteDTO> sites = referenceService.findSitesIn(siteCodes);
        Map<String, SiteDTO> siteMap = sites.stream().collect(Collectors.toMap(SiteDTO::getSiteCode, s -> s));

        for (PostViewDTO postView : postViews) {
          if (postView.getPrimarySiteCode() != null && !postView.getPrimarySiteCode().isEmpty()) {
            if (siteMap.containsKey(postView.getPrimarySiteCode())) {
              postView.setPrimarySiteName(siteMap.get(postView.getPrimarySiteCode()).getSiteName());
            }
          }
        }
        latch.countDown();
      } catch (Exception e) {
        e.printStackTrace();
        latch.countDown();
      }
    }
  }
}
