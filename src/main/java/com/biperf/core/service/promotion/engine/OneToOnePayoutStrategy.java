/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/OneToOnePayoutStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.biperf.core.dao.claim.MinimumQualifierStatusDAO;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.audit.SalesPayoutCalculationAudit;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.domain.claim.ProductClaim;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.exception.BeaconRuntimeException;

/**
 * CrossSellPayoutStrategy. Payout Strategy for "One-To-One" promotion payout type.
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
 * <td>Aug 3, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class OneToOnePayoutStrategy extends AbstractPayoutStrategy
{
  private static final Log log = LogFactory.getLog( OneToOnePayoutStrategy.class );

  /**
   * @param activities
   * @param promotion
   * @param payoutCalculationFacts
   * @return Set
   */
  protected Set processActivitiesInternal( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts )
  {
    if ( !promotion.isProductClaimPromotion() )
    {
      throw new BeaconRuntimeException( "CrossSellPayoutStrategy should be used only for ProductClaim promotions" );
    }

    if ( activities.isEmpty() )
    {
      throw new BeaconRuntimeException( "CrossSellPayoutStrategy input activity set must contain at least one activity, was empty" );
    }

    ProductClaimPromotion pcPromotion = (ProductClaimPromotion)promotion;
    Set payoutCalculationResults = new LinkedHashSet();

    SalesFacts salesFacts = (SalesFacts)payoutCalculationFacts;

    for ( Iterator iter = pcPromotion.getPromotionPayoutGroups().iterator(); iter.hasNext(); )
    {
      PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)iter.next();
      log.info( "** Calculating payout(s) for promotion: " + promotion.getId() + " of payout type: " + pcPromotion.getPayoutType().getCode() + "for participant: "
          + SalesPayoutStrategyUtil.getReferenceSalesActivity( activities ).getParticipant().getNameLFMNoComma() );
      PayoutCalculationResult payoutCalculationResult = processPayoutGroup( activities, promotionPayoutGroup, salesFacts );
      payoutCalculationResults.add( payoutCalculationResult );
    }

    return payoutCalculationResults;
  }

  /**
   * @param originalActivities
   * @param promotionPayoutGroup
   * @param salesFacts
   * @return PayoutCalculationResult
   */
  protected PayoutCalculationResult processPayoutGroup( Set originalActivities, PromotionPayoutGroup promotionPayoutGroup, SalesFacts salesFacts )
  {
    log.info( "** Calculating payout for promotionPayoutGroup: " + promotionPayoutGroup.getId() );
    boolean failureOccurred = false;

    SalesActivity referenceSalesActivity = SalesPayoutStrategyUtil.getReferenceSalesActivity( originalActivities );

    // Initialize calculation audit and result.
    PayoutCalculationResult payoutCalculationResult = new PayoutCalculationResult();
    payoutCalculationResult.setPromotionPayoutGroup( promotionPayoutGroup );
    SalesPayoutCalculationAudit payoutCalculationAudit = new SalesPayoutCalculationAudit();
    payoutCalculationResult.setPayoutCalculationAudit( payoutCalculationAudit );
    payoutCalculationAudit.setClaim( referenceSalesActivity.getClaim() );
    payoutCalculationAudit.setParticipant( referenceSalesActivity.getParticipant() );
    payoutCalculationAudit.setPromotionPayoutGroup( promotionPayoutGroup );

    // ****
    // ** Determine "promotion group fulfilled count"
    List promotionPayoutFulfilledCounts = new ArrayList();

    List promotionPayouts = promotionPayoutGroup.getPromotionPayouts();
    if ( promotionPayouts.size() != 1 )
    {
      throw new BeaconRuntimeException( "One to One payout type can only have one PromotionPayout object " + "per promotionPayoutGroup. promotionPayoutGroup id = " + promotionPayoutGroup.getId() );
    }
    PromotionPayout promotionPayout = (PromotionPayout)promotionPayouts.iterator().next();
    // Only one required for one-to-one
    int quantityRequired = 1;
    int qualifyingQuantitySold = 0;

    Set matchingSalesActivities = SalesPayoutStrategyUtil.findSalesActivitiesWithMatchingProduct( promotionPayout, originalActivities );
    if ( !matchingSalesActivities.isEmpty() )
    {
      // Store all activites that contributed.
      payoutCalculationResult.addAllContributingActivities( matchingSalesActivities );

      // create "promotion payout fulfilled count" value
      qualifyingQuantitySold = SalesPayoutStrategyUtil.getQuantitySoldForAllMatchingActivities( matchingSalesActivities );

      // Process "retro of" minimimum qualifier after quantity calculation but before payout since
      // in that case,we need to lower the effective quantity sold quantity for payout until the
      // minimum qualifier is met.
      if ( promotionPayoutGroup.isMinimumQualifierUsed() && !promotionPayoutGroup.isRetroPayout() && !SalesPayoutStrategyUtil.isMinQualifierMet( salesFacts, promotionPayoutGroup ) )
      {
        int oldMinQualifierCompleted = SalesPayoutStrategyUtil.getOldMinQualifierCompleted( salesFacts, promotionPayoutGroup );

        failureOccurred = SalesPayoutStrategyUtil
            .processMinimumQualifier( promotionPayoutGroup, salesFacts, failureOccurred, referenceSalesActivity, payoutCalculationResult, payoutCalculationAudit, qualifyingQuantitySold );

        int newMinQualifierCompleted = payoutCalculationResult.getMinimumQualifierStatus().getCompletedQuantity();
        int quantityUsedToMeetMinQualifier = newMinQualifierCompleted - oldMinQualifierCompleted;

        if ( quantityUsedToMeetMinQualifier != 1 )
        {
          qualifyingQuantitySold = qualifyingQuantitySold - quantityUsedToMeetMinQualifier;
        }
      }

      if ( !failureOccurred )
      {
        Long promotionPayoutFulfilledCount = new Long( qualifyingQuantitySold / quantityRequired );

        log.info( "promotionPayout " + promotionPayout.getId() + "  fulfilled " + promotionPayoutFulfilledCount + " times" );

        if ( promotionPayoutFulfilledCount.longValue() == 0 )
        {
          throw new BeaconRuntimeException( "Shouldn't be possible in one to one: sales activities can't have 0 quantity" );
        }

        promotionPayoutFulfilledCounts.add( promotionPayoutFulfilledCount );

      }
    }
    else
    {
      failureOccurred = true;
      payoutCalculationAudit.setReason( PayoutCalculationAuditReasonType.CROSS_SELL_UNMATCHED_PRODUCT, new String[] { promotionPayout.getRequiredProductOrProductCategoryName() } );
      log.info( "Engine calculation Failure of type(" + payoutCalculationAudit.getReasonType().getCode() + "): " + payoutCalculationAudit.getReasonText() );
    }

    if ( !failureOccurred )
    {
      long promotionGroupFulfilledCount = getMinPromotionPayoutFulfilledCount( promotionPayoutFulfilledCounts );

      log.info( "promotionPayoutGroup " + promotionPayoutGroup.getId() + "  fulfilled " + promotionGroupFulfilledCount + " times" );

      // ****
      // ** Calculate payouts for submitter or team member.
      long calculatedPayout = 0;
      if ( referenceSalesActivity.isSubmitter() )
      {
        calculatedPayout = promotionGroupFulfilledCount * promotionPayoutGroup.getSubmitterPayout();
      }
      else
      {
        int teamMemberPayout = 0;
        if ( promotionPayoutGroup.getTeamMemberPayout() != null )
        {
          teamMemberPayout = promotionPayoutGroup.getTeamMemberPayout().intValue();
        }
        calculatedPayout = promotionGroupFulfilledCount * teamMemberPayout;
      }

      if ( calculatedPayout == 0 )
      {
        // Team or submitter payout was zero.
        // Save as failure
        failureOccurred = true;
        payoutCalculationAudit.setReason( PayoutCalculationAuditReasonType.SALES_ZERO_PAYOUT_GROUP_PAYOUT, null );
        log.info( "Engine calculation Failure of type(" + payoutCalculationAudit.getReasonType().getCode() + "): " + payoutCalculationAudit.getReasonText() );

      }
      else
      {
        // Calculation produced non-zero payout.
        payoutCalculationResult.setCalculatedPayout( new Long( calculatedPayout ) );
        payoutCalculationAudit.setReason( PayoutCalculationAuditReasonType.SALES_SUCCESS, new String[] { String.valueOf( promotionGroupFulfilledCount ), String.valueOf( calculatedPayout ) } );
        log.info( "promotionPayoutGroup " + promotionPayoutGroup.getId() + "  calculated payout: " + calculatedPayout );
      }
    }

    // Process "retro on" minimimum qualifier after payout calculation since in that case,
    // payout will occur but journals will be put in a pending state
    if ( promotionPayoutGroup.isMinimumQualifierUsed() && promotionPayoutGroup.isRetroPayout() )
    {
      failureOccurred = SalesPayoutStrategyUtil.processMinimumQualifier( promotionPayoutGroup,
                                                                         salesFacts,
                                                                         failureOccurred,
                                                                         referenceSalesActivity,
                                                                         payoutCalculationResult,
                                                                         payoutCalculationAudit,
                                                                         qualifyingQuantitySold );
    }

    // If there was a successful payout and the promotion has a manager payout
    // then create a ManagerOverrideActivity.
    ProductClaimPromotion promotion = (ProductClaimPromotion)payoutCalculationResult.getPromotionPayoutGroup().getPromotion();
    if ( referenceSalesActivity.isSubmitter() && !failureOccurred && promotion.isPayoutManager() && salesFacts.getManager() != null )
    {
      ManagerOverrideActivity activity = new ManagerOverrideActivity( referenceSalesActivity.getClaim(), payoutCalculationResult.getCalculatedPayout() );
      activity.setPosted( false );
      activity.setNode( referenceSalesActivity.getNode() );
      activity.setPromotion( referenceSalesActivity.getPromotion() );
      activity.setParticipant( salesFacts.getManager() );
      activity.setSubmissionDate( new Date() );
      activity.setMinimumQualifierStatus( payoutCalculationResult.getMinimumQualifierStatus() );

      payoutCalculationResult.addGeneratedActivity( activity );
    }

    // For submitter, temporarily mark on claim that submitter has/has not met qualifier. Used for
    // team member
    // payout calculation.
    // Requires that team member calculation occurs against the same claim object.
    if ( referenceSalesActivity.isSubmitter() && payoutCalculationResult.getMinimumQualifierStatus() != null )
    {
      ( (ProductClaim)referenceSalesActivity.getClaim() ).setMinimumQualifierMet( Boolean.valueOf( payoutCalculationResult.getMinimumQualifierStatus().isMinQualifierMet() ) );
    }

    // ****
    // ** return result object
    return payoutCalculationResult;

  }

  private long getMinPromotionPayoutFulfilledCount( List promotionPayoutFulfilledCounts )
  {
    long minFulfilledCount = 0;

    if ( !promotionPayoutFulfilledCounts.isEmpty() )
    {

      Iterator iter = promotionPayoutFulfilledCounts.iterator();
      Long promotionPayoutFulfilledCount = (Long)iter.next();

      // Seed minimum with first value
      minFulfilledCount = promotionPayoutFulfilledCount.longValue();
      while ( iter.hasNext() )
      {
        promotionPayoutFulfilledCount = (Long)iter.next();
        if ( promotionPayoutFulfilledCount.longValue() < minFulfilledCount )
        {
          minFulfilledCount = promotionPayoutFulfilledCount.longValue();
        }
      }
    }
    return minFulfilledCount;
  }

  /**
   * This postProcess implementation will save any MinimumQualifierStatus adds/changes for the
   * submitter. Done here rather than in ProductClaimProcessingStrategy since hibernate needs to
   * query against these values before they are saved. Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.AbstractPayoutStrategy#postProcess(java.util.Set,
   *      com.biperf.core.domain.promotion.Promotion,
   *      com.biperf.core.service.promotion.engine.PayoutCalculationFacts, java.util.Set)
   * @param activities
   * @param promotion
   * @param payoutCalculationFacts
   * @param payoutCalculationResults
   */
  protected void postProcess( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts, Set payoutCalculationResults )
  {
    SalesActivity referenceSalesActivity = SalesPayoutStrategyUtil.getReferenceSalesActivity( activities );

    for ( Iterator iter = payoutCalculationResults.iterator(); iter.hasNext(); )
    {
      PayoutCalculationResult payoutCalculationResult = (PayoutCalculationResult)iter.next();
      Participant participant = payoutCalculationResult.getPayoutCalculationAudit().getParticipant();
      if ( participant.equals( referenceSalesActivity.getClaim().getSubmitter() ) )
      {
        MinimumQualifierStatus minimumQualifierStatus = payoutCalculationResult.getMinimumQualifierStatus();
        // will be null if min qualifier not used or has been exceeded.
        if ( minimumQualifierStatus != null )
        {
          minimumQualifierStatusDAO.save( minimumQualifierStatus );
        }
      }
    }
  }

  private MinimumQualifierStatusDAO minimumQualifierStatusDAO;

  /**
   * @param minimumQualifierStatusDAO value for minimumQualifierStatusDAO property
   */
  public void setMinimumQualifierStatusDAO( MinimumQualifierStatusDAO minimumQualifierStatusDAO )
  {
    this.minimumQualifierStatusDAO = minimumQualifierStatusDAO;
  }

}
