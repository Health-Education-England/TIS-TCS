package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.SpecialtyGroupDTO;
import com.transformuk.hee.tis.tcs.service.model.SpecialtyGroup;
import com.transformuk.hee.tis.tcs.service.repository.SpecialtyGroupRepository;
import com.transformuk.hee.tis.tcs.service.service.SpecialtyGroupService;
import com.transformuk.hee.tis.tcs.service.service.mapper.SpecialtyGroupMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;

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

	@Override
	public List<SpecialtyGroupDTO> save(List<SpecialtyGroupDTO> specialtyGroupDTO) {
		log.debug("Request to save SpecialtyGroup : {}", specialtyGroupDTO);
		List<SpecialtyGroup> specialtyGroup = specialtyGroupMapper.specialtyGroupDTOsToSpecialtyGroups(specialtyGroupDTO);
		specialtyGroup = specialtyGroupRepository.save(specialtyGroup);
		List<SpecialtyGroupDTO> result = specialtyGroupMapper.specialtyGroupsToSpecialtyGroupDTOs(specialtyGroup);
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SpecialtyGroupDTO> advancedSearch(
			String searchString, Pageable pageable) {

		List<Specification<SpecialtyGroup>> specs = new ArrayList<>();
		//add the text search criteria
		if (StringUtils.isNotEmpty(searchString)) {
			specs.add(Specifications.where(containsLike("name", searchString)));
		}

		Specifications<SpecialtyGroup> fullSpec = Specifications.where(specs.get(0));
		//add the rest of the specs that made it in
		for (int i = 1; i < specs.size(); i++) {
			fullSpec = fullSpec.and(specs.get(i));
		}
		Page<SpecialtyGroup> result = specialtyGroupRepository.findAll(fullSpec, pageable);

		return result.map(specialtyGroup -> specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(specialtyGroup));
	}

	/**
	 * Get all the specialtyGroups.
	 *
	 * @param pageable the pagination information
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public Page<SpecialtyGroupDTO> findAll(Pageable pageable) {
		log.debug("Request to get all SpecialtyGroups");
		Page<SpecialtyGroup> specialtyGroupPage = specialtyGroupRepository.findAll(pageable);
		return specialtyGroupPage.map(specialtyGroup -> specialtyGroupMapper.specialtyGroupToSpecialtyGroupDTO(specialtyGroup));
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
