/*
 * (c) 2005 BI, Inc.  All rights reserved.
 * $Source$
 */

package com.biperf.core.dao;

import org.hibernate.Criteria;

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
public interface QueryConstraint
{
  /**
   * Builds a Hibernate {@link Criteria} object that represents the SQL query specified by this
   * <code>QueryConstraint</code> object.
   * 
   * @return a Hibernate {@link Criteria} object that represents the SQL query specified by this
   *         <code>QueryConstraint</code> object.
   */
  Criteria buildCriteria();

  /**
   * Returns the class of the objects returned by the query specified by this query constraint.
   * 
   * @return the class of the objects returned by the query specified by this query constraint.
   */
  Class getResultClass();
}
