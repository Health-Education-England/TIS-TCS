package com.transformuk.hee.tis.service.impl;

import com.transformuk.hee.tis.domain.TrainingNumber;
import com.transformuk.hee.tis.repository.TrainingNumberRepository;
import com.transformuk.hee.tis.service.TrainingNumberService;
import com.transformuk.hee.tis.service.dto.TrainingNumberDTO;
import com.transformuk.hee.tis.service.mapper.TrainingNumberMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
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

	public TrainingNumberServiceImpl(TrainingNumberRepository trainingNumberRepository, TrainingNumberMapper trainingNumberMapper) {
		this.trainingNumberRepository = trainingNumberRepository;
		this.trainingNumberMapper = trainingNumberMapper;
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
	 * Get all the trainingNumbers.
	 *
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TrainingNumberDTO> findAll() {
		log.debug("Request to get all TrainingNumbers");
		List<TrainingNumberDTO> result = trainingNumberRepository.findAll().stream()
				.map(trainingNumberMapper::trainingNumberToTrainingNumberDTO)
				.collect(Collectors.toCollection(LinkedList::new));

		return result;
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
}
