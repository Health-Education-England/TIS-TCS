package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Type;

/**
 * A persistable representation of a Conditions of Joining.
 */
@Data
@Entity
public class ConditionsOfJoining {

  @Id
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID programmeMembershipUuid;
  private Instant signedAt;
  @Enumerated(EnumType.STRING)
  private GoldGuideVersion version;
}
