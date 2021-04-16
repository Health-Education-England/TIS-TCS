package com.transformuk.hee.tis.tcs.message;

import com.transformuk.hee.tis.tcs.api.dto.ConnectionInfoDto;
import com.transformuk.hee.tis.tcs.service.message.RevalidationMessageListener;
import com.transformuk.hee.tis.tcs.service.service.RevalidationService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RevalidationMessageListenerTest {

  private List<ConnectionInfoDto> connections;

  @Mock
  RabbitTemplate rabbitTemplate;

  @Mock
  RevalidationService revalidationService;

  @InjectMocks
  RevalidationMessageListener revalidationMessageListener;

  @BeforeAll
  void setup() {
    MockitoAnnotations.initMocks(this);

    ConnectionInfoDto connectionInfoDto = ConnectionInfoDto.builder()
        .tcsPersonId((long) 111)
        .gmcReferenceNumber("123")
        .doctorFirstName("first")
        .doctorLastName("last")
        .submissionDate(LocalDate.now())
        .programmeName("programme")
        .programmeMembershipType("visitor")
        .designatedBody("body")
        .tcsDesignatedBody("tcsbody")
        .programmeOwner("owner")
        .connectionStatus("Yes")
        .programmeMembershipStartDate(LocalDate.now().minusDays(100))
        .programmeMembershipEndDate(LocalDate.now().plusDays(100))
        .dataSource("source")
        .build();
    connections = new ArrayList<ConnectionInfoDto>();
    connections.add(connectionInfoDto);

  }

  @Test
  public void shouldExtractInfo() {
    MockitoAnnotations.initMocks(this);

    when(revalidationService.extractConnectionInfoForSync()).thenReturn(new ArrayList<ConnectionInfoDto>());

    revalidationMessageListener.receiveMessage("message");
    verify(revalidationService.extractConnectionInfoForSync());
  }
}
