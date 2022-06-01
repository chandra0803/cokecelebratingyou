
package com.biperf.core.process;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.awardbanq.AwardBanQServiceFactory;
import com.biperf.core.service.message.MessageService;

/**
 * 
 * @author poddutur
 * @since Mar 29, 2016
 */

public class CashCurrencyProcess extends BaseProcessImpl
{
  public static final String BEAN_NAME = "cashCurrencyProcess";

  private static final Log log = LogFactory.getLog( CashCurrencyProcess.class );

  private AwardBanQServiceFactory awardBanQServiceFactory;

  /** Flag can be set to true when testing to skip sending the update message */
  private boolean skipMessageFlag = false;

  public void onExecute()
  {
    try
    {
      awardBanQServiceFactory.getAwardBanQService().updateCashCurrencies();

      if ( !skipMessageFlag )
      {
        sendUpdateCashCurrenciesSummaryMessage();
      }
    }
    catch( ServiceErrorException e )
    {
      log.error( "An exception occurred while updating cash currencies.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while updating cash currencies.  " + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  private void sendUpdateCashCurrenciesSummaryMessage()
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.UPDATE_CASH_CURRENCIES_SUMMARY_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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

      addComment( "processName: " + BEAN_NAME + " - Update on Cash Currencies Summary email has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while updating cash currencies.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while updating cash currencies.  " + "See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
    }
  }

  public void setAwardBanQServiceFactory( AwardBanQServiceFactory awardBanQServiceFactory )
  {
    this.awardBanQServiceFactory = awardBanQServiceFactory;
  }

  public void setSkipMessageFlag( boolean skipMessageFlag )
  {
    this.skipMessageFlag = skipMessageFlag;
  }
}
