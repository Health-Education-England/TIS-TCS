package com.transformuk.hee.tis.tcs.service.listener.person;

import static org.mockito.Mockito.verify;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.event.SpecialtySavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SpecialtyElasticSearchEventListenerTest {

  private static final Long SPECIALTY_ID = 123456L;

  @Mock
  private PersonElasticSearchService personElasticSearchService;

  @Mock
  private PostElasticSearchService postElasticSearchService;

  @InjectMocks
  private SpecialtyElasticSearchEventListener testObj;

  @Test
  void shouldHandleSpecialtySavedEvent() {
    SpecialtyDTO specialtyDto = new SpecialtyDTO();
    specialtyDto.setId(SPECIALTY_ID);
    SpecialtySavedEvent event = new SpecialtySavedEvent(specialtyDto);

    testObj.handleSpecialtySavedEvent(event);

    verify(personElasticSearchService).updatePersonDocumentForSpecialty(SPECIALTY_ID);
    verify(postElasticSearchService).updatePostDocumentsForSpecialty(SPECIALTY_ID);
  }
}
