
package com.biperf.core.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.characteristic.Characteristic;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserCharacteristicService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.value.PurlContributorInviteValue;

/**
 * PurlAutoSendProcess
 * Process to auto send PURL invites to recipient's team members if the number of contributors is less than 1.
 * 
 * @author arasi
 * @since 23-Aug-2012
 * @version 1.0
 */
public class PurlAutoSendProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "purlAutoSendProcess";
  public static final String MESSAGE_NAME = "Purl Auto Send Process Notification";
  //client customization start - wip 52159
  public static final String DIVISION_KEY = "Division Key";
  //client customization end
  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( PurlAutoSendProcess.class );

  private PurlService purlService;
  private String numberOfDays; // coke customization WIP 26527
  /**
   * The ID of the promotion whose PURLs this process will auto send invitations for.
   */
  private String promotionId;
  private UserCharacteristicService userCharacteristicService;
  
  public PurlAutoSendProcess()
  {
    super();
    log.error( "process :" + toString() );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'PurlAutoSendProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'PurlAutoSendProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {
      List<PurlContributorInviteValue> allInvitesList = new ArrayList<PurlContributorInviteValue>();
      List<Long> purlRecipientList = purlService.getPurlRecipientsForAutoInvite( new Long( promotionId ), new Long( numberOfDays ) );
      log.error( "**** purlRecipientList.size=" + purlRecipientList.size() );
      int recipientCount = 0;

      for ( Long purlRecipientId : purlRecipientList )
      {
        try
        {
          PurlRecipient purlRecipient = purlService.getPurlRecipientById( purlRecipientId );

          List<PurlContributorInviteValue> inviteList = new ArrayList<PurlContributorInviteValue>();
          List<Participant> selectedContributors = new ArrayList<Participant>();
          if ( purlRecipient.isShowDefaultContributors() )
          {
            recipientCount += 1;
            selectedContributors = purlService.getPreSelectedContributors( purlRecipientId );

            for ( Participant preSelectedContributor : selectedContributors )
            {
              inviteList.add( populateInvite( preSelectedContributor ) );
            }
          }
          /* Customization for WIP 39735 starts here */
          List<Participant> nodeMemberForPurlMgrRecipientList = purlService.getNodeMemberForPurlMgrRecipient( purlRecipientId );
          if ( nodeMemberForPurlMgrRecipientList.size() > 0 && !purlRecipient.isShowDefaultContributors() )
          {
            recipientCount += 1;
          }
          for ( Participant nodeMemberForPurlMgrRecipient : nodeMemberForPurlMgrRecipientList )
          {
            inviteList.add( populateInvite( nodeMemberForPurlMgrRecipient ) );
          }          
          //client customization start - wip 52159
          List<Characteristic> characteristicList = userCharacteristicService.getAllCharacteristics();
          
          for ( Characteristic charc : characteristicList )
          {
        	  if ( charc.getCharacteristicName().equalsIgnoreCase( DIVISION_KEY ) )
        	  {
        		 String recipientDivisionValue = userCharacteristicService.getCharacteristicValueByUserAndCharacterisiticId( purlRecipient.getUser().getId(), charc.getId());
        		 
        		 String purlBUInvites = systemVariableService.getPropertyByName(SystemVariableService.COKE_PURL_BU_INVITE).getStringVal();
        		 String[] purlBUInvite = purlBUInvites.split(",");
        		 for ( String buInvite : purlBUInvite )
        		 {
        			 if ( recipientDivisionValue != null && buInvite != null && buInvite.trim().equalsIgnoreCase( recipientDivisionValue.trim() ) )
        			 {
        				List<Participant> divisionMembersList  = purlService.getUsersByCharacteristicIdAndValue( charc.getId(), recipientDivisionValue );
        		        for ( Participant divisionMember : divisionMembersList )
        		        {
        		          inviteList.add( populateInvite( divisionMember ) );
        		        }
        			 }
        		 }
        	  }    	  
          }
          //client customization end - wip 52159
          for ( PurlContributorInviteValue invite : inviteList )
          {
            allInvitesList.add( invite );
          }
          inviteList = purlService.sendContributorInvitationByManager( purlRecipient.getId(), inviteList, false );
          log.error( "**** Finished processing purlRecipientId " + purlRecipientId );
        }
        catch( Exception e )
        {
          addComment( "**** Exception occurred while processing purlRecipientId " + purlRecipientId + ". See application log for stack trace" );
          logErrorMessage( e );
        }
      }

      addComment( "processName: " + BEAN_NAME + " - Completed. " + recipientCount + " number of PURLs have been assigned contributors." );

      // Notify the administrator.
      sendSummaryMessage( allInvitesList, recipientCount );

    }
  }

  private PurlContributorInviteValue populateInvite( Participant preSelectedContributor )
  {
    PurlContributorInviteValue invite = new PurlContributorInviteValue();
    invite.setPaxId( preSelectedContributor.getId() );
    invite.setFirstName( preSelectedContributor.getFirstName() );
    invite.setLastName( preSelectedContributor.getLastName() );
    return invite;
  }

  /**
   * Composes and sends a summary e-mail to the "run by" user
   */
  private void sendSummaryMessage( List<PurlContributorInviteValue> allInvitesList, int recipientCount )
  {
    User recipientUser = getRunByUser();
    int failureCount = 0;
    int successCount = 0;
    int noEmailCount = 0;

    for ( PurlContributorInviteValue invite : allInvitesList )
    {
      if ( invite.getStatus().equals( PurlContributorInviteValue.STATUS_SUCCESS ) )
      {
        successCount++;
      }
      else if ( invite.getStatus().equals( PurlContributorInviteValue.STATUS_FAIL ) )
      {
        failureCount++;
      }
      if ( invite.getEmailAddr() == null )
      {
        noEmailCount++;
      }
    }

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "recipientCount", recipientCount );
    objectMap.put( "successCount", successCount );
    objectMap.put( "failureCount", failureCount );
    objectMap.put( "noEmailCount", noEmailCount );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.PURL_AUTO_SEND_INVITES_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " email has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public void setPurlService( PurlService purlService )
  {
    this.purlService = purlService;
  }
  public void setUserCharacteristicService( UserCharacteristicService userCharacteristicService )
  {
	  this.userCharacteristicService = userCharacteristicService;
  }

  // coke customization start
  public String getNumberOfDays()
  {
    return numberOfDays;
  }

  public void setNumberOfDays( String numberOfDays )
  {
    this.numberOfDays = numberOfDays;
  }
  // coke customization end
}
