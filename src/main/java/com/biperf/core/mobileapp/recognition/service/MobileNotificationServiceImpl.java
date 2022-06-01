
package com.biperf.core.mobileapp.recognition.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.biperf.core.dao.claim.ClaimDAO;
import com.biperf.core.dao.participant.UserDAO;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.claim.ClaimRecipient;
import com.biperf.core.domain.claim.RecognitionClaim;
import com.biperf.core.domain.country.Country;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.RecognitionPromotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserNode;
import com.biperf.core.google.android.gcm.server.Message;
import com.biperf.core.google.android.gcm.server.Message.Priority;
import com.biperf.core.google.android.gcm.server.Notification;
import com.biperf.core.google.android.gcm.server.Result;
import com.biperf.core.mobileapp.recognition.dao.UserDeviceDAO;
import com.biperf.core.mobileapp.recognition.domain.DeviceType;
import com.biperf.core.mobileapp.recognition.domain.UserDevice;
import com.biperf.core.mobileapp.recognition.service.RecognitionNotification.ParameterKeys;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.service.cms.CMAssetService;
import com.biperf.core.service.hierarchy.NodeService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.value.recognitionadvisor.RecognitionAdvisorValueBean;
import com.objectpartners.cms.util.CmsUtil;

public class MobileNotificationServiceImpl implements MobileNotificationService
{
  private UserDeviceDAO userDeviceDao;
  private UserDAO userDao;
  private ClaimDAO claimDao;

  private SystemVariableService systemVariableService;
  private GoogleCloudMessagingService gcmService;
  private CMAssetService cmAssetService;
  private NodeService nodeService;
  private UserDeviceService userDeviceService;

  private static final Log log = LogFactory.getLog( MobileNotificationServiceImpl.class );

  @Override
  public UserDevice createUserDeviceFor( Long userId, DeviceType deviceType, String deviceId, boolean debug )
  {
    User user = userDao.getUserById( userId );
    UserDevice userDevice = new UserDevice( user, deviceType, deviceId, debug );

    userDevice = userDeviceDao.create( userDevice );

    // be sure to reset the notificationCount
    userDevice.resetNotificationCount();

    return userDevice;
  }

  @Override
  public int deleteUserDevice( Long userId, String deviceId )
  {
    return userDeviceDao.delete( userId, deviceId );
  }

