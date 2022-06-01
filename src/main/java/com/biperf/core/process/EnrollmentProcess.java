/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/EnrollmentProcess.java,v $
 */

package com.biperf.core.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UserManager;

/**
 * <p>
 * EnrollmentProcess <b>Change History:</b><br>
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
public class EnrollmentProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( EnrollmentProcess.class );

  public static final String BEAN_NAME = "enrollmentProcess";
  public static final String MESSAGE_NAME = "Bank Account Enrollment Process";

  private AwardBanQServiceFactory awardBanQServiceFactory;

  /**
   *
   */
  public EnrollmentProcess()
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
    List paxs = participantService.getAllParticipantsNotInBanqSystem();

    if ( paxs.isEmpty() )
    {
      log.debug( "No participants that weren't already enrolled in banq system." );
      addComment( "No participants to process at this time." );
    }
    else
    {
      int successCount = 0;
      int failureCount = 0;
      for ( Iterator iter = paxs.iterator(); iter.hasNext(); )
      {
        Long userId = (Long)iter.next();
        try
        {
          getAwardBanQServiceFactory().getAwardBanQService().enrollParticipantInAwardBanQ( userId );
          successCount++;
        }
        catch( ServiceErrorException e )
        {
          failureCount++;
          logErrorMessage( e );
          // custom comment
          Participant pax = participantService.getParticipantById( userId );
          addComment( "An exception occurred while enrolling participant " + pax.getFirstName() + " " + pax.getLastName() + " " + " AwardBanqNbr: " + pax.getAwardBanqNumber() + " CentraxId: "
              + pax.getCentraxId() );
        }
      }
      addComment( successCount + " successful enrollments with " + failureCount + " failures." );

      // Send an email to the user who schedules/launches this process
      sendSummaryMessage( successCount, failureCount );
    }
  }

  /**
   * Composes and sends the summary e-mail to the "run by" user.
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
    Mailing mailing = composeMail( MessageService.ENROLLMENT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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

      addComment( "Enrollment process summary email sent to user id: " + UserManager.getUserId() );
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
