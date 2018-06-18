package com.transformuk.hee.tis.tcs.service.service.helper;

import com.google.common.base.Preconditions;
import org.flywaydb.core.internal.util.scanner.classpath.ClassPathResource;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Supplies SQL queries as strings looking into the resources/queries/ folder for SQL files.
 * Caches the queries in memory so that it does not have to read the file more than once.
 */
@Component
public class SqlQuerySupplier {
    public static final String PERSON_VIEW = "queries/personView.sql";
    public static final String POST_VIEW = "queries/postView.sql";
    public static final String SEARCH_POST_VIEW = "queries/searchPostView.sql";
    public static final String TRAINEE_PLACEMENT_SUMMARY = "queries/placementSummaryForTrainee.sql";
    public static final String PLACEMENT_DETAILS = "queries/placementDetails.sql";
    public static final String PLACEMENT_SUPERVISOR = "queries/placementSupervisor.sql";
    public static final String POST_PLACEMENT_SUMMARY = "queries/placementSummaryForPost.sql";
    private static final Logger LOG = getLogger(SqlQuerySupplier.class);
    private Map<String, String> files;

    public SqlQuerySupplier() {
        files = new HashMap<>();
    }

    /**
     * Returns an SQL query ready to run
     *
     * @param fileName the sql file to get the query from, use one of the public string constants of this class as a
     *                 parameter here, never null
     * @return the sql query ready to run, never null
     * @throws IOException when the file is not found or there is a problem reading the file.
     */
    public String getQuery(final String fileName) {
        Preconditions.checkNotNull(fileName);
        final String content;
        if (files.containsKey(fileName)) {
            content = files.get(fileName);
        } else {
            content = new ClassPathResource(fileName, Thread.currentThread().getContextClassLoader()).loadAsString("UTF-8");
            files.put(fileName, content);
        }
        final String query = content;
        LOG.debug("Running query:\n {}", query);
        return query;
    }
}
