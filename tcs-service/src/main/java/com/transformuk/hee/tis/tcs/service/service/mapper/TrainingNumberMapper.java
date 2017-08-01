package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity TrainingNumber and its DTO TrainingNumberDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TrainingNumberMapper {

  TrainingNumberDTO trainingNumberToTrainingNumberDTO(TrainingNumber trainingNumber);

  List<TrainingNumberDTO> trainingNumbersToTrainingNumberDTOs(List<TrainingNumber> trainingNumbers);

  TrainingNumber trainingNumberDTOToTrainingNumber(TrainingNumberDTO trainingNumberDTO);

  List<TrainingNumber> trainingNumberDTOsToTrainingNumbers(List<TrainingNumberDTO> trainingNumberDTOs);
}
