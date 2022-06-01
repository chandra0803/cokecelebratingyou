/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/PostProcessRetryProcess.java,v $
 */

package com.biperf.core.process;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.promotion.PostProcessJobs;
import com.biperf.core.domain.system.PropertySetItem;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.email.EmailHeader;
import com.biperf.core.service.email.EmailService;
import com.biperf.core.service.email.TextEmailBody;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PostProcessJobsService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * 
 * PostProcessRetryProcess.
 * 
 */
public class PostProcessRetryProcess extends BaseProcessImpl
{
  public static final String CLASS_NAME = "PostProcessRetryProcess";

  public static final String BEAN_NAME = "postProcessRetryProcess";
  public static final String MESSAGE_NAME = "Post Process Retry Process";

  /**
   * Return code returned from the stored procedure
   */
  public static final String OUTPUT_RETURN_CODE = "p_out_return_code";

  /**
   * Stored proc returns this code when the stored procedure executed without errors
   */
  public static final String BAD_OUTPUT = "99";

  private PostProcessJobsService postProcessJobsService;
  private EmailService emailService;

  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( PostProcessRetryProcess.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    int totalRescheduled = 0;
    int totalScheduledFail = 0;

    try
    {
      // Call the stored procedure procedure to clean up post process job activities and qrtz
      Map<String, Object> procedureOutMap = postProcessJobsService.runPostProcessJobsCleanUp();

      if ( BAD_OUTPUT.equals( procedureOutMap.get( OUTPUT_RETURN_CODE ) ) )
      {
        throw new Exception( "Stored procedure returned error. Procedure returned: " + procedureOutMap.get( OUTPUT_RETURN_CODE ) );
      }
    }
    catch( Exception e )
    {
      log.error( CLASS_NAME + " .onExecute - Unable to clean up post process.", e );
    }

    PropertySetItem retryAttempts = systemVariableService.getPropertyByName( SystemVariableService.POST_PROCESS_RETRY_ATTEMPTS );

    List<Long> postProcessRetryIds = postProcessJobsService.getPostProcessJobsIdsForRetryProcess( retryAttempts.getIntVal() );

    if ( postProcessRetryIds != null && !postProcessRetryIds.isEmpty() )
    {
      log.debug( MESSAGE_NAME + " : " + postProcessRetryIds.size() + " post process ids to be processed." );

      for ( Long postProcessId : postProcessRetryIds )
      {
        try
        {
          PostProcessJobs postProcessJobs = postProcessJobsService.getPostProcessJobsById( postProcessId );
          log.debug( "PostProcessRetryProcess .onExecute - postProcessId " + postProcessId );

          if ( postProcessJobs != null && postProcessJobs.getProcessBeanName() != null && postProcessJobs.getProcessBeanName().equals( JournalEntryOnlinePostProcess.BEAN_NAME ) )
          {
            // Schedule Journal Entry Post Process
            postProcessJobsService.scheduleRetryPostProcess( postProcessJobs, JournalEntryOnlinePostProcess.BEAN_NAME );
            totalRescheduled++;
          }
          else if ( postProcessJobs != null && postProcessJobs.getProcessBeanName() != null && postProcessJobs.getProcessBeanName().equals( ClaimOnlinePostProcess.BEAN_NAME ) )
          {
            // Schedule claim post process
            postProcessJobsService.scheduleRetryPostProcess( postProcessJobs, ClaimOnlinePostProcess.BEAN_NAME );
            totalRescheduled++;
          }
          else if ( postProcessJobs != null )
          {
            // Update retry counter and save to database
            log.debug( BEAN_NAME + " ." + MESSAGE_NAME + " - Unable to reschedule post process for postProcessId = " + postProcessId );
            addComment( "Invalid process bean name: " + postProcessJobs.getProcessBeanName() );

            postProcessJobs.setRetryCount( postProcessJobs.getRetryCount() + 1 );
            postProcessJobs.setRetryCountChangeDate( new Date() );
            postProcessJobsService.savePostProcessJobs( postProcessJobs );
          }
        }
        catch( Exception e )
        {
          if ( e.getMessage() != null && e.getMessage().trim().length() > 100 )
          {
            addComment( "The process did not complete successfully. Post Process Id = " + postProcessId + ", Exception name = " + e.getMessage().trim().substring( 0, 100 ) );
          }
          else if ( e.getMessage() != null )
          {
            addComment( "The process did not complete successfully. Post Process Id = " + postProcessId + ", Exception name = " + e.getMessage().trim() );
          }
          logErrorMessage( e );
        }
      }

    }

    // find number of postProcessRetry that's unable to get reschedule
    if ( postProcessRetryIds != null && postProcessRetryIds.size() > 0 )
    {
      totalScheduledFail = postProcessRetryIds.size() - totalRescheduled;
    }

    // Notify the administrator.
    sendSummaryMessage( retryAttempts, totalRescheduled, totalScheduledFail, postProcessRetryIds );
  }

