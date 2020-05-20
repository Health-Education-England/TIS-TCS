package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.QualificationDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Qualification;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Qualification and its DTO QualificationDTO.
 */
@Mapper(componentModel = "spring")
public interface QualificationMapper {

  QualificationDTO toDto(Qualification qualification);

  Qualification toEntity(QualificationDTO qualificationDto);

  List<Qualification> toEntities(List<QualificationDTO> qualificationDtos);

  List<QualificationDTO> toDTOs(List<Qualification> qualifications);

  // TODO: Look at other options to avoid cyclic mapping such as using @Context.
  @Mapping(target = "programmeMemberships", ignore = true)
  @Mapping(target = "qualifications", ignore = true)
  @Mapping(target = "trainerApprovals", ignore = true)
  PersonDTO personToPersonDto(Person person);

  // TODO: Look at other options to avoid cyclic mapping such as using @Context.
  @Mapping(target = "programmeMemberships", ignore = true)
  @Mapping(target = "qualifications", ignore = true)
  @Mapping(target = "trainerApprovals", ignore = true)
  Person personDtoToPerson(PersonDTO personDto);
}
