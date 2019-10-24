package com.transformuk.hee.tis.tcs.api.enumeration;

public enum LifecycleState {
  DRAFT("draft"),
  APPROVED("approved");

  private final String lifecycleState;

  LifecycleState(String lifecycleState) {
    this.lifecycleState = lifecycleState;
  }

  public String getLifecycleState() {
    return this.lifecycleState;
  }

  public String toString() {
    return this.lifecycleState;
  }
}
