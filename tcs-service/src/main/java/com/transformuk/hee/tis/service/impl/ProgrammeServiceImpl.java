package com.transformuk.hee.tis.service.impl;

import com.transformuk.hee.tis.domain.Programme;
import com.transformuk.hee.tis.repository.ProgrammeRepository;
import com.transformuk.hee.tis.service.ProgrammeService;
import com.transformuk.hee.tis.service.dto.ProgrammeDTO;
import com.transformuk.hee.tis.service.mapper.ProgrammeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Programme.
 */
@Service
@Transactional
public class ProgrammeServiceImpl implements ProgrammeService {

	private final Logger log = LoggerFactory.getLogger(ProgrammeServiceImpl.class);

	private final ProgrammeRepository programmeRepository;

	private final ProgrammeMapper programmeMapper;

	public ProgrammeServiceImpl(ProgrammeRepository programmeRepository, ProgrammeMapper programmeMapper) {
		this.programmeRepository = programmeRepository;
		this.programmeMapper = programmeMapper;
	}

	/**
	 * Save a programme.
	 *
	 * @param programmeDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public ProgrammeDTO save(ProgrammeDTO programmeDTO) {
		log.debug("Request to save Programme : {}", programmeDTO);
		Programme programme = programmeMapper.programmeDTOToProgramme(programmeDTO);
		programme = programmeRepository.save(programme);
		ProgrammeDTO result = programmeMapper.programmeToProgrammeDTO(programme);
		return result;
	}

	/**
	 * Get all the programmes.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<ProgrammeDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Programmes");
		Page<Programme> result = programmeRepository.findAll(pageable);
		return result.map(programme -> programmeMapper.programmeToProgrammeDTO(programme));
	}

	/**
	 * Get one programme by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public ProgrammeDTO findOne(Long id) {
		log.debug("Request to get Programme : {}", id);
		Programme programme = programmeRepository.findOne(id);
		ProgrammeDTO programmeDTO = programmeMapper.programmeToProgrammeDTO(programme);
		return programmeDTO;
	}

	/**
	 * Delete the  programme by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Programme : {}", id);
		programmeRepository.delete(id);
	}
}
