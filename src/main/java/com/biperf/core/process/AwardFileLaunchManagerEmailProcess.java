
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.awardgenerator.AwardGenBatch;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.awardgenerator.AwardGeneratorService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.AwardGeneratorManagerPaxBean;
import com.objectpartners.cms.util.CmsResourceBundle;

public class AwardFileLaunchManagerEmailProcess extends BaseProcessImpl
{
  private static final Log logger = LogFactory.getLog( AwardFileLaunchManagerEmailProcess.class );

  public static final String BEAN_NAME = "awardFileLaunchManagerEmailProcess";
  public static final String MESSAGE_NAME = "Award File Launch Manager Email Process";

  private AwardGeneratorService awardGeneratorService;

  String batchId;

  /**
   *
   */
  public AwardFileLaunchManagerEmailProcess()
  {
    super();
    logger.error( "process :" + toString() );
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    Long awardGenBatchId = new Long( batchId );
    AwardGenBatch awardGenBatch = awardGeneratorService.getAwardGenBatchById( awardGenBatchId );
    String promotionName = awardGenBatch.getAwardGen().getPromotion().getName();
    List<Long> managers = getAllManagersByBatchId( awardGenBatchId );

    if ( managers.isEmpty() )
    {
      logger.debug( "No managers found to send the award file manager email for awardGenBatchId: " + awardGenBatchId );
      addComment( "No managers found to send the award file manager email for awardGenBatchId: " + awardGenBatchId );
    }
    else
    {
      int successCount = 0;
      int failureCount = 0;
      for ( Iterator iter = managers.iterator(); iter.hasNext(); )
      {
        Long managerId = (Long)iter.next();
        try
        {
          sendManagerMessage( promotionName, managerId, awardGenBatchId );
          successCount++;
        }
        catch( Exception e )
        {
          failureCount++;
          logErrorMessage( e );
          addComment( "An exception occurred while sending award file launch manager email to participant id " + managerId + " " + "awardGenBatchId: " + awardGenBatchId );
        }
      }

      addComment( successCount + " successful award file participants with " + failureCount + " failures." );
      // Send an email to the user who schedules/launches this process
      sendSummaryMessage( promotionName, successCount, failureCount );
    }
  }

  private void sendManagerMessage( String promotionName, Long managerId, Long batchId )
  {
    User manager = getUserService().getUserById( managerId );
    Map objectMap = new HashMap();
    objectMap.put( "firstName", manager.getFirstName() );
    objectMap.put( "lastName", manager.getLastName() );
    objectMap.put( "promotionName", promotionName );

    String paxListDisplayText = buildManagerEmailPaxInfo( manager.getId(), batchId );
    objectMap.put( "paxListDisplayText", paxListDisplayText );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );

    Mailing mailing = composeMail( MessageService.AWARD_FILE_LAUNCH_MANAGER_MESSAGE_CM_ASSET_CODE, MailingType.PROMOTION );
    mailing.addMailingRecipient( addRecipient( manager ) );

    try
    {
      mailing = mailingService.submitMailingWithoutScheduling( mailing, objectMap );
      mailingService.processMailing( mailing.getId() );

      String msg = new String( "Award File Generator Launch " + " email message sent to " + manager.getFirstName() + " " + manager.getLastName() + "." + " (mailing ID = " + mailing.getId() + ")" );
      logger.info( msg );
    }
    catch( Exception e )
    {
      String msg = new String( "An exception occurred while sending a " + " Award File Generator Launch " + " (mailing ID = " + mailing.getId() + ")" );
      logger.error( msg, e );
    }
  }

  private String buildManagerEmailPaxInfo( Long userId, Long batchId )
  {
    StringBuffer sb = new StringBuffer( "" );
    List<AwardGeneratorManagerPaxBean> paxList = getPaxListByMgrAndBatchId( userId, batchId );
    if ( paxList != null && paxList.size() > 0 )
    {
      try
      {
        sb.append( "<table border = 1 id=\"\" class=\"table table-bordered table-striped\"> <tbody> <thead> <tr><th style=\"text-align:left\">" )
            .append( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.NAME" ) ).append( "</th>" ).append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" )
            .append( "<th>&nbsp;&nbsp;&nbsp;" ).append( CmsResourceBundle.getCmsBundle().getString( "profile.alerts.messages.AWARD_DATE" ) ).append( "</th>" ).append( "</tr> </thead>" );
        for ( AwardGeneratorManagerPaxBean paxBean : paxList )
        {
          sb.append( " <tr><td> " ).append( paxBean.getName() ).append( " </td>" );
          sb.append( "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;" );
          sb.append( "  <td>&nbsp;&nbsp;&nbsp;" ).append( DateUtils.toDisplayString( paxBean.getAwardDate() ) ).append( "</td>" );
        }
        sb.append( "</tr></tbody></table>" );

      }
      catch( Exception e )
      {
        logger.error( e.getMessage(), e );
      }
    }
    return sb.toString();
  }

  private List<Long> getAllManagersByBatchId( Long batchId )
  {
    return awardGeneratorService.getAllManagersByBatchId( batchId );
  }

  private List<AwardGeneratorManagerPaxBean> getPaxListByMgrAndBatchId( Long userId, Long batchId )
  {
    return awardGeneratorService.getPaxListByMgrAndBatchId( userId, batchId );
  }

  /**
   * Composes and sends the summary e-mail to the "run by" user.
   */
  private void sendSummaryMessage( String promotionName, int successCount, int failCount )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "promotionName", promotionName );
    objectMap.put( "successCount", new Integer( successCount ) );
    objectMap.put( "failCount", new Integer( failCount ) );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.AWARD_FILE_LAUNCH_ADMIN_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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
      logger.debug( "number of recipients successfully received " + MESSAGE_NAME + " Summary Email: " + successCount );
      logger.debug( "number of recipients failed to receive " + MESSAGE_NAME + " Summary Email: " + failCount );
      logger.debug( "--------------------------------------------------------------------------------" );

      addComment( "AwardFile Launch Manager process summary email sent to user id: " + UserManager.getUserId() );
    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public void setBatchId( String batchId )
  {
    this.batchId = batchId;
  }

  public AwardGeneratorService getAwardGeneratorService()
  {
    return awardGeneratorService;
  }

  public void setAwardGeneratorService( AwardGeneratorService awardGeneratorService )
  {
    this.awardGeneratorService = awardGeneratorService;
  }

}
