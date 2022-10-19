package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.ConnectionInfoDto;
import com.transformuk.hee.tis.tcs.api.dto.RevalidationRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ConnectionInfoToRevalidationRecordMapper {

  @Mapping(target = "tisPersonId", source = "tcsPersonId")
  @Mapping(target = "gmcNumber", source = "gmcReferenceNumber")
  @Mapping(target = "forenames", source = "doctorFirstName")
  @Mapping(target = "surname", source = "doctorLastName")
  RevalidationRecordDto toRevalidationRecord(ConnectionInfoDto connectionInfoDto);
}
