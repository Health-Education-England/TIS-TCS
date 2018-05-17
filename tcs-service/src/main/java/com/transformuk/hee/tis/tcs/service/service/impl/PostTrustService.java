package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Stopwatch;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.tcs.service.model.Post;
import com.transformuk.hee.tis.tcs.service.model.PostTrust;
import com.transformuk.hee.tis.tcs.service.repository.PostTrustRepository;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@ManagedResource(objectName = "tcs.mbean:name=PostTrustService",
    description = "Service that clears the PostTrust table and links Posts with Trusts")
public class PostTrustService extends TrustAdminSyncJobTemplate<PostTrust> {

  private static final Logger LOG = LoggerFactory.getLogger(PostTrustService.class);
  private static final int PAGE_SIZE = 5000;
  private static final int FIFTEEN_MIN = 15 * 60 * 1000;

  @Autowired
  private PostTrustRepository postTrustRepository;
  @Autowired
  private RestTemplate trustAdminEnabledRestTemplate;
  @Autowired
  private EntityManagerFactory entityManagerFactory;

  private Stopwatch mainStopWatch;

  @Value("${reference.service.url}")
  private String serviceUrl;

  @Scheduled(cron = "0 0/1 0 * * *")
  @SchedulerLock(name = "postTrustScheduledTask", lockAtLeastFor = FIFTEEN_MIN, lockAtMostFor = FIFTEEN_MIN)
  @ManagedOperation(description = "Run full sync of the PostTrust table")
  public void runPostTrustFullSync() {
    runSyncJob();
  }

  @Override
  protected EntityManagerFactory getEntityManagerFactory() {
    return this.entityManagerFactory;
  }

  @Override
  protected int getPageSize() {
    return PAGE_SIZE;
  }

  @Override
  protected String getServiceUrl() {
    return this.serviceUrl + "/api/sites/ids/in";
  }

  @Override
  protected void deleteData() {
    postTrustRepository.deleteAllInBatch();
  }

  @Override
  protected RestTemplate getTrustAdminEnabledRestTemplate() {
    return this.trustAdminEnabledRestTemplate;
  }

  @Override
  protected int convertData(int skipped, Set<PostTrust> postTrustsToSave, Map<Long, SiteDTO> siteIdToSiteDTO,
                            List<EntityData> entityData, EntityManager entityManager) {
    for (EntityData ed : entityData) {
      SiteDTO siteDTO = siteIdToSiteDTO.get(ed.getSiteId());
      if (ed.getEntityId() != null && siteDTO.getTrustId() != null) {
        PostTrust postTrust = new PostTrust();
        Post post = new Post();
        post.setId(ed.getEntityId());
        postTrust.setPost(post);
        postTrust.setTrustId(siteDTO.getTrustId());
        postTrust.setTrustCode(siteDTO.getTrustCode());
        postTrustsToSave.add(postTrust);
      } else {
        skipped++;
      }
    }
    return skipped;
  }


  protected List<EntityData> collectData(int pageSize, long lastPostId, long lastSiteId, EntityManager entityManager) {
    LOG.debug("Querying with lastPostId: [{}] and lastSiteId: [{}]", lastPostId, lastSiteId);

    Query query = entityManager.createNativeQuery(
        "SELECT distinct p.id, ps.siteId " +
            "FROM Post p " +
            "LEFT JOIN PostSite ps " +
            "ON p.id = ps.postId " +
            "WHERE ps.postSiteType = 'PRIMARY' " +
            "AND (p.id,ps.siteId) > (" + lastPostId + "," + lastSiteId + ") " +
            "ORDER BY p.id ASC, ps.siteId ASC " +
            "LIMIT " + pageSize);
    List<Object[]> resultList = query.getResultList();
    return resultList.stream().filter(Objects::nonNull).map(objArr -> {
      EntityData entityData = new EntityData()
          .entityId(((BigInteger) objArr[0]).longValue())
          .siteId(((BigInteger) objArr[1]).longValue());
      return entityData;
    }).collect(Collectors.toList());
  }
}
