package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
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
  ContactDetailsDTO contactDetails;

  public EmailDetailsProvidedEvent(Long personId, ContactDetailsDTO contactDetails) {
    this.personId = personId;
    this.contactDetails = contactDetails;
  }

  public Long getPersonId() {
    return personId;
  }

  public ContactDetailsDTO getContactDetails() {
    return contactDetails;
  }
}