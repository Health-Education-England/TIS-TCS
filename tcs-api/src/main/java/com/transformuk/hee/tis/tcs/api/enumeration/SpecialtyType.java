package com.transformuk.hee.tis.tcs.api.enumeration;

/**
 * The SpecialtyType enumeration.
 */
public enum SpecialtyType {

  //Remember to modify the specialtyType column in the SpecialtyTypes table if you change this Enum
  //CURRICULUM, POST, PLACEMENT, SUB_SPECIALTY

  CURRICULUM("CURRICULUM"),
  POST("POST"),
  PLACEMENT("PLACEMENT"),
  SUB_SPECIALTY("SUB_SPECIALTY");

  private final String name;

  SpecialtyType(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
