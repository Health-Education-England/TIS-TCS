package com.transformuk.hee.tis.tcs.service.service.impl;

import com.transformuk.hee.tis.security.model.Programme;
import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.security.model.UserProfile;
import com.transformuk.hee.tis.security.util.TisSecurityHelper;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Class that contains utility methods that work on the Principle of the logged in user
 */
@Service
public class PermissionService {

  protected static final String VIEW_SENSITIVE_DATA_ROLE = "personsensitive:view:entities";
  protected static final String EDIT_SENSITIVE_DATA_ROLE = "personsensitive:add:modify:entities";
  protected static final String APPROVE_PLACEMENT_PERM = "placement:approve";
  protected static final String BULK_UPLOAD_USER = "bulk_upload";

  public boolean canApprovePlacement() {
    UserProfile loggedInUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<String> permissions = loggedInUserProfile.getPermissions();
    return permissions.contains(APPROVE_PLACEMENT_PERM);
  }

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

  public boolean isUserTrustAdmin() {
    UserProfile loggedInUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<Trust> assignedTrusts = loggedInUserProfile.getAssignedTrusts();
    return !CollectionUtils.isEmpty(assignedTrusts);
  }

  public boolean isProgrammeObserver() {
    UserProfile loggedInUserProfile = TisSecurityHelper.getProfileFromContext();
    Set<Programme> assignedProgrammes = loggedInUserProfile.getAssignedProgrammes();
    return !CollectionUtils.isEmpty(assignedProgrammes);
  }

  public boolean isUserBulkUploadAdmin() {
    UserProfile loggedInUserProfile = TisSecurityHelper.getProfileFromContext();
    return StringUtils.equals(loggedInUserProfile.getUserName(), BULK_UPLOAD_USER);
  }

  public Set<Long> getUsersTrustIds() {
    UserProfile loggedInUserProfile = TisSecurityHelper.getProfileFromContext();
    if (!CollectionUtils.isEmpty(loggedInUserProfile.getAssignedTrusts())) {
      return loggedInUserProfile.getAssignedTrusts().stream().map(Trust::getId)
          .collect(Collectors.toSet());
    }
    return Collections.EMPTY_SET;
  }

  public Set<Long> getUsersProgrammeIds() {
    UserProfile loggedInUserProfile = TisSecurityHelper.getProfileFromContext();
    if (!CollectionUtils.isEmpty(loggedInUserProfile.getAssignedProgrammes())) {
      return loggedInUserProfile.getAssignedProgrammes().stream().map(Programme::getId)
          .collect(Collectors.toSet());
    }

    return Collections.EMPTY_SET;
  }
}
