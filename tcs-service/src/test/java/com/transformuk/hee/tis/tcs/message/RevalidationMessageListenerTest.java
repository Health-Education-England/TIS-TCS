package com.transformuk.hee.tis.tcs.message;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.ConnectionInfoDto;
import com.transformuk.hee.tis.tcs.service.message.RevalidationMessageListener;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RunWith(MockitoJUnitRunner.class)
public class RevalidationMessageListenerTest {

  private final String EXCHANGE = "exchange";
  private final String NO_EXCHANGE = "false";
  private final String ROUTING_KEY = "routingKey";

  @Mock
  RabbitTemplate rabbitTemplate;

  @Mock
  RevalidationService revalidationService;

  @Mock
  RevalidationRabbitService revalidationRabbitService;

  @InjectMocks
  RevalidationMessageListener revalidationMessageListener;

  @Captor
  ArgumentCaptor<Long> personIdCaptor;

  @Captor
  ArgumentCaptor<ConnectionInfoDto> connectionInfoDtoArguementCaptor;

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

  @Test
  public void shouldSendMsgForCurrentPmUpdate() {
    setField(revalidationMessageListener, EXCHANGE, EXCHANGE);
    setField(revalidationMessageListener, ROUTING_KEY, ROUTING_KEY);

    List<String> personIdStrs = Lists.newArrayList("1", "2", "3");
    revalidationMessageListener.receiveMessageNightlyPmSync(personIdStrs);

    verify(revalidationService, times(3))
        .buildTcsConnectionInfo(personIdCaptor.capture());
    verify(revalidationRabbitService, times(3))
        .updateReval(connectionInfoDtoArguementCaptor.capture());

    List<Long> personIds = personIdCaptor.getAllValues();
    List<ConnectionInfoDto> connectionInfoDtos = connectionInfoDtoArguementCaptor.getAllValues();

    assertThat(personIds).hasSize(3).containsExactlyInAnyOrder(1L, 2L, 3L);
    assertThat(connectionInfoDtos).hasSize(3);
  }
}
