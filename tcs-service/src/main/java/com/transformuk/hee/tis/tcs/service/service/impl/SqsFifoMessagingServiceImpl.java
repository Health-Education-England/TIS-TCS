package com.transformuk.hee.tis.tcs.service.service.impl;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transformuk.hee.tis.tcs.service.service.SqsFifoMessagingService;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * A service for sending messages to a SQS FIFO queue with appropriate message group ids.
 */
@Service
@Slf4j
public class SqsFifoMessagingServiceImpl implements SqsFifoMessagingService {

  private final AmazonSQS sqs;

  private static final String MESSAGE_GROUP_ID_FORMAT = "%s_%s_%s";
  protected static final String DEFAULT_SCHEMA = "tcs";

  public SqsFifoMessagingServiceImpl(AmazonSQS sqs) {
    this.sqs = sqs;
  }

  /**
   * Send a message to a SQS FIFO queue.
   *
   * @param queueUrl The message queue URL.
   * @param toSend   The object to send.
   * @param table    The object type (table name) contained in the message.
   * @param id       The object id.
   */
  public void sendMessageToFifoQueue(String queueUrl, Map<String, String> toSend,
      String table, String id) {

    //override default content-based deduplication
    String deduplicationId = getUniqueDeduplicationId(table, id);
    String messageGroupId = getMessageGroupId(table, id);

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonToSend;
    try {
      jsonToSend = objectMapper.writeValueAsString(toSend);
    } catch (JsonProcessingException jpe) {
      log.warn("FIFO queue {} message not sent, content could not be processed: {}",
          queueUrl, toSend);
      return;
    }

    log.debug("Sending to FIFO queue {} (messageGroupId {} : deduplicationId {}): {}",
        queueUrl, messageGroupId, deduplicationId, jsonToSend);

    SendMessageRequest sendMessageRequest = new SendMessageRequest();
    sendMessageRequest.setMessageBody(jsonToSend);
    sendMessageRequest.setMessageGroupId(messageGroupId);
    sendMessageRequest.setMessageDeduplicationId(deduplicationId);
    sendMessageRequest.setQueueUrl(queueUrl);

    sqs.sendMessage(sendMessageRequest);
  }

  /**
   * Create a unique deduplication id for a particular object.
   *
   * @param objectType The object type.
   * @param id         The object Id.
   * @return The unique deduplication string.
   */
  protected String getUniqueDeduplicationId(String objectType, String id) {
    return String.format(MESSAGE_GROUP_ID_FORMAT, objectType, id, Instant.now());
  }

  /**
   * Get a properly formatted Message Group Id for an object.
   *
   * @param objectType The object that will be sent in the message.
   * @param id         The object Id.
   * @return The Message Group Id, formatted as schema_table_id
   */
  protected String getMessageGroupId(String objectType, String id) {
    return String.format(MESSAGE_GROUP_ID_FORMAT, DEFAULT_SCHEMA, objectType, id);
  }
}
