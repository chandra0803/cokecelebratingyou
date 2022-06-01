
package com.biperf.core.ui.servlet;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContext;

import com.biperf.core.security.AuthenticatedUser;
import com.biperf.core.service.maincontent.DesignThemeService;
import com.biperf.core.service.maincontent.MainContentService;
import com.biperf.core.service.participant.AudienceService;
import com.biperf.core.service.security.AuthenticationService;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.UserManager;

/**
 * SessionUtils <p/> 
 * <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>robinsra</td>
 * <td>Sept 6, 2013</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SessionUtils
{
  private static final Log logger = LogFactory.getLog( SessionUtils.class );

  /**
   * clearUserSession
   * @param httpSession
   */
  public static void clearUserSession( HttpSession httpSession )
  {
    SecurityContext acegiContext = (SecurityContext)httpSession.getAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );
    if ( acegiContext != null )
    {
      httpSession.removeAttribute( AuthenticationService.SPRING_SECURITY_CONTEXT );
      if ( logger.isDebugEnabled() )
      {
        logger.debug( "Got acegiContext" );
      }
      if ( acegiContext.getAuthentication() == null )
      {
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "No acegiContext authentication" );
        }
        return;
      }

      Object obj = acegiContext.getAuthentication().getPrincipal();
      AuthenticatedUser authenticatedUser = null;
      if ( obj instanceof AuthenticatedUser )
      {
        authenticatedUser = (AuthenticatedUser)obj;
      }
      
      if ( authenticatedUser != null )
      {
        if ( logger.isDebugEnabled() )
        {
          logger.debug( "authenticatedUser is not null" );
        }
        // Do the stuff from logout action
        MainContentService mainContentService = (MainContentService)BeanLocator.getBean( MainContentService.BEAN_NAME );
        DesignThemeService designThemeService = (DesignThemeService)BeanLocator.getBean( DesignThemeService.BEAN_NAME );
        AudienceService audienceService = (AudienceService)BeanLocator.getBean( AudienceService.BEAN_NAME );

        mainContentService.processLogout( authenticatedUser );
        audienceService.processLogout( authenticatedUser );
        designThemeService.processLogout( authenticatedUser );

        acegiContext.setAuthentication( null );
        UserManager.removeUser();
      }
    }
  }
}
