/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/cms/impl/CMSSOAuthenticationServiceImpl.java,v $
 *
 */

package com.biperf.core.service.cms.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.security.auth.login.LoginException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.biperf.core.dao.cm.CmsAuthenticationDao;
import com.biperf.core.security.CmsAuthorizationUtil;
import com.biperf.core.service.cms.CmsAuthenticationService;
import com.objectpartners.cms.domain.CmsUser;
import com.objectpartners.cms.domain.User;

public class CMSSOAuthenticationServiceImpl extends CmsAuthenticationService
{

  public final String ROLE_CODE_AUTHENTICATED = "AUTHENTICATED";
  public final String ROLE_CODE_USER = "USER";
  public final String ROLE_CODE_PAX = "PARTICIPANT";
  public final String ROLE_CODE_BI_USER = "BI_USER";

  private CmsAuthenticationDao cmsAuthenticationDao;

  public CmsUser authenticate( String username ) throws LoginException
  {
    return cmsAuthenticationDao.getUser( username );
  }

  public List<GrantedAuthority> getRoles( User user ) throws AuthenticationException
  {
    Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();

    GrantedAuthority grantedAuthorityDefault = buildGrantedAuthority( ROLE_CODE_AUTHENTICATED );
    grantedAuthorities.add( grantedAuthorityDefault );

    GrantedAuthority grantedAuthority = buildGrantedAuthority( ROLE_CODE_USER );
    grantedAuthorities.add( grantedAuthority );

    if ( user instanceof CmsUser )
    {
      CmsUser cmsUser = (CmsUser)user;
      if ( cmsUser.getUserType() != null )
      {
        if ( "bi".equals( cmsUser.getUserType() ) )
        {
          grantedAuthority = buildGrantedAuthority( ROLE_CODE_BI_USER );
          grantedAuthorities.add( grantedAuthority );
        }
      }

      // Adding roles
      List<String> roles = cmsAuthenticationDao.getRoles( cmsUser.getUserId() );
      Iterator<String> roleIterator = roles.iterator();
      while ( roleIterator.hasNext() )
      {
        String userRole = (String)roleIterator.next();
        grantedAuthorities.add( buildGrantedAuthority( userRole ) );
      }
    }

    // Add CM roles based on these roles
    CmsAuthorizationUtil.addCmsRolesToGrantedAuthority( grantedAuthorities );

    return new ArrayList<GrantedAuthority>( grantedAuthorities );
  }

  private SimpleGrantedAuthority buildGrantedAuthority( String roleCode )
  {
    if ( roleCode == null || roleCode.trim().equals( "" ) )
    {
      return null;
    }
    return new SimpleGrantedAuthority( "ROLE_" + roleCode );
  }

  public void setCmsAuthenticationDao( CmsAuthenticationDao cmsAuthenticationDao )
  {
    this.cmsAuthenticationDao = cmsAuthenticationDao;
  }

}
