package com.transformuk.hee.tis.tcs.service.api.decorator;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.transformuk.hee.tis.reference.api.dto.GradeDTO;
import com.transformuk.hee.tis.reference.api.dto.SiteDTO;
import com.transformuk.hee.tis.reference.client.ReferenceService;
import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PersonViewDecoratorTest {

  private static final String SITE_CODE_1 = "site1";
  private static final String GRADE_CODE_1 = "grade1";
  private static final String GRADE_ABBR = "GRADE_ABBR";
  private static final String SITE_CODE = "SITE_CODE";
  private static final String GRADE_NAME = "GRADE NAME";
  private static final String SITE_NAME = "SITE NAME";

  @Spy
  @InjectMocks
  private PersonViewDecorator testObj;
  @Mock
  private ReferenceService referenceServiceMock;
  @Mock
  private PersonViewDTO personViewDTO1Mock, personViewDTO2Mock;
  @Mock
  private CompletableFuture<Void> gradesFutureMock, siteFutureMock;

  private List<PersonViewDTO> personViewsList;
  private Set<String> siteCodesSet, gradeCodeSet;
  private PersonViewDTO personView1, personViewWithNoGradeOrSite2;

  @Before
  public void setup() {
    personView1 = new PersonViewDTO();
    personView1.setSiteCode(SITE_CODE_1);
    personView1.setGradeAbbreviation(GRADE_CODE_1);

    personViewWithNoGradeOrSite2 = new PersonViewDTO();

    personViewsList = Lists.newArrayList(personView1, personViewWithNoGradeOrSite2);
    siteCodesSet = Sets.newHashSet(SITE_CODE_1);
    gradeCodeSet = Sets.newHashSet(GRADE_CODE_1);


    when(personViewDTO1Mock.getGradeAbbreviation()).thenReturn(GRADE_ABBR);
    when(personViewDTO1Mock.getGradeName()).thenReturn(GRADE_NAME);
    when(personViewDTO1Mock.getSiteCode()).thenReturn(SITE_CODE);
    when(personViewDTO1Mock.getSiteName()).thenReturn(SITE_NAME);
  }

  @Test(timeout = 5000L)
  public void decorateShouldSetGradesAndPersons() {

    CompletableFuture<Class<Void>> gradesCompletedFuture = CompletableFuture.completedFuture(Void.class);
    CompletableFuture<Class<Void>> sitesCompletedFuture = CompletableFuture.completedFuture(Void.class);
    doReturn(gradesCompletedFuture).when(testObj).decorateGradesOnPerson(gradeCodeSet, personViewsList);
    doReturn(sitesCompletedFuture).when(testObj).decorateSitesOnPerson(siteCodesSet, personViewsList);

    testObj.decorate(personViewsList);

    verify(testObj).decorateGradesOnPerson(gradeCodeSet, personViewsList);
    verify(testObj).decorateSitesOnPerson(siteCodesSet, personViewsList);
  }

  @Test
  public void decorateGradesOnPersonShouldCallReferenceAndPopulatePerson() {
    GradeDTO gradeDTO = new GradeDTO();
    gradeDTO.setAbbreviation(GRADE_ABBR);
    gradeDTO.setName(GRADE_NAME);
    List<GradeDTO> gradesDTO = Lists.newArrayList(gradeDTO);
    List<PersonViewDTO> personViewDTOS = Lists.newArrayList(personViewDTO1Mock, personViewDTO2Mock);

    when(referenceServiceMock.findGradesIn(gradeCodeSet)).thenReturn(gradesDTO);

    testObj.decorateGradesOnPerson(gradeCodeSet, personViewDTOS);

    verify(personViewDTO1Mock).setGradeName(GRADE_NAME);
    verify(personViewDTO2Mock, never()).setGradeName(any());
  }

  @Test
  public void decorateSitesOnPersonShouldCallReferenceAndPopulatePerson() {
    SiteDTO siteDTO = new SiteDTO();
    siteDTO.setSiteCode(SITE_CODE);
    siteDTO.setSiteName(SITE_NAME);
    List<SiteDTO> siteDTOS = Lists.newArrayList(siteDTO);
    List<PersonViewDTO> personViewDTOS = Lists.newArrayList(personViewDTO1Mock, personViewDTO2Mock);

    when(referenceServiceMock.findSitesIn(siteCodesSet)).thenReturn(siteDTOS);

    testObj.decorateSitesOnPerson(siteCodesSet, personViewDTOS);

    verify(personViewDTO1Mock).setSiteName(SITE_NAME);
    verify(personViewDTO2Mock, never()).setSiteName(any());
  }

}