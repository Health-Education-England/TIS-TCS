package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.EmailDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.TraineeUpdate;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * An event triggered when a trainee provides email details.
 */
public class EmailDetailsProvidedEvent {

  @NotNull(groups = TraineeUpdate.class)
  private final Long personId;

  @NotNull(groups = TraineeUpdate.class)
  @Valid
  private final EmailDetailsDTO emailDetails;

  public EmailDetailsProvidedEvent(Long personId, EmailDetailsDTO emailDetails) {
    this.personId = personId;
    this.emailDetails = emailDetails;
  }

  public Long getPersonId() {
    return personId;
  }

  public EmailDetailsDTO getEmailDetails() {
    return emailDetails;
  }
}