
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

public class PurlArchiveProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( PurlArchiveProcess.class );

  public static final String BEAN_NAME = "purlArchiveProcess";

  private PurlService purlService;

  public PurlArchiveProcess()
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
      log.info( " The Process 'PurlArchiveProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );
      addComment( " The Process 'PurlArchiveProcess' Has Been Restricted !!!!, Since The New Service Anniversary Has Been Enabled IN DM From Nackle Eco-System." );

    }
    else
    {

      int expireCount = processPurlExpire();

      int archiveCount = processPurlArchive();

      // Send an email to the user who schedules/launches this process
      sendSummaryMessage( expireCount, archiveCount );

    }

  }

  private int processPurlExpire()
  {
    int successCount = 0;
    List<PurlRecipient> purls = purlService.getAllPurlRecipientsToExpire();

    if ( purls.isEmpty() )
    {
      log.debug( "No purls expired to process." );
      addComment( "No purls expired to process." );
    }
    else
    {
      for ( PurlRecipient purl : purls )
      {
        Participant pax = (Participant)purl.getUser();
        try
        {
          boolean success = purlService.processExpirePurl( purl.getId() );
          if ( success )
          {
            successCount++;
          }
        }
        catch( Exception e )
        {
          logErrorMessage( e );
          // custom comment
          addComment( "An exception occurred while expiring PURL for " + pax.getFirstName() + " " + pax.getLastName() );
        }
      }
      addComment( successCount + " successfully expired." );
    }
    return successCount;
  }

  private int processPurlArchive()
  {
    int successCount = 0;
    List<PurlRecipient> purls = purlService.getAllPurlRecipientsToArchive();

    if ( purls.isEmpty() )
    {
      log.debug( "No purls to archive at this time." );
      addComment( "No purls to archive at this time." );
    }
    else
    {
      for ( PurlRecipient purl : purls )
      {
        Participant pax = (Participant)purl.getUser();
        try
        {
          boolean success = purlService.processArchivePurl( purl.getId() );
          if ( success )
          {
            successCount++;
          }
        }
        catch( Exception e )
        {
          logErrorMessage( e );
          // custom comment
          addComment( "An exception occurred while archiving PURL for " + pax.getFirstName() + " " + pax.getLastName() );
        }
      }
      addComment( successCount + " successfully archived." );
    }
    return successCount;
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
    Mailing mailing = composeMail( MessageService.PURL_ARCHIVE_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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
      log.debug( "number of PURLs successfully archived : " + successCount );
      log.debug( "number of PURLs failed archive : " + failCount );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( " Purl Archive Summary email sent to user id: " + UserManager.getUserId() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while archiving " + "(process invocation ID = " + getProcessInvocationId() + ")", e );

      addComment( "An exception occurred while archiving " + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  public void setPurlService( PurlService purlService )
  {
    this.purlService = purlService;
  }

}
