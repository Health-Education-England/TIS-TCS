package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.ConditionsOfJoiningDto;
import com.transformuk.hee.tis.tcs.service.model.ConditionsOfJoining;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * A mapper to convert between DTO and Entity representations of Conditions of Joining.
 */
@Mapper(componentModel = "spring")
public interface ConditionsOfJoiningMapper {

  ConditionsOfJoiningDto toDto(ConditionsOfJoining entity);

  ConditionsOfJoining toEntity(ConditionsOfJoiningDto dto);

  /**
   * Convert a list of Conditions of Joinings to their equivalent Dtos.
   *
   * @param conditionsOfJoinings the list of Conditions of Joining
   * @return a list of Conditions of Joining Dtos
   */
  default List<ConditionsOfJoiningDto> allEntityToDto(
      List<ConditionsOfJoining> conditionsOfJoinings) {
    List<ConditionsOfJoiningDto> result = Lists.newArrayList();
    for (ConditionsOfJoining conditionsOfJoining : conditionsOfJoinings) {
      result.add(toDto(conditionsOfJoining));
    }
    return result;
  }
}
