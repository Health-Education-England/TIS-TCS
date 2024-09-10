package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.TraineeUpdate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class GmcDetailsProvidedEvent {

  @NotNull(groups = TraineeUpdate.class)
  private final Long personId;

  @NotNull(groups = TraineeUpdate.class)
  @Valid
  private final GmcDetailsDTO gmcDetails;

  public GmcDetailsProvidedEvent(Long personId, GmcDetailsDTO gmcDetails) {
    this.personId = personId;
    this.gmcDetails = gmcDetails;
  }

  public Long getPersonId() {
    return personId;
  }

  public GmcDetailsDTO getGmcDetails() {
    return gmcDetails;
  }
}
