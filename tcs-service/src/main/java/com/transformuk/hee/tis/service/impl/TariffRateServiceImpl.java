package com.transformuk.hee.tis.service.impl;

import com.transformuk.hee.tis.domain.TariffRate;
import com.transformuk.hee.tis.repository.TariffRateRepository;
import com.transformuk.hee.tis.service.TariffRateService;
import com.transformuk.hee.tis.service.dto.TariffRateDTO;
import com.transformuk.hee.tis.service.mapper.TariffRateMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing TariffRate.
 */
@Service
@Transactional
public class TariffRateServiceImpl implements TariffRateService {

	private final Logger log = LoggerFactory.getLogger(TariffRateServiceImpl.class);

	private final TariffRateRepository tariffRateRepository;

	private final TariffRateMapper tariffRateMapper;

	public TariffRateServiceImpl(TariffRateRepository tariffRateRepository, TariffRateMapper tariffRateMapper) {
		this.tariffRateRepository = tariffRateRepository;
		this.tariffRateMapper = tariffRateMapper;
	}

	/**
	 * Save a tariffRate.
	 *
	 * @param tariffRateDTO the entity to save
	 * @return the persisted entity
	 */
	@Override
	public TariffRateDTO save(TariffRateDTO tariffRateDTO) {
		log.debug("Request to save TariffRate : {}", tariffRateDTO);
		TariffRate tariffRate = tariffRateMapper.tariffRateDTOToTariffRate(tariffRateDTO);
		tariffRate = tariffRateRepository.save(tariffRate);
		TariffRateDTO result = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);
		return result;
	}

	/**
	 * Get all the tariffRates.
	 *
	 * @return the list of entities
	 */
	@Override
	@Transactional(readOnly = true)
	public List<TariffRateDTO> findAll() {
		log.debug("Request to get all TariffRates");
		List<TariffRateDTO> result = tariffRateRepository.findAll().stream()
				.map(tariffRateMapper::tariffRateToTariffRateDTO)
				.collect(Collectors.toCollection(LinkedList::new));

		return result;
	}

	/**
	 * Get one tariffRate by id.
	 *
	 * @param id the id of the entity
	 * @return the entity
	 */
	@Override
	@Transactional(readOnly = true)
	public TariffRateDTO findOne(Long id) {
		log.debug("Request to get TariffRate : {}", id);
		TariffRate tariffRate = tariffRateRepository.findOne(id);
		TariffRateDTO tariffRateDTO = tariffRateMapper.tariffRateToTariffRateDTO(tariffRate);
		return tariffRateDTO;
	}

	/**
	 * Delete the  tariffRate by id.
	 *
	 * @param id the id of the entity
	 */
	@Override
	public void delete(Long id) {
		log.debug("Request to delete TariffRate : {}", id);
		tariffRateRepository.delete(id);
	}
}
