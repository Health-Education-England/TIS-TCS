package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.tcs.TestUtils;
import org.assertj.core.util.Sets;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PermissionServiceTest {

  private static final String TEST_USER = "test user";
  private static final String OTHER_PERMISSION = "random permission";

  @InjectMocks
  private PermissionService testObj;

  @Test
  public void canViewSensitiveDataShouldReturnTrueWhenUserHasCorrectPermission() {
    TestUtils.mockUserProfileWithPermissions(TEST_USER, Sets.newLinkedHashSet(PermissionService.VIEW_SENSITIVE_DATA_ROLE));
    boolean result = testObj.canViewSensitiveData();

    Assert.assertTrue(result);
  }

  @Test
  public void canViewSensitiveDataShouldReturnFalseWhenUserDoesNotHaveCorrectPermission() {
    TestUtils.mockUserProfileWithPermissions(TEST_USER, Sets.newLinkedHashSet(OTHER_PERMISSION));
    boolean result = testObj.canViewSensitiveData();

    Assert.assertFalse(result);
  }

  @Test
  public void canEditSensitiveDataShouldReturnTrueWhenUserHasCorrectPermission() {
    TestUtils.mockUserProfileWithPermissions(TEST_USER, Sets.newLinkedHashSet(PermissionService.EDIT_SENSITIVE_DATA_ROLE));
    boolean result = testObj.canEditSensitiveData();

    Assert.assertTrue(result);
  }

  @Test
  public void canEditSensitiveDataShouldReturnFalseWhenUserDoesNotHaveCorrectPermission() {
    TestUtils.mockUserProfileWithPermissions(TEST_USER, Sets.newLinkedHashSet(OTHER_PERMISSION));
    boolean result = testObj.canEditSensitiveData();

    Assert.assertFalse(result);
  }

}