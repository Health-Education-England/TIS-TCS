package com.transformuk.hee.tis.tcs.service.message;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import com.transformuk.hee.tis.tcs.service.event.ConditionsOfJoiningSignedEvent;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

class TraineeMessageListenerTest {

  private static final Long ID = 40L;
  private static final Instant SIGNED_AT = Instant.now();

  private TraineeMessageListener listener;
  private ConditionsOfJoiningService service;

  @BeforeEach
  void setUp() {
    service = mock(ConditionsOfJoiningService.class);
    listener = new TraineeMessageListener(service);
  }

  @Test
  void shouldSaveSignedCojWhenEventValid() {
    ConditionsOfJoiningDto dto = new ConditionsOfJoiningDto();
    dto.setSignedAt(SIGNED_AT);
    dto.setVersion(GoldGuideVersion.GG9);
    ConditionsOfJoiningSignedEvent event = new ConditionsOfJoiningSignedEvent(ID, dto);

    listener.receiveMessage(event);

    verify(service).save(ID, dto);
  }

  @Test
  void shouldNotRequeueMessageWhenEventArgumentsInvalid() {
    ConditionsOfJoiningDto dto = new ConditionsOfJoiningDto();
    dto.setSignedAt(SIGNED_AT);
    dto.setVersion(GoldGuideVersion.GG9);
    ConditionsOfJoiningSignedEvent event = new ConditionsOfJoiningSignedEvent(ID, dto);

    when(service.save(ID, dto)).thenThrow(IllegalArgumentException.class);

    assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.receiveMessage(event));
  }
}
