/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/TieredPayoutStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import com.biperf.core.dao.claim.MinimumQualifierStatusDAO;
import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.audit.SalesPayoutCalculationAudit;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.exception.BeaconRuntimeException;
import com.biperf.core.utils.GuidUtils;

/**
 * TieredPayoutStrategy <br/> Payout Strategy for "Tiered" promotion payout type.
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
public class TieredPayoutStrategy extends AbstractPayoutStrategy
{
  private static final Log log = LogFactory.getLog( TieredPayoutStrategy.class );

  /**
   * Each SalesActivity in activities is assumed to be for single participant, a single claim, and a
   * single promotion. They are also assumed to be unposted.
   * 
   * @param activities
   * @param promotion
   * @param payoutCalculationFacts
   * @return Set
   */
  protected Set processActivitiesInternal( Set activities, Promotion promotion, PayoutCalculationFacts payoutCalculationFacts )
  {
    if ( !promotion.isProductClaimPromotion() )
    {
      throw new BeaconRuntimeException( "TieredPayoutStrategy should be used only for ProductClaim promotions" );
    }

    if ( activities.isEmpty() )
    {
      throw new BeaconRuntimeException( "TieredPayoutStrategy input activity set must contain at least one activity, was empty" );
    }

    ProductClaimPromotion pcPromotion = (ProductClaimPromotion)promotion;

    Set payoutCalculationResults = new LinkedHashSet();

    SalesFacts salesFacts = (SalesFacts)payoutCalculationFacts;

    // The logic for calculating payout is as follows:
    // Loop through the payout groups in order of highest payout first.
    // If a payout occurs, check to see if there are any leftover activities (or partial)
    // If there are more activities to process, set them as the input activities for the
    // next group and clear out the generated activities for the currect group. (Carryover
    // will always be on the last group)

    // Sort the group highest payout first.
    List promotionPayoutGroups = pcPromotion.getPromotionPayoutGroups();
    PropertyComparator.sort( promotionPayoutGroups, new MutableSortDefinition( "submitterPayout", true, false ) );

    Set inputActivities = activities;

    int i = 1;
    int groupSize = promotionPayoutGroups.size();
    for ( Iterator iter = promotionPayoutGroups.iterator(); iter.hasNext(); i++ )
    {
      PromotionPayoutGroup promotionPayoutGroup = (PromotionPayoutGroup)iter.next();
      log.info( "** Calculating payout(s) for promotion: " + promotion.getId() + " of payout type: " + pcPromotion.getPayoutType().getCode() + "for participant: "
          + SalesPayoutStrategyUtil.getReferenceSalesActivity( activities ).getParticipant().getNameLFMNoComma() );

      PayoutCalculationResult payoutCalculationResult = processPayoutGroup( inputActivities, promotionPayoutGroup, salesFacts );

      payoutCalculationResults.add( payoutCalculationResult );

      // We should only use the sales activities fully once.
      if ( payoutCalculationResult.getCalculatedPayout() != null && payoutCalculationResult.getCalculatedPayout().longValue() > 0 )
      {
        Set leftOverActivities = new LinkedHashSet( inputActivities );
        leftOverActivities.removeAll( payoutCalculationResult.getContributingActivities() );

        if ( extractSalesActivities( payoutCalculationResult.getGeneratedActivities() ).isEmpty() && leftOverActivities.isEmpty() )
        {
          break;
        }

        // Set the activities to use for the next pass as the generated
        // activities and the non used sales activities.
        // Make sure we don't include Manager Override Activities
        leftOverActivities.addAll( extractSalesActivities( payoutCalculationResult.getGeneratedActivities() ) );

        inputActivities = leftOverActivities;

      } // if payout is greater than 0

      // only carryover the generated activities from the last group processed.
      // only do this if carryover is enabled for the promotion.
      if ( i < groupSize || !pcPromotion.isPayoutCarryOver() )
      {
        // Make sure we retain Manager Override Activities
        payoutCalculationResult.setGeneratedActivities( extractNonSalesActivities( payoutCalculationResult.getGeneratedActivities() ) );
      }

    } // for groups

    return payoutCalculationResults;
  }

  /**
   * Return all sales activities found in inputActivities
   */
  private Set extractSalesActivities( Set inputActivities )
  {
    Set salesActivities = new LinkedHashSet();
    for ( Iterator iter = inputActivities.iterator(); iter.hasNext(); )
    {
      Activity activity = (Activity)iter.next();
      if ( activity instanceof SalesActivity )
      {
        salesActivities.add( activity );
      }
    }

    return salesActivities;
  }

  /**
   * Return all sales activities found in inputActivities
   */
  private Set extractCarryoverActivities( Set inputActivities )
  {
    Set carryoverSalesActivities = new LinkedHashSet();
    for ( Iterator iter = inputActivities.iterator(); iter.hasNext(); )
    {
      Activity activity = (Activity)iter.next();
      if ( activity instanceof SalesActivity && ( (SalesActivity)activity ).isCarryover() )
      {
        carryoverSalesActivities.add( activity );
      }
    }

    return carryoverSalesActivities;
  }

  /**
   * Return all non-sales activities found in inputActivities
   */
  private Set extractNonSalesActivities( Set inputActivities )
  {
    Set salesActivities = new LinkedHashSet();
    for ( Iterator iter = inputActivities.iterator(); iter.hasNext(); )
    {
      Activity activity = (Activity)iter.next();
      if ( ! ( activity instanceof SalesActivity ) )
      {
        salesActivities.add( activity );
      }
    }

    return salesActivities;
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

    ProductClaimPromotion promotion = (ProductClaimPromotion)referenceSalesActivity.getPromotion();

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
    long promotionGroupFulfilledCount = 0;
    long promotionGroupCarryover = 0;
    int quantitySold = 0;
    int qualifyingQuantitySoldAfterMinQualifier = 0;

    // Might contain duplicates (if RECONSIDER_ACTIVITY_AFTER_PROMO_PAYOUT_MATCH_FOUND is true)
    // so make it a list - this would be trouble when storing journal to activity records
    Set totalMatchingSalesActivities = buildMatchingSalesActivities( originalActivities, promotionPayoutGroup );

    int quantityRequired = promotionPayoutGroup.getQuantity();
    if ( !totalMatchingSalesActivities.isEmpty() )
    {
      // create "promotion payout fulfilled count" value
      quantitySold = SalesPayoutStrategyUtil.getQuantitySoldForAllMatchingActivities( totalMatchingSalesActivities );

      if ( promotionPayoutGroup.isMinimumQualifierUsed() && !promotion.isPayoutCarryOver() && promotionPayoutGroup.isRetroPayout()
          && !SalesPayoutStrategyUtil.isMinQualifierMet( salesFacts, promotionPayoutGroup ) )
      {
        // Handle special case where carryover is off and retro is on.
        // In this case ,process the Minimum Qualifier immediately, since
        // min qual will not be accumulated or saved as pending
        failureOccurred = SalesPayoutStrategyUtil.processMinimumQualifier( promotionPayoutGroup,
                                                                           salesFacts,
                                                                           failureOccurred,
                                                                           referenceSalesActivity,
                                                                           payoutCalculationResult,
                                                                           payoutCalculationAudit,
                                                                           quantitySold );

        // retro on so don't subtract any min qualifier used
        qualifyingQuantitySoldAfterMinQualifier = quantitySold;

      }
      else if ( promotionPayoutGroup.isMinimumQualifierUsed() && !promotionPayoutGroup.isRetroPayout() && !SalesPayoutStrategyUtil.isMinQualifierMet( salesFacts, promotionPayoutGroup ) )
      {
        // Process "retro off" minimimum qualifier after quantity calculation but before payout
        // since
        // in that case,we need to lower the effective quantity sold quantity for payout until the
        // minimum qualifier is met.
        int oldMinQualifierCompleted = SalesPayoutStrategyUtil.getOldMinQualifierCompleted( salesFacts, promotionPayoutGroup );

        failureOccurred = SalesPayoutStrategyUtil.processMinimumQualifier( promotionPayoutGroup,
                                                                           salesFacts,
                                                                           failureOccurred,
                                                                           referenceSalesActivity,
                                                                           payoutCalculationResult,
                                                                           payoutCalculationAudit,
                                                                           quantitySold );

        int newMinQualifierCompleted = payoutCalculationResult.getMinimumQualifierStatus().getCompletedQuantity();
        int quantityUsedToMeetMinQualifier = newMinQualifierCompleted - oldMinQualifierCompleted;

        qualifyingQuantitySoldAfterMinQualifier = quantitySold - quantityUsedToMeetMinQualifier;
      }
      else
      {
        qualifyingQuantitySoldAfterMinQualifier = quantitySold;
      }

      if ( !failureOccurred )
      {
        promotionGroupFulfilledCount = qualifyingQuantitySoldAfterMinQualifier / quantityRequired;
        promotionGroupCarryover = qualifyingQuantitySoldAfterMinQualifier % quantityRequired;

        log.info( "promotionGroupFulfilledCount: " + promotionGroupFulfilledCount );
        log.info( "promotionGroupCarryover: " + promotionGroupCarryover );

        if ( promotionGroupFulfilledCount == 0 )
        {
          failureOccurred = true;
          payoutCalculationAudit.setReason( PayoutCalculationAuditReasonType.TIERED_INSUFFICIENT_QUANTITY,
                                            new String[] { String.valueOf( quantityRequired ), String.valueOf( qualifyingQuantitySoldAfterMinQualifier ) } );
          log.info( "Engine calculation Failure of type(" + payoutCalculationAudit.getReasonType().getCode() + "): " + payoutCalculationAudit.getReasonText() );

        } // if none fulfilled.
      }
    } // if there were matching sales activities.
    else
    {
      // no matching activities found
      failureOccurred = true;
      payoutCalculationAudit.setReason( PayoutCalculationAuditReasonType.TIERED_INSUFFICIENT_QUANTITY, new String[] { String.valueOf( quantityRequired ), "0" } );
      log.info( "Engine calculation Failure of type(" + payoutCalculationAudit.getReasonType().getCode() + "): " + payoutCalculationAudit.getReasonText() );
    }

    // ****
    // ** Generate Output Activities if needed
    Set generatedCarryoverActivities = generateLeftoverActivities( totalMatchingSalesActivities, quantitySold, promotionGroupCarryover, referenceSalesActivity );

    payoutCalculationResult.addAllGeneratedActivities( generatedCarryoverActivities );

    // also add any non-matching carryover activities for passed in activities
    Set nonMatchingCarryoverActivities = extractCarryoverActivities( originalActivities );
    nonMatchingCarryoverActivities.removeAll( totalMatchingSalesActivities );
    payoutCalculationResult.addAllGeneratedActivities( nonMatchingCarryoverActivities );

    // ****
    // ** Calculate payouts.
    if ( !failureOccurred )
    {
      long calculatedPayout = promotionGroupFulfilledCount * promotionPayoutGroup.getSubmitterPayout();

      if ( calculatedPayout == 0 )
      {
        // payout was zero.
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
      failureOccurred = SalesPayoutStrategyUtil
          .processMinimumQualifier( promotionPayoutGroup,
                                    salesFacts,
                                    failureOccurred,
                                    referenceSalesActivity,
                                    payoutCalculationResult,
                                    payoutCalculationAudit,
                                    qualifyingQuantitySoldAfterMinQualifier );
    }

    // If there was a successful payout and the promotion has a manager payout, create a
    // ManagerOverrideActivity
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

    // save all activities that contributed.
    // for the purpose of keeping track of activites that weren't used for this
    // group, we always pass the carryover activities around from group to group.
    // In the case where part of an activity is used for one group and one for
    // another, the contributing activity should still be the original activity,
    // not the carryover activity.
    for ( Iterator iter = totalMatchingSalesActivities.iterator(); iter.hasNext(); )
    {
      SalesActivity matchingSalesActivity = (SalesActivity)iter.next();

      if ( matchingSalesActivity.getOriginalActivity() == null )
      {
        payoutCalculationResult.addContributingActivity( matchingSalesActivity );
      }
      else
      {
        payoutCalculationResult.addContributingActivity( matchingSalesActivity.getOriginalActivity() );
      }
    }

    // ****
    // ** return result object
    return payoutCalculationResult;

  }

  private Set generateLeftoverActivities( Set contributingActivities, long quantitySold, long promotionGroupCarryover, SalesActivity referenceSalesActivity )
  {
    Set generatedActivities = new LinkedHashSet();

    if ( promotionGroupCarryover > 0 )
    {
      // Loop through the contributing activities. Once the quantityApplied to
      // group is reached, the rest of the activities should be carryover.
      long quantityAppliedToGroup = quantitySold - promotionGroupCarryover;
      for ( Iterator iter = contributingActivities.iterator(); iter.hasNext(); )
      {
        SalesActivity contributingActivity = (SalesActivity)iter.next();

        if ( contributingActivity.getQuantity().longValue() > quantityAppliedToGroup )
        {
          long carryOverQuantity = contributingActivity.getQuantity().longValue() - quantityAppliedToGroup;

          // All quantity applied to the Activity
          quantityAppliedToGroup = 0;

          SalesActivity salesCarryoverActivity = new SalesActivity( GuidUtils.generateGuid() );
          salesCarryoverActivity.setSubmissionDate( new Date() );
          salesCarryoverActivity.setProduct( contributingActivity.getProduct() );
          salesCarryoverActivity.setQuantity( new Long( carryOverQuantity ) );
          salesCarryoverActivity.setClaim( contributingActivity.getClaim() );
          salesCarryoverActivity.setPromotion( contributingActivity.getPromotion() );
          salesCarryoverActivity.setParticipant( contributingActivity.getParticipant() );
          salesCarryoverActivity.setNode( contributingActivity.getNode() );
          salesCarryoverActivity.setCarryover( true );
          salesCarryoverActivity.setSubmitter( contributingActivity.isSubmitter() );
          salesCarryoverActivity.setOriginalActivity( contributingActivity );

          generatedActivities.add( salesCarryoverActivity );
        }
        else
        {
          quantityAppliedToGroup -= contributingActivity.getQuantity().longValue();
        }
      } // for contributing activities
    } // if there was carryover for this group
    return generatedActivities;
  }

  private Set buildMatchingSalesActivities( Set originalActivities, PromotionPayoutGroup promotionPayoutGroup )
  {
    Set totalMatchingSalesActivities = new LinkedHashSet();

    // Since there are now multiple groups, we can't assume all products qualify since child proms
    // contain a
    // subset of the parent promos promo payout groups.
    // Also, carryover activities do not always qualify, as they are tied to a product

    for ( Iterator iter = promotionPayoutGroup.getPromotionPayouts().iterator(); iter.hasNext(); )
    {
      PromotionPayout promotionPayout = (PromotionPayout)iter.next();

      Set matchingSalesActivities = SalesPayoutStrategyUtil.findSalesActivitiesWithMatchingProduct( promotionPayout, originalActivities );

      totalMatchingSalesActivities.addAll( matchingSalesActivities );
    }
    return totalMatchingSalesActivities;
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
