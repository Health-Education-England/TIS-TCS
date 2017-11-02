package com.transformuk.hee.tis.tcs.service.runnable;

import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
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

public class PostSiteDecoratorRunnable extends PostDecoratorRunnable {

  private static final Logger LOG = LoggerFactory.getLogger(PostSiteDecoratorRunnable.class);

  public PostSiteDecoratorRunnable(Set<String> codes, List<PostViewDTO> postViews, CountDownLatch latch,
                                   ReferenceService referenceService) {
    super(codes, postViews, latch, referenceService);
  }

  @Override
  public void run() {
    try {
      List<SiteDTO> sites = getReferenceService().findSitesIn(getCodes());
      Map<String, SiteDTO> siteMap = sites.stream().collect(Collectors.toMap(SiteDTO::getSiteCode, s -> s));

      for (PostViewDTO postView : getPostViews()) {
        if (StringUtils.isNotBlank(postView.getPrimarySiteCode()) && siteMap.containsKey(postView.getPrimarySiteCode())) {
          postView.setPrimarySiteName(siteMap.get(postView.getPrimarySiteCode()).getSiteName());
        }
      }
    } catch (Exception e) {
      LOG.warn("Reference decorator call to sites failed", e);
    }
    getLatch().countDown();
  }
}
