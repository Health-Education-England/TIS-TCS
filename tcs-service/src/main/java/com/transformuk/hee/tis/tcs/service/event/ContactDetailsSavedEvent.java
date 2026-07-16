package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class ContactDetailsSavedEvent extends ApplicationEvent {

  private final ContactDetailsDTO contactDetailsDto;
  private final ContactDetailsDTO previousContactDetailsDto;

  public ContactDetailsSavedEvent(@NonNull ContactDetailsDTO source) {
    this(null, source);
  }

  public ContactDetailsSavedEvent(@Nullable ContactDetailsDTO previousContactDetails,
      @NonNull ContactDetailsDTO contactDetails) {
    super(contactDetails);
    this.contactDetailsDto = contactDetails;
    this.previousContactDetailsDto = previousContactDetails;
  }

  public ContactDetailsDTO getContactDetailsDto() {
    return contactDetailsDto;
  }

  public ContactDetailsDTO getPreviousContactDetailsDto() {
    return previousContactDetailsDto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContactDetailsSavedEvent that = (ContactDetailsSavedEvent) o;
    return Objects.equals(contactDetailsDto, that.contactDetailsDto)
        && Objects.equals(previousContactDetailsDto, that.previousContactDetailsDto);
  }

  @Override
  public int hashCode() {
    return Objects.hash(contactDetailsDto, previousContactDetailsDto);
  }
}
