package com.transformuk.hee.tis.tcs.service.api.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.transformuk.hee.tis.tcs.service.model.EsrNotification;
import java.io.IOException;
import java.time.LocalDate;
import org.junit.Test;


public class ObjectClonerTest {

  private LocalDate fromDate = LocalDate.now();
  private LocalDate toDate = LocalDate.now();

  @Test
  public void testDeepCloning() throws IOException, ClassNotFoundException {

    EsrNotification esrNotification = anEsrNotification();

    EsrNotification copiedNotification = ObjectCloner.deepCopy(esrNotification);

    assertThat(esrNotification != copiedNotification).isTrue();
    assertThat(copiedNotification.getNotificationTitleCode())
        .isEqualTo(esrNotification.getNotificationTitleCode());
    assertThat(copiedNotification.getChangeOfProjectedEndDate() != esrNotification
        .getChangeOfProjectedEndDate()).isTrue();
    assertThat(copiedNotification.getChangeOfProjectedEndDate())
        .isEqualTo(esrNotification.getChangeOfProjectedEndDate());
    assertThat(copiedNotification.getDeaneryPostNumber() != esrNotification.getDeaneryPostNumber())
        .isTrue();
    assertThat(copiedNotification.getDeaneryPostNumber())
        .isEqualTo(esrNotification.getDeaneryPostNumber());
    assertThat(copiedNotification).isNotEqualTo(esrNotification);
  }

  private EsrNotification anEsrNotification() {
    EsrNotification esrNotification = new EsrNotification();
    esrNotification.setNotificationTitleCode("1");
    esrNotification.setChangeOfProjectedHireDate(fromDate);
    esrNotification.setChangeOfProjectedEndDate(toDate);
    esrNotification.setDeaneryPostNumber("dn-01");

    return esrNotification;
  }

}
