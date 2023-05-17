package com.transformuk.hee.tis.tcs.api.dto;

import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import java.io.Serializable;
import java.time.Instant;
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
}
