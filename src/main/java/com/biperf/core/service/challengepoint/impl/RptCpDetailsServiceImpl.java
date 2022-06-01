
package com.biperf.core.service.challengepoint.impl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.biperf.core.dao.challengepoint.ChallengepointAwardDAO;
import com.biperf.core.dao.challengepoint.RptCpDetailDAO;
import com.biperf.core.domain.challengepoint.ChallengepointAward;
import com.biperf.core.domain.challengepoint.PendingChallengepointAwardSummary;
import com.biperf.core.domain.challengepoint.RptCpDetail;
import com.biperf.core.domain.enums.AchievementPrecision;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.enums.RoundingMethod;
import com.biperf.core.domain.participant.Participant;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.service.challengepoint.ChallengePointService;
import com.biperf.core.service.challengepoint.RptCpDetailsService;
import com.biperf.core.service.promotion.engine.ChallengePointAchievementStrategyFactory;
import com.biperf.core.service.promotion.engine.ChallengePointCalculationResult;
import com.biperf.core.service.promotion.engine.ChallengePointIncrementStrategyFactory;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.value.ChallengepointPaxAwardValueBean;

/**
 * RptCpDetailsServiceImpl.
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
 * <td>reddy</td>
 * <td>Aug 29, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class RptCpDetailsServiceImpl implements RptCpDetailsService
{
  /** RptGoalDetailDAO * */
  private RptCpDetailDAO rptCpDetailDAO;
  private ChallengePointAchievementStrategyFactory challengePointAchievementStrategyFactory;
  private ChallengePointIncrementStrategyFactory challengePointIncrementStrategyFactory;
  private ChallengePointService challengePointService;
  private ChallengepointAwardDAO challengepointAwardDAO;

  /**
   * Gets the list of rptGoalDetails Overridden from
   * 
   * @see com.biperf.core.service.goalquest.RptGoalDetailService.getAll#()
   * @return List
   */
  public List getAll()
  {
    return this.rptCpDetailDAO.getAll();
  }

  /**
   * Gets a List of all promotion ids. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalDetailService#getPromotionIds()
   * @return List
   */
  public List getPromotionIds()
  {
    return this.rptCpDetailDAO.getPromotionIds();
  }

  /**
   * Saves a rptGoalDetail object Overridden from
   * 
   * @see com.biperf.core.service.goalquest.RptGoalDetailService.saveRptGoalDetail#( RptGoalDetail rptGoalDetail )
   * @param rptGoalDetail
   * @return List
   */
  public RptCpDetail saveRptCpDetail( RptCpDetail rptCpDetail )
  {
    return this.rptCpDetailDAO.saveRptCpDetail( rptCpDetail );
  }

  /**
   * Updates a rptGoalDetail object Overridden from
   * @param calculationResultsByPromotionId 
   * 
   * @see com.biperf.core.service.goalquest.RptGoalDetailService.updateGoalAchievementDetails#()
   */
  public void updateCpAchievementDetails( Map calculationResultsByPromotionId )
  {
    List rptCpDetailList = getAll();
    BigDecimal thresholdCalculated = null;
    BigDecimal incrementalQuantity = null;

    if ( !rptCpDetailList.isEmpty() )
    {
      for ( Iterator iter = rptCpDetailList.iterator(); iter.hasNext(); )
      {
        RptCpDetail rptCpDetail = (RptCpDetail)iter.next();
        PendingChallengepointAwardSummary pendingCpAwardSummary = (PendingChallengepointAwardSummary)calculationResultsByPromotionId.get( rptCpDetail.getChallengePointPromotion().getId() );
        ChallengePointCalculationResult cpCalculationResult = getCalculationResultsForParticipant( pendingCpAwardSummary, rptCpDetail.getParticipant().getId() );
        if ( cpCalculationResult != null )
        {
          /**
           * If Manager can select CP, then he/she should be treated as a pax
           * in the report table. Assumption here is if manager can select,
           * there will not be manageroverride.
           */
          if ( rptCpDetail.getChallengePointPromotion().getManagerCanSelect() != null && rptCpDetail.getChallengePointPromotion().getManagerCanSelect().booleanValue() )
          {
            rptCpDetail.setManager( Boolean.FALSE );
          }
          else
          {
            rptCpDetail.setManager( new Boolean( cpCalculationResult.isManager() ) );
          }
          if ( rptCpDetail.getPaxGoal() != null )
          {
            rptCpDetail.setCurrentValue( cpCalculationResult.getTotalPerformance() );
            if ( cpCalculationResult.getPercentageAchieved() != null )
            {
              rptCpDetail.setPercentOfCp( cpCalculationResult.getPercentageAchieved() );
            }
            else
            {
              rptCpDetail.setPercentOfCp( new BigDecimal( 0 ) );
            }
            rptCpDetail.setAmountToAchieve( cpCalculationResult.getAmountToAchieve() );
            rptCpDetail.setBaseQuantity( cpCalculationResult.getBaseObjective() );
            // set the calculatedIncremetalQuantity
            ChallengePointIncrementStrategyFactory challengePointIncrementStrategyFactory = getChallengePointIncrementStrategyFactory();
            incrementalQuantity = challengePointIncrementStrategyFactory.getChallengePointIncrementStrategy( rptCpDetail.getChallengePointPromotion().getAwardIncrementType() )
                .processIncrement( rptCpDetail.getChallengePointPromotion(), rptCpDetail.getParticipant().getId() );
            rptCpDetail.setIncrementQuantity( incrementalQuantity );
            // set the threshold reached
            rptCpDetail.setCalculatedThreshold( new BigDecimal( 0 ) );
            thresholdCalculated = getChallengePointService().getCalculatedThreshold( rptCpDetail.getChallengePointPromotion().getId(), rptCpDetail.getParticipant().getId() );
            if ( thresholdCalculated != null )
            {
              rptCpDetail.setCalculatedThreshold( thresholdCalculated );
            }
            if ( cpCalculationResult.getTotalPerformance() != null )
            {
              if ( thresholdCalculated != null )
              {
                rptCpDetail.setThresholdReached( new Boolean( meetsAchievement( cpCalculationResult.getTotalPerformance(),
                                                                                thresholdCalculated,
                                                                                rptCpDetail.getChallengePointPromotion().getAchievementPrecision(),
                                                                                rptCpDetail.getChallengePointPromotion().getRoundingMethod() ) ) );
              }
              else
              {
                rptCpDetail.setThresholdReached( new Boolean( false ) );
              }
            }
            else
            {
              rptCpDetail.setThresholdReached( new Boolean( false ) );
            }
            updateBasicAwardsEarned( rptCpDetail, pendingCpAwardSummary );

            if ( ( (ChallengePointPromotion)pendingCpAwardSummary.getPromotion() ).isIssueAwardsRun() )
            {
              rptCpDetail.setBasicAwardsDeposited( rptCpDetail.getBasicAwardsEarned() );
            }
            else
            {
              updateBasicAwardsDeposited( rptCpDetail, pendingCpAwardSummary );
            }
          }

          if ( rptCpDetail.getChallengePointPromotion().isIssueAwardsRun() )
          {
            rptCpDetail.setAchieved( new Boolean( cpCalculationResult.isAchieved() ) );
            if ( rptCpDetail.getChallengePointPromotion().getChallengePointAwardType().isPoints() )
            {
              rptCpDetail.setCalculatedPayout( cpCalculationResult.getCalculatedAchievement() );
            }

          }
        }
        else
        { // Some managers will not have a cpCalculationResult if they did not achieve
          List ownedNodes = rptCpDetail.getParticipant().getNodes( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
          List managerNodes = rptCpDetail.getParticipant().getNodes( HierarchyRoleType.lookup( HierarchyRoleType.MANAGER ) );

          if ( ownedNodes.size() > 0 || managerNodes.size() > 0 )
          {
            rptCpDetail.setManager( Boolean.TRUE );

          }
          else
          {
            rptCpDetail.setManager( Boolean.FALSE );
          }

        }
        /**
         * If Manager can select CP, then he/she should be treated as a pax
         * in the report table. Assumption here is if manager can select,
         * there will not be manageroverride.
         */
        if ( rptCpDetail.getChallengePointPromotion().getManagerCanSelect() != null && rptCpDetail.getChallengePointPromotion().getManagerCanSelect().booleanValue() )
        {
          rptCpDetail.setManager( Boolean.FALSE );
        }
        // bug fix to make sure manager are marked as achieved only if they achieve points
        if ( rptCpDetail.getManager() != null && rptCpDetail.getManager().booleanValue() )
        {
          updateManagerAwardsDeposited( rptCpDetail, rptCpDetail.getChallengePointPromotion(), rptCpDetail.getParticipant() );
          if ( rptCpDetail.getCalculatedPayout() != null && rptCpDetail.getCalculatedPayout().longValue() > 0 )
          {
            rptCpDetail.setAchieved( new Boolean( true ) );
          }
          else
          {
            rptCpDetail.setAchieved( new Boolean( false ) );
          }

        }
        saveRptCpDetail( rptCpDetail );

      }
      // because another oracle procedure is dependent on this data being there a flush needs
      // to happen before that oracle procedure can see the updated data.
      HibernateSessionManager.getSession().flush();
    }
  }

  private ChallengePointCalculationResult getCalculationResultsForParticipant( PendingChallengepointAwardSummary pendingCpAwardSummary, Long userId )
  {
    if ( pendingCpAwardSummary != null )
    {
      if ( pendingCpAwardSummary.getParticipantChallengepointResults() != null )
      {
        for ( Iterator iter = pendingCpAwardSummary.getParticipantChallengepointResults().iterator(); iter.hasNext(); )
        {
          ChallengePointCalculationResult cpCalculationResult = null;
          ChallengepointPaxAwardValueBean cpPaxAwardValueBean = (ChallengepointPaxAwardValueBean)iter.next();

          if ( cpPaxAwardValueBean.getParticipant() != null && cpPaxAwardValueBean.getParticipant().getId().equals( userId ) )
          {

            cpCalculationResult = getChallengePointAchievementStrategyFactory().getChallengePointAchievementStrategy( cpPaxAwardValueBean.getChallengePointPromotion().getAchievementRule().getCode() )
                .processChallengePoint( cpPaxAwardValueBean.getPaxGoal() );

            return cpCalculationResult;
          }
        }
      }
      if ( pendingCpAwardSummary.getManagerOverrideResults() != null )
      {
        for ( Iterator iter = pendingCpAwardSummary.getManagerOverrideResults().iterator(); iter.hasNext(); )
        {
          ChallengePointCalculationResult cpCalculationResult = null;
          ChallengepointPaxAwardValueBean cpPaxAwardValueBean = (ChallengepointPaxAwardValueBean)iter.next();

          if ( cpPaxAwardValueBean.getParticipant() != null && cpPaxAwardValueBean.getParticipant().getId().equals( userId ) )
          {
            cpCalculationResult = new ChallengePointCalculationResult();
            cpCalculationResult.setManager( true );
            cpCalculationResult.setReciever( cpPaxAwardValueBean.getParticipant() );
            cpCalculationResult.setAchieved( cpPaxAwardValueBean.isAchieved() );
            if ( cpPaxAwardValueBean.getAwardEarned() != null )
            {
              cpCalculationResult.setTotalPerformance( new BigDecimal( cpPaxAwardValueBean.getAwardEarned().longValue() ) );
              cpCalculationResult.setCalculatedAchievement( new BigDecimal( cpPaxAwardValueBean.getAwardEarned().longValue() ) );
              cpCalculationResult.setAchieved( true );
            }
            else
            {
              cpCalculationResult.setTotalPerformance( null );
            }

            return cpCalculationResult;
          }
        }
      }

    }
    return null;
  }

  private void updateBasicAwardsEarned( RptCpDetail rptCpDetail, PendingChallengepointAwardSummary pendingCpAwardSummary )
  {
    if ( pendingCpAwardSummary.getParticipantChallengepointResults() != null )
    {
      for ( Iterator iter = pendingCpAwardSummary.getParticipantChallengepointResults().iterator(); iter.hasNext(); )
      {
        ChallengepointPaxAwardValueBean cpPaxAwardValueBean = (ChallengepointPaxAwardValueBean)iter.next();
        if ( cpPaxAwardValueBean.getAwardType() != null && cpPaxAwardValueBean.getAwardType().equals( "basic" )
            && cpPaxAwardValueBean.getParticipant().getId().longValue() == rptCpDetail.getParticipant().getId().longValue() )
        {
          if ( cpPaxAwardValueBean.getAwardEarned() != null )
          {
            rptCpDetail.setBasicAwardsEarned( new BigDecimal( cpPaxAwardValueBean.getAwardEarned().longValue() ) );
          }
          else
          {
            rptCpDetail.setBasicAwardsEarned( null );
          }
          break;
        }

      }
    }

  }

  private void updateManagerAwardsDeposited( RptCpDetail rptCpDetail, ChallengePointPromotion promotion, Participant participant )
  {

    ChallengepointAward award = challengepointAwardDAO.getAwardByPromotionIdAndUserId( promotion.getId(), participant.getId() );
    Long totalIssued = new Long( 0 );
    if ( award != null )
    {
      totalIssued = award.getTotalAwardIssued();
    }
    // for some reason for manager override total ward is not getting populated
    // but ideally totally issued and issued are same for mangers
    if ( award != null && award.getAwardType() != null && award.getAwardType().equals( "manageroverride" ) )
    {
      totalIssued = award.getAwardIssued();
    }

    if ( totalIssued == null )
    {
      totalIssued = new Long( 0 );
    }
    rptCpDetail.setCalculatedPayout( new BigDecimal( totalIssued.longValue() ) );

  }

  private void updateBasicAwardsDeposited( RptCpDetail rptCpDetail, PendingChallengepointAwardSummary pendingCpAwardSummary )
  {
    if ( rptCpDetail != null && pendingCpAwardSummary != null )
    {
      if ( pendingCpAwardSummary.getParticipantChallengepointResults() != null )
      {
        for ( Iterator iter = pendingCpAwardSummary.getParticipantChallengepointResults().iterator(); iter.hasNext(); )
        {
          ChallengepointPaxAwardValueBean cpPaxAwardValueBean = (ChallengepointPaxAwardValueBean)iter.next();
          if ( cpPaxAwardValueBean.getAwardType() != null && cpPaxAwardValueBean.getAwardType().equals( "basic" )
              && cpPaxAwardValueBean.getParticipant().getId().longValue() == rptCpDetail.getParticipant().getId().longValue() )
          {

            ChallengepointAward award = challengepointAwardDAO.getAwardByPromotionIdAndUserId( cpPaxAwardValueBean.getChallengePointPromotion().getId(), cpPaxAwardValueBean.getParticipant().getId() );
            Long totalIssued = new Long( 0 );
            if ( award != null )
            {
              totalIssued = award.getTotalAwardIssued();
            }

            if ( totalIssued == null )
            {
              totalIssued = new Long( 0 );
            }
            rptCpDetail.setBasicAwardsDeposited( new BigDecimal( totalIssued.longValue() ) );

            break;
          }

        }
      }
    }
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

  public RptCpDetailDAO getRptCpDetailDAO()
  {
    return rptCpDetailDAO;
  }

  public void setRptCpDetailDAO( RptCpDetailDAO rptCpDetailDAO )
  {
    this.rptCpDetailDAO = rptCpDetailDAO;
  }

  public ChallengePointAchievementStrategyFactory getChallengePointAchievementStrategyFactory()
  {
    return challengePointAchievementStrategyFactory;
  }

  public void setChallengePointAchievementStrategyFactory( ChallengePointAchievementStrategyFactory challengePointAchievementStrategyFactory )
  {
    this.challengePointAchievementStrategyFactory = challengePointAchievementStrategyFactory;
  }

  public ChallengePointService getChallengePointService()
  {
    return challengePointService;
  }

  public void setChallengePointService( ChallengePointService challengePointService )
  {
    this.challengePointService = challengePointService;
  }

  public ChallengePointIncrementStrategyFactory getChallengePointIncrementStrategyFactory()
  {
    return challengePointIncrementStrategyFactory;
  }

  public void setChallengePointIncrementStrategyFactory( ChallengePointIncrementStrategyFactory challengePointIncrementStrategyFactory )
  {
    this.challengePointIncrementStrategyFactory = challengePointIncrementStrategyFactory;
  }

  public ChallengepointAwardDAO getChallengepointAwardDAO()
  {
    return challengepointAwardDAO;
  }

  public void setChallengepointAwardDAO( ChallengepointAwardDAO challengepointAwardDAO )
  {
    this.challengepointAwardDAO = challengepointAwardDAO;
  }

}
