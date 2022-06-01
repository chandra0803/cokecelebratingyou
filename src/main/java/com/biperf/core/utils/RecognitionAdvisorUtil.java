
package com.biperf.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.utils.RequestUtils;

public class RecognitionAdvisorUtil
{

  private static final Log logger = LogFactory.getLog( RecognitionAdvisorUtil.class );

  public static void setUpLogin( HttpServletRequest request )
  {
    if ( isValidFlow( request, true ) && Objects.isNull( request.getSession().getAttribute( "isRaFlow" ) ) )
    {
      setupLoginAttributes( request );
    }
  }

  public static void setupLoginAttributes( HttpServletRequest request )
  {
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }
    try
    {
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );

      if ( !isLoginParamsHasErrors( request ) )
      {
        request.getSession().setAttribute( "isRaFlow", new Boolean( (boolean)clientStateMap.get( "isRaFlow" ) ) );

        request.getSession().setAttribute( "managerId", (String)clientStateMap.get( "managerId" ) );
        String reporteeIdEncrypted = (String)clientStateMap.get( "reporteeId" );
        if ( Objects.nonNull( reporteeIdEncrypted ) )
        {
          request.getSession().setAttribute( "reporteeId", reporteeIdEncrypted );
        }
      }
    }
    catch( InvalidClientStateException e )
    {
      logger.error( "Invalid Client state exception" );
    }

  }

  public static String buildRedirect( HttpServletRequest request )
  {
    if ( !isLoginParamsHasErrors( request ) )
    {
      String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
      String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
      String password = ClientStatePasswordManager.getPassword();

      if ( cryptoPass != null && cryptoPass.equals( "1" ) )
      {
        password = ClientStatePasswordManager.getGlobalPassword();
      }
      // Deserialize the client state.
      Map clientStateMap;
      try
      {
        clientStateMap = ClientStateSerializer.deserialize( clientState, password );
        return getRedirectUrl( (String)clientStateMap.get( "reporteeId" ) );
      }
      catch( InvalidClientStateException e )
      {
        logger.error( "Invalid client state exception " );
      }

    }
    return null;
  }

  public static boolean isLoginParamsHasErrors( HttpServletRequest request )
  {
    boolean hasErrors = false;
    String clientState = RequestUtils.getRequiredParamString( request, "clientState" );
    String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
    String password = ClientStatePasswordManager.getPassword();

    if ( cryptoPass != null && cryptoPass.equals( "1" ) )
    {
      password = ClientStatePasswordManager.getGlobalPassword();
    }

    String managerIdEncrypted = null;
    String reporteeIdEncrypted = null;
    try
    {
      Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
      managerIdEncrypted = (String)clientStateMap.get( "managerId" );
      reporteeIdEncrypted = (String)clientStateMap.get( "reporteeId" );
    }
    catch( InvalidClientStateException e )
    {
      logger.error( "Invalid client state exception " );
    }

    Long managerId = Objects.nonNull( managerIdEncrypted ) ? new Long( managerIdEncrypted ) : null;
    Long reporteeId = Objects.nonNull( reporteeIdEncrypted ) ? new Long( reporteeIdEncrypted ) : null;

    if ( logger.isDebugEnabled() )
    {
      logger.debug( "managerIdEncrypted value is " + managerIdEncrypted + ", reporteeIdEncrypted value is " + reporteeIdEncrypted + ", managerId value is " + managerId );
    }

    if ( Objects.isNull( managerId ) )
    {
      logger.error( "RA flow: not valid manager ID present" );
      hasErrors = true;
    }
    else if ( Objects.nonNull( reporteeIdEncrypted ) && Objects.isNull( reporteeId ) )
    {
      logger.error( "RA flow: not valid reportee ID present" );
      hasErrors = true;
    }
    else if ( Objects.nonNull( UserManager.getUserId() ) && !managerId.equals( UserManager.getUserId() ) )
    {
      logger.error( "RA flow: manager identifier doesnt match with logged in user ID" );
      hasErrors = true;
    }
    else if ( Objects.nonNull( reporteeId ) && managerId.equals( reporteeId ) )
    {
      logger.error( "RA flow: manager identifier is same as reportee identifier" );
      hasErrors = true;
    }
    return hasErrors;
  }

  public static boolean isLoginParamsHasErrorsPostLogin( HttpServletRequest request )
  {
    boolean hasErrors = false;
    String managerIdEncrypted = (String)request.getSession().getAttribute( "managerId" );
    Long managerId = new Long( managerIdEncrypted );

    if ( Objects.isNull( UserManager.getUserId() ) || Objects.isNull( managerId ) || !managerId.equals( UserManager.getUserId() ) )
    {
      logger.error( "RA flow: manager identifier doesnt match with logged in user ID" );
      hasErrors = true;
    }

    return hasErrors;
  }

  public static String getRedirectUrl( String reporteeIdEncrypted )
  {
    String siteUrlPrefix = getSystemVariableService().getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    if ( Objects.nonNull( reporteeIdEncrypted ) )
    {

      Map<String, Object> parms = new HashMap<String, Object>();
      parms.put( "reporteeId", reporteeIdEncrypted );
      parms.put( "isRARecognitionFlow", "yes" );
      parms.put( "isRaFlow", true );

      return ClientStateUtils.generateEncodedLink( "", siteUrlPrefix + "/recognitionWizard/sendRecognitionDisplay.do", parms );

    }
    else
    {
      return siteUrlPrefix + "/raDetails.do";
    }
  }

  public static boolean isValidFlow( HttpServletRequest request, boolean checkRequest )
  {
    Boolean isRaFlow = false;
    if ( checkRequest )
    {
      String clientState = RequestUtils.getOptionalParamString( request, "clientState" );

      if ( StringUtils.isNotBlank( clientState ) )
      {
        String cryptoPass = RequestUtils.getOptionalParamString( request, "cryptoPass" );
        String password = ClientStatePasswordManager.getPassword();

        if ( cryptoPass != null && cryptoPass.equals( "1" ) )
        {
          password = ClientStatePasswordManager.getGlobalPassword();
        }

        try
        {
          Map clientStateMap = ClientStateSerializer.deserialize( clientState, password );
          isRaFlow = new Boolean( (Boolean)clientStateMap.get( "isRaFlow" ) );
        }
        catch( InvalidClientStateException e )
        {
          logger.error( "Invalid client state exception" );
        }

        if ( logger.isDebugEnabled() )
        {
          logger.debug( "raEnabled value is " + isRAEnabled() + ", isRaFlow value is " + isRaFlow );
        }
      }

    }

    else
    {
      isRaFlow = (Boolean)request.getSession().getAttribute( "isRaFlow" );
      if ( !Objects.nonNull( isRaFlow ) || !isRaFlow )
      {
        isRaFlow = new Boolean( request.getParameter( "isRaFlow" ) );
      }
    }
    return isRAEnabled() && Objects.nonNull( isRaFlow ) && isRaFlow;
  }

  public static boolean isRAEnabled()
  {
    return getSystemVariableService().getPropertyByName( SystemVariableService.RA_ENABLE ).getBooleanVal();
  }

  public static void clearLoginParams( HttpServletRequest request )
  {
    request.getSession().removeAttribute( "isRaFlow" );
    request.getSession().removeAttribute( "managerId" );
    request.getSession().removeAttribute( "reporteeId" );

  }

  /**
   * @return SystemVariableService
   */
  private static SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)BeanLocator.getBean( SystemVariableService.BEAN_NAME );
  }

}
