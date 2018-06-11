package com.transformuk.hee.tis.tcs.service.api.util;

/**
 * Utility class for string operations
 */
public final class StringUtil {

  /**
   * Removes any non alphanumeric characters from the given string, except for whitespace and commas.
   * Please note that this method also trims whitespace from the beginning and end of the string.
   *
   * @param str the string to process
   * @return the given string with all non alphanumeric characters removed and trimmed
   */
  public static String sanitize(String str) {
    if (str == null) {
      return null;
    }
    return str.replaceAll("[^a-zA-Z0-9\\s\\,\\&\\'\\+\\/\\)\\(]", "").trim();
  }
}
