/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.ManagerOverrideActivity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.audit.SalesPayoutCalculationAudit;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.domain.enums.PayoutCalculationAuditReasonType;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.product.Product;
import com.biperf.core.domain.product.ProductCategory;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.PromotionPayout;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;

/**
 * SalesPayoutStrategyUtil.
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
 * <td>Aug 10, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SalesPayoutStrategyUtil
{
  /**
   * Logger for this class
   */
  private static final Logger log = Logger.getLogger( SalesPayoutStrategyUtil.class );

  /**
   * protected since static-method-only Util class.
   */
  protected SalesPayoutStrategyUtil()
  {
    super();
  }

  /**
   * Sum the quantity sold for each matching sales activity.
   * 
   * @param matchingSalesActivities
   * @return int
   */
  public static int getQuantitySoldForAllMatchingActivities( Collection matchingSalesActivities )
  {
    int totalQuantitySold = 0;
    for ( Iterator iterator = matchingSalesActivities.iterator(); iterator.hasNext(); )
    {
      SalesActivity matchingActivity = (SalesActivity)iterator.next();
      long activityQuantitySold = matchingActivity.getQuantity().longValue();
      totalQuantitySold += activityQuantitySold;
    }
    return totalQuantitySold;
  }

  public static Set findSalesActivitiesWithMatchingProduct( PromotionPayout promotionPayout, Set activities )
  {
    Set matchingActivities = new LinkedHashSet();

    for ( Iterator iter1 = activities.iterator(); iter1.hasNext(); )
    {
      SalesActivity activity = (SalesActivity)iter1.next();
      if ( SalesPayoutStrategyUtil.isProductSoldApplicableToPromotionPayout( activity.getProduct(), promotionPayout ) )
      {
        matchingActivities.add( activity );
      }
    }
    return matchingActivities;
  }

  /**
   * @param soldProduct
   * @param promotionPayout
   * @return boolean
   */
  public static boolean isProductSoldApplicableToPromotionPayout( Product soldProduct, PromotionPayout promotionPayout )
  {
    // PromotionPayout either refers to a required product or a required category, depending
    // on whether PromotionPayout product is null.
    Product promotionPayoutProduct = promotionPayout.getProduct();
    if ( promotionPayoutProduct != null )
    {
      // Does productSold equal promo payout product?
      if ( promotionPayoutProduct.equals( soldProduct ) )
      {
        return true;
      }
    }
    else
    {
      // Does productSold's category equal the promo payout product category?
      ProductCategory productSoldCategory = soldProduct.getProductCategory();
      ProductCategory promotionPayoutProductCategory = promotionPayout.getProductCategory();
      if ( productSoldCategory == null || promotionPayoutProductCategory == null )
      {
        return false;
      }

      if ( productSoldCategory.equals( promotionPayoutProductCategory ) )
      {
        return true;
      }

      // is productSold's category a subcategory of the promotionPayoutProductCategory.
      if ( promotionPayoutProductCategory.getSubcategories().contains( productSoldCategory ) )
      {
        return true;
      }
    }

    return false;
  }

  /**
   * Return first Sales (i.e., non-carryover) SalesActivity found. Useful to extract claim and
   * participant, since all sales activities should have the same claim and participant.
   * 
   * @param originalActivities
   * @return SalesActivity
   */
  public static SalesActivity getReferenceSalesActivity( Set originalActivities )
  {
    SalesActivity referenceActivity = null;
    SalesActivity carryoverActivity = null;

    for ( Iterator iter = originalActivities.iterator(); iter.hasNext(); )
    {
      Activity activity = (Activity)iter.next();
      if ( ! ( activity instanceof ManagerOverrideActivity ) )
      {
        SalesActivity salesActivity = (SalesActivity)activity;
        if ( salesActivity.isCarryover() )
        {
          carryoverActivity = salesActivity;
        }
        else
        {
          referenceActivity = salesActivity;
          break;
        }
      }
    }

    // if referenceActivity is null then all were carryover, so just use
    // carryover as referenceActivity.
    if ( referenceActivity == null )
    {
      referenceActivity = carryoverActivity;
    }

    return referenceActivity;
  }

  public static boolean processMinimumQualifier( PromotionPayoutGroup promotionPayoutGroup,
                                                 SalesFacts salesFacts,
                                                 boolean failureOccurred,
                                                 SalesActivity referenceSalesActivity,
                                                 PayoutCalculationResult payoutCalculationResult,
                                                 SalesPayoutCalculationAudit payoutCalculationAudit,
                                                 int qualifyingQuantitySold )
  {
    Participant activityParticipant = referenceSalesActivity.getParticipant();
    Map minQualifierStatusByPromoPayoutGroup = salesFacts.getMinQualifierStatusByPromoPayoutGroup();
    if ( referenceSalesActivity.isSubmitter() )
    {
      // ** Update completed quantity and "met" value if needed
      MinimumQualifierStatus minimumQualifierStatus = (MinimumQualifierStatus)minQualifierStatusByPromoPayoutGroup.get( promotionPayoutGroup );

      if ( minimumQualifierStatus == null )
      {
        minimumQualifierStatus = new MinimumQualifierStatus();
        minimumQualifierStatus.setSubmitter( activityParticipant );
        minimumQualifierStatus.setPromotionPayoutGroup( promotionPayoutGroup );
        minQualifierStatusByPromoPayoutGroup.put( promotionPayoutGroup, minimumQualifierStatus );
      }
      payoutCalculationResult.setMinimumQualifierStatus( minimumQualifierStatus );

      // Calculate minimumQualifierStatus if qualifier not already met
      if ( !minimumQualifierStatus.isMinQualifierMet() )
      {
        int quantityNotYetCompleted = promotionPayoutGroup.getMinimumQualifier().intValue() - minimumQualifierStatus.getCompletedQuantity();

        if ( qualifyingQuantitySold >= quantityNotYetCompleted )
        {
          // Min qualifier fulfilled, set completed value at the required value.
          minimumQualifierStatus.setCompletedQuantity( promotionPayoutGroup.getMinimumQualifier().intValue() );
          minimumQualifierStatus.setMinQualifierMet( true );
        }
        else
        {
          ProductClaimPromotion promotion = (ProductClaimPromotion)promotionPayoutGroup.getPromotion();
          // required quantity not yet met. Set new completed quantity, unless tiered with
          // carryover off.
          if ( !isPromotionTieredWithNoCarryover( promotion ) )
          {
            minimumQualifierStatus.setCompletedQuantity( qualifyingQuantitySold + minimumQualifierStatus.getCompletedQuantity() );
          }
          else
          {
            // tiered promo with no carryover with
            failureOccurred = true;
            payoutCalculationResult.setCalculatedPayout( null );

            payoutCalculationAudit.setReason( PayoutCalculationAuditReasonType.MIN_QUALIFIER_NOT_EXCEEDED_CARRYOVER_OFF_FAILURE,
                                              new String[] { String.valueOf( promotionPayoutGroup.getMinimumQualifier() ), String.valueOf( qualifyingQuantitySold ) } );
            log.info( "Engine calculation Failure of type(" + payoutCalculationAudit.getReasonType().getCode() + "): " + payoutCalculationAudit.getReasonText() );
          }
        }


        // if minimum qualifier is 1 in the promotion setup, points are not credited.
        // so added this check.
        // if minimum qualifier is 1 in the promotion setup there is nothing to check about noQualifingQuantityLeft.
        boolean noQualifingQuantityLeft = false;
        if ( promotionPayoutGroup.getMinimumQualifier().intValue() == 1 )
        {
          noQualifingQuantityLeft = qualifyingQuantitySold < quantityNotYetCompleted;
        }
        else
        {
          noQualifingQuantityLeft = qualifyingQuantitySold <= quantityNotYetCompleted;
        }
        if ( !failureOccurred && !promotionPayoutGroup.isRetroPayout() && noQualifingQuantityLeft )
        {
          failureOccurred = true;
          payoutCalculationResult.setCalculatedPayout( null );

          payoutCalculationAudit.setReason( PayoutCalculationAuditReasonType.MIN_QUALIFIER_NOT_EXCEEDED_RETRO_OFF_FAILURE,
                                            new String[] { String.valueOf( promotionPayoutGroup.getMinimumQualifier() ), String.valueOf( minimumQualifierStatus.getCompletedQuantity() ) } );
          log.info( "Engine calculation Failure of type(" + payoutCalculationAudit.getReasonType().getCode() + "): " + payoutCalculationAudit.getReasonText() );
        }
      }

    }
    else
    {
      // team member
      boolean submitterMinimumQualifierMet = isMinQualifierMet( salesFacts, promotionPayoutGroup );

      if ( promotionPayoutGroup.isMinimumQualifierUsed() && !promotionPayoutGroup.isRetroPayout() )
      {
        if ( !submitterMinimumQualifierMet )
        {
          failureOccurred = true;
          payoutCalculationResult.setCalculatedPayout( null );

          payoutCalculationAudit.setReason( PayoutCalculationAuditReasonType.MIN_QUALIFIER_NOT_EXCEEDED_RETRO_OFF_TEAM_MEMBER_FAILURE, null );
          log.info( "Engine calculation Failure of type(" + payoutCalculationAudit.getReasonType().getCode() + "): " + payoutCalculationAudit.getReasonText() );
        }
      }

      // Create simplified team member version of MinimumQualifierStatus to indicate
      // submitter's min qualifier "met/not met" status
      MinimumQualifierStatus minimumQualifierStatus = new MinimumQualifierStatus();
      minimumQualifierStatus.setMinQualifierMet( submitterMinimumQualifierMet );
      payoutCalculationResult.setMinimumQualifierStatus( minimumQualifierStatus );
    }

    return failureOccurred;
  }

  private static boolean isPromotionTieredWithNoCarryover( ProductClaimPromotion promotion )
  {
    return promotion.getPayoutType().getCode().equals( PromotionPayoutType.TIERED ) && !promotion.isPayoutCarryOver();
  }

  /**
   * @param salesFacts
   * @param promotionPayoutGroup
   */
  public static boolean isMinQualifierMet( SalesFacts salesFacts, PromotionPayoutGroup promotionPayoutGroup )
  {
    boolean minQualifierMet = false;
    MinimumQualifierStatus minimumQualifierStatus = (MinimumQualifierStatus)salesFacts.getMinQualifierStatusByPromoPayoutGroup().get( promotionPayoutGroup );

    if ( minimumQualifierStatus != null )
    {
      minQualifierMet = minimumQualifierStatus.isMinQualifierMet();
    }

    return minQualifierMet;
  }

  /**
   * @param salesFacts
   * @param promotionPayoutGroup
   */
  public static int getOldMinQualifierCompleted( SalesFacts salesFacts, PromotionPayoutGroup promotionPayoutGroup )
  {
    int oldMinQualifierCompleted = 0;
    MinimumQualifierStatus minimumQualifierStatus = (MinimumQualifierStatus)salesFacts.getMinQualifierStatusByPromoPayoutGroup().get( promotionPayoutGroup );

    if ( minimumQualifierStatus != null )
    {
      oldMinQualifierCompleted = minimumQualifierStatus.getCompletedQuantity();
    }

    return oldMinQualifierCompleted;
  }
}
