
package com.biperf.core.ui.servlet;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.Environment;

public abstract class BaseAccessFilter implements Filter
{
  private static final Log log = LogFactory.getLog( BaseAccessFilter.class );

  protected static final int COOKIE_VERSION = 1;
  protected static final boolean COOKIE_IS_HTTP_ONLY = true;

  protected static final String PROTOCOL_HTTP = "http";
  protected static final String PROTOCOL_HTTPS = "https";

  protected static final String PORT_HTTP = ":7001";
  protected static final String PORT_HTTPS = ":7002";

  protected static final String DELIMITER = ",";

  protected SystemVariableService systemVariableService;
  protected ParticipantService participantService;

  protected Set<String> exclusionPaths = new HashSet<String>();
  protected Boolean isWebServerUsed;

  @Override
  public void init( FilterConfig config ) throws ServletException
  {
    if ( Environment.isCtech() )
    {
      isWebServerUsed = Boolean.TRUE;
    }
  }

  protected void buildExclusions( FilterConfig config, String defaults, String configParameter )
  {
    String exclusionPathsString = config.getInitParameter( configParameter );
    if ( StringUtils.isEmpty( exclusionPathsString ) )
    {
      exclusionPathsString = defaults;
    }
    StringTokenizer tokenizer = new StringTokenizer( exclusionPathsString, DELIMITER );
    while ( tokenizer.hasMoreTokens() )
    {
      this.exclusionPaths.add( tokenizer.nextToken().trim() );
    }
  }

  @Override
  public abstract void doFilter( ServletRequest arg0, ServletResponse arg1, FilterChain arg2 ) throws IOException, ServletException;

  @Override
  public void destroy()
  {
  }

  protected ParticipantService getParticipantService()
  {
    if ( participantService == null )
    {
      participantService = (ParticipantService)BeanLocator.getBean( ParticipantService.BEAN_NAME );
    }
    return participantService;
  }

  protected boolean isSiteUrl( String domain )
  {
    String siteUrl = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    try
    {
      return domain.equals( new URL( siteUrl ).getHost() );
    }
    catch( MalformedURLException e )
    {
      log.error( "MalformedURLException : URL " + siteUrl, e );
    }

    return false;
  }

  protected SystemVariableService getSystemVariableService()
  {
    if ( systemVariableService == null )
    {
      systemVariableService = (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
    }
    return systemVariableService;
  }

  protected boolean isStaticContentAccess( String path )
  {
    return path.indexOf( ".css" ) != -1 || path.indexOf( ".js" ) != -1 || path.indexOf( ".gif" ) != -1 || path.indexOf( ".jpeg" ) != -1 || path.indexOf( ".jpg" ) != -1 || path.indexOf( ".png" ) != -1;
  }
}
