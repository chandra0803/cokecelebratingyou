
package com.biperf.core.dao.challengepoint;

import java.util.List;

import com.biperf.core.dao.DAO;
import com.biperf.core.domain.challengepoint.RptCpDetail;

public interface RptCpDetailDAO extends DAO
{
  public static final String BEAN_NAME = "rptCpDetailDAO";

  /**
   * Selects all the records from the rpt_cp_selection_detail.
   *
   *@return List of rptGoalDetail objects
   */
  public List getAll();

  /**
   * Gets a List of all promotion ids. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptCpDetailDAO#getPromotionIds()
   * @return List
   */
  public List getPromotionIds();

  /**
   * Save the RptCpDetail.
   * 
   * @param rptCpDetail
   * @return RptCpDetail
   */
  public RptCpDetail saveRptCpDetail( RptCpDetail rptCpDetail );

}
