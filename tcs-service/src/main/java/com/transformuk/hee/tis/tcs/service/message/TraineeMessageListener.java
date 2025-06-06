package com.transformuk.hee.tis.tcs.service.message;

import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.dto.validation.TraineeUpdate;
import com.transformuk.hee.tis.tcs.service.api.validation.GmcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.event.ConditionsOfJoiningSignedEvent;
import com.transformuk.hee.tis.tcs.service.event.GmcDetailsProvidedEvent;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import com.transformuk.hee.tis.tcs.service.service.GmcDetailsService;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * A listener for trainee focused events.
 */
@Component
public class TraineeMessageListener {


  private static final Logger LOG = LoggerFactory.getLogger(TraineeMessageListener.class);

  private final ConditionsOfJoiningService conditionsOfJoiningService;
  private final GmcDetailsService gmcDetailsService;
  private final GmcDetailsValidator gmcDetailsValidator;
  private final Validator validator;

  TraineeMessageListener(ConditionsOfJoiningService conditionsOfJoiningService,
      GmcDetailsService gmcDetailsService, GmcDetailsValidator gmcDetailsValidator,
      Validator validator) {
    this.conditionsOfJoiningService = conditionsOfJoiningService;
    this.gmcDetailsService = gmcDetailsService;
    this.gmcDetailsValidator = gmcDetailsValidator;
    this.validator = validator;
  }

  @RabbitListener(queues = "${app.rabbit.trainee.queue.coj.signed}", ackMode = "AUTO")
  public void receiveCojSignedMessage(final ConditionsOfJoiningSignedEvent event) {
    try {
      conditionsOfJoiningService.save(event.getId(),
          event.getConditionsOfJoining());
    } catch (IllegalArgumentException e) {
      // Do not requeue the message if the event arguments are not valid.
      throw new AmqpRejectAndDontRequeueException(e);
    }
  }

  /**
   * A listener for GMC details being provided by a trainee.
   *
   * @param event The event containing trainee provided GMC details.
   */
  @RabbitListener(queues = "${app.rabbit.trainee.queue.gmc-details.provided}", ackMode = "AUTO")
  public void receiveGmcDetailsProvidedMessage(GmcDetailsProvidedEvent event) {
    Set<ConstraintViolation<GmcDetailsProvidedEvent>> violations = validator.validate(event,
        TraineeUpdate.class);

    if (!violations.isEmpty()) {
      List<String> violationMessages = violations.stream()
          .sorted(Comparator.comparing(v -> v.getPropertyPath().toString()))
          .map(v -> v.getPropertyPath() + " " + v.getMessage())
          .collect(Collectors.toList());
      throw new AmqpRejectAndDontRequeueException(
          "Invalid GMC details provided event: " + violationMessages);
    }

    Long personId = event.getPersonId();
    LOG.info("Received GMC details provided event for id [{}]", personId);

    GmcDetailsDTO gmcDetails = event.getGmcDetails();
    gmcDetails.setId(personId);

    GmcDetailsDTO existingGmcDetails = gmcDetailsService.findOne(event.getPersonId());
    if (existingGmcDetails != null) {
      gmcDetails.setAmendedDate(existingGmcDetails.getAmendedDate());
    }

    try {
      // Keep the validation as what it is, so use Create mode here
      gmcDetailsValidator.validate(gmcDetails, null, Create.class);
    } catch (MethodArgumentNotValidException e) {
      throw new AmqpRejectAndDontRequeueException("Invalid GMC details.", e);
    }

    LOG.info("Saving provided GMC details for id [{}]", personId);
    gmcDetailsService.save(gmcDetails);
  }
}
