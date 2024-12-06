package com.transformuk.hee.tis.tcs.service.service;

import java.util.Map;

public interface SqsFifoMessagingService {

  void sendMessageToFifoQueue(String queueUrl, Map<String, String> toSend, String table,
      String id);

}
