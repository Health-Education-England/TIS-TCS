package com.transformuk.hee.tis.tcs.service.service.helper;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Utility for publishing events after commit when a transaction is active, or immediately when no
 * active transaction synchronization exists.
 */
public final class AfterCommitEventPublisher {

  private AfterCommitEventPublisher() {
    // Utility class.
  }

  /**
   * Publishes an event after the current transaction commits, or immediately if no transaction is
   * active.
   *
   * @param applicationEventPublisher the ApplicationEventPublisher to use for publishing the event
   * @param event                     the event to publish
   */
  public static void publishEventAfterCommit(ApplicationEventPublisher applicationEventPublisher,
      ApplicationEvent event) {
    if (TransactionSynchronizationManager.isSynchronizationActive()
        && TransactionSynchronizationManager.isActualTransactionActive()) {
      TransactionSynchronizationManager.registerSynchronization(
          new TransactionSynchronization() {
            @Override
            public void afterCommit() {
              applicationEventPublisher.publishEvent(event);
            }
          });
    } else {
      applicationEventPublisher.publishEvent(event);
    }
  }
}
