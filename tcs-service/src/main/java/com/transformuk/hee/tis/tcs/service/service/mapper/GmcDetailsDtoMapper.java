package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.GmcDetailsDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GmcDetailsDtoMapper {

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  void copyIfNotNull(GmcDetailsDTO gmcDetailsDto, @MappingTarget GmcDetailsDTO gmcDetailsDtoTarget);
}
