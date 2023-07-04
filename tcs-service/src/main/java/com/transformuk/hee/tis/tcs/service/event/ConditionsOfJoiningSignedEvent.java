package com.transformuk.hee.tis.tcs.service.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;

/**
 * An event to be received when a Conditions of Joining is signed.
 */
public class ConditionsOfJoiningSignedEvent {

  @JsonProperty("programmeMembershipTisId")
  private final Object id; //this may be a long or a UUID
  private final ConditionsOfJoiningDto conditionsOfJoining;

  public ConditionsOfJoiningSignedEvent(Object id,
      ConditionsOfJoiningDto conditionsOfJoining) {
    this.id = id;
    this.conditionsOfJoining = conditionsOfJoining;
  }

  public Object getId() {
    return id;
  }

  public ConditionsOfJoiningDto getConditionsOfJoining() {
    return conditionsOfJoining;
  }
}
