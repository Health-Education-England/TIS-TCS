package com.transformuk.hee.tis.tcs.service.job;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TrustAdminSyncJobTemplateTest {

  private TrustAdminSyncJobTemplateStub testObj;

  @Mock
  private EntityManagerFactory entityManagerFactoryMock;
  @Mock
  private EntityManager entityManagerMock;
  @Mock
  private EntityTransaction entityTransactionMock;

  @Test
  public void runShouldPersistWhenDataReturnedFromQuery() {
    List<EntityData> data = Lists.newArrayList();
    for (int i = 0; i < 100; i++) {
      data.add(new EntityData().entityId((long) i).otherId((long) i));
    }

    testObj = new TrustAdminSyncJobTemplateStub(entityManagerFactoryMock, data);

    when(entityManagerFactoryMock.createEntityManager()).thenReturn(entityManagerMock);
    when(entityManagerMock.getTransaction()).thenReturn(entityTransactionMock);

    testObj.run();

    verify(entityManagerMock, times(100)).persist(any());
    verify(entityManagerMock, times(2)).flush();
    verify(entityTransactionMock, times(2)).commit();
  }

  class TrustAdminSyncJobTemplateStub extends TrustAdminSyncJobTemplate<Object> {

    private EntityManagerFactory entityManagerFactoryMock;
    private List<EntityData> collectedData;
    private boolean firstCall = true;

    public TrustAdminSyncJobTemplateStub(EntityManagerFactory entityManagerFactoryMock,
        List<EntityData> collectedData) {
      this.entityManagerFactoryMock = entityManagerFactoryMock;
      this.collectedData = collectedData;
    }

    @Override
    protected String getJobName() {
      return null;
    }

    @Override
    protected int getPageSize() {
      return 10;
    }

    @Override
    protected EntityManagerFactory getEntityManagerFactory() {
      return this.entityManagerFactoryMock;
    }

    @Override
    protected void deleteData() {

    }

    @Override
    protected List<EntityData> collectData(int pageSize, long lastId, long lastSiteId,
        EntityManager entityManager) {
      if (firstCall) {
        firstCall = false;
        return this.collectedData;
      }
      return Lists.emptyList();
    }

    @Override
    protected int convertData(int skipped, Set<Object> entitiesToSave, List<EntityData> entityData,
        EntityManager entityManager) {
      for (Object o : entityData) {
        entitiesToSave.add(new Object());
      }
      return 0;
    }
  }
}
