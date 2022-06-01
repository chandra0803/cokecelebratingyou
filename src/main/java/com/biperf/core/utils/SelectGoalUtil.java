/*
 * (c) 2007 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/utils/SelectGoalUtil.java,v $
 */

package com.biperf.core.utils;

import java.math.BigDecimal;
import java.util.Iterator;

import com.biperf.core.domain.enums.AchievementRuleType;
import com.biperf.core.domain.enums.PromotionAwardsType;
import com.biperf.core.domain.goalquest.PaxGoal;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.AbstractGoalLevel;
import com.biperf.core.domain.promotion.GoalLevel;
import com.biperf.core.domain.promotion.GoalQuestPromotion;
import com.biperf.core.domain.promotion.ManagerOverrideGoalLevel;
import com.biperf.core.domain.promotion.Promotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.goalquest.PaxGoalService;
import com.biperf.core.service.participant.ParticipantService;
import com.biperf.core.service.promotion.PromotionAssociationRequest;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.service.promotion.engine.GoalPayoutStrategyFactory;
import com.biperf.core.value.GoalLevelValueBean;

/**
 * SelectGoalUtil.
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
 * <td>viswanat</td>
 * <td>Feb 8, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SelectGoalUtil
{
  /**
   * @param promotion
   * @param base
   * @param value
   * @return BigDecimal
   */
  public static BigDecimal computePercentOfBaseAmount( GoalQuestPromotion promotion, BigDecimal base, BigDecimal value )
  {
    BigDecimal calculatedValue = null;
    if ( base != null && value != null )
    {
      calculatedValue = value.movePointLeft( 2 ).multiply( base );
    }
    return promotion.roundValue( calculatedValue );
  }

  /**
   * @param paxGoal
   * @param promotion
   * @param goalLevel
   * @return GoalLevelValueBean
   */
  public static GoalLevelValueBean populateGoalLevelValueBean( PaxGoal paxGoal, GoalQuestPromotion promotion, GoalLevel goalLevel )
  {
    GoalLevelValueBean goalLevelValueBean = new GoalLevelValueBean();
    goalLevelValueBean.setGqPromotion( promotion );

    goalLevelValueBean.setGoalLevel( goalLevel );
    if ( goalLevel != null )
    {
      goalLevelValueBean.setCalculatedGoalAmount( getCalculatedGoalAmount( goalLevel, paxGoal ) );
      if ( paxGoal != null )
      {
        goalLevelValueBean.setBaseAmount( paxGoal.getBaseQuantity() );
      }
      if ( promotion.getAchievementRule().getCode().equals( AchievementRuleType.FIXED ) )
      {
        BigDecimal calculatedAchievementAmount = goalLevel.getAchievementAmount();
        BigDecimal calculatedMinimumQualifier = goalLevel.getMinimumQualifier();
        if ( paxGoal != null && paxGoal.getBaseQuantity() != null )
        {
          if ( calculatedAchievementAmount != null )
          {
            calculatedAchievementAmount = calculatedAchievementAmount.add( paxGoal.getBaseQuantity() );
          }
          if ( calculatedMinimumQualifier != null )
          {
            calculatedMinimumQualifier = calculatedMinimumQualifier.add( paxGoal.getBaseQuantity() );
          }
        }

        goalLevelValueBean.setCalculatedAchievementAmount( calculatedAchievementAmount );
        goalLevelValueBean.setCalculatedIncrementAmount( goalLevel.getIncrementalQuantity() );
        goalLevelValueBean.setCalculatedMinimumQualifier( calculatedMinimumQualifier );
      }
      else
      // Percent
      {
        if ( paxGoal != null )
        {
          goalLevelValueBean.setCalculatedAchievementAmount( computePercentOfBaseAmount( promotion, paxGoal.getBaseQuantity(), goalLevel.getAchievementAmount() ) );
          BigDecimal calculatedMinimumQualifier = computePercentOfBaseAmount( promotion, paxGoal.getBaseQuantity(), goalLevel.getMinimumQualifier() );
          goalLevelValueBean.setCalculatedIncrementAmount( computePercentOfBaseAmount( promotion, calculatedMinimumQualifier, goalLevel.getIncrementalQuantity() ) );
          goalLevelValueBean.setCalculatedMinimumQualifier( calculatedMinimumQualifier );
        }
      }
    }
    else if ( paxGoal != null )
    {
      goalLevelValueBean.setBaseAmount( paxGoal.getBaseQuantity() );
    }
    return goalLevelValueBean;
  }

  public static PaxGoal getLevelSpecificGoal( PaxGoal paxGoal, GoalQuestPromotion promotion, GoalLevel goalLevel )
  {
    PaxGoal levelSpecificGoal = new PaxGoal();
    if ( null == paxGoal )
    {
      levelSpecificGoal.setGoalLevel( goalLevel );
      levelSpecificGoal.setGoalQuestPromotion( promotion );
      levelSpecificGoal.setCurrentValue( goalLevel.getMinimumQualifier() );
      levelSpecificGoal.setOverrideQuantity( goalLevel.getIncrementalQuantity() );
    }
    else
    {
      levelSpecificGoal.setGoalLevel( goalLevel );
      levelSpecificGoal.setGoalQuestPromotion( paxGoal.getGoalQuestPromotion() );
      levelSpecificGoal.setCurrentValue( paxGoal.getCurrentValue() );
      levelSpecificGoal.setOverrideQuantity( paxGoal.getOverrideQuantity() );
      levelSpecificGoal.setBaseQuantity( paxGoal.getBaseQuantity() );
    }
    return levelSpecificGoal;
  }

  /**
   * @param abstractGoalLevel
   * @param paxGoal
   * @return BigDecimal
   */
  public static BigDecimal getCalculatedGoalAmount( AbstractGoalLevel abstractGoalLevel, PaxGoal paxGoal )
  {
    GoalQuestPromotion goalQuestPromotion = null;
    if ( abstractGoalLevel != null && abstractGoalLevel.getPromotion() != null )
    {
      goalQuestPromotion = (GoalQuestPromotion)abstractGoalLevel.getPromotion();

      if ( abstractGoalLevel.isManagerOverrideGoalLevel() )
      {
        ManagerOverrideGoalLevel managerOverrideGoalLevel = (ManagerOverrideGoalLevel)abstractGoalLevel;
        if ( managerOverrideGoalLevel.getTeamAchievementPercent() != null )
        {
          return managerOverrideGoalLevel.getTeamAchievementPercent();
        }
      }
      else
      {
        GoalPayoutStrategyFactory goalPayoutStrategyFactory = getGoalPayoutStrategyFactory();

        GoalCalculationResult goalCalculationResult = null;
        if ( goalQuestPromotion.getAwardType().getCode().equals( PromotionAwardsType.POINTS ) )
        {
          goalCalculationResult = goalPayoutStrategyFactory.getGoalPayoutStrategy( goalQuestPromotion.getPayoutStructure().getCode() ).processGoal( paxGoal );
        }
        else
        // Merch or Travel
        {
          goalCalculationResult = goalPayoutStrategyFactory.getGoalPayoutStrategy().processGoal( paxGoal );
        }
        return goalCalculationResult.getAmountToAchieve();
      }
    }
    return null;
  }

  /**
   * @param goalLevel
   * @return GoalLevelValueBean
   */
  public static GoalLevelValueBean populateGoalLevelValueBean( ManagerOverrideGoalLevel goalLevel )
  {
    GoalLevelValueBean goalLevelValueBean = new GoalLevelValueBean();
    goalLevelValueBean.setManagerOverride( true );
    goalLevelValueBean.setGoalLevel( goalLevel );
    goalLevelValueBean.setCalculatedGoalAmount( goalLevel.getTeamAchievementPercent() );
    return goalLevelValueBean;
  }

  /**
   * @param goalQuestPromotion
   * @param goalLevelSequence
   * @param isOwner
   * @return AbstractGoalLevel
   */
  public static AbstractGoalLevel getGoalLevelForOwnerOrMember( GoalQuestPromotion goalQuestPromotion, Integer goalLevelSequence )
  {
    // if owner get manager override goal levels else get goal levels
    for ( Iterator iter = goalQuestPromotion.getGoalLevels().iterator(); iter.hasNext(); )
    {
      AbstractGoalLevel goalLevel = (AbstractGoalLevel)iter.next();
      // if passed-in goal level sequence matches goal level sequence from db return that goal level
      if ( goalLevelSequence.intValue() == goalLevel.getSequenceNumber() )
      {
        return goalLevel;
      }
    }
    return null;
  }

  /**
   * @param promotion
   * @return boolean
   */
  public static boolean isValidGoalQuestPromotionForParticipant( Promotion promotion )
  {
    return promotion.isGoalQuestPromotion() && promotion.isLive();
  }

  /**
   * @param promotionId
   * @return
   */
  public static GoalQuestPromotion getPromotion( Long promotionId )
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.GOAL_LEVELS ) );
    associationRequestCollection.add( new PromotionAssociationRequest( PromotionAssociationRequest.NOTIFICATIONS ) );
    return (GoalQuestPromotion)getPromotionService().getPromotionByIdWithAssociations( promotionId, associationRequestCollection );
  }

  /**
   * @param userId
   * @return
   */
  public static Participant getParticipant( Long userId )
  {
    return getParticipantService().getParticipantById( userId );
  }

  /**
   * @param promotionId
   * @param userId
   * @return
   */
  public static PaxGoal getPaxGoal( Long promotionId, Long userId )
  {
    return getPaxGoalService().getPaxGoalByPromotionIdAndUserId( promotionId, userId );
  }

  /**
   * Get the PaxGoalService from the beanLocator.
   * 
   * @return PaxGoalService
   */
  /**
   * @return
   */
  private static PaxGoalService getPaxGoalService()
  {
    return (PaxGoalService)ServiceLocator.getService( PaxGoalService.BEAN_NAME );
  }

  /**
   * Get the PromotionService from the beanLocator.
   * 
   * @return PromotionService
   */
  private static PromotionService getPromotionService()
  {
    return (PromotionService)ServiceLocator.getService( PromotionService.BEAN_NAME );
  }

  /**
   * Get the ParticipantService from the beanLocator.
   * 
   * @return ParticipantService
   */
  private static ParticipantService getParticipantService()
  {
    return (ParticipantService)ServiceLocator.getService( ParticipantService.BEAN_NAME );
  }

  /**
   * Get the GoalPayoutStrategyFactory from the beanLocator.
   *
   * @return GoalPayoutStrategyFactory
   */
  private static GoalPayoutStrategyFactory getGoalPayoutStrategyFactory()
  {
    return (GoalPayoutStrategyFactory)BeanLocator.getBean( GoalPayoutStrategyFactory.BEAN_NAME );
  }

  /**
   * @param promotionId
   * @param userId
   * @param ascReqCollection
   * @return PaxGoal With Initialized Associations
   */
  public static PaxGoal getPaxGoal( Long promotionId, Long userId, AssociationRequestCollection ascReqCollection )
  {
    PaxGoalService paxGoalService = (PaxGoalService)ServiceLocator.getService( PaxGoalService.BEAN_NAME );
    return paxGoalService.getPaxGoalByPromotionIdAndUserId( promotionId, userId, ascReqCollection );
  }
}
