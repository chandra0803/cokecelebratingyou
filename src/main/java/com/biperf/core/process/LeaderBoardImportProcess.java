
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.leaderboard.LeaderBoard;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.leaderboard.LeaderBoardAssociationRequest;
import com.biperf.core.service.leaderboard.LeaderBoardService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;

public class LeaderBoardImportProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "leaderBoardImportProcess";
  public static final String MESSAGE_NAME = "LeaderBoard Import Process";

  private ImportService importService;
  private LeaderBoardService leaderBoardService;

  public LeaderBoardService getLeaderBoardService()
  {
    return leaderBoardService;
  }

  public void setLeaderBoardService( LeaderBoardService leaderBoardService )
  {
    this.leaderBoardService = leaderBoardService;
  }

  // properties set from jobDataMap
  String importFileId;
  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( LeaderBoardImportProcess.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  protected void onExecute()
  {
    boolean success = true;
    try
    {
      int counter = 1;
      while ( importService.importImportFile( new Long( importFileId ), counter, getRunByUser() ) )
      {
        counter++;
      }
      ImportFile importFile = importService.getImportFile( new Long( importFileId ) );
      importService.setImportFileStatus( importFile.getId(), ImportFileStatusType.IMPORTED );
      AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
      associationRequestCollection.add( new LeaderBoardAssociationRequest( LeaderBoardAssociationRequest.ALL_PAX ) );
      LeaderBoard leaderBoard = leaderBoardService.getLeaderBoardByIdWithAssociations( importFile.getLeaderboardId(), associationRequestCollection );
      leaderBoardService.saveLeaderBoard( leaderBoard );
    }
    catch( Exception e )
    {
      success = false;
      importService.setImportFileStatus( new Long( importFileId ), ImportFileStatusType.IMPORT_FAILED );

      logErrorMessage( e );
    }

    success = importService.getImportFile( new Long( importFileId ) ).getIsImported();
    // Notify the administrator.

    sendSummaryMessage( success );

  }

  /**
   * Composes and sends a summary e-mail.
   */
  private void sendSummaryMessage( boolean success )
  {
    User recipientUser = getRunByUser();
    ImportFile importFile = importService.getImportFile( new Long( importFileId ) );

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    if ( success )
    {
      objectMap.put( "success", "" );
    }
    else
    {
      objectMap.put( "success", "not " );
    }
    objectMap.put( "fileName", importFile.getFileName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );
    if ( importFile.getStatus().getCode().equals( ImportFileStatusType.IMPORT_FAILED ) )
    {
      objectMap.put( "importSuccess", "false" );
    }
    else
    {
      objectMap.put( "importSuccess", "true" );
    }
    // Compose the mailing
    Mailing mailing = composeMail( MessageService.LEADERBOARD_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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

  public void setImportService( ImportService importService )
  {
    this.importService = importService;
  }

  public void setImportFileId( String importFileId )
  {
    this.importFileId = importFileId;
  }

}
