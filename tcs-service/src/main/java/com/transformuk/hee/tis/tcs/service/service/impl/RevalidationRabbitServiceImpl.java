package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.service.service.RevalidationRabbitService;
import javax.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RevalidationRabbitServiceImpl implements RevalidationRabbitService {

  @Value("${app.rabbit.reval.exchange}")
  private String exchange;

  @Value("${app.rabbit.reval.routingKey.revalupdate.created}")
  private String routingKey;

  private RabbitTemplate rabbitTemplate;

  public RevalidationRabbitServiceImpl(
      RabbitTemplate rabbitTemplate
  ) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Override
  public void updateReval(Object object) {
    if (!exchange.equals("false")) {
      rabbitTemplate.convertAndSend(exchange, routingKey, object);
    }
  }
}
