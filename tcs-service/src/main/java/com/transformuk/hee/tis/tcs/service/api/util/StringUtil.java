package com.transformuk.hee.tis.tcs.service.api.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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
    try {
      /**
       * If FE send the parameter string without encoded string then some characters are escaped
       * e.g "OXF/RTH02/034/PSTR3+" will be converted into "OXF/RTH02/034/PSTR3 "
       * Its must be encoded at FE and decoded at BE to allow special characters
       */
      str = URLDecoder.decode(str, "UTF-8");
    }
    catch (UnsupportedEncodingException | IllegalArgumentException e){
      // if exception then do nothing
    }
    return str.replaceAll("[^a-zA-Z0-9\\s\\,\\&\\'\\-\\+\\/\\)\\(]", "").trim();
  }
}
