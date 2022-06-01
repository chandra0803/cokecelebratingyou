/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/QuizImportProcess.java,v $
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

public class QuizImportProcess extends BaseProcessImpl
{
  // ---------------------------------------------------------------------------
  // Constants
  // ---------------------------------------------------------------------------

  public static final String BEAN_NAME = "quizImportProcess";
  public static final String MESSAGE_NAME = "Quiz Import Process";

  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * the ID of the import file to process.
   */
  Long importFileId;

  private ImportService importService;

  private static final Log log = LogFactory.getLog( QuizImportProcess.class );

  // ---------------------------------------------------------------------------
  // Public Methods
  // ---------------------------------------------------------------------------

  public void onExecute()
  {
    try
    {
      int counter = 1;
      while ( importService.importImportFile( importFileId, counter, getRunByUser() ) )
      {
        counter++;
      }
    }
    catch( Exception e )
    {
      importService.setImportFileStatus( importFileId, ImportFileStatusType.IMPORT_FAILED );

      logErrorMessage( e );
    }

    sendSummaryMessage();
  }

  public void setImportFileId( String importFileId )
  {
    if ( importFileId != null )
    {
      this.importFileId = new Long( importFileId );
    }
  }

  public void setImportService( ImportService importService )
  {
    this.importService = importService;
  }

  // ---------------------------------------------------------------------------
  // Private Methods
  // ---------------------------------------------------------------------------

  private void sendSummaryMessage()
  {
    User recipientUser = getRunByUser();
    ImportFile importFile = importService.getImportFile( importFileId );

    // Add the summary info to the objectMap
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
    // Compose the mailing
    Mailing mailing = composeMail( MessageService.QUIZ_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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
