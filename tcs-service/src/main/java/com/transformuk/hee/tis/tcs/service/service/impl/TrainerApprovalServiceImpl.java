package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Preconditions;
import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.TrainerApproval;
import com.transformuk.hee.tis.tcs.service.repository.TrainerApprovalRepository;
import com.transformuk.hee.tis.tcs.service.service.TrainerApprovalService;
import com.transformuk.hee.tis.tcs.service.service.mapper.TrainerApprovalMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TrainerApprovalServiceImpl implements TrainerApprovalService {

  private final Logger log = LoggerFactory.getLogger(TrainerApprovalServiceImpl.class);

  private final TrainerApprovalRepository trainerApprovalRepository;

  private final TrainerApprovalMapper trainerApprovalMapper;

  public TrainerApprovalServiceImpl(TrainerApprovalRepository trainerApprovalRepository,
    TrainerApprovalMapper trainerApprovalMapper){
    this.trainerApprovalRepository = trainerApprovalRepository;
    this.trainerApprovalMapper = trainerApprovalMapper;
  }

  public TrainerApprovalDTO save(TrainerApprovalDTO trainerApprovalDTO){
    log.debug("Request to save Trainer approval : {}", trainerApprovalDTO);
    TrainerApproval trainerApproval = trainerApprovalMapper.toEntity(trainerApprovalDTO);
    trainerApproval = trainerApprovalRepository.saveAndFlush(trainerApproval);
    return trainerApprovalMapper.toDto(trainerApproval);
  }

  public List<TrainerApprovalDTO> save(List<TrainerApprovalDTO> trainerApprovalDTOS){
    log.debug("Request to save Trainer approvals : {}", trainerApprovalDTOS);
    List<TrainerApproval> trainerApprovals = trainerApprovalMapper.toEntities(trainerApprovalDTOS);
    trainerApprovals = trainerApprovalRepository.saveAll(trainerApprovals);
    return trainerApprovalMapper.toDTOs(trainerApprovals);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TrainerApprovalDTO> findAll(Pageable pageable){
    log.debug("Request to get all Trainer approvals");
    return trainerApprovalRepository.findAll(pageable).map(trainerApprovalMapper::toDto);
  }

  @Override
  @Transactional(readOnly = true)
  public TrainerApprovalDTO findOne(Long id){
    log.debug("Request to get Trainer approval : {}", id);
    TrainerApproval trainerApproval = trainerApprovalRepository.findById(id).orElse(null);
    return trainerApprovalMapper.toDto(trainerApproval);
  }

  @Transactional(readOnly = true)
  @Override
  public List<TrainerApprovalDTO> findTrainerApprovalsByPersonId(Long personId) {
    Preconditions.checkNotNull(personId);
    Person person = new Person();
    person.setId(personId);
    TrainerApproval trainerApprovalExample = new TrainerApproval();
    trainerApprovalExample.setPerson(person);
    Example<TrainerApproval> example = Example.of(trainerApprovalExample);
    List<TrainerApproval> trainerApprovals = trainerApprovalRepository.findAll(example);

    return trainerApprovalMapper.toDTOs(trainerApprovals);
  }

  @Override
  public void delete(Long id) {
    log.debug("Request to delete Trainer Approval : {}", id);
    trainerApprovalRepository.deleteById(id);
  }

}
