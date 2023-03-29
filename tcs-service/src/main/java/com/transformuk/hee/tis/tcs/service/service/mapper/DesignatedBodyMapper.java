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

  static {
    String activeProfiles = System.getenv("SPRING_PROFILES_ACTIVE");
    USE_TEMP_MAPPER = activeProfiles != null && activeProfiles.contains("stage");
  }

  private static final boolean USE_TEMP_MAPPER;

  /**
   * Maps between a designated body code and the owners that are considered matches to that code,
   * please note that London LETBs is considered a match for all london DBC's
   */
  private static final Map<String, List<String>> dbToOwnerMap = ImmutableMap.<String, List<String>>builder()
      //London LETBs match any of the london DBC's
      .put("1-AIIDR8",
          Lists.newArrayList("Health Education England Kent, Surrey and Sussex", "London LETBs"))
      .put("1-AIIDWA",
          Lists.newArrayList("Health Education England North West London", "London LETBs"))
      .put("1-AIIDVS", Lists
          .newArrayList("Health Education England North Central and East London", "London LETBs"))
      .put("1-AIIDWI", Lists.newArrayList("Health Education England South London", "London LETBs"))

      .put("1-AIIDSA", Lists.newArrayList("Health Education England East Midlands"))
      .put("1-AIIDWT", Lists.newArrayList("Health Education England East of England"))
      .put("1-AIIDSI", Lists.newArrayList("Health Education England North East"))
      .put("1-AIIDH1", Lists.newArrayList("Health Education England Thames Valley"))
      .put("1-1P9Y9QH", Lists.newArrayList("Health Education England Yorkshire and the Humber"))
      .put("1-AIIDMY", Lists.newArrayList("Health Education England West Midlands"))
      .put("1-AIIDMQ", Lists.newArrayList("Health Education England South West"))
      .put("1-AIIDHJ", Lists.newArrayList("Health Education England Wessex"))
      .put("1-1P9Y9R1", Lists.newArrayList("Health Education England North West"))
      .put("1-25U-830", Lists.newArrayList("Northern Ireland Medical and Dental Training Agency"))
      .build();

  /**
   * Maps between a designated body code and the owners that are considered matches to that code,
   * please note that London LETBs is considered a match for all london DBC's
   */
  private static final Map<String, List<String>> heeDbToOwnerMap = ImmutableMap.<String, List<String>>builder()
      //London LETBs match any of the london DBC's
      .put("1-1RUZV1D",
          Lists.newArrayList("Health Education England Kent, Surrey and Sussex", "London LETBs"))
      .put("1-1RUZV6H",
          Lists.newArrayList("Health Education England North West London", "London LETBs"))
      .put("1-1RUZV4H", Lists
          .newArrayList("Health Education England North Central and East London", "London LETBs"))
      .put("1-1RSSQ5L", Lists.newArrayList("Health Education England South London", "London LETBs"))

      .put("1-1RSSPZ7", Lists.newArrayList("Health Education England East Midlands"))
      .put("1-1RSSQ05", Lists.newArrayList("Health Education England East of England"))
      .put("1-1RSSQ1B", Lists.newArrayList("Health Education England North East"))
      .put("1-1RSSQ6R", Lists.newArrayList("Health Education England Thames Valley"))
      .put("1-1RSG4X0", Lists.newArrayList("Health Education England Yorkshire and the Humber"))
      .put("1-1RUZUYF", Lists.newArrayList("Health Education England West Midlands"))
      .put("1-1RUZUVB", Lists.newArrayList("Health Education England South West"))
      .put("1-1RUZUSF", Lists.newArrayList("Health Education England Wessex"))
      .put("1-1RSSQ2H", Lists.newArrayList("Health Education England North West"))
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
    Map<String, List<String>> mapToUse = USE_TEMP_MAPPER ? dbToOwnerMap : heeDbToOwnerMap;
    dbcs.forEach(dbc -> {
      if (mapToUse.containsKey(dbc)) {
        owners.addAll(mapToUse.get(dbc));
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
      if (USE_TEMP_MAPPER) {
        dbToOwnerMap.values().forEach(v -> allOwners.addAll(v));
      } else {
        heeDbToOwnerMap.values().forEach(v -> allOwners.addAll(v));
      }
    }
    return allOwners;
  }

  /**
   * @param owner name
   * @return the corresponding designated body code
   */
  public static String getDbcByOwner(String owner) {
    Map<String, List<String>> mapToUse = USE_TEMP_MAPPER ? dbToOwnerMap : heeDbToOwnerMap;
    for (Entry<String, List<String>> entry : mapToUse.entrySet()) {
      if (entry.getValue().contains(owner)) {
        return entry.getKey();
      }
    }
    return null;
  }

}
