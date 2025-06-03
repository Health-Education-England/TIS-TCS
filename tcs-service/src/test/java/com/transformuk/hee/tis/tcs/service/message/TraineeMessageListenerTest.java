package com.transformuk.hee.tis.tcs.service.message;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.validation.Create;
import com.transformuk.hee.tis.tcs.api.enumeration.GoldGuideVersion;
import com.transformuk.hee.tis.tcs.service.api.validation.GmcDetailsValidator;
import com.transformuk.hee.tis.tcs.service.event.ConditionsOfJoiningSignedEvent;
import com.transformuk.hee.tis.tcs.service.event.GmcDetailsProvidedEvent;
import com.transformuk.hee.tis.tcs.service.service.ConditionsOfJoiningService;
import com.transformuk.hee.tis.tcs.service.service.GmcDetailsService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.web.bind.MethodArgumentNotValidException;

class TraineeMessageListenerTest {

  private static final Long CURRICULUM_MEMBERSHIP_ID = 40L;
  private static final UUID PROGRAMME_MEMBERSHIP_ID = UUID.randomUUID();
  private static final Instant SIGNED_AT = Instant.now();

  private TraineeMessageListener listener;
  private ConditionsOfJoiningService cojService;
  private GmcDetailsService gmcDetailsService;
  private GmcDetailsValidator gmcDetailsValidator;
  private ConditionsOfJoiningDto dto;

  @BeforeEach
  void setUp() {
    cojService = mock(ConditionsOfJoiningService.class);
    gmcDetailsService = mock(GmcDetailsService.class);
    gmcDetailsValidator = mock(GmcDetailsValidator.class);
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    listener = new TraineeMessageListener(cojService, gmcDetailsService, gmcDetailsValidator,
        validator);
    dto = new ConditionsOfJoiningDto();
    dto.setSignedAt(SIGNED_AT);
    dto.setVersion(GoldGuideVersion.GG9);
  }

  @Test
  void shouldSaveSignedCojWhenEventValidWithCmId() {
    ConditionsOfJoiningSignedEvent event = new ConditionsOfJoiningSignedEvent(
        CURRICULUM_MEMBERSHIP_ID, dto);

    listener.receiveCojSignedMessage(event);

    verify(cojService).save(CURRICULUM_MEMBERSHIP_ID, dto);
  }

  @Test
  void shouldSaveSignedCojWhenEventValidWithPmId() {
    ConditionsOfJoiningSignedEvent event = new ConditionsOfJoiningSignedEvent(
        PROGRAMME_MEMBERSHIP_ID, dto);

    listener.receiveCojSignedMessage(event);

    verify(cojService).save(PROGRAMME_MEMBERSHIP_ID, dto);
  }

  @Test
  void shouldNotRequeueMessageWhenEventArgumentsInvalid() {
    ConditionsOfJoiningSignedEvent event = new ConditionsOfJoiningSignedEvent(
        CURRICULUM_MEMBERSHIP_ID, dto);

    when(cojService.save(CURRICULUM_MEMBERSHIP_ID, dto)).thenThrow(IllegalArgumentException.class);

    assertThrows(AmqpRejectAndDontRequeueException.class,
        () -> listener.receiveCojSignedMessage(event));
  }

  @Test
  void shouldNotSaveProvidedGmcDetailsWhenNoPersonId() {
    GmcDetailsDTO gmcDetails = new GmcDetailsDTO();
    gmcDetails.setGmcNumber("1234567");
    gmcDetails.setGmcStatus("gmcStatus");

    GmcDetailsProvidedEvent event = new GmcDetailsProvidedEvent(null, gmcDetails);

    assertThrows(AmqpRejectAndDontRequeueException.class,
        () -> listener.receiveGmcDetailsProvidedMessage(event));

    verifyNoInteractions(gmcDetailsService);
  }

