/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/ManagerOverridePayoutProcess.java,v $
 */

package com.biperf.core.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.PayoutProcessingService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;
import com.biperf.core.service.system.SystemVariableService;
import com.biperf.core.utils.DateUtils;

/**
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
public class ManagerOverridePayoutProcess extends BaseProcessImpl
{

  public static final String BEAN_NAME = "managerOverridePayoutProcess";
  public static final String MESSAGE_NAME = "Manager Override Process Notification";

  private PayoutProcessingService payoutProcessingService;
  private PromotionService promotionService;

  /**
   * The logger for this class.
   */
  private static final Log log = LogFactory.getLog( ManagerOverridePayoutProcess.class );

  // properties set from jobDataMap
  private String startDate;
  private String endDate;
  private String promotionId;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    Promotion promotion = promotionService.getPromotionById( new Long( promotionId ) );

    try
    {
      Set promotionCalculationResults = payoutProcessingService.processManagerOverride( new Long( promotionId ), DateUtils.toDate( startDate ), DateUtils.toDate( endDate ) );

      // Generate summary data for email - count of manager who got payout, and total payout
      Set uniqueManagerUserIds = new LinkedHashSet();
      long totalPayout = 0;

      for ( Iterator iter = promotionCalculationResults.iterator(); iter.hasNext(); )
      {
        PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
        Journal journal = payoutCalculationResult.getJournal();
        uniqueManagerUserIds.add( journal.getParticipant().getId() );
        totalPayout += journal.getTransactionAmount().longValue();
      }
      long managerCount = uniqueManagerUserIds.size();

      addComment( "Number of managers paid out: " + managerCount + " for a total payout of: " + totalPayout );

      // Notify the administrator.
      sendSummaryMessage( managerCount, totalPayout, promotion );
    }
    catch( ServiceErrorException e )
    {
      log.error( e.getServiceErrorsCMText() );
    }
  }

  /**
   * Composes and sends a summary e-mail to the "run by" user the number of deposits attempted, and
   * of that how many were success and failures. Also email the total number of points deposited
   */
  private void sendSummaryMessage( long managerCount, long totalPayout, Promotion promotion )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "promotionName", promotion.getName() );
    objectMap.put( "programName", promotion.getName() );
    objectMap.put( "startDate", startDate );
    objectMap.put( "endDate", endDate );
    objectMap.put( "managerCount", new Long( managerCount ) );
    objectMap.put( "totalPayout", new Long( totalPayout ) );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.MANAGER_OVERRIDE_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

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

  /**
   * @param payoutProcessingService value for payoutProcessingService property
   */
  public void setPayoutProcessingService( PayoutProcessingService payoutProcessingService )
  {
    this.payoutProcessingService = payoutProcessingService;
  }

  /**
   * @param promotionService value for promotionService property
   */
  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  /**
   * @param endDate value for endDate property
   */
  public void setEndDate( String endDate )
  {
    this.endDate = endDate;
  }

  /**
   * @param promotionId value for promotionId property
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @param startDate value for startDate property
   */
  public void setStartDate( String startDate )
  {
    this.startDate = startDate;
  }

}
