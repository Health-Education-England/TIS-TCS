package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.Data;

/**
 * A DTO representation of a Conditions of Joining.
 */
@Data
public class ConditionsOfJoiningDto {

  private UUID programmeMembershipUuid;
  private Instant signedAt;
  private GoldGuideVersion version;

  @Override
  public String toString() {
    if (programmeMembershipUuid != null) {
      LocalDate signedDate = signedAt.atZone(ZoneOffset.UTC).toLocalDate();
      return "Signed "
          + version.toString()
          + " "
          + signedDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    } else {
      return "Not signed through TIS Self-Service";
    }
  }
}
