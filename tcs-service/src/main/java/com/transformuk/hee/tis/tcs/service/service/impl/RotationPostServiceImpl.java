package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.service.service.RotationPostService;
import com.transformuk.hee.tis.tcs.service.model.RotationPost;
import com.transformuk.hee.tis.tcs.service.repository.RotationPostRepository;
import com.transformuk.hee.tis.tcs.api.dto.RotationPostDTO;
import com.transformuk.hee.tis.tcs.service.service.mapper.RotationPostMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing RotationPost.
 */
@Service
@Transactional
public class RotationPostServiceImpl implements RotationPostService {

    private final Logger log = LoggerFactory.getLogger(RotationPostServiceImpl.class);

    private final RotationPostRepository rotationPostRepository;

    private final RotationPostMapper rotationPostMapper;

    public RotationPostServiceImpl(RotationPostRepository rotationPostRepository, RotationPostMapper rotationPostMapper) {
        this.rotationPostRepository = rotationPostRepository;
        this.rotationPostMapper = rotationPostMapper;
    }

    /**
     * Save a rotationPost.
     *
     * @param rotationPostDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public RotationPostDTO save(RotationPostDTO rotationPostDTO) {
        log.debug("Request to save RotationPost : {}", rotationPostDTO);
        RotationPost rotationPost = rotationPostMapper.toEntity(rotationPostDTO);
        rotationPost = rotationPostRepository.save(rotationPost);
        return rotationPostMapper.toDto(rotationPost);
    }

    /**
     * Get all the rotationPosts.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<RotationPostDTO> findAll() {
        log.debug("Request to get all RotationPosts");
        return rotationPostRepository.findAll().stream()
            .map(rotationPostMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one rotationPost by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public RotationPostDTO findOne(Long id) {
        log.debug("Request to get RotationPost : {}", id);
        RotationPost rotationPost = rotationPostRepository.findOne(id);
        return rotationPostMapper.toDto(rotationPost);
    }

    /**
     * Delete the rotationPost by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RotationPost : {}", id);
        rotationPostRepository.delete(id);
    }
}
