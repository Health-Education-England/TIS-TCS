package com.transformuk.hee.tis.tcs.service.message;

import com.transformuk.hee.tis.tcs.service.event.ConditionsOfJoiningSignedEvent;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * A listener for trainee focused events.
 */
@Component
public class TraineeMessageListener {

  private final ConditionsOfJoiningService conditionsOfJoiningService;

  TraineeMessageListener(ConditionsOfJoiningService conditionsOfJoiningService) {
    this.conditionsOfJoiningService = conditionsOfJoiningService;
  }

  @RabbitListener(queues = "${app.rabbit.trainee.queue.coj.signed}", ackMode = "AUTO")
  public void receiveMessage(final ConditionsOfJoiningSignedEvent event) {
    conditionsOfJoiningService.save(event.getProgrammeMembershipId(),
        event.getConditionsOfJoining());
  }
}
