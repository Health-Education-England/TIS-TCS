package com.transformuk.hee.tis.tcs.service.api.util;

import com.transformuk.hee.tis.tcs.service.exception.InvalidDateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A Date utility class for any date conversions and parsing.
 */
public class DateUtil {

  private static final String DATE_FORMAT = "yyyy-MM-dd";

  private DateUtil() {
  }

  /**
   * @param dateStr a string representation of date to be parsed as java.util.LocalDate
   * @return
   */
  public static LocalDate getLocalDateFromString(final String dateStr) {

    try {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
      return LocalDate.parse(dateStr, formatter);
    } catch (DateTimeParseException e) {
      throw new InvalidDateException("Exception parsing date string " + dateStr);
    }
  }

}
