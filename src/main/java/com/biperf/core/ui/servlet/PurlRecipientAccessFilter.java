
package com.biperf.core.ui.servlet;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.PurlRecipientState;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.exception.DataException;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.serviceanniversary.ServiceAnniversaryService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.ClientStateUtils;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.opensymphony.oscache.util.StringUtil;

public class PurlRecipientAccessFilter extends BaseAccessFilter
{
  private static final Log log = LogFactory.getLog( PurlRecipientAccessFilter.class );

  private static final String PURL_RECIPIENT_URL_CONFIG_PARAM = "PURL_RECIPIENT_URL";
  private static final String DEFAULT_PURL_RECIPIENT_URL = "/purl/purlRecipient.do";
  private static final String PURL_EXCLUSION_URLS_CONFIG_PARAM = "PURL_EXCLUSION_URLS";
  private static final String DEFAULT_PURL_EXCLUSION_URLS = "purlRecipient.do";
  private static final String SESSION_PURL_ACCESS_URL = "SESSION_PURL_ACCESS_URL";

  private String purlRecipientPath;

  public void init( FilterConfig filterConfig ) throws ServletException
  {
    super.init( filterConfig );

    this.purlRecipientPath = filterConfig.getInitParameter( PURL_RECIPIENT_URL_CONFIG_PARAM );
    if ( StringUtils.isEmpty( this.purlRecipientPath ) )
    {
      this.purlRecipientPath = DEFAULT_PURL_RECIPIENT_URL;
    }

    buildExclusions( filterConfig, DEFAULT_PURL_EXCLUSION_URLS, PURL_EXCLUSION_URLS_CONFIG_PARAM );
  }

  public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException
  {
    if ( log.isDebugEnabled() )
    {
      log.debug( "com.biperf.core.ui.servlet.PurlRecipientAccessFilter  doFilter start" );
    }
    try
    {
      HttpServletRequest request = (HttpServletRequest)servletRequest;
      String path = request.getRequestURI();
      String userId = request.getParameter( "viewingId" );
      // log.error( "userId is " + userId );

      boolean isPurlUrl = isPurlUrl( request.getServerName().toLowerCase() );
      if ( isPurlUrl )
      {
        request.setAttribute( "isPurlUrl", "true" );
      }

      if ( isPurlUrl && !byPassRequest( path ) )
      {
        if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
        {
          String invalidUrl = getInvalidUrl( path, userId );
          if ( StringUtil.isEmpty( invalidUrl ) )
          {
            String saCelebrationUrl = getSACelebrationUrl( path, userId );
            if ( !StringUtil.isEmpty( saCelebrationUrl ) )
            {
              HttpServletResponse response = (HttpServletResponse)servletResponse;
              response.sendRedirect( saCelebrationUrl );
            }
          }
          else
          {
            request.getRequestDispatcher( invalidUrl ).forward( servletRequest, servletResponse );
          }
        }
        else
        {
          String purlRecipientUrl = getPurlRecipientUrl( path, userId );

          // Set the URL in session to get back to it from contact us page
          setPurlAccessUrlInSession( request, purlRecipientUrl );

          if ( log.isDebugEnabled() )
          {
            log.debug( "Forwarding request to URL : " + purlRecipientUrl );
          }
          request.getRequestDispatcher( purlRecipientUrl ).forward( servletRequest, servletResponse );
        }
      }
      else
      {
        if ( log.isDebugEnabled() )
        {
          log.debug( "com.biperf.core.ui.servlet.PurlRecipientAccessFilter continuing down filter chain" );
        }
        filterChain.doFilter( servletRequest, servletResponse );
        if ( log.isDebugEnabled() )
        {
          log.debug( "com.biperf.core.ui.servlet.PurlRecipientAccessFilter coming back from filter chain" );
        }
      }
    }
    finally
    {
      if ( log.isDebugEnabled() )
      {
        log.debug( "com.biperf.core.ui.servlet.PurlRecipientAccessFilter clean up code" );
      }
    }
  }

