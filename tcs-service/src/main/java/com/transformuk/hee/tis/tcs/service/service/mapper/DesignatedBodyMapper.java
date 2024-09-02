package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * Maps between a user's designated body code and owner
 */
public class DesignatedBodyMapper {

  /**
   * Maps between a designated body code and the owners that are considered matches to that code,
   * please note that London LETBs is considered a match for all london DBC's
   */
  private static final Map<String, List<String>> heeDbToOwnerMap = ImmutableMap.<String, List<String>>builder()
      //London LETBs match any of the london DBC's
      .put("1-1RUZV1D",
          Lists.newArrayList("Kent, Surrey and Sussex", "London LETBs"))
      .put("1-1RUZV6H",
          Lists.newArrayList("North West London", "London LETBs"))
      .put("1-1RUZV4H", Lists
          .newArrayList("North Central and East London", "London LETBs"))
      .put("1-1RSSQ5L", Lists.newArrayList("South London", "London LETBs"))

      .put("1-1RSSPZ7", Lists.newArrayList("East Midlands"))
      .put("1-1RSSQ05", Lists.newArrayList("East of England"))
      .put("1-1RSSQ1B", Lists.newArrayList("North East"))
      .put("1-1RSSQ6R", Lists.newArrayList("Thames Valley"))
      .put("1-1RSG4X0", Lists.newArrayList("Yorkshire and the Humber"))
      .put("1-1RUZUYF", Lists.newArrayList("West Midlands"))
      .put("1-1RUZUVB", Lists.newArrayList("South West"))
      .put("1-1RUZUSF", Lists.newArrayList("Wessex"))
      .put("1-1RSSQ2H", Lists.newArrayList("North West"))
      .put("1-25U-830", Lists.newArrayList("Northern Ireland Medical and Dental Training Agency"))
      .build();

  private static Set<String> allOwners = null;

  /**
   * @param dbcs a list of designated body codes not null
   * @return the list of owners that match the given list of codes
   */
  public static Set<String> map(Set<String> dbcs) {
    Preconditions.checkNotNull(dbcs);
    Set<String> owners = new HashSet<>();
    dbcs.forEach(dbc -> {
      if (heeDbToOwnerMap.containsKey(dbc)) {
        owners.addAll(heeDbToOwnerMap.get(dbc));
      }
    });
    return owners;
  }

  /**
   * @return all currently known owners
   */
  public static Set<String> getAllOwners() {
    if (allOwners == null) {
      allOwners = new HashSet<>();
      heeDbToOwnerMap.values().forEach(v -> allOwners.addAll(v));
    }
    return allOwners;
  }

  /**
   * @param owner name
   * @return the corresponding designated body code
   */
  public static String getDbcByOwner(String owner) {
    for (Entry<String, List<String>> entry : heeDbToOwnerMap.entrySet()) {
      if (entry.getValue().contains(owner)) {
        return entry.getKey();
      }
    }
    return null;
  }
}
