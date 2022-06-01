/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.Criteria;

import com.biperf.core.utils.HibernateSessionManager;

/**
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
 * <td>wadzinsk</td>
 * <td>Oct 20, 2005</td>
 * <td>1.0</td>
 * <td>created</td>
 * </tr>
 * </table>
 * 
 */
public abstract class BaseQueryConstraint implements QueryConstraint, Serializable
{
  /**
   * Returns a criteria query that selects all instances of the persistent class
   * returned by the method getResultClass.
   *
   * @return a criteria query that selects all instances of the persistent class
   *         returned by the method getResultClass.
   */
  public Criteria buildCriteria()
  {
    return HibernateSessionManager.getSession().createCriteria( getResultClass() );
  }

  /**
   * Avoids duplicate alias errors by testing for existing alias before creating one.
   */
  protected Criteria createAliasIfNotAlreadyCreated( Criteria criteria, String associationPath, String alias )
  {
    if ( !aliasSet.contains( alias ) )
    {
      aliasSet.add( alias );
      return criteria.createAlias( associationPath, alias );
    }
    // else
    return criteria;
  }

  private Set aliasSet = new LinkedHashSet();
}
