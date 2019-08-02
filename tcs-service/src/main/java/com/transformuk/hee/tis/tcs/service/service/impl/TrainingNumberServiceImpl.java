package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.tcs.service.event.TrainingNumberSavedEvent;
import com.transformuk.hee.tis.tcs.service.model.TrainingNumber;
import com.transformuk.hee.tis.tcs.service.repository.TrainingNumberRepository;
import com.transformuk.hee.tis.tcs.service.service.TrainingNumberService;
import com.transformuk.hee.tis.tcs.service.service.mapper.TrainingNumberMapper;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing TrainingNumber.
 */
@Service
@Transactional
public class TrainingNumberServiceImpl implements TrainingNumberService {

  private final Logger log = LoggerFactory.getLogger(TrainingNumberServiceImpl.class);

  private final TrainingNumberRepository trainingNumberRepository;
  private final TrainingNumberMapper trainingNumberMapper;
  private final ApplicationEventPublisher applicationEventPublisher;

  public TrainingNumberServiceImpl(TrainingNumberRepository trainingNumberRepository,
      TrainingNumberMapper trainingNumberMapper,
      ApplicationEventPublisher applicationEventPublisher) {
    this.trainingNumberRepository = trainingNumberRepository;
    this.trainingNumberMapper = trainingNumberMapper;
    this.applicationEventPublisher = applicationEventPublisher;
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
    TrainingNumber trainingNumber = trainingNumberMapper
        .trainingNumberDTOToTrainingNumber(trainingNumberDTO);
    trainingNumber = trainingNumberRepository.save(trainingNumber);
    TrainingNumberDTO trainingNumberDTO1 = trainingNumberMapper
        .trainingNumberToTrainingNumberDTO(trainingNumber);

    applicationEventPublisher.publishEvent(new TrainingNumberSavedEvent(trainingNumberDTO));

    return trainingNumberDTO1;
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
    List<TrainingNumber> trainingNumbers = trainingNumberMapper
        .trainingNumberDTOsToTrainingNumbers(trainingNumberDTO);
    trainingNumbers = trainingNumberRepository.saveAll(trainingNumbers);
    List<TrainingNumberDTO> trainingNumberDTOS = trainingNumberMapper
        .trainingNumbersToTrainingNumberDTOs(trainingNumbers);

    if (CollectionUtils.isNotEmpty(trainingNumberDTOS)) {
      trainingNumberDTO.stream()
          .forEach(tn -> applicationEventPublisher.publishEvent(new TrainingNumberSavedEvent(tn)));
    }

    return trainingNumberDTOS;
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
    return trainingNumberPage.map(trainingNumberMapper::trainingNumberToTrainingNumberDTO);
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
    TrainingNumber trainingNumber = trainingNumberRepository.findById(id).orElse(null);
    return trainingNumberMapper.trainingNumberToTrainingNumberDTO(trainingNumber);
  }

  /**
   * Delete the  trainingNumber by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete TrainingNumber : {}", id);
    trainingNumberRepository.deleteById(id);
  }
}