  @Test
  void shouldNotSaveProvidedGmcDetailsWhenNoGmcDetails() {
    GmcDetailsProvidedEvent event = new GmcDetailsProvidedEvent(40L, null);

    assertThrows(AmqpRejectAndDontRequeueException.class,
        () -> listener.receiveGmcDetailsProvidedMessage(event));

    verifyNoInteractions(gmcDetailsService);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldNotSaveProvidedGmcDetailsWhenNoGmcNumber(String gmcNumber) {
    GmcDetailsDTO gmcDetails = new GmcDetailsDTO();
    gmcDetails.setGmcNumber(gmcNumber);
    gmcDetails.setGmcStatus("gmcStatus");

    GmcDetailsProvidedEvent event = new GmcDetailsProvidedEvent(40L, gmcDetails);

    assertThrows(AmqpRejectAndDontRequeueException.class,
        () -> listener.receiveGmcDetailsProvidedMessage(event));

    verifyNoInteractions(gmcDetailsService);
  }

  @ParameterizedTest
  @NullAndEmptySource
  void shouldNotSaveProvidedGmcDetailsWhenNoGmcStatus(String gmcStatus) {
    GmcDetailsDTO gmcDetails = new GmcDetailsDTO();
    gmcDetails.setGmcNumber("1234567");
    gmcDetails.setGmcStatus(gmcStatus);

    GmcDetailsProvidedEvent event = new GmcDetailsProvidedEvent(40L, gmcDetails);

    assertThrows(AmqpRejectAndDontRequeueException.class,
        () -> listener.receiveGmcDetailsProvidedMessage(event));

    verifyNoInteractions(gmcDetailsService);
  }

  @Test
  void shouldNotSaveProvidedGmcDetailsWhenInvalid() throws MethodArgumentNotValidException {
    GmcDetailsDTO gmcDetails = new GmcDetailsDTO();
    gmcDetails.setGmcNumber("1234567");
    gmcDetails.setGmcStatus("Registered");

    GmcDetailsProvidedEvent event = new GmcDetailsProvidedEvent(40L, gmcDetails);

    doThrow(MethodArgumentNotValidException.class).when(gmcDetailsValidator).validate(any(),
        eq(null), eq(Create.class));

    assertThrows(AmqpRejectAndDontRequeueException.class,
        () -> listener.receiveGmcDetailsProvidedMessage(event));

    verify(gmcDetailsService).findOne(any());
    verifyNoMoreInteractions(gmcDetailsService);
  }

  @Test
  void shouldSaveProvidedGmcDetailsWhenNoExistingGmcDetails() {
    GmcDetailsDTO gmcDetails = new GmcDetailsDTO();
    gmcDetails.setGmcNumber("1234567");
    gmcDetails.setGmcStatus("Registered");

    GmcDetailsProvidedEvent event = new GmcDetailsProvidedEvent(40L, gmcDetails);

    listener.receiveGmcDetailsProvidedMessage(event);

    ArgumentCaptor<GmcDetailsDTO> detailsCaptor = ArgumentCaptor.forClass(GmcDetailsDTO.class);
    verify(gmcDetailsService).save(detailsCaptor.capture());

    GmcDetailsDTO savedDto = detailsCaptor.getValue();

    assertThat("Unexpected person ID.", savedDto.getId(), is(40L));
    assertThat("Unexpected GMC number.", savedDto.getGmcNumber(), is("1234567"));
    assertThat("Unexpected GMC status.", savedDto.getGmcStatus(), is("Registered"));
    assertThat("Unexpected GMC start date.", savedDto.getGmcStartDate(), nullValue());
    assertThat("Unexpected GMC end date.", savedDto.getGmcEndDate(), nullValue());
    assertThat("Unexpected amended date.", savedDto.getAmendedDate(), nullValue());
  }

  @Test
  void shouldSaveProvidedGmcDetailsWhenExistingGmcDetails() {
    GmcDetailsDTO gmcDetails = new GmcDetailsDTO();
    gmcDetails.setGmcNumber("1234567");
    gmcDetails.setGmcStatus("Registered");

    GmcDetailsProvidedEvent event = new GmcDetailsProvidedEvent(40L, gmcDetails);

    LocalDateTime now = LocalDateTime.now();
    GmcDetailsDTO existingGmcDetails = new GmcDetailsDTO();
    existingGmcDetails.setAmendedDate(now);
    when(gmcDetailsService.findOne(40L)).thenReturn(existingGmcDetails);

    listener.receiveGmcDetailsProvidedMessage(event);

    ArgumentCaptor<GmcDetailsDTO> detailsCaptor = ArgumentCaptor.forClass(GmcDetailsDTO.class);
    verify(gmcDetailsService).save(detailsCaptor.capture());

    GmcDetailsDTO savedDto = detailsCaptor.getValue();

    assertThat("Unexpected person ID.", savedDto.getId(), is(40L));
    assertThat("Unexpected GMC number.", savedDto.getGmcNumber(), is("1234567"));
    assertThat("Unexpected GMC status.", savedDto.getGmcStatus(), is("Registered"));
    assertThat("Unexpected GMC start date.", savedDto.getGmcStartDate(), nullValue());
    assertThat("Unexpected GMC end date.", savedDto.getGmcEndDate(), nullValue());
    assertThat("Unexpected amended date.", savedDto.getAmendedDate(), is(now));
  }
}
