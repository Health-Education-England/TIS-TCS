package com.transformuk.hee.tis.tcs.api.enumeration;

public enum PlacementLogType {
  CREATE("create"),
  UPDATE("update"),
  DELETE("delete");

  private final String placementLogType;

  PlacementLogType(String placementLogType) {
    this.placementLogType = placementLogType;
  }

  public String getPlacementLogType() {
    return this.placementLogType;
  }

  public String toString() {
    return this.placementLogType;
  }
}
