
package com.biperf.core.service.challengepoint.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.biperf.core.dao.challengepoint.RptCpProductionDAO;
import com.biperf.core.dao.promotion.hibernate.ChallengepointPromotionQueryConstraint;
import com.biperf.core.domain.challengepoint.RptChallengepointProduction;
import com.biperf.core.domain.enums.AchievementRuleType;
import com.biperf.core.domain.enums.ChallengepointProductionCountType;
import com.biperf.core.domain.enums.PrimaryAudienceType;
import com.biperf.core.domain.enums.PromotionStatusType;
import com.biperf.core.domain.enums.PromotionType;
import com.biperf.core.domain.promotion.ChallengePointPromotion;
import com.biperf.core.service.AssociationRequestCollection;
import com.biperf.core.service.challengepoint.RptCpProductionService;
import com.biperf.core.service.promotion.PromotionService;
import com.biperf.core.value.ChallengepointProductionValueBean;

public class RptCpProductionServiceImpl implements RptCpProductionService
{
  /** RptGoalROIDAO * */
  private RptCpProductionDAO rptCpProductionDAO;
  private PromotionService promotionService;

  /**
   * Set the RptGoalROIDAO through IoC
   * 
   * @param rptCpProductionDAO
   */
  public void setRptCpProductionDAO( RptCpProductionDAO rptCpProductionDAO )
  {
    this.rptCpProductionDAO = rptCpProductionDAO;
  }

  public void setPromotionService( PromotionService promotionService )
  {
    this.promotionService = promotionService;
  }

  /**
   * Selects records from the RPT_GOAL_ROI by promotion id.
   * 
   * @param id
   * @return List of RptChallengepointProduction objects
   */
  public List getRptCpProductionByPromotionId( Long id )
  {
    return rptCpProductionDAO.getRptCpProductionByPromotionId( id );
  }

  /**
   * Save the rptCpProduction.
   * 
   * @param rptCpProduction
   * @return rptCpProduction
   */
  public RptChallengepointProduction saveRptCpProduction( RptChallengepointProduction rptCpProduction )
  {
    return this.rptCpProductionDAO.saveRptCpProduction( rptCpProduction );
  }

  /**
   * Updates rptCpProduction with return on investment counts for finalized GoalQuest Promotions.
   */
  public void updateCpProductionCounts()
  {
    ArrayList promotions = (ArrayList)getCPPromotions();

    if ( !promotions.isEmpty() )
    {
      for ( Iterator iter = promotions.iterator(); iter.hasNext(); )
      {
        ChallengePointPromotion cpPromotion = (ChallengePointPromotion)iter.next();

        ArrayList listOfCounts = getCounts( ChallengepointProductionCountType.getList(), cpPromotion );

        updateCounts( listOfCounts );
      }
    }
  }

