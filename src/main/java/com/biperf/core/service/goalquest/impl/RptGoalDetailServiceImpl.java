/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/goalquest/impl/RptGoalDetailServiceImpl.java,v $
 */

package com.biperf.core.service.goalquest.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.biperf.core.dao.goalquest.RptGoalDetailDAO;
import com.biperf.core.domain.enums.HierarchyRoleType;
import com.biperf.core.domain.goalquest.PendingGoalQuestAwardSummary;
import com.biperf.core.domain.goalquest.RptGoalDetail;
import com.biperf.core.service.goalquest.RptGoalDetailService;
import com.biperf.core.service.promotion.engine.GoalCalculationResult;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * RptGoalDetailServiceImpl.
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
 * <td>sedey</td>
 * <td>Feb 27, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */

public class RptGoalDetailServiceImpl implements RptGoalDetailService
{
  /** RptGoalDetailDAO * */
  private RptGoalDetailDAO rptGoalDetailDAO;

  /**
   * Set the RptGoalDetailDAO through IoC
   * 
   * @param rptGoalDetailDAO
   */
  public void setRptGoalDetailDAO( RptGoalDetailDAO rptGoalDetailDAO )
  {
    this.rptGoalDetailDAO = rptGoalDetailDAO;
  }

  /**
   * Gets the list of rptGoalDetails Overridden from
   * 
   * @see com.biperf.core.service.goalquest.RptGoalDetailService.getAll#()
   * @return List
   */
  public List getAll()
  {
    return this.rptGoalDetailDAO.getAll();
  }

  /** 
   * Gets the list of rptGoalDetail Idss Overridden from
   * 
   * @see com.biperf.core.service.goalquest.RptGoalDetailService.getAllIds#()
   * @return List
   */
  public List getAllIds()
  {
    return this.rptGoalDetailDAO.getAllIds();
  }

  /**
   * Gets a List of all promotion ids. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalDetailService#getPromotionIds()
   * @return List
   */
  public List getPromotionIds()
  {
    return this.rptGoalDetailDAO.getPromotionIds();
  }

  /**
   * Saves a rptGoalDetail object Overridden from
   * 
   * @see com.biperf.core.service.goalquest.RptGoalDetailService.saveRptGoalDetail#( RptGoalDetail rptGoalDetail )
   * @param rptGoalDetail
   * @return List
   */
  public RptGoalDetail saveRptGoalDetail( RptGoalDetail rptGoalDetail )
  {
    return this.rptGoalDetailDAO.saveRptGoalDetail( rptGoalDetail );
  }

  /**
   * Updates a rptGoalDetail object Overridden from
   * 
   * @see com.biperf.core.service.goalquest.RptGoalDetailService.updateGoalAchievementDetails#()
   */
  public void updateGoalAchievementDetails( Map calculationResultsByPromotionId )
  {
    // List rptGoalDetailIDList = getAllIds();
    int blockSize = 1000;

    Long countObj = rptGoalDetailDAO.getAllCount();

    long totalCount = countObj.longValue();
    int index = 0;
    Long lastId = null;
    while ( countObj != null && countObj.longValue() > 0 && index < totalCount )
    {
      rptGoalDetailDAO.flush();
      List rptGoalDetailList = rptGoalDetailDAO.getRowsWithinRange( lastId, blockSize );
      for ( Iterator iter = rptGoalDetailList.iterator(); iter.hasNext(); )
      {
        index++;
        // Long rptGoalDetailId = (Long)iter.next();
        // RptGoalDetail rptGoalDetail = rptGoalDetailDAO.getRptGoalDetailById(rptGoalDetailId);
        RptGoalDetail rptGoalDetail = (RptGoalDetail)iter.next();
        lastId = rptGoalDetail.getId();
        PendingGoalQuestAwardSummary pendingGoalQuestAwardSummary = (PendingGoalQuestAwardSummary)calculationResultsByPromotionId.get( rptGoalDetail.getGoalQuestPromotion().getId() );
        GoalCalculationResult goalCalculationResult = getCalculationResultsForParticipant( pendingGoalQuestAwardSummary, rptGoalDetail.getParticipant().getId() );
        if ( goalCalculationResult != null )
        {
          rptGoalDetail.setManager( new Boolean( goalCalculationResult.isManager() ) );
          if ( goalCalculationResult.isManager() || rptGoalDetail.getPaxGoal() != null )
          {
            rptGoalDetail.setCurrentValue( goalCalculationResult.getTotalPerformance() );
            rptGoalDetail.setPercentOfGoal( goalCalculationResult.getPercentageAchieved() );
            rptGoalDetail.setAmountToAchieve( goalCalculationResult.getAmountToAchieve() );
            rptGoalDetail.setBaseQuantity( goalCalculationResult.getBaseObjective() );
          }
          if ( rptGoalDetail.getGoalQuestPromotion().isIssueAwardsRun() )
          {
            rptGoalDetail.setAchieved( new Boolean( goalCalculationResult.isAchieved() ) );
            rptGoalDetail.setCalculatedPayout( goalCalculationResult.getCalculatedPayout() );
          }
        }
        else
        { // Some managers will not have a GoalCalculationResult if they did not achieve
          List ownedNodes = rptGoalDetail.getParticipant().getNodes( HierarchyRoleType.lookup( HierarchyRoleType.OWNER ) );
          if ( ownedNodes.size() > 0 )
          {
            rptGoalDetail.setManager( Boolean.TRUE );
          }
          else
          {
            rptGoalDetail.setManager( Boolean.FALSE );
          }
        }
        saveRptGoalDetail( rptGoalDetail );

      }
      // because another oracle procedure is dependent on this data being there a flush needs
      // to happen before that oracle procedure can see the updated data.
      HibernateSessionManager.getSession().flush();
    }
  }

  private GoalCalculationResult getCalculationResultsForParticipant( PendingGoalQuestAwardSummary pendingGoalQuestAwardSummary, Long userId )
  {
    if ( pendingGoalQuestAwardSummary != null )
    {
      if ( pendingGoalQuestAwardSummary.getParticipantGQResults() != null )
      {
        for ( Iterator iter = pendingGoalQuestAwardSummary.getParticipantGQResults().iterator(); iter.hasNext(); )
        {
          GoalCalculationResult goalCalculationResult = (GoalCalculationResult)iter.next();
          if ( goalCalculationResult.getReciever() != null && goalCalculationResult.getReciever().getId().equals( userId ) )
          {
            return goalCalculationResult;
          }
        }
      }
      if ( pendingGoalQuestAwardSummary.getManagerOverrideResults() != null )
      {
        for ( Iterator iter = pendingGoalQuestAwardSummary.getManagerOverrideResults().iterator(); iter.hasNext(); )
        {
          GoalCalculationResult goalCalculationResult = (GoalCalculationResult)iter.next();
          if ( goalCalculationResult.getReciever() != null && goalCalculationResult.getReciever().getId().equals( userId ) )
          {
            return goalCalculationResult;
          }
        }
      }

    }
    return null;
  }
}
