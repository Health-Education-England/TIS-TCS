package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.mockito.Mockito.when;

import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import com.transformuk.hee.tis.tcs.service.model.TrainerApproval;
import com.transformuk.hee.tis.tcs.service.repository.TrainerApprovalRepository;
import com.transformuk.hee.tis.tcs.service.service.mapper.TrainerApprovalMapper;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Example;

@RunWith(MockitoJUnitRunner.class)
public class TrainerApprovalServiceImplTest {
  public static final long TRAINER_APPROVAL_ID = 1L;
  private static final Long PERSON_ID = 12345L;

  @InjectMocks
  private TrainerApprovalServiceImpl testObject;

  @Mock
  private TrainerApprovalRepository trainerApprovalRepository;
  @Mock
  private TrainerApprovalMapper trainerApprovalMapper;

  @Captor
  private ArgumentCaptor<Example<TrainerApproval>> exampleArgumentCaptor;
  @Captor
  private ArgumentCaptor<Converter> converterArgumentCaptor;

  @Before
  public void setup() {
    this.testObject = new TrainerApprovalServiceImpl(trainerApprovalRepository, trainerApprovalMapper);
  }

  @Test
  public void findPersonTrainerApprovalShouldCallRepositoryWithExampleOfPersonId() {
    TrainerApproval trainerApproval1  = new TrainerApproval(), trainerApproval2 = new TrainerApproval();
    List<TrainerApproval> foundApprovals = Lists.newArrayList(trainerApproval1, trainerApproval2);

    TrainerApprovalDTO trainerApprovalDTO1 = new TrainerApprovalDTO(), trainerApprovalDTO2 = new TrainerApprovalDTO();
    List<TrainerApprovalDTO> foundTrainerApprovalDTOs = Lists.newArrayList(trainerApprovalDTO1, trainerApprovalDTO2);

    when(trainerApprovalRepository.findAll(exampleArgumentCaptor.capture()))
      .thenReturn(foundApprovals);
    when(trainerApprovalMapper.toDTOs(foundApprovals)).thenReturn(foundTrainerApprovalDTOs);

    List<TrainerApprovalDTO> result = testObject.findTrainerApprovalsByPersonId(PERSON_ID);

    Assert.assertEquals(foundTrainerApprovalDTOs.size(), result.size());
    Assert.assertEquals(foundTrainerApprovalDTOs.get(0), result.get(0));
    Assert.assertEquals(foundTrainerApprovalDTOs.get(1), result.get(1));

    Example<TrainerApproval> exampleArgumentCaptorValue = exampleArgumentCaptor.getValue();
    TrainerApproval capturedTrainerApproval = exampleArgumentCaptorValue.getProbe();

    Assert.assertEquals(PERSON_ID, capturedTrainerApproval.getPerson().getId());
  }

  @Test
  public void findOneShouldReturnTheDtoOfTheFoundEntityOfTrainerApproval() {
    TrainerApproval foundEntity = new TrainerApproval();
    foundEntity.id(TRAINER_APPROVAL_ID);
    TrainerApprovalDTO dtoOfEntity = new TrainerApprovalDTO();

    when(trainerApprovalRepository.findById(TRAINER_APPROVAL_ID)).thenReturn(Optional.of(foundEntity));
    when(trainerApprovalMapper.toDto(foundEntity)).thenReturn(dtoOfEntity);

    TrainerApprovalDTO result = testObject.findOne(TRAINER_APPROVAL_ID);

    Assert.assertEquals(dtoOfEntity, result);
  }

  @Test
  public void saveActionShouldConvertSingleDtoAndSaveEntity() {
    TrainerApprovalDTO unsavedDto = new TrainerApprovalDTO(), savedDto = new TrainerApprovalDTO();
    TrainerApproval unsavedEntity = new TrainerApproval(), savedEntity = new TrainerApproval();

    when(trainerApprovalMapper.toEntity(unsavedDto)).thenReturn(unsavedEntity);
    when(trainerApprovalRepository.saveAndFlush(unsavedEntity)).thenReturn(savedEntity);
    when(trainerApprovalMapper.toDto(savedEntity)).thenReturn(savedDto);

    TrainerApprovalDTO result = testObject.save(unsavedDto);

    Assert.assertSame(savedDto, result);
  }
}
