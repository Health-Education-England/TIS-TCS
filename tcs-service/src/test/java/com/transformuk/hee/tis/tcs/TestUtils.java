package com.transformuk.hee.tis.tcs;


import com.transformuk.hee.tis.security.model.AuthenticatedUser;
import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.security.model.UserProfile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

import static org.mockito.internal.util.collections.Sets.newSet;

public class TestUtils {

  public static void mockUserprofile(String userName, String... designatedBodyCodes) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setDesignatedBodyCodes(newSet(designatedBodyCodes));
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToekn", userProfile, null);
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  public static void mockUserProfileWithPermissions(String userName, Set<String> permissions) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setPermissions(permissions);
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToken", userProfile, null);
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  public static void mockUserProfileWithAssociatedTrusts(String userName, Set<Trust> trusts) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setAssignedTrusts(trusts);
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToken", userProfile, null);
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

}

