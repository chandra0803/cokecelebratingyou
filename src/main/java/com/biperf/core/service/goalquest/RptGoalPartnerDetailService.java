/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/goalquest/RptGoalPartnerDetailService.java,v $
 */

package com.biperf.core.service.goalquest;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.goalquest.RptGoalPartnerDetail;
import com.biperf.core.service.SAO;

/**
 * RptGoalPartnerDetailService.
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

public interface RptGoalPartnerDetailService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "rptGoalPartnerDetailService";

  /**
   * Selects all the records from the rpt_goal_partner.
   *
   *@return List of rptGoalPartnerDetail objects
   */
  public List getAll();

  /**
   * Save the rptGoalPartnerDetail.
   * 
   * @param rptGoalPartnerDetail
   * @return rptGoalPartnerDetail
   */
  public RptGoalPartnerDetail saveRptGoalPartnerDetail( RptGoalPartnerDetail rptGoalPartnerDetail );

  /**
   * Updates rptGoalDetail with achievement info.
   * 
   */
  public void updateGoalAchievementDetails( Map calculationResultsByPromotionId );
}
