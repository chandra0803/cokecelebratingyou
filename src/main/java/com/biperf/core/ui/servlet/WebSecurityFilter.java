
package com.biperf.core.ui.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

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

import com.biperf.core.domain.enums.ParticipantTermsAcceptance;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.security.AuthorizationService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.Environment;
import com.biperf.core.utils.PageConstants;
import com.biperf.core.utils.RecognitionAdvisorUtil;
import com.biperf.core.utils.UserManager;

@SuppressWarnings( "unused" )
public class WebSecurityFilter extends BaseAccessFilter
{
  private static final Log log = LogFactory.getLog( WebSecurityFilter.class );
  private static final String DEFAULT_TNC_EXCLUSION_URLS = "loginPageFirstTime.do,loginPageFirstTimeSaveInfo.do,loginPageFirstTimeUploadAvatar.do,login.do,loginPageHelp.do,changePasswordViewNew.do,saveNewPassword.do";
  private static final String TNC_EXCLUSION_URLS_CONFIG_PARAM = "TNC_EXCLUSION_URLS";

  @Override
  public void init( FilterConfig filterConfig ) throws ServletException
  {
    super.init( filterConfig );
    buildExclusions( filterConfig, DEFAULT_TNC_EXCLUSION_URLS, TNC_EXCLUSION_URLS_CONFIG_PARAM );
  }

  @Override
  public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException
  {
    HttpServletRequest request = (HttpServletRequest)servletRequest;
    HttpServletResponse response = (HttpServletResponse)servletResponse;

    if ( log.isDebugEnabled() )
    {
      log.debug( "WebSecurityFilter.doFilter begin " + request.getRequestURL() );
    }

    boolean isSecureRequest = request.isSecure();
    String domain = request.getServerName();
    String path = request.getContextPath();

    // Set X-UA-Compatible on response header
    /**
     *  Edge mode tells Internet Explorer to display content in the highest mode available. 
     *  If a future release of Internet Explorer supported a higher compatibility mode, 
     *  pages set to edge mode would appear in the highest mode supported by that version.
     *  Those same pages would still appear in IE9 mode when viewed with Internet Explorer 9.
     */
    response.setHeader( "X-UA-Compatible", "IE=Edge,chrome=1" );

    // Added X-Frame-Options: SAMEORIGIN The page can only be displayed in a frame on the
    // same origin as the page itself. This setting will prevent other sites from putting our site
    // to be displayed in a frame. This protects site from clickjacking attacks
    response.setHeader( "X-Frame-Options", "SAMEORIGIN" );

    // If request is NOT secure but should be forced to https
    if ( !isSecureRequest && isSiteUrl( domain ) && isForceSecure() )
    {
      // Redirect as secure URL
      String redirectUrl = getRedirectUrl( request );
      response.sendRedirect( redirectUrl );
      return;
    }

    /*
     * Bug #37346 will be fixed in 4.1.2, so commenting this part out //If response contains COOKIE
     * if ( response.containsHeader(HEADER_COOKIE) ) { //Set JSESSIONID in response with appropriate
     * Secure and HttpOnly flags response.setHeader(HEADER_COOKIE, getCookieAsString( "JSESSIONID",
     * request.getSession().getId(), COOKIE_VERSION, domain, path, isSecureRequest,
     * COOKIE_IS_HTTP_ONLY )); //JROUTE cookie is set when using hubble instance or when in
     * QA/PPRD/PROD if( isWebServerUsed(domain) ) { response.addHeader(HEADER_COOKIE,
     * getCookieAsString( "JROUTE", request.getHeader("proxy-jroute"), COOKIE_VERSION, domain, path,
     * isSecureRequest, COOKIE_IS_HTTP_ONLY )); } }
     */

    // if user has not accepted terms and conditions, redirect to first time login page
    String tncRedirectUrl = null;
    if ( UserManager.getUserId() != null && !bypassTncRequest( request.getRequestURI() ) )
    {
      // we are setting this attribute in firstTimeLoginAction.java once they done with first time
      // login setup.
      if ( request.getSession().getAttribute( "userObj" ) == null )
      {
        boolean isTermsAndConditionsUsed = getSystemVariableService().getPropertyByName( SystemVariableService.TERMS_CONDITIONS_USED ).getBooleanVal();
        boolean isLoginAs = getAuthorizationService().isUserInRole( AuthorizationService.ROLE_CODE_LOGIN_AS );
        User userObj = getUserService().getUserById( UserManager.getUserId() );
        Participant participant = null;

        boolean tncDenyAccess = false;
        if ( isTermsAndConditionsUsed )
        {
          if ( userObj.isParticipant() )
          {
            participant = getParticipantService().getParticipantById( userObj.getId() );
            if ( participant.getTermsAcceptance() == null || participant.getTermsAcceptance().getCode().equals( ParticipantTermsAcceptance.NOTACCEPTED )
                || participant.getTermsAcceptance().getCode().equals( ParticipantTermsAcceptance.DECLINED ) )
            {
              tncDenyAccess = true;
            }
          }
        }

        // When tnc's used, do not allow not-accepted or declined through to the site - take him to
        // first time login page
        // Do not allow user to enter the site unless he set up profile - take him to first time
        // login page
        // However, do not redirect if recovery methods are not collected. Instead it will be
        // redirected to
        // login.do?recovery=true from RecoveryMethodsCheckFilter. RecoveryMethodsCheckFilter
        // executed after to WebSecurityFilter.
        if ( ( tncDenyAccess || ( userObj.isParticipant() && !userObj.isProfileSetupDone() && !isLoginAs ) ) && UserManager.getUser().isRecoveryMethodsCollected() )
        {
          tncRedirectUrl = RequestUtils.getBaseURI( request ) + PageConstants.FIRST_TIME_LOGIN_PAGE;
        }
        else
        {
          if ( userObj.isParticipant() && ( userObj.isForcePasswordChange() || isPasswordExpired( userObj ) ) && !isLoginAs
              && ( Objects.nonNull( request.getSession().getAttribute( "ssoLogin" ) ) ? false : true ) )
          {
            tncRedirectUrl = RequestUtils.getBaseURI( request ) + PageConstants.CHANGE_PWD_URL;

          }
          else
          {
            request.getSession().setAttribute( "userObj", userObj );
          }
        }
      }
    }

    // Redirect or continue on filter chain
    if ( tncRedirectUrl != null )
    {
      RecognitionAdvisorUtil.setUpLogin( request );
      response.sendRedirect( tncRedirectUrl );
    }
    else
    {
      // Continue filter chain
      if ( log.isDebugEnabled() )
      {
        log.debug( "WebSecurityFilter.doFilter continuing down chain " );
      }

      filterChain.doFilter( servletRequest, servletResponse );

      if ( log.isDebugEnabled() )
      {
        log.debug( "WebSecurityFilter.doFilter coming back from chain " );
      }
    }

  }

