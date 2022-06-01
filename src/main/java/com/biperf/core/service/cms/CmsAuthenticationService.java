/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/cms/CmsAuthenticationService.java,v $
 *
 */

package com.biperf.core.service.cms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.security.auth.login.LoginException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.biperf.core.security.AuthenticatedUser;
import com.objectpartners.cms.domain.CmsUser;
import com.objectpartners.cms.domain.User;
import com.objectpartners.cms.domain.enums.RoleEnum;
import com.objectpartners.cms.service.AuthenticationService;
import com.objectpartners.cms.util.CmsConfiguration;

/**
 * CmsAuthenticationService <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Aug 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 *
 */
public class CmsAuthenticationService implements AuthenticationService
{
  private CmsConfiguration cmsConfiguration = null;

  public UsernamePasswordAuthenticationToken authenticate( UsernamePasswordAuthenticationToken token ) throws AuthenticationException
  {
    return token == null ? createToken() : token;
  }

  public UsernamePasswordAuthenticationToken createToken()
  {
    User user = new User();

    // set authorities disapeared, that can be done only via the constructor
    UsernamePasswordAuthenticationToken userAuthentication = new UsernamePasswordAuthenticationToken( user, "password", getRoles( user ) );
    // With new ACEGI upgrade, the constructor already sets authenticated to true..
    // userAuthentication.setAuthenticated( true );

    return userAuthentication;
  }

  /**
   * @param username
   * @param credentials
   * @return User
   * @throws javax.security.auth.login.LoginException
   */
  public UserDetails authenticate( String username, Object credentials ) throws LoginException
  {
    AuthenticatedUser user = new AuthenticatedUser();

    List<GrantedAuthority> grantedAuthority = new ArrayList<GrantedAuthority>();
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority( "ROLE_" + RoleEnum.CONTENT_ADMINISTRATOR.getName() );
    grantedAuthority.add( simpleGrantedAuthority );

    user.setAuthorities( grantedAuthority );
    user.setUsername( "anonymous" );
    return user;
  }

  public List<GrantedAuthority> getRoles( User user ) throws AuthenticationException
  {
    List<GrantedAuthority> grantedAuthority = new ArrayList<GrantedAuthority>();
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority( "ROLE_" + RoleEnum.CONTENT_ADMINISTRATOR.getName() );
    grantedAuthority.add( simpleGrantedAuthority );

    return grantedAuthority;
  }

  public Set getAudienceNames( User user )
  {
    // todo jjd
    return new HashSet( cmsConfiguration.getDefaultAudienceNames() );
  }

  public String getDefaultApplication( User user )
  {
    // todo jjd...
    return cmsConfiguration.getDefaultApplication();
  }

  public Locale getDefaultLocale( User user )
  {
    // todo jjd
    return cmsConfiguration.getDefaultLocale();
  }

  public Set getApplicationNames( User user )
  {
    // todo
    Set applicationNames = new HashSet();
    applicationNames.add( cmsConfiguration.getDefaultApplication() );
    return applicationNames;
  }

  public void setCmsConfiguration( CmsConfiguration cmsConfig )
  {
    cmsConfiguration = cmsConfig;
  }

  public CmsUser authenticate( String username ) throws LoginException
  {
    return null;
  }

}
