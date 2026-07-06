/*
 * The MIT License (MIT)
 *
 * Copyright 2026 Crown Copyright (NHS England)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.transformuk.hee.tis.tcs.service.service.helper;

import static org.slf4j.LoggerFactory.getLogger;

import com.google.common.base.Preconditions;
import com.google.common.io.CharStreams;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.flywaydb.core.internal.resource.classpath.ClassPathResource;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * Supplies SQL queries as strings looking into the resources/queries/ folder for SQL files. Caches
 * the queries in memory so that it does not have to read the file more than once.
 */
@Component
public class SqlQuerySupplier {

  public static final String PERSON_VIEW = "queries/personView.sql";
  public static final String PERSON_ES_VIEW = "queries/personViewForES.sql";
  public static final String PROGRAMME_MEMBERSHIP_VIEW = "queries/programmeMembership.sql";
  public static final String POST_VIEW = "queries/postView.sql";
  public static final String SEARCH_POST_VIEW = "queries/searchPostView.sql";
  public static final String TRAINEE_PLACEMENT_SUMMARY = "queries/placementSummaryForTrainee.sql";
  public static final String PLACEMENT_DETAILS = "queries/placementDetails.sql";
  public static final String PLACEMENT_SUPERVISOR = "queries/placementSupervisor.sql";
  public static final String POST_PLACEMENT_SUMMARY = "queries/placementSummaryForPost.sql";
  public static final String PERSON_PLACEMENT_EMPLOYINGBODY = "queries/personPlacementEmployingBody.sql";
  public static final String PERSON_PLACEMENT_TRAININGBODY = "queries/personPlacementTrainingBody.sql";
  public static final String POST_EMPLOYINGBODY = "queries/postEmployingBody.sql";
  public static final String POST_TRAININGBODY = "queries/postTrainingBody.sql";
  public static final String TRAINEE_CONNECTION_INFO = "queries/traineeInfoForConnection.sql";
  private static final Logger LOG = getLogger(SqlQuerySupplier.class);
  private Map<String, String> files;

  public SqlQuerySupplier() {
    files = new HashMap<>();
  }

  /**
   * Returns an SQL query ready to run
   *
   * @param fileName the sql file to get the query from, use one of the public string constants of
   *                 this class as a parameter here, never null
   * @return the sql query ready to run, never null
   */
  public String getQuery(final String fileName) {
    Preconditions.checkNotNull(fileName);
    String content = files.computeIfAbsent(fileName, this::readContents);
    LOG.debug("Running query:\n {}", content);
    return content;
  }

  /**
   * Read the file contents.
   *
   * @param fileName The name of the file to read.
   * @return The read contents.
   */
  private String readContents(String fileName) {
    String content = "";
    ClassPathResource resource = new ClassPathResource(null, fileName,
        Thread.currentThread().getContextClassLoader(), StandardCharsets.UTF_8);

    try (Reader reader = resource.read()) {
      content = CharStreams.toString(reader);
    } catch (IOException e) {
      LOG.error("Unable to load content from {}.", fileName);
    }

    return content;
  }
}
