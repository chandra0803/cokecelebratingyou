/*
 * (c) 2006 BI, Inc.  All rights reserved.
 * $Source: /usr/local/ndscvsroot/products/penta-g/src/java/com/biperf/core/dao/security/hibernate/RoleQueryConstraint.java,v $
 */

package com.biperf.core.dao.security.hibernate;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.biperf.core.dao.BaseQueryConstraint;
import com.biperf.core.domain.user.Role;
import com.biperf.core.utils.HibernateSessionManager;

public class RoleQueryConstraint extends BaseQueryConstraint
{
  // ---------------------------------------------------------------------------
  // Fields
  // ---------------------------------------------------------------------------

  /**
   * If null, the result set contains both active and inactive roles; if not
   * null and true, the result set contains only active roles; if not null
   * and false, the result set contains only inactive roles.
   */
  private Boolean active;

  /**
   * If true, the result set should be ordered by role name; if false, the result
   * set is not ordered by role name.
   */
  private boolean isOrderedByName = false;

  /**
   * Include only roles that can be assumed by these types of users.
   */
  private String[] userTypesIncluded; // user type code

  // ---------------------------------------------------------------------------
  // Query Methods
  // ---------------------------------------------------------------------------

  public Criteria buildCriteria()
  {
    Criteria criteria = HibernateSessionManager.getSession().createCriteria( getResultClass(), "role" );

    if ( active != null )
    {
      criteria.add( Restrictions.eq( "active", active ) );
    }

    if ( isOrderedByName )
    {
      criteria.addOrder( Order.asc( "name" ) );
    }

    return criteria;
  }

  // ---------------------------------------------------------------------------
  // Getter and Setter Methods
  // ---------------------------------------------------------------------------

  /**
   * Return the class of the objects selected by this query constraint.
   *
   * @return the class of the objects selected by this query constraint.
   */
  public Class getResultClass()
  {
    return Role.class;
  }

  public Boolean getActive()
  {
    return active;
  }

  public void setActive( Boolean active )
  {
    this.active = active;
  }

  public boolean isOrderedByName()
  {
    return isOrderedByName;
  }

  public void setOrderedByName( boolean orderedByName )
  {
    isOrderedByName = orderedByName;
  }

  public String[] getUserTypesIncluded()
  {
    return userTypesIncluded;
  }

  public void setUserTypesIncluded( String[] userTypesIncluded )
  {
    this.userTypesIncluded = userTypesIncluded;
  }
}
