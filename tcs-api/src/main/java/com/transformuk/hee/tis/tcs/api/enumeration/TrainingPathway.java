package com.transformuk.hee.tis.tcs.api.enumeration;

/**
 * TrainingPathway is not in Reference service.
 * And this enumeration is added for validation purpose.
 */
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

  /**
   * Map a string text to the enumeration.
   *
   * @param text the string text to map
   * @return the enum value, if not found, return null
   */
  public static TrainingPathway fromString(String text) {
    for (TrainingPathway trainingPathway : TrainingPathway.values()) {
      if (trainingPathway.text.equalsIgnoreCase(text)) {
        return trainingPathway;
      }
    }
    return null;
  }
}
