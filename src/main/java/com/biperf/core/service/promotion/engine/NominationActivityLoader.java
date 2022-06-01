/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.service.promotion.engine;

import java.util.Set;

import com.biperf.core.dao.activity.ActivityDAO;
import com.biperf.core.domain.claim.ClaimGroup;
import com.biperf.core.domain.promotion.Promotion;

/**
 * RecognitionActivityLoader.
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
 * <td>zahler</td>
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NominationActivityLoader implements ActivityLoader
{
  private ActivityDAO activityDAO;

  /**
   * Overridden from
   * 
   * @see com.biperf.core.service.promotion.engine.ActivityLoader#loadActivities(com.biperf.core.service.promotion.engine.PayoutCalculationFacts,
   *      com.biperf.core.domain.promotion.Promotion)
   * @param payoutCalculationFacts
   * @param promotion
   * @return Set of RecognitionActivities
   */
  public Set loadActivities( PayoutCalculationFacts payoutCalculationFacts, Promotion promotion )
  {
    NominationFacts nominationFacts = (NominationFacts)payoutCalculationFacts;
    boolean isClaimGroup = false;
    if ( nominationFacts.getApprovable() instanceof ClaimGroup )
    {
      isClaimGroup = true;
    }
    return activityDAO.getUnpostedNominationActivities( nominationFacts.getParticipant().getId(), nominationFacts.getApprovable().getId(), promotion.getId(), isClaimGroup );
  }

  /**
   * @param activityDAO value for activityDAO property
   */
  public void setActivityDAO( ActivityDAO activityDAO )
  {
    this.activityDAO = activityDAO;
  }

}
