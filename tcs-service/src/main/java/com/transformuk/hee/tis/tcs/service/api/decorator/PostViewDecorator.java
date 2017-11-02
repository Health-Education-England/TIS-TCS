package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.service.runnable.PostDecoratorRunnable;
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

    PostDecoratorRunnable gradeDecoratorRunnable = new PostDecoratorRunnable(gradeCodes, postViews, latch) {
      @Override
      public void run() {
        try {
          List<GradeDTO> grades = referenceService.findGradesIn(getCodes());
          Map<String, GradeDTO> gradeMap = grades.stream().collect(Collectors.toMap(GradeDTO::getAbbreviation, g -> g));
          for (PostViewDTO postView : getPostViews()) {
            if (StringUtils.isNotBlank(postView.getApprovedGradeCode()) && gradeMap.containsKey(postView.getApprovedGradeCode())) {
              postView.setApprovedGradeName(gradeMap.get(postView.getApprovedGradeCode()).getName());
            }
          }
        } catch (Exception e) {
          log.warn("Reference decorator call to grades failed", e);
        }
        getLatch().countDown();
      }
    };

    executorService.submit(gradeDecoratorRunnable);
  }

  private void decorateSiteOnPost(Set<String> siteCodes, ExecutorService executorService,
                                  List<PostViewDTO> postViews, CountDownLatch latch) {

    PostDecoratorRunnable siteDecoratorRunnable = new PostDecoratorRunnable(siteCodes, postViews, latch) {
      @Override
      public void run() {
        try {
          List<SiteDTO> sites = referenceService.findSitesIn(getCodes());
          Map<String, SiteDTO> siteMap = sites.stream().collect(Collectors.toMap(SiteDTO::getSiteCode, s -> s));

          for (PostViewDTO postView : getPostViews()) {
            if (StringUtils.isNotBlank(postView.getPrimarySiteCode()) && siteMap.containsKey(postView.getPrimarySiteCode())) {
              postView.setPrimarySiteName(siteMap.get(postView.getPrimarySiteCode()).getSiteName());
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        getLatch().countDown();
      }
    };

    executorService.submit(siteDecoratorRunnable);

  }

}
