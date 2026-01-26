package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import com.transformuk.hee.tis.tcs.service.event.EmailDetailsProvidedEvent;
import java.io.IOException;

/**
 * Deserializer for EmailDetailsProvidedEvent.
 */
public class EmailDetailsProvidedEventDeserializer
    extends JsonDeserializer<EmailDetailsProvidedEvent> {

  @Override
  public EmailDetailsProvidedEvent deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    JsonNode node = p.getCodec().readTree(p);

    String personIdStr = node.get("personId").asText();
    long personId;
    try {
      personId = Long.parseLong(personIdStr);
    } catch (NumberFormatException e) {
      throw new IOException("Invalid personId: " + personIdStr, e);
    }

    // emailDetails.email
    JsonNode emailDetailsNode = node.get("emailDetails");
    String email = null;
    if (emailDetailsNode != null && emailDetailsNode.has("email")) {
      email = emailDetailsNode.get("email").asText();
    }

    return new EmailDetailsProvidedEvent(personId, email);
  }
}
