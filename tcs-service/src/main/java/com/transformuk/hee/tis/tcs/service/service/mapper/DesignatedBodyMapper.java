package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Maps between a user's designated body code and managing deaneries
 */
public class DesignatedBodyMapper {

	/**
	 * Maps between a designated body code and the managing deaneries that are considered matches to that code,
	 * please note that London LETBs is considered a match for all london DBC's
	 */
	private static final Map<String, List<String>> dbcToMangingDeaneryMap = ImmutableMap.<String, List<String>>builder()
			//London LETBs match any of the london DBC's
			.put("1-AIIDR8", Lists.newArrayList("Health Education England Kent, Surrey and Sussex", "London LETBs"))
			.put("1-AIIDWA", Lists.newArrayList("Health Education England North West London", "London LETBs"))
			.put("1-AIIDVS", Lists.newArrayList("Health Education England North Central and East London", "London LETBs"))
			.put("1-AIIDWI", Lists.newArrayList("Health Education England South London", "London LETBs"))

			.put("1-AIIDSA", Lists.newArrayList("Health Education England East Midlands"))
			.put("1-AIIDWT", Lists.newArrayList("Health Education England East of England"))
			.put("1-AIIDSI", Lists.newArrayList("Health Education England North East"))
			.put("1-AIIDH1", Lists.newArrayList("Health Education England Thames Valley"))
			.put("1-AIIDQQ", Lists.newArrayList("Health Education England Yorkshire and the Humber"))
			.put("1-AIIDMY", Lists.newArrayList("Health Education England West Midlands"))
			.build();

	/**
	 * @param dbcs a list of designated body codes not null
	 * @return the list of managing deaneries that match the given list of codes
	 */
	public static Set<String> map(Set<String> dbcs) {
		Preconditions.checkNotNull(dbcs);
		Set<String> deaneries = new HashSet<>();
		dbcs.forEach(dbc -> {
			if (dbcToMangingDeaneryMap.containsKey(dbc)) {
				deaneries.addAll(dbcToMangingDeaneryMap.get(dbc));
			}
		});
		return deaneries;
	}
}
