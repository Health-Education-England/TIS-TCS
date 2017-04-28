package com.transformuk.hee.tis.service.impl;

import com.transformuk.hee.tis.domain.SpecialtyGroup;
import com.transformuk.hee.tis.repository.SpecialtyGroupRepository;
import com.transformuk.hee.tis.service.SpecialtyGroupService;
import com.transformuk.hee.tis.service.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.service.mapper.SpecialtyGroupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing SpecialtyGroup.
 */
@Service
@Transactional
public class SpecialtyGroupServiceImpl implements SpecialtyGroupService {

	private final Logger log = LoggerFactory.getLogger(SpecialtyGroupServiceImpl.class);

	private final SpecialtyGroupRepository specialtyGroupRepository;

	private final SpecialtyGroupMapper specialtyGroupMapper;

	public SpecialtyGroupServiceImpl(SpecialtyGroupRepository specialtyGroupRepository, SpecialtyGroupMapper specialtyGroupMapper) {
		this.specialtyGroupRepository = specialtyGroupRepository;
		this.specialtyGroupMapper = specialtyGroupMapper;
	}

	/**
	 * Save a specialtyGroup.
	 *
	 * @param specialtyGroupDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public SpecialtyGroupDTO save(SpecialtyGroupDTO specialtyGroupDTO) {
		log.debug("Request to save SpecialtyGroup : {}", specialtyGroupDTO);
		SpecialtyGroup specialtyGroup = specialtyGroupMapper.specialtyGroupDTOToSpecialtyGroup(specialtyGroupDTO);
		specialtyGroup = specialtyGroupRepository.save(specialtyGroup);
		SpecialtyGroupDTO result = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(specialtyGroup);
		return result;
	}

	/**
	 * Get all the specialtyGroups.
	 *
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<SpecialtyGroupDTO> findAll() {
		log.debug("Request to get all SpecialtyGroups");
		List<SpecialtyGroupDTO> result = specialtyGroupRepository.findAll().stream()
				.map(specialtyGroupMapper::specialtyGroupToSpecialtyGroupDTO)
				.collect(Collectors.toCollection(LinkedList::new));

		return result;
	}

	/**
	 * Get one specialtyGroup by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public SpecialtyGroupDTO findOne(Long id) {
		log.debug("Request to get SpecialtyGroup : {}", id);
		SpecialtyGroup specialtyGroup = specialtyGroupRepository.findOne(id);
		SpecialtyGroupDTO specialtyGroupDTO = specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(specialtyGroup);
		return specialtyGroupDTO;
	}

	/**
	 * Delete the  specialtyGroup by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete SpecialtyGroup : {}", id);
		specialtyGroupRepository.delete(id);
	}
}
