package com.transformuk.hee.tis.service.mapper;

import com.transformuk.hee.tis.domain.*;
import com.transformuk.hee.tis.service.dto.ProgrammeDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Programme and its DTO ProgrammeDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProgrammeMapper {

    ProgrammeDTO programmeToProgrammeDTO(Programme programme);

    List<ProgrammeDTO> programmesToProgrammeDTOs(List<Programme> programmes);

    @Mapping(target = "programmeMemberships", ignore = true)
    Programme programmeDTOToProgramme(ProgrammeDTO programmeDTO);

    List<Programme> programmeDTOsToProgrammes(List<ProgrammeDTO> programmeDTOs);
}
