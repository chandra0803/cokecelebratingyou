/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/value/ChallengepointPaxValueBean.java,v $
 */

package com.biperf.core.value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.biperf.awardslinqDataRetriever.client.MerchLevelProduct;
import com.biperf.core.domain.challengepoint.ChallengepointAward;
import com.biperf.core.domain.enums.BaseUnitPosition;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.utils.DateUtils;
import com.biperf.core.utils.NumberFormatUtil;
import com.biperf.core.utils.UserManager;

/**
 * Object that holds participant's info in regards to a Challengepoint Promotion..
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
 * <td>babu</td>
 * <td>July 9, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 * 
 */
public class ChallengepointPaxValueBean implements Serializable
{
  private static final long serialVersionUID = 1L;

  private ChallengePointPromotion promotion;

  private GoalLevel goalLevel;
  private ChallengepointAward challengepointAward;

  private PaxGoal paxGoal;
  private BigDecimal baseAmount;

  // amount need to be reached to achieve Challengepoint
  private BigDecimal amountToAchieve;

  // Increment progress for perqs
  private BigDecimal calculatedIncrementAmount;

  private BigDecimal calculatedThreshold;

  // Achieved amount - current value
  private BigDecimal calculatedAchievementAmount;

  // list of MerchLevelProducts for this selected Level.
  private List products;

  // list of awadrs for this user & promotion.
  private List<ChallengepointAward> awards;

  // %Achieved towards challengepoint
  private BigDecimal percentAchieved;

  // URL to invoke AwardLinq
  private String shoppingURL;

  // basic earned
  private Long totalBasicAwardEarned = new Long( 0 );

  private Long challengepointAwardEarned = new Long( 0 );

  // earned
  private Long totalAwardEarned = new Long( 0 );

  // deposited
  private Long totalAwardDeposited = new Long( 0 );

  // deposited
  private Long interimAwardDeposited = new Long( 0 );

  // earned not deposited
  private Long totalAwardEarnedNotDeposited = new Long( 0 );

  // Date progress information is loaded
  private Date progressSubmissionDate;

  private String progressSubmissionDateDisplay;

  private boolean showProgramRules;

  public boolean isShowProgramRules()
  {
    return showProgramRules;
  }

  public void setShowProgramRules( boolean showProgramRules )
  {
    this.showProgramRules = showProgramRules;
  }

  public String getProgressSubmissionDateDisplay()
  {
    if ( progressSubmissionDate != null )
    {
      progressSubmissionDateDisplay = DateUtils.toDisplayString( progressSubmissionDate );
    }
    return progressSubmissionDateDisplay;
  }

  public Long getChallengepointAwardEarned()
  {
    return challengepointAwardEarned;
  }

  public void setChallengepointAwardEarned( Long challengepointAwardEarned )
  {
    this.challengepointAwardEarned = challengepointAwardEarned;
  }

  public Long getTotalAwardEarned()
  {
    return totalAwardEarned;
  }

  public void setTotalAwardEarned( Long totalAwardEarned )
  {
    this.totalAwardEarned = totalAwardEarned;
  }

  public Date getProgressSubmissionDate()
  {
    return progressSubmissionDate;
  }

  public void setProgressSubmissionDate( Date progressSubmissionDate )
  {
    this.progressSubmissionDate = progressSubmissionDate;
  }

  public PaxGoal getPaxGoal()
  {
    return paxGoal;
  }

  public void setPaxGoal( PaxGoal paxGoal )
  {
    this.paxGoal = paxGoal;
  }

  public Long getTotalAwardDeposited()
  {
    return totalAwardDeposited;
  }

  public void setTotalAwardDeposited( Long totalAwardDeposited )
  {
    this.totalAwardDeposited = totalAwardDeposited;
  }

  public Long getTotalBasicAwardEarned()
  {
    return totalBasicAwardEarned;
  }

  public void setTotalBasicAwardEarned( Long totalAwardEarned )
  {
    this.totalBasicAwardEarned = totalAwardEarned;
  }

  public Long getTotalAwardEarnedNotDeposited()
  {
    return totalAwardEarnedNotDeposited;
  }

  public void setTotalAwardEarnedNotDeposited( Long totalAwardEarnedNotDeposited )
  {
    this.totalAwardEarnedNotDeposited = totalAwardEarnedNotDeposited;
  }

  public BigDecimal getCalculatedAchievementAmount()
  {
    return calculatedAchievementAmount;
  }

  public void setCalculatedAchievementAmount( BigDecimal calculatedAchievementAmount )
  {
    this.calculatedAchievementAmount = calculatedAchievementAmount;
  }

  public BigDecimal getCalculatedIncrementAmount()
  {
    return calculatedIncrementAmount;
  }

  public void setCalculatedIncrementAmount( BigDecimal calculatedIncrementAmount )
  {
    this.calculatedIncrementAmount = calculatedIncrementAmount;
  }

  public BigDecimal getBaseAmount()
  {
    return baseAmount;
  }

  public void setBaseAmount( BigDecimal baseAmount )
  {
    this.baseAmount = baseAmount;
  }

  public List getProducts()
  {
    return products;
  }

