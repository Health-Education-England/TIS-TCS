package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * Used to decorate the Person View list with labels such as grade and site labels
 */
@Component
public class PersonViewDecorator {

  private static final Logger log = LoggerFactory.getLogger(PersonViewDecorator.class);
  private ReferenceService referenceService;

  @Autowired
  public PersonViewDecorator(ReferenceService referenceService) {
    this.referenceService = referenceService;
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
    });

    CompletableFuture<Void> gradesFuture = decorateGradesOnPerson(gradeCodes, personViews);
    CompletableFuture<Void> sitesFuture = decorateSitesOnPerson(siteCodes, personViews);

    CompletableFuture.allOf(gradesFuture, sitesFuture).join();

    return personViews;
  }

  @Async
  protected CompletableFuture<Void> decorateGradesOnPerson(Set<String> codes, List<PersonViewDTO> personViewDTOS) {
    if (CollectionUtils.isNotEmpty(codes)) {
      try {
        List<GradeDTO> grades = referenceService.findGradesIn(codes);
        Map<String, GradeDTO> gradeMap = grades.stream().collect(Collectors.toMap(GradeDTO::getAbbreviation, g -> g));
        for (PersonViewDTO personView : personViewDTOS) {
          if (StringUtils.isNotBlank(personView.getGradeAbbreviation()) && gradeMap.containsKey(personView.getGradeAbbreviation())) {
            personView.setGradeName(gradeMap.get(personView.getGradeAbbreviation()).getName());
          }
        }
      } catch (Exception e) {
        log.warn("Reference decorator call to grades failed", e);
      }
    }
    return CompletableFuture.completedFuture(null);
  }

  @Async
  protected CompletableFuture<Void> decorateSitesOnPerson(Set<String> codes, List<PersonViewDTO> personViewDTOS) {
    if (CollectionUtils.isNotEmpty(codes)) {
      try {
        List<SiteDTO> sites = referenceService.findSitesIn(codes);
        Map<String, SiteDTO> siteMap = sites.stream().collect(Collectors.toMap(SiteDTO::getSiteCode, s -> s));

        for (PersonViewDTO personView : personViewDTOS) {
          if (StringUtils.isNotBlank(personView.getSiteCode()) && siteMap.containsKey(personView.getSiteCode())) {
            personView.setSiteName(siteMap.get(personView.getSiteCode()).getSiteName());
          }
        }
      } catch (Exception e) {
        log.warn("Reference decorator call to sites failed", e);
      }
    }
    return CompletableFuture.completedFuture(null);
  }

}
