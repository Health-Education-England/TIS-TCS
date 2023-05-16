package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;
import lombok.Data;

/**
 * A DTO representation of a Conditions of Joining.
 */
@Data
public class ConditionsOfJoiningDto implements Serializable {

  private UUID programmeMembershipUuid;
  private Instant signedAt;
  private GoldGuideVersion version;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConditionsOfJoiningDto that = (ConditionsOfJoiningDto) o;
    return Objects.equals(programmeMembershipUuid, that.getProgrammeMembershipUuid())
        && Objects.equals(signedAt, that.getSignedAt())
        && Objects.equals(version, that.getVersion());
  }

  @Override
  public int hashCode() {
    return Objects.hash(programmeMembershipUuid, signedAt, version);
  }
}
