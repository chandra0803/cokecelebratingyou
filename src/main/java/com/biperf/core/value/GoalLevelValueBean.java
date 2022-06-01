/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/GoalLevelValueBean.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.biperf.awardslinqDataRetriever.client.MerchLevelProduct;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.UserManager;

/**
 * GoalLevelValueBean.
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
 * <td>meadows</td>
 * <td>Feb 2, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class GoalLevelValueBean implements Serializable
{
  private int sequenceNumber;
  private AbstractGoalLevel goalLevel;
  private BigDecimal calculatedGoalAmount;
  private BigDecimal calculatedIncrementAmount;
  private BigDecimal calculatedMinimumQualifier;
  private BigDecimal calculatedAchievementAmount;
  private boolean isManagerOverride;
  private BigDecimal baseAmount;
  // list of MerchLevelProducts for this Goal Level.
  private List products;
  // select MerchLevel Product
  private MerchLevelProduct selectedProduct;
  // URL to invoke AwardLinq
  private String shoppingURL;
  // cp related variables
  private BigDecimal cpCalculatedThreshold;
  private BigDecimal cpCalculatedIncrementAmount;
  private ChallengePointPromotion cpPromotion;
  private GoalQuestPromotion gqPromotion;
  private BigDecimal amountToAchieve;
  private PaxGoal paxGoal;
  private Long totalBasicAwardEarned;
  private boolean showProgramRules;

  public BigDecimal getCalculatedAchievementAmount()
  {
    return calculatedAchievementAmount;
  }

  public void setCalculatedAchievementAmount( BigDecimal calculatedAchievementAmount )
  {
    this.calculatedAchievementAmount = calculatedAchievementAmount;
  }

  public BigDecimal getCalculatedGoalAmount()
  {
    return calculatedGoalAmount;
  }

  public void setCalculatedGoalAmount( BigDecimal calculatedGoalAmount )
  {
    this.calculatedGoalAmount = calculatedGoalAmount;
  }

  public BigDecimal getCalculatedIncrementAmount()
  {
    return calculatedIncrementAmount;
  }

  public void setCalculatedIncrementAmount( BigDecimal calculatedIncrementAmount )
  {
    this.calculatedIncrementAmount = calculatedIncrementAmount;
  }

  public AbstractGoalLevel getGoalLevel()
  {
    return goalLevel;
  }

  public void setGoalLevel( AbstractGoalLevel goalLevel )
  {
    this.goalLevel = goalLevel;
  }

  public int getSequenceNumber()
  {
    return sequenceNumber;
  }

  public void setSequenceNumber( int sequenceNumber )
  {
    this.sequenceNumber = sequenceNumber;
  }

  public boolean isManagerOverride()
  {
    return isManagerOverride;
  }

  public void setManagerOverride( boolean isManagerOverride )
  {
    this.isManagerOverride = isManagerOverride;
  }

  public BigDecimal getBaseAmount()
  {
    return baseAmount;
  }

  public void setBaseAmount( BigDecimal baseAmount )
  {
    this.baseAmount = baseAmount;
  }

  public BigDecimal getCalculatedMinimumQualifier()
  {
    return calculatedMinimumQualifier;
  }

  public void setCalculatedMinimumQualifier( BigDecimal calculatedMinimumQualifier )
  {
    this.calculatedMinimumQualifier = calculatedMinimumQualifier;
  }

  public List getProducts()
  {
    return products;
  }

  public void setProducts( List products )
  {
    this.products = products;
  }

  public MerchLevelProduct getSelectedProduct()
  {
    return selectedProduct;
  }

  public void setSelectedProduct( MerchLevelProduct selectedProduct )
  {
    this.selectedProduct = selectedProduct;
  }

  /**
   * Get the first product in the list of products
   * @return
   */
  public MerchLevelProduct getFirstProduct()
  {
    if ( this.products != null && this.products.size() > 0 )
    {
      return (MerchLevelProduct)this.products.get( 0 );
    }
    return null;
  }

  /**
   * Get the award value for the goal level
   * @return
   */
  public BigDecimal getAwardValue()
  {
    if ( this.goalLevel != null )
    {
      if ( this.goalLevel.isManagerOverrideGoalLevel() )
      {
        return this.getGoalLevel().getManagerAward();
      }
      return ( (GoalLevel)this.getGoalLevel() ).getAward();
    }
    return null;
  }

  public String getShoppingURL()
  {
    return shoppingURL;
  }

  public void setShoppingURL( String shoppingURL )
  {
    this.shoppingURL = shoppingURL;
  }

  public BigDecimal getCpCalculatedThreshold()
  {
    return cpCalculatedThreshold;
  }

  public void setCpCalculatedThreshold( BigDecimal cpCalculatedThreshold )
  {
    this.cpCalculatedThreshold = cpCalculatedThreshold;
  }

  public BigDecimal getCpCalculatedIncrementAmount()
  {
    return cpCalculatedIncrementAmount;
  }

  public void setCpCalculatedIncrementAmount( BigDecimal cpCalculatedIncrementAmount )
  {
    this.cpCalculatedIncrementAmount = cpCalculatedIncrementAmount;
  }

  public ChallengePointPromotion getCpPromotion()
  {
    return cpPromotion;
  }

  public void setCpPromotion( ChallengePointPromotion cpPromotion )
  {
    this.cpPromotion = cpPromotion;
  }

  public GoalQuestPromotion getGqPromotion()
  {
    return gqPromotion;
  }

  public void setGqPromotion( GoalQuestPromotion gqPromotion )
  {
    this.gqPromotion = gqPromotion;
  }

  public BigDecimal getAmountToAchieve()
  {
    return amountToAchieve;
  }

  public void setAmountToAchieve( BigDecimal amountToAchieve )
  {
    this.amountToAchieve = amountToAchieve;
  }

  public PaxGoal getPaxGoal()
  {
    return paxGoal;
  }

  public void setPaxGoal( PaxGoal paxGoal )
  {
    this.paxGoal = paxGoal;
  }

  public Long getTotalBasicAwardEarned()
  {
    return totalBasicAwardEarned;
  }

  public void setTotalBasicAwardEarned( Long totalBasicAwardEarned )
  {
    this.totalBasicAwardEarned = totalBasicAwardEarned;
  }

  public boolean isShowProgramRules()
  {
    return showProgramRules;
  }

  public void setShowProgramRules( boolean showProgramRules )
  {
    this.showProgramRules = showProgramRules;
  }

  public String getCalculatedGoalAmtLocaleBased()
  {
    if ( this.calculatedGoalAmount != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getCalculatedGoalAmount(), gqPromotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getCalculatedIncrementAmountLocaleBased()
  {
    if ( this.getCalculatedIncrementAmount() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getCalculatedIncrementAmount(), gqPromotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getCalculatedMinimumQualifierLocaleBased()
  {
    if ( this.getCalculatedMinimumQualifier() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getCalculatedMinimumQualifier(), gqPromotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getCalculatedAchievementAmountLocaleBased()
  {
    if ( this.getCalculatedAchievementAmount() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getCalculatedAchievementAmount(), gqPromotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getBaseAmountLocaleBased()
  {
    if ( this.getBaseAmount() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getBaseAmount(), gqPromotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getMaximumPointsLocaleBased()
  {
    if ( ( (GoalLevel)goalLevel ).getMaximumPoints() != null && ( (GoalLevel)goalLevel ).getMaximumPoints().longValue() > 0 )
    {
      return NumberFormatUtil.getLocaleBasedNumberFormat( ( (GoalLevel)goalLevel ).getMaximumPoints().longValue() );
    }
    else
    {
      return "";
    }
  }

  public String getBonusAwardLocaleBased()
  {
    if ( ( (GoalLevel)goalLevel ).getBonusAward() != null && ( (GoalLevel)goalLevel ).getBonusAward().longValue() > 0 )
    {
      return NumberFormatUtil.getLocaleBasedNumberFormat( ( (GoalLevel)goalLevel ).getBonusAward().longValue() );
    }
    else
    {
      return "";
    }
  }

  public String getAwardLocaleBased()
  {
    if ( ( (GoalLevel)goalLevel ).getAward() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( ( (GoalLevel)goalLevel ).getAward(), 0, UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

}
