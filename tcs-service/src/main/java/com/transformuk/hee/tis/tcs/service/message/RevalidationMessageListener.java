package com.transformuk.hee.tis.tcs.service.message;

import com.transformuk.hee.tis.tcs.api.dto.ConnectionInfoDto;
import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RevalidationMessageListener {

  private static final Logger LOG = LoggerFactory.getLogger(RevalidationMessageListener.class);

  @Value("${app.rabbit.reval.exchange}")
  private String exchange;

  @Value("${app.rabbit.reval.routingKey.connection.syncdata}")
  private String routingKey;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  RevalidationService revalidationService;

  @Autowired
  RevalidationRabbitService revalidationRabbitService;

  /**
   * Receive message from the Rabbit queue if app is configured for the Reval exchange.
   *
   * @param start set to 'syncStart' to receive messages from the reval rabbit queue
   */
  @RabbitListener(queues = "${app.rabbit.reval.queue.connection.syncstart}", ackMode = "NONE")
  public void receiveMessage(final String start) {
    if (start.equals("syncStart") && !exchange.equals("false")) {
      List<ConnectionInfoDto> connections = revalidationService.extractConnectionInfoForSync();
      LOG.info("TCS to ES masterdoctorindex sync started. There are {} doctors extracted from TCS.",
          connections.size());

      for (ConnectionInfoDto connection : connections) {
        rabbitTemplate.convertAndSend(exchange, routingKey, connection);
      }
      rabbitTemplate.convertAndSend(exchange, routingKey, getSyncEndMessageDto());
    }
  }

  /**
   * Receive personIds from Rabbit queue for trainee changes to export to Reval.
   *
   * @param personIds the personIds for trainees that have changed
   */
  @RabbitListener(queues = "${app.rabbit.reval.queue.trainee.update.tcs}")
  public void receiveMessageTraineeSyncChanges(final List<String> personIds) {
    LOG.info("Received {} personIds to update trainees in reval.", personIds.size());
    personIds.forEach(id -> revalidationRabbitService.updateReval(
        revalidationService.buildTcsConnectionInfo(Long.valueOf(id))));
  }

  private ConnectionInfoDto getSyncEndMessageDto() {
    return ConnectionInfoDto.builder().syncEnd(true).build();
  }
}
