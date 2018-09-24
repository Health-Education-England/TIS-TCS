package com.transformuk.hee.tis.tcs.service.job;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.tcs.service.command.FindSitesInCommand;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public abstract class TrustAdminSyncJobTemplate<ENTITY> {

  private static final Logger LOG = LoggerFactory.getLogger(TrustAdminSyncJobTemplate.class);

  private Stopwatch mainStopWatch;

  protected static final int DEFAULT_PAGE_SIZE = 5000;

  @ManagedOperation(description = "Is the Post Trust sync just currently running")
  public boolean isCurrentlyRunning() {
    return mainStopWatch != null;
  }

  @ManagedOperation(description = "The current elapsed time of the current sync job")
  public String elaspedTime() {
    return mainStopWatch != null ? mainStopWatch.toString() : "0s";
  }

  protected void runSyncJob() {
    if (mainStopWatch != null) {
      LOG.info("Sync job [{}] already running, exiting this execution", getJobName());
      return;
    }
    CompletableFuture.runAsync(this::run);
  }

  protected abstract String getJobName();

  protected abstract int getPageSize();

  protected abstract EntityManagerFactory getEntityManagerFactory();

  protected abstract void deleteData();

  protected abstract List<EntityData> collectData(int pageSize, long lastId, long lastSiteId, EntityManager entityManager);

  protected abstract int convertData(int skipped, Set<ENTITY> entitiesToSave, List<EntityData> entityData, EntityManager entityManager);

  private void run() {
    try {
      LOG.info("Sync [{}] started", getJobName());
      mainStopWatch = Stopwatch.createStarted();
      Stopwatch stopwatch = Stopwatch.createStarted();

      int skipped = 0, totalRecords = 0;
      long lastEntityId = 0;
      long lastSiteId = 0;
      boolean hasMoreResults = true;

      LOG.info("deleting all data");
      deleteData();
      LOG.info("deleted all data {}", stopwatch.toString());
      stopwatch.reset().start();

      Set<ENTITY> dataToSave = Sets.newHashSet();

      while (hasMoreResults) {

        EntityManager entityManager = getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        List<EntityData> collectedData = collectData(getPageSize(), lastEntityId, lastSiteId, entityManager);
        hasMoreResults = collectedData.size() > 0;
        LOG.info("Time taken to read chunk : [{}]", stopwatch.toString());

        if (CollectionUtils.isNotEmpty(collectedData)) {
          lastEntityId = collectedData.get(collectedData.size() - 1).getEntityId();
          lastSiteId = collectedData.get(collectedData.size() - 1).getOtherId();
          totalRecords += collectedData.size();
          skipped = convertData(skipped, dataToSave, collectedData, entityManager);
        }

        stopwatch.reset().start();
        dataToSave.forEach(entityManager::persist);
        entityManager.flush();
        dataToSave.clear();

        transaction.commit();
        entityManager.close();
        LOG.info("Time taken to save chunk : [{}]", stopwatch.toString());
      }
      stopwatch.reset().start();
      LOG.info("Sync job [{}] finished. Total time taken {} for processing [{}] records", getJobName(),
          mainStopWatch.stop().toString(), totalRecords);
      LOG.info("Skipped records {}", skipped);
    } catch (Exception e) {
      LOG.error("An error occurred while running the scheduled job", e);
    } finally {
      mainStopWatch = null;
    }
  }


  class EntityData {
    private Long entityId;
    private Long otherId;

    public Long getEntityId() {
      return entityId;
    }

    public EntityData entityId(Long entityId) {
      this.entityId = entityId;
      return this;
    }

    public Long getOtherId() {
      return otherId;
    }

    public EntityData otherId(Long otherId) {
      this.otherId = otherId;
      return this;
    }
  }
}
