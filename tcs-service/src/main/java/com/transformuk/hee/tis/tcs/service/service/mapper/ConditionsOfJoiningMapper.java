package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import org.mapstruct.Mapper;

/**
 * A mapper to convert between DTO and Entity representations of Conditions of Joining.
 */
@Mapper(componentModel = "spring")
public interface ConditionsOfJoiningMapper {

  ConditionsOfJoiningDto toDto(ConditionsOfJoining entity);

  ConditionsOfJoining toEntity(ConditionsOfJoiningDto dto);
}
