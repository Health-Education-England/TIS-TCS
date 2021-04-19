package com.transformuk.hee.tis.tcs.service.message;

import com.transformuk.hee.tis.tcs.api.dto.ConnectionInfoDto;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RevalidationMessageListener {

  @Value("${app.rabbit.reval.exchange}")
  private String exchange;

  @Value("${app.rabbit.reval.routingKey.connection.syncdata}")
  private String routingKey;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  RevalidationService revalidationService;

  @RabbitListener(queues = "${app.rabbit.reval.queue.connection.syncstart}")
  public void receiveMessage(final String start) {
    if (start.equals("syncStart")) {
      List<ConnectionInfoDto> connections = revalidationService.extractConnectionInfoForSync();
      if (!exchange.equals("false")) {
        for (ConnectionInfoDto connection : connections) {
          rabbitTemplate.convertAndSend(exchange, routingKey, connection);
        }
      }
    }
  }

}
