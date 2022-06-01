/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/security/hibernate/RoleDAOImpl.java,v $
 */

package com.biperf.core.dao.security.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseDAO;
import com.biperf.core.dao.security.RoleDAO;
import com.biperf.core.domain.user.Role;
import com.biperf.core.utils.HibernateSessionManager;
import com.biperf.core.utils.hibernate.HibernateUtil;

/**
 * RoleDAOImpl.
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
 * <td>Apr 6, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public class RoleDAOImpl extends BaseDAO implements RoleDAO
{

  /**
   * Get the role by the role Id.
   * 
   * @param id
   * @return Role
   */
  public Role getRoleById( Long id )
  {
    return (Role)getSession().get( Role.class, id );
  }

  /**
   * Get the role by code.
   * 
   * @param code
   * @return Role
   */
  public Role getRoleByCode( String code )
  {
    Session session = HibernateSessionManager.getSession();

    Query query = session.getNamedQuery( "com.biperf.core.domain.user.GetRoleByCode" );
    query.setString( "code", code );

    return (Role)query.uniqueResult();
  }

  /**
   * Returns the specified roles.
   *
   * @param queryConstraint  specifies which roles are returned and in what order.
   * @return the specified roles, as a <code>List</code> of {@link Role} objects.
   */
  public List<Role> getRoleList( RoleQueryConstraint queryConstraint )
  {
    List<Role> roleList = new ArrayList<Role>();

    // Work around: Filter the role list by user type here, instead of in the query constraint,
    // because I am unable to find a way to do this using a Hibernate Criteria object.
    String[] userTypesIncluded = queryConstraint.getUserTypesIncluded();
    for ( Iterator iter = HibernateUtil.getObjectList( queryConstraint ).iterator(); iter.hasNext(); )
    {
      Role role = (Role)iter.next();

      for ( int i = 0; i < userTypesIncluded.length; i++ )
      {
        if ( role.getUserTypeCodes().contains( userTypesIncluded[i] ) )
        {
          roleList.add( role );
          break;
        }
      }
    }

    return roleList;
  }

  /**
   * Save or update the Role.
   * 
   * @param role
   * @return role
   */
  public Role saveRole( Role role )
  {
    getSession().saveOrUpdate( role );
    return role;
  }

  /**
   * Gets a list of all roles. Overridden from
   * 
   * @see com.biperf.core.dao.security.RoleDAO#getAll()
   * @return Set
   */
  public Set<Role> getAll()
  {
    Criteria criteria = getSession().createCriteria( Role.class );
    return new LinkedHashSet<Role>( criteria.list() );
  }

  /**
   * Search for the role with the given search params.
   * 
   * @param active
   * @param helpText
   * @return List
   */
  public List searchRole( String helpText, Boolean active )
  {

    Criteria searchCriteria = getSession().createCriteria( Role.class );

    if ( null != helpText && !"".equals( helpText ) )
    {
      searchCriteria.add( Restrictions.ilike( "helpText", helpText, MatchMode.ANYWHERE ) );
    }

    if ( null != active )
    {
      searchCriteria.add( Restrictions.like( "active", active ) );
    }

    searchCriteria.addOrder( Order.asc( "name" ) );

    return searchCriteria.list();

  }

  public boolean isRoleBiwOnly( String roleCode )
  {
    Query query = getSession().createSQLQuery( "select fnc_biw_only_role(:roleCode) from dual" );
    query.setParameter( "roleCode", roleCode );
    return query.list().get( 0 ).equals( new BigDecimal( 1 ) );
  }

  /**
   * * 
   * @see com.biperf.core.dao.security.RoleDAO#getRoleByName()
   * @return String
   */
  public String getRoleByName( Long roleId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getRoleByName" );
    query.setLong( "roleId", roleId );

    return (String)query.uniqueResult();
  }

  public boolean isUserHasRole( Long loginUserId, String roleCode )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.isUserHasRole" );
    query.setParameter( "code", roleCode );
    query.setParameter( "loginUserId", loginUserId );
    return null == query.uniqueResult() ? false : true;
  }

  /**
   * Retrieve all the user id belongs to Bi Admin.
   * @param biAdminroleId
   * @return List
   */
  @SuppressWarnings( "unchecked" )
  public List<Long> getBiAdminUserIds( Long biAdminroleId )
  {
    Query query = getSession().getNamedQuery( "com.biperf.core.domain.user.getAllUserIdWithMappedToRollId" );
    query.setLong( "biAdminRoleId", biAdminroleId );
    return query.list();
  }

}
