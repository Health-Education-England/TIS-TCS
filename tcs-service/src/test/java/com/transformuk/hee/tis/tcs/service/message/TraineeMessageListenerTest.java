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
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;

class TraineeMessageListenerTest {

  private static final Long CURRICULUM_MEMBERSHIP_ID = 40L;
  private static final UUID PROGRAMME_MEMBERSHIP_ID = UUID.randomUUID();
  private static final Instant SIGNED_AT = Instant.now();

  private TraineeMessageListener listener;
  private ConditionsOfJoiningService service;
  private ConditionsOfJoiningDto dto;

  @BeforeEach
  void setUp() {
    service = mock(ConditionsOfJoiningService.class);
    listener = new TraineeMessageListener(service);
    dto = new ConditionsOfJoiningDto();
    dto.setSignedAt(SIGNED_AT);
    dto.setVersion(GoldGuideVersion.GG9);
  }

  @Test
  void shouldSaveSignedCojWhenEventValidWithCmId() {
    ConditionsOfJoiningSignedEvent event = new ConditionsOfJoiningSignedEvent(
        CURRICULUM_MEMBERSHIP_ID, dto);

    listener.receiveMessage(event);

    verify(service).save(CURRICULUM_MEMBERSHIP_ID, dto);
  }

  @Test
  void shouldSaveSignedCojWhenEventValidWithPmId() {
    ConditionsOfJoiningSignedEvent event = new ConditionsOfJoiningSignedEvent(
        PROGRAMME_MEMBERSHIP_ID, dto);

    listener.receiveMessage(event);

    verify(service).save(PROGRAMME_MEMBERSHIP_ID, dto);
  }

  @Test
  void shouldNotRequeueMessageWhenEventArgumentsInvalid() {
    ConditionsOfJoiningSignedEvent event = new ConditionsOfJoiningSignedEvent(
        CURRICULUM_MEMBERSHIP_ID, dto);

    when(service.save(CURRICULUM_MEMBERSHIP_ID, dto)).thenThrow(IllegalArgumentException.class);

    assertThrows(AmqpRejectAndDontRequeueException.class, () -> listener.receiveMessage(event));
  }
}
