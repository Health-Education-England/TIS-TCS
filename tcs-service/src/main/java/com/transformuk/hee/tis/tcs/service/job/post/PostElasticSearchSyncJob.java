package com.transformuk.hee.tis.tcs.service.job.post;

import com.google.common.base.Stopwatch;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import com.transformuk.hee.tis.tcs.service.service.impl.PostElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.impl.PostViewRowMapper;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.index.IndexNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@ManagedResource(objectName = "tcs.mbean:name=PostElasticSearchJob",
    description = "Service that clears the Posts index in ES and repopulates the data")
public class PostElasticSearchSyncJob {

  private static final Logger LOG = LoggerFactory.getLogger(PostElasticSearchSyncJob.class);
  private static final String ES_INDEX = "posts";
  private static final int FIFTEEN_MIN = 15 * 60 * 1000;

  private Stopwatch mainStopWatch;

  protected static final int DEFAULT_PAGE_SIZE = 10_000;

  @Autowired
  private SqlQuerySupplier sqlQuerySupplier;
  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired
  private ElasticsearchOperations elasticSearchOperations;

  @Autowired
  private PostElasticSearchService postElasticSearchService;

  @ManagedOperation(description = "Is the Post es sync just currently running")
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

  @Scheduled(cron = "0 0 2 * * *")
  @SchedulerLock(name = "postsElasticSearchScheduledTask", lockAtLeastFor = FIFTEEN_MIN, lockAtMostFor = FIFTEEN_MIN)
  @ManagedOperation(description = "Run sync of the posts es index")
  public void postsElasticSearchSync() {
    runSyncJob();
  }

  private String getJobName() {
    return "Posts ES sync job";
  }

  private int getPageSize() {
    return DEFAULT_PAGE_SIZE;
  }

  private void deleteIndex() {
    LOG.info("deleting posts es index");
    try {
      elasticSearchOperations.deleteIndex(ES_INDEX);
    } catch (IndexNotFoundException e) {
      LOG.info("Could not delete an index that does not exist, continuing");
    }
  }

  private List<PostView> collectData(int page, int pageSize) {
    String query = sqlQuerySupplier.getQuery(SqlQuerySupplier.POST_VIEW);
    String limitClause = "limit " + pageSize + " offset " + page * pageSize;
    query = query.replace("TRUST_JOIN", "")
        .replace("WHERECLAUSE", "")
        .replace("ORDERBYCLAUSE", "ORDER BY id DESC")
        .replace("LIMITCLAUSE", limitClause);

    MapSqlParameterSource paramSource = new MapSqlParameterSource();
    List<PostView> queryResult = namedParameterJdbcTemplate.query(query, paramSource, new PostViewRowMapper());
    return queryResult;
  }

  protected void run() {

    try {
      LOG.info("Sync [{}] started", getJobName());
      mainStopWatch = Stopwatch.createStarted();
      Stopwatch stopwatch = Stopwatch.createStarted();

      int totalRecords = 0;
      int page = 0;
      boolean hasMoreResults = true;

      deleteIndex();
      createIndex();
      stopwatch.reset().start();

      while (hasMoreResults) {

        List<PostView> collectedData = collectData(page, getPageSize());
        page++;

        hasMoreResults = collectedData.size() > 0;

        LOG.info("Time taken to read chunk : [{}]", stopwatch.toString());
        if (CollectionUtils.isNotEmpty(collectedData)) {
          totalRecords += collectedData.size();
        }
        stopwatch.reset().start();

        postElasticSearchService.saveDocuments(collectedData);

        LOG.info("Time taken to save chunk : [{}]", stopwatch.toString());
      }
      elasticSearchOperations.refresh(PostView.class);
      stopwatch.reset().start();
      LOG.info("Sync job [{}] finished. Total time taken {} for processing [{}] records", getJobName(), mainStopWatch.stop().toString(), totalRecords);
      mainStopWatch = null;
    } catch (Exception e) {
      e.printStackTrace();
      mainStopWatch = null;
    }
  }

  private void createIndex() {
    LOG.info("creating and updating mappings");
    elasticSearchOperations.createIndex(ES_INDEX);
    elasticSearchOperations.putMapping(PostView.class);
  }
}
