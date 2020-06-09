package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = {ContactDetailsDtoMapper.class,
    PersonalDetailsDtoMapper.class, GdcDetailsDtoMapper.class, GmcDetailsDtoMapper.class,
    RightToWorkDtoMapper.class})
public interface PersonDtoMapper {

  @Mappings({
      @Mapping(target = "trainerApprovals", ignore = true),
      @Mapping(target = "qualifications", ignore = true),
      @Mapping(target = "programmeMemberships", ignore = true),
      @Mapping(target = "messageList", ignore = true)
  })
  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
      nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
  void copyIfNotNull(PersonDTO personDto, @MappingTarget PersonDTO personDtoTarget);
}
