package com.transformuk.hee.tis.tcs.api.enumeration;

public enum TrainingPathway {
  CCT("CCT"),
  CESR("CESR"),
  NA("N/A");

  private final String text;

  TrainingPathway(final String s) {
    this.text = s;
  }

  @Override
  public String toString() {
    return text;
  }

  public static TrainingPathway fromString(String text) {
    for (TrainingPathway trainingPathway : TrainingPathway.values()) {
      if (trainingPathway.text.equalsIgnoreCase(text)) {
        return trainingPathway;
      }
    }
    return null;
  }
}
