package com.transformuk.hee.tis.tcs.api.dto.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(parser.getText())),
                TimeZone.getDefault().toZoneId());
    }
}
