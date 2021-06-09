package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RunWith(MockitoJUnitRunner.class)
public class RevalidationRabbitServiceImplTest {

  private static final Object OBJECT = new Object();
  private static final String EXCHANGE_TCS = "reval-exchange";
  private static final String ROUTINGKEY_TCS = "reval.connection.update";
  private static final String EXCHANGE_NIMDTA = "false";
  private static final String ROUTINGKEY_NIMDTA = "false";

  @Mock
  RabbitTemplate rabbitTemplate;

  @InjectMocks
  RevalidationRabbitServiceImpl testObj;

  public void tcsFieldsSetup() {
    setField(testObj, "exchange", EXCHANGE_TCS);
    setField(testObj, "routingKey", ROUTINGKEY_TCS);
  }

  public void nimdtaFieldsSetup() {
    setField(testObj, "exchange", EXCHANGE_NIMDTA);
    setField(testObj, "routingKey", ROUTINGKEY_NIMDTA);
  }

  @Test
  public void shouldSendMessage() {
    tcsFieldsSetup();
    testObj.updateReval(OBJECT);
    verify(rabbitTemplate).convertAndSend(EXCHANGE_TCS, ROUTINGKEY_TCS,
        OBJECT);
  }

  @Test
  public void shouldNotSendMessageInNimdta() {
    nimdtaFieldsSetup();
    testObj.updateReval(OBJECT);
    verify(rabbitTemplate, never()).convertAndSend(EXCHANGE_NIMDTA, ROUTINGKEY_NIMDTA,
        OBJECT);
  }
}
