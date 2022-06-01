
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;

public class SSIContestDTGTImportProcess extends BaseProcessImpl
{

  public static final String BEAN_NAME = "ssicontestDTGTImportProcess";
  public static final String MESSAGE_NAME = "SSI Contest DTGT - Import Process";

  private ImportService importService;

  String importFileId;

  private static final Log log = LogFactory.getLog( SSIContestDTGTImportProcess.class );

  @Override
  public void onExecute()
  {
    boolean success = true;
    try
    {
      int counter = 1;
      while ( importService.importImportFile( new Long( importFileId ), counter, getRunByUser() ) )
      {
        counter++;
      }
    }
    catch( Exception e )
    {
      success = false;
      importService.setImportFileStatus( new Long( importFileId ), ImportFileStatusType.IMPORT_FAILED );
      logErrorMessage( e );
    }
    success = importService.getImportFile( new Long( importFileId ) ).getIsImported();
    sendSummaryMessage( success );
  }

  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private void sendSummaryMessage( boolean success )
  {
    User recipientUser = getRunByUser();
    ImportFile importFile = importService.getImportFile( new Long( importFileId ) );

    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "fileName", importFile.getFileName() );
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

    Mailing mailing = composeMail( MessageService.SSI_CONTEST_DTGT_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {

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
