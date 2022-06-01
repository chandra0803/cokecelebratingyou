/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/security/impl/AuthorizationServiceImpl.java,v $
 */

package com.biperf.core.service.security.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.biperf.core.security.acl.AclEntry;
import com.biperf.core.security.acl.GrantedAuthorityAclEntryImpl;
import com.biperf.core.service.security.AuthorizationService;

/**
 * AuthorizationServiceImpl.
 * <p>
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>Brian Repko</td>
 * <td>Sep 28, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AuthorizationServiceImpl implements AuthorizationService
{
  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.security.AuthorizationService#isUserInRole(java.lang.String)
   * @param roleCode
   * @return boolean
   */
  public boolean isUserInRole( String roleCode )
  {
    final String GRANTED_AUTHORITY_BEGIN_STRING = "ROLE_";
    Set result = new HashSet();
    if ( null == roleCode || "".equals( roleCode ) )
    {
      return false;
    }

    if ( roleCode.startsWith( GRANTED_AUTHORITY_BEGIN_STRING ) )
    {
      // roleCode = GRANTED_AUTHORITY_BEGIN_STRING + roleCode;
      roleCode = roleCode.substring( GRANTED_AUTHORITY_BEGIN_STRING.length(), roleCode.length() );
    }

    result.add( roleCode );
    return isUserInRole( result, null, null );

  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.security.AuthorizationService#isUserInRole(java.lang.String)
   * @param setAllRoles set of All roles
   * @param setAnyRoles set of Any roles
   * @param setNoneRoles set of None roles roleCode
   * @return boolean
   */
  public boolean isUserInRole( Set setAllRoles, Set setAnyRoles, Set setNoneRoles )
  {
    Set setAuthorities = getRoles();
    return isUserInRole( setAllRoles, setAnyRoles, setNoneRoles, setAuthorities );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.security.AuthorizationService#hasPermission(java.lang.String, int,
   *      java.lang.Object)
   * @param aclCode
   * @param permissions
   * @param objectToTest
   * @return boolean
   */
  public boolean hasPermission( String aclCode, int permissions, Object objectToTest )
  {
    boolean result = false;
    AclEntry acl = getAclEntry( aclCode );
    if ( acl != null )
    {
      result = acl.hasPermission( permissions, objectToTest );
    }
    // Code fix for bug 19064.It is modified from the ascend branch
    // result = true;
    return result;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.security.AuthorizationService#getAclEntry(java.lang.String)
   * @param aclCode
   * @return AclEntry
   */
  public AclEntry getAclEntry( String aclCode )
  {
    AclEntry result = null;
    Set setAuthorities = getAuthorities();
    if ( setAuthorities == null || setAuthorities.isEmpty() )
    {
      return result;
    }

    GrantedAuthority[] authorities = (GrantedAuthority[])setAuthorities.toArray( new GrantedAuthority[setAuthorities.size()] );
    for ( int i = 0; i < authorities.length; i++ )
    {
      GrantedAuthority authority = authorities[i];

      if ( null != authority.getAuthority() )
      {
        if ( authority.getAuthority().equals( "ACL_" + aclCode ) )
        {
          if ( authority instanceof GrantedAuthorityAclEntryImpl )
          {
            GrantedAuthorityAclEntryImpl grantedAuthorityAclEntryImpl = (GrantedAuthorityAclEntryImpl)authority;
            result = grantedAuthorityAclEntryImpl.getAclEntry();
            break;
          }
        }
      }
    }
    return result;
  }

  /**
   * getAuthorities returns a Set<GrantedAuthority> of the authorities from all the user's
   * GrantedAuthorities
   * 
   * @return set of GrantedAuthority objects return values
   */

  private Set getAuthorities()
  {
    SecurityContext context = SecurityContextHolder.getContext();

    if ( null == context )
    {
      return Collections.EMPTY_SET;
    }

    Authentication currentUser = context.getAuthentication();

    if ( null == currentUser )
    {
      return Collections.EMPTY_SET;
    }

    List<GrantedAuthority> authorities = (List<GrantedAuthority>)currentUser.getAuthorities();

    if ( null == authorities || authorities.size() < 1 )
    {
      return Collections.EMPTY_SET;
    }

    Set result = new HashSet();

    for ( int i = 0; i < authorities.size(); i++ )
    {
      GrantedAuthority authority = authorities.get( i );

      if ( null != authority )
      {
        result.add( authority );
      }
    }

    return result;
  }

  /**
   * getRoles returns a Set<String> of the ROLE authorities from all the user's GrantedAuthorities
   * 
   * @return set of strings (GrantedAuthority.getAuthority return values)
   */

  private Set getRoles()
  {
    final String GRANTED_AUTHORITY_BEGIN_STRING = "ROLE_";
    Set setAuthorities = getAuthorities();
    GrantedAuthority[] authorities = null;
    if ( ! ( null == setAuthorities || setAuthorities.size() < 1 ) )
    {
      authorities = (GrantedAuthority[])setAuthorities.toArray( new GrantedAuthority[setAuthorities.size()] );
    }

    if ( null == authorities || authorities.length < 1 )
    {
      return Collections.EMPTY_SET;
    }

    Set result = new HashSet();

    for ( int i = 0; i < authorities.length; i++ )
    {
      GrantedAuthority authority = authorities[i];

      if ( null != authority.getAuthority() )
      {
        // after taking to brian, we need to remove ROLE_ from authority not add
        if ( authority.getAuthority().startsWith( GRANTED_AUTHORITY_BEGIN_STRING ) )
        {
          String modifiedAuthority = authority.getAuthority().substring( GRANTED_AUTHORITY_BEGIN_STRING.length(), authority.getAuthority().length() );
          result.add( modifiedAuthority );
        }
        else
        {
          result.add( authority.getAuthority() );
        }
      }
    }

    return result;
  }

  /**
   * Overridden from This method is to find out the given user grated authority or not. Roles are
   * case sensitive as it is authorization issue.
   * 
   * @see com.biperf.core.service.security.AuthorizationService#isUserInRole(java.lang.String)
   * @param setAllRoles set of All roles
   * @param setAnyRoles set of Any roles
   * @param setNoneRoles set of None roles
   * @param setAuthorities set of Authorities
   * @return boolean
   */
  private boolean isUserInRole( Set setAllRoles, Set setAnyRoles, Set setNoneRoles, Set setAuthorities )
  {
    if ( null == setAuthorities || setAuthorities.isEmpty() )
    {
      return false;
    }

    if ( ( null == setAllRoles || setAllRoles.isEmpty() ) && ( null == setAnyRoles || setAnyRoles.isEmpty() ) && ( null == setNoneRoles || setNoneRoles.isEmpty() ) )
    {
      return false;
    }

    // return true even this null or empty or none matches
    if ( null == setNoneRoles || setNoneRoles.isEmpty() || getCommonSet( setNoneRoles, setAuthorities ).size() == 0 )
    {
      // proceed if everything matches also for intersection of All and Authority should be equals
      // to All
      if ( null == setAllRoles || setAllRoles.isEmpty() || getCommonSet( setAllRoles, setAuthorities ).size() == setAllRoles.size() )
      {
        // proceed even this null or empty or even one matches
        if ( null == setAnyRoles || setAnyRoles.isEmpty() || getCommonSet( setAnyRoles, setAuthorities ).size() > 0 )
        {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.security.AuthorizationService#isUserInRole(java.lang.String)
   * @param setOne first set one of All or Any or None roles
   * @param setTwo second set normallu authority set
   * @return Set consists of intesection
   */
  private Set getCommonSet( Set setOne, Set setTwo )
  {
    // gets the commen elements of the above two sets in a newset

    if ( null == setTwo || setTwo.isEmpty() )
    {
      return Collections.EMPTY_SET;
    }

    Set setIntersection = new HashSet( setOne );
    setIntersection.retainAll( setTwo );

    return setIntersection;

  }

}
