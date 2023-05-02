package com.transformuk.hee.tis.tcs.service.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;

/**
 * An event to be received when a Conditions of Joining is signed.
 */
public class ConditionsOfJoiningSignedEvent {

  @JsonProperty("programmeMembershipTisId")
  private final Long programmeMembershipId;
  private final ConditionsOfJoiningDto conditionsOfJoining;

  public ConditionsOfJoiningSignedEvent(Long programmeMembershipId,
      ConditionsOfJoiningDto conditionsOfJoining) {
    this.programmeMembershipId = programmeMembershipId;
    this.conditionsOfJoining = conditionsOfJoining;
  }

  public Long getProgrammeMembershipId() {
    return programmeMembershipId;
  }

  public ConditionsOfJoiningDto getConditionsOfJoining() {
    return conditionsOfJoining;
  }
}
