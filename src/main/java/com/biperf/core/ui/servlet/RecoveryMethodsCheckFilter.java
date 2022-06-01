
package com.biperf.core.ui.servlet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.RecognitionAdvisorUtil;
import com.biperf.core.utils.UserManager;

@SuppressWarnings( "unused" )
public class RecoveryMethodsCheckFilter extends BaseAccessFilter
{
  private static final Log log = LogFactory.getLog( RecoveryMethodsCheckFilter.class );
  private static final String DEFAULT_RECOVERY_CHECK_EXCLUSION_URLS = "login.do,logout.do,userByContactInformation.action,contactAutocomplete.action,forgotEmail.action,resetPasswordByEmail.action,userToken.action,resetPasswordByToken.action,resetPassword.action,activated.action,validateActivation.action,contactsActivation.action,activationLink.action,countryPhones.action";
  private static final String RECOVERY_CHECK_EXCLUSION_URLS_CONFIG_PARAM = "REC_EXCLUSION_URLS";

  @Override
  public void init( FilterConfig filterConfig ) throws ServletException
  {
    super.init( filterConfig );
    buildExclusions( filterConfig, DEFAULT_RECOVERY_CHECK_EXCLUSION_URLS, RECOVERY_CHECK_EXCLUSION_URLS_CONFIG_PARAM );
  }

  @Override
  public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException
  {
    HttpServletRequest request = (HttpServletRequest)servletRequest;
    HttpServletResponse response = (HttpServletResponse)servletResponse;

    // Redirect or continue on filter chain
    if ( isValidUrlForUser( request ) )
    {
      filterChain.doFilter( servletRequest, servletResponse );
    }
    else
    {
      RecognitionAdvisorUtil.setUpLogin( request );
      // TODO: GET FINAL URL FROM TOM
      response.sendRedirect( RequestUtils.getBaseURI( request ) + "/login.do?recovery=true" );
    }
  }

  private boolean isValidUrlForUser( HttpServletRequest request )
  {
    AuthenticatedUser user = UserManager.getUser();

    // No user logged in? Skip the filter. Auth filter is before this, so user exists on first login call.
    if ( user == null )
    {
      return true;
    }
    
    // NOTE: do we need to add a bypass for PURLs - the filter if registered prior to this one???
    if ( null != user && ( user.isRecoveryMethodsCollected() || user.isLaunched() || user.isDelegate() ) )
    {
      return true;
    }

    return isByPassRequest( RequestUtils.getRequestUrl( request ) );
  }

  private boolean isByPassRequest( String path )
  {
    for ( String exclusion : exclusionPaths )
    {
      if ( path.indexOf( exclusion ) != -1 )
      {
        return true;
      }
    }

    return isStaticContentAccess( path );
  }
}
