package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Maps between a user's designated body code and owner
 */
public class DesignatedBodyMapper {

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
      .put("1-AIIDQQ", Lists.newArrayList("Health Education England Yorkshire and the Humber"))
      .put("1-AIIDMY", Lists.newArrayList("Health Education England West Midlands"))
      .put("1-AIIDMQ", Lists.newArrayList("Health Education England South West"))
      .put("1-AIIDHJ", Lists.newArrayList("Health Education England Wessex"))
      .put("1-AIIDNQ", Lists.newArrayList("Health Education England North West"))
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
      if (dbToOwnerMap.containsKey(dbc)) {
        owners.addAll(dbToOwnerMap.get(dbc));
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
      dbToOwnerMap.values().forEach(v -> allOwners.addAll(v));
    }
    return allOwners;
  }
}
