package com.transformuk.hee.tis.tcs.service.api.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * FIXME : This is a workaround as could not get Spring boot to decode url encoded values to PathVariables
 *
 * See
 *  https://stackoverflow.com/questions/24054648/how-to-configure-characterencodingfilter-in-springboot
 *  https://github.com/spring-projects/spring-boot/issues/540
 *  https://github.com/spring-projects/spring-boot/issues/1182
 */
public class UrlDecoderUtil {
	public static String decode(String encodedString) {
		try {
			return java.net.URLDecoder.decode(encodedString, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static void decode(List<String> urlEncodedStrings) {
		urlEncodedStrings.replaceAll(UrlDecoderUtil::decode);
	}
}
