package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity TrainingNumber and its DTO TrainingNumberDTO.
 */
@Mapper(componentModel = "spring", uses = {ProgrammeMapper.class})
public interface TrainingNumberMapper {

  @Mapping(target = "programmeId", source = "programme.id")
  @Mapping(target = "programme", ignore = true)
  TrainingNumberDTO trainingNumberToTrainingNumberDTO(TrainingNumber trainingNumber);

  @Mapping(target = "programme", source = "programmeId")
  TrainingNumber trainingNumberDTOToTrainingNumber(TrainingNumberDTO trainingNumberDto);

  List<TrainingNumber> trainingNumberDTOsToTrainingNumbers(
      List<TrainingNumberDTO> trainingNumberDtos);

  List<TrainingNumberDTO> trainingNumbersToTrainingNumberDTOs(List<TrainingNumber> trainingNumbers);
}
