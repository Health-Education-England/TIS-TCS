package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import org.springframework.stereotype.Component;

import java.util.List;

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
    return toEntity(trainingNumberDTO);
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
    if(trainingNumber.getProgramme() == null){
      result.setProgramme(null);
    }
    else {
      result.setProgramme(trainingNumber.getProgramme().getId());
    }
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
      result.setManagingDeanery(programme.getOwner());
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
      result.setOwner(programmeDTO.getManagingDeanery());
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
    result.setProgramme(programmeFromId(trainingNumberDTO.getProgramme()));
    return result;
  }


  private Programme programmeFromId(Long id) {
    if (id == null) {
      return null;
    }
    Programme programme = new Programme();
    programme.setId(id);
    return programme;
  }

}
