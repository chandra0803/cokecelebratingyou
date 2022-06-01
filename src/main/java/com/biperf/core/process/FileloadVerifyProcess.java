/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/FileloadVerifyProcess.java,v $
 */

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

/**
 * FileloadVerifyProcess.
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
 * <td>Sathish Vankayala</td>
 * <td>Jan, 09, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class FileloadVerifyProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "fileloadVerifyProcess";
  public static final String MESSAGE_NAME = "FileLoad Verify Process Message";

  private ImportService importService;

  // properties set from jobDataMap
  String importFileId;
  String fullUserName;

  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( FileloadVerifyProcess.class );

  /**
   * Overridden from
   * 
   * @see BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    boolean success = true;
    try
    {
      int counter = 1;
      while ( importService.verifyImportFile( new Long( importFileId ), counter, getRunByUser() ) )
      {
        counter++;
      }
    }
    catch( Exception e )
    {
      success = false;
      importService.setImportFileStatus( new Long( importFileId ), ImportFileStatusType.VERIFY_FAILED );
      logErrorMessage( e );
    }

    success = importService.getImportFile( new Long( importFileId ) ).getIsVerified();
    // Notify the administrator.
    sendSummaryMessage( success );
  }

  /**
   * Composes and sends a summary e-mail.
   */
  private void sendSummaryMessage( boolean success )
  {
    String localeCode = getSystemVariableService().getPropertyByName( SystemVariableService.DEFAULT_LANGUAGE ).getStringVal();

    ImportFile importFile = importService.getImportFile( new Long( importFileId ) );
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "numErrorRecords", String.valueOf( importFile.getImportRecordErrorCount() ) );
    int validRecords = importFile.getImportRecordCount() - importFile.getImportRecordErrorCount();
    objectMap.put( "numValidRecords", String.valueOf( validRecords ) );
    objectMap.put( "fileType", importFile.getFileType().getName() );
    objectMap.put( "fileName", importFile.getFileName() );
    objectMap.put( "verified", String.valueOf( success ) );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.FILE_LOAD_VERIFY_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mr.setLocale( localeCode ); // ****************** I think this is the problem - mailingrecipient
                                // has
    // to have locale set

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

  public void setFullUserName( String fullUserName )
  {
    this.fullUserName = fullUserName;
  }

}
