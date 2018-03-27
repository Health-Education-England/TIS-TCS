package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.service.service.RotationPersonService;
import com.transformuk.hee.tis.tcs.service.model.RotationPerson;
import com.transformuk.hee.tis.tcs.service.repository.RotationPersonRepository;
import com.transformuk.hee.tis.tcs.api.dto.RotationPersonDTO;
import com.transformuk.hee.tis.tcs.service.service.mapper.RotationPersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing RotationPerson.
 */
@Service
@Transactional
public class RotationPersonServiceImpl implements RotationPersonService {

    private final Logger log = LoggerFactory.getLogger(RotationPersonServiceImpl.class);

    private final RotationPersonRepository rotationPersonRepository;

    private final RotationPersonMapper rotationPersonMapper;

    public RotationPersonServiceImpl(RotationPersonRepository rotationPersonRepository, RotationPersonMapper rotationPersonMapper) {
        this.rotationPersonRepository = rotationPersonRepository;
        this.rotationPersonMapper = rotationPersonMapper;
    }

    /**
     * Save a rotationPerson.
     *
     * @param rotationPersonDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RotationPersonDTO save(RotationPersonDTO rotationPersonDTO) {
        log.debug("Request to save RotationPerson : {}", rotationPersonDTO);
        RotationPerson rotationPerson = rotationPersonMapper.toEntity(rotationPersonDTO);
        rotationPerson = rotationPersonRepository.save(rotationPerson);
        return rotationPersonMapper.toDto(rotationPerson);
    }

    /**
     * Get all the rotationPeople.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RotationPersonDTO> findAll() {
        log.debug("Request to get all RotationPeople");
        return rotationPersonRepository.findAll().stream()
            .map(rotationPersonMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one rotationPerson by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RotationPersonDTO findOne(Long id) {
        log.debug("Request to get RotationPerson : {}", id);
        RotationPerson rotationPerson = rotationPersonRepository.findOne(id);
        return rotationPersonMapper.toDto(rotationPerson);
    }

    /**
     * Delete the rotationPerson by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RotationPerson : {}", id);
        rotationPersonRepository.delete(id);
    }
}
