package com.transformuk.hee.tis.tcs.service.listener.person;

import com.transformuk.hee.tis.tcs.service.event.SpecialtySavedEvent;
import com.transformuk.hee.tis.tcs.service.service.PersonElasticSearchService;
import com.transformuk.hee.tis.tcs.service.service.PostElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Listens for SpecialtySavedEvent.
 */
@Component
public class SpecialtyElasticSearchEventListener {

  private static final Logger LOG = LoggerFactory
      .getLogger(SpecialtyElasticSearchEventListener.class);

  private final PersonElasticSearchService personElasticSearchService;
  private final PostElasticSearchService postElasticSearchService;

  /**
   * Constructor for SpecialtyElasticSearchEventListener.
   *
   * @param personElasticSearchService
   * @param postElasticSearchService
   */
  public SpecialtyElasticSearchEventListener(
      PersonElasticSearchService personElasticSearchService,
      PostElasticSearchService postElasticSearchService) {
    this.personElasticSearchService = personElasticSearchService;
    this.postElasticSearchService = postElasticSearchService;
  }

  /**
   * Handles the SpecialtySavedEvent by updating the corresponding Person and Post documents in
   * Elasticsearch.
   *
   * @param event the SpecialtySavedEvent containing the specialty ID to update
   */
  @EventListener
  public void handleSpecialtySavedEvent(SpecialtySavedEvent event) {
    Long specialtyId = event.getSpecialtyDTO().getId();
    LOG.info("Received SpecialtySavedEvent with id: [{}]", specialtyId);
    personElasticSearchService.updatePersonDocumentForSpecialty(specialtyId);
    postElasticSearchService.updatePostDocumentsForSpecialty(specialtyId);
  }
}