  private void sendSummaryMessage( PropertySetItem retryAttempts, int totalRescheduled, int totalScheduledFail, List<Long> postProcessRetryIds )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.clear();
    objectMap.put( "postProcessRetryIds", postProcessRetryIds );
    objectMap.put( "totalRescheduled", totalRescheduled );
    objectMap.put( "totalScheduledFail", totalScheduledFail );
    objectMap.put( "maxAttemptsReachedCount", postProcessJobsService.getMaxAttemptsReachedCount( retryAttempts.getIntVal() ) );
    objectMap.put( "stuckJournalCount", postProcessJobsService.getStuckJournals() );
    objectMap.put( "delayedCount", postProcessJobsService.getScheduledJobsDelayedJobs() );
    objectMap.put( "qrtzSchedulerStateCount", postProcessJobsService.getQrtzSchedulerStateCount() );
    objectMap.put( "unsentMailingsCount", postProcessJobsService.getUnsentMailingsCount() );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.POST_PROCESS_RETRY_SUMMARY_MESSAGE, MailingType.PROCESS_EMAIL );

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

      addComment( "processName: " + BEAN_NAME + " has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending Post Process Retry Mailing Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending Post Process Retry Mailing Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
    addComment( "Processes rescheduled: " + objectMap.get( "totalRescheduled" ) + ", Processes not rescheduled: " + objectMap.get( "totalScheduledFail" ) + ", Processes at max attempts: "
        + objectMap.get( "maxAttemptsReachedCount" ) + ", Stuck journals from yesterday: " + objectMap.get( "stuckJournalCount" ) + ", Delayed recognitions not processed yesterday: "
        + objectMap.get( "delayedCount" ) + ", Records in qrtz_scheduler_state table: " + objectMap.get( "qrtzSchedulerStateCount" ) + ", Mailings not sent yesterday: "
        + objectMap.get( "unsentMailingsCount" ) );
  }

  protected void sendSummaryMessage( String comment )
  {
    User runByUser = getRunByUser();

    String subject = MESSAGE_NAME;
    EmailHeader header = new EmailHeader();

    PropertySetItem sysProperty = systemVariableService.getPropertyByName( SystemVariableService.SYSTEM_EMAIL_ADDRESS );
    header.setSender( sysProperty.getStringVal() );

    String runByUserEmailAddr = userService.getPrimaryUserEmailAddress( runByUser.getId() ).getEmailAddr();
    header.setRecipients( new String[] { runByUserEmailAddr } );

    subject = "[ " + systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() + " ] " + subject;
    header.setSubject( subject );

    String name = "";
    StringBuffer bodyHeader = null;

    bodyHeader = new StringBuffer();
    bodyHeader.append( comment );
    bodyHeader.append( "\n" );

    name = bodyHeader.toString();

    TextEmailBody body = new TextEmailBody( name );
    try
    {
      emailService.sendMessage( header, body );
    }
    catch( ServiceErrorException e )
    {
      throw new BeaconRuntimeException( "Error sending message", e );
    }
  }

  public void setPostProcessJobsService( PostProcessJobsService postProcessJobsService )
  {
    this.postProcessJobsService = postProcessJobsService;
  }

  public void setEmailService( EmailService emailService )
  {
    this.emailService = emailService;
  }

}
