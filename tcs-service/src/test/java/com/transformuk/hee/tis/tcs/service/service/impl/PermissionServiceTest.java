package com.transformuk.hee.tis.tcs.service.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.tcs.TestUtils;
import java.util.Collections;
import java.util.Set;
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
  private static final String TRUST_ID = "random permission";

  @InjectMocks
  private PermissionService testObj;

  @Test
  public void canViewSensitiveDataShouldReturnTrueWhenUserHasCorrectPermission() {
    TestUtils.mockUserProfileWithPermissions(TEST_USER,
        Sets.newLinkedHashSet(PermissionService.VIEW_SENSITIVE_DATA_ROLE));
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
    TestUtils.mockUserProfileWithPermissions(TEST_USER,
        Sets.newLinkedHashSet(PermissionService.EDIT_SENSITIVE_DATA_ROLE));
    boolean result = testObj.canEditSensitiveData();

    Assert.assertTrue(result);
  }

  @Test
  public void canEditSensitiveDataShouldReturnFalseWhenUserDoesNotHaveCorrectPermission() {
    TestUtils.mockUserProfileWithPermissions(TEST_USER, Sets.newLinkedHashSet(OTHER_PERMISSION));
    boolean result = testObj.canEditSensitiveData();

    Assert.assertFalse(result);
  }

  @Test
  public void isUserTrustAdminWillReturnTrueWhenTheyHaveAnAssociatedTrust() {
    Trust trust = new Trust(1L, "ABC", "ST TRUST");
    TestUtils.mockUserProfileWithAssociatedTrusts(TEST_USER, Sets.newLinkedHashSet(trust));
    boolean result = testObj.isUserTrustAdmin();

    Assert.assertTrue(result);

  }

  @Test
  public void isUserTrustAdminWillReturnFalseWhenTheyHaveNoAssociatedTrust() {

    TestUtils.mockUserProfileWithAssociatedTrusts(TEST_USER, Sets.newLinkedHashSet());
    boolean result1 = testObj.isUserTrustAdmin();

    Assert.assertFalse(result1);

    TestUtils.mockUserProfileWithAssociatedTrusts(TEST_USER, null);
    boolean result2 = testObj.isUserTrustAdmin();

    Assert.assertFalse(result2);
  }

  @Test
  public void canApprovePlacementWillReturnTrueWhenUserHasCorrectPermission() {
    TestUtils.mockUserProfileWithPermissions(TEST_USER,
        Sets.newLinkedHashSet(PermissionService.APPROVE_PLACEMENT_PERM));
    boolean result = testObj.canApprovePlacement();

    Assert.assertTrue(result);
  }

  @Test
  public void canApprovePlacementWillReturnFalseWhenUserDoesNotHaveCorrectPermission() {
    TestUtils.mockUserProfileWithPermissions(TEST_USER, Sets.newLinkedHashSet(OTHER_PERMISSION));
    boolean result = testObj.canApprovePlacement();

    Assert.assertFalse(result);
  }

  @Test
  public void shouldGetUserProfileDesignatedBodies() {
    TestUtils.mockUserprofile("jamesh", "1-1RUZV6H");
    Set<String> expectedBodies = Collections.singleton("1-1RUZV6H");

    Set<String> result = testObj.getUserProfileDesignatedBodies();
    assertEquals(expectedBodies, result);
  }
}
