
package com.biperf.core.ui.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.biperf.acegi.authentication.dao.UserCredential;
import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.UserSessionAttributes;

public class SwitchProfileFilter implements Filter
{

  /**
   * @param filterConfig
   * @throws javax.servlet.ServletException
   * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
   */
  @Override
  public void init( FilterConfig filterConfig ) throws ServletException
  {
    // empty
  }

  /**
   * Create the contentReader and store in the the ContentReaderManager.
   * 
   * @param servletRequest ServletRequest
   * @param servletResponse ServletResponse
   * @param filterChain FilterChain
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException
  {
    HttpServletRequest httpServletRequest = (HttpServletRequest)servletRequest;

    String switchProfile = httpServletRequest.getParameter( "switchProfile" );

    if ( !StringUtils.isEmpty( switchProfile ) )
    {
      SecurityContext acegiContext = (SecurityContext)httpServletRequest.getSession().getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );
      AuthenticatedUser user = null;
      if ( acegiContext != null )
      {
        Authentication auth = acegiContext.getAuthentication();
        Object obj = null;
        if ( auth != null )
        {
          obj = auth.getPrincipal();
        }
        if ( obj != null && obj instanceof AuthenticatedUser )
        {
          user = (AuthenticatedUser)obj;
        }
      }

      // code to set original user
      AuthenticatedUser originalUser = user.getOriginalAuthenticatedUser();
      originalUser.setDelegate( false );
      originalUser.setLaunched( false );

      UserCredential userCredentials = new UserCredential( originalUser.getUserId(), originalUser.getUsername(), originalUser.getPassword() );
      Object principal = originalUser;
      UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken( principal, userCredentials, originalUser.getAuthorities() );

      authentication.setDetails( originalUser );
      SecurityContext sc = SecurityContextHolder.getContext();
      sc.setAuthentication( authentication );

      if ( originalUser != null && originalUser.getUserId() > 0 )
      {
        UserManager.removeUser();
        UserManager.setUser( originalUser );
      }
      httpServletRequest.getSession().removeAttribute( "eligiblePromotions" );
      httpServletRequest.getSession().removeAttribute( "pointsView" );
      httpServletRequest.getSession().removeAttribute( UserSessionAttributes.PARTICIPANT_FOLLOWERS );

      httpServletRequest.getSession().setAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT, sc );
    }

    filterChain.doFilter( servletRequest, servletResponse );
  }

  /**
   * @see javax.servlet.Filter#destroy()
   */
  @Override
  public void destroy()
  {
    // empty
  }

}
