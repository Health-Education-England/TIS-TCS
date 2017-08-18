package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity PersonalDetails and its DTO PersonalDetailsDTO.
 */
@Mapper(componentModel = "spring")
public interface PersonalDetailsMapper extends EntityMapper<PersonalDetailsDTO, PersonalDetails> {


  default PersonalDetails fromId(Long id) {
    if (id == null) {
      return null;
    }
    PersonalDetails personalDetails = new PersonalDetails();
    personalDetails.setId(id);
    return personalDetails;
  }
}
