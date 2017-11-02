package com.transformuk.hee.tis.tcs.service.runnable;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

public class PostGradeDecoratorRunnable extends PostDecoratorRunnable {

  private static final Logger LOG = LoggerFactory.getLogger(PostGradeDecoratorRunnable.class);

  public PostGradeDecoratorRunnable(Set<String> codes, List<PostViewDTO> postViews, CountDownLatch latch, ReferenceService referenceService) {
    super(codes, postViews, latch, referenceService);
  }

  @Override
  public void run() {
    try {
      List<GradeDTO> grades = getReferenceService().findGradesIn(getCodes());
      Map<String, GradeDTO> gradeMap = grades.stream().collect(Collectors.toMap(GradeDTO::getAbbreviation, g -> g));
      for (PostViewDTO postView : getPostViews()) {
        if (StringUtils.isNotBlank(postView.getApprovedGradeCode()) && gradeMap.containsKey(postView.getApprovedGradeCode())) {
          postView.setApprovedGradeName(gradeMap.get(postView.getApprovedGradeCode()).getName());
        }
      }
    } catch (Exception e) {
      LOG.warn("Reference decorator call to grades failed", e);
    }
    getLatch().countDown();
  }
}
