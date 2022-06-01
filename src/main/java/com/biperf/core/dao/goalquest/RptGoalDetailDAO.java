
package com.biperf.core.dao.goalquest;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.goalquest.RptGoalDetail;

/**
 * RptGoalDetailDAO.
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
 * <td>sedey</td>
 * <td>Feb 27, 2007</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface RptGoalDetailDAO extends DAO
{
  public static final String BEAN_NAME = "rptGoalDetailDAO";

  /**
   * Selects all the records from the rpt_goal_selection_detail.
   *
   *@return List of rptGoalDetail objects
   */
  public List getAll();

  /** 
   * Selects all the record ids from the rpt_goal_selection_detail
   * @return List of rptGoalDetail Ids
   */
  public List getAllIds();

  /**
   * Gets a List of all promotion ids. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalDetailDAO#getPromotionIds()
   * @return List
   */
  public List getPromotionIds();

  /**
   * Save the rptGoalDetail.
   * 
   * @param rptGoalDetail
   * @return rptGoalDetail
   */
  public RptGoalDetail saveRptGoalDetail( RptGoalDetail rptGoalDetail );

  /**
   * Gets a List of all available rptGoalDetail records. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalDetailDAO#getAll()
   * @return List
   */
  public List getRowsWithinRange( Long lastId, int numOfRows );

  /**
   * Gets a count of List of all available rptGoalDetail Ids. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalDetailDAO#getAllIds()
   * @return Long
   */
  public Long getAllCount();

  /**
   * Flush the session
   * 
   */
  public void flush();

}
