package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PermissionService {

  protected static final String VIEW_SENSITIVE_DATA_ROLE = "personsensitive:view:entities";
  protected static final String EDIT_SENSITIVE_DATA_ROLE = "personsensitive:add:modify:entities";

  public boolean canViewSensitiveData() {
    UserProfile loggedInUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<String> permissions = loggedInUserProfile.getPermissions();
    return permissions.contains(VIEW_SENSITIVE_DATA_ROLE);
  }

  public boolean canEditSensitiveData() {
    UserProfile loggedInUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<String> permissions = loggedInUserProfile.getPermissions();
    return permissions.contains(EDIT_SENSITIVE_DATA_ROLE);
  }
}
