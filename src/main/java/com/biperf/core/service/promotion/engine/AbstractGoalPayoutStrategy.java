/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/service/promotion/engine/AbstractGoalPayoutStrategy.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.enums.RoundingMethod;
import com.biperf.core.domain.goalquest.GoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.promotion.GoalLevel;

/**
 * AbstractGoalPayoutStrategy.
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
 * <td>Todd</td>
 * <td>Jan 8, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public abstract class AbstractGoalPayoutStrategy implements GoalPayoutStrategy
{

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.GoalPayoutStrategy#processGoal(com.biperf.core.domain.goalquest.PaxGoal)
   * @param paxGoal
   * @return
   */
  public final GoalCalculationResult processGoal( PaxGoal paxGoal )
  {
    GoalCalculationResult goalCalculationResult = processGoalInternal( paxGoal );
    if ( goalCalculationResult != null )
    {
      if ( paxGoal != null )
      {
        goalCalculationResult.setGoalLevel( paxGoal.getGoalLevel() );
        goalCalculationResult.setTotalPerformance( paxGoal.getCurrentValue() );
        goalCalculationResult.setBaseObjective( paxGoal.getBaseQuantity() );
      }
      goalCalculationResult.setManager( false );
    }
    return goalCalculationResult;
  }

  /**
   * @param paxGoal to calcluate award for
   * @return a GoalCalculationResult
   */
  protected abstract GoalCalculationResult processGoalInternal( PaxGoal paxGoal );

  /**
   * @param goalQuestAwardSummaryParticipantMap is a map containing GoalQuestAwardSummary objects
   * @return a List of GoalQuestAwardSummary objects
   */
  public List<GoalQuestAwardSummary> summarizeResults( Map goalQuestAwardSummaryParticipantMap )
  {
    List<GoalQuestAwardSummary> goalQuestAwardSummaryParticipantList = new ArrayList<GoalQuestAwardSummary>();
    int totalSelected = 0;
    int totalAchieved = 0;
    BigDecimal totalAward = new BigDecimal( "0" );
    SortedMap sortedMap = null;
    if ( goalQuestAwardSummaryParticipantMap != null )
    {
      sortedMap = new TreeMap( goalQuestAwardSummaryParticipantMap );
    }
    if ( sortedMap != null )
    {
      goalQuestAwardSummaryParticipantList.addAll( sortedMap.values() );
    }
    for ( Iterator<GoalQuestAwardSummary> it = goalQuestAwardSummaryParticipantList.iterator(); it.hasNext(); )
    {
      GoalQuestAwardSummary goalQuestAwarySummary = (GoalQuestAwardSummary)it.next();
      totalSelected += goalQuestAwarySummary.getTotalSelected();
      totalAchieved += goalQuestAwarySummary.getTotalAchieved();
      if ( goalQuestAwarySummary.getTotalAward() != null )
      {
        totalAward = totalAward.add( goalQuestAwarySummary.getTotalAward() );
      }
    }
    GoalQuestAwardSummary totalParticipantGoalQuestAwardSummary = new GoalQuestAwardSummary();
    totalParticipantGoalQuestAwardSummary.setTotalSelected( totalSelected );
    totalParticipantGoalQuestAwardSummary.setTotalAchieved( totalAchieved );
    totalParticipantGoalQuestAwardSummary.setTotalAward( totalAward );
    totalParticipantGoalQuestAwardSummary.setParticipantTotals( true );
    goalQuestAwardSummaryParticipantList.add( totalParticipantGoalQuestAwardSummary );
    return goalQuestAwardSummaryParticipantList;
  }

  /**
   * @param currentValue
   * @param achievementAmount
   * @param achievementPrecision
   * @param roundingMethod
   * @return boolean
   */
  protected boolean meetsAchievement( BigDecimal currentValue, BigDecimal achievementAmount, AchievementPrecision achievementPrecision, RoundingMethod roundingMethod )
  {
    if ( currentValue != null && achievementAmount != null )
    {
      int precision = achievementPrecision.getPrecision();
      int roundingMode = roundingMethod.getBigDecimalRoundingMode();
      BigDecimal roundedCurrentValue = currentValue.setScale( precision, roundingMode );
      BigDecimal roundedAchievementAmount = achievementAmount.setScale( precision, roundingMode );
      if ( roundedCurrentValue.compareTo( roundedAchievementAmount ) >= 0 )
      {
        return true;
      }
    }
    return false;
  }

  /**
   * Calculates award for Rate and Both Strategy
   */
  protected void calculateFixedAmountForRateAndBoth( BigDecimal currentValue, PaxGoal paxGoal, GoalCalculationResult goalCalculationResult, boolean isRateStrategy )
  {
    GoalLevel goalLevel = (GoalLevel)paxGoal.getGoalLevel();
    BigDecimal extraAchieved = null;
    if ( goalLevel.getMinimumQualifier() != null )
    {
      BigDecimal minToEarnExtra = null;
      if ( isRateStrategy )
      {
        minToEarnExtra = goalCalculationResult.getAmountToAchieve();
      }
      else
      {
        minToEarnExtra = goalLevel.getMinimumQualifier();
        if ( paxGoal.getBaseQuantity() != null )
        {
          minToEarnExtra = minToEarnExtra.add( paxGoal.getBaseQuantity() );
        }
      }
      extraAchieved = currentValue.subtract( minToEarnExtra );
    }
    BigDecimal awardPerIncrement = null;
    if ( isRateStrategy )
    {
      awardPerIncrement = goalLevel.getAward();
    }
    else
    {
      Integer awardPerIncInteger = goalLevel.getBonusAward();
      if ( awardPerIncInteger != null )
      {
        awardPerIncrement = new BigDecimal( awardPerIncInteger.toString() );
      }
    }
    BigDecimal incrementalAward = null;
    if ( extraAchieved != null && extraAchieved.longValue() > 0 && goalLevel.getIncrementalQuantity() != null && goalLevel.getIncrementalQuantity().longValue() > 0 && awardPerIncrement != null )
    {
      incrementalAward = getCalculatedIncrementAward( extraAchieved, goalLevel.getIncrementalQuantity(), awardPerIncrement );
    }
    calculateAwardForRateAndBoth( paxGoal, goalCalculationResult, incrementalAward, isRateStrategy );
  }

  protected void calculatePercentBaseForRateAndBoth( BigDecimal currentValue, PaxGoal paxGoal, GoalCalculationResult goalCalculationResult, boolean isRateStrategy )
  {
    GoalLevel goalLevel = (GoalLevel)paxGoal.getGoalLevel();
    BigDecimal extraAchieved = null;
    if ( goalLevel.getMinimumQualifier() != null && paxGoal.getBaseQuantity() != null )
    {
      BigDecimal minToEarnExtra = null;
      if ( isRateStrategy )
      {
        minToEarnExtra = goalCalculationResult.getAmountToAchieve();
      }
      else
      {
        minToEarnExtra = goalLevel.getMinimumQualifier().movePointLeft( 2 ).multiply( paxGoal.getBaseQuantity() );
      }
      extraAchieved = currentValue.subtract( minToEarnExtra );
    }
    BigDecimal awardPerIncrement = null;
    if ( isRateStrategy )
    {
      awardPerIncrement = goalLevel.getAward();
    }
    else
    {
      Integer awardPerIncInteger = goalLevel.getBonusAward();
      if ( awardPerIncInteger != null )
      {
        awardPerIncrement = new BigDecimal( awardPerIncInteger.toString() );
      }
    }
    BigDecimal incrementalAward = null;
    if ( extraAchieved != null && extraAchieved.longValue() > 0 && goalCalculationResult.getCalculatedIncremental() != null && goalCalculationResult.getCalculatedIncremental().longValue() > 0
        && awardPerIncrement != null )
    {
      incrementalAward = getCalculatedIncrementAward( extraAchieved, goalCalculationResult.getCalculatedIncremental(), awardPerIncrement );
    }
    calculateAwardForRateAndBoth( paxGoal, goalCalculationResult, incrementalAward, isRateStrategy );
  }

  private BigDecimal getCalculatedIncrementAward( BigDecimal extraAchieved, BigDecimal increment, BigDecimal awardPerIncrement )
  {
    return new BigDecimal( extraAchieved.longValue() ).divide( new BigDecimal( increment.longValue() ), BigDecimal.ROUND_DOWN ).multiply( new BigDecimal( awardPerIncrement.longValue() ) );
  }

  private void calculateAwardForRateAndBoth( PaxGoal paxGoal, GoalCalculationResult goalCalculationResult, BigDecimal incrementalAward, boolean isRateStrategy )
  {
    GoalLevel goalLevel = (GoalLevel)paxGoal.getGoalLevel();
    BigDecimal totalAward = null;
    if ( goalLevel.getAward() != null )
    {
      totalAward = goalLevel.getAward();
      if ( incrementalAward != null )
      {
        totalAward = totalAward.add( incrementalAward );
      }
    }
    if ( totalAward != null )
    {
      // if the total award greater than maxPoints then set total award = MaxPoints
      Integer maxPoints = goalLevel.getMaximumPoints();
      if ( maxPoints != null && totalAward.intValue() > maxPoints.intValue() )
      {
        goalCalculationResult.setCalculatedPayout( new BigDecimal( goalLevel.getMaximumPoints().toString() ) );
      }
      else
      {
        if ( paxGoal.getGoalQuestPromotion().getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
        {
          goalCalculationResult.setCalculatedPayout( new BigDecimal( totalAward.longValue() ) );
        }
        else
        {
          goalCalculationResult.setCalculatedPayout( totalAward );
        }
      }
    }
  }

}
