package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Functions;
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.api.dto.RotationDTO;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.PostView;
import com.transformuk.hee.tis.tcs.service.model.Programme;
import com.transformuk.hee.tis.tcs.service.model.Rotation;
import com.transformuk.hee.tis.tcs.service.repository.ProgrammeRepository;
import com.transformuk.hee.tis.tcs.service.repository.RotationRepository;
import com.transformuk.hee.tis.tcs.service.service.RotationService;
import com.transformuk.hee.tis.tcs.service.service.mapper.RotationMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;


/**
 * Service Implementation for managing Rotation.
 */
@Service
@Transactional
public class RotationServiceImpl implements RotationService {
    
    private final Logger log = LoggerFactory.getLogger(RotationServiceImpl.class);
    
    private final RotationRepository rotationRepository;
    
    private final ProgrammeRepository programmeRepository;
    
    private final RotationMapper rotationMapper;
    
    public RotationServiceImpl(RotationRepository rotationRepository, RotationMapper rotationMapper, ProgrammeRepository programmeRepository) {
        this.rotationRepository = rotationRepository;
        this.rotationMapper = rotationMapper;
        this.programmeRepository = programmeRepository;
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
        
        Page<Rotation> page = rotationRepository.findAll(pageable);
    
        return mapRotations(page);
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
        RotationDTO dto = rotationMapper.toDto(rotation);
        
        if (dto != null && dto.getProgrammeId() != null) {
            try {
                setProgrammeInfo(dto, programmeRepository.getOne(dto.getProgrammeId()));
            }
            catch (EntityNotFoundException ene) {
                log.info("Programme with id {} not found", dto.getProgrammeId());
            }
        }
        return dto;
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<RotationDTO> advancedSearchBySpecification(List<ColumnFilter> columnFilters, Pageable pageable) {
        
        List<Specification<Rotation>> specs = new ArrayList<>();
        if (columnFilters != null && !columnFilters.isEmpty()) {
            columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
        }
        
        Page<Rotation> result;
        if (!specs.isEmpty()) {
            Specifications<Rotation> fullSpec = Specifications.where(specs.get(0));
            //add the rest of the specs that made it in
            for (int i = 1; i < specs.size(); i++) {
                fullSpec = fullSpec.and(specs.get(i));
            }
            result = rotationRepository.findAll(fullSpec, pageable);
        } else {
            result = rotationRepository.findAll(pageable);
        }
    
        return mapRotations(result);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean rotationExists(String value) {
        return rotationRepository.findByName(value).isPresent();
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
    
    private Page<RotationDTO> mapRotations(Page<Rotation> page) {
        Set<Long> programmeIds = page.getContent().stream()
                .map(Rotation::getProgrammeId)
                .collect(Collectors.toSet());
        
        Map<Long, Programme> programmeMap = !programmeIds.isEmpty() ?
                programmeRepository.findByIdIn(programmeIds).stream()
                        .collect(Collectors.toMap(Programme::getId, Functions.identity()))
                : Collections.emptyMap();
        
        return page.map(rotationMapper::toDto)
                .map(rd -> {
                    setProgrammeInfo(rd, programmeMap.get(rd.getProgrammeId()));
                    return rd;
                });
    }

    private void setProgrammeInfo(RotationDTO rd, Programme p) {
        if (p != null) {
            rd.setProgrammeName(p.getProgrammeName());
            rd.setProgrammeNumber(p.getProgrammeNumber());
        }
    }
}
