package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
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
  public static final int EXECUTOR_THREAD_POOL_SIZE = 3;

  private ReferenceService referenceService;

  @Autowired
  public PostViewDecorator(ReferenceService referenceService) {
    this.referenceService = referenceService;
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
      if (postView.getApprovedGradeCode() != null && !postView.getApprovedGradeCode().isEmpty()) {
        gradeCodes.add(postView.getApprovedGradeCode());
      }
      if (postView.getPrimarySiteCode() != null && !postView.getPrimarySiteCode().isEmpty()) {
        siteCodes.add(postView.getPrimarySiteCode());
      }
    });

    ExecutorService executorService = Executors.newFixedThreadPool(EXECUTOR_THREAD_POOL_SIZE);

    Future<Map<String, SiteDTO>> sitesFuture = getSitesFuture(siteCodes, executorService);
    Future<Map<String, GradeDTO>> gradesFuture = getGradesFuture(gradeCodes, executorService);

    decorateGradesOnPost(gradesFuture, executorService, postViews);
    decorateSiteOnPost(sitesFuture, executorService, postViews);

    try {
      executorService.awaitTermination(5L, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      log.error("Exception occurred while waiting for executor service to shutdown {}", e);
    }
  }

  private Future<Map<String, GradeDTO>> getGradesFuture(Set<String> gradeCodes, ExecutorService executorService) {
    return executorService.submit(() -> {
        List<GradeDTO> grades = Lists.newArrayList();
        try {
          referenceService.findGradesIn(gradeCodes);
        } catch (Exception e) {
          log.warn("Reference decorator call to grades failed", e);
        }
        return grades.stream().collect(Collectors.toMap(GradeDTO::getAbbreviation, g -> g));
      });
  }

  private Future<Map<String, SiteDTO>> getSitesFuture(Set<String> siteCodes, ExecutorService executorService) {
    return executorService.submit(() -> {
        List<SiteDTO> sites = Lists.newArrayList();
        try {
          sites = referenceService.findSitesIn(siteCodes);
        } catch (Exception e) {
          log.warn("Reference decorator call to sites failed", e);
        }
        return sites.stream().collect(Collectors.toMap(SiteDTO::getSiteCode, s -> s));
      });
  }

  private void decorateGradesOnPost(Future<Map<String, GradeDTO>> gradesFuture, ExecutorService executorService,
                                    List<PostViewDTO> postViews) {
    Runnable postGradeDecoratorRunnable = new PostGradeDecoratorRunnable(gradesFuture, postViews);
    executorService.submit(postGradeDecoratorRunnable);
  }

  private void decorateSiteOnPost(Future<Map<String, SiteDTO>> sitesFuture, ExecutorService executorService,
                                  List<PostViewDTO> postViews) {
    Runnable postSiteDecoratorRunnable = new PostSiteDecoratorRunnable(sitesFuture, postViews);
    executorService.submit(postSiteDecoratorRunnable);
  }

  class PostGradeDecoratorRunnable implements Runnable {
    private Future<Map<String, GradeDTO>> gradesFuture;
    private List<PostViewDTO> postViews;

    public PostGradeDecoratorRunnable(Future<Map<String, GradeDTO>> gradesFuture, List<PostViewDTO> postViews) {
      this.gradesFuture = gradesFuture;
      this.postViews = postViews;
    }

    @Override
    public void run() {
      try {
        Map<String, GradeDTO> gradeMap = gradesFuture.get();
        for (PostViewDTO postView : postViews) {
          if (postView.getApprovedGradeCode() != null && !postView.getApprovedGradeCode().isEmpty()) {
            if (gradeMap.containsKey(postView.getApprovedGradeCode())) {
              postView.setApprovedGradeName(gradeMap.get(postView.getApprovedGradeCode()).getName());
            }
          }
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }

    }
  }

  class PostSiteDecoratorRunnable implements Runnable {

    private Future<Map<String, SiteDTO>> sitesFuture;
    private List<PostViewDTO> postViews;

    public PostSiteDecoratorRunnable(Future<Map<String, SiteDTO>> sitesFuture, List<PostViewDTO> postViews) {
      this.sitesFuture = sitesFuture;
      this.postViews = postViews;
    }

    @Override
    public void run() {
      try {
        Map<String, SiteDTO> siteMap = sitesFuture.get();
        for (PostViewDTO postView : postViews) {
          if (postView.getPrimarySiteCode() != null && !postView.getPrimarySiteCode().isEmpty()) {
            if (siteMap.containsKey(postView.getPrimarySiteCode())) {
              postView.setPrimarySiteName(siteMap.get(postView.getPrimarySiteCode()).getSiteName());
            }
          }
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
  }
}
