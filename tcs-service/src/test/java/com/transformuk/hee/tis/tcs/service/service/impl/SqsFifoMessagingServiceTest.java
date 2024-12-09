package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.tcs.service.service.impl.SqsFifoMessagingServiceImpl.DEFAULT_SCHEMA;
import static com.transformuk.hee.tis.tcs.service.service.impl.SqsFifoMessagingServiceImpl.MESSAGE_GROUP_ID_FORMAT;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.transformuk.hee.tis.tcs.service.service.SqsFifoMessagingService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class SqsFifoMessagingServiceTest {

  private static final String QUEUE_URL = "queue.url";
  private static final String TABLE = "table";
  private static final String ID = "id";

  private AmazonSQS sqs;

  @Spy
  private SqsFifoMessagingService testObj;

  private ObjectMapper mapper;

  @BeforeEach
  void setUp() {
    sqs = mock(AmazonSQS.class);
    mapper = mock(ObjectMapper.class);
    testObj = new SqsFifoMessagingServiceImpl(sqs, mapper);
  }

  @Test
  void shouldSendValidMessageToFifoQueue() throws JsonProcessingException {
    Map<String, String> theMap = new HashMap<>();
    theMap.put("key", "value");
    String theMapJson = "{\"key\":\"value\"}";
    when(mapper.writeValueAsString(theMap)).thenReturn(theMapJson);

    testObj.sendMessageToFifoQueue(QUEUE_URL, theMap, TABLE, ID);

    ArgumentCaptor<SendMessageRequest> sendMessageRequest
        = ArgumentCaptor.forClass(SendMessageRequest.class);
    verify(sqs).sendMessage(sendMessageRequest.capture());
    verifyNoMoreInteractions(sqs);

    SendMessageRequest sent = sendMessageRequest.getValue();
    String messageBody = sent.getMessageBody();
    assertThat("Unexpected message body.", messageBody, is(theMapJson));
    assertThat("Unexpected message queue.", sent.getQueueUrl(), is(QUEUE_URL));
    String msgGroupId = String.format(MESSAGE_GROUP_ID_FORMAT, DEFAULT_SCHEMA, TABLE, ID);
    assertThat("Unexpected message group ID.", sent.getMessageGroupId(), is(msgGroupId));
    int dedupPrefixLength = TABLE.length() + "_".length() + ID.length() + "_".length();
    String deduplicationIdPrefix = String.format(MESSAGE_GROUP_ID_FORMAT, TABLE, ID, "");
    assertThat("Unexpected message deduplication ID.",
        sent.getMessageDeduplicationId().substring(0, dedupPrefixLength),
        is(deduplicationIdPrefix));
  }

  @Test
  void shouldHandleJsonExceptions() throws JsonProcessingException {
    when(mapper.writeValueAsString(anyMap())).thenThrow(new JsonProcessingException("error"){});

    assertDoesNotThrow(()
        -> testObj.sendMessageToFifoQueue(QUEUE_URL, new HashMap<>(), TABLE, ID));

  }
}
