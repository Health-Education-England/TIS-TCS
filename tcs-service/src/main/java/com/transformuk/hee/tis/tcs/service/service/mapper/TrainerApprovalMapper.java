package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainerApprovalDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.TrainerApproval;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class TrainerApprovalMapper {

  public TrainerApprovalDTO toDto(TrainerApproval trainerApproval) {
    TrainerApprovalDTO result = null;
    if (trainerApproval != null) {
      result = trainerApprovalToTrainerApprovalDTO(trainerApproval);
    }
    return result;
  }

  public TrainerApproval toEntity(TrainerApprovalDTO trainerApprovalDTO) {
    return trainerApprovalDTOToTrainerApproval(trainerApprovalDTO);
  }

  public List<TrainerApproval> toEntities(List<TrainerApprovalDTO> trainerApprovalDTOs) {
    List<TrainerApproval> result = Lists.newArrayList();

    for (TrainerApprovalDTO trainerApprovalDTO : trainerApprovalDTOs) {
      result.add(toEntity(trainerApprovalDTO));
    }

    return result;
  }

  public List<TrainerApprovalDTO> toDTOs(List<TrainerApproval> trainerApprovals) {
    List<TrainerApprovalDTO> result = Lists.newArrayList();

    for (TrainerApproval trainerApproval : trainerApprovals) {
      result.add(toDto(trainerApproval));
    }

    return result;
  }

  private TrainerApprovalDTO trainerApprovalToTrainerApprovalDTO(TrainerApproval trainerApproval) {
    TrainerApprovalDTO result = new TrainerApprovalDTO();

    result.setId(trainerApproval.getId());
    result.setStartDate(trainerApproval.getStartDate());
    result.setEndDate(trainerApproval.getEndDate());
    result.setTrainerType(trainerApproval.getTrainerType());
    result.setApprovalStatus(trainerApproval.getApprovalStatus());

    if (trainerApproval.getPerson() == null) {
      result.setPerson(null);
    } else {
      result.setPerson(personToPersonDTO(trainerApproval.getPerson()));
    }
    return result;
  }

  private PersonDTO personToPersonDTO(Person person) {
    PersonDTO result = null;
    if (person != null) {
      result = new PersonDTO();
      result.setId(person.getId());
      result.setIntrepidId(person.getIntrepidId());
      result.setAddedDate(person.getAddedDate());
      result.setAmendedDate(person.getAmendedDate());
      result.setRole(person.getRole());
      result.setStatus(person.getStatus());
      result.setComments(person.getComments());
      result.setInactiveDate(person.getInactiveDate());
      result.setInactiveNotes(person.getInactiveNotes());
      result.setPublicHealthNumber(person.getPublicHealthNumber());
      result.setRegulator(person.getRegulator());
    }
    return result;
  }

  private Person personDTOToPerson(PersonDTO personDTO) {
    Person result = null;
    if (personDTO != null) {
      result = new Person();
      result.setId(personDTO.getId());
      result.setIntrepidId(personDTO.getIntrepidId());
      result.setAddedDate(personDTO.getAddedDate());
      result.setAmendedDate(personDTO.getAmendedDate());
      result.setRole(personDTO.getRole());
      result.setStatus(personDTO.getStatus());
      result.setComments(personDTO.getComments());
      result.setInactiveDate(personDTO.getInactiveDate());
      result.setInactiveNotes(personDTO.getInactiveNotes());
      result.setPublicHealthNumber(personDTO.getPublicHealthNumber());
      result.setRegulator(personDTO.getRegulator());

    }
    return result;
  }

  private TrainerApproval trainerApprovalDTOToTrainerApproval(TrainerApprovalDTO trainerApprovalDTO) {
    TrainerApproval result = new TrainerApproval();
    result.setId(trainerApprovalDTO.getId());
    result.setStartDate(trainerApprovalDTO.getStartDate());
    result.setEndDate(trainerApprovalDTO.getEndDate());
    result.setTrainerType(trainerApprovalDTO.getTrainerType());
    result.setApprovalStatus(trainerApprovalDTO.getApprovalStatus());

    if (trainerApprovalDTO.getPerson() == null) {
      result.setPerson(null);
    } else {
      result.setPerson(personDTOToPerson(trainerApprovalDTO.getPerson()));
    }
    return result;
  }
}
