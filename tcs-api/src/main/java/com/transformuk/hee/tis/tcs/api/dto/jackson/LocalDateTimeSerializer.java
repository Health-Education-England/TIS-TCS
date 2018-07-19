package com.transformuk.hee.tis.tcs.api.dto.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
    @Override
    public void serialize(final LocalDateTime dateTime, final JsonGenerator generator, final SerializerProvider provider) throws IOException {
        generator.writeString(String.valueOf(dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()));
    }
}
