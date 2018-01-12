package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    Set<String> gradeCodes = new HashSet<>();
    Set<String> siteCodes = new HashSet<>();
    personViews.forEach(personView -> {
      if (StringUtils.isNotBlank(personView.getGradeAbbreviation())) {
        gradeCodes.add(personView.getGradeAbbreviation());
      }
      if (StringUtils.isNotBlank(personView.getSiteCode())) {
        siteCodes.add(personView.getSiteCode());
      }
      // fix the roles
      if (StringUtils.isNotBlank(personView.getRole())) {
        personView.setRole(personView.getRole().replaceAll(",",", "));
      }
    });

    CompletableFuture.allOf(
            decorateGradesOnPerson(gradeCodes, personViews),
            decorateSitesOnPerson(siteCodes, personViews))
            .join();

    return personViews;
  }

  protected CompletableFuture<Void> decorateGradesOnPerson(Set<String> gradeCodes, List<PersonViewDTO> personViewDTOs) {
    return asyncReferenceService.doWithGradesAsync(gradeCodes, gradeMap -> {
      for (PersonViewDTO personView : personViewDTOs) {
        if (StringUtils.isNotBlank(personView.getGradeAbbreviation()) && gradeMap.containsKey(personView.getGradeAbbreviation())) {
          personView.setGradeName(gradeMap.get(personView.getGradeAbbreviation()).getName());
        }
      }
    });
  }

  protected CompletableFuture<Void> decorateSitesOnPerson(Set<String> siteCodes, List<PersonViewDTO> personViewDTOs) {
    return asyncReferenceService.doWithSitesAsync(siteCodes, siteMap -> {
      for (PersonViewDTO personView : personViewDTOs) {
        if (StringUtils.isNotBlank(personView.getSiteCode()) && siteMap.containsKey(personView.getSiteCode())) {
          personView.setSiteName(siteMap.get(personView.getSiteCode()).getSiteName());
        }
      }
    });
  }
}
