package com.transformuk.hee.tis.tcs.service.api.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.transformuk.hee.tis.tcs.service.api.util.StringUtil.sanitize;
import static java.util.Collections.EMPTY_LIST;
import static java.util.stream.Collectors.toList;

/**
 * Utility class for Column filters
 */
public final class ColumnFilterUtil {

	private static final Logger log = LoggerFactory.getLogger(ColumnFilterUtil.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	public ColumnFilterUtil() {
	}

	/**
	 * Parse json string to column filter list
	 *
	 * @param columnFilterJson
	 * @return
	 * @throws IOException
	 */
	public static List<ColumnFilter> getColumnFilters(String columnFilterJson, List<Class> enumList) throws IOException {
		if (columnFilterJson != null) {
			if (!columnFilterJson.startsWith("{")) {
				//attempt to decode
				try {
					columnFilterJson = new URLCodec().decode(columnFilterJson);
				} catch (DecoderException e) {
					log.error(e.getMessage(), e);
					throw new IllegalArgumentException("Cannot interpret column filters: " + columnFilterJson);
				}
			}
			TypeReference<HashMap<String, List<String>>> typeRef = new TypeReference<HashMap<String, List<String>>>() {
			};

			try {
				Map<String, List<String>> columns = mapper.readValue(columnFilterJson, typeRef);
				List<ColumnFilter> cfList = new ArrayList<>(columns.size());

				for (Map.Entry<String, List<String>> e : columns.entrySet()) {
					// Find the enum type from given list
					Optional<Class> selectedEnumClass = enumList.stream()
							.filter(en -> e.getKey().equalsIgnoreCase(en.getSimpleName())).findFirst();
					if (selectedEnumClass.isPresent()) {
						addToCfList(cfList, e, selectedEnumClass.get());
					} else {
						List<Object> values = e.getValue().stream().map(v -> sanitize(v)).collect(toList());
						cfList.add(new ColumnFilter(e.getKey(), values));
					}
				}
				return cfList;
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				throw new IllegalArgumentException("Cannot interpret column filters: " + columnFilterJson);
			}
		}
		return EMPTY_LIST;
	}

	private static void addToCfList(List<ColumnFilter> cfList, Map.Entry<String, List<String>> e, Class en) {
		List<Object> values = new ArrayList<>();
		e.getValue().forEach(v -> {
			if (EnumUtils.isValidEnum(en, String.valueOf(v))) {
				values.add(Enum.valueOf(en, String.valueOf(v)));
			}
		});

		if (!values.isEmpty()) {
			cfList.add(new ColumnFilter(e.getKey(), values));
		}
	}

}
