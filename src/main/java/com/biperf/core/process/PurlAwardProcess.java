
package com.biperf.core.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.purl.PurlRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.purl.PurlService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.NewServiceAnniversaryUtil;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.PurlAwardValue;

public class PurlAwardProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( PurlAwardProcess.class );

  public static final String BEAN_NAME = "purlAwardProcess";
  public static final String MESSAGE_NAME = "Purl Award";

  private PurlService purlService;

  public PurlAwardProcess()
  {
    super();
    log.error( "process :" + toString() );
  }

  public void onExecute()
  {
    // Disabling The Process As New Service Anniversary & Celebration Module Enabled From Nackle
    // Eco-System.
    if ( NewServiceAnniversaryUtil.isNewServiceAnniversaryEnabled() )
    {
      log.info( " The Process 'PurlAwardProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'PurlAwardProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {

      List<PurlRecipient> purls = purlService.getAllPurlRecipientsForAwardIssuance();

      if ( purls.isEmpty() )
      {
        log.debug( "No purls to process awards at this time." );
        addComment( "No purls to process awards at this time." );
      }
      else
      {
        int successCount = 0;
        int failureCount = 0;
        int totalPointsDeposited = 0;
        int numberOfLevels = 0;
        int numberOfStandardEmail = 0;
        int numberOfPurlEmail = 0;
        for ( PurlRecipient purl : purls )
        {
          Participant pax = (Participant)purl.getUser();
          try
          {
            PurlAwardValue purlAwardValue = purlService.processAward( purl.getId() );
            successCount++;
            totalPointsDeposited += purlAwardValue.getTotalPointsDeposited();
            if ( purlAwardValue.isNumberOfLevels() )
            {
              numberOfLevels += 1;
            }
            if ( purlAwardValue.isStandardEmailSent() )
            {
              numberOfStandardEmail += 1;
            }
            if ( purlAwardValue.isPurlEmailSent() )
            {
              numberOfPurlEmail += 1;
            }
          }
          catch( Exception e )
          {
            failureCount++;
            logErrorMessage( e );
            // custom comment
            addComment( "An exception occurred while processing award for " + pax.getFirstName() + " " + pax.getLastName() );
          }
        }
        addComment( successCount + " successful " + MESSAGE_NAME + " processed." );

        // Send an email to the user who schedules/launches this process
        sendSummaryMessage( successCount, failureCount, totalPointsDeposited, numberOfLevels, numberOfStandardEmail, numberOfPurlEmail );
      }

    }

  }

  /**
   * Composes and sends the summary e-mail to the "run by" user.
   */
  private void sendSummaryMessage( int successCount, int failCount, int totalPointsDeposited, int numberOfLevels, int numberOfStandardEmail, int numberOfPurlEmail )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "successCount", new Integer( successCount ) );
    objectMap.put( "failCount", new Integer( failCount ) );
    objectMap.put( "numberOfPointsDeposited", new Integer( totalPointsDeposited ) );
    objectMap.put( "numberOfLevels", new Integer( numberOfLevels ) );
    objectMap.put( "numberOfStandardEmail", new Integer( numberOfStandardEmail ) );
    objectMap.put( "numberOfPurlEmail", new Integer( numberOfPurlEmail ) );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.PURL_AWARD_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    // if(mr.getUser().getUserEmailAddresses().size()>0)
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "number of recipients successfully processed " + MESSAGE_NAME + " : " + successCount );
      log.debug( "number of recipients failed processing " + MESSAGE_NAME + " : " + failCount );
      log.debug( "number of recipients successfully processed " + MESSAGE_NAME + " : " + totalPointsDeposited );
      log.debug( "number of recipients successfully processed " + MESSAGE_NAME + " : " + numberOfLevels );
      log.debug( "number of recipients successfully processed " + MESSAGE_NAME + " : " + numberOfStandardEmail );
      log.debug( "number of recipients successfully processed " + MESSAGE_NAME + " : " + numberOfPurlEmail );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( MESSAGE_NAME + " Summary email sent to user id: " + UserManager.getUserId() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while processing " + MESSAGE_NAME + "(process invocation ID = " + getProcessInvocationId() + ")", e );

      addComment( "An exception occurred while processing " + MESSAGE_NAME + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  public void setPurlService( PurlService purlService )
  {
    this.purlService = purlService;
  }

}
