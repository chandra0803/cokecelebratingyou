
package com.biperf.core.dao.goalquest;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.goalquest.RptGoalROI;

public interface RptGoalROIDAO extends DAO
{
  public static final String BEAN_NAME = "rptGoalROIDAO";

  /**
   * Selects records from the RPT_GOAL_ROI by promotion id.
   *
   *@param id
   *@return List of RptGoalROI objects
   */
  public List getRptGoalROIByPromotionId( Long id );

  /**
   * Save the rptGoalROI.
   * 
   * @param rptGoalROI
   * @return rptGoalROI
   */
  public RptGoalROI saveRptGoalROI( RptGoalROI rptGoalROI );

  /**
   * @return Integer number of all active participants (used in primary audience)
   */
  public Integer getNbrOfAllActivePax();

  /**
   * @param promotionId
   * @return List - get goal achieved counts for a certain promotion.
   */
  public List getAchievedCounts( Long promotionId );

  /**
   * @param promotionId
   * @return List - get goal not achieved over baseline counts for a certain promotion.
   */
  public List getNotAchievedOverBaselineCounts( Long promotionId );

  /**
   * @param promotionId
   * @return List - get subtotal sum of achieved counts and
   *                goal not achieved over baseline counts for a certain promotion.
   */
  public List getSubtotalCounts( Long promotionId );

  /**
   * @param promotionId
   * @return List - get goal not achieved under baseline counts for a certain promotion.
   */
  public List getNotAchievedUnderBaselineCounts( Long promotionId );

  /**
   * @param promotionId
   * @return List - get did not select goal counts for a certain promotion.
   */
  public List getDidNotSelectGoalCounts( Long promotionId );

}
