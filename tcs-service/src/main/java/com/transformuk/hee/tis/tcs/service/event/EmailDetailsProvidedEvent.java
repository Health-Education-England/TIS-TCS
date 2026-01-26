package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.EmailDetailsDto;
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
  private final EmailDetailsDto emailDetails;

  public EmailDetailsProvidedEvent(Long personId, EmailDetailsDto emailDetails) {
    this.personId = personId;
    this.emailDetails = emailDetails;
  }

  public Long getPersonId() {
    return personId;
  }

  public EmailDetailsDto getEmailDetails() {
    return emailDetails;
  }
}