package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonTrust;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonTrustRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@ManagedResource(objectName = "tcs.mbean:name=PersonTrustService",
    description = "Service that clears the PersonTrust table and links Persons with Trusts")
public class PersonTrustService {

  private static final Logger LOG = LoggerFactory.getLogger(PersonTrustService.class);
  private static final int PAGE_SIZE = 5000;

  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private PersonTrustRepository personTrustRepository;
  @Autowired
  private EntityManagerFactory entityManagerFactory;
  @Autowired
  private RestTemplate trustAdminEnabledRestTemplate;

  @Value("${reference.service.url}")
  private String serviceUrl;

  //used to keep state if the sync job is currently running
  private Stopwatch mainStopwatch;

  /**
   * The scheduled job method that links a person to a trust
   */
  @Scheduled(cron = "0 0 0 * * *") //run every day at 12am
  @ManagedOperation(description = "Run full sync of the PersonTrust table")
  public void runPersonTrustFullSync() {

    CompletableFuture.runAsync(() -> {

      if (mainStopwatch != null) {
        LOG.info("Person Trust sync job is already running, exiting this execution");
        return;
      }

      mainStopwatch = Stopwatch.createStarted();

      try {

        long lastGreatestPersonId = 0;
        long lastGreatestSiteId = 0;
        boolean hasMoreResults = true;

        LOG.debug("deleting all PersonTrust");
        personTrustRepository.deleteAllInBatch();
        LOG.debug("deleted all PersonTrust");

        Set<PersonTrust> personTrustsToSave = Sets.newHashSet();
        Map<Long, SiteDTO> siteIdToSiteDTO = Maps.newHashMap();
        Stopwatch stopwatch = Stopwatch.createStarted();
        int skipped = 0;
        while (hasMoreResults) {

          EntityManager entityManager = entityManagerFactory.createEntityManager();
          EntityTransaction transaction = entityManager.getTransaction();
          transaction.begin();

          List<PersonData> personDataList = collectData(PAGE_SIZE, lastGreatestPersonId, lastGreatestSiteId, entityManager);
          LOG.info("Time taken to read chunk : [{}]", stopwatch.toString());

          hasMoreResults = personDataList.size() > 0;

          if (CollectionUtils.isNotEmpty(personDataList)) {

            lastGreatestPersonId = personDataList.get(personDataList.size() - 1).getPersonId();
            lastGreatestSiteId = personDataList.get(personDataList.size() - 1).getSiteId();
            getSiteAndTrustReferenceData(siteIdToSiteDTO, personDataList);

            stopwatch.reset().start();
            skipped = convertData(personTrustsToSave, siteIdToSiteDTO, skipped, entityManager, personDataList);
            LOG.debug("Time taken to transform chunk : [{}]", stopwatch.toString());
          }
          stopwatch.reset().start();
          personTrustsToSave.forEach(entityManager::persist);
          entityManager.flush();
          personTrustsToSave.clear();

          transaction.commit();
          entityManager.close();
          LOG.info("Time taken to save chunk : [{}]", stopwatch.toString());
        }
        LOG.info("Time taken to repopulate entire table: [{}]", mainStopwatch.toString());
        LOG.info("Skipped records: [{}]", skipped);
      } catch (Exception e) {
        LOG.error("An error occurred while running the Person Trust sync job.", e);
      }

      //reset the time started so that it can be triggered again
      this.mainStopwatch = null;
    });
  }

  @ManagedOperation(description = "Is the Person Trust sync just currently running")
  public boolean isCurrentlyRunning() {
    return mainStopwatch != null;
  }

  @ManagedOperation(description = "The current elapsed time of the current sync job")
  public String elaspedTime() {
    return mainStopwatch != null ? mainStopwatch.toString() : "0s";
  }

