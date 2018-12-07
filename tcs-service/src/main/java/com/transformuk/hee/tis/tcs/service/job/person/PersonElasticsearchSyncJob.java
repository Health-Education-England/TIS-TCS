package com.transformuk.hee.tis.tcs.service.job.person;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.impl.PersonViewRowMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@ManagedResource(objectName = "tcs.mbean:name=PersonElasticSearchJob",
    description = "Service that clears the tcs-person index in ES and repopulates the data")
public class PersonElasticsearchSyncJob {

  private static final Logger LOG = LoggerFactory.getLogger(PersonElasticsearchSyncJob.class);

  private Stopwatch mainStopWatch;

  protected static final int DEFAULT_PAGE_SIZE = 10_000;

  @Autowired
  private SqlQuerySupplier sqlQuerySupplier;
  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  private ElasticsearchOperations elasticSearchOperations;


  @ManagedOperation(description = "Is the Person es sync just currently running")
  public boolean isCurrentlyRunning() {
    return mainStopWatch != null;
  }

  @ManagedOperation(description = "The current elapsed time of the current sync job")
  public String elapsedTime() {
    return mainStopWatch != null ? mainStopWatch.toString() : "0s";
  }

  protected void runSyncJob() {
    if (mainStopWatch != null) {
      LOG.info("Sync job [{}] already running, exiting this execution", getJobName());
      return;
    }
    CompletableFuture.runAsync(this::run);
  }

  @ManagedOperation(description = "Run sync of the tcs-person index")
  public void PersonElasticSearchSync() {
    runSyncJob();
  }

  private String getJobName() {
    return "Person sync job";
  }

  private int getPageSize() {
    return DEFAULT_PAGE_SIZE;
  }

  private void deleteData() {
    elasticSearchOperations.deleteIndex("tcs-person");
  }

  private List<PersonView> collectData(int page, int pageSize) {
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.PERSON_VIEW);
    String limitClause = "limit " + pageSize + " offset " + page * pageSize;
    query = query.replace("TRUST_JOIN", "")
        .replace("WHERECLAUSE", "")
        .replace("ORDERBYCLAUSE", "ORDER BY id DESC")
        .replace("LIMITCLAUSE", limitClause);

    MapSqlParameterSource paramSource = new MapSqlParameterSource();

    return namedParameterJdbcTemplate.query(query, paramSource, new PersonViewRowMapper());
  }

  private void sendToEs(List<PersonView> dataToSave) {
    if (CollectionUtils.isNotEmpty(dataToSave)) {
      LOG.info("sending data");
      List<IndexQuery> documentsToIndex = Lists.newArrayList();
      for (PersonView personView : dataToSave) {
        IndexQuery indexQuery = new IndexQuery();
        indexQuery.setIndexName("tcs-person");
        indexQuery.setObject(personView);
        indexQuery.setType("person");
        documentsToIndex.add(indexQuery);
      }

      LOG.info("doing bulk");
      elasticSearchOperations.bulkIndex(documentsToIndex);
    }
  }

  protected void run() {

    try {
      LOG.info("Sync [{}] started", getJobName());
      mainStopWatch = Stopwatch.createStarted();
      Stopwatch stopwatch = Stopwatch.createStarted();

      int totalRecords = 0;
      int page = 0;
      boolean hasMoreResults = true;

      LOG.info("deleting all data");
      deleteData();
      LOG.info("deleted all data took {}", stopwatch.toString());
      stopwatch.reset().start();

      while (hasMoreResults) {

        List<PersonView> collectedData = collectData(page, getPageSize());
        page++;

        hasMoreResults = collectedData.size() > 0;

        LOG.info("Time taken to read chunk : [{}]", stopwatch.toString());
        if (CollectionUtils.isNotEmpty(collectedData)) {
          totalRecords += collectedData.size();
        }
        stopwatch.reset().start();

        sendToEs(collectedData);

        LOG.info("Time taken to save chunk : [{}]", stopwatch.toString());
      }
      stopwatch.reset().start();
      LOG.info("Sync job [{}] finished. Total time taken {} for processing [{}] records", getJobName(), mainStopWatch.stop().toString(), totalRecords);
      mainStopWatch = null;
    } catch (Exception e) {
      e.printStackTrace();
      mainStopWatch = null;
    }
  }

}
