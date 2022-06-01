/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/BatchModePromotionEngineProcess.java,v $
 */

package com.biperf.core.process;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.claim.hibernate.ClaimQueryConstraint;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.domain.enums.MailingType;
import com.biperf.core.domain.mailing.Mailing;
import com.biperf.core.domain.mailing.MailingRecipient;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.user.User;
import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.claim.ClaimProcessingStrategy;
import com.biperf.core.service.claim.ClaimProcessingStrategyFactory;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.message.MessageService;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;
import com.biperf.core.service.system.SystemVariableService;

/**
 * BatchModePromotionEngineProcess.
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
public class BatchModePromotionEngineProcess extends BaseProcessImpl
{

  private static final Log log = LogFactory.getLog( BatchModePromotionEngineProcess.class );

  public static final String BEAN_NAME = "batchModePromotionEngineProcess";
  public static final String MESSAGE_NAME = "Batch Promotion Process";

  private ClaimProcessingStrategyFactory claimProcessingStrategyFactory;
  private ClaimService claimService;

  // properties set from jobDataMap
  private String promotionId;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {
    List claims = getClaimsForPromotion( new Long( promotionId ) );

    int successfulJournalPostCount = 0;
    if ( claims.isEmpty() )
    {
      addComment( "No claims to process for this promotion" );
    }
    else
    {
      Promotion promotion = ( (Claim)claims.get( 0 ) ).getPromotion();
      ClaimProcessingStrategy claimProcessingStrategy = claimProcessingStrategyFactory.getClaimProcessingStrategy( promotion.getPromotionType() );

      Set payoutCalculationResults = new LinkedHashSet();
      for ( Iterator iter = claims.iterator(); iter.hasNext(); )
      {
        Claim claim = (Claim)iter.next();
        try
        {
          payoutCalculationResults.addAll( claimProcessingStrategy.calculateAndSavePayouts( claim, true, null ) ); // Fixed
                                                                                                                   // as
                                                                                                                   // part
                                                                                                                   // of
                                                                                                                   // bug
                                                                                                                   // #56006,55519
        }
        catch( ServiceErrorException e )
        {
          log.error( e.getServiceErrorsCMText() );
        }
      }

      // Log count of successful journal posts
      for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
      {
        PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
        if ( payoutCalculationResult.getJournal() != null )
        {
          successfulJournalPostCount++;
        }
      }
      addComment( "Number of successful journal posts: " + successfulJournalPostCount );

      // Noify Administrator
      sendSummaryMessage( successfulJournalPostCount, promotion );
    }
  }

  private List getClaimsForPromotion( Long promotionId )
  {
    ClaimQueryConstraint claimQueryConstraint = new ClaimQueryConstraint();
    claimQueryConstraint.setIncludedPromotionIds( new Long[] { promotionId } );
    List claims = claimService.getClaimList( claimQueryConstraint );

    return claims;
  }

  /**
   * Composes and sends the summary e-mail to the "run by" user.
   */
  private void sendSummaryMessage( int successCount, Promotion promo )
  {
    User recipientUser = getRunByUser();

    // Add the summary info to the objectMap
    Map objectMap = new HashMap();
    objectMap.put( "firstName", recipientUser.getFirstName() );
    objectMap.put( "lastName", recipientUser.getLastName() );
    objectMap.put( "processName", BEAN_NAME );
    objectMap.put( "promotionName", promo.getName() );
    objectMap.put( "successCount", new Integer( successCount ) );
    objectMap.put( "clientName", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_NAME ).getStringVal() );
    objectMap.put( "clientPrefix", systemVariableService.getPropertyByName( SystemVariableService.CLIENT_PREFIX ).getStringVal() );

    // Compose the mailing
    Mailing mailing = composeMail( MessageService.BATCH_PROMOTION_PROCESS_MESSAGE_CM_ASSET_CODE, MailingType.PROCESS_EMAIL );

    // Add the recipient
    MailingRecipient mr = addRecipient( recipientUser );
    mailing.addMailingRecipient( mr );

    try
    {
      // Send the e-mail message with personalization
      mailingService.submitMailing( mailing, objectMap );

      log.debug( "--------------------------------------------------------------------------------" );
      log.debug( "processName: " + BEAN_NAME + " has been sent to:" );
      log.debug( recipientUser.getFirstName() + " " + recipientUser.getLastName() );
      log.debug( "--------------------------------------------------------------------------------" );

      addComment( "processName: " + BEAN_NAME + " summary email has been sent to:" + recipientUser.getFirstName() + " " + recipientUser.getLastName() );
    }
    catch( Exception e )
    {
      log.error( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "(process invocation ID = " + getProcessInvocationId() + ")", e );
      addComment( "An exception occurred while sending " + MESSAGE_NAME + " Summary Email.  " + "See the log file for additional information.  " + "(process invocation ID = "
          + getProcessInvocationId() + ")" );
    }
  }

  /**
   * @param claimProcessingStrategyFactory value for claimProcessingStrategyFactory property
   */
  public void setClaimProcessingStrategyFactory( ClaimProcessingStrategyFactory claimProcessingStrategyFactory )
  {
    this.claimProcessingStrategyFactory = claimProcessingStrategyFactory;
  }

  /**
   * @param promotionId value for promotionId property
   */
  public void setPromotionId( String promotionId )
  {
    this.promotionId = promotionId;
  }

  /**
   * @param claimService value for claimService property
   */
  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

}
