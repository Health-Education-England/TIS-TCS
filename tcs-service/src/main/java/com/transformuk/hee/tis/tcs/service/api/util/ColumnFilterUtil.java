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
 * JPA is expecting Enum type if the entity column is Enum, this class checks for filter then convert string to Enum if
 * the column is type of Enum
 */
public final class ColumnFilterUtil {

	private static final Logger log = LoggerFactory.getLogger(ColumnFilterUtil.class);

	private static final ObjectMapper mapper = new ObjectMapper();

	public ColumnFilterUtil() {
	}

	/**
	 * Parse json string to column filter list and checks for filter column if enum then converts string to Enum
	 *
	 * @param columnFilterJson json string to parse
	 * @param enumList list of enums as column filter for the entity
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

					// if columnFilter is enum and value is not null
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

	/**
	 * Checks for column filter is valid enum then add into columnFilter list with Enum object
	 * @param cfList
	 * @param e
	 * @param en
	 */
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
