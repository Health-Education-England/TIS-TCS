package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
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
    Date signedDate = Date.from(signedAt); //TODO: Date or LocalDate?
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    return "Signed "
        + version.toString()
        + " "
        + dateFormat.format(signedDate);
  }
}
