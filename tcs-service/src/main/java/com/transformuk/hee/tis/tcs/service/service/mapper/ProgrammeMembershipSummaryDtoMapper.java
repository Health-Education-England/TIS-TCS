package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * This mapper is used to convert the detailed programme membership data into a summary form
 * by extracting specific fields such as the programme name and start date.
 */
@Mapper(componentModel = "spring")
public interface ProgrammeMembershipSummaryDtoMapper {
  ProgrammeMembershipSummaryDtoMapper INSTANCE =
      Mappers.getMapper(ProgrammeMembershipSummaryDtoMapper.class);

  @Mapping(source = "id", target = "programmeMembershipUuid")
  @Mapping(source = "programmeMembershipDto.programmeName", target = "programmeName")
  @Mapping(source = "programmeMembershipDto.programmeStartDate", target = "programmeStartDate")
  ProgrammeMembershipSummaryDTO toSummaryDTO(
      String id, ProgrammeMembershipDTO programmeMembershipDto);
}
