
package com.biperf.core.dao.nonredemptionreminder;

import java.util.Map;

import com.biperf.core.dao.DAO;

/**
 * NonRedemptionExtractDAO
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
 */
public interface NonRedemptionExtractDAO extends DAO
{

  /**
   * Bean name
   */
  public static final String BEAN_NAME = "nonRedemptionExtractDAO";

  /**
   * Calls the stored procedure to get an extract for Non redemption PAX info
   * 
   * @param promotionId
   * @param envName
   * @return Map result set
   */
  public Map nonRedemptionExtract( Long promotionId, String envName );

}
