/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/security/hibernate/AclDAOImpl.java,v $
 */

package com.biperf.core.dao.security.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.security.AclDAO;
import com.biperf.core.domain.user.Acl;

/**
 * AclDAOImpl.
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
 * <td>Mar 31, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class AclDAOImpl extends BaseDAO implements AclDAO
{

  /**
   * Get the Acl DAO for the id parameter. Overridden from
   * 
   * @see com.biperf.core.dao.security.AclDAO#getAclById(java.lang.Long)
   * @param id
   * @return Acl
   */
  public Acl getAclById( Long id )
  {
    Acl userAcl = (Acl)getSession().get( Acl.class, id );
    return userAcl;
  }

  /**
   * Save or update the Acl. Overridden from
   * 
   * @see com.biperf.core.dao.security.AclDAO#saveAcl(com.biperf.core.domain.user.Acl)
   * @param acl
   * @return Acl
   */
  public Acl saveAcl( Acl acl )
  {
    getSession().saveOrUpdate( acl );
    return acl;
  }

  /**
   * Search for an Acl using the provided criteria.
   * 
   * @param code
   * @param helpText
   * @param className
   * @return List
   */
  public List searchAcl( String code, String helpText, String className )
  {
    Criteria searchCriteria = getSession().createCriteria( Acl.class );

    if ( null != code && !"".equals( code ) )
    {
      searchCriteria.add( Restrictions.ilike( "code", code, MatchMode.ANYWHERE ) );
    }

    if ( null != helpText && !"".equals( helpText ) )
    {
      searchCriteria.add( Restrictions.ilike( "helpText", helpText, MatchMode.ANYWHERE ) );
    }

    if ( null != className && !"".equals( className ) )
    {
      searchCriteria.add( Restrictions.ilike( "className", className, MatchMode.ANYWHERE ) );
    }

    searchCriteria.addOrder( Order.asc( "code" ) );

    return searchCriteria.list();

  }

  /**
   * Get a list of All Acls from the database. Overridden from
   * 
   * @see com.biperf.core.dao.security.AclDAO#getAll()
   * @return List
   */
  public List getAll()
  {
    List allAcls = getSession().getNamedQuery( "com.biperf.core.domain.acl.AllAcls" ).list();
    return allAcls;
  }

}
