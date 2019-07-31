package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Programme and its DTO ProgrammeDTO.
 */
@Mapper(componentModel = "spring", uses = {CurriculumMapper.class, TrainingNumberMapper.class})
public interface ProgrammeMapper {

  ProgrammeDTO programmeToProgrammeDTO(Programme programme);

  List<ProgrammeDTO> programmesToProgrammeDTOs(List<Programme> programmes);

  Programme programmeDTOToProgramme(ProgrammeDTO programmeDTO);

  List<Programme> programmeDTOsToProgrammes(List<ProgrammeDTO> programmeDTOs);

  default Curriculum corriculumFromId(Long id) {
    if (id == null) {
      return null;
    }
    Curriculum curriculum = new Curriculum();
    curriculum.setId(id);
    return curriculum;
  }
}
