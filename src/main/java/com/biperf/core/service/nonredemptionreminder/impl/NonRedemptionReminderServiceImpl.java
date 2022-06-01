
package com.biperf.core.service.nonredemptionreminder.impl;

import java.util.Map;

import com.biperf.core.dao.nonredemptionreminder.NonRedemptionExtractDAO;
import com.biperf.core.service.nonredemptionreminder.NonRedemptionReminderService;

/**
 * NonRedemptionReminderServiceImpl.java
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
 * <td>Krishna Mattam</td>
 * <td>June 02, 2009</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 *
 */
public class NonRedemptionReminderServiceImpl implements NonRedemptionReminderService
{
  /** NonRedemptionExtractDAO */
  private NonRedemptionExtractDAO nonRedemptionExtractDAO;

  /**
   * Calls the stored procedure to get an extract for Non redemption PAX info
   * 
   * @param userId
   * @param promotionId
   * @param envName
   * @param displayFormData
   * @return Map result set
   */
  public Map nonRedemptionExtract( Long promotionId, String envName )
  {
    return nonRedemptionExtractDAO.nonRedemptionExtract( promotionId, envName );
  }

  public NonRedemptionExtractDAO getNonRedemptionExtractDAO()
  {
    return nonRedemptionExtractDAO;
  }

  public void setNonRedemptionExtractDAO( NonRedemptionExtractDAO nonRedemptionExtractDAO )
  {
    this.nonRedemptionExtractDAO = nonRedemptionExtractDAO;
  }

}
