package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ConnectionInfoDto;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for revalidation record objects.
 */
@Mapper(componentModel = "spring")
public interface RevalidationRecordMapper {

  @Mapping(target = "tisPersonId", source = "tcsPersonId")
  @Mapping(target = "gmcNumber", ignore = true)
  @Mapping(target = "forenames", source = "doctorFirstName")
  @Mapping(target = "surname", source = "doctorLastName")
  void toRevalidationRecord(ConnectionInfoDto source,
      @MappingTarget RevalidationRecordDto target);
}
