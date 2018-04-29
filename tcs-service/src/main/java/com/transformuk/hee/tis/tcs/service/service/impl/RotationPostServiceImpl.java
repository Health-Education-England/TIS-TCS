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

import java.util.ArrayList;
import java.util.Collections;
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
    public RotationPostDTO save(RotationPostDTO rotationPostDTO) {
        log.debug("Request to save RotationPost : {}", rotationPostDTO);
        RotationPost rotationPost = rotationPostMapper.toEntity(rotationPostDTO);
        rotationPost = rotationPostRepository.save(rotationPost);
        return rotationPostMapper.toDto(rotationPost);
    }
    
    @Override
    @Transactional
    public List<RotationPostDTO> saveAll(List<RotationPostDTO> rotationPostDTOs) {
        if (!rotationPostDTOs.isEmpty()) {
            Long postId = rotationPostDTOs.get(0).getPostId();
            Long count = rotationPostRepository.deleteByPostId(postId);
            log.info("Deleted {} rotationPosts", count);
            return rotationPostDTOs.stream().map(this::save).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
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
     * Get one rotationPost by post id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public List<RotationPostDTO> findByPostId(Long id) {
        log.debug("Request to get RotationPost : {}", id);
        return rotationPostRepository.findByPostId(id)
                .stream().map(rotationPostMapper::toDto).collect(Collectors.toList());
    }
}