  private int convertData(Set<PersonTrust> personTrustsToSave, Map<Long, SiteDTO> siteIdToSiteDTO, int skipped, EntityManager entityManager, List<PersonData> personDataList) {
    if (CollectionUtils.isNotEmpty(personDataList)) {
      for (PersonData pd : personDataList) {
        if (pd != null) {
          SiteDTO siteDTO = siteIdToSiteDTO.get(pd.siteId);
          if (pd.getPersonId() != null && siteDTO.getTrustId() != null) {
            PersonTrust personTrust = new PersonTrust();
            Person person = entityManager.getReference(Person.class, pd.getPersonId());
            personTrust.setPerson(person);
            personTrust.setTrustId(siteDTO.getTrustId());
            personTrust.setTrustCode(siteDTO.getTrustCode());
            personTrustsToSave.add(personTrust);
          } else {
            skipped++;
          }
        }
      }
    }
    return skipped;
  }


  /**
   * Get the Site and Trust information from the Reference service but first check if the data is cached first
   *
   * @param siteIdToSiteDTO
   * @param personData
   */
  private void getSiteAndTrustReferenceData(Map<Long, SiteDTO> siteIdToSiteDTO, List<PersonData> personData) {
    Set<Long> siteIds = personData.stream()
        .map(PersonData::getSiteId)
        .filter(Objects::nonNull)
        .filter(siteId -> !siteIdToSiteDTO.keySet().contains(siteId))
        .collect(Collectors.toSet());

    if (CollectionUtils.isNotEmpty(siteIds)) {
      LOG.debug("requesting [{}] site records", siteIds.size());
      List<SiteDTO> sitesIdIn = findSitesIdIn(siteIds);
      LOG.debug("got all site records");
      sitesIdIn.forEach(s -> siteIdToSiteDTO.put(s.getId(), s));
    }
  }

  /**
   * Copied from the Reference client as we want to communicate to the Reference service using the TCS credentials.
   * <p>
   * We dont have a user context if we run this as a scheduled job so we need to talk to KC to log in and get a OIDC key
   *
   * @param ids
   * @return
   */
  private List<SiteDTO> findSitesIdIn(Set<Long> ids) {
    String url = serviceUrl + "/api/sites/ids/in";
    String joinedIds = StringUtils.join(ids, ",");
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("ids", joinedIds);
    try {
      ResponseEntity<List<SiteDTO>> responseEntity = trustAdminEnabledRestTemplate.
          exchange(uriBuilder.build().encode().toUri(), HttpMethod.GET, null,
              new ParameterizedTypeReference<List<SiteDTO>>() {
              });
      return responseEntity.getBody();
    } catch (Exception e) {
      LOG.error("Exception during find sites id in for ids [{}], returning empty list. Here's the error message {}",
          joinedIds, e.getMessage());
      return Collections.EMPTY_LIST;
    }
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
  private List<PersonData> collectData(int pageSize, long lastPersonId, long lastSiteId, EntityManager entityManager) {
    LOG.debug("Querying with lastPersonId: [{}] and lastSiteId: [{}]", lastPersonId, lastSiteId);
    Query query = entityManager.createNativeQuery("SELECT distinct p.id, pl.siteId " +
        "FROM Person p " +
        "LEFT JOIN Placement pl " +
        "ON p.id = pl.traineeId " +
        "WHERE (p.id, pl.siteId) > (" + lastPersonId + "," + lastSiteId + ") " +
        "AND pl.siteId IS NOT NULL " +
        "ORDER BY p.id ASC, pl.siteId ASC " +
        "LIMIT " + pageSize);
    List<Object[]> resultList = query.getResultList();
    List<PersonData> result = resultList.stream().filter(Objects::nonNull).map(objArr -> {
      PersonData personData = new PersonData()
          .personId(((BigInteger) objArr[0]).longValue())
          .siteId(((BigInteger) objArr[1]).longValue());
      return personData;
    }).collect(Collectors.toList());

    return result;
  }

  /**
   * DTO type class used to collect the data from the DB
   */
  class PersonData {
    private Long personId;
    private Long siteId;

    public Long getPersonId() {
      return personId;
    }

    public Long getSiteId() {
      return siteId;
    }

    public PersonData siteId(Long siteId) {
      this.siteId = siteId;
      return this;
    }

    public PersonData personId(Long personId) {
      this.personId = personId;
      return this;
    }

  }

}
