/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/CrossSellPayoutStrategy.java,v $
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

import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.audit.SalesPayoutCalculationAudit;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;
import com.biperf.core.exception.BeaconRuntimeException;

/**
 * CrossSellPayoutStrategy.<br/> Payout Strategy for "Cross Sell" promotion payout type.
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
public class CrossSellPayoutStrategy extends AbstractPayoutStrategy
{
  private static final Log log = LogFactory.getLog( CrossSellPayoutStrategy.class );

  // The BA's went back and forth about this a few times, so leaving as a choice.
  private static final boolean RECONSIDER_ACTIVITY_AFTER_PROMO_PAYOUT_MATCH_FOUND = false;

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

    // Make copy to allow us to remove elements while processing this payout group.
    Set localActivities = new LinkedHashSet( originalActivities );

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

    List promotionPayoutsOrderedByProdCatHierarchy = orderByProdCatHierarchy( promotionPayoutGroup.getPromotionPayouts() );
    for ( Iterator iter = promotionPayoutsOrderedByProdCatHierarchy.iterator(); iter.hasNext(); )
    {
      PromotionPayout promotionPayout = (PromotionPayout)iter.next();
      int quantityRequired = promotionPayout.getQuantity();

      Set matchingSalesActivities = SalesPayoutStrategyUtil.findSalesActivitiesWithMatchingProduct( promotionPayout, localActivities );
      if ( !matchingSalesActivities.isEmpty() )
      {
        if ( !RECONSIDER_ACTIVITY_AFTER_PROMO_PAYOUT_MATCH_FOUND )
        {
          localActivities.removeAll( matchingSalesActivities );
        }

        // Store all activites that contributed.
        payoutCalculationResult.addAllContributingActivities( matchingSalesActivities );

        // create "promotion payout fulfilled count" value
        long quantitySold = SalesPayoutStrategyUtil.getQuantitySoldForAllMatchingActivities( matchingSalesActivities );

        Long promotionPayoutFulfilledCount = new Long( quantitySold / quantityRequired );

        log.info( "promotionPayout " + promotionPayout.getId() + "  fulfilled " + promotionPayoutFulfilledCount + " times" );

        if ( promotionPayoutFulfilledCount.longValue() == 0 )
        {
          // failureOccurred = true;
          payoutCalculationAudit.setReason( PayoutCalculationAuditReasonType.CROSS_SELL_INSUFFICIENT_QUANTITY,
                                            new String[] { promotionPayout.getRequiredProductOrProductCategoryName(), String.valueOf( quantitySold ), String.valueOf( quantityRequired ) } );
          log.info( "Engine calculation Failure of type(" + payoutCalculationAudit.getReasonType().getCode() + "): " + payoutCalculationAudit.getReasonText() );
          // break;
        }
        else
        {
          promotionPayoutFulfilledCounts.add( promotionPayoutFulfilledCount );
        }
      }
      else
      {
        // failureOccurred = true;
        payoutCalculationAudit.setReason( PayoutCalculationAuditReasonType.CROSS_SELL_UNMATCHED_PRODUCT, new String[] { promotionPayout.getRequiredProductOrProductCategoryName() } );
        log.info( "Engine calculation Failure of type(" + payoutCalculationAudit.getReasonType().getCode() + "): " + payoutCalculationAudit.getReasonText() );
        // break;
      }
    }

    if ( !promotionPayoutFulfilledCounts.isEmpty() )
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
    else
    {
      failureOccurred = true;
    }

    // If there was a successful payout and the promotion has a manager payout, create a
    // ManagerOverrideActivity
    ProductClaimPromotion promotion = (ProductClaimPromotion)payoutCalculationResult.getPromotionPayoutGroup().getPromotion();
    if ( !failureOccurred && promotion.isPayoutManager() && salesFacts.getManager() != null )
    {
      ManagerOverrideActivity activity = new ManagerOverrideActivity( referenceSalesActivity.getClaim(), payoutCalculationResult.getCalculatedPayout() );
      activity.setPosted( false );
      activity.setNode( referenceSalesActivity.getNode() );
      activity.setPromotion( referenceSalesActivity.getPromotion() );
      activity.setParticipant( salesFacts.getManager() );
      activity.setSubmissionDate( new Date() );
      payoutCalculationResult.addGeneratedActivity( activity );
    }

    // ****
    // ** return result object
    return payoutCalculationResult;

  }

  /**
   * Order the promotion payouts with most specific product/category definition being first, as in
   * the following manner: <br/>
   * <ol>
   * <li>First, promotion payouts with a non-null product association</li>
   * <li>Second, promotion payouts with a non-null subcategory association</li>
   * <li>Third, promotion payouts with a non-null master product category association</li>
   * </ol>
   * This fulfills the business requirement that claimed products get applied only to one promotion
   * payout for a given payout group, with order of precendence being: product, subcategory, master
   * category. This come up if the promotion group require a) 5 pizza products, and b) 5 frozen food
   * category products. In the case that the claim contains 5 pizzas, the claimed pizza will only
   * apply to the a) requirement.
   * 
   * @param promotionPayouts
   * @return List
   */
  private List orderByProdCatHierarchy( List promotionPayouts )
  {
    List reorderedList = new ArrayList( promotionPayouts.size() );
    List productPromotionPayouts = new ArrayList();
    List productSubcategoryPromotionPayouts = new ArrayList();
    List productCategoryPromotionPayouts = new ArrayList();

    for ( Iterator iter = promotionPayouts.iterator(); iter.hasNext(); )
    {
      PromotionPayout promotionPayout = (PromotionPayout)iter.next();
      if ( promotionPayout.getProduct() != null )
      {
        productPromotionPayouts.add( promotionPayout );
      }
      else if ( promotionPayout.getProductCategory().getParentProductCategory() != null )
      {
        productSubcategoryPromotionPayouts.add( promotionPayout );
      }
      else
      {
        productCategoryPromotionPayouts.add( promotionPayout );
      }
    }

    reorderedList.addAll( productPromotionPayouts );
    reorderedList.addAll( productSubcategoryPromotionPayouts );
    reorderedList.addAll( productCategoryPromotionPayouts );

    return reorderedList;
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

}
