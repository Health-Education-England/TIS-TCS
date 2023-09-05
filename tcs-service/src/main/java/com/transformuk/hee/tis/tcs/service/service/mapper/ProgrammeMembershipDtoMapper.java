package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ProgrammeMembershipDTO;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * Mapper for copying fields from a programme membership source dto to the target dto.
 */
@Mapper(componentModel = "spring") //, uses = {RotationDtoMapper.class})
public interface ProgrammeMembershipDtoMapper {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  @Mapping(target = "rotation", expression = "java(mapRotationDto(source.getRotation(), target.getRotation()))")
  void copyIfNotNull(ProgrammeMembershipDTO source, @MappingTarget ProgrammeMembershipDTO target);

  default RotationDTO mapRotationDto(RotationDTO sourceDto, RotationDTO targetDto) {
    if (sourceDto == null || StringUtils.isEmpty(sourceDto.getName())) {
      return targetDto;
    }
    targetDto = new RotationDTO();
    targetDto.setName(sourceDto.getName());
    return targetDto;
  }
}
