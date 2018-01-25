package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Used to decorate the Person View list with labels such as grade and site labels
 */
@Component
public class PersonViewDecorator {

  private static final Logger log = LoggerFactory.getLogger(PersonViewDecorator.class);
  private AsyncReferenceService asyncReferenceService;

  @Autowired
  public PersonViewDecorator(AsyncReferenceService referenceServiceAccessor) {
    this.asyncReferenceService = referenceServiceAccessor;
  }

  /**
   * Decorates the given person views with sites and grades labels
   *
   * @param personViews the person views to decorate
   */
  public List<PersonViewDTO> decorate(List<PersonViewDTO> personViews) {
    // collect all the codes from the list
    Set<Long> gradeIds = new HashSet<>();
    Set<Long> siteIds = new HashSet<>();
    personViews.forEach(personView -> {
      if (personView.getGradeId() != null) {
        gradeIds.add(personView.getGradeId());
      }
      if (personView.getSiteId() != null) {
        siteIds.add(personView.getSiteId());
      }
      // fix the roles
      if (StringUtils.isNotBlank(personView.getRole())) {
        personView.setRole(personView.getRole().replaceAll(",",", "));
      }
    });

    CompletableFuture.allOf(
            decorateGradesOnPerson(gradeIds, personViews),
            decorateSitesOnPerson(siteIds, personViews))
            .join();

    return personViews;
  }

  protected CompletableFuture<Void> decorateGradesOnPerson(Set<Long> gradeIds, List<PersonViewDTO> personViewDTOs) {
    return asyncReferenceService.doWithGradesAsync(gradeIds, gradeMap -> {
      for (PersonViewDTO personView : personViewDTOs) {
        if (personView.getGradeId() != null && gradeMap.containsKey(personView.getGradeId())) {
          personView.setGradeName(gradeMap.get(personView.getGradeId()).getName());
        }
      }
    });
  }

  protected CompletableFuture<Void> decorateSitesOnPerson(Set<Long> siteIds, List<PersonViewDTO> personViewDTOs) {
    return asyncReferenceService.doWithSitesAsync(siteIds, siteMap -> {
      for (PersonViewDTO personView : personViewDTOs) {
        if (personView.getSiteCode() != null && siteMap.containsKey(personView.getSiteId())) {
          personView.setSiteName(siteMap.get(personView.getSiteId()).getSiteName());
        }
      }
    });
  }
}
