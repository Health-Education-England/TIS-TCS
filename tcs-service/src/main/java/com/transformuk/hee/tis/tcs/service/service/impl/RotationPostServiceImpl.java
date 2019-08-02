package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.RotationPostDTO;
import com.transformuk.hee.tis.tcs.service.model.RotationPost;
import com.transformuk.hee.tis.tcs.service.repository.RotationPostRepository;
import com.transformuk.hee.tis.tcs.service.service.RotationPostService;
import com.transformuk.hee.tis.tcs.service.service.mapper.RotationPostMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RotationPostServiceImpl implements RotationPostService {

  private static final Logger LOG = LoggerFactory.getLogger(RotationPostServiceImpl.class);

  private final RotationPostRepository rotationPostRepository;
  private final RotationPostMapper rotationPostMapper;

  public RotationPostServiceImpl(final RotationPostRepository rotationPostRepository,
      final RotationPostMapper rotationPostMapper) {
    this.rotationPostRepository = rotationPostRepository;
    this.rotationPostMapper = rotationPostMapper;
  }

  public RotationPostDTO save(final RotationPostDTO rotationPostDTO) {
    LOG.debug("Request to save RotationPost : {}", rotationPostDTO);

    final RotationPost rotationPost = rotationPostMapper.toEntity(rotationPostDTO);
    rotationPostRepository.save(rotationPost);

    return rotationPostMapper.toDto(rotationPost);
  }

  @Override
  @Transactional
  public List<RotationPostDTO> saveAll(final List<RotationPostDTO> newRotationPostDTOs) {
    if (newRotationPostDTOs.isEmpty()) {
      return Collections.emptyList();
    }

    final Long postId = newRotationPostDTOs.get(0).getPostId();

    final List<RotationPostDTO> existingRotationPostDTOs = findByPostId(postId);
    final List<RotationPostDTO> rotationPostDTOS = new ArrayList<>(
        combineRotationPost(existingRotationPostDTOs, new HashSet<>(newRotationPostDTOs)));

    final Long totalRotationPostDeleted = rotationPostRepository.deleteByPostId(postId);
    rotationPostRepository.flush();
    LOG.info("Deleted '{}' RotationPosts with postId '{}'", totalRotationPostDeleted, postId);

    rotationPostRepository.saveAll(rotationPostMapper.toEntity(rotationPostDTOS));

    return rotationPostDTOS;
  }

  @Override
  @Transactional(readOnly = true)
  public List<RotationPostDTO> findAll() {
    LOG.debug("Request to get all RotationPosts");

    return rotationPostRepository.findAll().stream()
        .map(rotationPostMapper::toDto)
        .collect(Collectors.toCollection(LinkedList::new));
  }

  @Override
  @Transactional(readOnly = true)
  public List<RotationPostDTO> findByPostId(final Long id) {
    LOG.debug("Request to get RotationPost : {}", id);

    return rotationPostRepository.findByPostId(id)
        .stream().map(rotationPostMapper::toDto).collect(Collectors.toList());
  }

  @Override
  public void delete(final Long postId) {
    LOG.debug("Request to delete RotationPost : {}", postId);

    rotationPostRepository.deleteByPostId(postId);
  }

  private Set<RotationPostDTO> combineRotationPost(final List<RotationPostDTO> existingRotations,
      final Set<RotationPostDTO> changedRotations) {
    // filters deleted RotationPost
    final Set<RotationPostDTO> deletedTags = existingRotations.stream()
        .filter(tag -> Optional.ofNullable(changedRotations)
            .orElse(Collections.emptySet())
            .contains(new RotationPostDTO(tag.getRotationId(), tag.getPostId())))
        .collect(Collectors.toSet());

    // combines added RotationPost
    final Stream<RotationPostDTO> combinedTags = Stream.concat(
        Optional.ofNullable(deletedTags).orElse(Collections.emptySet()).stream(),
        Optional.ofNullable(changedRotations).orElse(Collections.emptySet()).stream()
    );

    return combinedTags.collect(Collectors.toSet());
  }
}