  private ArrayList getCounts( List cpProductionCountTypes, ChallengePointPromotion cpPromotion )
  {
    ArrayList listOfCounts = new ArrayList();

    Long promotionId = cpPromotion.getId();

    // Get total number of paxs in promotion audiences
    Integer paxsInAudience = new Integer( 0 );
    Integer totPaxsInAudience = new Integer( 0 );
    boolean noneSelected = false;
    if ( cpPromotion.getPrimaryAudienceType().equals( PrimaryAudienceType.lookup( PrimaryAudienceType.ALL_ACTIVE_PAX_CODE ) ) )
    {
      paxsInAudience = rptCpProductionDAO.getNbrOfAllActivePax();
      totPaxsInAudience = rptCpProductionDAO.getNbrOfPaxInSelectPromoAudienceIncludeOwners( promotionId );

    }
    if ( cpPromotion.getManagerCanSelect() != null && cpPromotion.getManagerCanSelect().booleanValue() )
    {
      paxsInAudience = new Integer( paxsInAudience.intValue() + rptCpProductionDAO.getNbrOfPaxInSpecifyPromoAudienceIncludeOwners( promotionId ).intValue() );
      totPaxsInAudience = rptCpProductionDAO.getNbrOfPaxInSelectPromoAudienceIncludeOwners( promotionId );
    }
    else
    {
      paxsInAudience = new Integer( paxsInAudience.intValue() + rptCpProductionDAO.getNbrOfPaxInSpecifyPromoAudience( promotionId ).intValue() );
      totPaxsInAudience = rptCpProductionDAO.getNbrOfPaxInSelectPromoAudienceIncludeOwners( promotionId );
    }

    if ( !cpProductionCountTypes.isEmpty() )
    {
      // iterate and get counts for each ROI count type
      for ( Iterator iter = cpProductionCountTypes.iterator(); iter.hasNext(); )
      {
        ChallengepointProductionCountType cpProductionCountType = (ChallengepointProductionCountType)iter.next();

        List counts = new ArrayList();

        if ( cpProductionCountType.equals( ChallengepointProductionCountType.lookup( ChallengepointProductionCountType.CP_ACHIEVED ) ) )
        {
          counts = rptCpProductionDAO.getAchievedCounts( promotionId );
        }
        else if ( cpProductionCountType.equals( ChallengepointProductionCountType.lookup( ChallengepointProductionCountType.CP_NOT_ACHIEVED_SALES_OVER_BASELINE ) ) )
        {
          counts = rptCpProductionDAO.getNotAchievedOverBaselineCounts( promotionId );
        }
        else if ( cpProductionCountType.equals( ChallengepointProductionCountType.lookup( ChallengepointProductionCountType.SUBTOTAL ) ) )
        {
          counts = rptCpProductionDAO.getSubtotalCounts( promotionId );
        }
        else if ( cpProductionCountType.equals( ChallengepointProductionCountType.lookup( ChallengepointProductionCountType.CP_NOT_ACHIEVED_SALES_UNDER_BASELINE ) ) )
        {
          counts = rptCpProductionDAO.getNotAchievedUnderBaselineCounts( promotionId );
        }
        else if ( cpProductionCountType.equals( ChallengepointProductionCountType.lookup( ChallengepointProductionCountType.TOTAL ) ) )
        {
          counts = rptCpProductionDAO.getTotalCounts( promotionId );
        }
        else if ( cpProductionCountType.equals( ChallengepointProductionCountType.lookup( ChallengepointProductionCountType.DID_NOT_SELECT_CP ) ) )
        {
          counts = rptCpProductionDAO.getDidNotSelectCPCounts( promotionId );
          noneSelected = true;
        }

        if ( !counts.isEmpty() )
        {
          ChallengepointProductionValueBean valueBean = (ChallengepointProductionValueBean)counts.get( 0 ); // always
                                                                                                            // 1
                                                                                                            // value
                                                                                                            // bean
          Integer nbrOfPaxs = valueBean.getTotNbrOfUsers();
          BigDecimal totActualProduction = valueBean.getTotCurrentValue();
          BigDecimal totBaselineObjective = valueBean.getTotBaseQuantity();

          RptChallengepointProduction rptCpProduction = new RptChallengepointProduction();
          rptCpProduction.setChallengepointPromotion( cpPromotion );
          rptCpProduction.setChallengepointProductionCountType( cpProductionCountType );
          rptCpProduction.setNbrOfParticipants( nbrOfPaxs );
          rptCpProduction.setTotActualProduction( totActualProduction );
          if ( nbrOfPaxs.intValue() > 0 )
          {
            Double pctOfPaxs = null;
            if ( noneSelected )
            {
              if ( paxsInAudience != null && paxsInAudience.intValue() > 0 )
              {
                pctOfPaxs = new Double( nbrOfPaxs.doubleValue() / paxsInAudience.doubleValue() );
              }
            }
            else if ( totPaxsInAudience != null && totPaxsInAudience.intValue() > 0 )
            {
              pctOfPaxs = new Double( nbrOfPaxs.doubleValue() / totPaxsInAudience.doubleValue() );
            }
            rptCpProduction.setPctOfParticipants( pctOfPaxs );
          }

          boolean baseIncluded = false;
          if ( cpPromotion.getAwardThresholdType() != null && cpPromotion.getAwardThresholdType().equals( ChallengePointPromotion.PRIMARY_AWARD_THRESHOLD_PCT_BASE ) )
          {
            baseIncluded = true;
          }
          if ( cpPromotion.getAwardIncrementType() != null && cpPromotion.getAwardIncrementType().equals( ChallengePointPromotion.PRIMARY_AWARD_INCREMENT_PCT_BASE ) )
          {
            baseIncluded = true;
          }
          if ( cpPromotion.getAchievementRule() != null && ( cpPromotion.getAchievementRule().getCode().equals( AchievementRuleType.BASE_PLUS_FIXED )
              || cpPromotion.getAchievementRule().getCode().equals( AchievementRuleType.PERCENT_OF_BASE ) ) )
          {
            baseIncluded = true;
          }
          if ( baseIncluded )
          {
            rptCpProduction.setTotBaselineObjective( totBaselineObjective );

            if ( totActualProduction != null && totActualProduction.doubleValue() > 0.00 && totBaselineObjective != null )
            {
              Double pctIncrease = new Double( totActualProduction.subtract( totBaselineObjective ).setScale( 4 ).divide( totBaselineObjective, 4, BigDecimal.ROUND_DOWN ).doubleValue() );
              rptCpProduction.setPctIncrease( pctIncrease );
            }
            if ( totActualProduction != null && totBaselineObjective != null )
            {
              Double unitIncrease = new Double( totActualProduction.subtract( totBaselineObjective ).doubleValue() );
              rptCpProduction.setUnitDollarIncrease( unitIncrease );
            }
          }

          listOfCounts.add( rptCpProduction );
        }

      } // End for loop ROI count type

    }
    return listOfCounts;
  }

  private void updateCounts( ArrayList listOfCounts )
  {
    // iterate and update counts for each ROI count type
    for ( Iterator iter = listOfCounts.iterator(); iter.hasNext(); )
    {
      RptChallengepointProduction rptCpProduction = (RptChallengepointProduction)iter.next();

      rptCpProductionDAO.saveRptCpProduction( rptCpProduction );
    }
  }

  private List getCPPromotions()
  {
    AssociationRequestCollection associationRequestCollection = new AssociationRequestCollection();
    ChallengepointPromotionQueryConstraint queryConstraint = new ChallengepointPromotionQueryConstraint();
    queryConstraint.setPromotionStatusTypesIncluded( new PromotionStatusType[] { PromotionStatusType.lookup( PromotionStatusType.LIVE ), PromotionStatusType.lookup( PromotionStatusType.EXPIRED ) } );
    queryConstraint.setPromotionTypesIncluded( new PromotionType[] { PromotionType.lookup( PromotionType.CHALLENGE_POINT ) } );

    // Get all GQ promotions whose promotion calculation have been completed and results approved.
    queryConstraint.setHasIssueAwardsRun( Boolean.TRUE );

    return this.promotionService.getPromotionListWithAssociations( queryConstraint, associationRequestCollection );
  }
}
