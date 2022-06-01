
package com.biperf.core.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.user.User;
import com.biperf.core.domain.user.UserCountryChanges;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.participant.UserCountryChangesService;

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
 * <td>Bala</td>
 * <td>Dec 9, 2011</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class CampaignTransferProcess extends BaseProcessImpl
{
  private UserCountryChangesService userCountryChangesService;
  public static final String BEAN_NAME = "campaignTransferProcess";

  private static final Log logger = LogFactory.getLog( CampaignTransferProcess.class );

  public void onExecute()
  {
    List<UserCountryChanges> userList = new ArrayList<UserCountryChanges>();
    List<Long> uccList = new ArrayList<Long>();
    long campaignSuccessfullyTransferedCnt = 0;
    long campaignTransferErrCnt = 0;
    long balancesSuccessfullyTransferedCnt = 0;
    long balanceTransferErrCnt = 0;

    // ----------------------------
    // Account Transfers
    // ----------------------------
    addComment( "Starting Account Transfers." );
    try
    {
      userList = userCountryChangesService.getUsersToMoveBalance();
      if ( userList != null && !userList.isEmpty() )
      {
        for ( Iterator iter = userList.iterator(); iter.hasNext(); )
        {
          UserCountryChanges userCountryChanges = (UserCountryChanges)iter.next();
          boolean successCampaign = userCountryChangesService.processCampaignTransfers( userCountryChanges );
          if ( successCampaign )
          {
            campaignSuccessfullyTransferedCnt++;
          }
          else
          {
            campaignTransferErrCnt++;
          }
        }
      }
    }
    catch( ServiceErrorException e )
    {
      logErrorMessage( e );
      addComment( "An exception occurred while running campaign transfer process" );
      campaignTransferErrCnt++;
    }
    User runByUser = getRunByUser();
    String msg = new String( "Process " + BEAN_NAME + " sent an email message to " + runByUser.getFirstName() + " " + runByUser.getLastName() + "." + "(process invocation ID = "
        + getProcessInvocationId() + ")" );

    addComment( msg );

    addComment( "Account Transfers Completed.  Total UCCs with accounts to transfer=" + uccList.size() + " Success Count=" + campaignSuccessfullyTransferedCnt + " Failure Count="
        + campaignTransferErrCnt );

    // ----------------------------
    // Account Balance Transfers
    // ----------------------------

    addComment( "Starting Account Balance Transfers." );
    // Get UCC processed with balances
    uccList = userCountryChangesService.getUCCsWithAccountBalancesToTranfer();
    logger.debug( "Total UCCs with balances to transfer :" + uccList.size() );

    for ( Long uccId : uccList )
    {
      logger.debug( "uccId=" + uccId );
      try
      {
        boolean success = userCountryChangesService.processAccountBalanceTransfer( uccId );
        if ( success )
        {
          balancesSuccessfullyTransferedCnt++;
        }
        else
        {
          balanceTransferErrCnt++;
        }
      }
      catch( ServiceErrorException se )
      {
        logger.error( "Error with Account Balance Transfer. uccId=" + uccId + " err=" + se.toString() );
        addComment( "Error with Account Balance Transfer. uccId=" + uccId + " err=" + se.toString() );
        balanceTransferErrCnt++;
      }
      catch( Exception e )
      {
        logger.error( "Error with Account Balance Transfer. uccId=" + uccId + " err=" + e.toString() );
        addComment( "Error with Account Balance Transfer. uccId=" + uccId + " err=" + e.toString() );
        balanceTransferErrCnt++;
      }
    }

    addComment( "Account Balance Transfers Completed.  Total UCCs with balances to transfer=" + uccList.size() + " Success Count=" + balancesSuccessfullyTransferedCnt + " Failure Count="
        + balanceTransferErrCnt );

    sendAcctBalSummaryMessage( userList.size(), campaignSuccessfullyTransferedCnt, campaignTransferErrCnt, uccList.size(), balancesSuccessfullyTransferedCnt, balanceTransferErrCnt );
  }

  /**
   * @param totalCnt
   * @param successCnt
   * @param failCnt
   */
  private void sendAcctBalSummaryMessage( long totalUserCnt,
                                          long campaignSuccessfullyTransferedCnt,
                                          long campaignTransferErrCnt,
                                          long totalUccCnt,
                                          long balancesSuccessfullyTransferedCnt,
                                          long balanceTransferErrCnt )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "totalCampaignCnt", totalUserCnt );
    objectMap.put( "successCampaignCnt", campaignSuccessfullyTransferedCnt );
    objectMap.put( "failCampaignCnt", campaignTransferErrCnt );
    objectMap.put( "totalCnt", totalUccCnt );
    objectMap.put( "successCnt", balancesSuccessfullyTransferedCnt );
    objectMap.put( "failCnt", balanceTransferErrCnt );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.CAMPAIGN_TRANS_ACCT_BAL_TRANSFER_SUMMARY_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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

      addComment( "processName: " + BEAN_NAME + " - Account Balance Transfer Summary email has been sent to: " + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      logger.error( "An exception occurred while sending Campaign Transfer Account Balance Transfer Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending Campaign Transfer Account Balance Transfer Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  public void setUserCountryChangesService( UserCountryChangesService userCountryChangesService )
  {
    this.userCountryChangesService = userCountryChangesService;
  }
}
