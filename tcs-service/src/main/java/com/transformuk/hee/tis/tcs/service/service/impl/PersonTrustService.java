package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonTrust;
import com.transformuk.hee.tis.tcs.service.repository.PersonTrustRepository;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.apache.commons.collections4.CollectionUtils;
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
@ManagedResource(objectName = "tcs.mbean:name=PersonTrustService",
    description = "Service that clears the PersonTrust table and links Persons with Trusts")
public class PersonTrustService extends TrustAdminSyncJobTemplate<PersonTrust> {

  private static final Logger LOG = LoggerFactory.getLogger(PersonTrustService.class);
  private static final int PAGE_SIZE = 5000;
  private static final int FIFTEEN_MIN = 15 * 60 * 1000;

  @Autowired
  private PersonTrustRepository personTrustRepository;
  @Autowired
  private EntityManagerFactory entityManagerFactory;
  @Autowired
  private RestTemplate trustAdminEnabledRestTemplate;

  @Value("${reference.service.url}")
  private String serviceUrl;

  /**
   * The scheduled job method that links a person to a trust
   */
  @Scheduled(cron = "0 0 0 * * *") //run every day at 12am
  @SchedulerLock(name = "personTrustScheduledTask", lockAtLeastFor = FIFTEEN_MIN, lockAtMostFor = FIFTEEN_MIN)
  @ManagedOperation(description = "Run full sync of the PersonTrust table")
  public void runPersonTrustFullSync() {
    runSyncJob();
  }

  @Override
  protected int getPageSize() {
    return PAGE_SIZE;
  }

  @Override
  protected EntityManagerFactory getEntityManagerFactory() {
    return this.entityManagerFactory;
  }

  @Override
  protected String getServiceUrl() {
    return this.serviceUrl + "/api/sites/ids/in";
  }

  @Override
  protected RestTemplate getTrustAdminEnabledRestTemplate() {
    return this.trustAdminEnabledRestTemplate;
  }

  @Override
  protected void deleteData() {
    personTrustRepository.deleteAllInBatch();
  }

  @Override
  protected int convertData(int skipped, Set<PersonTrust> entitiesToSave, Map<Long, SiteDTO> siteIdToSiteDTO,
                            List<EntityData> entityData, EntityManager entityManager) {
    if (CollectionUtils.isNotEmpty(entityData)) {
      for (EntityData ed : entityData) {
        if (ed != null) {
          SiteDTO siteDTO = siteIdToSiteDTO.get(ed.getSiteId());
          if (ed.getEntityId() != null && siteDTO.getTrustId() != null) {
            PersonTrust personTrust = new PersonTrust();
            Person person = entityManager.getReference(Person.class, ed.getEntityId());
            personTrust.setPerson(person);
            personTrust.setTrustId(siteDTO.getTrustId());
            personTrust.setTrustCode(siteDTO.getTrustCode());
            entitiesToSave.add(personTrust);
          } else {
            skipped++;
          }
        }
      }
    }
    return skipped;
  }

  /**
   * The table being queried here is very large and seeing as we need to put ordering on it, the query becomes really slow
   * To bypass the slowness, we use the seek method in querying this table/join using the last highest id and site id
   *
   * @param pageSize
   * @param lastPersonId
   * @param lastSiteId
   * @param entityManager
   * @return
   */
  protected List<EntityData> collectData(int pageSize, long lastPersonId, long lastSiteId, EntityManager entityManager) {
    LOG.info("Querying with lastPersonId: [{}] and lastSiteId: [{}]", lastPersonId, lastSiteId);
    Query query = entityManager.createNativeQuery("SELECT distinct p.id, pl.siteId " +
        "FROM Person p " +
        "LEFT JOIN Placement pl " +
        "ON p.id = pl.traineeId " +
        "WHERE (p.id, pl.siteId) > (" + lastPersonId + "," + lastSiteId + ") " +
        "AND pl.siteId IS NOT NULL " +
        "ORDER BY p.id ASC, pl.siteId ASC " +
        "LIMIT " + pageSize);
    List<Object[]> resultList = query.getResultList();
    List<EntityData> result = resultList.stream().filter(Objects::nonNull).map(objArr -> {
      EntityData entityData = new EntityData()
          .entityId(((BigInteger) objArr[0]).longValue())
          .siteId(((BigInteger) objArr[1]).longValue());
      return entityData;
    }).collect(Collectors.toList());

    return result;
  }


}
