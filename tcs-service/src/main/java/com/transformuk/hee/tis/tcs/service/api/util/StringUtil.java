package com.transformuk.hee.tis.tcs.service.api.util;

import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for string operations
 */
public final class StringUtil {
  private static String SANITIZER_REGEX = "[^a-zA-Z0-9\\s,/\\-]";
  private static String sanitiserRegex;

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

    return str.replaceAll(sanitiserRegex, "").trim();
  }

  public static void setSanitiserRegex(String regex) {
    sanitiserRegex = StringUtils.isEmpty(regex) ? SANITIZER_REGEX : regex;
  }
}
