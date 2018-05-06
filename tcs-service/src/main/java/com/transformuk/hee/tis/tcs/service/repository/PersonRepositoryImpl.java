package com.transformuk.hee.tis.tcs.service.repository;

import com.transformuk.hee.tis.tcs.service.model.PersonLite;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PersonRepositoryImpl implements CustomPersonRepository {
    private final Logger log = LoggerFactory.getLogger(PersonRepositoryImpl.class);

    private static final String BASE_QUERY =
            "select distinct p.id, trim(concat(cd.forenames, ' ', cd.surname)) as name, p.publicHealthNumber, gmc.gmcNumber, gdc.gdcNumber \n" +
                    "from Person p \n" +
                    "left join ContactDetails cd \n" +
                    "  on cd.id = p.id \n" +
                    "left join GmcDetails gmc \n" +
                    "  on gmc.id = p.id \n" +
                    "left join GdcDetails gdc \n" +
                    "  on gdc.id = p.id \n" +
                    "join (\n" +
                    "$(join)\n" +
                    ") r \n" +
                    "  on p.role like concat('%', r.code, '%') \n" +
                    "where p.status = 'CURRENT' \n" +
                    "  and (surname like '%$(query)%'\n" +
                    "  or forenames like '%$(query)%'\n" +
                    "  or knownAs like '%$(query)%'\n" +
                    "  or maidenName like '%$(query)%'\n" +
                    "  or legalSurname like '%$(query)%'\n" +
                    "  or legalForenames like '%$(query)%'\n" +
                    "  or gmcNumber like '%$(query)%'\n" +
                    "  or gdcNumber like '%$(query)%'\n" +
                    "  )";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Collection<PersonLite> searchByRoleCategory(final String query, final Set<String> roles) {
        log.debug("Received request to search '{}' with roles '{}' and query '{}'",
                PersonLite.class.getSimpleName(), roles, query);

        if (roles.isEmpty()) {
            return Collections.emptyList();
        }

        final Iterator<String> rolesIterator = roles.iterator();

        final StringBuilder join = new StringBuilder();
        join.append("select '").append(rolesIterator.next()).append("' as code ");

        while (rolesIterator.hasNext()) {
            join.append("union all select '").append(rolesIterator.next()).append("' ");
        }

        final Map<String, String> values = new HashMap<>();
        values.put("join", join.toString());
        values.put("query", query);

        final String sql = new StrSubstitutor(values, "$(", ")").replace(BASE_QUERY);

        return jdbcTemplate.query(sql, new PersonLiteRowMapper());
    }

    private class PersonLiteRowMapper implements RowMapper<PersonLite> {
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
