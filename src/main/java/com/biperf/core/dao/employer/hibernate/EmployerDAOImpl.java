/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/employer/hibernate/EmployerDAOImpl.java,v $
 */

package com.biperf.core.dao.employer.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.employer.EmployerDAO;
import com.biperf.core.domain.employer.Employer;
import com.biperf.core.utils.HibernateSessionManager;

/**
 * EmployerDAOImpl.
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
 * <td>crosenquest</td>
 * <td>Apr 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class EmployerDAOImpl extends BaseDAO implements EmployerDAO
{

  /**
   * Saves or updates the employer record in the database. Overridden from
   * 
   * @see com.biperf.core.dao.employer.EmployerDAO#saveEmployer(com.biperf.core.domain.employer.Employer)
   * @param employer
   * @return Employer
   */
  public Employer saveEmployer( Employer employer )
  {
    getSession().saveOrUpdate( employer );

    return employer;
  }

  /**
   * Search the database for the employer with the given criteria param. Overridden from
   * 
   * @see com.biperf.core.dao.employer.EmployerDAO#searchEmployer(java.lang.String)
   * @param name
   * @return List
   */
  public List searchEmployer( String name )
  {
    Criteria searchCriteria = getSession().createCriteria( Employer.class );

    if ( null != name && !"".equals( name ) )
    {
      searchCriteria.add( Restrictions.ilike( "name", name, MatchMode.ANYWHERE ) );
    }

    searchCriteria.addOrder( Order.asc( "name" ) );

    return searchCriteria.list();
  }

  /**
   * Overridden from
   * 
   * @see com.biperf.core.dao.employer.EmployerDAO#getEmployerById(java.lang.Long)
   * @param id
   * @return Employer
   */
  public Employer getEmployerById( Long id )
  {

    Employer employer = (Employer)getSession().get( Employer.class, id );

    return employer;
  }

  /**
   * Get a set of all employers in the database. Overridden from
   * 
   * @see com.biperf.core.dao.employer.EmployerDAO#getAll()
   * @return Set
   */
  public List getAll()
  {
    Session session = HibernateSessionManager.getSession();
    return session.getNamedQuery( "com.biperf.core.domain.employer.AllEmployerList" ).list();
  }

  @Override
  public List getActiveEmployers()
  {
    return getSession().getNamedQuery( "com.biperf.core.domain.employer.ActiveEmployerList" ).list();
  }
}
