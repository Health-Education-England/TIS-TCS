package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.service.service.RotationService;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import com.transformuk.hee.tis.tcs.service.repository.RotationRepository;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.service.service.mapper.RotationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Rotation.
 */
@Service
@Transactional
public class RotationServiceImpl implements RotationService {

    private final Logger log = LoggerFactory.getLogger(RotationServiceImpl.class);

    private final RotationRepository rotationRepository;

    private final RotationMapper rotationMapper;

    public RotationServiceImpl(RotationRepository rotationRepository, RotationMapper rotationMapper) {
        this.rotationRepository = rotationRepository;
        this.rotationMapper = rotationMapper;
    }

    /**
     * Save a rotation.
     *
     * @param rotationDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RotationDTO save(RotationDTO rotationDTO) {
        log.debug("Request to save Rotation : {}", rotationDTO);
        Rotation rotation = rotationMapper.toEntity(rotationDTO);
        rotation = rotationRepository.save(rotation);
        return rotationMapper.toDto(rotation);
    }

    /**
     * Get all the rotations.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RotationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Rotations");
        return rotationRepository.findAll(pageable)
            .map(rotationMapper::toDto);
    }

    /**
     * Get one rotation by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RotationDTO findOne(Long id) {
        log.debug("Request to get Rotation : {}", id);
        Rotation rotation = rotationRepository.findOne(id);
        return rotationMapper.toDto(rotation);
    }

    /**
     * Delete the rotation by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rotation : {}", id);
        rotationRepository.delete(id);
    }
}
