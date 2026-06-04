package com.transformuk.hee.tis.tcs.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AcknowledgeMode;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty("spring.rabbitmq.host")
@Configuration
public class RabbitConfig {

  @Bean
  public MessageConverter jsonMessageConverter() {
    final ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    return new Jackson2JsonMessageConverter(mapper);
  }

  /**
   * Rabbit template for setting message to RabbitMQ.
   */
  @Bean
  public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonMessageConverter());
    rabbitTemplate.containerAckMode(AcknowledgeMode.AUTO);
    return rabbitTemplate;
  }
}
