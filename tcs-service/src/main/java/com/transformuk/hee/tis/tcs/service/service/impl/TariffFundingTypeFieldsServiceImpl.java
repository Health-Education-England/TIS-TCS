package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.service.model.TariffFundingTypeFields;
import com.transformuk.hee.tis.tcs.service.repository.TariffFundingTypeFieldsRepository;
import com.transformuk.hee.tis.tcs.service.service.TariffFundingTypeFieldsService;
import com.transformuk.hee.tis.tcs.service.service.mapper.TariffFundingTypeFieldsMapper;
import com.transformuk.hee.tis.tcs.api.dto.TariffFundingTypeFieldsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TariffFundingTypeFields.
 */
@Service
@Transactional
public class TariffFundingTypeFieldsServiceImpl implements TariffFundingTypeFieldsService {

	private final Logger log = LoggerFactory.getLogger(TariffFundingTypeFieldsServiceImpl.class);

	private final TariffFundingTypeFieldsRepository tariffFundingTypeFieldsRepository;

	private final TariffFundingTypeFieldsMapper tariffFundingTypeFieldsMapper;

	public TariffFundingTypeFieldsServiceImpl(TariffFundingTypeFieldsRepository tariffFundingTypeFieldsRepository, TariffFundingTypeFieldsMapper tariffFundingTypeFieldsMapper) {
		this.tariffFundingTypeFieldsRepository = tariffFundingTypeFieldsRepository;
		this.tariffFundingTypeFieldsMapper = tariffFundingTypeFieldsMapper;
	}

	/**
	 * Save a tariffFundingTypeFields.
	 *
	 * @param tariffFundingTypeFieldsDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public TariffFundingTypeFieldsDTO save(TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO) {
		log.debug("Request to save TariffFundingTypeFields : {}", tariffFundingTypeFieldsDTO);
		TariffFundingTypeFields tariffFundingTypeFields = tariffFundingTypeFieldsMapper.tariffFundingTypeFieldsDTOToTariffFundingTypeFields(tariffFundingTypeFieldsDTO);
		tariffFundingTypeFields = tariffFundingTypeFieldsRepository.save(tariffFundingTypeFields);
		TariffFundingTypeFieldsDTO result = tariffFundingTypeFieldsMapper.tariffFundingTypeFieldsToTariffFundingTypeFieldsDTO(tariffFundingTypeFields);
		return result;
	}

	/**
	 * Get all the tariffFundingTypeFields.
	 *
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TariffFundingTypeFieldsDTO> findAll() {
		log.debug("Request to get all TariffFundingTypeFields");
		List<TariffFundingTypeFieldsDTO> result = tariffFundingTypeFieldsRepository.findAll().stream()
				.map(tariffFundingTypeFieldsMapper::tariffFundingTypeFieldsToTariffFundingTypeFieldsDTO)
				.collect(Collectors.toCollection(LinkedList::new));

		return result;
	}

	/**
	 * Get one tariffFundingTypeFields by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public TariffFundingTypeFieldsDTO findOne(Long id) {
		log.debug("Request to get TariffFundingTypeFields : {}", id);
		TariffFundingTypeFields tariffFundingTypeFields = tariffFundingTypeFieldsRepository.findOne(id);
		TariffFundingTypeFieldsDTO tariffFundingTypeFieldsDTO = tariffFundingTypeFieldsMapper.tariffFundingTypeFieldsToTariffFundingTypeFieldsDTO(tariffFundingTypeFields);
		return tariffFundingTypeFieldsDTO;
	}

	/**
	 * Delete the  tariffFundingTypeFields by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete TariffFundingTypeFields : {}", id);
		tariffFundingTypeFieldsRepository.delete(id);
	}
}
