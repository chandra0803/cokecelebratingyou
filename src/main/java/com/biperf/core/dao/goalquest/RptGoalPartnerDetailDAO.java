
package com.biperf.core.dao.goalquest;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.goalquest.RptGoalPartnerDetail;

/**
 * RptGoalPartnerDetailDAO.
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
 */
public interface RptGoalPartnerDetailDAO extends DAO
{
  public static final String BEAN_NAME = "rptGoalPartnerDetailDAO";

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

}
