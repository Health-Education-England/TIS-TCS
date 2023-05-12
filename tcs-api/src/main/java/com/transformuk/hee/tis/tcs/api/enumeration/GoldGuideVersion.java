package com.transformuk.hee.tis.tcs.api.enumeration;

/**
 * An enumeration of supported Gold Guide versions.
 */
public enum GoldGuideVersion {
  GG9("Gold Guide 9");
  private final String description;

  private GoldGuideVersion(String description) {
    this.description = description;
  }

  @Override
  public String toString() {
    return description;
  }
}
