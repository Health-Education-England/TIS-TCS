/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.event;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import java.util.Objects;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * An event triggered when a trainee's contact details are saved.
 */
public class ContactDetailsSavedEvent extends ApplicationEvent {

  private final ContactDetailsDTO contactDetailsDto;
  private final ContactDetailsDTO previousContactDetailsDto;

  /**
   * Constructor for ContactDetailsSavedEvent with only the source contact details.
   *
   * @param source the current contact details that were saved
   */
  public ContactDetailsSavedEvent(@NonNull ContactDetailsDTO source) {
    this(null, source);
  }

  /**
   * Constructor for ContactDetailsSavedEvent with both the previous and current contact details.
   *
   * @param previousContactDetails the previous contact details before the save operation,
   *                               can be null if not applicable
   * @param contactDetails the current contact details that were saved, must not be null
   */
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
