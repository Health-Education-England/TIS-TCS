package com.transformuk.hee.tis.tcs.api.dto;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConditionsOfJoiningDtoTest {

  private static final UUID PROGRAMME_MEMBERSHIP_UUID = UUID.randomUUID();
  private static final Instant SIGNED_AT = Instant.now();
  private static final GoldGuideVersion VERSION = GoldGuideVersion.GG9;
  private ConditionsOfJoiningDto dto;

  @BeforeEach
  void setUp() {
    dto = new ConditionsOfJoiningDto();
    dto.setProgrammeMembershipUuid(PROGRAMME_MEMBERSHIP_UUID);
    dto.setSignedAt(SIGNED_AT);
    dto.setVersion(VERSION);
  }

  @Test
  void shouldGenerateCorrectTextIfPopulated() {
    String text = dto.toString();
    String expectedText = "Signed "
        + VERSION
        + " "
        + SIGNED_AT.atZone(ZoneOffset.UTC)
        .toLocalDate()
        .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    assertThat("Unexpected DTO text", text, is(expectedText));
  }

  @Test
  void shouldGenerateCorrectTextIfNotPopulated() {
    String text = new ConditionsOfJoiningDto().toString();
    String expectedText = "Not signed through TIS Self-Service";

    assertThat("Unexpected DTO text", text, is(expectedText));
  }
}
