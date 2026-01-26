package com.transformuk.hee.tis.tcs.service.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transformuk.hee.tis.tcs.service.event.EmailDetailsProvidedEvent;
import java.io.IOException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailDetailsProvidedEventDeserializerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void shouldDeserializeValidMessage() throws IOException {
    String json = "{\"personId\": \"123\", \"emailDetails\": {\"email\": \"test@example.com\"}}";
    EmailDetailsProvidedEvent event = objectMapper
        .readerFor(EmailDetailsProvidedEvent.class)
        .readValue(json);

    assertNotNull(event);
    assertEquals(123L, event.getPersonId());
    assertEquals("test@example.com", event.getEmail());
  }

  @Test
  void shouldDeserializeWithNullEmailIfMissing() throws IOException {
    String json = "{\"personId\": \"456\", \"emailDetails\": {}}";
    EmailDetailsProvidedEvent event = objectMapper
        .readerFor(EmailDetailsProvidedEvent.class)
        .readValue(json);

    assertNotNull(event);
    assertEquals(456L, event.getPersonId());
    assertNull(event.getEmail());
  }

  @Test
  void shouldThrowExceptionForInvalidPersonId() {
    String json = "{\"personId\": \"abc\", \"emailDetails\": {\"email\": \"test@example.com\"}}";
    assertThrows(IOException.class, () -> objectMapper
        .readerFor(EmailDetailsProvidedEvent.class)
        .readValue(json));
  }

  @Test
  void shouldThrowExceptionIfPersonIdMissing() {
    String json = "{\"emailDetails\": {\"email\": \"test@example.com\"}}";
    assertThrows(NullPointerException.class, () -> objectMapper
        .readerFor(EmailDetailsProvidedEvent.class)
        .readValue(json));
  }
}
