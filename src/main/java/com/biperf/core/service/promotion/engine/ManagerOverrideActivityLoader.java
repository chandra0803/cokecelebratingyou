/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/beacon/src/java/com/biperf/core/service/promotion/engine/ManagerOverrideActivityLoader.java,v $
 */

package com.biperf.core.service.promotion.engine;

import java.util.Set;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.domain.enums.PromotionPayoutType;
import com.biperf.core.domain.promotion.ProductClaimPromotion;
import com.biperf.core.domain.promotion.Promotion;

/*
 * ManagerOverrideActivityLoader <p> <b>Change History:</b><br> <table border="1"> <tr> <th>Author</th>
 * <th>Date</th> <th>Version</th> <th>Comments</th> </tr> <tr> <td>OPI Admin</td> <td>Aug
 * 23, 2005</td> <td>1.0</td> <td>created</td> </tr> </table> </p>
 * 
 *
 */

public class ManagerOverrideActivityLoader implements ActivityLoader
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
    ManagerOverrideFacts managerOverrideFacts = (ManagerOverrideFacts)payoutCalculationFacts;
    PromotionPayoutType payoutType = null;
    if ( promotion instanceof ProductClaimPromotion )
    {
      ProductClaimPromotion pcPromo = (ProductClaimPromotion)promotion;
      payoutType = pcPromo.getPayoutType();
    }
    return activityDAO.getUnpostedManagerOverrideActivitiesByParticipant( promotion.getId(),
                                                                          managerOverrideFacts.getManager().getId(),
                                                                          managerOverrideFacts.getStartDate(),
                                                                          managerOverrideFacts.getEndDate(),
                                                                          payoutType );
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
