package com.transformuk.hee.tis.tcs.service.service.helper;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.apache.commons.lang.reflect.FieldUtils;
import org.flywaydb.core.api.FlywayException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlQuerySupplierTest {

  private static final String DUMMY_FILE = "scripts/dummy.sql";

  private SqlQuerySupplier supplier;

  @BeforeEach
  void setUp() {
    supplier = new SqlQuerySupplier();
  }

  @Test
  void shouldThrowExceptionWhenFilenameNull() {
    assertThrows(NullPointerException.class, () -> supplier.getQuery(null));
  }

  @Test
  void shouldThrowExceptionWhenFileNotExists() {
    assertThrows(FlywayException.class, () -> supplier.getQuery("notExists.file"));
  }

  @Test
  void shouldReturnQueryContentsFromFileWhenNotCached() {
    String content = supplier.getQuery(DUMMY_FILE);

    assertThat("Unexpected content.", content, is("This is a dummy file."));
  }

  @Test
  void shouldReturnQueryContentsFromCacheWhenCached() throws IllegalAccessException {
    Map files = (Map) FieldUtils.readField(supplier, "files", true);
    files.put(DUMMY_FILE, "This is a cached file.");

    String content = supplier.getQuery(DUMMY_FILE);

    assertThat("Unexpected content.", content, is("This is a cached file."));
  }
}
