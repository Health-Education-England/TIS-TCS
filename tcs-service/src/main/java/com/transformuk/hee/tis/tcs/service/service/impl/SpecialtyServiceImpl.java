package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.service.model.Specialty;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyRepository;
import com.transformuk.hee.tis.tcs.service.service.SpecialtyService;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyMapper;
import com.transformuk.hee.tis.tcs.api.dto.SpecialtyDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing Specialty.
 */
@Service
@Transactional
public class SpecialtyServiceImpl implements SpecialtyService {

	private final Logger log = LoggerFactory.getLogger(SpecialtyServiceImpl.class);

	private final SpecialtyRepository specialtyRepository;

	private final SpecialtyMapper specialtyMapper;

	public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository, SpecialtyMapper specialtyMapper) {
		this.specialtyRepository = specialtyRepository;
		this.specialtyMapper = specialtyMapper;
	}

	/**
	 * Save a specialty.
	 *
	 * @param specialtyDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public SpecialtyDTO save(SpecialtyDTO specialtyDTO) {
		log.debug("Request to save Specialty : {}", specialtyDTO);
		Specialty specialty = specialtyMapper.specialtyDTOToSpecialty(specialtyDTO);
		specialty = specialtyRepository.save(specialty);
		SpecialtyDTO result = specialtyMapper.specialtyToSpecialtyDTO(specialty);
		return result;
	}

    /**
     * Save a list of specialties.
     *
     * @param specialtyDTO the entities to save
     * @return the list of persisted entities
     */
    @Override
    public List<SpecialtyDTO> save(List<SpecialtyDTO> specialtyDTO) {
        log.debug("Request to save Specialties : {}", specialtyDTO);
        List<Specialty> specialty = specialtyMapper.specialtyDTOsToSpecialties(specialtyDTO);
        specialty = specialtyRepository.save(specialty);
        List<SpecialtyDTO> result = specialtyMapper.specialtiesToSpecialtyDTOs(specialty);
        return result;
    }

    /**
	 * Get all the specialties.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<SpecialtyDTO> findAll(Pageable pageable) {
		log.debug("Request to get all Specialties");
		Page<Specialty> result = specialtyRepository.findAll(pageable);
		return result.map(specialty -> specialtyMapper.specialtyToSpecialtyDTO(specialty));
	}

	/**
	 * Get one specialty by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public SpecialtyDTO findOne(Long id) {
		log.debug("Request to get Specialty : {}", id);
		Specialty specialty = specialtyRepository.findOne(id);
		SpecialtyDTO specialtyDTO = specialtyMapper.specialtyToSpecialtyDTO(specialty);
		return specialtyDTO;
	}

	/**
	 * Delete the  specialty by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete Specialty : {}", id);
		specialtyRepository.delete(id);
	}
}
