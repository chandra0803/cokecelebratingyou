/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/Attic/AdminTestWelcomeEmailProcess.java,v $
 */

package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.SsoLoginEnum;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.usertoken.UserTokenType;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.utils.crypto.SHA256Hash;

public class AdminTestWelcomeEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( AdminTestWelcomeEmailProcess.class );

  public static final String PROCESS_NAME = "Admin Test Welcome Email Process";
  public static final String BEAN_NAME = "adminTestWelcomeEmailProcess";

  private PasswordResetService passwordResetService;

  private String userName;
  private String seperateEmail;
  private String recipientLocale;

  public AdminTestWelcomeEmailProcess()
  {
    super();
  }

  @Override
  public void onExecute()
  {
    /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    // Set it here for password validation - This sets it in thread local that MD5Hash uses
    // MD5Hash.setDefaultUpperCase( !getSystemVariableService().getPropertyByName(
    // SystemVariableService.PASSWORD_SHOULD_USE_REGEX ).getBooleanVal() );
    SHA256Hash.setDefaultUpperCase( !getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_SHOULD_USE_REGEX ).getBooleanVal() );
    /* END MD5 to SHA256 conversion code: TO BE UPDATED LATER */

    log.debug( "Starting Admin Test Welcome Email Process with User Name: " + userName + " and Send Seperate Email: " + seperateEmail );
    try
    {
      Participant participant = participantService.getParticipantByUserName( userName );

      if ( participant != null )
      {
        String password = passwordResetService.generateTokenAndSave( participant.getId(), UserTokenType.EMAIL ).getUnencryptedTokenValue();
        sendWelcomeEmail( password, participant, UserManager.getUserId() );
      }
      else
      {
        addComment( "User name " + userName + " not available in the system to launch Admin Test Welcome Email Process." );
      }
    }
    catch( Exception e )
    {
      logErrorMessage( e );
      addComment( "An exception occurred while processing Admin Test Welcome Email Process with Username: " + userName );
    }
  }

  public void sendWelcomeEmail( String password, Participant receiver, Long runByUserId )
  {
    // Compose the mailing
    Mailing mailing = composeMail( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE, MailingType.WELCOME_BOTH_EMAIL );

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( receiver );
    mailingRecipient.setLocale( recipientLocale );

    addMailingRecipientData( mailingRecipient, "firstName", receiver.getFirstName() );
    addMailingRecipientData( mailingRecipient, "lastName", receiver.getLastName() );
    addMailingRecipientData( mailingRecipient, "userName", receiver.getUserName() );
    addMailingRecipientData( mailingRecipient, "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    String loginUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    if ( SsoLoginEnum.SSO.toString().equalsIgnoreCase( systemVariableService.getPropertyByName( SystemVariableService.SSO_LOGIN_TYPE ).getStringVal() ) && !isTermedUserAndInActive( receiver ) )
    {

      String url = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SSO_LOGIN_URL ).getStringVal();
      addMailingRecipientData( mailingRecipient, "loginUrl", url );
      addMailingRecipientData( mailingRecipient, "programUrl", url );
    }
    else
    {
      addMailingRecipientData( mailingRecipient, "loginUrl", loginUrl );
      addMailingRecipientData( mailingRecipient, "programUrl", loginUrl );
    }

    mailing.addMailingRecipient( mailingRecipient );

    mailing.addMailingRecipient( mailingRecipient );

    mailing = mailingService.submitMailing( mailing, buildObjectMap(), runByUserId );

  }

  private void addMailingRecipientData( MailingRecipient mailingRecipient, String key, String value )
  {
    MailingRecipientData mrd = new MailingRecipientData();
    mrd.setKey( key );
    mrd.setValue( value );
    mailingRecipient.addMailingRecipientData( mrd );
  }

  private Map buildObjectMap()
  {
    String siteUrlPrefix = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
    Map objectMap = new HashMap();
    objectMap.put( "company", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "websiteUrl", siteUrlPrefix + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_URL ).getStringVal() );
    objectMap.put( "contactUsUrl", siteUrlPrefix + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_CONTACT_URL ).getStringVal() );
    objectMap.put( "notice", "" );
    return objectMap;
  }

  private boolean isTermedUserAndInActive( Participant pax )
  {
    if ( Objects.nonNull( pax.getTerminationDate() ) && Objects.nonNull( pax.getStatus() ) && Objects.nonNull( pax.getStatus().getCode() )
        && pax.getStatus().getCode().equals( ParticipantStatus.INACTIVE ) )
    {
      return true;
    }
    return false;
  }

  public String getUserName()
  {
    return userName;
  }

  public void setUserName( String userName )
  {
    this.userName = userName;
  }

  public String getSeperateEmail()
  {
    return seperateEmail;
  }

  public void setSeperateEmail( String seperateEmail )
  {
    this.seperateEmail = seperateEmail;
  }

  public String getRecipientLocale()
  {
    return recipientLocale;
  }

  public void setRecipientLocale( String recipientLocale )
  {
    this.recipientLocale = recipientLocale;
  }

  @Override
  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  public void setPasswordResetService( PasswordResetService passwordResetService )
  {
    this.passwordResetService = passwordResetService;
  }

}
