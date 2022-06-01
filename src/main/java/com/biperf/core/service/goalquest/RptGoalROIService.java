
package com.biperf.core.service.goalquest;

import java.util.List;

import com.biperf.core.domain.goalquest.RptGoalROI;
import com.biperf.core.service.SAO;

public interface RptGoalROIService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "rptGoalROIService";

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
   * Updates rptGoalROI with return on investment counts.
   */
  public void updateGoalROICounts();
}
