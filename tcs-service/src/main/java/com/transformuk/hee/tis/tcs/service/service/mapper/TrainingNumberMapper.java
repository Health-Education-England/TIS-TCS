package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.*;
import com.transformuk.hee.tis.tcs.service.model.*;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity TrainingNumber and its DTO TrainingNumberDTO.
 */
@Mapper(componentModel = "spring", uses = {ProgrammeMapper.class,CurriculumMapper.class})
public interface TrainingNumberMapper {

  TrainingNumberDTO trainingNumberToTrainingNumberDTO(TrainingNumber trainingNumber);

  List<TrainingNumber> trainingNumberDTOsToTrainingNumbers(List<TrainingNumberDTO> trainingNumberDTOs);

  List<TrainingNumberDTO> trainingNumbersToTrainingNumberDTOs(List<TrainingNumber> trainingNumbers);

  TrainingNumber trainingNumberDTOToTrainingNumber(TrainingNumberDTO trainingNumberDTO);

  default Programme programmeFromId(Long id){
    if(id == null){
      return null;
    }
    Programme programme = new Programme();
    programme.setId(id);
    return programme;
  }
}
