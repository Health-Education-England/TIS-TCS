package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrainerApprovalService {

  TrainerApprovalDTO save(TrainerApprovalDTO trainerApprovalDTO);
  List<TrainerApprovalDTO> save(List<TrainerApprovalDTO> trainerApprovalDTOS);
  Page<TrainerApprovalDTO> findAll(Pageable pageable);
  TrainerApprovalDTO findOne(Long id);
  List<TrainerApprovalDTO> findTrainerApprovalsByPersonId(Long personId);
  void delete(Long id);

}
