package com.transformuk.hee.tis.tcs.service.service.impl;

import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.containsLike;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.in;
import static com.transformuk.hee.tis.tcs.service.service.impl.SpecificationFactory.isEqual;

import com.transformuk.hee.tis.tcs.api.dto.CurriculumDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.model.Curriculum;
import com.transformuk.hee.tis.tcs.service.repository.CurriculumRepository;
import com.transformuk.hee.tis.tcs.service.service.CurriculumService;
import com.transformuk.hee.tis.tcs.service.service.mapper.CurriculumMapper;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Curriculum.
 */
@Service
@Transactional
public class CurriculumServiceImpl implements CurriculumService {

  private final Logger log = LoggerFactory.getLogger(CurriculumServiceImpl.class);

  private final CurriculumRepository curriculumRepository;

  private final CurriculumMapper curriculumMapper;

  public CurriculumServiceImpl(CurriculumRepository curriculumRepository,
      CurriculumMapper curriculumMapper) {
    this.curriculumRepository = curriculumRepository;
    this.curriculumMapper = curriculumMapper;
  }

  /**
   * Save a curriculum.
   *
   * @param curriculumDTO the entity to save
   * @return the persisted entity
   */
  @Override
  public CurriculumDTO save(CurriculumDTO curriculumDTO) {
    log.debug("Request to save Curriculum : {}", curriculumDTO);
    Curriculum curriculum = curriculumMapper.curriculumDTOToCurriculum(curriculumDTO);
    curriculum = curriculumRepository.save(curriculum);
    CurriculumDTO result = curriculumMapper.curriculumToCurriculumDTO(curriculum);
    return result;
  }

  @Override
  public List<CurriculumDTO> save(List<CurriculumDTO> curriculumDTOs) {
    log.debug("Request to save Curriculum : {}", curriculumDTOs);
    List<Curriculum> curriculums = curriculumMapper.curriculumDTOsToCurricula(curriculumDTOs);
    curriculums = curriculumRepository.saveAll(curriculums);
    List<CurriculumDTO> result = curriculumMapper.curriculaToCurriculumDTOs(curriculums);
    return result;
  }

  @Override
  @Transactional(readOnly = true)
  public Page<CurriculumDTO> advancedSearch(
      String searchString, List<ColumnFilter> columnFilters, Pageable pageable, boolean current) {

    List<Specification<Curriculum>> specs = new ArrayList<>();
    //add the text search criteria
    if (StringUtils.isNotEmpty(searchString)) {
      specs.add(Specifications.where(containsLike("name", searchString)));
    }

    //add status
    if (current) {
      specs.add(Specifications.where(isEqual("status", Status.CURRENT)));
    }

    //add the column filters criteria
    if (columnFilters != null && !columnFilters.isEmpty()) {
      columnFilters.forEach(cf -> specs.add(in(cf.getName(), cf.getValues())));
    }

    //specs may be empty if passing an empty column filters object
    if (specs.isEmpty()) {
      return findAll(pageable);
    } else {
      Specifications<Curriculum> fullSpec = Specifications.where(specs.get(0));
      //add the rest of the specs that made it in
      for (int i = 1; i < specs.size(); i++) {
        fullSpec = fullSpec.and(specs.get(i));
      }
      Page<Curriculum> result = curriculumRepository.findAll(fullSpec, pageable);
      return result.map(curriculum -> curriculumMapper.curriculumToCurriculumDTO(curriculum));
    }
  }

  /**
   * Get all the curricula.
   *
   * @param pageable the pagination information
   * @return the list of entities
   */
  @Override
  @Transactional(readOnly = true)
  public Page<CurriculumDTO> findAll(Pageable pageable) {
    log.debug("Request to get all Curricula");
    Page<Curriculum> result = curriculumRepository.findAll(pageable);
    return result.map(curriculum -> curriculumMapper.curriculumToCurriculumDTO(curriculum));
  }

  @Override
  public Page<CurriculumDTO> findAllCurrent(Pageable pageable) {
    log.debug("Request to get all Curricula");
    Curriculum example = new Curriculum().status(Status.CURRENT);
    Page<Curriculum> result = curriculumRepository.findAll(Example.of(example), pageable);
    return result.map(curriculum -> curriculumMapper.curriculumToCurriculumDTO(curriculum));
  }

  /**
   * Get one curriculum by id.
   *
   * @param id the id of the entity
   * @return the entity
   */
  @Override
  @Transactional(readOnly = true)
  public CurriculumDTO findOne(Long id) {
    log.debug("Request to get Curriculum : {}", id);
    Curriculum curriculum = curriculumRepository.findCurriculaByIdEagerFetch(id).orElse(null);
    CurriculumDTO curriculumDTO = curriculumMapper.curriculumToCurriculumDTO(curriculum);
    return curriculumDTO;
  }

  /**
   * Delete the  curriculum by id.
   *
   * @param id the id of the entity
   */
  @Override
  public void delete(Long id) {
    log.debug("Request to delete Curriculum : {}", id);
    curriculumRepository.deleteById(id);
  }
}
