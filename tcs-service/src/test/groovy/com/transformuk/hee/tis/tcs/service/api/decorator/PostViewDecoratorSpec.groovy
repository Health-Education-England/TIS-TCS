package com.transformuk.hee.tis.tcs.service.api.decorator

import com.transformuk.hee.tis.reference.api.dto.GradeDTO
import com.transformuk.hee.tis.reference.api.dto.SiteDTO
import com.transformuk.hee.tis.tcs.api.dto.PostViewDTO
import spock.lang.Specification

import java.util.concurrent.CompletableFuture

class PostViewDecoratorSpec extends Specification {
    static final String GRADE_ABBR = "GRADE_ABBR";
    static final String SITE_CODE = "SITE_CODE";
    static final String GRADE_NAME = "GRADE NAME";
    static final String PRIMARY_SITE_NAME = "PRIMARY SITE NAME";

    AsyncReferenceService referenceServiceMock = Mock();

    PostViewDecorator postViewDecorator = new PostViewDecorator(referenceServiceMock);

    def postView1 = new PostViewDTO(approvedGradeCode: GRADE_ABBR, approvedGradeName: GRADE_NAME,
            primarySiteCode: SITE_CODE, primarySiteName: PRIMARY_SITE_NAME)
    def postView2 = new PostViewDTO()

    def decorateGradesOnPostShouldCallReferenceAndPopulatePost() {
        given:
            GradeDTO gradeDTO = new GradeDTO(abbreviation: GRADE_NAME, name: GRADE_NAME)

            referenceServiceMock.doWithSitesAsync(_, _) >> CompletableFuture.completedFuture(null)
            referenceServiceMock.doWithGradesAsync(_, _) >> {codes, consumer ->
                consumer.accept([GRADE_ABBR: gradeDTO])
                CompletableFuture.completedFuture(null)
            }

        when:
            postViewDecorator.decorate([postView1, postView2])

        then:
            postView1.approvedGradeName == GRADE_NAME
            postView2.approvedGradeName == null
    }

    def decorateSitesOnPostShouldCallReferenceAndPopulatePost() {
        given:
            SiteDTO siteDTO = new SiteDTO(siteCode: SITE_CODE, siteName: PRIMARY_SITE_NAME)

            referenceServiceMock.doWithSitesAsync(_, _) >> {codes, consumer ->
                consumer.accept([SITE_CODE: siteDTO])
                CompletableFuture.completedFuture(null)
            }
            referenceServiceMock.doWithGradesAsync(_, _) >> CompletableFuture.completedFuture(null)

        when:
            postViewDecorator.decorate([postView1, postView2]);

        then:
            postView1.primarySiteName == PRIMARY_SITE_NAME
            postView2.primarySiteName == null
    }

}
