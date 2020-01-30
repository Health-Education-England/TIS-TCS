package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.service.model.Absence;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AbsenceMapper extends EntityMapper<AbsenceDTO, Absence> {

  @Override
  @Mappings(@Mapping(source = "personId", target = "person.id"))
  Absence toEntity(AbsenceDTO dto);

  @Override
  @Mappings(@Mapping(source = "person.id", target = "personId"))
  AbsenceDTO toDto(Absence entity);
}