  @Override
  public int deleteUserDevices( Long userId )
  {
    return userDeviceDao.deleteAllDevicesFor( userId );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public void onRecognitionClaimSent( Long recognitionClaimId )
  {
    // get the recognition claim
    Claim claim = claimDao.getClaimById( recognitionClaimId );

    if ( claim != null && claim instanceof RecognitionClaim )
    {
      RecognitionClaim recognition = (RecognitionClaim)claim;

      // get the recipients...
      Set<ClaimRecipient> recipients = recognition.getClaimRecipients();
      if ( recipients != null )
      {
        // send a notification to each recipient
        for ( ClaimRecipient cr : recipients )
        {
          Participant recipient = cr.getRecipient();

          // notify the recipient....
          sendRecognitionNotificationToRecipient( recipient, recognition );

          // notify the recipient's managers...
          Set<User> managers = nodeService.getNodeManagersForUser( recipient, recipient.getPrimaryUserNode().getNode() );

          for ( User manager : managers )
          {
            sendRecogntionNotificationToManager( manager, recipient, recognition );
          }

        }
      }
    }
  }

  @Override
  public void budgetEndNotification( List<BudgetEndNotification> budgetEndNotifications )
  {
    for ( BudgetEndNotification budgetEndNotification : budgetEndNotifications )
    {
      // Get filtered devices for the user
      Set<UserDevice> devices = filterDuplicates( userDeviceDao.findUserDevicesFor( budgetEndNotification.getUserId() ) );
      try
      {
        // Device count tracing
        log.info( "Found " + ( devices == null ? 0 : devices.size() ) + " devices while sending Budget End Notification to recipient for User : " + budgetEndNotification.getUserId() );

        if ( devices.size() > 0 )
        {
          String message = getBudgetEndNotificationMessageText( budgetEndNotification.getLocaleCode(),
                                                                budgetEndNotification.getPromotionName(),
                                                                budgetEndNotification.getBudgetRemaining(),
                                                                budgetEndNotification.getAwardMedia(),
                                                                budgetEndNotification.getEndDate() );
          for ( UserDevice device : devices )
          {
            sendBudgetEndNotificationToRecipient( message, device );
          }
        }
      }
      catch( Exception ex )
      {
        log.error( "Exception while sending Budget End Notification to " + ( devices == null ? 0 : devices.size() ) + " devices for User : " + budgetEndNotification.getUserId() );
        ex.printStackTrace();
      }
    }
  }

  @Override
  public void inactivityRecognitionNotification( List<RecognitionInactivityNotification> recognitionInactivityNotifications )
  {
    // Get all of the devices we're going to send notifications to, in one database hop
    List<Long> notificationUserIds = recognitionInactivityNotifications.stream().map( ( notif ) -> notif.getUserId() ).collect( Collectors.toList() );
    Map<Long, Set<UserDevice>> userDeviceMap = userDeviceService.getUniqueUserDevicesForAll( notificationUserIds );

    for ( RecognitionInactivityNotification recognitionInactivityNotification : recognitionInactivityNotifications )
    {
      Set<UserDevice> devices = userDeviceMap.get( recognitionInactivityNotification.getUserId() );
      try
      {
        // Device count tracing
        log.info( "Found " + ( devices == null ? 0 : devices.size() ) + " devices while sending Inactivity Recognition Notification for User : " + recognitionInactivityNotification.getUserId() );

        if ( devices != null && devices.size() > 0 )
        {
          String message = getRecognitionInactivityNotificationMessageText( recognitionInactivityNotification.getLocaleCode(),
                                                                            recognitionInactivityNotification.getPromotionName(),
                                                                            recognitionInactivityNotification.getDeadline() );

          for ( UserDevice device : devices )
          {
            sendInactivityRecognitionNotificationToRecipient( message, device );
          }
        }
      }
      catch( Exception ex )
      {
        log.error( "Exception while sending Inactivity Recognition Notification to " + ( devices == null ? 0 : devices.size() ) + " devices for User : "
            + recognitionInactivityNotification.getUserId() );
        ex.printStackTrace();
      }
    }
  }

  @Override
  public void purlContributorInviteNotification( String localeCode, String firstName, String lastName, Long purlRecipientId, String milestone, Long contributorId, boolean reminder )
  {
    // Get filtered devices for the user
    Set<UserDevice> devices = filterDuplicates( userDeviceDao.findUserDevicesFor( contributorId ) );
    try
    {
      // Device count tracing
      log.info( "Found " + ( devices == null ? 0 : devices.size() ) + " devices while sending Purl Contribution Invite Notification for User : " + contributorId );

      if ( devices.size() > 0 )
      {
        String message = getPurlContributorInviteMessage( localeCode, firstName, lastName, reminder );

        for ( UserDevice device : devices )
        {
          sendPurlContributorInviteNotification( message, device, firstName, lastName, purlRecipientId, milestone );
        }
      }
    }
    catch( Exception ex )
    {
      log.error( "Exception while sending Purl Contributor Invite Notification to " + ( devices == null ? 0 : devices.size() ) + " devices for User : " + contributorId );
      ex.printStackTrace();
    }
  }

  @Override
  public void raTeamOverDueNotification( Long managerId, RecognitionAdvisorValueBean teamMember )
  {
    // Get filtered devices for the user
    Set<UserDevice> devices = filterDuplicates( userDeviceDao.findUserDevicesFor( managerId ) );
    try
    {
      // Device count tracing
      log.info( "Found " + ( devices == null ? 0 : devices.size() ) + " devices to send RA Team Overdue Notification for User : " + managerId );

      if ( null != devices && devices.size() > 0 )
      {
        String message = getRATeamOverdueMessage( managerId, teamMember );

        devices.stream().forEach( device ->
        {
          sendRAPushNotification( message, device, teamMember );
        } );

      }
    }
    catch( Exception ex )
    {
      log.error( "Exception while sending RA Team Overdue Notification to " + ( devices == null ? 0 : devices.size() ) + " devices for User : " + managerId );
      ex.printStackTrace();
    }
  }

  @Override
  public void raNewHireNotification( Long managerId, RecognitionAdvisorValueBean teamMember )
  {
    // Get filtered devices for the user
    Set<UserDevice> devices = filterDuplicates( userDeviceDao.findUserDevicesFor( managerId ) );
    try
    {
      // Device count tracing
      log.info( "Found " + ( devices == null ? 0 : devices.size() ) + " devices to send RA New Hire Notification for User : " + managerId );

      if ( null != devices && devices.size() > 0 )
      {
        String message = getRANewHireMessage( managerId, teamMember );

        devices.stream().forEach( device ->
        {
          sendRAPushNotification( message, device, teamMember );
        } );
      }
    }
    catch( Exception ex )
    {
      log.error( "Exception while sending RA New Hire Notification to " + ( devices == null ? 0 : devices.size() ) + " devices for User : " + managerId );
      ex.printStackTrace();
    }
  }

  protected void sendRAPushNotification( String text, UserDevice device, RecognitionAdvisorValueBean singlePax )
  {
    try
    {
      Message.Builder messageBuilder = new Message.Builder();
      messageBuilder.addData( ParameterKeys.MESSAGE.toString(), text );
      if ( !Objects.isNull( singlePax ) )
      {
        Country country = userDao.getPrimaryUserAddressCountry( singlePax.getId() );
        UserNode receipientPrimaryNode = userDao.getUserById( singlePax.getId() ).getPrimaryUserNode();
        Long receipientPrimaryNodeId = null != receipientPrimaryNode ? receipientPrimaryNode.getNode().getId() : null;
        messageBuilder.addData( ParameterKeys.LANDING_SCREEN.toString(), LandingScreen.SEND_RECOGNITION.toString() );
        messageBuilder.addData( ParameterKeys.USER_ID.toString(), String.valueOf( singlePax.getId() ) );
        messageBuilder.addData( ParameterKeys.FIRST_NAME.toString(), String.valueOf( singlePax.getFirstName() ) );
        messageBuilder.addData( ParameterKeys.LAST_NAME.toString(), String.valueOf( singlePax.getLastName() ) );
        messageBuilder.addData( ParameterKeys.AVATAR_URL.toString(), singlePax.getAvatarUrl() );
        messageBuilder.addData( ParameterKeys.NODE_ID.toString(), null != receipientPrimaryNodeId ? String.valueOf( receipientPrimaryNodeId ) : "" );
        if ( Objects.nonNull( country ) )
        {
          messageBuilder.addData( ParameterKeys.COUNTRY_NAME.toString(), country.getI18nCountryName() );
          messageBuilder.addData( ParameterKeys.COUNTRY_CODE.toString(), country.getCountryCode() );
        }
      }
      else
      {
        messageBuilder.addData( ParameterKeys.LANDING_SCREEN.toString(), LandingScreen.RA_SCREEN.toString() );
      }

      if ( device.getDeviceType() == DeviceType.IOS )
      {
        addiOSComponents( messageBuilder, text );
      }

      Message message = messageBuilder.build();
      Result result = sendToGoogleCloudMessaging( message, device );
      handleGoogleCloudNotification( text, device, result );
    }
    catch( Throwable t )
    {
      // Bummer. Message not created. Log the message
      logGoogleCloudMessagingUnexpectedException( device, "sendRAPushNotification", t );
    }
  }

  private String getRANewHireMessage( Long managerId, RecognitionAdvisorValueBean teamMember )
  {
    User user = userDao.getUserById( managerId );

    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if ( user.getLanguageType() != null )
    {
      localeCode = user.getLanguageType().getCode();
    }
    // get the MessageFormat format from Content Manager
    String format = "";
    String message = "";
    MessageFormat formatter = null;
    if ( !Objects.isNull( teamMember ) )
    {
      format = cmAssetService.getString( "recognition.content.model.info", "RA_PUSH_NOTIFICATION_ONE_NEW_HIRE", CmsUtil.getLocale( localeCode ), true );
      formatter = new MessageFormat( format );
      message = formatter.format( new Object[] { teamMember.getFirstName() } );
    }
    else
    {
      format = cmAssetService.getString( "recognition.content.model.info", "RA_PUSH_NOTIFICATION_MULTIPLE_NEW_HIRE", CmsUtil.getLocale( localeCode ), true );
      message = format;
    }

    return message;
  }

  private String getRATeamOverdueMessage( Long managerId, RecognitionAdvisorValueBean teamMember )
  {
    User user = userDao.getUserById( managerId );

    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if ( user.getLanguageType() != null )
    {
      localeCode = user.getLanguageType().getCode();
    }
    // get the MessageFormat format from Content Manager
    String format = "";
    String message = "";
    MessageFormat formatter = null;
    if ( !Objects.isNull( teamMember ) )
    {
      format = cmAssetService.getString( "recognition.content.model.info", "RA_PUSH_NOTIFICATION_ONE_OVER_DUE", CmsUtil.getLocale( localeCode ), true );
      formatter = new MessageFormat( format );
      message = formatter.format( new Object[] { teamMember.getFirstName(), teamMember.getNoOfDaysPastDueDate(), teamMember.getFirstName() } );
    }
    else
    {
      format = cmAssetService.getString( "recognition.content.model.info", "RA_PUSH_NOTIFICATION_MULTIPLE_OVER_DUE", CmsUtil.getLocale( localeCode ), true );
      formatter = new MessageFormat( format );
      Integer days = systemVariableService.getPropertyByName( SystemVariableService.RA_NUMBER_OF_DAYS_EMPLOYEE_REMINDER ).getIntVal();
      message = formatter.format( new Object[] { days } );
    }

    return message;
  }

  protected void sendBudgetEndNotificationToRecipient( String text, UserDevice device )
  {
    try
    {
      Message.Builder messageBuilder = new Message.Builder();
      messageBuilder.addData( ParameterKeys.MESSAGE.toString(), text );

      if ( device.getDeviceType() == DeviceType.IOS )
      {
        addiOSComponents( messageBuilder, text );
      }

      Message message = messageBuilder.build();

      log.info( "About to send Budget End Notification to Recipient ..." );
      log.info( "Message size trying to send: " + text.getBytes( "UTF-8" ).length + " bytes" );

      Result result = sendToGoogleCloudMessaging( message, device );
      handleGoogleCloudNotification( text, device, result );
    }
    catch( Throwable t )
    {
      // Bummer. Message not created. Log the message
      logGoogleCloudMessagingUnexpectedException( device, "sendBudgetEndNotificationToRecipient", t );
    }
  }

  protected void sendInactivityRecognitionNotificationToRecipient( String text, UserDevice device )
  {
    try
    {
      Message.Builder messageBuilder = new Message.Builder();
      messageBuilder.addData( ParameterKeys.MESSAGE.toString(), text );

      if ( device.getDeviceType() == DeviceType.IOS )
      {
        addiOSComponents( messageBuilder, text );
      }

      Message message = messageBuilder.build();
      log.info( "About to send Inactivity Recognition Notification ..." );
      log.info( "Message size trying to send: " + text.getBytes( "UTF-8" ).length + " bytes" );

      Result result = sendToGoogleCloudMessaging( message, device );
      handleGoogleCloudNotification( text, device, result );
    }
    catch( Throwable t )
    {
      // Bummer. Message not created. Log the message
      logGoogleCloudMessagingUnexpectedException( device, "sendInactivityRecognitionNotificationToRecipient", t );
    }
  }

  protected void sendRecognitionNotificationToRecipient( Participant recipient, RecognitionClaim recognition )
  {
    RecipientRecognitionNotification notification = new RecipientRecognitionNotification( recognition.getId(),
                                                                                          LandingScreen.RECOGNITION_DETAIL,
                                                                                          true,
                                                                                          getRecognitionPromotionFrom( recognition ).isAllowPublicRecognitionPoints(),
                                                                                          getRecognitionRecipientNotificationMessageText( recipient, recognition ) );

    // get the recipient's devices
    Set<UserDevice> devices = filterDuplicates( userDeviceDao.findUserDevicesFor( recipient.getId() ) );

    // temporary tracing
    log.info( "in sendRecognitionNotificationToRecipient, device count for user ID " + recipient.getId() + ": " + ( devices == null ? 0 : devices.size() ) );

    for ( UserDevice device : devices )
    {
      sendRecognitionNotificationToRecipient( recipient, notification, device );
    }
  }

  protected String getRecognitionRecipientNotificationMessageText( Participant recipient, RecognitionClaim recognition )
  {
    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    // get the MessageFormat format from Content Manager
    String format = cmAssetService.getString( "recognition.detail", "MOBILE_NOTIFICATION_RECIPIENT", CmsUtil.getLocale( localeCode ), true );

    MessageFormat formatter = new MessageFormat( format );
    String message = formatter.format( new String[] { recipient.getFirstName(), recipient.getLastName() } );

    return message;
  }

  protected void sendRecogntionNotificationToManager( User manager, Participant recipient, RecognitionClaim recognition )
  {
    boolean mine = manager.getId().equals( recipient.getId() );

    ManagerRecognitionNotification notification = new ManagerRecognitionNotification( recognition.getId(),
                                                                                      LandingScreen.RECOGNITION_DETAIL,
                                                                                      mine,
                                                                                      getRecognitionPromotionFrom( recognition ).isAllowPublicRecognitionPoints(),
                                                                                      getRecognitionManagerNotificationMessageText( manager, recipient, recognition ) );

    // get the manager's devices
    Set<UserDevice> devices = filterDuplicates( userDeviceDao.findUserDevicesFor( manager.getId() ) );
    for ( UserDevice device : devices )
    {
      sendRecognitionNotificationToManager( manager, recipient, notification, device );
    }
  }

  protected String getRecognitionManagerNotificationMessageText( User manager, Participant recipient, RecognitionClaim recognition )
  {
    String localeCode = systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();
    if ( recipient.getLanguageType() != null )
    {
      localeCode = recipient.getLanguageType().getCode();
    }
    // get the MessageFormat format from Content Manager
    String format = cmAssetService.getString( "recognition.detail", "MOBILE_NOTIFICATION_MANAGER", CmsUtil.getLocale( localeCode ), true );

    MessageFormat formatter = new MessageFormat( format );
    String message = formatter.format( new String[] { recipient.getFirstName(), recipient.getLastName() } );

    return message;
  }

  protected String getBudgetEndNotificationMessageText( String languageType, String promotionName, String budgetRemaining, String awardMedia, String endDate )
  {
    String localeCode = !StringUtils.isEmpty( languageType ) ? languageType : systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();

    // get the MessageFormat format from Content Manager
    String format = cmAssetService.getString( "recognition.detail", "MOBILE_NOTIFICATION_BUDGET_END", CmsUtil.getLocale( localeCode ), true );

    MessageFormat formatter = new MessageFormat( format );
    String message = formatter.format( new String[] { promotionName, budgetRemaining, awardMedia, endDate } );

    return message;
  }

  protected String getRecognitionInactivityNotificationMessageText( String languageType, String promotionName, String deadline )
  {
    String localeCode = !StringUtils.isEmpty( languageType ) ? languageType : systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();

    // get the MessageFormat format from Content Manager
    String format = cmAssetService.getString( "recognition.detail", "MOBILE_NOTIFICATION_REC_INACTIVITY", CmsUtil.getLocale( localeCode ), true );

    MessageFormat formatter = new MessageFormat( format );
    String message = formatter.format( new String[] { promotionName, deadline } );

    return message;
  }

  protected String getPurlContributorInviteMessage( String languageType, String firstName, String lastName, boolean reminder )
  {
    String localeCode = !StringUtils.isEmpty( languageType ) ? languageType : systemVariableService.getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();

    // get the MessageFormat format from Content Manager
    String format = "";
    if ( reminder )
    {
      format = cmAssetService.getString( "purl.recipient", "CONTRIBUTOR_PN_REMINDER", CmsUtil.getLocale( localeCode ), true );
    }
    else
    {
      format = cmAssetService.getString( "purl.recipient", "CONTRIBUTOR_PN", CmsUtil.getLocale( localeCode ), true );
    }

    MessageFormat formatter = new MessageFormat( format );
    String message = formatter.format( new String[] { firstName, lastName } );

    return message;
  }

  protected Set<UserDevice> filterDuplicates( List<UserDevice> devices )
  {
    Set<UserDevice> filtered = new HashSet<>();

    filtered.addAll( devices );

    return filtered;
  }

  protected void handleGoogleCloudNotification( String message, UserDevice device, Result result )
  {
    // temporary logging to figure out if this thing is working!
    StringBuilder s = new StringBuilder( "*\n" );
    s.append( "\n\n****************************************************" );
    s.append( "\nHandling GoogleCloudMessaging Result" );
    s.append( "\n****************************************************" );
    s.append( "\nGCM Message ID:" ).append( result.getMessageId() );
    s.append( "\nDEVICE TYPE: " ).append( device.getDeviceType().name() );
    s.append( "\nNotification Message: " ).append( message );
    s.append( "\nUser ID: " ).append( device.getUser().getId() );
    s.append( "\nUser Device ID: " ).append( device.getId() );
    s.append( "\nDevice ID: " ).append( device.getRegistrationId() );
    s.append( "\nError Code: " ).append( result.getErrorCodeName() );
    s.append( "\n****************************************************" );
    s.append( "\nEND MESSAGE" );
    s.append( "\n****************************************************\n\n" );
    log.info( s );

    if ( result.getMessageId() != null )
    {
      // the message was created! yay!

      // check to see if the device reg ID needs to be updated...
      if ( result.getCanonicalRegistrationId() != null )
      {
        device.setRegistrationId( result.getCanonicalRegistrationId() );
      }
    }
    else
    {
      // bummer. message not created. log it.
      StringBuilder sb = new StringBuilder( "*\n" );
      sb.append( "\n\n****************************************************" );
      sb.append( "\nERROR sending message through GoogleCloudMessaging" );
      sb.append( "\n****************************************************" );
      sb.append( "\nUser ID: " ).append( device.getUser().getId() );
      sb.append( "\nDevice Type: " ).append( device.getDeviceType().name() );
      sb.append( "\nUser Device ID: " ).append( device.getId() );
      sb.append( "\nDevice ID: " ).append( device.getRegistrationId() );
      sb.append( "\nError Code: " ).append( result.getErrorCodeName() );
      sb.append( "\n****************************************************" );
      sb.append( "\nEND ERROR" );
      sb.append( "\n****************************************************\n\n" );
      log.error( sb );
    }
  }

  protected void handleGoogleCloudMessagingResult( RecognitionNotification notification, UserDevice device, Result result )
  {
    // temporary logging to figure out if this thing is working!
    StringBuilder s = new StringBuilder( "*\n" );
    s.append( "\n\n****************************************************" );
    s.append( "\nHandling GoogleCloudMessaging Result" );
    s.append( "\n****************************************************" );
    s.append( "\nGCM Message ID:" ).append( result.getMessageId() );
    s.append( "\nRecognition ID: " ).append( notification.getRecognitionId() );
    s.append( "\nLanding Screen: " ).append( notification.getLandingScreen() );
    s.append( "\nMine: " ).append( notification.isMine() );
    s.append( "\nAllow add points: " ).append( notification.isAllowAddPoints() );
    s.append( "\nMessage: " ).append( notification.getMessage() );
    s.append( "\nUser ID: " ).append( device.getUser().getId() );
    s.append( "\nUser Device ID: " ).append( device.getId() );
    s.append( "\nDevice ID: " ).append( device.getRegistrationId() );
    s.append( "\nDevice Type: " ).append( device.getDeviceType().name() );
    s.append( "\nError Code: " ).append( result.getErrorCodeName() );
    s.append( "\n****************************************************" );
    s.append( "\nEND MESSAGE" );
    s.append( "\n****************************************************\n\n" );
    log.info( s );

    if ( result.getMessageId() != null )
    {
      // the message was created! yay!

      // check to see if the device reg ID needs to be updated...
      if ( result.getCanonicalRegistrationId() != null )
      {
        device.setRegistrationId( result.getCanonicalRegistrationId() );
      }
    }
    else
    {
      // bummer. message not created. log it.
      StringBuilder sb = new StringBuilder( "*\n" );
      sb.append( "\n\n****************************************************" );
      sb.append( "\nERROR sending message through GoogleCloudMessaging" );
      sb.append( "\n****************************************************" );
      sb.append( "\nUser ID: " ).append( device.getUser().getId() );
      sb.append( "\nUser Device ID: " ).append( device.getId() );
      sb.append( "\nDevice Type: " ).append( device.getDeviceType().name() );
      sb.append( "\nDevice ID: " ).append( device.getRegistrationId() );
      sb.append( "\nError Code: " ).append( result.getErrorCodeName() );
      sb.append( "\n****************************************************" );
      sb.append( "\nEND ERROR" );
      sb.append( "\n****************************************************\n\n" );
      log.error( sb );
    }
  }

  protected Result sendToGoogleCloudMessaging( Message message, UserDevice device ) throws IOException
  {
    Result sendNoRetry = gcmService.sendNoRetry( message, device );
    return sendNoRetry;
  }

  protected void sendRecognitionNotificationToRecipient( Participant recipient, RecipientRecognitionNotification notification, UserDevice device )
  {
    Message.Builder messageBuilder = new Message.Builder();
    for ( Map.Entry<ParameterKeys, String> values : notification.toValues().entrySet() )
    {
      messageBuilder.addData( values.getKey().toString(), values.getValue() );
    }

    if ( device.getDeviceType() == DeviceType.IOS )
    {
      addiOSComponents( messageBuilder, notification.getMessage() );
    }

    Message message = messageBuilder.build();

    try
    {
      Result result = sendToGoogleCloudMessaging( message, device );
      handleGoogleCloudMessagingResult( notification, device, result );
    }
    catch( Throwable t )
    {
      // bummer. message not created. log it.
      logGoogleCloudMessagingUnexpectedException( device, "sendRecognitionNotificationToRecipient", t );
    }
  }

  protected void sendRecognitionNotificationToManager( User manager, Participant recipient, ManagerRecognitionNotification notification, UserDevice device )
  {
    Message.Builder messageBuilder = new Message.Builder();
    for ( Map.Entry<ParameterKeys, String> values : notification.toValues().entrySet() )
    {
      messageBuilder.addData( values.getKey().toString(), values.getValue() );
    }

    if ( device.getDeviceType() == DeviceType.IOS )
    {
      addiOSComponents( messageBuilder, notification.getMessage() );
    }

    Message message = messageBuilder.build();

    try
    {
      Result result = sendToGoogleCloudMessaging( message, device );
      handleGoogleCloudMessagingResult( notification, device, result );
    }
    catch( Throwable t )
    {
      // bummer. message not created. log it.
      logGoogleCloudMessagingUnexpectedException( device, "sendRecognitionNotificationToManager", t );
    }
  }

  protected void sendPurlContributorInviteNotification( String text, UserDevice device, String firstName, String lastName, Long purlRecipientId, String milestone )
  {

    Message.Builder messageBuilder = new Message.Builder();
    messageBuilder.addData( ParameterKeys.MESSAGE.toString(), text );
    messageBuilder.addData( ParameterKeys.LANDING_SCREEN.toString(), LandingScreen.PURL_CELEBRATION.toString() );
    messageBuilder.addData( ParameterKeys.FIRST_NAME.toString(), firstName );
    messageBuilder.addData( ParameterKeys.LAST_NAME.toString(), lastName );
    messageBuilder.addData( ParameterKeys.PURL_RECIPIENT_ID.toString(), purlRecipientId.toString() );
    messageBuilder.addData( ParameterKeys.MILESTONE.toString(), milestone );

    if ( device.getDeviceType() == DeviceType.IOS )
    {
      addiOSComponents( messageBuilder, text );
    }

    Message message = messageBuilder.build();

    log.info( "in sendPurlContributorInviteNotification about to send message..." );

    try
    {
      Result result = sendToGoogleCloudMessaging( message, device );
      handleGoogleCloudNotification( text, device, result );
    }
    catch( Throwable t )
    {
      // bummer. message not created. log it.
      logGoogleCloudMessagingUnexpectedException( device, "sendRecognitionNotificationToRecipient", t );
    }
  }

  protected void logGoogleCloudMessagingUnexpectedException( UserDevice device, String methodName, Throwable ex )
  {
    StringBuilder sb = new StringBuilder( "*\n" );
    sb.append( "\n\n****************************************************" );
    sb.append( "\nERROR sending message through GoogleCloudMessaging" );
    sb.append( "\n****************************************************" );
    sb.append( "\nDEVICE TYPE " ).append( device.getDeviceType().name() );
    sb.append( "\nUser ID: " ).append( device.getUser().getId() );
    sb.append( "\nUser Device ID: " ).append( device.getId() );
    sb.append( "\nDevice ID: " ).append( device.getRegistrationId() );
    sb.append( "\nMethod name: " ).append( methodName );
    sb.append( "\nError Text: " ).append( ex.getMessage() );
    sb.append( "\n****************************************************" );
    sb.append( "\nEND ERROR" );
    sb.append( "\n****************************************************\n\n" );

    log.error( sb.toString() );

    StringWriter sw = new StringWriter();
    ex.printStackTrace( new PrintWriter( sw ) );
    log.error( "GOOGLE IOS PUSH ERROR " + sw.toString() );
  }

  /*
   * In order for Apple to display the default notification alert on devices the below iOS
   * components need to be added to the Firebase (GCM) Message.
   */
  private void addiOSComponents( Message.Builder messageBuilder, String messageText )
  {
    Notification.Builder notificationBuilder = new Notification.Builder( "" );
    notificationBuilder.badge( 0 );
    notificationBuilder.body( messageText );
    messageBuilder.notification( notificationBuilder.build() );
    messageBuilder.priority( Priority.HIGH );
    messageBuilder.contentAvailable( true );
  }

  private RecognitionPromotion getRecognitionPromotionFrom( RecognitionClaim claim )
  {
    Promotion promotion = BaseAssociationRequest.initializeAndUnproxy( claim.getPromotion() );
    return (RecognitionPromotion)promotion;
  }

  public void setUserDao( UserDAO userDao )
  {
    this.userDao = userDao;
  }

  public void setUserDeviceDao( UserDeviceDAO userDeviceDao )
  {
    this.userDeviceDao = userDeviceDao;
  }

  public void setClaimDao( ClaimDAO claimDao )
  {
    this.claimDao = claimDao;
  }

  public void setGcmService( GoogleCloudMessagingService gcmService )
  {
    this.gcmService = gcmService;
  }

  public void setCmAssetService( CMAssetService cmAssetService )
  {
    this.cmAssetService = cmAssetService;
  }

  public void setSystemVariableService( SystemVariableService systemVariableService )
  {
    this.systemVariableService = systemVariableService;
  }

  public void setNodeService( NodeService nodeService )
  {
    this.nodeService = nodeService;
  }

  public void setUserDeviceService( UserDeviceService userDeviceService )
  {
    this.userDeviceService = userDeviceService;
  }

}
