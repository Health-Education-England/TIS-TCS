package com.transformuk.hee.tis.tcs.service.message;

import com.transformuk.hee.tis.tcs.service.event.ConditionsOfJoiningSignedEvent;
import com.transformuk.hee.tis.tcs.service.service.impl.ConditionsOfJoiningServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * A listener for trainee focused events.
 */
@Component
public class TraineeMessageListener {

  private final ConditionsOfJoiningServiceImpl conditionsOfJoiningService;

  TraineeMessageListener(ConditionsOfJoiningServiceImpl conditionsOfJoiningService) {
    this.conditionsOfJoiningService = conditionsOfJoiningService;
  }

  @RabbitListener(queues = "${app.rabbit.trainee.queue.coj.signed}", ackMode = "AUTO")
  public void receiveMessage(final ConditionsOfJoiningSignedEvent event) {
    conditionsOfJoiningService.save(event.getProgrammeMembershipId(),
        event.getConditionsOfJoining());
  }
}
