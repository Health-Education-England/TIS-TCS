package com.transformuk.hee.tis.tcs.service.service;

import java.util.Map;

/**
 * Interface for sending messages to SQS FIFO (first-in first-out) queues.
 */
public interface SqsFifoMessagingService {

  void sendMessageToFifoQueue(String queueUrl, Map<String, String> toSend, String table,
      String id);

}
