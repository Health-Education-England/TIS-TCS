package com.transformuk.hee.tis.tcs.message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.transformuk.hee.tis.tcs.api.dto.ConnectionInfoDto;
import com.transformuk.hee.tis.tcs.service.message.RevalidationMessageListener;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import lombok.var;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;


@RunWith(MockitoJUnitRunner.class)
public class RevalidationMessageListenerTest {

  private final String EXCHANGE = "exchange";
  private final String NO_EXCHANGE = "false";
  private final String ROUTING_KEY = "routingKey";


  @Mock
  RabbitTemplate rabbitTemplate;

  @Mock
  RevalidationService revalidationService;

  @InjectMocks
  RevalidationMessageListener revalidationMessageListener;

  @BeforeAll
  void setupMocks () {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void shouldExtractInfo() {
    setField(revalidationMessageListener, EXCHANGE, EXCHANGE);
    setField(revalidationMessageListener, ROUTING_KEY, ROUTING_KEY);

    revalidationMessageListener.receiveMessage("syncStart");
    verify(revalidationService).extractConnectionInfoForSync();
  }

  @Test
  public void shouldNotSendIfExhangeFalse() {
    setField(revalidationMessageListener, EXCHANGE, NO_EXCHANGE);
    setField(revalidationMessageListener, ROUTING_KEY, ROUTING_KEY);

    revalidationMessageListener.receiveMessage("syncStart");
    verify(rabbitTemplate, never()).convertAndSend(any(String.class), any(String.class), any(ConnectionInfoDto.class));
  }

}
