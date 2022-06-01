
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.ImportFileStatusType;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.fileload.ImportFile;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.fileload.ImportService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.value.MailingBatchHolder;

public class AutoVinImportProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "autoVinImportProcess";
  public static final String MESSAGE_NAME = "GoalQuest Auto Vin Import Process";

  private ImportService importService;

  // properties set from jobDataMap
  String importFileId;

  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( AutoVinImportProcess.class );

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    MailingBatchHolder mailingBatchHolder = null;
    try
    {
      int counter = 1;
      mailingBatchHolder = getMailingBatchHolderForProgressImportProcess(); // all progress imports
                                                                            // are potential batch
                                                                            // emails
      while ( importService.importImportFile( new Long( importFileId ), counter, getRunByUser(), mailingBatchHolder ) )
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
    sendSummaryMessage( mailingBatchHolder );
    logMailingBatchHolderComments( mailingBatchHolder );
  }

  protected void logMailingBatchHolderComments( MailingBatchHolder mailingBatchHolder )
  {
    if ( mailingBatchHolder == null )
    {
      return;
    }

    String participantAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPaxAchievedMailingBatch() );
    if ( !StringUtils.isEmpty( participantAchievedBatchEmailComments ) )
    {
      addComment( participantAchievedBatchEmailComments );
    }

    String participantNotAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPaxNotAchievedMailingBatch() );
    if ( !StringUtils.isEmpty( participantNotAchievedBatchEmailComments ) )
    {
      addComment( participantNotAchievedBatchEmailComments );
    }

    String partnerAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerAchievedMailingBatch() );
    if ( !StringUtils.isEmpty( partnerAchievedBatchEmailComments ) )
    {
      addComment( partnerAchievedBatchEmailComments );
    }

    String partnerAchievedNoPayoutBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerAchievedNoPayoutMailingBatch() );
    if ( !StringUtils.isEmpty( partnerAchievedNoPayoutBatchEmailComments ) )
    {
      addComment( partnerAchievedNoPayoutBatchEmailComments );
    }

    String partnerNotAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerNotAchievedMailingBatch() );
    if ( !StringUtils.isEmpty( partnerNotAchievedBatchEmailComments ) )
    {
      addComment( partnerNotAchievedBatchEmailComments );
    }

    String notSelected = getMailingBatchProcessComments( mailingBatchHolder.getGoalNotSelectedBatch() );
    if ( !StringUtils.isEmpty( notSelected ) )
    {
      addComment( notSelected );
    }

    String partnerProgress = getMailingBatchProcessComments( mailingBatchHolder.getPartnerProgressMailingBatch() );
    if ( !StringUtils.isEmpty( partnerProgress ) )
    {
      addComment( partnerProgress );
    }

    String participantProgress = getMailingBatchProcessComments( mailingBatchHolder.getPaxProgressMailingBatch() );
    if ( !StringUtils.isEmpty( participantProgress ) )
    {
      addComment( participantProgress );
    }
  }

  private MailingBatchHolder getMailingBatchHolderForProgressImportProcess()
  {
    Promotion promotion = importService.getImportFile( new Long( importFileId ) ).getPromotion();
    MailingBatchHolder batchHolder = new MailingBatchHolder();// fix bug #37434
    if ( getMailingService().isBatchEmailEnabled() )
    {
      batchHolder.setPaxProgressMailingBatch( applyBatch( promotion.getName() + " Import Pax Progress " ) );
      batchHolder.setPartnerProgressMailingBatch( applyBatch( promotion.getName() + " Import Partner Progress " ) );// TODO
                                                                                                                    // this
                                                                                                                    // in
                                                                                                                    // strategy??
    }
    return batchHolder;
  }

  /**
   * Composes and sends a summary e-mail.
   */
  private void sendSummaryMessage( MailingBatchHolder mailingBatchHolder )
  {
    User recipientUser = getRunByUser();
    ImportFile importFile = importService.getImportFile( new Long( importFileId ) );

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
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
    if ( mailingBatchHolder != null )
    {
      String batchComments = generateMailingBatchComments( mailingBatchHolder );
      if ( !StringUtils.isEmpty( batchComments ) )
      {
        objectMap.put( "batchComments", batchComments );
      }
    }
    // Compose the mailing
    Mailing mailing = composeMail( MessageService.AUTO_VIN_IMPORT_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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

  private String generateMailingBatchComments( MailingBatchHolder mailingBatchHolder )
  {
    if ( null == mailingBatchHolder )
    {
      return "";
    }

    StringBuilder sb = new StringBuilder();

    String participantAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPaxAchievedMailingBatch() );
    sb.append( participantAchievedBatchEmailComments );

    String participantNotAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPaxNotAchievedMailingBatch() );
    sb.append( participantNotAchievedBatchEmailComments );

    String partnerAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerAchievedMailingBatch() );
    sb.append( partnerAchievedBatchEmailComments );

    String partnerAchievedNoPayoutBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerAchievedNoPayoutMailingBatch() );
    sb.append( partnerAchievedNoPayoutBatchEmailComments );

    String partnerNotAchievedBatchEmailComments = getMailingBatchProcessComments( mailingBatchHolder.getPartnerNotAchievedMailingBatch() );
    sb.append( partnerNotAchievedBatchEmailComments );

    return sb.toString();
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
