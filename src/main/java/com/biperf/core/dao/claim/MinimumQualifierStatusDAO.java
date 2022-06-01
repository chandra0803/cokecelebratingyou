/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao.claim;

import java.util.Map;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.claim.MinimumQualifierStatus;

/**
 * .
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
 * <td>Mar 2, 2006</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface MinimumQualifierStatusDAO extends DAO
{

  /**
   * Bean name
   */
  public static final String BEAN_NAME = "minimumQualifierStatusDAO";

  public MinimumQualifierStatus save( MinimumQualifierStatus minimumQualifierStatus );

  public MinimumQualifierStatus getMinimumQualifierStatus( Long minimumQualifierStatusId );

  public MinimumQualifierStatus getMinimumQualifierStatusById( Long minimumQualifierStatusId );

  /**
   * Get the MinimumQualifierStatus objects for a given submitter and promotion keyed by
   * PromotionPayoutGroup.
   */
  public Map getMinQualifierStatusByPromoPayoutGroup( Long submitterId, Long promotionId );

}
