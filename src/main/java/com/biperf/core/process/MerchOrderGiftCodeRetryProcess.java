
package com.biperf.core.process;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.enums.ProcessFrequencyType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.merchandise.MerchOrder;
import com.biperf.core.domain.process.Process;
import com.biperf.core.domain.process.ProcessSchedule;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.merchorder.MerchOrderService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.process.ProcessService;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.UserManager;
import com.biperf.core.value.DepositProcessBean;

/**
 * 
 * This process was created to re-submit merchOrderCreateProcess for
 * merch_order records where gift code is null. Bug 66870
 */
public class MerchOrderGiftCodeRetryProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "merchOrderGiftCodeRetryProcess";
  public static final String MESSAGE_NAME = "Merch Order Gift Code Retry Process Notification";

  private static final Log logger = LogFactory.getLog( MerchOrderGiftCodeRetryProcess.class );

  private MerchOrderService merchOrderService;
  private ProcessService processService;
  // properties set from jobDataMap
  private String promotionId;

  public void onExecute()
  {
    int totalProcessErrors = 0;
    int totalGiftCodesPushedThru = 0;

    List<Long> merchOrderList = merchOrderService.getGiftCodeFailures( new Long( promotionId ) );

    Process process = processService.createOrLoadSystemProcess( MerchOrderCreateProcess.PROCESS_NAME, MerchOrderCreateProcess.BEAN_NAME );

    Long merchOrderId = null;
    for ( Iterator merchOrderIter = merchOrderList.iterator(); merchOrderIter.hasNext(); )
    {
      try
      {
        merchOrderId = (Long)merchOrderIter.next();

        MerchOrder merchOrder = merchOrderService.getMerchOrderById( merchOrderId );

        List<DepositProcessBean> depositProcessMerchList = new ArrayList<DepositProcessBean>();
        DepositProcessBean depositProcessBean = new DepositProcessBean();
        depositProcessBean.setMerchOrderId( merchOrder.getId() );
        depositProcessBean.setParticipantId( merchOrder.getParticipant().getId() );

        depositProcessMerchList.add( depositProcessBean );

        LinkedHashMap parameterValueMap = new LinkedHashMap();
        parameterValueMap.put( "depositProcessMerchList", depositProcessMerchList );
        Long promotionId = null;
        if ( Objects.nonNull( merchOrder.getClaim() ) )
        {
          promotionId = merchOrder.getClaim().getPromotion().getId();
        }
        else
        {
          promotionId = merchOrderService.getPromotionIdByMerchOrderId( merchOrder.getId() );
        }
        parameterValueMap.put( "promotionId", promotionId );
        parameterValueMap.put( "retry", "0" );
        parameterValueMap.put( "isRetriable", String.valueOf( Boolean.FALSE ) );

        ProcessSchedule processSchedule = new ProcessSchedule();
        processSchedule.setStartDate( new Date() );
        processSchedule.setTimeOfDayMillis( new Long( 0 ) );
        processSchedule.setProcessFrequencyType( ProcessFrequencyType.lookup( ProcessFrequencyType.ONE_TIME_ONLY ) );

        processService.scheduleProcess( process, processSchedule, parameterValueMap, UserManager.getUserId() );

        totalGiftCodesPushedThru++;
      }
      catch( Exception e )
      {
        totalProcessErrors++;
        logErrorMessage( e );

        String errorMessage = "An exception occurred while processing merch_order_id " + merchOrderId + ". See logs for additional information.";
        addComment( errorMessage );
      }
    }

    logger.info( "processName: " + BEAN_NAME + " - completed for promotion id " + promotionId + ". Total merch_order reprocessed = " + totalGiftCodesPushedThru );
    addComment( "processName: " + BEAN_NAME + " - completed for promotion id " + promotionId + ". Total merch_order reprocessed = " + totalGiftCodesPushedThru );

    // Notify the administrator.
    sendSummaryMessage( totalProcessErrors, totalGiftCodesPushedThru );

  }

  /**
   * sendSummaryMessage
   * @param totalMailingsSent
   */
  private void sendSummaryMessage( int totalProcessErrors, int totalGiftCodesPushedThru )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "totalGiftCodesPushedThru", totalGiftCodesPushedThru );
    objectMap.put( "promotionId", promotionId );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );
    if ( totalProcessErrors > 0 )
    {
      objectMap.put( "errorMessage", "The process completed with errors. See process log for more information" );
    }
    else
    {
      objectMap.put( "errorMessage", "" );
    }

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.MERCH_ORDER_GIFT_CODE_RETRY_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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
      logger.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " email has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public void setMerchOrderService( MerchOrderService merchOrderService )
  {
    this.merchOrderService = merchOrderService;
  }

  public void setProcessService( ProcessService processService )
  {
    this.processService = processService;
  }

  public String getPromotionId()
  {
    return promotionId;
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

}