  private String getRedirectUrl( HttpServletRequest request )
  {
    StringBuilder redirectUrl = new StringBuilder();
    redirectUrl.append( request.getRequestURL() );

    if ( !StringUtils.isEmpty( request.getQueryString() ) )
    {
      redirectUrl.append( '?' );
      redirectUrl.append( request.getQueryString() );
    }

    int startIdx = redirectUrl.indexOf( PROTOCOL_HTTP );
    redirectUrl.replace( startIdx, startIdx + PROTOCOL_HTTP.length(), PROTOCOL_HTTPS );

    if ( !isWebServerUsed( request.getServerName() ) )
    {
      startIdx = redirectUrl.indexOf( PORT_HTTP );
      if ( startIdx != -1 )
      {
        redirectUrl.replace( startIdx, startIdx + PORT_HTTP.length(), PORT_HTTPS );
      }
    }
    return redirectUrl.toString();
  }

  private boolean isPasswordExpired( User user )
  {
    Date lastResetDate = user.getLastResetDate();
    if ( lastResetDate != null )
    {
      long expiredPeriod = getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_EXPIRED_PERIOD ).getLongVal();
      return System.currentTimeMillis() - user.getLastResetDate().getTime() > expiredPeriod;
    }
    return false;
  }

  private boolean bypassTncRequest( String path )
  {
    for ( String exclusion : exclusionPaths )
    {
      if ( path.indexOf( exclusion ) != -1 )
      {
        return true;
      }
    }

    return false;
  }

  private boolean isWebServerUsed( String domain )
  {
    // In QA/PPRD/PROD isWebServerUsed would have been set in init()
    if ( null == isWebServerUsed )
    {
      // In Local isWebServerUsed will be set here, based on hubble instance usage
      isWebServerUsed = domain.startsWith( "ndsdev" ) || domain.startsWith( "cwsdev" );
    }
    return isWebServerUsed;
  }

  private boolean isForceSecure()
  {
    return Environment.isCtech();
  }

  private String getCookieAsString( String name, String value, int version, String domain, String path, boolean isSecureRequest, boolean isHttpOnly )
  {
    StringBuilder buffer = new StringBuilder();

    buffer.append( name );
    buffer.append( "=" );
    buffer.append( value );

    buffer.append( "; Version=" );
    buffer.append( version );

    buffer.append( "; Comment=Cookie generated programatically to set 'Secure' and 'HttpOnly' flags" );

    buffer.append( "; Path=" );
    buffer.append( path );

    if ( isSecureRequest )
    {
      buffer.append( "; Secure" );
    }

    if ( isHttpOnly )
    {
      buffer.append( "; HttpOnly" );
    }

    return buffer.toString();
  }

  private UserService getUserService()
  {
    return (UserService)BeanLocator.getBean( UserService.BEAN_NAME );
  }

  private static AuthorizationService getAuthorizationService()
  {
    return (AuthorizationService)BeanLocator.getBean( AuthorizationService.BEAN_NAME );
  }

}
