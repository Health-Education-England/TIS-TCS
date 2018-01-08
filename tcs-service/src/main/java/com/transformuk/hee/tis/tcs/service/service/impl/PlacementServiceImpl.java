package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementDetailsDTO;
import com.transformuk.hee.tis.tcs.api.dto.PlacementSpecialtyDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.TCSDateColumns;
import com.transformuk.hee.tis.tcs.service.api.util.ColumnFilterUtil;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.Placement;
import com.transformuk.hee.tis.tcs.service.model.PlacementDetails;
import com.transformuk.hee.tis.tcs.service.model.PlacementSpecialty;
import com.transformuk.hee.tis.tcs.service.model.PlacementSupervisor;
import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementDetailsRepository;
import com.transformuk.hee.tis.tcs.service.repository.PlacementRepository;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.PlacementService;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementDetailsMapper;
import com.transformuk.hee.tis.tcs.service.service.mapper.PlacementMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.tcs.service.api.util.DateUtil.getLocalDateFromString;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.isBetween;

/**
 * Service Implementation for managing Placement.
 */
@Service
@Transactional
public class PlacementServiceImpl implements PlacementService {

  private final Logger log = LoggerFactory.getLogger(PlacementServiceImpl.class);

  private final PlacementRepository placementRepository;
  private final PlacementDetailsRepository placementDetailsRepository;
  private final PlacementMapper placementMapper;
  private final PlacementDetailsMapper placementDetailsMapper;
  private final SpecialtyRepository specialtyRepository;
  private final PersonRepository personRepository;

  public PlacementServiceImpl(PlacementRepository placementRepository,
                              PlacementDetailsRepository placementDetailsRepository,
                              PlacementMapper placementMapper,
                              PlacementDetailsMapper placementDetailsMapper,
                              SpecialtyRepository specialtyRepository,
                              PersonRepository personRepository) {
    this.placementRepository = placementRepository;
    this.placementDetailsRepository = placementDetailsRepository;
    this.placementMapper = placementMapper;
    this.placementDetailsMapper = placementDetailsMapper;
    this.specialtyRepository = specialtyRepository;
    this.personRepository = personRepository;
  }

