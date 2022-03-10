package com.transformuk.hee.tis.tcs.service.message;

import com.transformuk.hee.tis.tcs.api.dto.MainDoctorViewDto;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import java.util.List;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

  /**
   * Receive message from the Rabbit queue if app is configured for the Reval exchange.
   *
   * @param start set to 'syncStart' to receive messages from the reval rabbit queue
   */
  @RabbitListener(queues = "${app.rabbit.reval.queue.connection.syncstart}")
  public void receiveMessage(final String start) {
    if (start.equals("syncStart") && !exchange.equals("false")) {
      List<MainDoctorViewDto> connections = revalidationService.extractConnectionInfoForSync();
      for (MainDoctorViewDto connection : connections) {
        rabbitTemplate.convertAndSend(exchange, routingKey, connection);
      }
      rabbitTemplate.convertAndSend(exchange, routingKey, getSyncEndMessageDto());
    }
  }

  private MainDoctorViewDto getSyncEndMessageDto() {
    return MainDoctorViewDto.builder().syncEnd(true).build();
  }
}
