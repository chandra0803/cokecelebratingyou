
package com.biperf.core.ui.mobilerecogapp;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.service.security.LoginToken;
import com.biperf.core.utils.BeanLocator;

public class AuthenticatedFilter implements Filter
{

  private FilterConfig filterConfig;

  public void init( FilterConfig filterConfig ) throws ServletException
  {
    this.filterConfig = filterConfig;
  }

  public void doFilter( ServletRequest request, ServletResponse response, FilterChain chain ) throws IOException, ServletException
  {
    HttpServletRequest httpRequest = (HttpServletRequest)request;

    AuthenticatedUser authUser = getAuthenticatedUser( httpRequest );

    if ( authUser == null )
    {
      LoginToken token = new LoginToken( httpRequest.getHeader( "username" ), httpRequest.getHeader( "token" ) );
      authUser = getAuthenticationService().authenticate( token, httpRequest );

      if ( authUser == null )
      {
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        httpResponse.setStatus( HttpServletResponse.SC_UNAUTHORIZED );
      }
      else
      {
        // continue the chain
        chain.doFilter( request, response );
      }
    }
    else
    {
      chain.doFilter( request, response );
    }
  }

  protected AuthenticatedUser getAuthenticatedUser( HttpServletRequest request )
  {
    AuthenticatedUser authUser = null;

    SecurityContext acegiContext = (SecurityContext)request.getSession().getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );

    if ( acegiContext != null )
    {
      Authentication auth = acegiContext.getAuthentication();
      Object obj = null;
      if ( auth != null )
      {
        obj = auth.getPrincipal();
      }
      if ( obj != null )
      {
        if ( obj instanceof AuthenticatedUser )
        {
          authUser = (AuthenticatedUser)obj;
        }
      }
    }

    return authUser;
  }

  public void destroy()
  {
    this.filterConfig = null;
  }

  protected AuthenticationService getAuthenticationService()
  {
    return (AuthenticationService)BeanLocator.getBean( AuthenticationService.BEAN_NAME );
  }
}
