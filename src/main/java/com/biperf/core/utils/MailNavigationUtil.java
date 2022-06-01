
package com.biperf.core.utils;

import java.net.URLEncoder;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.system.SystemVariableService;

public class MailNavigationUtil
{
  private static final Log logger = LogFactory.getLog( RecognitionAdvisorUtil.class );

  public static boolean isValidFlow( HttpServletRequest request )
  {
    try
    {
      if ( Objects.nonNull( request.getSession().getAttribute( "celebrationFlow" ) ) && request.getSession().getAttribute( "celebrationFlow" ).equals( "celebrationPage" ) )
      {
        return new Boolean( Objects.nonNull( request.getSession().getAttribute( "clientStateCelebPage" ) ) && Objects.nonNull( request.getSession().getAttribute( "cryptoPassCelebPage" ) ) );
      }
    }
    catch( Exception e )
    {
      return false;
    }
    return false;

  }

  public static void clearCelebrationParams( HttpServletRequest request )
  {
    try
    {
      request.getSession().removeAttribute( "celebrationFlow" );
      request.getSession().removeAttribute( "clientStateCelebPage" );
      request.getSession().removeAttribute( "cryptoPassCelebPage" );
    }
    catch( Exception e )
    {
      logger.error( "Expection on removing celebration attributes " );
    }
  }

  public static String getRedirectUrl( HttpServletRequest request ) throws Exception
  {
    String clientState = URLEncoder.encode( (String)request.getSession().getAttribute( "clientStateCelebPage" ), "UTF-8" );
    String cryptoPass = URLEncoder.encode( (String)request.getSession().getAttribute( "cryptoPassCelebPage" ), "UTF-8" );
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/celebration/celebrationPage.do?" + "clientState="
        + clientState + "&cryptoPass=" + cryptoPass;

    return siteUrlPrefix;
  }

  public static boolean isLoginParamsHasErrorsPostLogin( HttpServletRequest request ) throws Exception
  {
    boolean hasErrors = false;
    try
    {
      String clientState = (String)request.getSession().getAttribute( "clientStateCelebPage" );
      String cryptoPass = (String)request.getSession().getAttribute( "cryptoPassCelebPage" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }

      String recipientIdEncrypted = null;
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      recipientIdEncrypted = (String)clientStateMap.get( "recipientId" );
      Long recipientId = Objects.nonNull( recipientIdEncrypted ) ? new Long( recipientIdEncrypted ) : null;

      if ( recipientId.equals( UserManager.getUserId() ) || UserManager.getUser().isManager() || UserManager.getUser().isOwner() )
      {
        hasErrors = false;
      }
      else
      {
        logger.error( "Mail Navigation Flow: Celebration Page doesnt match with logged in user ID" );
        hasErrors = true;
      }
    }
    catch( Exception e )
    {
      logger.error( "Error in processing login params to redirect celebration page " + e );
      clearCelebrationParams( request );
      hasErrors = true;
    }

    return hasErrors;
  }

  /**
   * @return SystemVariableService
   */
  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
