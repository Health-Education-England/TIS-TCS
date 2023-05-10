package com.transformuk.hee.tis.tcs.service.message;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import com.transformuk.hee.tis.tcs.service.event.ConditionsOfJoiningSignedEvent;
import com.transformuk.hee.tis.tcs.service.service.impl.ConditionsOfJoiningServiceImpl;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TraineeMessageListenerTest {

  private static final Long ID = 40L;
  private static final Instant SIGNED_AT = Instant.now();

  private TraineeMessageListener listener;
  private ConditionsOfJoiningServiceImpl service;

  @BeforeEach
  void setUp() {
    service = mock(ConditionsOfJoiningServiceImpl.class);
    listener = new TraineeMessageListener(service);
  }

  @Test
  void shouldSaveSignedCoj() {
    ConditionsOfJoiningDto dto = new ConditionsOfJoiningDto();
    dto.setSignedAt(SIGNED_AT);
    dto.setVersion(GoldGuideVersion.GG9);
    ConditionsOfJoiningSignedEvent event = new ConditionsOfJoiningSignedEvent(ID, dto);

    listener.receiveMessage(event);

    verify(service).save(ID, dto);
  }
}
