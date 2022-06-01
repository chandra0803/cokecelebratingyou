/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/ParticipantUpdateProcess.java,v $
 */

package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.value.ParticipantUpdateProcessSummaryBean;

/**
 * <p>
 * ParticipantUpdateProcess <b>Change History:</b><br>
 * <table border="1">
 * <tr>
 * <th>Author</th>
 * <th>Date</th>
 * <th>Version</th>
 * <th>Comments</th>
 * </tr>
 * <tr>
 * <td>jenniget</td>
 * <td>Dec 1, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantUpdateProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( ParticipantUpdateProcess.class );

  public static final String BEAN_NAME = "participantUpdateProcess";
  public static final String MESSAGE_NAME = "Bank Account Update Process";

  private AwardBanQServiceFactory awardBanQServiceFactory;

  /**
   * 
   */
  public ParticipantUpdateProcess()
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
    Long lastIdProcessed = null;
    int batchSize = systemVariableService.getPropertyByName( SystemVariableService.AWARDBANQ_PAX_UPDATE_PROCESS_BATCH_SIZE ).getIntVal();
    ParticipantUpdateProcessSummaryBean summary = new ParticipantUpdateProcessSummaryBean();
    // bug fix:37649
    try
    {
      do
      {
        lastIdProcessed = participantService.updateParticipantInAwardbanqByBatch( lastIdProcessed, batchSize, summary );
      }
      while ( lastIdProcessed != null );
    }
    catch( Throwable e )
    {
      // skipping update process on encountering -3 award bank error.
    }
    // End of bug fix:37649
    if ( summary.getTotalCount() == 0 )
    {
      log.debug( "No participants that were already enrolled in banq system." );
      addComment( "No participants to process at this time." );
    }
    else
    {
      int errorCount = summary.getErrorCount();
      if ( errorCount > 0 )
      {
        for ( int i = 0; i < errorCount; ++i )
        {
          logErrorMessage( summary.getError( i ) );
          // custom comment
          addComment( summary.getErrorMessage( i ) );
        }
      }
      addComment( summary.getSuccessCount() + " successful participant updates with " + summary.getFailureCount() + " failures." );

      // Send an email to the user who schedules/launches this process
      sendSummaryMessage( summary.getSuccessCount(), summary.getFailureCount() );

    }
  }

  /*
   * public void onExecute() { List paxs = getPaxService().getAllParticipantsInBanqSystem(); if (
   * paxs.isEmpty() ) { log.debug( "No participants that were already enrolled in banq system." );
   * addComment( "No participants to process at this time." ); } else { int successCount = 0; int
   * failureCount = 0; for ( Iterator iter = paxs.iterator(); iter.hasNext(); ) { Participant pax =
   * (Participant)iter.next(); try { //BugFix 18062 Skip those records with out any awardBanq number
   * if( pax.getAwardBanqNumber() != null && pax.getAwardBanqNumber().trim().length() > 0 ) {
   * getAwardBanQServiceFactory().getAwardBanQService().updateParticipantInAwardBanQ( pax );
   * successCount++; } } catch( ServiceErrorException e ) { failureCount++; logErrorMessage(e);
   * //custom comment addComment( "An exception occurred while updating participant " +
   * pax.getFirstName() + " " + pax.getLastName() + " " + " AwardBanqNbr: " +
   * pax.getAwardBanqNumber() + " CentraxId: " + pax.getCentraxId() ); } } addComment( successCount
   * + " successful participant updates with " + failureCount + " failures." ); // Send an email to
   * the user who schedules/launches this process sendSummaryMessage( successCount, failureCount );
   * } } /** Composes and sends the summary e-mail to the "run by" user.
   */
  private void sendSummaryMessage( int successCount, int failCount )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "successCount", new Integer( successCount ) );
    objectMap.put( "failCount", new Integer( failCount ) );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.PARTICIPANT_UPDATE_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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
      log.debug( "number of recipients successfully received " + MESSAGE_NAME + " Summary Email: " + successCount );
      log.debug( "number of recipients failed to receive " + MESSAGE_NAME + " Summary Email: " + failCount );
      log.debug( "--------------------------------------------------------------------------------" );
      // Fix for bug# 40742
      // addComment( successCount + " users received " + MESSAGE_NAME + " Summary email
      // successfully." );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public AwardBanQServiceFactory getAwardBanQServiceFactory()
  {
    return awardBanQServiceFactory;
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }
}
