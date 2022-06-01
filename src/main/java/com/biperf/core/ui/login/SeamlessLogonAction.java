
package com.biperf.core.ui.login;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.security.credentials.StandardLoginIdSeamlessLogonCredentials;
import com.biperf.core.security.credentials.StandardSSOIdSeamlessLogonCredentials;
import com.biperf.core.service.awardbanq.AwardBanQService;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.ui.BaseDispatchAction;
import com.biperf.core.ui.constants.ActionConstants;
import com.biperf.core.ui.servlet.SessionUtils;
import com.biperf.core.ui.utils.RequestUtils;
import com.biperf.core.utils.BeanLocator;
import com.biperf.core.utils.IPAddressUtils;
import com.biperf.core.utils.SecurityUtils;
import com.biperf.core.utils.StringUtil;
import com.biperf.core.utils.crypto.SHA256HashSeamless;

/**
 * 
 * SeamlessLogonAction.
 * <p>
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
 * <td>Mar 29, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */

public class SeamlessLogonAction extends BaseDispatchAction
{
  private static final Log logger = LogFactory.getLog( SeamlessLogonAction.class );

  /**
   *  These are the request parameters, sent by client.
   */
  private static final String SSO_UNIQUE_ID = "UniqueID";
  private static final String SSO_TIME_STAMP = "TimeStamp";
  private static final String SSO_HASH_STRING = "HashString";
  private AwardBanQServiceFactory awardBanQServiceFactory;

  /**
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */

  public ActionForward unspecified( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {
    SessionUtils.clearUserSession( request.getSession() );

    ActionMessages errors = new ActionMessages();
    boolean detLoggingOn = isDetailLoggingOn();

    Map requestParams = buildParmMap( request, errors );
    if ( !errors.isEmpty() )
    {
      if ( detLoggingOn )
      {
        logger.error( "Errors during buildParmMap" );
      }
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    SeamlessLogonForm form = (SeamlessLogonForm)actionForm;

    String userName = "";
    String userNameEncrypted = "";

    // set the credentials to session
    String ssoUniqueId = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_UNIQUE_ID ).getStringVal();

    if ( ssoUniqueId.equalsIgnoreCase( "SSO ID" ) )
    {
      StandardSSOIdSeamlessLogonCredentials ssoIdSeamlessLogonCredentials = (StandardSSOIdSeamlessLogonCredentials)buildCredentials( request, requestParams, ssoUniqueId );

      form.setClassObjName( "SSO_ID" );
      form.setUniqueId( ssoIdSeamlessLogonCredentials.getUniqueId() );
      form.setTimeStamp( ssoIdSeamlessLogonCredentials.getTimeStamp() );
      form.setHashString( ssoIdSeamlessLogonCredentials.getHashString() );

      request.getSession().setAttribute( "LOGIN_CREDENTIALS", ssoIdSeamlessLogonCredentials );
      userName = getUserName( ssoIdSeamlessLogonCredentials, errors );
    }
    else if ( ssoUniqueId.equalsIgnoreCase( "Login ID" ) )
    {
      StandardLoginIdSeamlessLogonCredentials loginIdSeamlessLogonCredentials = (StandardLoginIdSeamlessLogonCredentials)buildCredentials( request, requestParams, ssoUniqueId );

      form.setClassObjName( "Login_ID" );
      form.setUniqueId( loginIdSeamlessLogonCredentials.getUniqueId() );
      form.setTimeStamp( loginIdSeamlessLogonCredentials.getTimeStamp() );
      form.setHashString( loginIdSeamlessLogonCredentials.getHashString() );

      request.getSession().setAttribute( "LOGIN_CREDENTIALS", loginIdSeamlessLogonCredentials );
      userName = getUserName( loginIdSeamlessLogonCredentials, errors );
    }

    if ( !StringUtil.isNullOrEmpty( userName ) )
    {
      userNameEncrypted = encryptUserNameAES( userName );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    // redirect
    if ( detLoggingOn )
    {
      if ( StringUtil.isNullOrEmpty( userName ) )
      {
        logger.error( "Username is empty!, Please check System variable 'sso.unique.id' or Make sure the username available in DB." );
      }

      String userAgentHeader = request.getHeader( "user-agent" );
      logger.error( "userName=" + userName + " Browser user-agent=" + userAgentHeader );
      logger.error( "SSO - redirect to acegi. userName=" + userName + " userNameEncrypted=" + userNameEncrypted );
    }

    form.setActionURL( RequestUtils.getBaseURI( request ) + "/j_acegi_security_check.do" );
    request.setAttribute( "j_username", userName );
    /*
     * Since it passes through various filters, request attribute and header will not work out.
     * Hence adding session attribute as an exceptional case.
     */
    request.getSession().setAttribute( "ssoLogin", true );
    request.getSession().setAttribute( "j_username_encrypted", userNameEncrypted );
    form.setJusernameEncrypted( userNameEncrypted );

    return mapping.findForward( ActionConstants.LOGIN_FORWARD );
  }

  private String encryptUserNameAES( String userName )
  {
    String aesKey = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_AES256_KEY ).getStringVal();
    String aesInitVector = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_INIT_VECTOR ).getStringVal();
    String encryptedValue = null;
    try
    {
      encryptedValue = SecurityUtils.encryptAES( userName, aesKey, aesInitVector );
    }
    catch( Exception e )
    {
      logger.error( "Unable to encrypt username: " + userName, e );
    }
    return encryptedValue;
  }

