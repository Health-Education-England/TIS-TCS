package com.transformuk.hee.tis.tcs.service.model;

import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import lombok.Data;
import org.hibernate.annotations.Type;

/**
 * A persistable representation of a Conditions of Joining.
 */
@Data
@Entity
public class ConditionsOfJoining implements Serializable {

  @Id
  @Type(type = "org.hibernate.type.UUIDCharType")
  private UUID programmeMembershipUuid;
  private Instant signedAt;
  @Enumerated(EnumType.STRING)
  private GoldGuideVersion version;

  @OneToOne
  @MapsId
  @JoinColumn(name = "programmeMembershipUuid")
  private ProgrammeMembership programmeMembership;
}
