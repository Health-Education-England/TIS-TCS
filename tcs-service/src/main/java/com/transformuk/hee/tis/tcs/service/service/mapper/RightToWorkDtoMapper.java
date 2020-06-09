package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.RightToWorkDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface RightToWorkDtoMapper {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  void copyIfNotNull(RightToWorkDTO rightToWorkDto,
      @MappingTarget RightToWorkDTO rightToWorkDtoTarget);
}
