/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/goalquest/impl/RptGoalPartnerDetailServiceImpl.java,v $
 */

package com.biperf.core.service.goalquest.impl;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.biperf.core.dao.goalquest.RptGoalPartnerDetailDAO;
import com.biperf.core.domain.goalquest.PendingGoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.RptGoalPartnerDetail;
import com.biperf.core.service.goalquest.RptGoalPartnerDetailService;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * RptGoalPartnerDetailServiceImpl.
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
 * <td>gadapa</td>
 * <td>Apr 10, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class RptGoalPartnerDetailServiceImpl implements RptGoalPartnerDetailService
{
  /** RptGoalPartnerDetailDAO * */
  private RptGoalPartnerDetailDAO rptGoalPartnerDetailDAO;

  /**
   * Set the RptGoalPartnerDetailDAO through IoC
   * 
   * @param rptGoalPartnerDetailDAO
   */
  public void setRptGoalPartnerDetailDAO( RptGoalPartnerDetailDAO rptGoalPartnerDetailDAO )
  {
    this.rptGoalPartnerDetailDAO = rptGoalPartnerDetailDAO;
  }

  /**
   * Gets the list of rptGoalPartnerDetails Overridden from
   * 
   * @see com.biperf.core.service.goalquest.RptGoalPartnerDetailService.getAll#()
   * @return List
   */
  public List getAll()
  {
    return this.rptGoalPartnerDetailDAO.getAll();
  }

  /**
   * Saves a rptGoalPartnerDetail object Overridden from
   * 
   * @see com.biperf.core.service.goalquest.RptGoalPartnerDetailService.saveRptGoalPartnerDetail#( RptGoalPartnerDetail rptGoalPartnerDetail )
   * @param rptGoalPartnerDetail
   * @return rptGoalPartnerDetail
   */
  public RptGoalPartnerDetail saveRptGoalPartnerDetail( RptGoalPartnerDetail rptGoalPartnerDetail )
  {
    return this.rptGoalPartnerDetailDAO.saveRptGoalPartnerDetail( rptGoalPartnerDetail );
  }

  /**
   * Updates a rptGoalPartnerDetail object Overridden from
   * 
   * @see com.biperf.core.service.goalquest.RptGoalPartnerDetailService.updateGoalAchievementDetails#()
   */
  public void updateGoalAchievementDetails( Map calculationResultsByPromotionId )
  {
    List rptGoalPartnerDetailList = getAll();

    if ( !rptGoalPartnerDetailList.isEmpty() )
    {
      for ( Iterator iter = rptGoalPartnerDetailList.iterator(); iter.hasNext(); )
      {
        RptGoalPartnerDetail rptGoalPartnerDetail = (RptGoalPartnerDetail)iter.next();
        PendingGoalQuestAwardSummary pendingGoalQuestAwardSummary = (PendingGoalQuestAwardSummary)calculationResultsByPromotionId.get( rptGoalPartnerDetail.getGoalQuestPromotion().getId() );
        GoalCalculationResult goalCalculationResult = getCalculationResultsForPartner( pendingGoalQuestAwardSummary,
                                                                                       rptGoalPartnerDetail.getPartner().getId(),
                                                                                       rptGoalPartnerDetail.getParticipant().getId() );
        if ( goalCalculationResult != null )
        {
          rptGoalPartnerDetail.setCurrentValue( goalCalculationResult.getTotalPerformance() );
          rptGoalPartnerDetail.setPercentOfGoal( goalCalculationResult.getPercentageAchieved() );
          rptGoalPartnerDetail.setAmountToAchieve( goalCalculationResult.getAmountToAchieve() );
          rptGoalPartnerDetail.setBaseQuantity( goalCalculationResult.getBaseObjective() );
          if ( rptGoalPartnerDetail.getGoalQuestPromotion().isIssueAwardsRun() )
          {
            Boolean achieved = goalCalculationResult.isAchieved() ? Boolean.TRUE : goalCalculationResult.isPartnersParticipantAchieved() ? Boolean.TRUE : Boolean.FALSE;
            rptGoalPartnerDetail.setAchieved( achieved );
            BigDecimal payout = new BigDecimal( "0" );
            if ( null != goalCalculationResult.getCalculatedPayout() )
            {
              payout = goalCalculationResult.getCalculatedPayout();
            }
            rptGoalPartnerDetail.setCalculatedPayout( payout );
          }
          saveRptGoalPartnerDetail( rptGoalPartnerDetail );
        }
      }
      // because another oracle procedure is dependent on this data being there a flush needs
      // to happen before that oracle procedure can see the updated data.
      HibernateSessionManager.getSession().flush();
    }
  }

  private GoalCalculationResult getCalculationResultsForPartner( PendingGoalQuestAwardSummary pendingGoalQuestAwardSummary, Long partnerId, Long paxId )
  {
    if ( pendingGoalQuestAwardSummary != null )
    {
      if ( pendingGoalQuestAwardSummary.getPartnerGQResults() != null )
      {
        for ( Iterator iter = pendingGoalQuestAwardSummary.getPartnerGQResults().iterator(); iter.hasNext(); )
        {
          GoalCalculationResult goalCalculationResult = (GoalCalculationResult)iter.next();
          if ( goalCalculationResult.getReciever() != null && goalCalculationResult.getReciever().getId().equals( partnerId ) && goalCalculationResult.getPartnerToParticipant() != null
              && goalCalculationResult.getPartnerToParticipant().getId().equals( paxId ) )
          {
            return goalCalculationResult;
          }
        }
      }
    }
    return null;
  }
}
