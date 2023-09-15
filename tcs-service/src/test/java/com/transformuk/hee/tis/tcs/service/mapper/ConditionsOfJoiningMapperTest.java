package com.transformuk.hee.tis.tcs.service.mapper;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import java.io.IOException;
import java.time.Instant;
import java.util.UUID;
import org.junit.Test;

public class ConditionsOfJoiningMapperTest {

  @Test
  public void shouldIgnoreUnknownPropertiesWhenDeserializing() throws JsonProcessingException {

    UUID uuid = UUID.randomUUID();
    Instant signedAt = Instant.now();
    GoldGuideVersion goldGuideVersion = GoldGuideVersion.GG9;

    String jsonAsString =
        "{\"programmeMembershipUuid\":\"" + uuid.toString() + "\"," +
        "\"signedAt\":\"" + signedAt.toString() + "\"," +
        "\"version\":\"" + goldGuideVersion + "\"," +
        "\"unknownField\":\"something\"}";
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    ConditionsOfJoiningDto readValue = mapper
        .readValue(jsonAsString, ConditionsOfJoiningDto.class);

    assertNotNull(readValue);
    assertThat(readValue.getProgrammeMembershipUuid()).isEqualTo(uuid);
    assertThat(readValue.getSignedAt()).isEqualTo(signedAt);
    assertThat(readValue.getVersion()).isEqualTo(goldGuideVersion);
  }
}
