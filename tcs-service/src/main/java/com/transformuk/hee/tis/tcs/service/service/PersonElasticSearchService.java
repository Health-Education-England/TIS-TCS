package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import com.transformuk.hee.tis.tcs.service.job.person.PersonView;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.repository.PersonEsRepository;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonElasticSearchService {

  private static final Logger LOG = LoggerFactory.getLogger(PersonElasticSearchService.class);

  @Autowired
  private ElasticsearchOperations elasticsearchOperations;

  @Autowired
  private PersonEsRepository personEsRepository;

  public List<PersonViewDTO> searchForPage(Pageable pageable) {

    List<PersonView> content = personEsRepository.findAll(pageable).getContent();
    return convertPersonViewToDTO(content);
  }

  private List<PersonViewDTO> convertPersonViewToDTO(List<PersonView> content) {
    return content.stream().map(pv -> {
      PersonViewDTO personViewDTO = new PersonViewDTO();
      personViewDTO.setId(pv.getId());
      personViewDTO.setIntrepidId(pv.getIntrepidId());
      personViewDTO.setSurname(pv.getSurname());
      personViewDTO.setForenames(pv.getForenames());
      personViewDTO.setGmcNumber(pv.getGmcNumber());
      personViewDTO.setGdcNumber(pv.getGdcNumber());
      personViewDTO.setPublicHealthNumber(pv.getPublicHealthNumber());
      personViewDTO.setProgrammeId(pv.getProgrammeId());
      personViewDTO.setProgrammeName(pv.getProgrammeName());
      personViewDTO.setProgrammeNumber(pv.getProgrammeNumber());
      personViewDTO.setTrainingNumber(pv.getTrainingNumber());
      personViewDTO.setGradeId(pv.getGradeId());
      personViewDTO.setGradeAbbreviation(pv.getGradeAbbreviation());
      personViewDTO.setGradeName(pv.getGradeName());
      personViewDTO.setSiteId(pv.getSiteId());
      personViewDTO.setSiteCode(pv.getSiteCode());
      personViewDTO.setSiteName(pv.getSiteName());
      personViewDTO.setPlacementType(pv.getPlacementType());
      personViewDTO.setSpecialty(pv.getSpecialty());
      personViewDTO.setRole(pv.getRole());
      personViewDTO.setStatus(pv.getStatus());
      personViewDTO.setCurrentOwner(pv.getCurrentOwner());

      return personViewDTO;
    }).collect(Collectors.toList());
  }

  public List<PersonViewDTO> searchForPage(String searchQuery, List<ColumnFilter> columnFilters, Pageable pageable) {

    BoolQueryBuilder filterQuery = new BoolQueryBuilder();

    for (ColumnFilter columnFilter : columnFilters) {
      List<Object> values = columnFilter.getValues();
      for (Object value : values) {
        filterQuery = filterQuery.filter(new MatchQueryBuilder(columnFilter.getName(), value.toString()));
      }
    }

    BoolQueryBuilder shouldQuery = new BoolQueryBuilder();
    if (StringUtils.isNotEmpty(searchQuery)) {
      shouldQuery
          .should(new FuzzyQueryBuilder("publicHealthNumber", searchQuery))
          .should(new FuzzyQueryBuilder("surname", searchQuery))
          .should(new FuzzyQueryBuilder("forenames", searchQuery))
          .should(new MatchQueryBuilder("gmcNumber", searchQuery))
          .should(new MatchQueryBuilder("gdcNumber", searchQuery))
          .should(new FuzzyQueryBuilder("role", searchQuery));

      if (StringUtils.isNumeric(searchQuery)) {
        shouldQuery = shouldQuery.should(new TermQueryBuilder("id", searchQuery));
      }
    }

//    QueryBuilder filterQuery = new BoolQueryBuilder().filter(new TermQueryBuilder("status", "CURRENT"));
//    shouldQuery = shouldQuery.filter(new MatchQueryBuilder("status", "CURRENT"));



//    Page<PersonView> search = personEsRepository.search(shouldQuery, pageable);


    NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
        .withQuery(shouldQuery)
        .withFilter(filterQuery)
        .withPageable(pageable)
        .build();

    LOG.info("Query {}", nativeSearchQuery.getQuery().toString());
    List<PersonView> response = elasticsearchOperations.queryForList(nativeSearchQuery, PersonView.class);
    return convertPersonViewToDTO(response);
  }
}
