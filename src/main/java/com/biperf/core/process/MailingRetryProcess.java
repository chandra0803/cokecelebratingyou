
package com.biperf.core.process;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.utils.DateUtils;

/**
 * 
 * MailingRetryProcess.
 * 
 * PURPOSE: To push thru mailings that were created but not sent. 
 *          mailing_recipient.date_sent IS NULL. Bug 67162
 * 
 * @author bethke
 * @since Jun 22, 2016
 */
public class MailingRetryProcess extends BaseProcessImpl
{
  private static final Log logger = LogFactory.getLog( MailingRetryProcess.class );

  public static final String PROCESS_NAME = "Mailing Retry Process";
  public static final String BEAN_NAME = "mailingRetryProcess";

  private MailingService mailingService;

  // properties set from jobDataMap
  String startDate;
  String endDate;

  // private variables

  public void onExecute()
  {
    try
    {
      Date startDateAsDate = DateUtils.toDate( startDate );
      Date endDateAsDate = DateUtils.toDate( endDate );
      int totalEmailsPushedThru = mailingService.reScheduleEmailFailures( startDateAsDate, endDateAsDate, getRunByUser().getId() );

      String processComment = "Start Date = " + startDate + " End Date =" + endDate + " Total Emails Pushed Thru = " + totalEmailsPushedThru;
      addComment( processComment );
      sendSummaryMessage( totalEmailsPushedThru );
    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while processing  (process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred. See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  private void sendSummaryMessage( int totalEmailsPushedThru )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "startDate", startDate );
    objectMap.put( "endDate", endDate );
    objectMap.put( "totalEmailsPushedThru", new Integer( totalEmailsPushedThru ) );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.MAILING_RETRY_PROCESS_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      logger.debug( "--------------------------------------------------------------------------------" );
      logger.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      logger.debug( "Run By User: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      logger.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while sending " + PROCESS_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + PROCESS_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public String getStartDate()
  {
    return startDate;
  }

  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

  public String getEndDate()
  {
    return endDate;
  }

  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

}
