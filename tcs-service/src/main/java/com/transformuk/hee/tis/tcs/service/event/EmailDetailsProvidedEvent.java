package com.transformuk.hee.tis.tcs.service.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.TraineeUpdate;
import com.transformuk.hee.tis.tcs.api.dto.validation.Update;
import com.transformuk.hee.tis.tcs.service.service.mapper.EmailDetailsProvidedEventDeserializer;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * An event triggered when a trainee provides email details.
 */
@JsonDeserialize(using = EmailDetailsProvidedEventDeserializer.class)
public class EmailDetailsProvidedEvent {

  @NotNull(groups = TraineeUpdate.class)
  private final Long personId;

  @NotBlank(message = "Email is required",
      groups = {Update.class, Create.class, TraineeUpdate.class})
  @Email(message = "Valid email format required",
      groups = {Update.class, Create.class, TraineeUpdate.class})
  String email;

  public EmailDetailsProvidedEvent(Long personId, String email) {
    this.personId = personId;
    this.email = email;
  }

  public Long getPersonId() {
    return personId;
  }

  public String getEmail() {
    return email;
  }
}