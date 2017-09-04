package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.*;
import com.transformuk.hee.tis.tcs.service.model.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper for the entity TrainingNumber and its DTO TrainingNumberDTO.
 */
@Component
public class TrainingNumberMapper {

  public TrainingNumberDTO trainingNumberToTrainingNumberDTO(TrainingNumber trainingNumber){
    TrainingNumberDTO result = null;
    if(trainingNumber != null) {
      result = new TrainingNumberDTO();
      result.setId(trainingNumber.getId());
      result.setTrainingNumberType(trainingNumber.getTrainingNumberType());
      result.setTypeOfContract(trainingNumber.getTypeOfContract());
      result.setNumber(trainingNumber.getNumber());
      result.setAppointmentYear(trainingNumber.getAppointmentYear());
      result.setSuffix(trainingNumber.getSuffix());
    }

    result.setProgrammes(programmeToProgrammeDTO(trainingNumber.getProgrammeId()));

    return result;
  }

  public List<TrainingNumber> trainingNumberDTOsToTrainingNumbers(List<TrainingNumberDTO> trainingNumberDTOs){
   List<TrainingNumber> result = Lists.newArrayList();

   for (TrainingNumberDTO trainingNumberDTO : trainingNumberDTOs){
     result.add(trainingNumberDTOToTrainingNumber(trainingNumberDTO));
   }
   return result;
  }

  public List<TrainingNumberDTO> trainingNumbersToTrainingNumberDTOs(List<TrainingNumber> trainingNumbers){
    List<TrainingNumberDTO> result = Lists.newArrayList();

    for (TrainingNumber trainingNumber: trainingNumbers){
      result.add(trainingNumberToTrainingNumberDTO(trainingNumber));
    }
    return result;
  }

  ProgrammeDTO programmeToProgrammeDTO(Programme programme){
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

  public TrainingNumber trainingNumberDTOToTrainingNumber(TrainingNumberDTO trainingNumberDTO){
    TrainingNumber result = null;
    if (trainingNumberDTO != null) {
      result = new TrainingNumber();
      result.setId(trainingNumberDTO.getId());
      result.setTrainingNumberType(trainingNumberDTO.getTrainingNumberType());
      result.setTypeOfContract(trainingNumberDTO.getTypeOfContract());
      result.setNumber(trainingNumberDTO.getNumber());
      result.setAppointmentYear(trainingNumberDTO.getAppointmentYear());
      result.setSuffix(trainingNumberDTO.getSuffix());
    }

    result.setProgrammeId(programmeDTOToProgramme(trainingNumberDTO.getProgrammes()));

    return result;
  }

  Programme programmeDTOToProgramme(ProgrammeDTO programmeDTO){
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

}
