/**
 * 
 */

package com.biperf.core.service.promotion;

import java.util.Date;
import java.util.Set;

import com.biperf.core.exception.ServiceErrorException;
import com.biperf.core.service.SAO;

/**
 * PayoutProcessingService.
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
 * <td>asondgeroth</td>
 * <td>Jul 4, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public interface PayoutProcessingService extends SAO
{
  public static final String BEAN_NAME = "payoutProcessingService";

  /**
   * Process the ManagerOverrideActivities and create a payout for the managers involved in this
   * promotion.
   * 
   * @param promotionId
   * @param startDate
   * @param endDate
   * @return Set of PromotionCalculationResults objects
   */
  public Set processManagerOverride( Long promotionId, Date startDate, Date endDate ) throws ServiceErrorException;
}
