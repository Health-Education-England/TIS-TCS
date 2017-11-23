package com.transformuk.hee.tis.tcs.service.service.mapper;

import com.google.common.collect.Sets;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.PlacementStatus;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialty;
import com.transformuk.hee.tis.tcs.service.model.PlacementSupervisor;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PostRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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

  @Autowired
  private PlacementSpecialtyMapper placementSpecialtyMapper;

  public PlacementDTO placementToPlacementDTO(Placement placement) {
    PlacementDTO placementDTO = null;
    if (placement != null) {
      placementDTO = new PlacementDTO();
      placementDTO.setId(placement.getId());
      placementDTO.setClinicalSupervisorIds(placement.getClinicalSupervisors() != null ?
          placement.getClinicalSupervisors().stream().map(ps -> ps.getClinicalSupervisor().getId()).collect(Collectors.toSet()) : null);
      placementDTO.setTraineeId(placement.getTrainee() != null ? placement.getTrainee().getId() : null);
      placementDTO.setPostId(placement.getPost() != null ? placement.getPost().getId() : null);
      placementDTO.setGradeAbbreviation(placement.getGradeAbbreviation());
      placementDTO.setSiteCode(placement.getSiteCode());
      placementDTO.setDateFrom(placement.getDateFrom());
      placementDTO.setDateTo(placement.getDateTo());
      placementDTO.setIntrepidId(placement.getIntrepidId());
      placementDTO.setLocalPostNumber(placement.getLocalPostNumber());
      placementDTO.setPlacementType(placement.getPlacementType());
      placementDTO.setTrainingDescription(placement.getTrainingDescription());
      placementDTO.setPlacementWholeTimeEquivalent(placement.getPlacementWholeTimeEquivalent());
      placementDTO.setStatus(getStatus(placement.getDateFrom(), placement.getDateTo()));

      placementDTO.setSpecialties(placementSpecialtyMapper.toDTOs(placement.getSpecialties()));
    }
    return placementDTO;
  }

  public List<PlacementDTO> placementsToPlacementDTOs(List<Placement> placements) {
    return placements.stream().map(this::placementToPlacementDTO).collect(Collectors.toList());
  }

  public Placement placementDTOToPlacement(PlacementDTO placementDTO) {
    if (placementDTO == null) return null;

    final Placement placement = new Placement();
    placement.setId(placementDTO.getId());
    if (placementDTO.getClinicalSupervisorIds() != null) {
      Set<PlacementSupervisor> clinicalSupervisors = placementDTO.getClinicalSupervisorIds().stream().map(csi -> {
        PlacementSupervisor placementSupervisor = new PlacementSupervisor();
        placementSupervisor.setClinicalSupervisor(personRepository.findOne(csi));
        placementSupervisor.setPlacement(placement);
        return placementSupervisor;
      }).collect(Collectors.toSet());
      placement.setClinicalSupervisors(clinicalSupervisors);
    }
    placement.setTrainee(personRepository.findOne(placementDTO.getTraineeId()));
    if (placementDTO.getPostId() != null) {
      placement.setPost(postRepository.findOne(placementDTO.getPostId()));
    }
    placement.setGradeAbbreviation(placementDTO.getGradeAbbreviation());
    placement.setSiteCode(placementDTO.getSiteCode());
    placement.setDateFrom(placementDTO.getDateFrom());
    placement.setDateTo(placementDTO.getDateTo());
    placement.setIntrepidId(placementDTO.getIntrepidId());
    placement.setLocalPostNumber(placementDTO.getLocalPostNumber());
    placement.setPlacementType(placementDTO.getPlacementType());
    placement.setTrainingDescription(placementDTO.getTrainingDescription());
    placement.setPlacementWholeTimeEquivalent(placementDTO.getPlacementWholeTimeEquivalent());

    if (CollectionUtils.isNotEmpty(placementDTO.getSpecialties())) {
      Set<PlacementSpecialty> specialties = Sets.newHashSet();
      for (PlacementSpecialtyDTO placementSpecialtyDTO : placementDTO.getSpecialties()) {
        PlacementSpecialty placementSpecialty = new PlacementSpecialty();
        placementSpecialty.setPlacementSpecialtyType(placementSpecialtyDTO.getPlacementSpecialtyType());
        placementSpecialty.setPlacement(placement);
        placementSpecialty.setSpecialty(specialtyRepository.findOne(placementSpecialtyDTO.getSpecialtyId()));
        specialties.add(placementSpecialty);
      }
      placement.setSpecialties(specialties);
    }
    return placement;
  }

  public List<Placement> placementDTOsToPlacements(List<PlacementDTO> placementDTOs) {
    return placementDTOs.stream().map(this::placementDTOToPlacement).collect(Collectors.toList());
  }

  private PlacementStatus getStatus(LocalDate dateFrom, LocalDate dateTo) {
    if (dateFrom == null || dateTo == null) {
      return null;
    }

    LocalDate today = LocalDate.now();
    if (today.isBefore(dateFrom)) {
      return PlacementStatus.FUTURE;
    } else if (today.isAfter(dateTo)) {
      return PlacementStatus.PAST;
    }
    return PlacementStatus.CURRENT;
  }
}
