
package com.biperf.core.dao.goalquest.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.goalquest.RptGoalPartnerDetailDAO;
import com.biperf.core.domain.goalquest.RptGoalPartnerDetail;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * RptGoalPartnerDetailDAOImpl.
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

public class RptGoalPartnerDetailDAOImpl implements RptGoalPartnerDetailDAO
{

  /**
   * Gets a List of all available rptGoalPartnerDetail records. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalPartnerDetailDAO#getAll()
   * @return List
   */
  public List getAll()
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.goalquest.AllRptGoalPartnerDetails" );
    return query.list();
  }

  /**
   * Update the RptGoalPartnerDetail record. 
   * Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalPartnerDetailDAO#saveRptGoalPartnerDetail(com.biperf.core.domain.goalquest.RptGoalPartnerDetail)
   * @param rptGoalPartnerDetail
   * @return RptGoalPartnerDetail
   */
  public RptGoalPartnerDetail saveRptGoalPartnerDetail( RptGoalPartnerDetail rptGoalPartnerDetail )
  {
    return (RptGoalPartnerDetail)HibernateUtil.saveOrUpdateOrDeepMerge( rptGoalPartnerDetail );
  }

}
