package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.service.model.Qualification;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Qualification and its DTO QualificationDTO.
 */
@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface QualificationMapper {

  QualificationDTO toDto(Qualification qualification);

  Qualification toEntity(QualificationDTO qualificationDto);

  List<Qualification> toEntities(List<QualificationDTO> qualificationDtos);

  List<QualificationDTO> toDTOs(List<Qualification> qualifications);
}
