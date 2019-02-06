package com.transformuk.hee.tis.tcs.service.job;

import com.transformuk.hee.tis.tcs.service.model.Person;
import com.transformuk.hee.tis.tcs.service.model.PersonTrust;
import com.transformuk.hee.tis.tcs.service.repository.PersonRepository;
import com.transformuk.hee.tis.tcs.service.repository.PersonTrustRepository;
import com.transformuk.hee.tis.tcs.service.service.helper.SqlQuerySupplier;
import net.javacrumbs.shedlock.core.SchedulerLock;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@ManagedResource(objectName = "tcs.mbean:name=PersonPlacementEmployingBodyJob",
        description = "Service that clears the PersonTrust table and links Person with Placement EmployingBody(Trust)")
public class PersonPlacementEmployingBodyTrustJob extends TrustAdminSyncJobTemplate<PersonTrust> {

    private static final Logger LOG = LoggerFactory.getLogger(PersonPlacementEmployingBodyTrustJob.class);
    private static final int FIFTEEN_MIN = 15 * 60 * 1000;

    @Autowired
    private PersonTrustRepository personTrustRepository;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private SqlQuerySupplier sqlQuerySupplier;

    @Scheduled(cron = "0 10 0 * * *")
    @SchedulerLock(name = "personTrustEmployingBodyScheduledTask", lockAtLeastFor = FIFTEEN_MIN, lockAtMostFor = FIFTEEN_MIN)
    @ManagedOperation(description = "Run sync of the PersonTrust table with Person to Placement EmployingBody")
    public void PersonPlacementEmployingBodyFullSync() {
        runSyncJob();
    }

    @Override
    protected String getJobName() {
        return "PersonPlacementEmployingBodyTrustJob";
    }

    @Override
    protected int getPageSize() {
        return 5000;
    }

    @Override
    protected EntityManagerFactory getEntityManagerFactory() {
        return this.entityManagerFactory;
    }


    @Override
    protected void deleteData() {
      LOG.info("deleting all data");
      personTrustRepository.deleteAllInBatch();
      LOG.info("deleted all PersonTrust data");
    }


    @Override
    protected List<EntityData> collectData(int pageSize, long lastId, long lastEmployingBodyId, EntityManager entityManager) {
        LOG.info("Querying with lastPersonId: [{}] and lastEmployingBodyId: [{}]", lastId, lastEmployingBodyId);
        String personPlacementQuery = sqlQuerySupplier.getQuery(SqlQuerySupplier.PERSON_PLACEMENT_EMPLOYINGBODY);

        Query query = entityManager.createNativeQuery(personPlacementQuery).setParameter("lastId", lastId )
                                                                     .setParameter("lastEmployingBodyId", lastEmployingBodyId)
                                                                     .setParameter("pageSize", pageSize);
        List<Object[]> resultList = query.getResultList();

      return resultList.stream().filter(Objects::nonNull).map(objArr -> new EntityData()
        .entityId(((BigInteger) objArr[0]).longValue())
        .otherId(((BigInteger) objArr[1]).longValue())).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    protected int convertData(int skipped, Set<PersonTrust> entitiesToSave, List<EntityData> entityData,
                              EntityManager entityManager) {

        if (CollectionUtils.isNotEmpty(entityData)) {

            Set<Long> personIds = entityData.stream().map(EntityData::getEntityId).collect(Collectors.toSet());
          List<Person> allPersons = personRepository.findAllById(personIds);
            Map<Long, Person> personIdToPerson = allPersons.stream().collect(Collectors.toMap(Person::getId, person -> person));

            for (EntityData ed : entityData) {
                if (ed != null) {

                    if (ed.getEntityId() != null || ed.getOtherId() != null) {
                        PersonTrust personTrust = new PersonTrust();
                        personTrust.setPerson(personIdToPerson.get(ed.getEntityId()));
                        personTrust.setTrustId(ed.getOtherId());

                        entitiesToSave.add(personTrust);
                    } else {
                        skipped++;
                    }
                }
            }
        }
        return skipped;
    }
}
