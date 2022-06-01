/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/security/provider/AuthServiceAuthenticationProvider.java,v $
 */

package com.biperf.core.security.provider;

import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.security.credentials.ClientSeamlessLogonCredentials;
import com.biperf.core.security.credentials.StandardLoginIdSeamlessLogonCredentials;
import com.biperf.core.security.credentials.StandardSSOIdSeamlessLogonCredentials;
import com.biperf.core.security.exception.AccountLockoutException;
import com.biperf.core.security.exception.InvalidSSOException;
import com.biperf.core.security.exception.PaxLockoutException;
import com.biperf.core.service.security.AuthenticationService;

/**
 * AuthServiceAuthenticationProvider.
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
 * <td>zahler</td>
 * <td>Apr 12, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class AuthServiceAuthenticationProvider implements AuthenticationProvider
{

  private AuthenticationService authenticationService = null;

  /**
   * Overridden from
   * 
   * @see org.acegisecurity.providers.AuthenticationProvider#authenticate(org.acegisecurity.Authentication)
   * @param authentication
   * @return Authentication
   * @throws AuthenticationException
   */
  public Authentication authenticate( Authentication authentication ) throws AuthenticationException
  {
    com.biperf.core.security.UsernamePasswordAuthenticationToken authRequest = null;
    if ( authentication.isAuthenticated() )
    {
      return authentication;
    }

    String username = "";

    if ( authentication.getPrincipal() != null )
    {
      username = authentication.getPrincipal().toString();
    }

    if ( authentication.getPrincipal() instanceof UserDetails )
    {
      username = ( (UserDetails)authentication.getPrincipal() ).getUsername();
    }

    AuthenticatedUser user = null;
    try
    {
      Object credentials = authentication.getCredentials();

      if ( credentials instanceof ClientSeamlessLogonCredentials || credentials instanceof StandardLoginIdSeamlessLogonCredentials || credentials instanceof StandardSSOIdSeamlessLogonCredentials )
      {
        if ( authentication instanceof com.biperf.core.security.UsernamePasswordAuthenticationToken )
        {
          authRequest = (com.biperf.core.security.UsernamePasswordAuthenticationToken)authentication;
          if ( authRequest.isInvalidSSO() )
          {
            throw new InvalidSSOException();
          }
          else
          {
            user = authenticationService.authenticate( username, credentials );
          }
        }
      }
      else
      {
        user = authenticationService.authenticate( username, credentials );
      }
    }
    catch( LoginException loginException )
    {
      if ( loginException instanceof FailedLoginException )
      {
        throw new BadCredentialsException( "FailedLoginException Occurred", loginException );
      }
      else if ( loginException instanceof AccountExpiredException )
      {
        try
        {
          throw new AccountExpiredException( "Account Has Expired" );
        }
        catch( AccountExpiredException e )
        {
          e.printStackTrace();
        }
      }
      else if ( loginException instanceof CredentialExpiredException )
      {
        throw new CredentialsExpiredException( "Password Has Expired", loginException );
      }
      else if ( loginException instanceof AccountLockoutException )
      {
        throw new LockedException( "Account is Locked", loginException );
      }
      else if ( loginException instanceof PaxLockoutException )
      {
        throw new LockedException( "Pax inactive with no points", loginException );
      }
      else if ( loginException instanceof InvalidSSOException )
      {
        throw new BadCredentialsException( "Invalid SSO login attempt was made for this user" );
      }
    }

    if ( user == null )
    {
      throw new BadCredentialsException( "User was null" );
    }
    WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails)authentication.getDetails();
    user.setSessionId( webAuthenticationDetails.getSessionId() );
    Object principalToReturn = user;

    return createSuccessAuthentication( principalToReturn, authentication, user );
  }

  /**
   * Creates a successful {@link Authentication} object.
   * <P>
   * Protected so subclasses can override.
   * </p>
   * <P>
   * Subclasses will usually store the original credentials the user supplied (not salted or encoded
   * passwords) in the returned <code>Authentication</code> object.
   * </p>
   * 
   * @param principal that should be the principal in the returned object
   * @param authentication that was presented to the <code>DaoAuthenticationProvider</code> for
   *          validation
   * @param user that was loaded by the <code>AuthenticationDao</code>
   * @return the successful authentication token
   */
  protected Authentication createSuccessAuthentication( Object principal, Authentication authentication, UserDetails user )
  {
    // Ensure we return the original credentials the user supplied,
    // so subsequent attempts are successful even with encoded passwords.
    // Also ensure we return the original getDetails(), so that future
    // authentication events after cache expiry contain the details
    UsernamePasswordAuthenticationToken userPasswordAuth = new UsernamePasswordAuthenticationToken( principal, authentication.getCredentials(), user.getAuthorities() );

    userPasswordAuth.setDetails( authentication.getDetails() );

    return userPasswordAuth;
  }

  /**
   * Overridden from
   * 
   * @see org.acegisecurity.providers.AuthenticationProvider#supports(java.lang.Class)
   * @param authentication
   * @return boolean
   */
  public boolean supports( Class authentication )
  {
    return UsernamePasswordAuthenticationToken.class.isAssignableFrom( authentication );
  }

  /**
   * Set the AuthenticationService through IoC
   * 
   * @param authenticationService
   */
  public void setAuthenticationService( AuthenticationService authenticationService )
  {
    this.authenticationService = authenticationService;
  }

}
