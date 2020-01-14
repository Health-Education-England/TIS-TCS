package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.transformuk.hee.tis.tcs.api.dto.PersonalDetailsDTO;
import com.transformuk.hee.tis.tcs.service.model.PersonalDetails;
import java.util.Optional;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity PersonalDetails and its DTO PersonalDetailsDTO.
 */
@Mapper(componentModel = "spring")
public abstract class PersonalDetailsMapper implements
    EntityMapper<PersonalDetailsDTO, PersonalDetails> {


  public PersonalDetails fromId(Long id) {
    if (id == null) {
      return null;
    }
    PersonalDetails personalDetails = new PersonalDetails();
    personalDetails.setId(id);
    return personalDetails;
  }

  public Optional<PersonalDetails> toPatchedEntity(PersonalDetails originalPersonDetail,
      PersonalDetailsDTO personalDetailsDTO) {
    if (originalPersonDetail == null || personalDetailsDTO == null) {
      return Optional.empty();
    } else {

      if (personalDetailsDTO.getSexualOrientation() != null) {
        originalPersonDetail.setSexualOrientation(personalDetailsDTO.getSexualOrientation());
      }
      if (personalDetailsDTO.getReligiousBelief() != null) {
        originalPersonDetail.setReligiousBelief(personalDetailsDTO.getReligiousBelief());
      }
      if (personalDetailsDTO.getNationality() != null) {
        originalPersonDetail.setNationality(personalDetailsDTO.getNationality());
      }
      if (personalDetailsDTO.getNationalInsuranceNumber() != null) {
        originalPersonDetail
            .setNationalInsuranceNumber(personalDetailsDTO.getNationalInsuranceNumber());
      }
      if (personalDetailsDTO.getMaritalStatus() != null) {
        originalPersonDetail.setMaritalStatus(personalDetailsDTO.getMaritalStatus());
      }
      if (personalDetailsDTO.getGender() != null) {
        originalPersonDetail.setGender(personalDetailsDTO.getGender());
      }
      if (personalDetailsDTO.getEthnicOrigin() != null) {
        originalPersonDetail.setEthnicOrigin(personalDetailsDTO.getEthnicOrigin());
      }
      if (personalDetailsDTO.getDualNationality() != null) {
        originalPersonDetail.setDualNationality(personalDetailsDTO.getDualNationality());
      }
      if (personalDetailsDTO.getDisability() != null) {
        originalPersonDetail.setDisability(personalDetailsDTO.getDisability());
      }
      if (personalDetailsDTO.getDisabilityDetails() != null) {
        originalPersonDetail.setDisabilityDetails(personalDetailsDTO.getDisabilityDetails());
      }
      if (personalDetailsDTO.getDateOfBirth() != null) {
        originalPersonDetail.setDateOfBirth(personalDetailsDTO.getDateOfBirth());
      }
      if (personalDetailsDTO.getAmendedDate() != null) {
        originalPersonDetail.setAmendedDate(personalDetailsDTO.getAmendedDate());
      }

      return Optional.ofNullable(originalPersonDetail);
    }
  }

}
