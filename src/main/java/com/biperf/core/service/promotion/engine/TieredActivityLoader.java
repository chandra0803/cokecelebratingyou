/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import java.util.Set;

import com.biperf.core.domain.promotion.Promotion;

/**
 * TieredActivityLoader.
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
 * <td>Aug 11, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class TieredActivityLoader extends SalesActivityLoader
{

  /**
   * Load Sales Activities including carryover Activities. Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.SalesActivityLoader#loadActivities(com.biperf.core.service.promotion.engine.PayoutCalculationFacts,
   *      com.biperf.core.domain.promotion.Promotion)
   * @param payoutCalculationFacts
   * @param promotion
   * @return List
   */
  public Set loadActivities( PayoutCalculationFacts payoutCalculationFacts, Promotion promotion )
  {
    Set activities = super.loadActivities( payoutCalculationFacts, promotion );

    SalesFacts salesFacts = (SalesFacts)payoutCalculationFacts;

    Set carryoverActivities = getActivityDAO().getUnpostedCarryoverActivities( salesFacts.getParticipant().getId(), promotion.getId() );

    activities.addAll( carryoverActivities );

    return activities;
  }

}
