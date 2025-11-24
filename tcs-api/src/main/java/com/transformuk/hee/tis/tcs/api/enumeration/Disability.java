package com.transformuk.hee.tis.tcs.api.enumeration;

public enum Disability {
  YES,
  NO;

  public static String normaliseOrKeepOriginal(String value) {
    if (value == null)
      return null;

    try {
      return Disability.valueOf(value.trim().toUpperCase()).name();
    } catch (IllegalArgumentException e) { // There are legacy non-enumeration values in the DB.
      return value;
    }
  }
}
