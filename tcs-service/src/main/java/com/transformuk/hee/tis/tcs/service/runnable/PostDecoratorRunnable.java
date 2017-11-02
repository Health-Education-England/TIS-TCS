package com.transformuk.hee.tis.tcs.service.runnable;

import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public abstract class PostDecoratorRunnable implements Runnable {
  private Set<String> codes;
  private List<PostViewDTO> postViews;
  private CountDownLatch latch;
  private ReferenceService referenceService;

  public PostDecoratorRunnable(Set<String> codes, List<PostViewDTO> postViews, CountDownLatch latch,
                               ReferenceService referenceService) {
    this.codes = codes;
    this.postViews = postViews;
    this.latch = latch;
    this.referenceService = referenceService;
  }

  protected Set<String> getCodes() {
    return codes;
  }

  protected List<PostViewDTO> getPostViews() {
    return postViews;
  }

  protected CountDownLatch getLatch() {
    return latch;
  }

  protected ReferenceService getReferenceService() {
    return referenceService;
  }
}