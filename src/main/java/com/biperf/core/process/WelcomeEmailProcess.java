/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/WelcomeEmailProcess.java,v $
 */

package com.biperf.core.process;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.AudienceType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ParticipantStatus;
import com.biperf.core.domain.enums.SsoLoginEnum;
import com.biperf.core.domain.hierarchy.Hierarchy;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.message.Message;
import com.biperf.core.domain.participant.Audience;
import com.biperf.core.domain.participant.AudienceParticipant;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.StrongMailUser;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserEmailAddress;
import com.biperf.core.domain.welcomemail.WelcomeMessage;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.email.WelcomeEmailService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.AudienceToParticipantAssociationRequest;
import com.biperf.core.service.participant.AudienceToParticipantsAssociationRequest;
import com.biperf.core.service.participant.PasswordResetService;
import com.biperf.core.service.participant.UserAssociationRequest;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.strongmail.StrongMailService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.strategy.usertoken.UserTokenType;
import com.biperf.core.utils.crypto.SHA256Hash;
import com.biperf.core.value.FormattedValueBean;
import com.biperf.core.value.UserValueBean;

/**
 * WelcomeEmailProcess. this is the main process to handle sending of Welcome Emails to Pax who have
 * not already received one. This is the process an Admin would schedule to run. A system variable
 * controls whether the pax should get the Welcome message in one email (with both Login ID and
 * password), OR separate emails,(Login ID and Password in 2 emails.)
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
 * <td>Ramesh Kunasekaran</td>
 * <td>Jan 5, 2006</td>
 * <td>1.0</td>
 * <td>modified</td>
 * </tr>
 * </table>
 * 
 *
 */
