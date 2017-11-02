package com.transformuk.hee.tis.tcs.service.runnable;

import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public abstract class PostDecoratorRunnable implements Runnable {
  private Set<String> codes;
  private List<PostViewDTO> postViews;
  private CountDownLatch latch;

  public PostDecoratorRunnable(Set<String> codes, List<PostViewDTO> postViews, CountDownLatch latch) {
    this.codes = codes;
    this.postViews = postViews;
    this.latch = latch;
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
}