  public void setProducts( List products )
  {
    this.products = products;
  }

  /**
   * Get the first product in the list of products
   * 
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

  public String getShoppingURL()
  {
    return shoppingURL;
  }

  public void setShoppingURL( String shoppingURL )
  {
    this.shoppingURL = shoppingURL;
  }

  public BigDecimal getCalculatedThreshold()
  {
    return calculatedThreshold;
  }

  public void setCalculatedThreshold( BigDecimal calculatedThreshold )
  {
    this.calculatedThreshold = calculatedThreshold;
  }

  public GoalLevel getGoalLevel()
  {
    return goalLevel;
  }

  public void setGoalLevel( GoalLevel goalLevel )
  {
    this.goalLevel = goalLevel;
  }

  public ChallengePointPromotion getPromotion()
  {
    return promotion;
  }

  public void setPromotion( ChallengePointPromotion promotion )
  {
    this.promotion = promotion;
  }

  public BigDecimal getPercentAchieved()
  {
    return percentAchieved;
  }

  public void setPercentAchieved( BigDecimal percentAchieved )
  {
    this.percentAchieved = percentAchieved;
  }

  public List<ChallengepointAward> getAwards()
  {
    return awards;
  }

  public void setAwards( List<ChallengepointAward> awards )
  {
    this.awards = awards;
  }

  public String getAfterUnitLabel()
  {
    // TODO
    if ( promotion.getBaseUnit() != null && promotion.getBaseUnitPosition() != null && promotion.getBaseUnitPosition().getCode().equals( BaseUnitPosition.UNIT_AFTER ) )
    {
      return promotion.getBaseUnitText();
    }
    else
    {
      return "";
    }
  }

  public String getBeforeUnitLabel()
  {
    // TODO
    if ( promotion.getBaseUnit() != null && promotion.getBaseUnitPosition() != null && promotion.getBaseUnitPosition().getCode().equals( BaseUnitPosition.UNIT_BEFORE ) )
    {
      return promotion.getBaseUnitText();
    }
    else
    {
      return "";
    }
  }

  public BigDecimal getAmountToAchieve()
  {
    return amountToAchieve;
  }

  public void setAmountToAchieve( BigDecimal amountToAchieve )
  {
    this.amountToAchieve = amountToAchieve;
  }

  public ChallengepointAward getChallengepointAward()
  {
    return challengepointAward;
  }

  public void setChallengepointAward( ChallengepointAward challengepointAward )
  {
    this.challengepointAward = challengepointAward;
  }

  public Long getInterimAwardDeposited()
  {
    return interimAwardDeposited;
  }

  public void setInterimAwardDeposited( Long interimAwardDeposited )
  {
    this.interimAwardDeposited = interimAwardDeposited;
  }

  public String getBaseAmountLocaleBased()
  {
    if ( this.getBaseAmount() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getBaseAmount(), promotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getAmountToAchieveLocaleBased()
  {
    if ( this.getAmountToAchieve() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getAmountToAchieve(), promotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
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
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getCalculatedIncrementAmount(), promotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getCalculatedThresholdLocaleBased()
  {
    if ( this.getCalculatedThreshold() != null && !this.getCalculatedThreshold().equals( new BigDecimal( 0 ) ) )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getCalculatedThreshold(), promotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public void setProgressSubmissionDateDisplay( String progressSubmissionDateDisplay )
  {
    this.progressSubmissionDateDisplay = progressSubmissionDateDisplay;
  }

  public String getAwardPerIncrementLocaleBased()
  {
    if ( promotion.getAwardPerIncrement() != null && promotion.getAwardPerIncrement().longValue() > 0 )
    {
      return NumberFormatUtil.getLocaleBasedNumberFormat( promotion.getAwardPerIncrement().longValue() );
    }
    else
    {
      return "";
    }
  }

  public String getBonusAwardLocaleBased()
  {
    if ( this.getBaseAmount() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( this.getBaseAmount(), promotion.getAchievementPrecision().getPrecision(), UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getAwardLocaleBased()
  {
    if ( goalLevel.getAward() != null )
    {
      return NumberFormatUtil.getLocaleBasedBigDecimalFormat( goalLevel.getAward(), 0, UserManager.getLocale() );
    }
    else
    {
      return "";
    }
  }

  public String getTotalAwardEarnedLocaleBased()
  {
    if ( this.getTotalAwardEarned() != null )
    {
      return NumberFormatUtil.getLocaleBasedNumberFormat( this.getTotalAwardEarned() );
    }
    else
    {
      return "";
    }
  }

  public String getTotalAwardDepositedLocaleBased()
  {
    if ( this.getTotalAwardDeposited() != null )
    {
      return NumberFormatUtil.getLocaleBasedNumberFormat( this.getTotalAwardDeposited() );
    }
    else
    {
      return "";
    }
  }

  public String getTotalBaseAwardEarnedLocaleBased()
  {
    if ( this.getTotalBasicAwardEarned() != null )
    {
      return NumberFormatUtil.getLocaleBasedNumberFormat( this.getTotalBasicAwardEarned() );
    }
    else
    {
      return "";
    }
  }
}
