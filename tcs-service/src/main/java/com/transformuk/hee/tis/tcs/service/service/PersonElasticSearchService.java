package com.transformuk.hee.tis.tcs.service.service;

import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import com.transformuk.hee.tis.tcs.service.job.person.PersonView;
import com.transformuk.hee.tis.tcs.service.model.ColumnFilter;
import com.transformuk.hee.tis.tcs.service.repository.PersonEsRepository;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.impl.PersonViewRowMapper;
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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonElasticSearchService {

  private static final Logger LOG = LoggerFactory.getLogger(PersonElasticSearchService.class);

  @Autowired
  private PersonEsRepository personEsRepository;
  @Autowired
  private SqlQuerySupplier sqlQuerySupplier;
  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public List<PersonViewDTO> searchForPage(Pageable pageable) {
    List<PersonView> content = personEsRepository.findAll(pageable).getContent();
    return convertPersonViewToDTO(content);
  }

  public List<PersonViewDTO> searchForPage(String searchQuery, List<ColumnFilter> columnFilters, Pageable pageable) {

    // iterate over the column filters, if they have multiple values per filter, place a should between then
    // for each column filter set, place a must between them
    BoolQueryBuilder mustBetweenDifferentColumnFilters = new BoolQueryBuilder();
    for (ColumnFilter columnFilter : columnFilters) {

      BoolQueryBuilder shouldBetweenSameColumnFilter = new BoolQueryBuilder();
      for (Object value : columnFilter.getValues()) {

        if (StringUtils.equals(columnFilter.getName(), "status")) {
          value = value.toString().toUpperCase();
        }

        shouldBetweenSameColumnFilter.should(new MatchQueryBuilder(columnFilter.getName(), value));
      }

      mustBetweenDifferentColumnFilters.must(shouldBetweenSameColumnFilter);
    }

    // this part is the free text part of the query, place a should between all of the searchable fields
    BoolQueryBuilder shouldQuery = new BoolQueryBuilder();
    if (StringUtils.isNotEmpty(searchQuery)) {
      searchQuery = StringUtils.remove(searchQuery, '"');
      shouldQuery
          .should(new MatchQueryBuilder("publicHealthNumber", searchQuery))
          .should(new FuzzyQueryBuilder("fullName", searchQuery))
          .should(new FuzzyQueryBuilder("surname", searchQuery))
          .should(new FuzzyQueryBuilder("forenames", searchQuery))
          .should(new MatchQueryBuilder("gmcNumber", searchQuery))
          .should(new MatchQueryBuilder("gdcNumber", searchQuery))
          .should(new MatchQueryBuilder("role", searchQuery));

      if (StringUtils.isNumeric(searchQuery)) {
        shouldQuery = shouldQuery.should(new TermQueryBuilder("id", searchQuery));
      }
    }

    // add the free text query with a must to the column filters query
    BoolQueryBuilder fullQuery = mustBetweenDifferentColumnFilters.must(shouldQuery);

    LOG.info("Query {}", fullQuery.toString());
    Page<PersonView> result = personEsRepository.search(fullQuery, pageable);
    return convertPersonViewToDTO(result.getContent());
  }

  public void updatePersonDocument(Long personId) {
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PERSON_VIEW);
    query = query.replace("TRUST_JOIN", "")
        .replace("WHERECLAUSE", "WHERE p.id=:id")
        .replace("ORDERBYCLAUSE", "ORDER BY id DESC")
        .replace("LIMITCLAUSE", "");

    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    paramSource.addValue("id", personId);

    List<PersonView> queryResult = namedParameterJdbcTemplate.query(query, paramSource, new PersonViewRowMapper());
    queryResult.stream().forEach(pv -> pv.setFullName(pv.getForenames() + " " + pv.getSurname()));

    personEsRepository.saveAll(queryResult);

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
}
