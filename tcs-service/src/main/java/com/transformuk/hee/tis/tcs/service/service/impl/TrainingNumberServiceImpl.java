package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.transformuk.hee.tis.tcs.api.dto.ProgrammeDTO;
import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.TrainingNumberRepository;
import com.transformuk.hee.tis.tcs.service.service.TrainingNumberService;
import com.transformuk.hee.tis.tcs.service.service.mapper.TrainingNumberMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TrainingNumber.
 */
@Service
@Transactional
public class TrainingNumberServiceImpl implements TrainingNumberService {

  private final Logger log = LoggerFactory.getLogger(TrainingNumberServiceImpl.class);

  private final TrainingNumberRepository trainingNumberRepository;

  private final TrainingNumberMapper trainingNumberMapper;

  @Autowired
  private ProgrammeRepository programmeRepository;

  public TrainingNumberServiceImpl(TrainingNumberRepository trainingNumberRepository, TrainingNumberMapper trainingNumberMapper, ProgrammeRepository programmeRepository) {
    this.trainingNumberRepository = trainingNumberRepository;
    this.trainingNumberMapper = trainingNumberMapper;
    this.programmeRepository = programmeRepository;
  }

  /**
   * Save a trainingNumber.
   *
   * @param trainingNumberDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public TrainingNumberDTO save(TrainingNumberDTO trainingNumberDTO) {
    log.debug("Request to save TrainingNumber : {}", trainingNumberDTO);
    TrainingNumber trainingNumber = trainingNumberMapper.trainingNumberDTOToTrainingNumber(trainingNumberDTO);
    trainingNumber = trainingNumberRepository.save(trainingNumber);
    TrainingNumberDTO result = trainingNumberMapper.trainingNumberToTrainingNumberDTO(trainingNumber);
    return result;
  }

  /**
   * Save a list of trainingNumbers.
   *
   * @param trainingNumberDTO the entities to save
   * @return the list of persisted entities
   */
  @Override
  public List<TrainingNumberDTO> save(List<TrainingNumberDTO> trainingNumberDTO) {
    log.debug("Request to save TrainingNumber : {}", trainingNumberDTO);
    List<TrainingNumber> trainingNumber = trainingNumberMapper.trainingNumberDTOsToTrainingNumbers(trainingNumberDTO);
    trainingNumber = trainingNumberRepository.save(trainingNumber);
    List<TrainingNumberDTO> result = trainingNumberMapper.trainingNumbersToTrainingNumberDTOs(trainingNumber);
    return result;
  }

  /**
   * Get all the trainingNumbers.
   *
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<TrainingNumberDTO> findAll(Pageable pageable) {
    log.debug("Request to get all TrainingNumbers");
    Page<TrainingNumber> trainingNumberPage = trainingNumberRepository.findAll(pageable);
    return trainingNumberPage.map(trainingNumber -> trainingNumberMapper.trainingNumberToTrainingNumberDTO(trainingNumber));
  }

  /**
   * Get one trainingNumber by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public TrainingNumberDTO findOne(Long id) {
    log.debug("Request to get TrainingNumber : {}", id);
    TrainingNumber trainingNumber = trainingNumberRepository.findOne(id);
    TrainingNumberDTO trainingNumberDTO = trainingNumberMapper.trainingNumberToTrainingNumberDTO(trainingNumber);
    return trainingNumberDTO;
  }

  /**
   * Delete the  trainingNumber by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete TrainingNumber : {}", id);
    trainingNumberRepository.delete(id);
  }

  @Override
  public List<TrainingNumberDTO> patchTrainingNumberProgrammes(List<TrainingNumberDTO> trainingNumberDTOList) {
    List<TrainingNumber> trainingNumbers = Lists.newArrayList();
    Map<Long, TrainingNumber> idToTrainingNumber = getTrainingNumbersById(trainingNumberDTOList);

    Set<Long> programmeIds = trainingNumberDTOList
        .stream()
        .map(TrainingNumberDTO::getProgrammes)
        .map(ProgrammeDTO::getId)
        .collect(Collectors.toSet());

    Map<Long, Programme> idToProgramme = programmeRepository.findAll(programmeIds)
        .stream().collect(Collectors.toMap(Programme::getId, p -> p));

    for(TrainingNumberDTO dto : trainingNumberDTOList) {
      TrainingNumber trainingNumber = idToTrainingNumber.get(dto.getId());
      Programme programme = idToProgramme.get(dto.getProgrammes().getId());
      if (trainingNumber != null && programme != null) {
        trainingNumber.setProgrammeId(programme);
        trainingNumbers.add(trainingNumber);
      }

    }
    List<TrainingNumber> savedTrainingNumbers = trainingNumberRepository.save(trainingNumbers);
    return trainingNumberMapper.trainingNumbersToTrainingNumberDTOs(savedTrainingNumbers);
  }

  private Map<Long, TrainingNumber> getTrainingNumbersById (List<TrainingNumberDTO> trainingNumberDTOList) {
    Set<Long> trainingNumberId = trainingNumberDTOList.stream().map(TrainingNumberDTO::getId).collect(Collectors.toSet());
    Set<TrainingNumber> trainingNumberFound = trainingNumberRepository.findTrainingNumberById(trainingNumberId);
    Map<Long, TrainingNumber> result = Maps.newHashMap();
    if (CollectionUtils.isNotEmpty(trainingNumberFound)) {
      result = trainingNumberFound.stream().collect(
              Collectors.toMap(TrainingNumber::getId, trainingNumber -> trainingNumber)
      );
    }
    return result;
  }
}
