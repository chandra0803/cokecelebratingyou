/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/goalquest/RptGoalDetailService.java,v $
 */

package com.biperf.core.service.goalquest;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.goalquest.RptGoalDetail;
import com.biperf.core.service.SAO;

/**
 * RptGoalDetailService.
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

public interface RptGoalDetailService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "rptGoalDetailService";

  /**
   * Selects all the records from the rpt_goal_selection_detail.
   *
   *@return List of rptGoalDetail objects
   */
  public List getAll();

  /**
   * Gets a List of all promotion ids. Overridden from
   * 
   * @return List
   */
  public List getPromotionIds();

  /**
   * Save the rptGoalDetail.
   * 
   * @param rptGoalDetail
   * @return rptGoalDetail
   */
  public RptGoalDetail saveRptGoalDetail( RptGoalDetail rptGoalDetail );

  /**
   * Updates rptGoalDetail with achievement info.
   * 
   */
  public void updateGoalAchievementDetails( Map calculationResultsByPromotionId );
}
