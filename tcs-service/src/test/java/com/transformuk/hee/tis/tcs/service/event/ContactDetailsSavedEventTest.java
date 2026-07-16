package com.transformuk.hee.tis.tcs.service.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.transformuk.hee.tis.tcs.api.dto.ContactDetailsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ContactDetailsSavedEventTest {

  private static final Long PREVIOUS_CONTACT_DETAILS_ID = 1L;
  private static final Long CURRENT_CONTACT_DETAILS_ID = 2L;
  private static final Long DIFFERENT_CURRENT_CONTACT_DETAILS_ID = 3L;
  private static final Long DIFFERENT_PREVIOUS_CONTACT_DETAILS_ID = 4L;

  private static final String PREVIOUS_FORENAMES = "Old";
  private static final String PREVIOUS_SURNAME = "Name";
  private static final String PREVIOUS_EMAIL = "old@example.com";

  private static final String CURRENT_FORENAMES = "New";
  private static final String CURRENT_SURNAME = "Name";
  private static final String CURRENT_EMAIL = "new@example.com";

  private static final String SAME_ID_FORENAMES = "Another";
  private static final String SAME_ID_PREVIOUS_SURNAME = "Previous";
  private static final String SAME_ID_PREVIOUS_EMAIL = "previous2@example.com";
  private static final String SAME_ID_CURRENT_SURNAME = "Current";
  private static final String SAME_ID_CURRENT_EMAIL = "current2@example.com";

  private static final String DIFFERENT_FORENAMES = "Different";
  private static final String DIFFERENT_CURRENT_SURNAME = "Current";
  private static final String DIFFERENT_CURRENT_EMAIL = "different@example.com";
  private static final String DIFFERENT_PREVIOUS_SURNAME = "Previous";
  private static final String DIFFERENT_PREVIOUS_EMAIL = "different.previous@example.com";

  private ContactDetailsDTO previousContactDetails;
  private ContactDetailsDTO currentContactDetails;

  @BeforeEach
  void setUp() {
    previousContactDetails = createContactDetails(PREVIOUS_CONTACT_DETAILS_ID, PREVIOUS_FORENAMES,
        PREVIOUS_SURNAME, PREVIOUS_EMAIL);
    currentContactDetails = createContactDetails(CURRENT_CONTACT_DETAILS_ID, CURRENT_FORENAMES,
        CURRENT_SURNAME, CURRENT_EMAIL);
  }

  @Test
  void shouldCreateEventWithCurrentContactDetailsOnly() {
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(currentContactDetails);

    assertSame(currentContactDetails, event.getContactDetailsDto());
    assertNull(event.getPreviousContactDetailsDto());
    assertSame(currentContactDetails, event.getSource());
  }

  @Test
  void shouldCreateEventWithPreviousAndCurrentContactDetails() {
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(previousContactDetails,
        currentContactDetails);

    assertSame(currentContactDetails, event.getContactDetailsDto());
    assertSame(previousContactDetails, event.getPreviousContactDetailsDto());
    assertSame(currentContactDetails, event.getSource());
  }

  @Test
  void shouldEvaluateEquality() {
    ContactDetailsSavedEvent event = new ContactDetailsSavedEvent(previousContactDetails,
        currentContactDetails);
    ContactDetailsSavedEvent sameIdEvent = new ContactDetailsSavedEvent(
        createContactDetails(PREVIOUS_CONTACT_DETAILS_ID, SAME_ID_FORENAMES,
            SAME_ID_PREVIOUS_SURNAME, SAME_ID_PREVIOUS_EMAIL),
        createContactDetails(CURRENT_CONTACT_DETAILS_ID, SAME_ID_FORENAMES,
            SAME_ID_CURRENT_SURNAME, SAME_ID_CURRENT_EMAIL));
    ContactDetailsSavedEvent differentCurrent = new ContactDetailsSavedEvent(previousContactDetails,
        createContactDetails(DIFFERENT_CURRENT_CONTACT_DETAILS_ID, DIFFERENT_FORENAMES,
            DIFFERENT_CURRENT_SURNAME, DIFFERENT_CURRENT_EMAIL));
    ContactDetailsSavedEvent differentPrevious = new ContactDetailsSavedEvent(
        createContactDetails(DIFFERENT_PREVIOUS_CONTACT_DETAILS_ID,
            DIFFERENT_FORENAMES, DIFFERENT_PREVIOUS_SURNAME,
            DIFFERENT_PREVIOUS_EMAIL),
        currentContactDetails);

    assertEquals(event, sameIdEvent);
    assertEquals(event.hashCode(), sameIdEvent.hashCode());
    assertNotEquals(null, event);
    assertNotEquals(differentCurrent, event);
    assertNotEquals(differentPrevious, event);
    assertNotEquals(event.hashCode(), differentCurrent.hashCode());
    assertNotEquals(event.hashCode(), differentPrevious.hashCode());
  }

  private ContactDetailsDTO createContactDetails(Long id, String forenames, String surname,
      String email) {
    ContactDetailsDTO contactDetails = new ContactDetailsDTO();
    contactDetails.setId(id);
    contactDetails.setForenames(forenames);
    contactDetails.setSurname(surname);
    contactDetails.setEmail(email);
    return contactDetails;
  }
}
