/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/process/NominationAutoNotificationProcess.java,v $
 */

package com.biperf.core.process;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.claim.hibernate.ClaimGroupQueryConstraint;
import com.biperf.core.dao.claim.hibernate.NominationClaimQueryConstraint;
import com.biperf.core.domain.claim.Approvable;
import com.biperf.core.domain.claim.Claim;
import com.biperf.core.service.claim.ClaimGroupService;
import com.biperf.core.service.claim.ClaimProcessingStrategy;
import com.biperf.core.service.claim.ClaimProcessingStrategyFactory;
import com.biperf.core.service.claim.ClaimService;
import com.biperf.core.service.gamification.GamificationService;
import com.biperf.core.service.promotion.engine.PayoutCalculationResult;

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
public class NominationAutoNotificationProcess extends BaseProcessImpl
{

  public static final String BEAN_NAME = "nominationAutoNotificationProcess";
  private static final Log logger = LogFactory.getLog( NominationAutoNotificationProcess.class ); // bug
                                                                                                  // 73458

  private ClaimProcessingStrategyFactory claimProcessingStrategyFactory;
  private ClaimService claimService;
  private ClaimGroupService claimGroupService;
  private GamificationService gamificationService;

  // properties set from jobDataMap - none

  /**
   * Overridden from
   * 
   * @see com.biperf.core.process.BaseProcessImpl#onExecute()
   */
  public void onExecute()
  {

    NominationClaimQueryConstraint unprocessedClaimQueryConstraint = new NominationClaimQueryConstraint();
    unprocessedClaimQueryConstraint.setAnyActivitityUnposted( Boolean.TRUE );
    unprocessedClaimQueryConstraint.setRecipientNotificationDateExists( Boolean.TRUE );
    unprocessedClaimQueryConstraint.setRecipientNotificationDatePastDate( new Date() );
    unprocessedClaimQueryConstraint.setCumulative( Boolean.FALSE );
    List claims = claimService.getClaimList( unprocessedClaimQueryConstraint );

    ClaimGroupQueryConstraint unprocessedClaimGroupQueryConstraint = new ClaimGroupQueryConstraint();
    unprocessedClaimGroupQueryConstraint.setAnyActivitityUnposted( Boolean.TRUE );
    unprocessedClaimGroupQueryConstraint.setNotificationDateExists( Boolean.TRUE );
    unprocessedClaimGroupQueryConstraint.setNotificationDatePastDate( new Date() );
    List claimGroups = claimGroupService.getClaimGroupList( unprocessedClaimGroupQueryConstraint );

    Set approvables = new LinkedHashSet();
    approvables.addAll( claims );
    approvables.addAll( claimGroups );

    int successfulJournalPostCount = 0;
    int claimCount = 0;
    if ( approvables.isEmpty() )
    {
      addComment( "No approvables to process for this promotion" );
    }
    else
    {
      Set payoutCalculationResults = new LinkedHashSet();
      for ( Iterator iter = approvables.iterator(); iter.hasNext(); )
      {
        Approvable claim = (Approvable)iter.next();
        try
        {
          ClaimProcessingStrategy claimProcessingStrategy = claimProcessingStrategyFactory.getClaimProcessingStrategy( claim.getPromotion().getPromotionType() );
          claimCount++;
          payoutCalculationResults.addAll( claimProcessingStrategy.calculateAndSavePayouts( claim, true, null ) ); // Fixed
                                                                                                                   // as
                                                                                                                   // part
                                                                                                                   // of
                                                                                                                   // bug
                                                                                                                   // #56006,55519

          gamificationService.populateBadgePartcipant( (Claim)claim );

        }
        catch( Exception e ) // bug 73458
        {
          logger.error( "An exception occurred for claim_id " + claim.getId() + " (process invocation ID = " + getProcessInvocationId() + ")", e );
          addComment( "An exception occurred for claim_id " + claim.getId() + " See the log file for additional information.  " + "(process invocation ID = " + getProcessInvocationId() + ")" );
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
      addComment( "Number of successful journal posts: " + successfulJournalPostCount + " Total claims processed: " + claimCount );

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
   * @param claimService value for claimService property
   */
  public void setClaimService( ClaimService claimService )
  {
    this.claimService = claimService;
  }

  public void setClaimGroupService( ClaimGroupService claimGroupService )
  {
    this.claimGroupService = claimGroupService;
  }

  public void setGamificationService( GamificationService gamificationService )
  {
    this.gamificationService = gamificationService;
  }

}
