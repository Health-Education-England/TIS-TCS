package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO;
import com.transformuk.hee.tis.tcs.service.api.decorator.PostViewDecorator;
import com.transformuk.hee.tis.tcs.service.api.util.BasicPage;
import com.transformuk.hee.tis.tcs.service.job.post.CurrentTrainee;
import com.transformuk.hee.tis.tcs.service.job.post.FundingType;
import com.transformuk.hee.tis.tcs.service.job.post.PostView;
import com.transformuk.hee.tis.tcs.service.job.post.ProgrammeName;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.repository.PostElasticSearchRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Component
public class PostElasticSearchService {

  @Autowired
  private PostElasticSearchRepository postElasticSearchRepository;

  @Autowired
  private PostViewDecorator postViewDecorator;

  public void saveDocuments(List<PostView> collectedData) {
    if (CollectionUtils.isNotEmpty(collectedData)) {
      postElasticSearchRepository.saveAll(collectedData);
    }
  }

  public BasicPage<PostViewDTO> searchForPage(String searchQuery, List<ColumnFilter> columnFilters, Pageable pageable) {
    BoolQueryBuilder mustBetweenDifferentColumnFilters = new BoolQueryBuilder();
    Page<PostView> result = postElasticSearchRepository.search(mustBetweenDifferentColumnFilters, pageable);
    List<PostViewDTO> postViewDTOS = convertPostViewToDTO(result.getContent());
    postViewDecorator.decorate(postViewDTOS);
    return new BasicPage<>(postViewDTOS, pageable, result.hasNext());

  }

  private List<PostViewDTO> convertPostViewToDTO(List<PostView> content) {
    return content.stream().map(pv -> {
      PostViewDTO postViewDTO = new PostViewDTO();
      postViewDTO.setId(pv.getId());
      postViewDTO.setNationalPostNumber(pv.getNationalPostNumber());
      postViewDTO.setPrimarySiteId(pv.getPrimarySiteId());
      postViewDTO.setApprovedGradeId(pv.getApprovedGradeId());
      postViewDTO.setPrimarySpecialtyId(pv.getPrimarySpecialty());
      Set<String> programmeName = pv.getProgrammeName().stream().map(ProgrammeName::getProgrammeName).collect(toSet());
      postViewDTO.setProgrammeNames(String.join(", ", programmeName));
      Set<String> currentTrainee = pv.getCurrentTrainee().stream().map(CurrentTrainee::getCurrentTrainee).collect(toSet());
      postViewDTO.setCurrentTraineeForenames(String.join(", ", currentTrainee));
      Set<String> fundingType = pv.getFundingType().stream().map(FundingType::getFundingType).collect(toSet());
      postViewDTO.setFundingType(String.join(", ", fundingType));
      postViewDTO.setStatus(pv.getStatus());
      postViewDTO.setOwner(pv.getOwner());
      return postViewDTO;
    }).collect(Collectors.toList());
  }
}
