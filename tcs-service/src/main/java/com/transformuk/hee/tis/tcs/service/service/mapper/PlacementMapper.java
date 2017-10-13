package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialty;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Mapper for the entity Placement and its DTO PlacementDTO.
 */
@Component
public class PlacementMapper {

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private PostRepository postRepository;

  @Autowired
  private SpecialtyRepository specialtyRepository;

  public PlacementDTO placementToPlacementDTO(Placement placement) {
    PlacementDTO placementDTO = null;
    if (placement != null) {
      placementDTO = new PlacementDTO();
      placementDTO.setId(placement.getId());
      placementDTO.setClinicalSupervisorId(placement.getClinicalSupervisor() != null ? placement.getClinicalSupervisor().getId() : null);
      placementDTO.setTraineeId(placement.getTrainee() != null ? placement.getTrainee().getId() : null);
      placementDTO.setPostId(placement.getPost() != null ? placement.getPost().getId() : null);
      placementDTO.setGradeId(placement.getGradeId());
      placementDTO.setSiteId(placement.getSiteId());
      placementDTO.setDateFrom(placement.getDateFrom());
      placementDTO.setDateTo(placement.getDateTo());
      placementDTO.setIntrepidId(placement.getIntrepidId());
      placementDTO.setLocalPostNumber(placement.getLocalPostNumber());
      placementDTO.setManagingLocalOffice(placement.getManagingLocalOffice());
      placementDTO.setPlacementType(placement.getPlacementType());
      placementDTO.setStatus(placement.getStatus());
      placementDTO.setTrainingDescription(placement.getTrainingDescription());
      placementDTO.setPlacementWholeTimeEquivalent(placement.getPlacementWholeTimeEquivalent());

      if (CollectionUtils.isNotEmpty(placement.getSpecialties())) {
        Set<PlacementSpecialtyDTO> specialties = Sets.newHashSet();
        for (PlacementSpecialty placementSpecialty : placement.getSpecialties()) {
          PlacementSpecialtyDTO placementSpecialtyDTO = new PlacementSpecialtyDTO();
          placementSpecialtyDTO.setId(placementSpecialty.getId());
          placementSpecialtyDTO.setPlacementSpecialtyType(placementSpecialty.getPlacementSpecialtyType());
          placementSpecialtyDTO.setSpecialtyId(placementSpecialty.getSpecialty().getId());
          specialties.add(placementSpecialtyDTO);
        }
        placementDTO.setSpecialties(specialties);
      }
    }
    return placementDTO;
  }

  public List<PlacementDTO> placementsToPlacementDTOs(List<Placement> placements) {
    return placements.stream().map(this::placementToPlacementDTO).collect(Collectors.toList());
  }

  public Placement placementDTOToPlacement(PlacementDTO placementDTO) {
    Placement placement = null;
    if (placementDTO != null) {
      placement = new Placement();
      placement.setId(placementDTO.getId());
      placement.setClinicalSupervisor(personRepository.findOne(placementDTO.getClinicalSupervisorId()));
      placement.setTrainee(personRepository.findOne(placementDTO.getTraineeId()));
      placement.setPost(postRepository.findOne(placementDTO.getPostId()));
      placement.setGradeId(placementDTO.getGradeId());
      placement.setSiteId(placementDTO.getSiteId());
      placement.setDateFrom(placementDTO.getDateFrom());
      placement.setDateTo(placementDTO.getDateTo());
      placement.setIntrepidId(placementDTO.getIntrepidId());
      placement.setLocalPostNumber(placementDTO.getLocalPostNumber());
      placement.setManagingLocalOffice(placementDTO.getManagingLocalOffice());
      placement.setPlacementType(placementDTO.getPlacementType());
      placement.setStatus(placementDTO.getStatus());
      placement.setTrainingDescription(placementDTO.getTrainingDescription());
      placement.setPlacementWholeTimeEquivalent(placementDTO.getPlacementWholeTimeEquivalent());

      if (CollectionUtils.isNotEmpty(placementDTO.getSpecialties())) {
        Set<PlacementSpecialty> specialties = Sets.newHashSet();
        for (PlacementSpecialtyDTO placementSpecialtyDTO : placementDTO.getSpecialties()) {
          PlacementSpecialty placementSpecialty = new PlacementSpecialty();
          placementSpecialty.setId(placementSpecialtyDTO.getId());
          placementSpecialty.setPlacementSpecialtyType(placementSpecialtyDTO.getPlacementSpecialtyType());
          placementSpecialty.setPlacement(placement);
          placementSpecialty.setSpecialty(specialtyRepository.findOne(placementSpecialtyDTO.getSpecialtyId()));
          specialties.add(placementSpecialty);
        }
        placement.setSpecialties(specialties);
      }
    }
    return placement;
  }

  public List<Placement> placementDTOsToPlacements(List<PlacementDTO> placementDTOs) {
    return placementDTOs.stream().map(this::placementDTOToPlacement).collect(Collectors.toList());
  }
}
