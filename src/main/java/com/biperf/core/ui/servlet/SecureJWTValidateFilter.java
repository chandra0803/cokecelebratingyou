
package com.biperf.core.ui.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.jwt.JWTException;
import com.biperf.core.jwt.JWTTokenHandler;
import com.biperf.core.jwt.JWTTokenHandlerFactory;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;

public class SecureJWTValidateFilter extends BaseAccessFilter
{
  private static final Log logger = LogFactory.getLog( SecureJWTValidateFilter.class );
  private static final String DEFAULT_CSRF_CHECK_EXCLUSION_URLS = "login.do,logoutStatic.do,logout.do,j_acegi_security_check.do,homePage.do,seamlessLogon.do,clientSeamlessLogon.do,submitRewardOffering.do,raLogin.do";
  private static final String CSRF_CHECK_EXCLUSION_URLS_CONFIG_PARAM = "CSRF_EXCLUSION_URLS";

  @Override
  public void init( FilterConfig filterConfig ) throws ServletException
  {
    super.init( filterConfig );
    buildExclusions( filterConfig, DEFAULT_CSRF_CHECK_EXCLUSION_URLS, CSRF_CHECK_EXCLUSION_URLS_CONFIG_PARAM );
  }

  @Override
  public void doFilter( ServletRequest request, ServletResponse response, FilterChain filterChain ) throws IOException, ServletException
  {

    logger.debug( ">>>doFilter" );

    HttpServletRequest httpRequest = (HttpServletRequest)request;
    HttpServletResponse httpResponse = (HttpServletResponse)response;

    try
    {
      if ( "POST".equalsIgnoreCase( httpRequest.getMethod() ) )
      {
        String incomingUrlPath = httpRequest.getRequestURI();

        String token = null;
        JWTTokenHandlerFactory jwtTokenHandlerFactory = (JWTTokenHandlerFactory)BeanLocator.getBean( "JWTTokenHandlerFactory" );
        JWTTokenHandler secureJWTTokenHandler = jwtTokenHandlerFactory.getSecureJWTTokenHandler();

        if ( isExculdedUrlPath( incomingUrlPath ) )
        {
          Optional<String> tokenValue = readCookie( "VFT", httpRequest );

          if ( tokenValue.isPresent() )
          {
            token = tokenValue.get();

            boolean isValidToken = false;

            if ( StringUtils.isNotEmpty( token ) )
            {
              isValidToken = secureJWTTokenHandler.validate( token );

              if ( isValidToken )
              {
                filterChain.doFilter( request, response );
                return;
              }
              else
              {
                logger.error( " VFT token not valid !! : " + "\n URL : " + httpRequest.getRequestURI() );
                httpResponse.sendError( 403 );
              }
            }
            else
            {
              logger.error( " VFT token is empty !! : " + "\n URL : " + httpRequest.getRequestURI() );
              httpResponse.sendError( 403 );
            }
          }
          else
          {
            logger.error( " VFT token is empty !! : " + "\n URL : " + httpRequest.getRequestURI() );
            httpResponse.sendError( 403 );
          }
        }

      }

    }
    catch( JWTException jwtException )
    {
      logger.error( "Exception while validating VFT token, Url  : " + httpRequest.getRequestURI() + "\n  -> ", jwtException );
      httpResponse.sendError( 403 );
    }

    filterChain.doFilter( request, response );

  }

  @Override
  public void destroy()
  {
  }

  private boolean isExculdedUrlPath( String urlPath )
  {

    List<String> systemUrlPaths = new ArrayList<String>( Arrays.asList( getExculdedUrlPaths() ) );
    systemUrlPaths.add( "seamlessLogon" );

    if ( systemUrlPaths != null && !systemUrlPaths.isEmpty() )
    {
      for ( String url : systemUrlPaths )
      {
        if ( urlPath.contains( url ) )
        {
          return false;
        }
      }
    }

    return true;
  }

  private Optional<String> readCookie( String key, HttpServletRequest request )
  {
    return Arrays.stream( request.getCookies() ).filter( c -> key.equals( c.getName() ) ).map( Cookie::getValue ).findAny();
  }

  private String[] getExculdedUrlPaths()
  {
    try
    {
      PropertySetItem item = getSystemVariableService().getPropertyByName( SystemVariableService.EXCLUDE_CSRF_URLS );

      if ( item != null )
      {
        return item.getStringVal().split( "," );
      }
    }
    catch( Exception exception )
    {
      logger.error( " Getting excluded URL paths from system variable : " + exception );
    }

    return new String[0];
  }

  protected SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
