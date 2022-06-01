
package com.biperf.core.process;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingAttachmentInfo;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.email.MailingService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * MailingAttachmentCleanupProcess.
 * 
 * Deletes files on app server that have been mailed to the user.
 * 
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
 * <td>Tammy Cheng</td>
 * <td>Aug 17, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class MailingAttachmentCleanupProcess extends BaseProcessImpl
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  /**
   * The name of the Spring bean for this class.
   */
  public static final String BEAN_NAME = "mailingAttachmentCleanupProcess";
  public static final String MESSAGE_NAME = "Mailing Attachment Cleanup Notice";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( MailingAttachmentCleanupProcess.class );

  /**
   * A reference to the mailing service.
   */
  private MailingService mailingService;

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  /**
   * Deletes files on app server that have been mailed to the user.
   */
  public void onExecute()
  {
    int deletedCount = 0;

    // Fetch the mailing attachments to be deleted
    List mailingAttachments = mailingService.getAllMailingAttachmentInfoAlreadySent();

    Iterator iter = mailingAttachments.iterator();
    while ( iter.hasNext() )
    {
      MailingAttachmentInfo mailingAttachmentInfo = (MailingAttachmentInfo)iter.next();

      try
      {
        boolean success = new File( mailingAttachmentInfo.getFullFileName() ).delete();
        if ( success )
        {
          deletedCount++;
        }
      }
      catch( Exception e )
      {
        log.error( "An exception occurred while deleting a mailing attachment file.  " + " (filename = " + mailingAttachmentInfo.getFullFileName() + ")" + " (process invocation ID = "
            + getProcessInvocationId() + ")", e );
        addComment( "An exception occurred while deleting a mailing attachment file.  " + " (filename = " + mailingAttachmentInfo.getFullFileName() + ")"
            + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
      }

      if ( isInterrupted() )
      {
        log.error( "Deposit process interrupted.  (process invocation ID = " + getProcessInvocationId() + ")" );
        addComment( "Deposit process interrupted.  (process invocation ID = " + getProcessInvocationId() + ")" );
        break;
      }
    }

    // Notify the administrator.
    sendSummaryMessage( deletedCount );

    log.info( "Total number of files deleted: " + deletedCount );
    addComment( "Total number of files deleted: " + deletedCount );
  }

  /**
   * Sets the mailing service
   * 
   * @param mailingService a reference to the mailing service.
   */
  public void setMailingService( MailingService mailingService )
  {
    this.mailingService = mailingService;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  /**
   * Composes and sends a summary e-mail to the "run by" user the number of the
   * number of files deleted.
   */
  private void sendSummaryMessage( int deletedCount )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "deletedCount", new Integer( deletedCount ) );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.MAILING_ATTACHMENT_CLEANUP_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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
      log.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

}
