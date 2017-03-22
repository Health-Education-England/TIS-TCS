package com.transformuk.hee.tis.service.impl;

import com.transformuk.hee.tis.domain.Curriculum;
import com.transformuk.hee.tis.repository.CurriculumRepository;
import com.transformuk.hee.tis.service.CurriculumService;
import com.transformuk.hee.tis.service.dto.CurriculumDTO;
import com.transformuk.hee.tis.service.mapper.CurriculumMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Curriculum.
 */
@Service
@Transactional
public class CurriculumServiceImpl implements CurriculumService {

	private final Logger log = LoggerFactory.getLogger(CurriculumServiceImpl.class);

	private final CurriculumRepository curriculumRepository;

	private final CurriculumMapper curriculumMapper;

	public CurriculumServiceImpl(CurriculumRepository curriculumRepository, CurriculumMapper curriculumMapper) {
		this.curriculumRepository = curriculumRepository;
		this.curriculumMapper = curriculumMapper;
	}

	/**
	 * Save a curriculum.
	 *
	 * @param curriculumDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public CurriculumDTO save(CurriculumDTO curriculumDTO) {
		log.debug("Request to save Curriculum : {}", curriculumDTO);
		Curriculum curriculum = curriculumMapper.curriculumDTOToCurriculum(curriculumDTO);
		curriculum = curriculumRepository.save(curriculum);
		CurriculumDTO result = curriculumMapper.curriculumToCurriculumDTO(curriculum);
		return result;
	}

	/**
	 * Get all the curricula.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<CurriculumDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Curricula");
		Page<Curriculum> result = curriculumRepository.findAll(pageable);
		return result.map(curriculum -> curriculumMapper.curriculumToCurriculumDTO(curriculum));
	}

	/**
	 * Get one curriculum by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public CurriculumDTO findOne(Long id) {
		log.debug("Request to get Curriculum : {}", id);
		Curriculum curriculum = curriculumRepository.findOneWithEagerRelationships(id);
		CurriculumDTO curriculumDTO = curriculumMapper.curriculumToCurriculumDTO(curriculum);
		return curriculumDTO;
	}

	/**
	 * Delete the  curriculum by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Curriculum : {}", id);
		curriculumRepository.delete(id);
	}
}
