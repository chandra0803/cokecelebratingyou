
package com.biperf.core.dao.goalquest.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.goalquest.RptGoalDetailDAO;
import com.biperf.core.domain.goalquest.RptGoalDetail;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * RptGoalDetailDAOImpl.
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

public class RptGoalDetailDAOImpl implements RptGoalDetailDAO
{

  /**
   * Gets a List of all available rptGoalDetail records. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalDetailDAO#getAll()
   * @return List
   */
  public List getAll()
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.goalquest.AllRptGoalDetails" );
    return query.list();
  }

  /** 
   * Flush the session
   * 
   */
  public void flush()
  {
    Session session = HibernateSessionManager.getSession();
    session.flush();
  }

  /** 
   * Gets a List of all available rptGoalDetail records. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalDetailDAO#getAll()
   * @return List
   */
  public List getRowsWithinRange( Long lastId, int numOfRows )
  {
    Session session = HibernateSessionManager.getSession();
    Query query = null;
    if ( lastId == null || lastId.longValue() <= 0 )
    {
      query = session.getNamedQuery( "com.biperf.core.domain.goalquest.AllRptGoalDetails" );
      query.setMaxResults( numOfRows );
    }
    else
    {
      query = session.getNamedQuery( "com.biperf.core.domain.goalquest.AllRptGoalDetailsRange" );
      query.setLong( "lastId", lastId.longValue() );
      query.setMaxResults( numOfRows );
    }
    return query.list();
  }

  /** 
   * Gets a count of List of all available rptGoalDetail Ids. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalDetailDAO#getAllIds()
   * @return Long
   */
  public Long getAllCount()
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.goalquest.RptGoalDetailCount" );
    return (Long)query.uniqueResult();
  }

  /** 
   * Gets a List of all available rptGoalDetail Ids. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalDetailDAO#getAllIds()
   * @return List
   */
  public List getAllIds()
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.goalquest.AllRptGoalDetailIds" );
    return query.list();
  }

  /**
   * Gets a List of all promotion ids. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalDetailDAO#getPromotionIds()
   * @return List
   */
  public List getPromotionIds()
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.goalquest.RptGoalDetailsPromotionIds" );
    return query.list();
  }

  /**
   * Update the RptGoalDetail record. 
   * Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptGoalDetailDAO#saveRptGoalDetail(com.biperf.core.domain.goalquest.RptGoalDetail)
   * @param rptGoalDetail
   * @return RptGoalDetail
   */
  public RptGoalDetail saveRptGoalDetail( RptGoalDetail rptGoalDetail )
  {
    return (RptGoalDetail)HibernateUtil.saveOrUpdateOrDeepMerge( rptGoalDetail );
  }

}
