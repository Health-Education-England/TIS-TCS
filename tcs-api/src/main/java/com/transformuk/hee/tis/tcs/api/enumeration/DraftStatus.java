package com.transformuk.hee.tis.tcs.api.enumeration;

public enum DraftStatus {
  DRAFT("draft"),
  APPROVED("approved");

  private final String draftStatus;

  DraftStatus(String draftStatus) {
    this.draftStatus = draftStatus;
  }

  public String getDraftStatus() {
    return this.draftStatus;
  }

  public String toString() {
    return this.draftStatus;
  }
}
