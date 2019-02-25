package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.collect.ImmutableMap;
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
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Component
public class PostElasticSearchService {

  @Autowired
  private PostElasticSearchRepository postElasticSearchRepository;

  @Autowired
  private PostViewDecorator postViewDecorator;

  //map of field names that are nested against the nest path value
  private Map<String, String> nestedPostFields;

  @PostConstruct
  public void postConstruct() {
    this.nestedPostFields = ImmutableMap.of("programmeNames", "programmeNames.programmeName", "fundingType", "fundingType");
  }

  public void saveDocuments(List<PostView> collectedData) {
    if (CollectionUtils.isNotEmpty(collectedData)) {
      postElasticSearchRepository.saveAll(collectedData);
    }
  }

  public BasicPage<PostViewDTO> searchForPage(String searchQuery, List<ColumnFilter> columnFilters, Pageable pageable) {
    BoolQueryBuilder mustBetweenDifferentColumnFilters = new BoolQueryBuilder();

    if (CollectionUtils.isNotEmpty(columnFilters)) {
      for (ColumnFilter columnFilter : columnFilters) {
        BoolQueryBuilder shouldBetweenSameColumnFilter = new BoolQueryBuilder();
        for (Object value : columnFilter.getValues()) {
//          if (appliedFilters.contains(columnFilter.getName())) { // skip if we've already applied this type of filter via role based filters
//            continue;
//          }

          //if the column filter is filtering on a nested field, do a nested search
          if (nestedPostFields.containsKey(columnFilter.getName())) {
            shouldBetweenSameColumnFilter.should(new NestedQueryBuilder(columnFilter.getName(),
                new MatchQueryBuilder(nestedPostFields.get(columnFilter.getName()), columnFilter.getValues()), ScoreMode.None));
          } else {
            shouldBetweenSameColumnFilter.should(new MatchQueryBuilder(columnFilter.getName(), value.toString()));
          }
        }
        mustBetweenDifferentColumnFilters.must(shouldBetweenSameColumnFilter);
      }
    }


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
      if (CollectionUtils.isNotEmpty(pv.getProgrammeNames())) {
        Set<String> programmeName = pv.getProgrammeNames().stream().map(ProgrammeName::getProgrammeName).collect(toSet());
        postViewDTO.setProgrammeNames(String.join(", ", programmeName));
      }
      if (CollectionUtils.isNotEmpty(pv.getCurrentTrainee())) {
        Set<String> currentTrainee = pv.getCurrentTrainee().stream().map(CurrentTrainee::getCurrentTrainee).collect(toSet());
        postViewDTO.setCurrentTraineeForenames(String.join(", ", currentTrainee));
      }
      if (CollectionUtils.isNotEmpty(pv.getFundingType())) {
        Set<String> fundingType = pv.getFundingType().stream().map(FundingType::getFundingType).collect(toSet());
        postViewDTO.setFundingType(String.join(", ", fundingType));
      }
      postViewDTO.setStatus(pv.getStatus());
      postViewDTO.setOwner(pv.getOwner());
      return postViewDTO;
    }).collect(Collectors.toList());
  }
}