  public static void setPurlAccessUrlInSession( HttpServletRequest request, String purlAccessUrl )
  {
    request.getSession().setAttribute( SESSION_PURL_ACCESS_URL, purlAccessUrl );
  }

  public static String getPurlAccessUrlFromSession( HttpServletRequest request )
  {
    return (String)request.getSession().getAttribute( SESSION_PURL_ACCESS_URL );
  }

  private boolean byPassRequest( String path )
  {
    for ( String exclusion : exclusionPaths )
    {
      if ( path.indexOf( exclusion ) != -1 )
      {
        return true;
      }
    }
    // In local all requests pass through this filter, so byPass all static content
    if ( !Environment.isCtech() && isStaticContentAccess( path ) )
    {
      return true;
    }
    return false;
  }

  private String getInvalidUrl( String path, String userId )
  {
    PurlRecipientRequestWrapper wrapper = new PurlRecipientRequestWrapper( path );

    String invalidUrl = "";

    if ( null == wrapper.getPurlRecipientId() || null == wrapper.getFirstName() || null == wrapper.getLastName() )
    {
      invalidUrl = this.purlRecipientPath + "?method=invalid";
    }
    else
    {
      PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( wrapper.getPurlRecipientId() );
      if ( !isPurlValid( purlRecipient, wrapper.getFirstName(), wrapper.getLastName() ) )
      {
        invalidUrl = this.purlRecipientPath + "?method=invalid";
      }
      else if ( isPurlExpired( purlRecipient ) )
      {
        invalidUrl = this.purlRecipientPath + "?method=expired";
      }
    }
    return invalidUrl;
  }

  private String getSACelebrationUrl( String path, String userId )
  {
    PurlRecipientRequestWrapper wrapper = new PurlRecipientRequestWrapper( path );

    PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( wrapper.getPurlRecipientId() );
    Long claimId = purlRecipient.getClaim().getId();
    String celebrationUrl = "";
    try
    {
      if ( null != claimId && null != userId )
      {
        int numOfDays = getSystemVariableService().getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();

        String celebrationId = getServiceAnniversaryService().getCelebrationIdByClaim( claimId, Long.parseLong( userId ), numOfDays );
        if ( !StringUtil.isEmpty( celebrationId ) )
        {
          celebrationUrl = getServiceAnniversaryService().getContributePageUrl( celebrationId, getUserService().getRosterUserIdByUserId( Long.parseLong( userId ) ).toString() );
        }
      }
    }
    catch( DataException e )
    {
      log.error( "Data Exception while getting page url " + e.getMessage() );
    }
    catch( Exception e )
    {
      log.error( "Error while getting page url " + e.getMessage() );
    }

    return celebrationUrl;
  }

  private String getPurlRecipientUrl( String path, String userId )
  {
    PurlRecipientRequestWrapper wrapper = new PurlRecipientRequestWrapper( path );

    Map<String, Object> clientStateParameterMap = new HashMap<String, Object>();

    if ( null == wrapper.getPurlRecipientId() || null == wrapper.getFirstName() || null == wrapper.getLastName() )
    {
      return this.purlRecipientPath + "?method=invalid";
    }
    else
    {
      PurlRecipient purlRecipient = getPurlService().getPurlRecipientById( wrapper.getPurlRecipientId() );
      if ( !isPurlValid( purlRecipient, wrapper.getFirstName(), wrapper.getLastName() ) )
      {
        return this.purlRecipientPath + "?method=invalid";
      }
      else if ( isPurlExpired( purlRecipient ) )
      {
        return this.purlRecipientPath + "?method=expired";
      }
      else
      {
        clientStateParameterMap.put( "purlRecipientId", wrapper.getPurlRecipientId() );
        if ( StringUtils.isNotEmpty( userId ) )
        {
          clientStateParameterMap.put( "loggedinUserId", new Long( userId ) );
        }

        return ClientStateUtils.generateEncodedLink( "", this.purlRecipientPath + "?method=display", clientStateParameterMap );
      }
    }
  }

