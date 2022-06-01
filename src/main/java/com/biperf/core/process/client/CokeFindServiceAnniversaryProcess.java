
package com.biperf.core.process.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.process.BaseProcessImpl;
import com.biperf.core.service.client.CokeProcessService;
import com.biperf.core.service.message.MessageService;

public class CokeFindServiceAnniversaryProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "cokeFindServiceAnniversaryProcess";
  public static final String MESSAGE_NAME = "Coke Find Service Anniversary Process Notification";

  private CokeProcessService cokeProcessService;

  private static final Log logger = LogFactory.getLog( CokeFindServiceAnniversaryProcess.class );

  private Long startDays;
  private Long endDays;

  public void onExecute()
  {
    logger.info( "processName: " + BEAN_NAME + " - Calling Find Service Anniversary Procedure with # of start days: " + startDays + " and # of end days: " + endDays );
    addComment( "processName: " + BEAN_NAME + " - Calling Find Service Anniversary Procedure with # of start days: " + startDays + " and # of end days: " + endDays );

    Map<String, Object> resultMap = cokeProcessService.callCokeFindServiceAnniversaryProc( startDays, endDays );
    Long resultCode = ( (Long)resultMap.get( "p_out_return_code" ) );
    String resultMsg = ( (String)resultMap.get( "p_out_message" ) );

    boolean success;
    if ( ( resultCode.intValue() == 0 ) )
    {
      success = true;
    }
    else
    {
      success = false;
    }
    logger.info( "processName: " + BEAN_NAME + " - Completed Find Service Anniversary Procedure with # of start days: " + startDays + " and # of end days: " + endDays );
    addComment( "processName: " + BEAN_NAME + " - Completed Find Service Anniversary Procedure with # of start days: " + startDays + " and # of end days: " + endDays );
    sendSummaryMessage( success, resultMsg );
  }

  private void sendSummaryMessage( boolean success, String resultMsg )
  {
    User recipientUser = getRunByUser();

    Map<String, Object> objectMap = new HashMap<>();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    if ( success )
    {
      objectMap.put( "success", "Completed without errors." );
    }
    else
    {
      objectMap.put( "success", "Completed with errors. " + resultMsg );
    }

    Mailing mailing = composeMail( MessageService.COKE_FIND_SERVICE_ANNIVERSARY_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {

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

  public Long getStartDays()
  {
    return startDays;
  }

  public void setStartDays( Long startDays )
  {
    this.startDays = startDays;
  }

  public Long getEndDays()
  {
    return endDays;
  }

  public void setEndDays( Long endDays )
  {
    this.endDays = endDays;
  }

  public CokeProcessService getCokeProcessService()
  {
    return cokeProcessService;
  }

  public void setCokeProcessService( CokeProcessService cokeProcessService )
  {
    this.cokeProcessService = cokeProcessService;
  }

}
