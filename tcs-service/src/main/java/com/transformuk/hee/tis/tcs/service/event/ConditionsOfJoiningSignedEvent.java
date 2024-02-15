package com.transformuk.hee.tis.tcs.service.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;

/**
 * An event to be received when a Conditions of Joining is signed.
 */
public class ConditionsOfJoiningSignedEvent {

  @JsonProperty("programmeMembershipTisId")
  private final String id;
  private final ConditionsOfJoiningDto conditionsOfJoining;

  public ConditionsOfJoiningSignedEvent(String id,
      ConditionsOfJoiningDto conditionsOfJoining) {
    this.id = id;
    this.conditionsOfJoining = conditionsOfJoining;
  }

  public String getId() {
    return id;
  }

  public ConditionsOfJoiningDto getConditionsOfJoining() {
    return conditionsOfJoining;
  }
}
