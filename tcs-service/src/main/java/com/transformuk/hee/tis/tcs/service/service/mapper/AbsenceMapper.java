package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.AbsenceDTO;
import com.transformuk.hee.tis.tcs.service.model.Absence;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface AbsenceMapper extends EntityMapper<AbsenceDTO, Absence> {

  @Override
  @Mappings(@Mapping(source = "personId", target = "person.id"))
  Absence toEntity(AbsenceDTO dto);

  @Override
  @Mappings(@Mapping(source = "person.id", target = "personId"))
  AbsenceDTO toDto(Absence entity);

  default void patch(Map<String, Object> source, @MappingTarget Absence target) {

    target.setAbsenceAttendanceId(
        (String) source.getOrDefault("absenceAttendanceId", target.getAbsenceAttendanceId()));

    if (source.containsKey("durationInDays")) {
      int durationInDays = (int) source.get("durationInDays");
      target.setDurationInDays((long) durationInDays);
    }

    if (source.containsKey("startDate")) {
      String startDate = (String) source.get("startDate");
      target.setStartDate(LocalDate.parse(startDate));
    }

    if (source.containsKey("endDate")) {
      String endDate = (String) source.get("endDate");
      target.setEndDate(LocalDate.parse(endDate));
    }

    if (source.containsKey("amendedDate")) {
      String amendedDate = (String) source.get("amendedDate");
      target.setAmendedDate(LocalDateTime.parse(amendedDate));
    }
  }
}
