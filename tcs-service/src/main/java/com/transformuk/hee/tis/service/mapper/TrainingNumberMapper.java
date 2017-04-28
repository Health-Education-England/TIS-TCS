package com.transformuk.hee.tis.service.mapper;

import com.transformuk.hee.tis.domain.TrainingNumber;
import com.transformuk.hee.tis.service.dto.TrainingNumberDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity TrainingNumber and its DTO TrainingNumberDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TrainingNumberMapper {

	TrainingNumberDTO trainingNumberToTrainingNumberDTO(TrainingNumber trainingNumber);

	List<TrainingNumberDTO> trainingNumbersToTrainingNumberDTOs(List<TrainingNumber> trainingNumbers);

	@Mapping(target = "programmeMemberships", ignore = true)
	TrainingNumber trainingNumberDTOToTrainingNumber(TrainingNumberDTO trainingNumberDTO);

	List<TrainingNumber> trainingNumberDTOsToTrainingNumbers(List<TrainingNumberDTO> trainingNumberDTOs);
}