  /**
   * Verifies the request parameters and return them in a map.
   * @param request
   * @param errors
   * @return String
   */
  private Map buildParmMap( HttpServletRequest request, ActionMessages errors )
  {
    String hashString = null;
    String uniqueId = null;
    String timeStamp = null;
    HashMap reqParams = null;
    boolean detLoggingOn = isDetailLoggingOn();

    try
    {
      // Required params
      hashString = RequestUtils.getRequiredParamString( request, SSO_HASH_STRING );
      if ( detLoggingOn )
      {
        logger.error( "**** SSO input parameters - SSO_HASH_STRING :" + hashString );
      }

      // guard - empty strings
      if ( "".equals( hashString ) || "".equals( hashString ) )
      {
        throw new IllegalArgumentException();
      }

      uniqueId = RequestUtils.getRequiredParamString( request, SSO_UNIQUE_ID );
      if ( detLoggingOn )
      {
        logger.error( "**** SSO input parameters - SSO_UNIQUE_ID :" + uniqueId );
      }

      // guard - empty strings
      if ( "".equals( uniqueId ) || "".equals( uniqueId ) )
      {
        throw new IllegalArgumentException();
      }

      timeStamp = RequestUtils.getRequiredParamString( request, SSO_TIME_STAMP );
      if ( detLoggingOn )
      {
        logger.error( "**** SSO input parameters - SSO_TIME_STAMP :" + timeStamp );
      }

      // guard - empty strings
      if ( "".equals( timeStamp ) || "".equals( timeStamp ) )
      {
        logger.error( "Empty timeStamp" );
        throw new IllegalArgumentException();
      }

      reqParams = new HashMap();
      reqParams.put( SSO_HASH_STRING, hashString );
      reqParams.put( SSO_UNIQUE_ID, uniqueId );
      reqParams.put( SSO_TIME_STAMP, timeStamp );
    }
    catch( IllegalArgumentException iae )
    {
      if ( hashString == null || "".equals( hashString ) )
      {
        // errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        // "login.seamlesslogon.SSO_REQUEST_PARAM_HASH_STRING_MISSING" ) );
        logger.error( "SSO_HASH_STRING is missing" );
      }
      if ( uniqueId == null || "".equals( uniqueId ) )
      {
        // errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        // "login.seamlesslogon.SSO_REQUEST_PARAM_UNIQUE_ID_MISSING" ) );
        logger.error( "SSO_UNIQUE_ID is missing" );
      }
      if ( timeStamp == null || "".equals( timeStamp ) )
      {
        // errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
        // "login.seamlesslogon.SSO_REQUEST_PARAM_TIME_STAMP_MISSING" ) );
        logger.error( "SSO_TIME_STAMP is missing" );
      }
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "login.seamlesslogon.USER_DOES_NOT_EXIST" ) );
    }
    return reqParams;
  }

  /**
   * This is the default implementation and can be changed by client as per their requirements.
   * @param request
   * @param map 
   * @return Object
   */
  protected Object buildCredentials( HttpServletRequest request, Map map, String ssoUniqueId )
  {
    if ( ssoUniqueId.equalsIgnoreCase( "SSO ID" ) )
    {
      StandardSSOIdSeamlessLogonCredentials credentials = new StandardSSOIdSeamlessLogonCredentials();
      credentials.setHashString( (String)map.get( SSO_HASH_STRING ) );
      credentials.setUniqueId( credentials.getDecryptedValue( (String)map.get( SSO_UNIQUE_ID ) ) );
      credentials.setTimeStamp( credentials.getDecryptedValue( (String)map.get( SSO_TIME_STAMP ) ) );

      if ( isDetailLoggingOn() )
      {
        logger.error( "HashString=" + credentials.getHashString() );
        logger.error( "UniqueID=" + credentials.getUniqueId() );
        logger.error( "TimeStamp=" + credentials.getTimeStamp() );
      }

      return credentials;
    }
    else if ( ssoUniqueId.equalsIgnoreCase( "Login ID" ) )
    {
      StandardLoginIdSeamlessLogonCredentials credentials = new StandardLoginIdSeamlessLogonCredentials();
      credentials.setHashString( (String)map.get( SSO_HASH_STRING ) );
      credentials.setUniqueId( credentials.getDecryptedValue( (String)map.get( SSO_UNIQUE_ID ) ) );
      credentials.setTimeStamp( credentials.getDecryptedValue( (String)map.get( SSO_TIME_STAMP ) ) );

      if ( isDetailLoggingOn() )
      {
        logger.error( "HashString=" + credentials.getHashString() );
        logger.error( "UniqueID=" + credentials.getUniqueId() );
        logger.error( "TimeStamp=" + credentials.getTimeStamp() );
      }

      return credentials;
    }

    return null;
  }

  private String getUserName( StandardSSOIdSeamlessLogonCredentials credentials, ActionMessages errors )
  {
    String userName = null;
    List<Participant> participants = getParticipantService().getUserBySSOId( credentials.getUniqueId() );

    if ( Objects.nonNull( participants ) && ( participants.size() == 1 ) && isTermedUserAndInActive( participants.get( 0 ) ) )
    {

      if ( !getSystemVariableService().getPropertyByName( SystemVariableService.TERMED_USER_ALLOW_REDEEM ).getBooleanVal() )
      {
        errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "login.account.activation.messages.TERMED_USER_SHOPPING_OFF" ) );
      }
      else
      {
        if ( !isUserHasPoints( participants.get( 0 ) ) )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "login.account.activation.messages.TERMED_USER_SHOPPING_ON_NO_POINTS" ) );
        }

      }
    }

    if ( participants == null || participants.size() != 1 )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "login.seamlesslogon.USER_DOES_NOT_EXIST" ) );
    }
    else
    {
      userName = participants.get( 0 ).getUserName();
    }
    return userName;
  }

  private String getUserName( StandardLoginIdSeamlessLogonCredentials credentials, ActionMessages errors )
  {
    String userName = null;
    if ( credentials.getUniqueId() == null )
    {
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "login.seamlesslogon.USER_DOES_NOT_EXIST" ) );
      return userName;
    }

    User user = getUserService().getUserByUserName( credentials.getUniqueId() );
    if ( user == null )
    {
      if ( isDetailLoggingOn() )
      {
        logger.error( "user not found. user/uniqueId=" + credentials.getUniqueId() );
      }
      errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "login.seamlesslogon.USER_DOES_NOT_EXIST" ) );
    }
    else
    {
      Participant pax = null;
      if ( user instanceof Participant )
      {
        pax = (Participant)user;
      }
      if ( Objects.nonNull( pax ) && isTermedUserAndInActive( pax ) )
      {

        if ( !getSystemVariableService().getPropertyByName( SystemVariableService.TERMED_USER_ALLOW_REDEEM ).getBooleanVal() )
        {
          errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "login.account.activation.messages.TERMED_USER_SHOPPING_OFF" ) );
        }
        else
        {
          if ( !isUserHasPoints( pax ) )
          {
            errors.add( ActionMessages.GLOBAL_MESSAGE, new ActionMessage( "login.account.activation.messages.TERMED_USER_SHOPPING_ON_NO_POINTS" ) );
          }

        }
      }
      userName = user.getUserName();
    }
    return userName;
  }

  // --------------------------
  // Testing Methods
  // --------------------------
  /**
   * Method Called to encrypt parameters set on the jsp - then place them back on the request encrypted
   * @param mapping
   * @param actionForm
   * @param request
   * @param response
   * @return ActionForward
   * @throws IOException
   */
  public ActionForward encryptParmsTest( ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response ) throws IOException
  {

    if ( !IPAddressUtils.isAllowAccess( SystemVariableService.ADMIN_IP_RESTRICTIONS, getRemoteAddress( request ) ) )
    {
      logger.error( "IP Address: " + getRemoteAddress( request ) + " is not a valid ip addresses" );
      return mapping.findForward( "access_denied" );
    }
    ActionMessages errors = new ActionMessages();
    String secretKey = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_SECRET_KEY ).getStringVal();
    String aesEncryptionKey = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_AES256_KEY ).getStringVal();
    String aesInitVector = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_INIT_VECTOR ).getStringVal();

    String loginIdUnencrypted = request.getParameter( "loginId" );
    String ssoIdUnencrypted = request.getParameter( "ssoId" );
    String timeStampUnencrypted = request.getParameter( "timeStamp" );

    if ( StringUtils.isNotEmpty( loginIdUnencrypted ) )
    {
      String encryptedLoginId = "";
      try
      {
        encryptedLoginId = SecurityUtils.encryptAES( loginIdUnencrypted, aesEncryptionKey, aesInitVector );
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }

      request.setAttribute( "LoginId_encrypted", encryptedLoginId );
      /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
      // request.setAttribute( "HashString", new MD5Hash().encryptDefault( loginIdUnencrypted +
      // timeStampUnencrypted + secretKey ) );
      request.setAttribute( "HashString", new SHA256HashSeamless().encryptDefault( loginIdUnencrypted + timeStampUnencrypted + secretKey ) );
      /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    }
    else if ( StringUtils.isNotEmpty( ssoIdUnencrypted ) )
    {
      String encryptedSSOId = "";
      try
      {
        encryptedSSOId = SecurityUtils.encryptAES( ssoIdUnencrypted, aesEncryptionKey, aesInitVector );
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }

      request.setAttribute( "LoginId_encrypted", encryptedSSOId );
      /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
      // request.setAttribute( "HashString", new MD5Hash().encryptDefault( ssoIdUnencrypted +
      // timeStampUnencrypted + secretKey ) );
      request.setAttribute( "HashString", new SHA256HashSeamless().encryptDefault( ssoIdUnencrypted + timeStampUnencrypted + secretKey ) );
      /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    }

    if ( StringUtils.isNotEmpty( timeStampUnencrypted ) )
    {
      String encryptedTimeStamp = "";
      try
      {
        encryptedTimeStamp = SecurityUtils.encryptAES( timeStampUnencrypted, aesEncryptionKey, aesInitVector );
      }
      catch( Exception e )
      {
        e.printStackTrace();
      }

      request.setAttribute( "TimeStamp_encrypted", encryptedTimeStamp );
    }

    if ( !errors.isEmpty() )
    {
      saveErrors( request, errors );
      return mapping.findForward( ActionConstants.FAIL_FORWARD );
    }

    return mapping.findForward( "test" );
  }

  private boolean isDetailLoggingOn()
  {
    boolean detailLoggingOn = getSystemVariableService().getPropertyByName( SystemVariableService.SSO_DETAILED_LOGGING_ON ).getBooleanVal();
    return detailLoggingOn;
  }

  private String getRemoteAddress( HttpServletRequest httpServletRequest )
  {
    String remoteAddress = httpServletRequest.getHeader( "X-FORWARDED-FOR" );
    if ( remoteAddress == null )
    {
      remoteAddress = httpServletRequest.getRemoteAddr();
    }
    if ( remoteAddress.indexOf( "," ) >= 0 )
    {
      remoteAddress = remoteAddress.substring( 0, remoteAddress.indexOf( "," ) );
    }
    logger.debug( "Remote address:" + remoteAddress + ", Request url:" + httpServletRequest.getRequestURL() );
    return remoteAddress;
  }

  private ParticipantService getParticipantService()
  {
    return (ParticipantService)getService( ParticipantService.BEAN_NAME );
  }

  private UserService getUserService()
  {
    return (UserService)getService( UserService.BEAN_NAME );
  }

  private SystemVariableService getSystemVariableService()
  {
    return (SystemVariableService)getService( SystemVariableService.BEAN_NAME );
  }

  private AwardBanQService getAwardBanQService()
  {
    AwardBanQServiceFactory factory = (AwardBanQServiceFactory)BeanLocator.getBean( AwardBanQServiceFactory.BEAN_NAME );
    return factory.getAwardBanQService();
  }

  protected boolean isTermedUserAndInActive( Participant pax )
  {
    if ( Objects.nonNull( pax.getTerminationDate() ) && Objects.nonNull( pax.getStatus() ) && Objects.nonNull( pax.getStatus().getCode() )
        && pax.getStatus().getCode().equals( ParticipantStatus.INACTIVE ) )
    {
      return true;
    }
    return false;
  }

  protected boolean isUserHasPoints( Participant pax )
  {
    // Get awardBanQ Balance for the Pax
    Long balance = getAwardBanQService().getAccountBalanceForParticipantId( pax.getId() );

    if ( Objects.nonNull( balance ) && balance.longValue() > 0 )
    {
      return true;
    }

    return false;
  }

}
