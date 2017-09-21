package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity Person and its DTO PersonDTO.
 */
@Mapper(componentModel = "spring", uses = {ContactDetailsMapper.class, PersonalDetailsMapper.class, GmcDetailsMapper.class, GdcDetailsMapper.class, QualificationMapper.class, RightToWorkMapper.class,})
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {

  PersonDTO toDto(Person person);

  @Mapping(source = "id", target = "contactDetails")

  @Mapping(source = "id", target = "personalDetails")

  @Mapping(source = "id", target = "gmcDetails")

  @Mapping(source = "id", target = "gdcDetails")

  @Mapping(source = "id", target = "rightToWork")
  Person toEntity(PersonDTO personDTO);

  default Person fromId(Long id) {
    if (id == null) {
      return null;
    }
    Person person = new Person();
    person.setId(id);
    return person;
  }
}
