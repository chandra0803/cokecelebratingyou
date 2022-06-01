
package com.biperf.core.dao.challengepoint.hibernate;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.biperf.core.dao.challengepoint.RptCpDetailDAO;
import com.biperf.core.domain.challengepoint.RptCpDetail;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

public class RptCpDetailDAOImpl implements RptCpDetailDAO
{

  /**
   * Gets a List of all available rptCpDetail records. Overridden from
   * 
   * @see com.biperf.core.dao.goalquest.RptCpDetailDAO#getAll()
   * @return List
   */
  public List getAll()
  {
    Session session = HibernateSessionManager.getSession();
    Query query = session.getNamedQuery( "com.biperf.core.domain.challengepoint.AllRptCpDetails" );
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
    Query query = session.getNamedQuery( "com.biperf.core.domain.challengepoint.RptCpPromotionIds" );
    return query.list();
  }

  public RptCpDetail saveRptCpDetail( RptCpDetail rptCpDetail )
  {
    return (RptCpDetail)HibernateUtil.saveOrUpdateOrDeepMerge( rptCpDetail );
  }

}
