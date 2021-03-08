package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipCreatedEvent;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipDeletedEvent;
import com.transformuk.hee.tis.tcs.service.event.ProgrammeMembershipSavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ProgrammeMembershipEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(ProgrammeMembershipEventListener.class);

  @Value("${app.rabbit.exchange}")
  private String exchange;

  @Value("${app.rabbit.routingkey}")
  private String routingKey;

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private PersonElasticSearchService personElasticSearchService;

  @Autowired
  private RevalidationService revalidationService;

  @EventListener
  public void handleProgrammeMembershipSavedEvent(ProgrammeMembershipSavedEvent event) {
    LOG.info("Received ProgrammeMembership saved event for ProgrammeMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    final Long personId = event.getProgrammeMembershipDTO().getPerson().getId();
    personElasticSearchService.updatePersonDocument(personId);
    if (!exchange.equals("false")) {
      rabbitTemplate.convertAndSend(exchange, routingKey,
          revalidationService.buildTcsConnectionInfo(personId));
    }
  }

  @EventListener
  public void handleProgrammeMembershipCreatedEvent(ProgrammeMembershipCreatedEvent event) {
    LOG.info("Received ProgrammeMembership created event for ProgrammeMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    final Long personId = event.getProgrammeMembershipDTO().getPerson().getId();
    personElasticSearchService.updatePersonDocument(personId);
    if (!exchange.equals("false")) {
      rabbitTemplate.convertAndSend(exchange, routingKey,
          revalidationService.buildTcsConnectionInfo(personId));
    }
  }

  @EventListener
  public void handleProgrammeMembershipDeletedEvent(ProgrammeMembershipDeletedEvent event) {
    LOG.info("Received ProgrammeMembership deleted event for ProgrammeMembership id: [{}]",
        event.getProgrammeMembershipDTO().getId());
    final Long personId = event.getProgrammeMembershipDTO().getPerson().getId();
    personElasticSearchService.deletePersonDocument(personId);
    if (!exchange.equals("false")) {
      rabbitTemplate.convertAndSend(exchange, routingKey,
          revalidationService.buildTcsConnectionInfo(personId));
    }
  }

}
