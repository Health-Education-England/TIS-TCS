package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.*;
import com.transformuk.hee.tis.tcs.service.model.*;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Mapper for the entity TrainingNumber and its DTO TrainingNumberDTO.
 */
@Component
public class TrainingNumberMapper {

  public TrainingNumberDTO trainingNumberToTrainingNumberDTO(TrainingNumber trainingNumber){
    TrainingNumberDTO result = null;
    if(trainingNumber != null){
      result = toDTO(trainingNumber);
    }
    return result;
  }

  public TrainingNumber trainingNumberDTOToTrainingNumber(TrainingNumberDTO trainingNumberDTO) {
    TrainingNumber result = toEntity(trainingNumberDTO);
    return result;
  }

  public List<TrainingNumber> trainingNumberDTOsToTrainingNumbers(List<TrainingNumberDTO> trainingNumberDTOs) {
    List<TrainingNumber> result =  Lists.newArrayList();

    for (TrainingNumberDTO trainingNumberDTO : trainingNumberDTOs) {
      result.add(toEntity(trainingNumberDTO));
    }

    return result;
  }

  public List<TrainingNumberDTO> trainingNumbersToTrainingNumberDTOs(List<TrainingNumber> trainingNumbers) {
    List<TrainingNumberDTO> result =  Lists.newArrayList();

    for (TrainingNumber trainingNumber : trainingNumbers) {
      result.add(toDTO(trainingNumber));
    }

    return result;
  }

  private TrainingNumberDTO toDTO(TrainingNumber trainingNumber) {
    TrainingNumberDTO result = new TrainingNumberDTO();

    result.setId(trainingNumber.getId());
    result.setNumber(trainingNumber.getNumber());
    result.setAppointmentYear(trainingNumber.getAppointmentYear());
    result.setSuffix(trainingNumber.getSuffix());
    result.setTrainingNumberType(trainingNumber.getTrainingNumberType());
    result.setTypeOfContract(trainingNumber.getTypeOfContract());
    result.setProgramme(programmeToProgrammeDTO(trainingNumber.getProgramme()));
    return result;
  }

  private ProgrammeDTO programmeToProgrammeDTO(Programme programme) {
    ProgrammeDTO result = null;
    if (programme != null) {
      result = new ProgrammeDTO();
      result.setId(programme.getId());
      result.setIntrepidId(programme.getIntrepidId());
      result.setProgrammeNumber(programme.getProgrammeNumber());
      result.setProgrammeName(programme.getProgrammeName());
      result.setManagingDeanery(programme.getManagingDeanery());
      result.setStatus(programme.getStatus());
    }
    return result;
  }
  private Programme programmeDTOToProgramme(ProgrammeDTO programmeDTO) {
    Programme result = null;
    if (programmeDTO != null) {
      result = new Programme();

      result.setId(programmeDTO.getId());
      result.setIntrepidId(programmeDTO.getIntrepidId());
      result.setProgrammeNumber(programmeDTO.getProgrammeNumber());
      result.setProgrammeName(programmeDTO.getProgrammeName());
      result.setManagingDeanery(programmeDTO.getManagingDeanery());
      result.setStatus(programmeDTO.getStatus());
    }
    return result;
  }

  private TrainingNumber toEntity(TrainingNumberDTO trainingNumberDTO) {
    TrainingNumber result = new TrainingNumber();
    result.setId(trainingNumberDTO.getId());
    result.setNumber(trainingNumberDTO.getNumber());
    result.setAppointmentYear(trainingNumberDTO.getAppointmentYear());
    result.setSuffix(trainingNumberDTO.getSuffix());
    result.setTrainingNumberType(trainingNumberDTO.getTrainingNumberType());
    result.setTypeOfContract(trainingNumberDTO.getTypeOfContract());
    result.setProgramme(programmeDTOToProgramme(trainingNumberDTO.getProgramme()));
    return result;
  }

}