public class WelcomeEmailProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( WelcomeEmailProcess.class );

  public static final String BEAN_NAME = "welcomeEmailProcess";

  public static final String LOGIN_MESSAGE_NAME = "Welcome Login";

  public static final String BOTH_MESSAGE_NAME = "Welcome Login and Password";

  public static final String MESSAGE_NAME = "Welcome Email Process";

  protected WelcomeEmailService welcomeEmailService;

  protected PasswordResetService passwordResetService;

  protected ProcessService processService;

  protected StrongMailService strongMailService;

  /**
   * Return code returned from the stored procedure
   */
  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  /**
   * Stored proc returns this code when the stored procedure executed without errors
   */
  public static final String BAD_OUTPUT = "99";

  public WelcomeEmailProcess()
  {
    super();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  @Override
  public void onExecute()
  {

    // Set it here for password validation - This sets it in thread local that MD5Hash uses
    /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */
    // MD5Hash.setDefaultUpperCase( !getSystemVariableService().getPropertyByName(
    // SystemVariableService.PASSWORD_SHOULD_USE_REGEX ).getBooleanVal() );
    SHA256Hash.setDefaultUpperCase( !getSystemVariableService().getPropertyByName( SystemVariableService.PASSWORD_SHOULD_USE_REGEX ).getBooleanVal() );
    /* START MD5 to SHA256 conversion code: TO BE UPDATED LATER */

    // creates mailing process if not created already
    createOrLoadMailingProcess();

    if ( !isBounceBackVerified() )
    {
      notifyBounceBackVerifyError();
    }
    else
    {
      List<WelcomeMessage> welccomeMessageList = welcomeMessageService.getAllMessageByNotificationDate();

      if ( welccomeMessageList != null && welccomeMessageList.size() > 0 )
      {
        for ( Iterator<WelcomeMessage> welccomeMessageIter = welccomeMessageList.iterator(); welccomeMessageIter.hasNext(); )
        {
          List<Participant> allUsers = new ArrayList<Participant>();
          List<Participant> successUsers = new ArrayList<Participant>();
          List<String> failedUserNames = new ArrayList<String>();
          List<String> secondaryFailedUserNames = null;
          List<Participant> usersWithoutEmail = new ArrayList<Participant>();
          WelcomeMessage welcomeMessage = welccomeMessageIter.next();
          Message message = messageService.getMessageById( welcomeMessage.getMessageId() );
          Boolean hasSecondary = welcomeMessage.getSecondaryMessageId() != null;
          Message secondaryMessage = null;

          findUsersForWelcomeEmail( welcomeMessage, allUsers, successUsers, failedUserNames, usersWithoutEmail );

          if ( hasSecondary )
          {
            secondaryMessage = messageService.getMessageById( welcomeMessage.getSecondaryMessageId() );
            secondaryFailedUserNames = new ArrayList<String>( failedUserNames );
          }

          for ( Iterator<Participant> iter = successUsers.iterator(); iter.hasNext(); )
          {
            Participant pax = iter.next();
            boolean uniquePrimaryEmail = userService.isUniqueEmail( pax.getPrimaryEmailAddress().getEmailAddr() );
            if ( hasSecondary )
            {
              sendSegmentedWelcomeEmail( pax, message, failedUserNames, secondaryMessage, secondaryFailedUserNames, uniquePrimaryEmail );
            }
            else
            {
              sendNewWelcomeEmail( pax, message, failedUserNames, buildToken( pax.getId() ), uniquePrimaryEmail );
            }
          }

          int successCount = allUsers.size() - ( failedUserNames.size() + usersWithoutEmail.size() );
          int secondarySuccessCount = 0;
          if ( hasSecondary )
          {
            secondarySuccessCount = allUsers.size() - ( secondaryFailedUserNames.size() + usersWithoutEmail.size() );
          }

          // Send Summary Email to Run By User

          if ( hasSecondary )
          {
            sendSummaryMessage( successCount, failedUserNames, secondarySuccessCount, secondaryFailedUserNames, message.getName(), secondaryMessage.getName() );
          }
          else
          {
            sendSummaryMessage( successCount, failedUserNames, message.getName() );
          }
        }
      }
      else if ( systemVariableService.getPropertyByName( SystemVariableService.EMAIL_USE_STRONGMAIL ).getBooleanVal() )
      {
        try
        {
          // Call the stored procedure procedure to generate random passwords and insert into a
          // temporary table strongmail_user
          Map<String, Object> procedureOutMap = strongMailService.executeWelcomeEmailProcedure();

          if ( BAD_OUTPUT.equals( procedureOutMap.get( OUTPUT_RETURN_CODE ) ) )
          {
            throw new Exception( "Stored procedure returned error. Procedure returned: " + procedureOutMap.get( OUTPUT_RETURN_CODE ) );
          }
          List<StrongMailUser> strongMailUsers = userService.getAllStrongMailUsers();
          StrongMailUser smu = null;
          String wesSiteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/login.do";
          for ( StrongMailUser strongMailUser : strongMailUsers )
          {
            smu = strongMailUser;
            Participant pax = participantService.getParticipantByUserName( smu.getUserName() );
            AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
            associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
            pax = participantService.getParticipantByIdWithAssociations( pax.getId(), associationRequestCollection );
            boolean uniquePrimaryEmail = userService.isUniqueEmail( pax.getPrimaryEmailAddress().getEmailAddr() );

            if ( SsoLoginEnum.SSO.toString().equalsIgnoreCase( systemVariableService.getPropertyByName( SystemVariableService.SSO_LOGIN_TYPE ).getStringVal() ) && !isTermedUserAndInActive( pax ) )
            {
              String siteUrl = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SSO_LOGIN_URL ).getStringVal();
              smu.setWebsiteUrl( siteUrl );
              smu.setUserTokenUrl( siteUrl );
            }
            else
            {
              smu.setWebsiteUrl( wesSiteUrl );

              if ( uniquePrimaryEmail )
              {
                smu.setUserTokenUrl( wesSiteUrl + "?userToken=" + buildToken( pax.getId() ) + "&activation=true" );
              }
              else
              {
                smu.setUserTokenUrl( wesSiteUrl );

              }

            }
            userService.updateStrongMailUserByUserName( smu );
          }
          Message message = messageService.getMessageByCMAssetCode( MessageService.WELCOME_EMAIL_MESSAGE_CM_ASSET_CODE );
          strongMailService.sendWelcomeEmail( message, "Login And Password" );
        }
        catch( Exception e )
        {
          log.error( "Exception in sending the batch email using strong mail", e );
        }
      }
      else
      {
        sendMailDefaultAudience();
      }
    }
  }

  protected void createOrLoadMailingProcess()
  {
    processService.createOrLoadSystemProcess( MailingProcess.PROCESS_NAME, MailingProcess.BEAN_NAME );
  }

  protected void sendNewWelcomeEmail( Participant pax, Message message, List<String> failedUserNames, String passwordToken, boolean uniquePrimaryEmail )
  {
    try
    {
      if ( !uniquePrimaryEmail )
      {
        passwordToken = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/login.do";
      }
      welcomeEmailService.sendNewWelcomeEmail( pax, message, passwordToken, getRunByUser(), uniquePrimaryEmail );
      welcomeEmailService.updateUser( pax.getId() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Welcome Message " + message.getName() + "to Pax: " + pax.getUserName() + ".  " + "(process invocation ID = " + getProcessInvocationId() + ")",
                 e );
      addComment( "An exception occurred while sending Welcome Message " + message.getName() + "to Pax: " + pax.getUserName() + ".  " + "See the log file for additional information.  "
          + "(process invocation ID = " + getProcessInvocationId() + ")" );
      failedUserNames.add( pax.getUserName() );
    }
  }

  protected void sendSegmentedWelcomeEmail( Participant pax,
                                            Message message,
                                            List<String> failedUserNames,
                                            Message secondaryMessage,
                                            List<String> secondaryFailedUserNames,
                                            boolean uniquePrimaryEmail )
  {
    String passwordToken = null;
    boolean unique = false;
    if ( uniquePrimaryEmail )
    {
      passwordToken = buildToken( pax.getId() );
      unique = true;
    }
    else
    {
      passwordToken = systemVariableService.getPropertyByNameAndEnvironment( SystemVariableService.SITE_URL_PREFIX ).getStringVal() + "/login.do";
    }
    sendNewWelcomeEmail( pax, message, failedUserNames, passwordToken, unique );
    sendNewWelcomeEmail( pax, secondaryMessage, secondaryFailedUserNames, passwordToken, unique );
  }

  protected void sendWelcomeEmail( UserValueBean userValueBean, List failedUserNames, boolean notice, File file )
  {
    try
    {
      String passwordToken = null;
      if ( !userValueBean.getDuplicateEmail() )
      {
        passwordToken = buildToken( userValueBean.getId() );
      }

      // Send one Welcome Email to paxs with both the Login ID and token in one mailing
      welcomeEmailService.sendWelcomeEmail( userValueBean, passwordToken, getRunByUser(), notice, file );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Welcome Email to Pax: " + userValueBean.getUserName() + ".  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending Welcome Email to Pax: " + userValueBean.getUserName() + ".  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
      failedUserNames.add( userValueBean.getUserName() );
    }
  }

  protected boolean isBounceBackVerified()
  {
    return systemVariableService.getPropertyByName( SystemVariableService.BOUNCEBACK_EMAIL_VERIFIED ).getBooleanVal();
  }

  protected void notifyBounceBackVerifyError()
  {
    String errorMessage = "Bounce Back Email Verification is not complete. Cannot send Welcome Emails.";
    addComment( errorMessage );
    List failedReason = new ArrayList();
    failedReason.add( errorMessage );
    sendSummaryMessage( 0, failedReason, "BOUNCE BACK VERIFICATION" );
  }

  protected void findUsersForWelcomeEmail( WelcomeMessage welcomeMessage,
                                           List<Participant> allUsers,
                                           List<Participant> successUsers,
                                           List<String> failedUserNames,
                                           List<Participant> usersWithoutEmail )
  {

    StringBuilder audienceId = new StringBuilder();
    Set criteriaAudienceSet = new HashSet();
    Set paxAudienceSet = new HashSet();
    Audience audience;
    Map<String, Object> userIds;
    Set users;
    for ( Iterator iter = welcomeMessage.getAudienceSet().iterator(); iter.hasNext(); )
    {
      Audience criteriaAudience = audienceService.getAudienceById( (Long)iter.next(), null );
      if ( criteriaAudience.getAudienceType().getCode().equals( AudienceType.SEARCH_CRITERIA_TYPE ) )
      {
        criteriaAudienceSet.add( criteriaAudience.getId() );
      }
      else
      {
        paxAudienceSet.add( criteriaAudience.getId() );
      }
    }
    if ( !paxAudienceSet.isEmpty() )
    {
      users = getMailingRecipients( paxAudienceSet );
      Participant pax = null;
      for ( Iterator iter = users.iterator(); iter.hasNext(); )
      {
        Object temp = iter.next();
        try
        {
          pax = (Participant)temp;
        }
        catch( ClassCastException cce )
        {
          pax = ( (AudienceParticipant)temp ).getParticipant();
        }
        allUsers.add( pax );
        UserEmailAddress userEmailAddress = userService.getPrimaryUserEmailAddress( pax.getId() );
        if ( userEmailAddress != null && userEmailAddress.getEmailAddr() != null )
        {

          if ( !pax.isWelcomeEmailSent().booleanValue() && pax.getLastResetDate() == null )
          {
            successUsers.add( pax );
          }
          else
          {
            failedUserNames.add( pax.getUserName() );
          }
        }
        else
        {
          usersWithoutEmail.add( pax );
        }
      }
    }
    if ( !criteriaAudienceSet.isEmpty() )
    {
      Iterator<String> iterator = criteriaAudienceSet.iterator();
      while ( iterator.hasNext() )
      {
        // Get the next element.
        audienceId.append( String.valueOf( iterator.next() ) );
        // If there is another element add a comma.
        if ( iterator.hasNext() )
        {
          audienceId.append( ", " );
        }
      }
      Map<String, Object> inputParameters = new HashMap<String, Object>();
      inputParameters.put( "audienceId", audienceId.toString() );
      inputParameters.put( "resendFlag", 0 );

      userIds = audienceService.getCriteriaAudienceActiveandInactive( inputParameters );
      BigDecimal returnCode = (BigDecimal)userIds.get( "p_out_return_code" );

      if ( returnCode.intValue() == 0 )
      {
        Participant pax = null;
        ArrayList userList = new ArrayList();

        userList.add( userIds.get( "p_out_refcursor" ) );

        String userIdStr = userList.get( 0 ).toString();
        String user = userIdStr.replace( "[", "" );
        user = user.replace( "]", "" );
        // user = user.trim();
        String[] userArray = user.split( "," );
        for ( int i = 0; i < userArray.length; i++ )
        {
          // Get the next element.
          // audienceId.append(String.valueOf( userIdIterator.next()));
          // If there is another element add a comma.
          if ( !userArray[i].trim().equals( "" ) )
          {
            AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
            associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
            pax = participantService.getParticipantByIdWithAssociations( Long.parseLong( userArray[i].trim() ), associationRequestCollection );
          }
          if ( pax != null )
          {
            allUsers.add( pax );
            UserEmailAddress userEmailAddress = userService.getPrimaryUserEmailAddress( pax.getId() );
            if ( userEmailAddress != null && userEmailAddress.getEmailAddr() != null )
            {

              if ( !pax.isWelcomeEmailSent().booleanValue() && pax.getLastResetDate() == null )
              {
                successUsers.add( pax );
              }
              else
              {
                failedUserNames.add( pax.getUserName() );
              }
            }
            else
            {
              usersWithoutEmail.add( pax );
            }
          }
        }
      }
    }
  }

  /**
   * 
   * @param audienceList
   * @return Set
   */
  protected Set getMailingRecipients( Set audienceList )
  {

    AssociationRequestCollection reqCollection = new AssociationRequestCollection();
    reqCollection.add( new AudienceToParticipantAssociationRequest() );
    reqCollection.add( new AudienceToParticipantsAssociationRequest() );
    // List audiences = sendMessageForm.getAudienceList();

    Set paxs = new HashSet();
    for ( Iterator iter = audienceList.iterator(); iter.hasNext(); )
    {
      Long audienceId = new Long( iter.next().toString() );
      Audience audience = getAudienceService().getAudienceById( audienceId, reqCollection );

      Hierarchy primaryHierarchy = getHierarchyService().getPrimaryHierarchy();
      Long primaryHierarchyId = primaryHierarchy.getId();

      Set audienceSet = new LinkedHashSet();
      audienceSet.add( audience );
      List userIdList = getListBuilderService().searchParticipants( audienceSet, primaryHierarchyId, false, null, true );

      ArrayList participants = new ArrayList();
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new UserAssociationRequest( UserAssociationRequest.EMAIL ) );
      for ( Iterator userIdListIterator = userIdList.iterator(); userIdListIterator.hasNext(); )
      {
        FormattedValueBean fvb = (FormattedValueBean)userIdListIterator.next();
        participants.add( getParticipantService().getParticipantByIdWithAssociations( fvb.getId(), associationRequestCollection ) );
      }

      paxs.addAll( participants );

    }

    return paxs;
  }

  protected void sendMailDefaultAudience()
  {
    List<UserValueBean> allUsers = new ArrayList<UserValueBean>();
    List<UserValueBean> successUsers = new ArrayList<UserValueBean>();
    List<String> failedUserNames = new ArrayList<String>();
    List<UserValueBean> usersWithoutEmail = new ArrayList<UserValueBean>();

    findUsersFromDefaultAudience( allUsers, successUsers, failedUserNames, usersWithoutEmail );

    if ( allUsers.isEmpty() )
    {
      addComment( "No users found for a welcome email." );
      sendSummaryMessage( 0, new ArrayList(), BOTH_MESSAGE_NAME ); // Admin wants an email even
                                                                   // though no paxs got none
    }
    else
    {
      for ( Iterator iter = successUsers.iterator(); iter.hasNext(); )
      {
        UserValueBean userValueBean = (UserValueBean)iter.next();
        sendWelcomeEmail( userValueBean, failedUserNames, false, null );
      }

      // Number of users that *should* get a welcome email
      int successCount = allUsers.size() - ( failedUserNames.size() + usersWithoutEmail.size() );
      // Send Summary Email to Run By User
      sendSummaryMessage( successCount, failedUserNames, BOTH_MESSAGE_NAME );
    }
  }

  protected void findUsersFromDefaultAudience( List allUsers, List successUsers, List failedUserNames, List usersWithoutEmail )
  {
    List userValueBeans = null;

    userValueBeans = userService.getAllUsersForWelcomeMail();

    if ( userValueBeans != null )
    {
      for ( Iterator iter = userValueBeans.iterator(); iter.hasNext(); )
      {
        UserValueBean userValueBean = (UserValueBean)iter.next();
        allUsers.add( userValueBean );
        if ( userValueBean.getEmailAddress() != null )
        {
          successUsers.add( userValueBean );
        }
        else
        {
          usersWithoutEmail.add( userValueBean );
        }
      }
    }
  }

  /**
   * Composes and sends the summary e-mail to the "run by" user.
   * 
   * @param failedUserNames
   */
  protected Mailing sendSummaryMessage( int successCount, List failedUserNames, String messageName )
  {
    return sendSummaryMessage( successCount, failedUserNames, 0, null, messageName, null );
  }

  protected Mailing sendSummaryMessage( int successCount,
                                        List<String> failedUserNames,
                                        int secondarySuccessCount,
                                        List<String> secondaryFailedUserNames,
                                        String messageName,
                                        String secondaryMessageName )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map<String, Object> objectMap = new HashMap<String, Object>();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );
    objectMap.put( "successCount", new Integer( successCount ) );
    objectMap.put( "failedUserNames", StringUtils.join( failedUserNames.iterator(), ", " ) );
    objectMap.put( "messageName", messageName );
    objectMap.put( "hasSecondaryMessage", secondaryFailedUserNames != null );
    if ( secondaryFailedUserNames != null )
    {
      objectMap.put( "secondaryMessageName", secondaryMessageName );
      objectMap.put( "secondarySuccessCount", new Integer( successCount ) );
      objectMap.put( "failedSecondaryUserNames", StringUtils.join( secondaryFailedUserNames.iterator(), ", " ) );
    }

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.WELCOME_EMAIL_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROMOTION );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailing = mailingService.submitMailing( mailing, objectMap, getRunByUser().getId() );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( successCount + " recipients successfully received " + messageName.toUpperCase() + " email successfully." );
      addComment( successCount + " recipients successfully received " + messageName.toUpperCase() + " email successfully." );
      if ( secondaryFailedUserNames != null )
      {
        log.debug( secondarySuccessCount + " recipients successfully received " + secondaryMessageName.toUpperCase() + " email successfully." );
        addComment( secondarySuccessCount + " recipients successfully received " + secondaryMessageName.toUpperCase() + " email successfully." );
      }
      log.debug( "--------------------------------------------------------------------------------" );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Welcome Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending Welcome Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }

    return mailing;
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

  /**
   * 
   * @return Password
   */
  protected String buildToken( Long paxID )
  {
    return getPasswordResetService().generateTokenAndSave( paxID, UserTokenType.WELCOME_EMAIL ).getUnencryptedTokenValue();
  }

  public void setWelcomeEmailService( WelcomeEmailService welcomeEmailService )
  {
    this.welcomeEmailService = welcomeEmailService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public void setStrongMailService( StrongMailService strongMailService )
  {
    this.strongMailService = strongMailService;
  }

  public PasswordResetService getPasswordResetService()
  {
    return passwordResetService;
  }

  public void setPasswordResetService( PasswordResetService passwordResetService )
  {
    this.passwordResetService = passwordResetService;
  }

}
