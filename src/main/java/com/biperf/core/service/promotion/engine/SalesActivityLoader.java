/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import java.util.Set;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.domain.promotion.Promotion;

/**
 * SalesActivityLoader.
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
 * <td>wadzinsk</td>
 * <td>Aug 8, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class SalesActivityLoader implements ActivityLoader
{
  private ActivityDAO activityDAO;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.ActivityLoader#loadActivities(com.biperf.core.service.promotion.engine.PayoutCalculationFacts,
   *      com.biperf.core.domain.promotion.Promotion)
   * @param payoutCalculationFacts
   * @param promotion
   * @return List
   */
  public Set loadActivities( PayoutCalculationFacts payoutCalculationFacts, Promotion promotion )
  {
    SalesFacts salesFacts = (SalesFacts)payoutCalculationFacts;
    return activityDAO.getUnpostedSalesActivities( salesFacts.getParticipant().getId(), salesFacts.getClaim().getId(), promotion.getId() );
  }

  /**
   * @return value of activityDAO property
   */
  public ActivityDAO getActivityDAO()
  {
    return activityDAO;
  }

  /**
   * @param activityDAO value for activityDAO property
   */
  public void setActivityDAO( ActivityDAO activityDAO )
  {
    this.activityDAO = activityDAO;
  }

}
