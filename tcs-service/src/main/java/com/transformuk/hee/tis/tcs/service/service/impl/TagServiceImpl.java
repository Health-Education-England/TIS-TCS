package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.TagDTO;
import com.transformuk.hee.tis.tcs.service.model.Tag;
import com.transformuk.hee.tis.tcs.service.repository.TagRepository;
import com.transformuk.hee.tis.tcs.service.service.TagService;
import com.transformuk.hee.tis.tcs.service.service.mapper.TagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    private static final Logger LOG = LoggerFactory.getLogger(TagServiceImpl.class);

    private final TagRepository tagRepository;
    private final TagMapper tagMapper;

    public TagServiceImpl(final TagRepository tagRepository, final TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public Collection<TagDTO> findByNameStartingWithOrderByName(final String query) {
        LOG.debug("Received request to find all '{}' with name starting with '{}'", TagDTO.class.getSimpleName(), query);

        final Collection<Tag> tags = tagRepository.findByNameStartingWithOrderByName(query);

        return tagMapper.toDto(tags);
    }
}
