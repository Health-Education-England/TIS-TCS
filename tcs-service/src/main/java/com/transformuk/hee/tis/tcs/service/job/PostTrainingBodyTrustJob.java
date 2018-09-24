package com.transformuk.hee.tis.tcs.service.job;

import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This job runs on a daily basis and MUST be run after the PostEmployingBodyTrustJob.
 * <p>
 * Its purpose is to populate the PostTrust table with post ids and the linked training body trust id
 */
@Component
@ManagedResource(objectName = "tcs.mbean:name=PostTrainingBodyTrustJob",
    description = "Service that links Post with Training Body Trusts")
public class PostTrainingBodyTrustJob extends TrustAdminSyncJobTemplate<PostTrust> {

  private static final Logger LOG = LoggerFactory.getLogger(PostEmployingBodyTrustJob.class);
  private static final int FIFTEEN_MIN = 15 * 60 * 1000;

  @Autowired
  private EntityManagerFactory entityManagerFactory;


  @Scheduled(cron = "0 20 1 * * *")
  @SchedulerLock(name = "postTrustScheduledTask", lockAtLeastFor = FIFTEEN_MIN, lockAtMostFor = FIFTEEN_MIN)
  @ManagedOperation(description = "Run sync of the PostTrust table with Post to Training Body Trust")
  public void PostTrainingBodyTrustFullSync() {
    runSyncJob();
  }

  @Override
  protected String getJobName() {
    return "Post associated with Training Body";
  }

  @Override
  protected int getPageSize() {
    return DEFAULT_PAGE_SIZE;
  }

  @Override
  protected EntityManagerFactory getEntityManagerFactory() {
    return this.entityManagerFactory;
  }

  @Override
  protected void deleteData() {
    //This job runs after the PostEmployingBodyTrustJob and therefore shouldn't truncate the table
  }

  @Override
  protected List<EntityData> collectData(int pageSize, long lastId, long lastTrainingBodyId, EntityManager entityManager) {
    LOG.info("Querying with lastPersonId: [{}] and lastSiteId: [{}]", lastId, lastTrainingBodyId);
    Query query = entityManager.createNativeQuery(
        "SELECT distinct p.id, p.trainingBodyId " +
            "FROM Post p " +
            "WHERE (p.id, p.trainingBodyId) > (" + lastId + "," + lastTrainingBodyId + ") " +
            "AND p.trainingBodyId IS NOT NULL " +
            "ORDER BY p.id ASC, p.trainingBodyId ASC " +
            "LIMIT " + pageSize);

    List<Object[]> resultList = query.getResultList();
    List<EntityData> result = resultList.stream().filter(Objects::nonNull).map(objArr -> {
      EntityData entityData = new EntityData()
          .entityId(((BigInteger) objArr[0]).longValue())
          .otherId(((BigInteger) objArr[1]).longValue());
      return entityData;
    }).collect(Collectors.toList());

    return result;
  }

  @Override
  protected int convertData(int skipped, Set<PostTrust> entitiesToSave, List<EntityData> entityData,
                            EntityManager entityManager) {

    if (CollectionUtils.isNotEmpty(entityData)) {
      for (EntityData ed : entityData) {
        if (ed != null) {

          if (ed.getEntityId() != null) {
            PostTrust postTrust = new PostTrust();

            Post post = new Post();
            post.setId(ed.getEntityId());

            postTrust.setPost(post);
            postTrust.setTrustId(ed.getOtherId());

            entitiesToSave.add(postTrust);
          } else {
            skipped++;
          }
        }
      }
    }
    return skipped;
  }
}
