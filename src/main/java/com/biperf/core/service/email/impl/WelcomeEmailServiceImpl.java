/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/email/impl/WelcomeEmailServiceImpl.java,v $
 */

package com.biperf.core.service.email.impl;

import java.io.File;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.log4j.Logger;

import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.SsoLoginEnum;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingMessageLocale;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.mailing.MailingRecipientData;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.user.UserToken;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.email.WelcomeEmailService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.participant.UserService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.usertoken.UserTokenType;
import com.biperf.core.utils.GuidUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.UserValueBean;
import com.biperf.core.utils.DateUtils;

/**
 * .
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
 * <td>wadzinsk</td>
 * <td>Jun 13, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class WelcomeEmailServiceImpl implements WelcomeEmailService
{
  /**
   * Logger for this class
   */
  private static final Logger log = Logger.getLogger( WelcomeEmailServiceImpl.class );

  private MailingService mailingService;
  private UserDAO userDAO;
  private MessageService messageService;
  private SystemVariableService systemVariableService;
  private PasswordResetService passwordResetService;
  private UserService userService;
  private ParticipantService participantService;

  public ParticipantService getParticipantService()
  {
    return participantService;
  }

  public void setParticipantService( ParticipantService participantService )
  {
    this.participantService = participantService;
  }

  /**
   * Compose and send the Welcome email with both the LoginID and Password
   * 
   * Note: Actual email send occurs after the TX that this method is part of is complete. See
   * QuartzScheduleJobTransactionSynchronization.
   *    
   * @param userValueBean - user to send email
   * @param password - site password to include in email
   * @param runByUserId - the (admin) person who sends the email
   * @param falsePasswordChange - should the pax change password upon login
   * 
   */
  @Override
  public void sendWelcomeEmail( UserValueBean userValueBean, String passwordToken, User runByUser, boolean notice, File file )
  {
    Mailing mailing = composeMail( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE, MailingType.WELCOME_LOGIN_EMAIL );
    MailingRecipient mailingRecipient = null;
    MailingRecipient receiver = createMailingRecipient( userValueBean );
    if ( !notice )
    {
      mailingRecipient = receiver;
    }
    else
    {
      if ( file != null )
      {
        String absPath = file.getAbsolutePath();
        String fileName = file.getName();
        mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, absPath, fileName ) );
      }
      mailingRecipient = createMailingRecipient( runByUser );
    }
    addMailingRecipientData( mailingRecipient, "firstName", receiver.getUser().getFirstName() );
    addMailingRecipientData( mailingRecipient, "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    addMailingRecipientData( mailingRecipient, "userName", userValueBean.getUserName() );
    String loginUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    // Fixed for the pure SSO client.

    Participant pax = participantService.getParticipantById( userValueBean.getId() );
    // only add the token if the email address is unique
    String url = null;

    if ( SsoLoginEnum.SSO.toString().equalsIgnoreCase( systemVariableService.getPropertyByName( SystemVariableService.SSO_LOGIN_TYPE ).getStringVal() ) && !isTermedUserAndInActive( pax ) )
    {
      url = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SSO_LOGIN_URL ).getStringVal();
      addMailingRecipientData( mailingRecipient, "loginUrl", url );
      addMailingRecipientData( mailingRecipient, "programUrl", url );
    }
    else
    {
      addMailingRecipientData( mailingRecipient, "loginUrl", loginUrl );
      if ( passwordToken != null )
      {
        addMailingRecipientData( mailingRecipient, "programUrl", mailingService.buildUserTokenURL( passwordToken ) + "&activation=true" );
      }
      else
      {
        addMailingRecipientData( mailingRecipient,
                                 "programUrl",
                                 systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/login.do?activationPage=true" );
      }

      UserEmailAddress email = mailingRecipient.getUser().getPrimaryEmailAddress();

      if ( !Objects.isNull( passwordToken ) && !Objects.isNull( email ) && userDAO.isUniqueEmail( email.getEmailAddr() ) )
      {
        url = mailingService.buildUserTokenURL( passwordToken ) + "&activation=true";
      }
      else
      {
        url = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();
      }

    }

    addMailingRecipientData( mailingRecipient, "userTokenUrl", url );
    mailing.addMailingRecipient( mailingRecipient );

    mailing = mailingService.submitMailing( mailing, null, runByUser.getId() );
    if ( !notice )
    {
      updateUser( userValueBean.getId() );
    }
  }

  @Override
  public void sendNewWelcomeEmail( User pax, Message message, String passwordToken, User runByUser, boolean isUniqueEmail )
  {
    Mailing mailing = composeMail( message.getCmAssetCode(), MailingType.WELCOME_LOGIN_EMAIL );
    Set<MailingRecipient> mailingRecipients = new HashSet<MailingRecipient>();
    MailingRecipient mailingRecipient = createMailingRecipient( pax );
    Map<String, Object> dataMap = buildDataMap( pax, passwordToken, isUniqueEmail );
    mailingRecipient.addMailingRecipientDataFromMap( dataMap );
    mailingRecipients.add( mailingRecipient );
    mailing.addMailingRecipients( mailingRecipients );
    mailing = mailingService.submitMailing( mailing, null, runByUser.getId() );
  }

  /**
   * Update the user's password, welcome sent flag, and change password flag.
   */

  public void updateUser( Long userId )
  {
    User user = userDAO.getUserById( userId );
    user.setWelcomeEmailSent( Boolean.TRUE );
    user.setOneTimePassword( true );
    user.setOneTimePasswordDate( new Date() );
    userDAO.saveUser( user );
  }

  protected Map<String, Object> buildDataMap( User user, String passwordToken, boolean isUniqueEmail )
  {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put( "firstName", user.getFirstName() );
    dataMap.put( "userToken", passwordToken );
    dataMap.put( "programName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    dataMap.put( "userName", user.getUserName() );
    String loginUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal();

    // Fixed for the pure SSO client.
    Participant pax = participantService.getParticipantById( user.getId() );
    if ( SsoLoginEnum.SSO.toString().equalsIgnoreCase( systemVariableService.getPropertyByName( SystemVariableService.SSO_LOGIN_TYPE ).getStringVal() ) && !isTermedUserAndInActive( pax ) )
    {
      String url = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SSO_LOGIN_URL ).getStringVal();
      dataMap.put( "loginUrl", url );
      dataMap.put( "programUrl", url );
    }
    else
    {
      dataMap.put( "loginUrl", loginUrl );
      if ( isUniqueEmail )
      {
        dataMap.put( "programUrl", mailingService.buildUserTokenURL( passwordToken ) + "&activation=true" );
      }
      else
      {
        dataMap.put( "programUrl", loginUrl + "/login.do?activation=true" );
      }

    }
    return dataMap;
  }

  public UserService getUserService()
  {
    return userService;
  }

  private void addMailingRecipientData( MailingRecipient mailingRecipient, String key, String value )
  {
    MailingRecipientData mrd = new MailingRecipientData();
    mrd.setKey( key );
    mrd.setValue( value );
    mailingRecipient.addMailingRecipientData( mrd );
  }

  private MailingRecipient createMailingRecipient( UserValueBean userValueBean )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );

    User user = userDAO.getUserById( userValueBean.getId() );

    if ( user.getLanguageType() != null )
    {
      mailingRecipient.setLocale( user.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    mailingRecipient.setUser( user );

    return mailingRecipient;
  }

  private MailingRecipient createMailingRecipient( User user )
  {
    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );

    if ( user.getLanguageType() != null )
    {
      mailingRecipient.setLocale( user.getLanguageType().getCode() );
    }
    else
    {
      mailingRecipient.setLocale( systemVariableService.getDefaultLanguage().getStringVal() );
    }

    mailingRecipient.setUser( user );

    return mailingRecipient;
  }

  /**
   * Adds the message by name and mailing type to a mailing object
   * 
   * @param cmAssetCode
   * @param mailingType
   * @return a mailing object that is mostly assembled, except for the mailingRecipient(s)
   */
  protected Mailing composeMail( String cmAssetCode, String mailingType )
  {
    Mailing mailing = composeMail();

    mailing.setMailingType( MailingType.lookup( mailingType ) );

    Message message = messageService.getMessageByCMAssetCode( cmAssetCode );
    mailing.setMessage( message );

    return mailing;
  }

  /**
   * Creates a new mailing object and add Guid, Sender and Delivery Date to it
   * 
   * @return a partially assembled mailing object
   */
  private Mailing composeMail()
  {
    Mailing mailing = new Mailing();

    // Needs Guid due to lack of a business key
    mailing.setGuid( GuidUtils.generateGuid() );

    // Sender
    String sender = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS ).getStringVal();
    mailing.setSender( sender );

    // Delivery Date - Assumes Now (i.e. immediate delivery)
    Timestamp deliveryDate = new Timestamp( com.biperf.core.utils.DateUtils.getCurrentDate().getTime() );
    mailing.setDeliveryDate( deliveryDate );

    return mailing;

  }

  private MailingAttachmentInfo addMailingAttachmentInfo( Mailing mailing, String fullFileName, String attachmentFileName )
  {
    MailingAttachmentInfo mailingAttachmentInfo = new MailingAttachmentInfo();
    mailingAttachmentInfo.setFullFileName( fullFileName );
    mailingAttachmentInfo.setAttachmentFileName( attachmentFileName );
    mailingAttachmentInfo.setMailing( mailing );
    return mailingAttachmentInfo;
  }

  @Override
  public void resendWelcomeEmail( List<Long> userIds )
  {
    if ( null != userIds )
    {
      for ( Long userId : userIds )
      {
        resendWelcomeEmail( userId );
      }
    }
  }

  @Override
  public void resendWelcomeEmail( Long userId )
  {
    UserValueBean userValueBean = userDAO.getUserDetailsForWelcomeMail( userId );
    User runByUser = userDAO.getUserById( UserManager.getUserId() );

    UserToken passwordToken = passwordResetService.generateTokenAndSave( userId, UserTokenType.WELCOME_EMAIL );
    // Send one Welcome Email to paxs with both the Login ID and Password in one mailing
    sendWelcomeEmail( userValueBean, passwordToken.getUnencryptedTokenValue(), runByUser, false, null );
  }

  @Override
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public void sendWelcomeEmailCountToAdmin( int count, File attachment )
  {
    User recipient = userDAO.getUserById( UserManager.getUserId() );
    Mailing mailing = composeMail( MessageService.WELCOME_EMAIL_COUNT_ADMIN_MESSAGE_CM_ASSET_CODE, MailingType.SYSTEM );

    MailingRecipient mailingRecipient = new MailingRecipient();
    mailingRecipient.setGuid( GuidUtils.generateGuid() );
    mailingRecipient.setUser( recipient );
    mailingRecipient.setLocale( MailingMessageLocale.DEFAULT_LOCALE );
    mailing.addMailingRecipient( mailingRecipient );

    if ( attachment != null )
    {
      String absPath = attachment.getAbsolutePath();
      String fileName = attachment.getName();
      mailing.addMailingAttachmentInfo( addMailingAttachmentInfo( mailing, absPath, fileName ) );
    }

    Map objectMap = new HashMap();
    objectMap.put( "count", count );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    mailing = mailingService.submitMailing( mailing, objectMap );
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

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  public void setUserDAO( UserDAO userDAO )
  {
    this.userDAO = userDAO;
  }

  public void setMessageService( MessageService messageService )
  {
    this.messageService = messageService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setPasswordResetService( PasswordResetService passwordResetService )
  {
    this.passwordResetService = passwordResetService;
  }

  public void setUserService( UserService userService )
  {
    this.userService = userService;
  }

}
