package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PersonLite;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;

public class PersonRepositoryImpl implements CustomPersonRepository {

  private static final String BASE_QUERY_PERSON =
      "select distinct p.id, trim(concat(cd.forenames, ' ', cd.surname)) as name, p.publicHealthNumber, gmc.gmcNumber, gdc.gdcNumber \n"
          +
          "from Person p \n" +
          "left join ContactDetails cd \n" +
          "  on cd.id = p.id \n" +
          "left join GmcDetails gmc \n" +
          "  on gmc.id = p.id \n" +
          "left join GdcDetails gdc \n" +
          "  on gdc.id = p.id \n";
  private static final String TRAINER_APPROVAL_STATUS_QUERY =
          "join TrainerApproval ta \n" +
          "  on ta.personId = p.id and ta.approvalStatus = 'CURRENT' \n";
  private static final String BASE_QUERY_PERSON_SEARCH =
      "where p.status = 'CURRENT' \n" +
          "  and (surname like '%$(query)%'\n" +
          "  or forenames like '%$(query)%'\n" +
          "  or gmcNumber like '%$(query)%'\n" +
          "  or gdcNumber like '%$(query)%'\n" +
          "  or publicHealthNumber like '%$(query)%'\n" +
          "  )";
  private static final String BASE_QUERY_PERSON_ROLES = BASE_QUERY_PERSON +
      "join (\n" +
      "$(join)\n" +
      ") r \n" +
      "  on p.role like concat('%', r.code, '%') \n";
  private final Logger log = LoggerFactory.getLogger(PersonRepositoryImpl.class);
  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Override
  public Page<PersonLite> searchByRoleCategory(final String query, final Set<String> roles,
      final Pageable pageable, final boolean filterByTrainerApprovalStatus) {
    log.debug("Received request to search '{}' with roles '{}' and query '{}'",
        PersonLite.class.getSimpleName(), roles, query);

    final StopWatch stopWatch = new StopWatch();
    stopWatch.start();

    if (roles.isEmpty()) {
      return new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    final Iterator<String> rolesIterator = roles.iterator();

    final StringBuilder join = new StringBuilder();
    join.append("select '").append(rolesIterator.next()).append("' as code ");

    while (rolesIterator.hasNext()) {
      join.append("union all select '").append(rolesIterator.next()).append("' ");
    }

    final Map<String, String> values = new HashMap<>();
    values.put("join", join.toString());
    String sql = BASE_QUERY_PERSON_ROLES;

    if(filterByTrainerApprovalStatus){
      sql += TRAINER_APPROVAL_STATUS_QUERY;
    }

    if (StringUtils.isNotBlank(query)) {
      values.put("query", query);
      sql += BASE_QUERY_PERSON_SEARCH;
    }

    sql = new StrSubstitutor(values, "$(", ")").replace(sql);

    List<PersonLite> searchResult = jdbcTemplate.query(sql, new PersonLiteRowMapper());
    final int originalTotal = searchResult.size();

    final boolean hasNext = searchResult.size() > pageable.getPageSize();
    if (hasNext) {
      searchResult = searchResult.subList(
          (int) Math.min(searchResult.size(), pageable.getOffset()),
          (int) Math.min(searchResult.size(), pageable.getPageSize() + pageable.getOffset())
      );
    }
    stopWatch.stop();

    log.info("Request to search '{}' with roles '{}' and query '{}' finished; took '{}'ms",
        PersonLite.class.getSimpleName(), roles, query, stopWatch.getTotalTimeSeconds());

    if (CollectionUtils.isEmpty(searchResult)) {
      return new PageImpl<>(searchResult, pageable, 0);
    }

    return new PageImpl<>(searchResult, pageable, originalTotal);
  }

  public static class PersonLiteRowMapper implements RowMapper<PersonLite> {

    @Override
    public PersonLite mapRow(final ResultSet resultSet, final int i) throws SQLException {
      final PersonLite personLite = new PersonLite();
      personLite.setId(resultSet.getLong("id"));
      personLite.setName(resultSet.getString("name"));
      personLite.setPublicHealthNumber(resultSet.getString("publicHealthNumber"));
      personLite.setGmcNumber(resultSet.getString("gmcNumber"));
      personLite.setGdcNumber(resultSet.getString("gdcNumber"));

      return personLite;
    }
  }
}
