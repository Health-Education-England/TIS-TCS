package com.transformuk.hee.tis.tcs.service.service.impl;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonTrust;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonTrustRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
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
public class PersonTrustService {

  private static final Logger LOG = LoggerFactory.getLogger(PersonTrustService.class);
  private static final int PAGE_SIZE = 5000;


  @Autowired
  private PersonRepository personRepository;
  @Autowired
  private PersonTrustRepository personTrustRepository;
  @Autowired
  private ReferenceService referenceService;
  @Autowired
  private EntityManagerFactory entityManagerFactory;

  @ManagedOperation(description = "Run full sync of the PersonTrust table")
  public void runPersonTrustFullSync() {
    int totalSoFar = 0;
    boolean hasMoreResults = true;

    LOG.info("deleting all PersonTrust");
    personTrustRepository.deleteAllInBatch();
    LOG.info("deleted all PersonTrust");

    Set<PersonTrust> personTrustsToSave = Sets.newHashSet();
    Map<Long, SiteDTO> siteIdToSiteDTO = Maps.newHashMap();
    Stopwatch mainStopwatch = Stopwatch.createStarted();
    Stopwatch stopwatch = Stopwatch.createStarted();
    int skipped = 0;
    while (hasMoreResults) {

      EntityManager entityManager = entityManagerFactory.createEntityManager();
      EntityTransaction transaction = entityManager.getTransaction();
      transaction.begin();

      List<PersonData> personDataList = collectData(PAGE_SIZE, totalSoFar, entityManager);
      LOG.info("Time taken to get all data : [{}]", stopwatch.toString());

      totalSoFar += personDataList.size();
      hasMoreResults = personDataList.size() > 0;
      LOG.info("got [{}] records, offset [{}]", personDataList.size(), totalSoFar);

      if (CollectionUtils.isNotEmpty(personDataList)) {
        getSiteAndTrustReferenceData(siteIdToSiteDTO, personDataList);

        stopwatch.reset().start();
        for (PersonData pd : personDataList) {
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
        LOG.info("Time taken to transform all data : [{}]", stopwatch.toString());
      }
      stopwatch.reset().start();
      personTrustsToSave.forEach(entityManager::persist);
      personTrustsToSave.clear();
      transaction.commit();
      entityManager.close();
      LOG.info("Time taken to save all data : [{}]", stopwatch.toString());
    }
    LOG.info("Time taken to repopulate entire table: [{}]", mainStopwatch.toString());
    LOG.info("Skipped records: [{}]", skipped);
  }


  private void getSiteAndTrustReferenceData(Map<Long, SiteDTO> siteIdToSiteDTO, List<PersonData> personData) {
    //get all the site ids from the collection and filter any that we dont yet have
    Set<Long> siteIds = personData.stream()
        .map(PersonData::getSiteId)
        .filter(Objects::nonNull)
        .filter(siteId -> !siteIdToSiteDTO.keySet().contains(siteId))
        .collect(Collectors.toSet());

    //create a map of site ids to trust objs
    if (CollectionUtils.isNotEmpty(siteIds)) {
      LOG.debug("requesting [{}] site records", siteIds.size());
      List<SiteDTO> sitesIdIn = referenceService.findSitesIdIn(siteIds);
      LOG.debug("got all site records");
      sitesIdIn.forEach(s -> siteIdToSiteDTO.put(s.getId(), s));
    }
  }

  private List<PersonData> collectData(int pageSize, int totalSoFar, EntityManager entityManager) {
    Query query = entityManager.createNativeQuery("SELECT distinct p.id, pl.siteId " +
        "FROM Person p " +
        "LEFT JOIN Placement pl " +
        "ON p.id = pl.traineeId " +
        "WHERE pl.siteId IS NOT NULL " +
        "LIMIT " + pageSize + " OFFSET " + totalSoFar);
    List<Object[]> resultList = query.getResultList();
    List<PersonData> result = resultList.stream().map(objArr -> {
      PersonData personData = new PersonData();
      personData.setPersonId(((BigInteger) objArr[0]).longValue());
      personData.setSiteId(((BigInteger) objArr[1]).longValue());
      return personData;
    }).collect(Collectors.toList());

    return result;
  }

  class PersonData {
    private Long personId;
    private Long siteId;

    public PersonData() {
    }

    public Long getPersonId() {
      return personId;
    }

    public void setPersonId(Long personId) {
      this.personId = personId;
    }

    public Long getSiteId() {
      return siteId;
    }

    public void setSiteId(Long siteId) {
      this.siteId = siteId;
    }
  }
}
