package com.transformuk.hee.tis.tcs.service.util;

import static com.transformuk.hee.tis.tcs.service.util.ReflectionUtil.copyIfNotNullOrEmpty;

import com.transformuk.hee.tis.tcs.api.dto.PersonDTO;
import com.transformuk.hee.tis.tcs.api.enumeration.Status;
import java.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

public class ReflectionUtilTest {

  @Test
  public void canCopyStringFields() {
    PersonDTO from = new PersonDTO();
    PersonDTO to = new PersonDTO();

    from.setPublicHealthNumber("1");
    copyIfNotNullOrEmpty(from, to, "publicHealthNumber");
    Assert.assertEquals("1", to.getPublicHealthNumber());
  }

  @Test
  public void doesNotCopyEmptyStringFields() {
    PersonDTO from = new PersonDTO();
    PersonDTO to = new PersonDTO();

    from.setInactiveNotes("");
    copyIfNotNullOrEmpty(from, to, "inactiveNotes");
    Assert.assertEquals(null, to.getInactiveNotes());
  }

  @Test
  public void canCopyEnumFields() {
    PersonDTO from = new PersonDTO();
    PersonDTO to = new PersonDTO();

    from.setStatus(Status.CURRENT);
    copyIfNotNullOrEmpty(from, to, "status");
    Assert.assertEquals(Status.CURRENT, to.getStatus());
  }

  @Test
  public void canCopyDateFields() {

    PersonDTO from = new PersonDTO();
    PersonDTO to = new PersonDTO();

    LocalDateTime now = LocalDateTime.now();
    from.setInactiveDate(now);
    copyIfNotNullOrEmpty(from, to, "inactiveDate");
    Assert.assertEquals(now, to.getInactiveDate());
  }

  @Test
  public void canCopyMultipleFields() {
    PersonDTO from = new PersonDTO();
    PersonDTO to = new PersonDTO();

    LocalDateTime now = LocalDateTime.now();
    from.setInactiveDate(now);
    from.setStatus(Status.CURRENT);
    from.setInactiveNotes("");
    from.setPublicHealthNumber("1");

    copyIfNotNullOrEmpty(from, to, "inactiveDate", "status", "inactiveNotes", "publicHealthNumber");
    Assert.assertEquals(now, to.getInactiveDate());
    Assert.assertEquals(Status.CURRENT, to.getStatus());
    Assert.assertEquals(null, to.getInactiveNotes());
    Assert.assertEquals("1", to.getPublicHealthNumber());
  }
}
