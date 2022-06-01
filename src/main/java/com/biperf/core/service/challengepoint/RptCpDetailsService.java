
package com.biperf.core.service.challengepoint;

import java.util.List;
import java.util.Map;

import com.biperf.core.domain.challengepoint.RptCpDetail;
import com.biperf.core.service.SAO;

/**
 * RptCpDetailsService.
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
 * <td>reddy</td>
 * <td>Aug 29, 2008</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public interface RptCpDetailsService extends SAO
{
  /** Name of service bean in the beanFactory */
  public static final String BEAN_NAME = "rptCpDetailsService";

  /**
   * Selects all the records from the rpt_cp_selection_detail.
   *
   *@return List of rptCpDetail objects
   */
  public List getAll();

  /**
   * Gets a List of all promotion ids. Overridden from
   * 
   * @return List
   */
  public List getPromotionIds();

  /**
   * Save the rptCpDetail.
   * 
   * @param rptCpDetail
   * @return rptCpDetail
   */
  public RptCpDetail saveRptCpDetail( RptCpDetail rptCpDetail );

  /**
   * Updates rptCpDetail with achievement info.
   * 
   */
  public void updateCpAchievementDetails( Map calculationResultsByPromotionId );
}
