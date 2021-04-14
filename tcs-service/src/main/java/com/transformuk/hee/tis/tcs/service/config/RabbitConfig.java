package com.transformuk.hee.tis.tcs.service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty("spring.rabbitmq.host")
@Configuration
public class RabbitConfig {

  @Value("${app.rabbit.reval.queue.connection.update}")
  private String revalQueueName;

  @Value("${app.rabbit.reval.queue.connection.syncstart}")
  private String revalSyncQueueName;

  @Value("${app.rabbit.reval.queue.connection.syncdata}")
  private String revalSyncDataQueueName;

  @Value("${app.rabbit.reval.exchange}")
  private String revalExchange;

  @Value("${app.rabbit.reval.routingKey.connection.update}")
  private String revalRoutingKey;

  @Value("${app.rabbit.reval.routingKey.connection.syncstart}")
  private String revalSyncStartRoutingKey;

  @Value("${app.rabbit.reval.routingKey.connection.syncdata}")
  private String revalSyncDataRoutingKey;

  @Bean
  public Queue revalQueue() {
    return new Queue(revalQueueName, false);
  }

  @Bean
  public Queue revalSyncqueue() {
    return new Queue(revalSyncQueueName, false);
  }

  @Bean
  public Queue revalDataqueue() {
    return new Queue(revalSyncDataQueueName, false);
  }

  @Bean
  public DirectExchange exchange() {
    return new DirectExchange(revalExchange);
  }

  @Bean
  public Binding revalBinding(final Queue revalQueue, final DirectExchange exchange) {
    return BindingBuilder.bind(revalQueue).to(exchange).with(revalRoutingKey);
  }

  @Bean
  public Binding revalSyncBinding(final Queue revalSyncqueue, final DirectExchange exchange) {
    return BindingBuilder.bind(revalSyncqueue).to(exchange).with(revalSyncStartRoutingKey);
  }

  @Bean
  public Binding revalDataBinding(final Queue revalDataqueue, final DirectExchange exchange) {
    return BindingBuilder.bind(revalDataqueue).to(exchange).with(revalSyncDataRoutingKey);
  }

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