  private boolean isPurlValid( PurlRecipient purlRecipient, String firstName, String lastName )
  {
    if ( null == purlRecipient )
    {
      return false;
    }

    if ( !firstName.toLowerCase().equals( purlRecipient.getUser().getFirstName().toLowerCase() ) )
    {
      return false;
    }

    if ( !lastName.toLowerCase().equals( purlRecipient.getUser().getLastName().toLowerCase() ) )
    {
      return false;
    }

    if ( !purlRecipient.getUser().isActive() )
    {
      return false;
    }

    return true;
  }

  private boolean isPurlExpired( PurlRecipient purlRecipient )
  {
    String state = purlRecipient.getState().getCode();
    if ( PurlRecipientState.EXPIRED.equals( state ) )
    {
      return true;
    }
    Date awardDate = purlRecipient.getAwardDate();
    int daysToExpire = getSystemVariableService().getPropertyByName( SystemVariableService.PURL_DAYS_TO_EXP ).getIntVal();
    Date adjustedAwardDate = new Date( new Date().getTime() - daysToExpire * org.apache.commons.lang3.time.DateUtils.MILLIS_PER_DAY );
    return null != awardDate && awardDate.before( adjustedAwardDate );
  }

  private boolean isPurlUrl( String incomingDomainName )
  {
    String purlUrlPath = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.PURL_URL_PREFIX ).getStringVal();
    String purlDomainName = getDomainName( purlUrlPath.toLowerCase() );
    return null != purlDomainName && purlDomainName.equals( incomingDomainName );
  }

  private String getDomainName( String path )
  {
    String serverName = null;
    try
    {
      URL url = new URL( path );
      serverName = url.getHost();
    }
    catch( MalformedURLException e )
    {
      log.error( "Malformed URL [" + path + "]", e );
    }
    return serverName;
  }

  private static PurlService getPurlService()
  {
    return (PurlService)BeanLocator.getBean( PurlService.BEAN_NAME );
  }

  private ServiceAnniversaryService getServiceAnniversaryService()
  {
    return (ServiceAnniversaryService)BeanLocator.getBean( ServiceAnniversaryService.BEAN_NAME );
  }

  private static UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  @SuppressWarnings( "serial" )
  class PurlRecipientRequestWrapper implements Serializable
  {
    private Long purlRecipientId;
    private String firstName;
    private String lastName;

    public PurlRecipientRequestWrapper( String requestUrl )
    {
      String[] requestToken = requestUrl.substring( requestUrl.lastIndexOf( '/' ) + 1 ).split( "\\." );
      URLDecoder decoder = new URLDecoder();
      if ( requestToken.length == 3 )
      {
        try
        {
          String firstNameForDecode = decoder.decode( requestToken[0], "UTF-8" );
          String lastNameForDecode = decoder.decode( requestToken[1], "UTF-8" );
          purlRecipientId = new Long( requestToken[2] );

          firstName = firstNameForDecode.replace( PurlService.PURL_RECIPIENT_URL_DELIMITER, PurlService.PURL_RECIPIENT_URL_NAME );
          lastName = lastNameForDecode.replace( PurlService.PURL_RECIPIENT_URL_DELIMITER, PurlService.PURL_RECIPIENT_URL_NAME );
        }
        catch( NumberFormatException e )
        {
          // Do nothing
        }
        catch( UnsupportedEncodingException uee )
        {
        }
      }
    }

    public Long getPurlRecipientId()
    {
      return purlRecipientId;
    }

    public String getFirstName()
    {
      return firstName;
    }

    public String getLastName()
    {
      return lastName;
    }

  }

}
