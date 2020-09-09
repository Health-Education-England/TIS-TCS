package com.transformuk.hee.tis.tcs;


import static org.mockito.internal.util.collections.Sets.newSet;

import com.transformuk.hee.tis.security.model.AuthenticatedUser;
import com.transformuk.hee.tis.security.model.Trust;
import com.transformuk.hee.tis.security.model.UserProfile;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class TestUtils {

  public static void mockUserprofile(String userName, String... designatedBodyCodes) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setDesignatedBodyCodes(newSet(designatedBodyCodes));
    List authorities = Arrays.asList(new SimpleGrantedAuthority("HEE"));
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToekn", userProfile,
        authorities);
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null, authorities);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  public static void mockUserprofileWithAuthorities(String userName, List<String> authorities, String... designatedBodyCodes) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setDesignatedBodyCodes(newSet(designatedBodyCodes));
    List grantedAuthorities = authorities.stream().map(a -> new SimpleGrantedAuthority(a)).collect(Collectors.toList());
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToekn", userProfile,
        grantedAuthorities);
    // set the authorities will set Authenticated to true in the constructor
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null, grantedAuthorities);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  public static void mockUserProfileWithPermissions(String userName, Set<String> permissions) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setPermissions(permissions);
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToken", userProfile,
        null);
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

  public static void mockUserProfileWithAssociatedTrusts(String userName, Set<Trust> trusts) {
    UserProfile userProfile = new UserProfile();
    userProfile.setUserName(userName);
    userProfile.setAssignedTrusts(trusts);
    AuthenticatedUser authenticatedUser = new AuthenticatedUser(userName, "dummyToken", userProfile,
        null);
    UsernamePasswordAuthenticationToken authenticationToken = new
        UsernamePasswordAuthenticationToken(authenticatedUser, null);

    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
  }

}

