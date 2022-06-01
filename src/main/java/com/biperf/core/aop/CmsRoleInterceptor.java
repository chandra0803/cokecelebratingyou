/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/aop/CmsRoleInterceptor.java,v $
 *
 */

package com.biperf.core.aop;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.security.CmsAuthorizationUtil;

/**
 * CmsRoleInterceptor <p/> <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>dunne</td>
 * <td>Sep 21, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * @author dunne
 */
public class CmsRoleInterceptor implements MethodInterceptor
{
  /**
   * Put the needed roles on the AuthenticatedUser for content manager. Overridden from
   * 
   * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
   * @param methodInvocation
   * @return Object
   * @throws Throwable
   */
  public Object invoke( MethodInvocation methodInvocation ) throws Throwable
  {
    List<GrantedAuthority> originalAuthorities = null;
    AuthenticatedUser user = null;

    // add the needed cm roles for the cmAssetService that the current user may not have.
    SecurityContext secureContext = SecurityContextHolder.getContext();
    Object authenticationToken = secureContext.getAuthentication();
    if ( authenticationToken instanceof UsernamePasswordAuthenticationToken )
    {
      UsernamePasswordAuthenticationToken userNamePasswordAuthToken = (UsernamePasswordAuthenticationToken)authenticationToken;
      if ( userNamePasswordAuthToken.getPrincipal() instanceof AuthenticatedUser )
      {
        user = (AuthenticatedUser) ( (UsernamePasswordAuthenticationToken)authenticationToken ).getPrincipal();
        originalAuthorities = user.getAuthorities();

        Set authorities = new HashSet( Arrays.asList( originalAuthorities ) );
        CmsAuthorizationUtil.addCmsInvocationRolesToGrantedAuthority( authorities );
        user.setAuthorities( (List<GrantedAuthority>)authorities );
      }
    }

    // proceed...
    Object object = methodInvocation.proceed();

    // remove cm roles
    if ( user != null && originalAuthorities != null )
    {
      user.setAuthorities( originalAuthorities );
    }
    return object;
  }

}
