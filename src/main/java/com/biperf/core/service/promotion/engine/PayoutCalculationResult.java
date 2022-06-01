/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/PayoutCalculationResult.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

import com.biperf.core.domain.activity.Activity;
import com.biperf.core.domain.activity.SalesActivity;
import com.biperf.core.domain.audit.PayoutCalculationAudit;
import com.biperf.core.domain.claim.MinimumQualifierStatus;
import com.biperf.core.domain.journal.Journal;
import com.biperf.core.domain.promotion.PromoMerchProgramLevel;
import com.biperf.core.domain.promotion.PromotionPayoutGroup;

/**
 * PayoutCalculationResult.
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
 * <td>Aug 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class PayoutCalculationResult implements Serializable
{
  private Long calculatedPayout;
  private BigDecimal calculatedCashPayout;
  private Journal journal;
  private PromotionPayoutGroup promotionPayoutGroup;

  private MinimumQualifierStatus minimumQualifierStatus;

  private PayoutCalculationAudit payoutCalculationAudit;

  private PromoMerchProgramLevel promoMerchProgramLevel;

  private boolean isFileLoadDeposit = false; //bug 73458
  
  /**
   * The activities used to calculate the payout.
   */
  private Set contributingActivities = new LinkedHashSet();

  /**
   * The activities that result from calculating the payout.
   */
  private Set generatedActivities = new LinkedHashSet();

  /**
   * Returns true if the promotion engine successfully calculated a non-zero payout; returns false
   * otherwise.
   * 
   * @return true if the promotion engine successfully calculated a non-zero payout; returns false
   *         otherwise.
   */
  public boolean isCalculationSuccessful()
  {
    // We want 0 amount journal records for participants that have opted out - to leave a record
    // trail
    if ( payoutCalculationAudit != null && payoutCalculationAudit.getParticipant() != null && payoutCalculationAudit.getParticipant().getOptOutAwards() )
    {
      return calculatedPayout != null;
    }
    else
    {
      return calculatedPayout != null && calculatedPayout.longValue() > 0;
    }
  }

  public boolean isCashCalculationSuccessful()
  {
    // We want 0 amount journal records for participants that have opted out - to leave a record
    // trail
    if ( payoutCalculationAudit != null && payoutCalculationAudit.getParticipant() != null && payoutCalculationAudit.getParticipant().getOptOutAwards() )
    {
      return calculatedCashPayout != null;
    }
    else
    {
      return calculatedCashPayout != null && calculatedCashPayout.longValue() > 0;
    }
  }

  public boolean isMerchCalculationSuccessful()
  {
    return promoMerchProgramLevel != null;
  }

  /**
   * Returns the payout.
   * 
   * @return the payout.
   */
  public Long getCalculatedPayout()
  {
    return calculatedPayout;
  }

  /**
   * Sets the payout.
   * 
   * @param calculatedPayout the payout.
   */
  public void setCalculatedPayout( Long calculatedPayout )
  {
    this.calculatedPayout = calculatedPayout;
  }

  public BigDecimal getCalculatedCashPayout()
  {
    return calculatedCashPayout;
  }

  public void setCalculatedCashPayout( BigDecimal calculatedCashPayout )
  {
    this.calculatedCashPayout = calculatedCashPayout;
  }

  /**
   * Returns the promotion payout group used to calculate the payout.
   * 
   * @return the promotion payout group used to calculate the payout.
   */
  public PromotionPayoutGroup getPromotionPayoutGroup()
  {
    return promotionPayoutGroup;
  }

  /**
   * Sets the promotion payout group used to calculate the payout.
   * 
   * @param promotionPayoutGroup the promotion payout group used to calculate the payout.
   */
  public void setPromotionPayoutGroup( PromotionPayoutGroup promotionPayoutGroup )
  {
    this.promotionPayoutGroup = promotionPayoutGroup;
  }

  /**
   * Returns the activities that result from calculating the payout.
   * 
   * @return the activities that result from calculating the payout, as a <code>Set</code> of
   *         {@link SalesActivity} objects.
   */
  public Set getGeneratedActivities()
  {
    return generatedActivities;
  }

  /**
   * Sets the activities that result from calculating the payout.
   * 
   * @param generatedActivities the activities that result from calculating the payout.
   */
  public void setGeneratedActivities( Set generatedActivities )
  {
    this.generatedActivities = generatedActivities;
  }

  /**
   * Adds one activity to the set of generated activities.
   * 
   * @param generatedActivity an activity.
   */
  public void addGeneratedActivity( Activity generatedActivity )
  {
    generatedActivities.add( generatedActivity );
  }

  /**
   * Adds many activities to the set of generated activities.
   * 
   * @param generatedActivities many activities.
   */
  public void addAllGeneratedActivities( Set generatedActivities )
  {
    this.generatedActivities.addAll( generatedActivities );
  }

  /**
   * Returns the activities used to calculate the payout.
   * 
   * @return the activities used to calculate the payout, as a <code>Set</code> of
   *         {@link SalesActivity} objects.
   */
  public Set getContributingActivities()
  {
    return contributingActivities;
  }

  /**
   * Sets the activities used to calculate the payout.
   * 
   * @param contributingActivities the activities used to calculate the payout.
   */
  public void setContributingActivities( Set contributingActivities )
  {
    this.contributingActivities = contributingActivities;
  }

  /**
   * Adds one activity to the set of contributing activities.
   * 
   * @param contributingActivity an activity.
   */
  public void addContributingActivity( Activity contributingActivity )
  {
    contributingActivities.add( contributingActivity );
  }

  /**
   * Adds many activities to the set of contributing activities.
   * 
   * @param contributingActivities many activities.
   */
  public void addAllContributingActivities( Set contributingActivities )
  {
    this.contributingActivities.addAll( contributingActivities );
  }

  /**
   * @return value of payoutCalculationAudit property
   */
  public PayoutCalculationAudit getPayoutCalculationAudit()
  {
    return payoutCalculationAudit;
  }

  /**
   * @param payoutCalculationAudit value for payoutCalculationAudit property
   */
  public void setPayoutCalculationAudit( PayoutCalculationAudit payoutCalculationAudit )
  {
    this.payoutCalculationAudit = payoutCalculationAudit;
  }

  public Journal getJournal()
  {
    return journal;
  }

  public void setJournal( Journal journal )
  {
    this.journal = journal;
  }

  /**
   * @return value of minimumQualifierStatus property
   */
  public MinimumQualifierStatus getMinimumQualifierStatus()
  {
    return minimumQualifierStatus;
  }

  /**
   * @param minimumQualifierStatus value for minimumQualifierStatus property
   */
  public void setMinimumQualifierStatus( MinimumQualifierStatus minimumQualifierStatus )
  {
    this.minimumQualifierStatus = minimumQualifierStatus;
  }

  public PromoMerchProgramLevel getPromoMerchProgramLevel()
  {
    return promoMerchProgramLevel;
  }

  public void setPromoMerchProgramLevel( PromoMerchProgramLevel promoMerchProgramLevel )
  {
    this.promoMerchProgramLevel = promoMerchProgramLevel;
  }

  public boolean isFileLoadDeposit()
  {
    return isFileLoadDeposit;
  }

  public void setFileLoadDeposit( boolean isFileLoadDeposit )
  {
    this.isFileLoadDeposit = isFileLoadDeposit;
  }

}
