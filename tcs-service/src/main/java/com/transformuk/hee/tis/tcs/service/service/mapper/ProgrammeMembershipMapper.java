package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.service.model.ProgrammeMembership;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * Mapper for the entity ProgrammeMembership and its DTO ProgrammeMembershipDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProgrammeMembershipMapper {

  ProgrammeMembershipDTO programmeMembershipToProgrammeMembershipDTO(ProgrammeMembership programmeMembership);

  List<ProgrammeMembershipDTO> programmeMembershipsToProgrammeMembershipDTOs(List<ProgrammeMembership> programmeMemberships);

  ProgrammeMembership programmeMembershipDTOToProgrammeMembership(ProgrammeMembershipDTO programmeMembershipDTO);

  List<ProgrammeMembership> programmeMembershipDTOsToProgrammeMemberships(List<ProgrammeMembershipDTO> programmeMembershipDTOs);
}
