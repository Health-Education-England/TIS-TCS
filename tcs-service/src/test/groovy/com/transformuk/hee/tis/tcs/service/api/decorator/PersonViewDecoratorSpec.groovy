package com.transformuk.hee.tis.tcs.service.api.decorator

import com.transformuk.hee.tis.reference.api.dto.GradeDTO
import com.transformuk.hee.tis.reference.api.dto.SiteDTO
import com.transformuk.hee.tis.tcs.api.dto.PersonViewDTO
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class PersonViewDecoratorSpec extends Specification {
    static final String GRADE_ABBR = "GRADE_ABBR"
    static final String SITE_CODE = "SITE_CODE"
    static final String GRADE_NAME = "GRADE NAME"
    static final String SITE_NAME = "SITE NAME"

    def personView1 = new PersonViewDTO(gradeAbbreviation: GRADE_ABBR, gradeName: GRADE_NAME)
    def personView2 = new PersonViewDTO(siteCode: SITE_CODE, siteName: SITE_NAME)

    AsyncReferenceService asyncReferenceService = Mock()

    def personViewDecorator = new PersonViewDecorator(asyncReferenceService)


    def "should call reference and populate grades"() {
        given:
            def gradeDTO = new GradeDTO(abbreviation: GRADE_ABBR, name: GRADE_NAME)
            asyncReferenceService.doWithSitesAsync(_, _) >> CompletableFuture.completedFuture(null)
            asyncReferenceService.doWithGradesAsync(_, _) >> { codes, consumer ->
                consumer.accept([GRADE_ABBR: gradeDTO])
                CompletableFuture.completedFuture(null)
            }

        when:
            def x = personViewDecorator.decorate([personView1, personView2]);

        then:
            personView1.gradeName == GRADE_NAME
            personView2.gradeName == null
    }


    def "should call reference and populate sites"() {
        given:
            def siteDTO = new SiteDTO(siteCode: SITE_CODE, siteName: SITE_NAME)

            asyncReferenceService.doWithSitesAsync(_ as Set, _) >> {codes, consumer ->
                consumer.accept([SITE_CODE: siteDTO])
                CompletableFuture.completedFuture(null)
            }
            asyncReferenceService.doWithGradesAsync(_, _) >> CompletableFuture.completedFuture(null)

        when:
            personViewDecorator.decorate([personView1, personView2])

        then:
            personView1.siteName == null
            personView2.siteName == SITE_NAME
    }
}
