package com.transformuk.hee.tis.service.mapper;

import com.transformuk.hee.tis.domain.Curriculum;
import com.transformuk.hee.tis.domain.Programme;
import com.transformuk.hee.tis.domain.ProgrammeMembership;
import com.transformuk.hee.tis.domain.TrainingNumber;
import com.transformuk.hee.tis.service.dto.ProgrammeMembershipDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity ProgrammeMembership and its DTO ProgrammeMembershipDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProgrammeMembershipMapper {

	@Mapping(source = "programme.id", target = "programmeId")
	@Mapping(source = "curriculum.id", target = "curriculumId")
	@Mapping(source = "trainingNumber.id", target = "trainingNumberId")
	ProgrammeMembershipDTO programmeMembershipToProgrammeMembershipDTO(ProgrammeMembership programmeMembership);

	List<ProgrammeMembershipDTO> programmeMembershipsToProgrammeMembershipDTOs(List<ProgrammeMembership> programmeMemberships);

	@Mapping(source = "programmeId", target = "programme")
	@Mapping(source = "curriculumId", target = "curriculum")
	@Mapping(source = "trainingNumberId", target = "trainingNumber")
	ProgrammeMembership programmeMembershipDTOToProgrammeMembership(ProgrammeMembershipDTO programmeMembershipDTO);

	List<ProgrammeMembership> programmeMembershipDTOsToProgrammeMemberships(List<ProgrammeMembershipDTO> programmeMembershipDTOs);

	default Programme programmeFromId(Long id) {
		if (id == null) {
			return null;
		}
		Programme programme = new Programme();
		programme.setId(id);
		return programme;
	}

	default Curriculum curriculumFromId(Long id) {
		if (id == null) {
			return null;
		}
		Curriculum curriculum = new Curriculum();
		curriculum.setId(id);
		return curriculum;
	}

	default TrainingNumber trainingNumberFromId(Long id) {
		if (id == null) {
			return null;
		}
		TrainingNumber trainingNumber = new TrainingNumber();
		trainingNumber.setId(id);
		return trainingNumber;
	}
}
