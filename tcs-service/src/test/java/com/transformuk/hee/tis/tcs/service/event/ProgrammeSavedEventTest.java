package com.transformuk.hee.tis.tcs.service.event;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgrammeSavedEventTest {

  private static final Long PREVIOUS_PROGRAMME_ID = 1L;
  private static final Long CURRENT_PROGRAMME_ID = 2L;
  private static final Long DIFFERENT_PROGRAMME_ID = 3L;

  private ProgrammeDTO previousProgramme;
  private ProgrammeDTO currentProgramme;

  @BeforeEach
  void setUp() {
    previousProgramme = createProgramme(PREVIOUS_PROGRAMME_ID);
    currentProgramme = createProgramme(CURRENT_PROGRAMME_ID);
  }

  @Test
  void shouldCreateEventWithCurrentProgrammeOnly() {
    ProgrammeSavedEvent event = new ProgrammeSavedEvent(currentProgramme);

    assertSame(currentProgramme, event.getProgrammeDTO());
    assertNull(event.getPreviousProgrammeDto());
    assertSame(currentProgramme, event.getSource());
  }

  @Test
  void shouldCreateEventWithPreviousAndCurrentProgramme() {
    ProgrammeSavedEvent event = new ProgrammeSavedEvent(previousProgramme, currentProgramme);

    assertSame(currentProgramme, event.getProgrammeDTO());
    assertSame(previousProgramme, event.getPreviousProgrammeDto());
    assertSame(currentProgramme, event.getSource());
  }

  @Test
  void shouldEvaluateEqualityAndHashCode() {
    ProgrammeSavedEvent event = new ProgrammeSavedEvent(previousProgramme, currentProgramme);
    ProgrammeSavedEvent sameIdEvent = new ProgrammeSavedEvent(
        createProgramme(PREVIOUS_PROGRAMME_ID), createProgramme(CURRENT_PROGRAMME_ID));
    ProgrammeSavedEvent differentCurrent = new ProgrammeSavedEvent(previousProgramme,
        createProgramme(DIFFERENT_PROGRAMME_ID));
    ProgrammeSavedEvent differentPrevious = new ProgrammeSavedEvent(
        createProgramme(DIFFERENT_PROGRAMME_ID), currentProgramme);

    assertEquals(event, sameIdEvent);
    assertEquals(event.hashCode(), sameIdEvent.hashCode());
    assertNotEquals(null, event);
    assertNotEquals(differentCurrent, event);
    assertNotEquals(differentPrevious, event);
  }

  private ProgrammeDTO createProgramme(Long id) {
    ProgrammeDTO programme = new ProgrammeDTO();
    programme.setId(id);
    return programme;
  }
}

