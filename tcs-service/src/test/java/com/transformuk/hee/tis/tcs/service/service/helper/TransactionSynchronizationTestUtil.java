package com.transformuk.hee.tis.tcs.service.service.helper;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Utility methods for simulating transaction commit callbacks in unit tests.
 */
public final class TransactionSynchronizationTestUtil {

  private TransactionSynchronizationTestUtil() {
    // Utility class.
  }

  /**
   * Simulates the start of a transaction and registers a synchronization callback.
   * This method should be called before the code under test that is expected to register a
   * synchronization callback.
   */
  public static void startTransactionSynchronization() {
    TransactionSynchronizationManager.setActualTransactionActive(true);
    TransactionSynchronizationManager.initSynchronization();
  }

  /**
   * Simulates the commit of a transaction and triggers the registered synchronization callbacks.
   * This method should be called after the code under test that is expected to register a
   * synchronization callback.
   */
  public static void triggerAfterCommit() {
    List<TransactionSynchronization> synchronizations =
        new ArrayList<>(TransactionSynchronizationManager.getSynchronizations());
    synchronizations.forEach(TransactionSynchronization::afterCommit);
  }

  /**
   * Clears the transaction synchronization state.
   */
  public static void clearTransactionSynchronization() {
    if (TransactionSynchronizationManager.isSynchronizationActive()) {
      TransactionSynchronizationManager.clearSynchronization();
    }
    TransactionSynchronizationManager.setActualTransactionActive(false);
  }
}