  /**
   * Save a placement.
   *
   * @param placementDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public PlacementDTO save(PlacementDTO placementDTO) {
    log.debug("Request to save Placement : {}", placementDTO);
    Placement placement = placementMapper.placementDTOToPlacement(placementDTO);
    placement = placementRepository.save(placement);
    return placementMapper.placementToPlacementDTO(placement);
  }

  @Override
  public PlacementDetailsDTO saveDetails(PlacementDetailsDTO placementDetailsDTO) {
    log.debug("Request to save Placement : {}", placementDetailsDTO);
    PlacementDetails placementDetails = placementDetailsMapper.placementDetailsDTOToPlacementDetails(placementDetailsDTO);
    placementDetails = placementDetailsRepository.save(placementDetails);
    return placementDetailsMapper.placementDetailsToPlacementDetailsDTO(placementDetails);
  }

  /**
   * Save a list of placements.
   *
   * @param placementDTO the list of entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<PlacementDTO> save(List<PlacementDTO> placementDTO) {
    log.debug("Request to save Placements : {}", placementDTO);
    List<Placement> placements = placementMapper.placementDTOsToPlacements(placementDTO);
    placements = placementRepository.save(placements);
    return placementMapper.placementsToPlacementDTOs(placements);
  }

  /**
   * Get all the placements.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PlacementDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Placements");
    Page<Placement> result = placementRepository.findAll(pageable);
    return result.map(placementMapper::placementToPlacementDTO);
  }

  /**
   * Get one placement by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public PlacementDTO findOne(Long id) {
    log.debug("Request to get Placement : {}", id);
    Placement placement = placementRepository.findOne(id);
    return placementMapper.placementToPlacementDTO(placement);
  }

  @Override
  @Transactional(readOnly = true)
  public PlacementDetailsDTO getDetails(Long id) {
    PlacementDetails pd = placementDetailsRepository.findOne(id);
    return placementDetailsMapper.placementDetailsToPlacementDetailsDTO(pd);
  }

  /**
   * Delete the  placement by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Placement : {}", id);
    placementRepository.delete(id);
  }

  private Map<String, Placement> getPlacementsByIntrepidId(List<PlacementDTO> placementDtoList) {
    Set<String> placementIntrepidIds = placementDtoList.stream().map(PlacementDTO::getIntrepidId).collect(Collectors.toSet());
    Set<Placement> placementsFound = placementRepository.findByIntrepidIdIn(placementIntrepidIds);
    Map<String, Placement> result = Maps.newHashMap();
    if (CollectionUtils.isNotEmpty(placementsFound)) {
      result = placementsFound.stream().collect(
          Collectors.toMap(Placement::getIntrepidId, post -> post)
      );
    }
    return result;
  }

  @Override
  public List<PlacementDTO> patchPlacementSpecialties(List<PlacementDTO> placementDTOList) {
    List<Placement> placements = Lists.newArrayList();
    Map<String, Placement> intrepidIdToPlacement = getPlacementsByIntrepidId(placementDTOList);

    Set<Long> specialtyIds = placementDTOList
        .stream()
        .map(PlacementDTO::getSpecialties)
        .flatMap(Collection::stream)
        .map(PlacementSpecialtyDTO::getSpecialtyId)
        .collect(Collectors.toSet());

    Map<Long, Specialty> idToSpecialty = specialtyRepository.findAll(specialtyIds).stream().collect(Collectors.toMap(Specialty::getId, sp -> sp));
    for (PlacementDTO dto : placementDTOList) {
      Placement placement = intrepidIdToPlacement.get(dto.getIntrepidId());
      if (placement != null) {
        Set<PlacementSpecialty> attachedSpecialties = placement.getSpecialties();
        Set<Long> attachedSpecialtyIds = attachedSpecialties.stream().map(ps -> ps.getSpecialty().getId()).collect(Collectors.toSet());
        for (PlacementSpecialtyDTO placementSpecialtyDTO : dto.getSpecialties()) {
          Specialty specialty = idToSpecialty.get(placementSpecialtyDTO.getSpecialtyId());
          if (specialty != null && !attachedSpecialtyIds.contains(specialty.getId())) {
            PlacementSpecialty placementSpecialty = new PlacementSpecialty();
            placementSpecialty.setPlacementSpecialtyType(placementSpecialtyDTO.getPlacementSpecialtyType());
            placementSpecialty.setPlacement(placement);
            placementSpecialty.setSpecialty(specialty);
            attachedSpecialties.add(placementSpecialty);
          }
        }
        placement.setSpecialties(attachedSpecialties);
        placements.add(placement);
      }
    }
    List<Placement> savedPlacements = placementRepository.save(placements);
    return placementMapper.placementsToPlacementDTOs(savedPlacements);
  }

  @Override
  public List<PlacementDTO> patchPlacementClinicalSupervisors(List<PlacementDTO> placementDTOList) {
    List<Placement> placements = Lists.newArrayList();
    Map<String, Placement> intrepidIdToPlacement = getPlacementsByIntrepidId(placementDTOList);

    Set<Long> clinicalSupervisorIds = placementDTOList
        .stream()
        .map(PlacementDTO::getClinicalSupervisorIds)
        .flatMap(Collection::stream)
        .collect(Collectors.toSet());

    Map<Long, Person> idToPerson = personRepository.findAll(clinicalSupervisorIds).stream().collect(Collectors.toMap(Person::getId, p -> p));
    for (PlacementDTO dto : placementDTOList) {
      Placement placement = intrepidIdToPlacement.get(dto.getIntrepidId());
      if (placement != null) {
        Set<PlacementSupervisor> attachedClinicalSupervisors = placement.getClinicalSupervisors();
        Set<Long> attachedClinicalSupervisorIds = attachedClinicalSupervisors.stream().map(ps -> ps.getClinicalSupervisor().getId()).collect(Collectors.toSet());
        for (Long clinicalSupervisorId : dto.getClinicalSupervisorIds()) {
          Person person = idToPerson.get(clinicalSupervisorId);
          if (person != null && !attachedClinicalSupervisorIds.contains(clinicalSupervisorId)) {
            PlacementSupervisor placementSupervisor = new PlacementSupervisor();
            placementSupervisor.setPlacement(placement);
            placementSupervisor.setClinicalSupervisor(person);
            attachedClinicalSupervisors.add(placementSupervisor);
          }
        }
        placement.setClinicalSupervisors(attachedClinicalSupervisors);
        placements.add(placement);
      }
    }
    List<Placement> savedPlacements = placementRepository.save(placements);
    return placementMapper.placementsToPlacementDTOs(savedPlacements);
  }

  /**
   * Get all placement details by given column filters.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PlacementDetailsDTO> findAllPlacementDetails(Pageable pageable) {
    log.debug("Request to get all Placements details");
    Page<PlacementDetails> result = placementDetailsRepository.findAll(pageable);
    return result.map(placementDetailsMapper::placementDetailsToPlacementDetailsDTO);
  }


  /**
   * Get all placement details by given column filters.
   *
   * @param columnFilterJson column filters represented in json object
   * @param pageable the pagination information
   * @return the list of entities
   * @throws IOException
   */
  @Override
  @Transactional(readOnly = true)
  public Page<PlacementDetailsDTO> findFilteredPlacements(String columnFilterJson, Pageable pageable) throws IOException {

    log.debug("Request to get all Revalidations filtered by columns {}", columnFilterJson);
    List<Class> filterEnumList = Collections.emptyList();
    List<ColumnFilter> columnFilters = ColumnFilterUtil.getColumnFilters(columnFilterJson, filterEnumList);

    List<Specification<PlacementDetails>> specs = new ArrayList<>();

    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> {
        if (TCSDateColumns.contains(cf.getName())) {
          specs.add(isBetween(
              cf.getName(),
              getLocalDateFromString(cf.getValues().get(0).toString()),
              getLocalDateFromString(cf.getValues().get(1).toString())
              )
          );
        } else {
          specs.add(in(cf.getName(), Collections.unmodifiableCollection(cf.getValues())));
        }
      });
    }
    Page<PlacementDetails> result;
    if (!specs.isEmpty()) {
      Specifications<PlacementDetails> fullSpec = Specifications.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      result = placementDetailsRepository.findAll(fullSpec, pageable);
    } else {
      result = placementDetailsRepository.findAll(pageable);
    }

    return result.map(placementDetailsMapper::placementDetailsToPlacementDetailsDTO);
  }
}
