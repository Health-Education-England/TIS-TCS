package com.transformuk.hee.tis.tcs.api.enumeration;

/**
 * Represents any date/datetime columns used within TCS. This is used to filter results based on a given date range.
 * Any date columns needed to searched on date range and using column filter and ColumnFilterUtil should added here.
 */
public enum TCSDateColumns {

  // Placement entity date columns
  dateFrom,
  dateTo;

  public static boolean contains(String dateColumn) {

    for (TCSDateColumns c : TCSDateColumns.values()) {
      if (c.name().equals(dateColumn)) {
        return true;
      }
    }
    return false;
  }

}
