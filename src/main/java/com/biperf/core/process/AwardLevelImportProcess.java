
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.fileload.ImportRecordError;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.BaseAssociationRequest;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;

/**
 * AwardLevelImportProcess.
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
 * <td>shanmuga</td>
 * <td>Feb 26, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class AwardLevelImportProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "awardLevelImportProcess";
  public static final String MESSAGE_NAME = "Award Level Import Process";
  public static final String ENROLLMENT_FAILURE = "ENROLLMENT_FAILED";  

  private ImportService importService;

  // properties set from jobDataMap
  String importFileId;

  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( AwardLevelImportProcess.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
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
      importService.setImportFileStatus( new Long( importFileId ), ImportFileStatusType.IMPORT_FAILED );

      logErrorMessage( e );
    }

    // Notify the administrator.
    sendSummaryMessage();
  }

  /**
   * Composes and sends a summary e-mail.
   */
  private void sendSummaryMessage()
  {
    User recipientUser = getRunByUser();
    ImportFile importFile = importService.getImportFile( new Long( importFileId ), new BaseAssociationRequest()
    {
      public void execute( Object domainObject )
      {
        ImportFile importFile = (ImportFile)domainObject;

        // Hydrate the import file's import
        // records.
        initialize( importFile.getImportRecordErrors() );
      }
    } );
    int enrollFailureCounter = 0;
    if ( importFile.getImportRecordErrors() != null && !importFile.getImportRecordErrors().isEmpty() )
    {
      for ( Iterator iterator = importFile.getImportRecordErrors().iterator(); iterator.hasNext(); )
      {
        ImportRecordError importRecordError = (ImportRecordError)iterator.next();

        // if we find any ENROLLMENT_FAILURE errors then count them
        if ( importRecordError != null && ENROLLMENT_FAILURE.equals( importRecordError.getItemKey() ) )
        {
          enrollFailureCounter++;
        }
      }
    }

    int totalRecords = importFile.getImportRecordCount();
    int recordsLoaded = totalRecords - importFile.getImportRecordErrorCount();
    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "fileName", importFile.getFileName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "total", String.valueOf( totalRecords ) );
    objectMap.put( "loaded", String.valueOf( recordsLoaded ) );
    objectMap.put( "failed", String.valueOf( enrollFailureCounter ) );
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
    Mailing mailing = composeMail( MessageService.AWARD_LEVEL_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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
