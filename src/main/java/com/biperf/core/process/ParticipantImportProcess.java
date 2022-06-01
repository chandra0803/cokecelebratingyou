/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/ParticipantImportProcess.java,v $
 */

package com.biperf.core.process;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.cache.oscache.ManageableCacheAdministrator;
import com.biperf.core.domain.enums.CampaignTransferProcessModeType;
import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserCountryChangesService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.ApplicationContextFactory;
import com.biperf.core.utils.UserManager;

/**
 * .
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
 * <td>wadzinsk</td>
 * <td>Nov 29, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class ParticipantImportProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "participantImportProcess";
  public static final String MESSAGE_NAME = "Participant Import Process";

  private ImportService importService;

  private UserCountryChangesService userCountryChangesService;

  private ProcessService processService;

  // properties set from jobDataMap
  String importFileId;

  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( ParticipantImportProcess.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
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

    ImportFile importFile = importService.getImportFile( new Long( importFileId ) );
    boolean countryChanges = importFile.getIsCountryChanged();
    if ( countryChanges )
    {
      importService.setImportFileStatus( new Long( importFileId ), ImportFileStatusType.IMPORTED );
    }

    success = importFile.getIsImported() || countryChanges;
    if ( success )
    {
      ManageableCacheAdministrator cache = (ManageableCacheAdministrator)ApplicationContextFactory.getContentManagerApplicationContext().getBean( "cmsCacheAdministrator" );
      cache.flushAll( new Date() );
      cache.clear();
      addComment( "Content Manager cache flushed and cleared" );
    }
    // Notify the administrator.
    sendSummaryMessage( success, countryChanges );

    if ( success && countryChanges )
    {
      boolean runCampaignMoveProcess = systemVariableService.getPropertyByName( SystemVariableService.RUN_CAMPAIGN_TRANSFER_PROCESS ).getBooleanVal();
      if ( runCampaignMoveProcess )
      {
        LinkedHashMap<String, Object> parameterValueMap = new LinkedHashMap<String, Object>();
        parameterValueMap.put( "campaignTransferMode", CampaignTransferProcessModeType.ALL );
        Process process = getProcessService().createOrLoadSystemProcess( "campaignTransferProcess", CampaignTransferProcess.BEAN_NAME );
        getProcessService().launchProcess( process, parameterValueMap, UserManager.getUserId() );
      }
    }

  }

  /**
   * Composes and sends a summary e-mail.
   */
  private void sendSummaryMessage( boolean success, boolean countryChanges )
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

    if ( countryChanges )
    {
      objectMap.put( "countryChanges", "true" );
    }
    if ( importFile.getStatus().getCode().equals( ImportFileStatusType.IMPORT_FAILED ) )
    {
      objectMap.put( "importSuccess", "false" );
    }
    else
    {
      objectMap.put( "importSuccess", "true" );
    }

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.PARTICIPANT_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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

  public void setUserCountryChangesService( UserCountryChangesService userCountryChangesService )
  {
    this.userCountryChangesService = userCountryChangesService;
  }

  public UserCountryChangesService getUserCountryChangesService()
  {
    return userCountryChangesService;
  }

  public ProcessService getProcessService()
  {
    return processService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

}
