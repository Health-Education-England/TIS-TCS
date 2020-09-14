package com.transformuk.hee.tis.tcs;


import static org.mockito.internal.util.collections.Sets.newSet;

import com.transformuk.hee.tis.security.model.AuthenticatedUser;
import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestUtils {

  private static Set<String> defaultRoles = Collections.singleton("HEE");
  private static List<GrantedAuthority> defaultAuthorities = defaultRoles.stream()
      .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

  public static void mockUserprofile(String userName, String... designatedBodyCodes) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setDesignatedBodyCodes(newSet(designatedBodyCodes));
    userProfile.setRoles(defaultRoles);
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToekn", userProfile,
        defaultAuthorities);
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null, defaultAuthorities);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  public static void mockUserprofileWithAuthorities(String userName, Set<String> authorities,
      String... designatedBodyCodes) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setDesignatedBodyCodes(newSet(designatedBodyCodes));
    userProfile.setRoles(authorities);
    List<GrantedAuthority> grantedAuthorities = authorities.stream()
        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToekn", userProfile,
        null);
    // set the authorities will set Authenticated to true in the constructor
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null, grantedAuthorities);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  public static void mockUserProfileWithPermissions(String userName, Set<String> permissions) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setPermissions(permissions);
    userProfile.setRoles(defaultRoles);
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToken", userProfile,
        null);
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null, null);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  public static void mockUserProfileWithAssociatedTrusts(String userName, Set<Trust> trusts) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setAssignedTrusts(trusts);
    userProfile.setRoles(defaultRoles);
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToken", userProfile,
        null);
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null, null);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

}

