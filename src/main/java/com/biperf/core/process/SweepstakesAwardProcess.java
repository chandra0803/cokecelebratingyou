
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PromotionSweepstakeService;

public class SweepstakesAwardProcess extends BaseProcessImpl
{
  private static final Log log = LogFactory.getLog( SweepstakesAwardProcess.class );

  public static final String PROCESS_NAME = "Sweepstakes Award Process";
  public static final String BEAN_NAME = "sweepstakesAwardProcess";

  private String promotionId;

  private PromotionSweepstakeService promotionSweepstakeService;

  protected void onExecute()
  {
    Boolean completed = Boolean.FALSE;
    Long winnerSize = new Long( 0 );
    try
    {
      winnerSize = promotionSweepstakeService.processAward( new Long( promotionId ) );
      addComment( "Sweepstakes Award has been processed for " + winnerSize + " recipients" );
    }
    catch( Exception e )
    {
      log.error( "Exception occured while executing SweepstakesAwardProcess : \n" + e );
    }
    if ( !isInterrupted() )
    {
      completed = Boolean.TRUE;
    }
    sendSummaryMessage( completed, winnerSize );
  }

  private void sendSummaryMessage( Boolean completed, Long winnerSize )
  {
    User runByUser = getRunByUser();

    // Compose the mailing.
    Mailing mailing = composeMail( MessageService.SWEEPSTAKES_AWARD_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Collect e-mail message parameters.
    Map<String, Object> objectMap = new HashMap<String, Object>();
    objectMap.put( "firstName", runByUser.getFirstName() );
    objectMap.put( "lastName", runByUser.getLastName() );
    if ( completed )
    {
      objectMap.put( "completed", String.valueOf( Boolean.TRUE ) );
    }
    else
    {
      objectMap.put( "completed", String.valueOf( Boolean.FALSE ) );
    }
    objectMap.put( "winnerSize", winnerSize.toString() );

    // Add the recipient.
    MailingRecipient mailingRecipient = addRecipient( runByUser );
    mailing.addMailingRecipient( mailingRecipient );

    // Send the e-mail message.
    try
    {
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "------------------------------------------------------------------------------" );
      log.debug( "Process " + BEAN_NAME + " sent an email message to " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
      log.debug( "Process " + BEAN_NAME + " was run by user " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." );
      log.debug( "------------------------------------------------------------------------------" );

      addComment( "Summary email has been sent to system user" );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending a " + PROCESS_NAME + " email message. " + "(process invocation ID = " + getProcessInvocationId() + ")", e );

      addComment( "An exception occurred while sending a " + PROCESS_NAME + " email message. " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  public void setPromotionSweepstakeService( PromotionSweepstakeService promotionSweepstakeService )
  {
    this.promotionSweepstakeService = promotionSweepstakeService;
  }
}
