package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity Person and its DTO PersonDTO.
 */
@Mapper(componentModel = "spring", uses = {ContactDetailsMapper.class, PersonalDetailsMapper.class, GmcDetailsMapper.class, GdcDetailsMapper.class, QualificationMapper.class, RightToWorkMapper.class,ProgrammeMembershipMapper.class})
public interface PersonMapper extends EntityMapper<PersonDTO, Person> {
}
